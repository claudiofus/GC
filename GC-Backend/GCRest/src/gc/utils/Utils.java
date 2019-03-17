package gc.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import gc.einvoice.DatiDDTType;
import gc.model.Product;
import gc.model.Provider;
import gc.model.Vehicle;

public class Utils {
	private static DatatypeFactory datatypeFactory;

	private static final Logger logger = LogManager
			.getLogger(Utils.class.getName());

	public static boolean containsName(final List<Product> list,
			final String name) {
		return list.stream().filter(o -> o.getName().equalsIgnoreCase(name))
				.findFirst().isPresent();
	}

	// TODO GENERALIZZARE
	public static boolean containsProvName(final List<Provider> list,
			final String name) {
		return list.stream().filter(o -> o.getName().equalsIgnoreCase(name))
				.findFirst().isPresent();
	}

	public static boolean containsPlate(final List<Vehicle> list,
			final String plate) {
		return list.stream().filter(o -> o.getPlate().equals(plate)).findFirst()
				.isPresent();
	}

	public static boolean containsRifLinea(final List<DatiDDTType> list) {
		return list.stream()
				.filter(o -> !o.getRiferimentoNumeroLinea().isEmpty())
				.findFirst().isPresent();
	}

	public static BigDecimal calcDiscount(BigDecimal original,
			BigDecimal discount) {
		if (original.compareTo(BigDecimal.ZERO) == 0
				&& discount.compareTo(BigDecimal.ZERO) == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal divRes = divide(discount, original);
		BigDecimal subRes = BigDecimal.ONE.subtract(divRes);

		return subRes.scaleByPowerOfTen(2);
	}

	public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
		return dividend.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal divide(BigDecimal dividend, int divisor) {
		BigDecimal divisorBD = new BigDecimal(divisor);
		return dividend.divide(divisorBD, 2, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal addIva(BigDecimal price, BigDecimal iva) {
		return price.multiply(BigDecimal.ONE.add(divide(iva, 100)));
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
			boolean created = theDir.mkdirs();
			if (!created) {
				logger.error("Directory not created in: " + dirName);
			}
		}
	}

	public static String getStringFromDate(Date date, boolean time) {
		String dtFormat = time ? "dd/MM/yyyy HH:mm:ss" : "dd/MM/yyyy";
		DateFormat dateFormat = new SimpleDateFormat(dtFormat);
		return dateFormat.format(date);
	}

	public static String sqlDateToDate(java.sql.Date sqlDate, String format) {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(format);
		return DATE_FORMAT.format(sqlDate);
	}

	public static Date fromXMLGrToDate(XMLGregorianCalendar cal) {
		try {
			int timezone = DatatypeConstants.FIELD_UNDEFINED;
			int year = cal.getYear();
			int month = cal.getMonth();
			int day = cal.getDay();
			XMLGregorianCalendar newDate = addGMT(getDatatypeFactory().newXMLGregorianCalendarDate(year,
					month, day, timezone));
			return newDate.toGregorianCalendar().getTime();
		} catch (DatatypeConfigurationException e) {
			// Should never happen if JDK is correctly configured.
			throw new RuntimeException(
					"Error in instancing XMLGregorianCalendar XML datatype", e);
		}
	}

	private static XMLGregorianCalendar addGMT(XMLGregorianCalendar dataXML) {
		int offset = TimeZone.getTimeZone("GMT").getDSTSavings();
		dataXML.setTimezone(offset);
		return dataXML;
	}

	private static DatatypeFactory getDatatypeFactory()
			throws DatatypeConfigurationException {
		if (datatypeFactory == null) {
			datatypeFactory = DatatypeFactory.newInstance();
		}
		return datatypeFactory;
	}

	public static byte[] removeP7MCodes(final String fileFullPath) {
		if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
			Security.addProvider(new BouncyCastleProvider());
		}

		try {
			byte[] p7bytes = Files.readAllBytes(Paths.get(fileFullPath));
			if (p7bytes == null
					|| !fileFullPath.toUpperCase().endsWith(".P7M")) {
				return p7bytes;
			}
			try {
				p7bytes = org.bouncycastle.util.encoders.Base64.decode(p7bytes);
			} catch (Exception e) {
				logger.warn("File P7m not in base64, keep standard content: "
						+ e.getMessage());
			}

			CMSSignedData cms = new CMSSignedData(p7bytes);
			if (cms.getSignedContent() == null)
				logger.warn(
						"Impossible to find signed Content while decoding from P7M for file: "
								+ fileFullPath);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			cms.getSignedContent().write(out);
			out.flush();
			return out.toByteArray();
		} catch (CMSException | IOException e) {
			logger.error("Impossible to decode P7M for file:", e);
			return null;
		}
	}
}
