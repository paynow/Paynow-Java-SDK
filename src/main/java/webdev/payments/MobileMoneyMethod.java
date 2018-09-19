package webdev.payments;

/**
 * @deprecated
 */
public enum MobileMoneyMethod {
    Ecocash;

    public static final int SIZE = java.lang.Integer.SIZE;

    public static MobileMoneyMethod forValue(int value) {
        return values()[value];
    }

    public int getValue() {
        return this.ordinal();
    }
}