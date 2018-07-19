package gc.fornitori;

import java.awt.Rectangle;

import gc.model.Order;

public class Mag extends Order {
	public static String DDT_DESCR = "DDT n.";
	public static Rectangle PDFBOX_RECT = new Rectangle(22, 346, 543, 266);
	public static String DB_CODE = "mag";
	public static String DATE_ORDER_REGEX = "\\d{1,2}\\/\\d{2}\\/\\d{2}";
	public static String DATE_FORMAT = "dd/MM/yy";

	public Mag() {
	}
}