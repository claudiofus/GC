package gc.fornitori;

import java.awt.Rectangle;

import gc.model.Order;

public class Intermobil extends Order {

	public static String DDT_DESCR = "D.d.T.";
	public static Rectangle PDFBOX_RECT = new Rectangle(15, 233, 564, 402);
	public static String DB_CODE = "intermobil";
	public static String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	public static String DATE_FORMAT = "dd/MM/yyyy";
	
	public Intermobil() {
	}
}