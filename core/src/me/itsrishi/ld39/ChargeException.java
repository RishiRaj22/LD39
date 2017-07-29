package me.itsrishi.ld39;

/**
 * @author Rishi Raj
 */

public class ChargeException extends Exception {
    public static final int OVERCHARGE = 1;
    public static final int DISCHARGE = 2;
    Phone phone;
    int reason;

    public ChargeException(Phone phone, int reason) {
        this.phone = phone;
        this.reason = reason;
    }
}
