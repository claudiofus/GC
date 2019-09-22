//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrè persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per DatiAnagraficiTerzoIntermediarioType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiAnagraficiTerzoIntermediarioType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IdFiscaleIVA" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}IdFiscaleType" minOccurs="0"/>
 *         &lt;element name="CodiceFiscale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}CodiceFiscaleType" minOccurs="0"/>
 *         &lt;element name="Anagrafica" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}AnagraficaType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiAnagraficiTerzoIntermediarioType", propOrder = {
    "idFiscaleIVA",
    "codiceFiscale",
    "anagrafica"
})
public class DatiAnagraficiTerzoIntermediarioType {

    @XmlElement(name = "IdFiscaleIVA")
    protected IdFiscaleType idFiscaleIVA;
    @XmlElement(name = "CodiceFiscale")
    protected String codiceFiscale;
    @XmlElement(name = "Anagrafica", required = true)
    protected AnagraficaType anagrafica;

    /**
     * Recupera il valore della proprietè idFiscaleIVA.
     * 
     * @return
     *     possible object is
     *     {@link IdFiscaleType }
     *     
     */
    public IdFiscaleType getIdFiscaleIVA() {
        return idFiscaleIVA;
    }

    /**
     * Imposta il valore della proprietè idFiscaleIVA.
     * 
     * @param value
     *     allowed object is
     *     {@link IdFiscaleType }
     *     
     */
    public void setIdFiscaleIVA(IdFiscaleType value) {
        this.idFiscaleIVA = value;
    }

    /**
     * Recupera il valore della proprietè codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietè codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietè anagrafica.
     * 
     * @return
     *     possible object is
     *     {@link AnagraficaType }
     *     
     */
    public AnagraficaType getAnagrafica() {
        return anagrafica;
    }

    /**
     * Imposta il valore della proprietè anagrafica.
     * 
     * @param value
     *     allowed object is
     *     {@link AnagraficaType }
     *     
     */
    public void setAnagrafica(AnagraficaType value) {
        this.anagrafica = value;
    }

}
