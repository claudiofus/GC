package gc.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Config {
	private static final Logger logger = LogManager.getLogger(Config.class.getName());
	private static Properties defaultProps = new Properties();

	static {
		String configFile = isWindows() ? "config.properties" : "raspi-config.properties";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(configFile);

		try {
			defaultProps.load(input);
			input.close();
			logger.info("Properties loaded from: " + configFile);
		} catch (IOException e) {
			logger.error("Exception retrieving config properties - " + e);
		}
	}

	public static String getProperty(String key) {
		return defaultProps.getProperty(key);
	}

	public static String getOsName() {
		String OS = null;
		if (OS == null) {
			OS = System.getProperty("os.name");
		}
		return OS;
	}

	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}
}
