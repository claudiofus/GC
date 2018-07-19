package gc.model;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.itextpdf.kernel.pdf.canvas.parser.filter.TextRegionEventFilter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredTextEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import gc.utils.DBUtils;
import gc.utils.Utils;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Order {
	@XmlElement
	private int id;
	@XmlElement
	private Integer building_id;
	@XmlElement
	private String code;
	@XmlElement
	private String name;
	@XmlElement
	private String um;
	@XmlElement
	private float quantity;
	@XmlElement
	private float price;
	@XmlElement
	private float discount;
	@XmlElement
	private float adj_price;
	@XmlElement
	private float iva;
	private java.sql.Date date_order;
	private boolean state;

	public Order() {
		super();
	}

	public Order(int id, String code, String name, String um, float quantity,
			float price, float discount, float adj_price, float iva,
			java.sql.Date date_order) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.um = um;
		this.quantity = quantity;
		this.price = price;
		this.discount = discount;
		this.adj_price = adj_price;
		this.iva = iva;
		this.date_order = new java.sql.Date(date_order.getTime());
	}

	public Order(String code, String name, String um, float quantity,
			float price, float discount, float adj_price, float iva,
			java.sql.Date date_order) {
		super();
		this.code = code;
		this.name = name;
		this.um = um;
		this.quantity = quantity;
		this.price = price;
		this.discount = discount;
		this.adj_price = adj_price;
		this.iva = iva;
		this.date_order = new java.sql.Date(date_order.getTime());
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getUm() {
		return um;
	}

	public void setUm(String um) {
		this.um = um;
	}

	public float getQuantity() {
		return quantity;
	}

	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	public float getAdj_price() {
		return adj_price;
	}

	public void setAdj_price(float adj_price) {
		this.adj_price = adj_price;
	}

	public float getIva() {
		return iva;
	}

	public void setIva(float iva) {
		this.iva = iva;
	}

	public float getDiscount() {
		return discount;
	}

	public void setDiscount(float discount) {
		this.discount = discount;
	}

	public java.sql.Date getDate_order() {
		return new java.sql.Date(date_order.getTime());
	}

	public void setDate_order(java.sql.Date date_order) {
		this.date_order = new java.sql.Date(date_order.getTime());
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Integer getBuilding_id() {
		return building_id;
	}

	public void setBuilding_id(Integer building_id) {
		this.building_id = building_id;
	}

	public ITextExtractionStrategy getStrategy(int x, int y, int width,
			int height) {
		com.itextpdf.kernel.geom.Rectangle rectangle = new com.itextpdf.kernel.geom.Rectangle(
				x, y, width, height);
		TextRegionEventFilter filter = new TextRegionEventFilter(rectangle);
		return new FilteredTextEventListener(
				new LocationTextExtractionStrategy(), filter);
	}

	public LinkedMap<String, ArrayList<Order>> parseOrder(File file,
			Connection conn) {
		String descDDT = null, dateOrderRegex = null, dateFormat = null;
		Rectangle rect = null;
		try {
			Field descDDTField = this.getClass().getDeclaredField("DDT_DESCR");
			descDDTField.setAccessible(true);
			descDDT = (String) descDDTField.get(String.class);
			Field pdfboxRectField = this.getClass()
					.getDeclaredField("PDFBOX_RECT");
			pdfboxRectField.setAccessible(true);
			rect = (Rectangle) pdfboxRectField.get(Rectangle.class);
			Field dateOrderRegexField = this.getClass()
					.getDeclaredField("DATE_ORDER_REGEX");
			dateOrderRegexField.setAccessible(true);
			dateOrderRegex = (String) dateOrderRegexField.get(String.class);
			Field dateFormatField = this.getClass()
					.getDeclaredField("DATE_FORMAT");
			dateFormatField.setAccessible(true);
			dateFormat = (String) dateFormatField.get(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		final NumberFormat format = NumberFormat
				.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
		LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
		String esito = extractData(file, rect);
		java.sql.Date sqlDate = null;
		String[] righe = esito.split("\n");
		for (String riga : righe) {
			if (riga.startsWith(descDDT) && !map.containsKey(riga)) {
				sqlDate = extractOrderDate(dateOrderRegex, riga, dateFormat);
				map.put(riga, null);
			} else if (map.size() > 0) {
				try {
					populateMap(sqlDate, map, conn, riga, false);
					conn.commit();
				} catch (Exception e) {
					System.err.println("Error parsing : " + riga);
					System.out.println(
							"Strategy changed, retry to parse : " + riga);
					try {
						conn.rollback();
						populateMap(sqlDate, map, conn, riga, true);
						conn.commit();
					} catch (ParseException | SQLException e1) {
						System.err.println("Unable to parse : " + riga);
						e.printStackTrace();
					}
				}
			}
		}
		return map;
	}

	public String extractData(File file, Rectangle rect) {
		String readText = "";
		StringBuffer buf = new StringBuffer();
		try (PDDocument document = PDDocument.load(file)) {
			if (!document.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);
				stripper.addRegion("class1", rect);
				for (int page = 0; page < document.getNumberOfPages(); page++) {
					if (page > 0)
						buf.append("\n");
					stripper.extractRegions(document.getPage(page));
					buf.append(stripper.getTextForRegion("class1"));
				}
				readText = buf.toString();
				System.out.println("readText: " + readText);
			}
		} catch (IOException e) {
			System.err.println(
					"Exception while trying to read pdf document - " + e);
		}
		return readText;
	}

	public java.sql.Date extractOrderDate(String regex, String text,
			String dateFormat) {
		java.sql.Date sqlDate = null;
		Matcher m = Pattern.compile(regex).matcher(text);
		if (m.find()) {
			String date = m.group();
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			Date dob = null;
			try {
				dob = sdf.parse(date);
				sqlDate = new java.sql.Date(
						dob.getTime() + 24 * 60 * 60 * 1000);
			} catch (ParseException e) {
				System.err.println("Parse exception, incorrect input in text: "
						+ text + " with regex: " + regex);
				e.printStackTrace();
			}
		} else if (sqlDate == null) {
			// Bad input
			System.err.println("Errors while trying to get date in text: "
					+ text + " with regex: " + regex);
			System.out.println("I will use the actual date.");
			sqlDate = new java.sql.Date(new Date().getTime());
		}
		return sqlDate;
	}

	public void populateMap(java.sql.Date sqlDate,
			LinkedMap<String, ArrayList<Order>> map, Connection conn,
			String riga, boolean useDot) throws ParseException, SQLException {
		final NumberFormat format = NumberFormat
				.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}

		String dbCode = null;
		try {
			Field dbCodeField = this.getClass().getDeclaredField("DB_CODE");
			dbCodeField.setAccessible(true);
			dbCode = (String) dbCodeField.get(String.class);
		} catch (Exception e) {
			System.err.println(
					"Unable to get field DB_CODE value from the subClass");
			e.printStackTrace();
			return;
		}

		String lastKey = map.lastKey();
		ArrayList<Order> items = map.get(lastKey) == null
				? new ArrayList<>()
				: map.get(lastKey);
		String[] itemParts = riga.trim().replaceAll(" +", ";").split(";");

		if (itemParts.length > 5) {
			Order ord = new Order();
			StringBuilder strBuild = new StringBuilder();
			int indice = 0;

			/*
			 * TODO CONTROLLARE QUANTI ELEMENTI DI UM CI SONO NELLA RIGA E
			 * PRENDERE L'ULTIMO COME UM TUTTO CIO' QUELLO PRIMA FA PARTE DELLA
			 * DESCRIZIONE
			 */

			for (int i = 1; i < itemParts.length; i++) {
				if (!Utils.isInEnum(itemParts[i], UM.class, useDot)) {
					strBuild.append(itemParts[i] + " ");
					indice = i + 1;
				} else {
					break;
				}
			}
			String productID = itemParts[0].trim();
			String productDesc = strBuild.toString().trim();
			Product prd = new Product(productID, productDesc, dbCode);
			// 0-based
			String um = itemParts[indice];
			float quantity = format.parse(itemParts[indice + 1]).floatValue();
			float price = format.parse(itemParts[indice + 2]).floatValue();
			if (itemParts.length == indice + 5 + 1) {
				float adj_price = format.parse(itemParts[indice + 4])
						.floatValue();
				float iva = format.parse(itemParts[indice + 5]).floatValue();
				float discount = Utils.round(
						Utils.calcDiscount(price, adj_price / quantity), 2);
				ord = new Order(productID, productDesc, um, quantity, price,
						discount < 1 ? 0 : discount, adj_price, iva, sqlDate);
			} else if (itemParts.length == indice + 4 + 1) {
				float adj_price = format.parse(itemParts[indice + 3])
						.floatValue();
				float iva = format.parse(itemParts[indice + 4]).floatValue();
				float discount = Utils.round(
						Utils.calcDiscount(price, adj_price / quantity), 2);
				ord = new Order(productID, productDesc, um, quantity, price,
						discount < 1 ? 0 : discount, adj_price, iva, sqlDate);
			} else {
				map.put("Errore", items);
			}
			Product prdToFind = DBUtils.findProduct(conn, prd);
			if (prdToFind == null) {
				DBUtils.insertProduct(conn, prd);
			}
			DBUtils.insertOrdine(conn, ord, false);
			items.add(ord);
			map.put(lastKey, items);
		}
	}
}