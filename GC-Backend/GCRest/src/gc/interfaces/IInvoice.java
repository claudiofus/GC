package gc.interfaces;

import java.awt.Rectangle;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import gc.model.types.Scadenza;

public interface IInvoice {
	public Rectangle getIdFatt();
	public String getNumber(PDDocument file, int page);
	public java.sql.Date getDate(PDDocument file, int page);
	public List<Scadenza> getDeadlines(PDDocument file, int page);
}
