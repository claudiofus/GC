package gc.fornitori;

import java.awt.Rectangle;

import gc.model.Order;

public class ResinaColor extends Order {
	public static String DDT_DESCR = "BOLLA N. ";
	public static Rectangle PDFBOX_RECT = new Rectangle(18, 272, 547, 380);
	public static String DB_CODE = "resinaColor";
	public static String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	public static String DATE_FORMAT = "dd/MM/yyyy";
	
	public ResinaColor() {
	}
}