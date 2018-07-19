package gc.fornitori;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.collections4.map.LinkedMap;
import org.apache.commons.io.FileUtils;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;

import gc.model.Order;
import gc.model.Product;
import gc.utils.DBUtils;
import gc.utils.Utils;

public class Montone extends Order {
	public static final String DDT_DESCR = "D.D.T.";
	public static final String DB_CODE = "montone";
	public static final String DATE_ORDER_REGEX = "\\d{2}\\/\\d{2}\\/\\d{2}";
	public static final String DATE_FORMAT = "dd/MM/yy";

	public Montone() {
	}

	public LinkedMap<String, ArrayList<Order>> parseOrder(File file, Connection conn) {
		final NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
		if (format instanceof DecimalFormat) {
			((DecimalFormat) format).setParseBigDecimal(true);
		}
		LinkedMap<String, ArrayList<Order>> map = new LinkedMap<>();
		String esito = extractDataIText(file);
		String[] righe = esito.split("\n");
		java.sql.Date sqlDate = null;
		for (String riga : righe) {
			if (riga.startsWith(DDT_DESCR)) {
				sqlDate = extractOrderDate(DATE_ORDER_REGEX, riga, DATE_FORMAT);
				map.put(riga, null);
			} else if (map.size() > 0) {
				String lastKey = map.lastKey();
				ArrayList<Order> items = map.get(lastKey) == null ? new ArrayList<>() : map.get(lastKey);
				String[] itemParts = riga.trim().replaceAll("  +", ";").split(";");
				Order ord = new Order();
				try {
					if (itemParts.length > 1) {
						String productID = itemParts[0].trim();
						String productDesc = itemParts[1].trim();
						Product prd = new Product(productID, productDesc, DB_CODE);
						String um = itemParts[2];
						float quantity = format.parse(itemParts[3]).floatValue();
						float price = format.parse(itemParts[4]).floatValue();
						float adj_price = format.parse(itemParts[5]).floatValue();
						float iva = format.parse(itemParts[6]).floatValue();
						float discount = Utils.round(Utils.calcDiscount(price, adj_price / quantity), 2);
						if (itemParts.length == 7) {
							ord = new Order(productID, productDesc, um, quantity, price, discount < 1 ? 0 : discount,
									adj_price, iva, sqlDate);
						}
						Product prdToFind = DBUtils.findProduct(conn, prd);
						if (prdToFind == null) {
							DBUtils.insertProduct(conn, prd);
						}

						DBUtils.insertOrdine(conn, ord, false);
						conn.commit();
						items.add(ord);
						map.put(lastKey, items);
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						conn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
		return map;
	}

	public String extractDataIText(File file) {
		StringBuffer buf = new StringBuffer();
		try {
			InputStream targetStream = FileUtils.openInputStream(file);
			PdfDocument pdfDoc = new PdfDocument(new PdfReader(targetStream));

			// Left-bottom is (0,0)
			// http://developers.itextpdf.com/content/best-itext-questions-stackoverview/questions-about-pdf-general/itext7-where-origin-xy-pdf-page
			// To find the correct rectangle for each section we have to open the PDF with
			// GIMP or other editor
			// rotate the image of 180° and reflect the image selecting the options from the
			// right panel
			// select "pt" as unit misure at left-lower corner and point the cursor on the
			// text you want to extract
			// then create a rectangle and check it's width and length from right panel
			for (int page = 1; page <= pdfDoc.getNumberOfPages(); page++) {
				if (page > 1)
					buf.append("\n");
				ITextExtractionStrategy dataDocStr = getStrategy(6, 250, 600, 325);
				buf.append(PdfTextExtractor.getTextFromPage(pdfDoc.getPage(page), dataDocStr).trim());
			}
			pdfDoc.close();
		} catch (IOException ioExc) {
			ioExc.printStackTrace();
		}
		String readText = buf.toString();
		System.out.println("Parsed text : " + readText);
		return readText;
	}
}