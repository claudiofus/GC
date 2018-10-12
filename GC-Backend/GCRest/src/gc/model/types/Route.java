package gc.model.types;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

	@JsonProperty("legs")
	private List<Leg> legs = null;

	@JsonProperty("legs")
	public List<Leg> getLegs() {
		return legs;
	}

	@JsonProperty("legs")
	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}
}
