package gc.utils;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JaxbUtil {
	private static Map<Class<?>, JAXBContext> jaxbContextClassCache = new HashMap<Class<?>, JAXBContext>();
	private static final Logger logger = LogManager
			.getLogger(JaxbUtil.class.getName());

	private static JAXBContext getJaxbContextClass(Class<?> clazz)
			throws JAXBException {
		JAXBContext jc = jaxbContextClassCache.get(clazz);
		if (jc == null) {
			jc = JAXBContext.newInstance(clazz);
			jaxbContextClassCache.put(clazz, jc);
		}
		return jc;
	}

	public static <T extends Object> T jaxbUnMarshal(String xml,
			Class<T> claz) {
		T obj = null;
		if (xml != null && claz != null) {
			try {
				JAXBContext context = getJaxbContextClass(claz);
				Unmarshaller um = context.createUnmarshaller();
				ByteArrayInputStream baos = new ByteArrayInputStream(xml.getBytes());
				JAXBElement<T> userElement = um
						.unmarshal(new StreamSource(baos), claz);
				obj = userElement.getValue();
			} catch (Exception e) {
				logger.error("Error in JAXB UnMarshal ", e);
			}
		}
		return obj;
	}
}
