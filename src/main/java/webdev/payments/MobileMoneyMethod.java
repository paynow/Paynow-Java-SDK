package webdev.payments;

public enum MobileMoneyMethod
{
	Ecocash;

	public static final int SIZE = java.lang.Integer.SIZE;

	public int getValue()
	{
		return this.ordinal();
	}

	public static MobileMoneyMethod forValue(int value)
	{
		return values()[value];
	}
}