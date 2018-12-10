package gc.factory;

import gc.fornitori.Akifix;
import gc.fornitori.AutofficinaLippolis;
import gc.fornitori.FinishVillage;
import gc.fornitori.Intermobil;
import gc.fornitori.Mag;
import gc.fornitori.Montone;
import gc.fornitori.ResinaColor;
import gc.model.types.BaseOrder;

public class OrderFactory {

	public BaseOrder getBaseOrder(String provider) {
		BaseOrder ord = null;
		switch (provider) {
		case "montone":
			ord = new Montone();
			break;
		case "mag":
			ord = new Mag();
			break;
		case "resinaColor":
			ord = new ResinaColor();
			break;
		case "intermobil":
			ord = new Intermobil();
			break;
		case "finishVillage":
			ord = new FinishVillage();
			break;
		case "autoffLippolis":
			ord = new AutofficinaLippolis();
			break;
		case "akifix":
			ord = new Akifix();
			break;
		}

		return ord;
	}
}
