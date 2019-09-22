package gc.utils;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class RESTUtil {
	private RESTUtil() {
		throw new IllegalStateException("RESTUtil class");
	}

	public static <T extends Object> T jsonDeserialize(String msg, Class<T> cl) throws IOException {
		if (msg != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			return mapper.readValue(msg, cl);
		}
		return null;
	}

	public static String jsonSerialize(Object o) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		return mapper.writeValueAsString(o);
	}

	public static <T extends Object> List<T> jsonDeserializeCollection(String msg, Class<T> cl) throws IOException {
		if (msg != null) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JavaType type = mapper.constructType(cl);
			CollectionType tf = TypeFactory.defaultInstance().constructCollectionType(List.class, type);
			return mapper.readValue(msg, tf);
		}
		return Collections.emptyList();
	}
}
