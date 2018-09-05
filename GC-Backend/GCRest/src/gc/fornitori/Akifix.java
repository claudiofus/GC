package gc.fornitori;

import java.awt.Rectangle;
import java.sql.Date;

import gc.model.types.BaseOrder;

public class Akifix extends BaseOrder {
	private static String DDT_DESCR = "BOLLA N. ";
	private static Rectangle ORDERS_AREA = new Rectangle(18, 272, 547, 380);
	private static String DB_CODE = "akifix";
	private static String DATE_ORDER_REGEX = "TO_DEFINE";
	private static String DATE_FORMAT = "TO_DEFINE";

	public Akifix() {
	}

	public Akifix(int id, String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public Akifix(String productID, String productDesc, String um,
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
	public Rectangle getORDERS_AREA() {
		return ORDERS_AREA;
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