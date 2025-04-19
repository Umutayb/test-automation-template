package models.sql.database;

import context.ContextStore;
import io.github.soldelv.sql.builder.QueryBuilder;
import lombok.Getter;
import utils.DBUtilities;
import utils.Printer;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static utils.StringUtilities.Color.BLUE;
import static utils.StringUtilities.firstLetterCapped;
import static utils.StringUtilities.markup;
import static utils.reflection.ReflectionUtilities.getMethod;

public class Database extends QueryBuilder {

    Printer log = new Printer(Database.class);

    DBUtilities dbUtilities = new DBUtilities();

    @Getter
    public enum Table {
        TRANSACTIONS("[master].[dbo].[Transaction]");

        private final String from;

        Table(String from){
            this.from = from;
        }
    }

    public <T> List<T> getData(Class<T> dataType, QueryAttributes queryAttributes) {
        String sqlQuery = sqlQueryBuilder(queryAttributes);
        return getData(dataType, sqlQuery);
    }

    public <T> List<T> getData(Class<T> dataType, String sqlQuery) {
        List<T> records = new ArrayList<>();
        for (Map<String, Object> userMap : executeQuery(sqlQuery)) {
            log.info("Mapping object acquired: " + userMap);
            try {
                T record = dataType.getDeclaredConstructor().newInstance(); // Create a new instance for each iteration
                for (String key : userMap.keySet()) {
                    try {
                        if (userMap.get(key) != null) {
                            getMethod("set" + firstLetterCapped(key), record.getClass())
                                    .invoke(record, userMap.get(key).toString());
                        }
                    } catch (NoSuchMethodException ignored) {
                    } catch (InvocationTargetException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
                records.add(record);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException("Failed to create an instance of " + dataType.getName(), e);
            }
        }
        return records;
    }

    public List<Map<String, Object>> executeQuery(String sqlQuery) {
        String catalog = ContextStore.get("sql-database-catalog");

        // Retrieve the SQL Server credentials from ContextStore
        String user = ContextStore.get("sql-database-user");
        String password = ContextStore.get("sql-database-password");
        String baseUrl = ContextStore.get("sql-database-url");  // The base URL like jdbc:sqlserver://SC_LAPTOP\SQLSERVER

        // Modify URL to include username and password for SQL Authentication
        String url = baseUrl + ";databaseName=" + catalog + ";user=" + user + ";password=" + password + ";encrypt=true;trustServerCertificate=true";

        System.out.println(url);

        // Connect to the database using SQL Authentication
        Connection connection = dbUtilities.getConnection(user, password, url);

        List<Map<String, Object>> results;
        long cdpRecordTimeout = Long.parseLong(ContextStore.get("record-timeout", "10000"));
        int cdpCheckInterval = 60;
        long initialTime = System.currentTimeMillis();
        boolean resultsAcquired;
        boolean timeout = false;
        int duration = 0;
        int counter = 0;

        do {
            counter++;

            // Execute the query
            results = dbUtilities.getResults(connection, sqlQuery, false);
            resultsAcquired = !results.isEmpty();

            if (resultsAcquired) break;

            try {
                TimeUnit.SECONDS.sleep(cdpCheckInterval);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (System.currentTimeMillis() - initialTime > cdpRecordTimeout) timeout = true;
            duration = (int) ((System.currentTimeMillis() - initialTime) / 1000);
        }
        while (!timeout);

        if (counter > 1) log.info("Had to check " + markup(BLUE, String.valueOf(counter)) + " times.");

        if (resultsAcquired) log.info(String.format("It took about %s seconds for the entry to be found in database.",
                markup(BLUE, String.valueOf(duration))));

        return results;
    }
}
