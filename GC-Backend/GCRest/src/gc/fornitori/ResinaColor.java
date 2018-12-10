
package gc.fornitori;

import java.awt.Rectangle;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;

import gc.model.types.BaseOrder;
import gc.model.types.Deadline;
import gc.utils.Utils;

public class ResinaColor extends BaseOrder {
	private static final Logger logger = LogManager.getLogger(ResinaColor.class.getName());

	private static final Rectangle ID_FATT = new Rectangle(373, 171, 60, 12);
	private static final Rectangle DATA_FATT = new Rectangle(436, 170, 69, 14);
	private static final Rectangle SCADENZE_FATT = new Rectangle(19, 772, 288, 24);
	private static String DDT_DESCR = "BOLLA N. ";
	private static Rectangle ORDERS_AREA = new Rectangle(18, 272, 547, 380);
	private static String DB_CODE = "resinaColor";
	private static String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{4}";
	private static String DATE_FORMAT = "dd/MM/yyyy";

	public ResinaColor() {
	}

	public ResinaColor(int id, String productID, String productDesc, String um, float quantity, float price,
			float discount, float adj_price, float iva, java.sql.Date sqlDate) {
		super(id, productID, productDesc, um, quantity, price, discount, adj_price, iva, sqlDate);
	}

	public ResinaColor(String productID, String productDesc, String um, float quantity, float price, float discount,
			float adj_price, float iva, java.sql.Date sqlDate) {

		super(productID, productDesc, um, quantity, price, discount, adj_price, iva, sqlDate);
	}

	@Override
	public Rectangle getIdFatt() {
		return ID_FATT;
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

	@Override
	public String getNumber(PDDocument document, int page) {
		return Utils.extractDataNoSpaces(document, ID_FATT, page);
	}

	@Override
	public java.sql.Date getDate(PDDocument document, int page) {
		try {
			String dateStr = Utils.extractDataNoSpaces(document, DATA_FATT, page);
			Date date = new SimpleDateFormat("dd/MM/yy").parse(dateStr);
			java.sql.Date sqlDate = new java.sql.Date(date.getTime());
			return sqlDate;
		} catch (ParseException e) {
			logger.error("Error in method getDate: ", e);
		}
		return null;
	}

	@Override
	public List<Deadline> getDeadlines(PDDocument document, int page) {
		String scad = Utils.extractDataNoSpaces(document, SCADENZE_FATT, page);
		List<Deadline> scadList = new ArrayList<Deadline>();
		List<String> dateList = Utils.getDateFromString(scad);
		List<Float> amount = getAmountFromString(scad);

		try {
			for (int i = 0; i < dateList.size(); i++) {
				Deadline sc = new Deadline();
				java.util.Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateList.get(i));
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				sc.setDeadlineDate(sqlDate);
				sc.setAmount(amount.get(i));
				scadList.add(sc);
			}
		} catch (Exception e) {
			logger.error("Error in method getDeadlines: ", e);
		}
		return scadList;
	}

	private List<Float> getAmountFromString(String str) {
		List<Float> allMatches = new ArrayList<>();
		NumberFormat numberFormat = NumberFormat.getInstance(Locale.ITALY);
		Arrays.stream(str.split("\\r?\\n")).forEach(line -> {
			try {
				if (line != null && !line.isEmpty()) {
					Pattern p = Pattern.compile("([0-9]{1,3}[.])*[0-9]{1,3},[0-9]{1,2}");
					Matcher m = p.matcher(line);
					while (m.find()) {
						allMatches.add(numberFormat.parse(m.group()).floatValue());
					}
				}
			} catch (ParseException e) {
				logger.error("Error in method getAmountFromString: ", e);
			}
		});
		return allMatches;
	}
}