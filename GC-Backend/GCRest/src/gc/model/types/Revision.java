package gc.model.types;

import java.io.Serializable;
import java.util.Date;

public class Revision extends AScadenza implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6846517484524854580L;

	public static java.sql.Date calcRev(java.sql.Date buyDate) {
		//1a revisione dopo 4 anni poi 2 anni
		return new java.sql.Date(new Date().getTime());
	}
}
