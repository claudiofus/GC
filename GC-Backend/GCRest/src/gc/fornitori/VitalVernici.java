package gc.fornitori;

import java.awt.Rectangle;
import java.sql.Date;

import gc.model.types.BaseOrder;

public class VitalVernici extends BaseOrder {
	private static String DDT_DESCR = "D.D.T.";
	private static Rectangle PDFBOX_RECT = new Rectangle(22, 330, 544, 343);
	private static String DB_CODE = "viltalvernici";

	public VitalVernici() {
	}

	public VitalVernici(int id, String productID, String productDesc, String um,
			float quantity, float price, float discount, float adj_price,
			float iva, Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount,
				adj_price, iva, sqlDate);
	}

	public VitalVernici(String productID, String productDesc, String um,
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
}