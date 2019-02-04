package zw.co.paynow.payments;

/**
 * Enumeration for the mobile money method to use
 *
 * @deprecated No longer used in this project
 */
public enum MobileMoneyMethod {
    ECOCASH, TELECASH, ONE_MONEY;

    public static final int SIZE = java.lang.Integer.SIZE;

    public static MobileMoneyMethod forValue(int value) {
        return values()[value];
    }

    public int getValue() {
        return this.ordinal();
    }
}