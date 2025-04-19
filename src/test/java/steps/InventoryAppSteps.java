package steps;

import context.ContextStore;
import io.cucumber.java.en.Given;
import io.github.soldelv.sql.builder.QueryBuilder;
import models.sql.database.Database;
import org.junit.Assert;
import pages.inventory.app.Transaction;
import utils.reflection.ReflectionUtilities;

import static utils.StringUtilities.contextCheck;

public class InventoryAppSteps extends Database {

    Transaction transactions = new Transaction();

    @Given("Save transaction to context")
    public void saveTransaction(){
        models.sql.database.model.Transaction transaction = transactions.getLast();
        ContextStore.put("CONTEXT-transaction", transaction);
    }

    @Given("Verify save transaction against the database")
    public void getTransaction(){
        models.sql.database.model.Transaction contextTransaction = ContextStore.get("CONTEXT-transaction");
        String transactionId = "'" + contextCheck(contextTransaction.getTransactionId()) + "'";

        QueryBuilder.QueryAttributes attributes = new QueryBuilder.QueryAttributes();
        attributes.selectAll();
        attributes.setFrom(Database.Table.TRANSACTIONS.getFrom());
        attributes.setWhere("TransactionId", QueryBuilder.Operator.IS, transactionId);
        String query = sqlQueryBuilder(attributes);

        models.sql.database.model.Transaction transaction = models.sql.database.model.Transaction.from(executeQuery(query).get(0));
        Assert.assertTrue(
                "Data mismatch between the UI and the database!",
                ReflectionUtilities.objectsMatch(contextTransaction, transaction, "Cash", "Id", "Uid", "Date")
        );
    }

}
