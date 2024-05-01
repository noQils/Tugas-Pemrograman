package assignments.assignment3.payment;

public class DebitPayment implements DepeFoodPaymentSystem {
    private double MINIMUM_TOTAL_PRICE = 50000;

    public long processPayment (long amount, long saldoUser){
        /*
         * return the remaining balance after the transaction
         */
        return saldoUser - amount;
    }

    public long getMinPrice(){
        /*
         * return the minimum total price for a transaction
         */
        return (long) MINIMUM_TOTAL_PRICE;
    }

    public String paymentType(){
        /*
         * return the payment type
         */
        return "debit";
    }
}
