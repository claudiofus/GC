//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrè persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per DettaglioLineeType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DettaglioLineeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroLinea" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}NumeroLineaType"/>
 *         &lt;element name="TipoCessionePrestazione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}TipoCessionePrestazioneType" minOccurs="0"/>
 *         &lt;element name="CodiceArticolo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}CodiceArticoloType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Descrizione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String1000LatinType"/>
 *         &lt;element name="Quantita" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}QuantitaType" minOccurs="0"/>
 *         &lt;element name="UnitaMisura" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String10Type" minOccurs="0"/>
 *         &lt;element name="DataInizioPeriodo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="DataFinePeriodo" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="PrezzoUnitario" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount8DecimalType"/>
 *         &lt;element name="ScontoMaggiorazione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}ScontoMaggiorazioneType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PrezzoTotale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount8DecimalType"/>
 *         &lt;element name="AliquotaIVA" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RateType"/>
 *         &lt;element name="Ritenuta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RitenutaType" minOccurs="0"/>
 *         &lt;element name="Natura" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}NaturaType" minOccurs="0"/>
 *         &lt;element name="RiferimentoAmministrazione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String20Type" minOccurs="0"/>
 *         &lt;element name="AltriDatiGestionali" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}AltriDatiGestionaliType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DettaglioLineeType", propOrder = {
    "numeroLinea",
    "tipoCessionePrestazione",
    "codiceArticolo",
    "descrizione",
    "quantita",
    "unitaMisura",
    "dataInizioPeriodo",
    "dataFinePeriodo",
    "prezzoUnitario",
    "scontoMaggiorazione",
    "prezzoTotale",
    "aliquotaIVA",
    "ritenuta",
    "natura",
    "riferimentoAmministrazione",
    "altriDatiGestionali"
})
public class DettaglioLineeType {

    @XmlElement(name = "NumeroLinea")
    @XmlSchemaType(name = "integer")
    protected int numeroLinea;
    @XmlElement(name = "TipoCessionePrestazione")
    @XmlSchemaType(name = "string")
    protected TipoCessionePrestazioneType tipoCessionePrestazione;
    @XmlElement(name = "CodiceArticolo")
    protected List<CodiceArticoloType> codiceArticolo;
    @XmlElement(name = "Descrizione", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String descrizione;
    @XmlElement(name = "Quantita")
    protected BigDecimal quantita;
    @XmlElement(name = "UnitaMisura")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String unitaMisura;
    @XmlElement(name = "DataInizioPeriodo")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataInizioPeriodo;
    @XmlElement(name = "DataFinePeriodo")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dataFinePeriodo;
    @XmlElement(name = "PrezzoUnitario", required = true)
    protected BigDecimal prezzoUnitario;
    @XmlElement(name = "ScontoMaggiorazione")
    protected List<ScontoMaggiorazioneType> scontoMaggiorazione;
    @XmlElement(name = "PrezzoTotale", required = true)
    protected BigDecimal prezzoTotale;
    @XmlElement(name = "AliquotaIVA", required = true)
    protected BigDecimal aliquotaIVA;
    @XmlElement(name = "Ritenuta")
    @XmlSchemaType(name = "string")
    protected RitenutaType ritenuta;
    @XmlElement(name = "Natura")
    @XmlSchemaType(name = "string")
    protected NaturaType natura;
    @XmlElement(name = "RiferimentoAmministrazione")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String riferimentoAmministrazione;
    @XmlElement(name = "AltriDatiGestionali")
    protected List<AltriDatiGestionaliType> altriDatiGestionali;

    /**
     * Recupera il valore della proprietè numeroLinea.
     * 
     */
    public int getNumeroLinea() {
        return numeroLinea;
    }

    /**
     * Imposta il valore della proprietè numeroLinea.
     * 
     */
    public void setNumeroLinea(int value) {
        this.numeroLinea = value;
    }

    /**
     * Recupera il valore della proprietè tipoCessionePrestazione.
     * 
     * @return
     *     possible object is
     *     {@link TipoCessionePrestazioneType }
     *     
     */
    public TipoCessionePrestazioneType getTipoCessionePrestazione() {
        return tipoCessionePrestazione;
    }

    /**
     * Imposta il valore della proprietè tipoCessionePrestazione.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoCessionePrestazioneType }
     *     
     */
    public void setTipoCessionePrestazione(TipoCessionePrestazioneType value) {
        this.tipoCessionePrestazione = value;
    }

    /**
     * Gets the value of the codiceArticolo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the codiceArticolo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCodiceArticolo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CodiceArticoloType }
     * 
     * 
     */
    public List<CodiceArticoloType> getCodiceArticolo() {
        if (codiceArticolo == null) {
            codiceArticolo = new ArrayList<CodiceArticoloType>();
        }
        return this.codiceArticolo;
    }

    /**
     * Recupera il valore della proprietè descrizione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizione() {
        return descrizione;
    }

    /**
     * Imposta il valore della proprietè descrizione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizione(String value) {
        this.descrizione = value;
    }

    /**
     * Recupera il valore della proprietè quantita.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantita() {
        return quantita;
    }

    /**
     * Imposta il valore della proprietè quantita.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantita(BigDecimal value) {
        this.quantita = value;
    }

    /**
     * Recupera il valore della proprietè unitaMisura.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUnitaMisura() {
        return unitaMisura;
    }

    /**
     * Imposta il valore della proprietè unitaMisura.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUnitaMisura(String value) {
        this.unitaMisura = value;
    }

    /**
     * Recupera il valore della proprietè dataInizioPeriodo.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizioPeriodo() {
        return dataInizioPeriodo;
    }

    /**
     * Imposta il valore della proprietè dataInizioPeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizioPeriodo(XMLGregorianCalendar value) {
        this.dataInizioPeriodo = value;
    }

    /**
     * Recupera il valore della proprietè dataFinePeriodo.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFinePeriodo() {
        return dataFinePeriodo;
    }

    /**
     * Imposta il valore della proprietè dataFinePeriodo.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFinePeriodo(XMLGregorianCalendar value) {
        this.dataFinePeriodo = value;
    }

    /**
     * Recupera il valore della proprietè prezzoUnitario.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrezzoUnitario() {
        return prezzoUnitario;
    }

    /**
     * Imposta il valore della proprietè prezzoUnitario.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrezzoUnitario(BigDecimal value) {
        this.prezzoUnitario = value;
    }

    /**
     * Gets the value of the scontoMaggiorazione property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the scontoMaggiorazione property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getScontoMaggiorazione().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ScontoMaggiorazioneType }
     * 
     * 
     */
    public List<ScontoMaggiorazioneType> getScontoMaggiorazione() {
        if (scontoMaggiorazione == null) {
            scontoMaggiorazione = new ArrayList<ScontoMaggiorazioneType>();
        }
        return this.scontoMaggiorazione;
    }

    /**
     * Recupera il valore della proprietè prezzoTotale.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPrezzoTotale() {
        return prezzoTotale;
    }

    /**
     * Imposta il valore della proprietè prezzoTotale.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPrezzoTotale(BigDecimal value) {
        this.prezzoTotale = value;
    }

    /**
     * Recupera il valore della proprietè aliquotaIVA.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAliquotaIVA() {
        return aliquotaIVA;
    }

    /**
     * Imposta il valore della proprietè aliquotaIVA.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAliquotaIVA(BigDecimal value) {
        this.aliquotaIVA = value;
    }

    /**
     * Recupera il valore della proprietè ritenuta.
     * 
     * @return
     *     possible object is
     *     {@link RitenutaType }
     *     
     */
    public RitenutaType getRitenuta() {
        return ritenuta;
    }

    /**
     * Imposta il valore della proprietè ritenuta.
     * 
     * @param value
     *     allowed object is
     *     {@link RitenutaType }
     *     
     */
    public void setRitenuta(RitenutaType value) {
        this.ritenuta = value;
    }

    /**
     * Recupera il valore della proprietè natura.
     * 
     * @return
     *     possible object is
     *     {@link NaturaType }
     *     
     */
    public NaturaType getNatura() {
        return natura;
    }

    /**
     * Imposta il valore della proprietè natura.
     * 
     * @param value
     *     allowed object is
     *     {@link NaturaType }
     *     
     */
    public void setNatura(NaturaType value) {
        this.natura = value;
    }

    /**
     * Recupera il valore della proprietè riferimentoAmministrazione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiferimentoAmministrazione() {
        return riferimentoAmministrazione;
    }

    /**
     * Imposta il valore della proprietè riferimentoAmministrazione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiferimentoAmministrazione(String value) {
        this.riferimentoAmministrazione = value;
    }

    /**
     * Gets the value of the altriDatiGestionali property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the altriDatiGestionali property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAltriDatiGestionali().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AltriDatiGestionaliType }
     * 
     * 
     */
    public List<AltriDatiGestionaliType> getAltriDatiGestionali() {
        if (altriDatiGestionali == null) {
            altriDatiGestionali = new ArrayList<AltriDatiGestionaliType>();
        }
        return this.altriDatiGestionali;
    }

}
