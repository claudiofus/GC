package gc.utils;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.itextpdf.kernel.pdf.canvas.parser.filter.TextRegionEventFilter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.FilteredTextEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;

import gc.model.Product;

public class Utils {
	public static <E extends Enum<E>> boolean isInEnum(String value,
			Class<E> enumClass, boolean useDot) {
		if (useDot) {
			for (E e : enumClass.getEnumConstants()) {
				if (e.name().equalsIgnoreCase(value)
						|| (e.name() + ".").equalsIgnoreCase(value)) {
					return true;
				}
			}
		} else {
			for (E e : enumClass.getEnumConstants()) {
				if (e.name().equalsIgnoreCase(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean containsName(final List<Product> list,
			final String name) {
		return list.stream().filter(o -> o.getName().equalsIgnoreCase(name))
				.findFirst().isPresent();
	}

	public static boolean containsCode(final List<Product> list,
			final String code) {
		return list.stream().filter(o -> o.getCode().equalsIgnoreCase(code))
				.findFirst().isPresent();
	}

	public static float calcDiscount(float original, float discount) {
		if (original == 0 && discount == 0) {
			return 0;
		}

		return (1 - (discount / original)) * 100;
	}

	public static float round(float number, int scale) {
		int pow = 10;
		for (int i = 1; i < scale; i++)
			pow *= 10;
		float tmp = number * pow;
		return ((float) ((int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp)))
				/ pow;
	}

	/**
	 * Utility method to save InputStream data to target location/file
	 * 
	 * @param inStream
	 *            - InputStream to be saved
	 * @param target
	 *            - full path to destination file
	 * @throws IOException
	 */
	public static void saveToFile(InputStream inStream, String target)
			throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}

	/**
	 * Creates a folder to desired location if it not already exists
	 * 
	 * @param dirName
	 *            - full path to the folder
	 * @throws SecurityException
	 *             - in case you don't have permission to create the folder
	 */
	public static void createFolderIfNotExists(String dirName) {
		File theDir = new File(dirName);
		if (!theDir.exists()) {
			boolean created = theDir.mkdir();
			if (!created) {
				System.err.println("Directory not created in: " + dirName);
			}
		}
	}

	public static ITextExtractionStrategy getStrategy(int x, int y, int width,
			int height) {
		com.itextpdf.kernel.geom.Rectangle rectangle = new com.itextpdf.kernel.geom.Rectangle(
				x, y, width, height);
		TextRegionEventFilter filter = new TextRegionEventFilter(rectangle);
		return new FilteredTextEventListener(
				new LocationTextExtractionStrategy(), filter);
	}

	public static String extractData(File file, Rectangle rect) {
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

	public static String extractDataOnePage(File file, Rectangle rect) {
		String readText = "";
		StringBuffer buf = new StringBuffer();
		try (PDDocument document = PDDocument.load(file)) {
			if (!document.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);
				stripper.addRegion("class1", rect);
				stripper.extractRegions(document.getPage(0));
				buf.append(stripper.getTextForRegion("class1"));
				readText = buf.toString();
				System.out.println("readText: " + readText);
			}
		} catch (IOException e) {
			System.err.println(
					"Exception while trying to read pdf document - " + e);
		}
		return readText;
	}

	public static java.sql.Date extractOrderDate(String regex, String text,
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

	public static List<String> getDateFromString(String str) {
		List<String> allMatches = new ArrayList<>();
		Matcher m = Pattern.compile(
				"(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d")
				.matcher(str);
		while (m.find()) {
			allMatches.add(m.group());
		}
		return allMatches;
	}

	public static String sqlDateToDate(java.sql.Date sqlDate, String format) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
		return DATE_FORMAT.format(sqlDate);
	}
}
