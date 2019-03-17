//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per FatturaElettronicaHeaderType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FatturaElettronicaHeaderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatiTrasmissione" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}DatiTrasmissioneType"/>
 *         &lt;element name="CedentePrestatore" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}CedentePrestatoreType"/>
 *         &lt;element name="RappresentanteFiscale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RappresentanteFiscaleType" minOccurs="0"/>
 *         &lt;element name="CessionarioCommittente" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}CessionarioCommittenteType"/>
 *         &lt;element name="TerzoIntermediarioOSoggettoEmittente" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}TerzoIntermediarioSoggettoEmittenteType" minOccurs="0"/>
 *         &lt;element name="SoggettoEmittente" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}SoggettoEmittenteType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FatturaElettronicaHeaderType", propOrder = {
    "datiTrasmissione",
    "cedentePrestatore",
    "rappresentanteFiscale",
    "cessionarioCommittente",
    "terzoIntermediarioOSoggettoEmittente",
    "soggettoEmittente"
})
public class FatturaElettronicaHeaderType {

    @XmlElement(name = "DatiTrasmissione", required = true)
    protected DatiTrasmissioneType datiTrasmissione;
    @XmlElement(name = "CedentePrestatore", required = true)
    protected CedentePrestatoreType cedentePrestatore;
    @XmlElement(name = "RappresentanteFiscale")
    protected RappresentanteFiscaleType rappresentanteFiscale;
    @XmlElement(name = "CessionarioCommittente", required = true)
    protected CessionarioCommittenteType cessionarioCommittente;
    @XmlElement(name = "TerzoIntermediarioOSoggettoEmittente")
    protected TerzoIntermediarioSoggettoEmittenteType terzoIntermediarioOSoggettoEmittente;
    @XmlElement(name = "SoggettoEmittente")
    @XmlSchemaType(name = "string")
    protected SoggettoEmittenteType soggettoEmittente;

    /**
     * Recupera il valore della proprietà datiTrasmissione.
     * 
     * @return
     *     possible object is
     *     {@link DatiTrasmissioneType }
     *     
     */
    public DatiTrasmissioneType getDatiTrasmissione() {
        return datiTrasmissione;
    }

    /**
     * Imposta il valore della proprietà datiTrasmissione.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiTrasmissioneType }
     *     
     */
    public void setDatiTrasmissione(DatiTrasmissioneType value) {
        this.datiTrasmissione = value;
    }

    /**
     * Recupera il valore della proprietà cedentePrestatore.
     * 
     * @return
     *     possible object is
     *     {@link CedentePrestatoreType }
     *     
     */
    public CedentePrestatoreType getCedentePrestatore() {
        return cedentePrestatore;
    }

    /**
     * Imposta il valore della proprietà cedentePrestatore.
     * 
     * @param value
     *     allowed object is
     *     {@link CedentePrestatoreType }
     *     
     */
    public void setCedentePrestatore(CedentePrestatoreType value) {
        this.cedentePrestatore = value;
    }

    /**
     * Recupera il valore della proprietà rappresentanteFiscale.
     * 
     * @return
     *     possible object is
     *     {@link RappresentanteFiscaleType }
     *     
     */
    public RappresentanteFiscaleType getRappresentanteFiscale() {
        return rappresentanteFiscale;
    }

    /**
     * Imposta il valore della proprietà rappresentanteFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link RappresentanteFiscaleType }
     *     
     */
    public void setRappresentanteFiscale(RappresentanteFiscaleType value) {
        this.rappresentanteFiscale = value;
    }

    /**
     * Recupera il valore della proprietà cessionarioCommittente.
     * 
     * @return
     *     possible object is
     *     {@link CessionarioCommittenteType }
     *     
     */
    public CessionarioCommittenteType getCessionarioCommittente() {
        return cessionarioCommittente;
    }

    /**
     * Imposta il valore della proprietà cessionarioCommittente.
     * 
     * @param value
     *     allowed object is
     *     {@link CessionarioCommittenteType }
     *     
     */
    public void setCessionarioCommittente(CessionarioCommittenteType value) {
        this.cessionarioCommittente = value;
    }

    /**
     * Recupera il valore della proprietà terzoIntermediarioOSoggettoEmittente.
     * 
     * @return
     *     possible object is
     *     {@link TerzoIntermediarioSoggettoEmittenteType }
     *     
     */
    public TerzoIntermediarioSoggettoEmittenteType getTerzoIntermediarioOSoggettoEmittente() {
        return terzoIntermediarioOSoggettoEmittente;
    }

    /**
     * Imposta il valore della proprietà terzoIntermediarioOSoggettoEmittente.
     * 
     * @param value
     *     allowed object is
     *     {@link TerzoIntermediarioSoggettoEmittenteType }
     *     
     */
    public void setTerzoIntermediarioOSoggettoEmittente(TerzoIntermediarioSoggettoEmittenteType value) {
        this.terzoIntermediarioOSoggettoEmittente = value;
    }

    /**
     * Recupera il valore della proprietà soggettoEmittente.
     * 
     * @return
     *     possible object is
     *     {@link SoggettoEmittenteType }
     *     
     */
    public SoggettoEmittenteType getSoggettoEmittente() {
        return soggettoEmittente;
    }

    /**
     * Imposta il valore della proprietà soggettoEmittente.
     * 
     * @param value
     *     allowed object is
     *     {@link SoggettoEmittenteType }
     *     
     */
    public void setSoggettoEmittente(SoggettoEmittenteType value) {
        this.soggettoEmittente = value;
    }

}
