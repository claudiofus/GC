package gc.fornitori;

import java.awt.Rectangle;
import java.sql.Date;

import gc.model.types.BaseOrder;

public class Intermobil extends BaseOrder {
	private static String DDT_DESCR = "D.d.T.";
	private static Rectangle PDFBOX_RECT = new Rectangle(15, 233, 564, 402);
	private static String DB_CODE = "intermobil";
	private static String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	private static String DATE_FORMAT = "dd/MM/yyyy";
	
	public Intermobil() {
	}
	
	public Intermobil(int id, String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public Intermobil(String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, Date sqlDate) {

		super(productID, productDesc, um, quantity, price, discount, adj_price,
				iva, sqlDate);
	}
	
	@Override
	public String getDDT() {
		return DDT_DESCR;
	}

	@Override
	public Rectangle getPDFRECT() {
		return PDFBOX_RECT;
	}

	@Override
	public String getDBCODE() {
		return DB_CODE;
	}

	@Override
	public String getDATEORDER() {
		return DATE_ORDER_REGEX;
	}

	@Override
	public String getDATEFORMAT() {
		return DATE_FORMAT;
	}
}