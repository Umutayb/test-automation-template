package models.sql.database.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
public class Transaction {
    int Uid;
    String Total;
    String Subtotal;
    int Id;
    String Change;
    String Cash;
    String Date;
    String DiscountPercent;
    String DiscountAmount;
    String TransactionId;

    public Transaction(String date, String subtotal, String total, String discountPercent, String change, String discountAmount, String transactionId) {
        Date = date;
        Subtotal = subtotal;
        Total = total;
        DiscountPercent = discountPercent;
        Change = change;
        DiscountAmount = discountAmount;
        TransactionId = transactionId;
    }

    public static Transaction from(Map<String, Object> map){
        Transaction transaction = new Transaction();
        transaction.Cash = String.valueOf(map.get("Cash"));
        transaction.Uid = (int) map.get("Uid");
        transaction.Total = String.valueOf(map.get("Total"));
        transaction.Subtotal = String.valueOf(map.get("Subtotal"));
        transaction.Id = (int) map.get("Id");
        transaction.Change = String.valueOf(map.get("Change"));
        transaction.DiscountPercent = String.valueOf(map.get("DiscountPercent"));
        transaction.DiscountAmount = String.valueOf(map.get("DiscountAmount"));
        transaction.TransactionId = String.valueOf(map.get("TransactionId"));
        transaction.Date = String.valueOf(map.get("Date"));
        return transaction;
    }
}
