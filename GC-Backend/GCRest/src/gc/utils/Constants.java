package gc.utils;

public class Constants {
	public static final String ROLLBACK_ERROR = "Error in connection rollback: {}";
	public static final String CLOSING_CONN_ERROR = "Error in closing connection: {}";

	private Constants() {
		throw new IllegalStateException("Constants class");
	}
}
