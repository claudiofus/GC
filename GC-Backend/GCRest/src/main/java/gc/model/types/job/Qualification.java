package gc.model.types.job;

public enum Qualification {
	OPERAIO_COM("Operaio comune"),
	OPERAIO_QUAL("Operaio qualificato"),
	OPERAIO_SPE("Operaio specializzato"),
	IMPIEGATO("Impiegato"),
	QUADRO("Quadro"),
	DIRIGENTE("Dirigente");

	private String profQualification;

	private Qualification(String qualification) {
		this.profQualification = qualification;
	}

	public String getQualification() {
		return profQualification;
	}

	public static String getEnumByString(String code){
        for(Qualification e : Qualification.values()){
            if(code.equals(e.profQualification)) return e.name();
        }
        return null;
    }
}
