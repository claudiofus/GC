package gc.model.types;

public class Insurance extends BaseDeadline {
	private boolean sixmonths;

	public boolean isSixmonths() {
		return sixmonths;
	}

	public void setSixmonths(boolean sixmonths) {
		this.sixmonths = sixmonths;
	}
}