package gc.model.types.job;

public enum Qualification {
	OPERAIO_COM("Operaio comune"),
	OPERAIO_QUAL("Operaio qualificato"),
	OPERAIO_SPE("Operaio specializzato"),
	IMPIEGATO("Impiegato"),
	QUADRO("Quadro"),
	DIRIGENTE("Dirigente");

	private String qualification;

	private Qualification(String qualification) {
		this.qualification = qualification;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	
	public static String getEnumByString(String code){
        for(Qualification e : Qualification.values()){
            if(code == e.qualification) return e.name();
        }
        return null;
    }
}
