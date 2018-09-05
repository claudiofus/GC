package gc.interfaces;

import java.io.File;
import java.util.List;

import gc.model.types.Scadenza;

public interface IInvoice {
	public String getNumber(File file);
	public java.sql.Date getDate(File file);
	public List<Scadenza> getDeadlines(File file);
}
