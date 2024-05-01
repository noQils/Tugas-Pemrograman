package assignments.assignment3.payment;

public interface DepeFoodPaymentSystem {
    long saldo = 0;
    public long processPayment(long amount, long saldoUser);
    public String paymentType();
}