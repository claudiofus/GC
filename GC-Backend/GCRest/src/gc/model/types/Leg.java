package gc.model.types;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg {

	@JsonProperty("distance")
	private Distance distance;
	@JsonProperty("duration")
	private Duration duration;

	@JsonProperty("distance")
	public Distance getDistance() {
		return distance;
	}

	@JsonProperty("distance")
	public void setDistance(Distance distance) {
		this.distance = distance;
	}

	@JsonProperty("duration")
	public Duration getDuration() {
		return duration;
	}

	@JsonProperty("duration")
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
}