package assignments.assignment3.payment;

public class CreditCardPayment implements DepeFoodPaymentSystem {
    private double TRANSACTION_FEE_PERCENTAGE;

    public long countTransactionFee(long amount){
        /*
         * return the transaction fee (2% of the total transaction amount)
         */
        TRANSACTION_FEE_PERCENTAGE = (0.02 * amount);
        return (long) TRANSACTION_FEE_PERCENTAGE;
    }

    public long processPayment(long amount, long saldoUser){
        /*
         * return the remaining balance after the transaction
         */
        return saldoUser - (amount + countTransactionFee(amount));
    }

    public String paymentType(){
        /*
         * return the payment type
         */
        return "credit";
    }
}
