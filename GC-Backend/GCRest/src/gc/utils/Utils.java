package gc.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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
}
