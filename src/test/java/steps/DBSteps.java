package steps;

import context.ContextStore;
import io.cucumber.java.en.Given;
import io.github.soldelv.sql.builder.QueryBuilder;
import lombok.Getter;
import utils.DBUtilities;
import utils.Printer;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static utils.StringUtilities.*;
import static utils.reflection.ReflectionUtilities.getMethod;
import static utils.StringUtilities.Color.BLUE;

public class CustomerDataPlatform extends QueryBuilder {

    Printer log = new Printer(CustomerDataPlatform.class);

    DBUtilities dbUtilities = new DBUtilities();

    @Getter
    public enum CdpTable {
        AZB_CACHE("dbo.azbCache"),
        IRU_RESERVATION("cdp.iruReservation"),
        CONSENTED_GOLDEN_RECORD("[cdp].[cmConsentedGoldenRecord]"),
        SINGLE_RESULT("int");

        private final String value;

        CdpTable(String operator) {
            this.value = operator;
        }
    }

    public List<User> getUsers(QueryAttributes queryAttributes) {
        String sqlQuery = sqlQueryBuilder(queryAttributes);
        List<User> users = new ArrayList<>();
        for (Map<String, Object> userMap : executeQuery(sqlQuery)) {
            User user = new User();

            for (String key : userMap.keySet()) {
                try {
                    if (userMap.get(key) != null)
                        getMethod("set" + firstLetterCapped(key), user.getClass())
                                .invoke(user, userMap.get(key).toString());
                } catch (NoSuchMethodException ignored) {
                } catch (InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            users.add(user);
        }
        return users;
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
        String username = ContextStore.get("sql-database-user");
        String password = ContextStore.get("sql-database-password");
        String catalog = ContextStore.get("sql-database-catalog");
        String baseUrl = "jdbc:sqlserver://" + ContextStore.get("sql-database-url");
        String url = baseUrl + ";database=" + catalog;

        Connection connection = dbUtilities.getConnection(username, password, url); // TODO: @Database include connection on hooks ?
        List<Map<String, Object>> results;
        long cdpRecordTimeout = Long.parseLong(ContextStore.get("cdp-record-timeout"));
        int cdpCheckInterval = 60;
        long initialTime = System.currentTimeMillis();
        boolean resultsAcquired;
        boolean timeout = false;
        int duration = 0;
        int counter = 0;
        do {
            counter++;

            results = dbUtilities.getResults(connection, sqlQuery, false);
            resultsAcquired = results.size() > 0;

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

    @Given("Print {} attribute of {} from {} table")
    public void printResultsFromTable(String attribute, DataType dataType, CustomerDataPlatform.CdpTable tableName, List<CustomerDataPlatform.QueryConditions> conditions) {
        log.info("Generating query to check table " + tableName.getValue());

        CustomerDataPlatform.QueryAttributes queryValues = new CustomerDataPlatform.QueryAttributes();
        queryValues.selectAll();
        queryValues.setFrom(tableName.getValue());
        queryValues.setConditions(conditions);

        List<?> dataList = List.of();

        switch (dataType) {
            case USER, UPODI_CUSTOMER, UPODI_SUBSCRIPTION, IRU_RESERVATION ->
                    dataList = cdp.getData(User.class, queryValues);
        }

        dataList.forEach(data -> {
            try {
                log.important(attribute + " : " + getMethod("get" + attribute, data.getClass()).invoke(data));
            } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
