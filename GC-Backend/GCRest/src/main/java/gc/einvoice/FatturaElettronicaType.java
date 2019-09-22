//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrè persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per FatturaElettronicaType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="FatturaElettronicaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="FatturaElettronicaHeader" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}FatturaElettronicaHeaderType"/>
 *         &lt;element name="FatturaElettronicaBody" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}FatturaElettronicaBodyType" maxOccurs="unbounded"/>
 *         &lt;element ref="{http://www.w3.org/2000/09/xmldsig#}Signature" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="versione" use="required" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}FormatoTrasmissioneType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FatturaElettronicaType", propOrder = {
    "fatturaElettronicaHeader",
    "fatturaElettronicaBody",
    "signature"
})
public class FatturaElettronicaType {

    @XmlElement(name = "FatturaElettronicaHeader", required = true)
    protected FatturaElettronicaHeaderType fatturaElettronicaHeader;
    @XmlElement(name = "FatturaElettronicaBody", required = true)
    protected List<FatturaElettronicaBodyType> fatturaElettronicaBody;
    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;
    @XmlAttribute(name = "versione", required = true)
    protected FormatoTrasmissioneType versione;

    /**
     * Recupera il valore della proprietè fatturaElettronicaHeader.
     * 
     * @return
     *     possible object is
     *     {@link FatturaElettronicaHeaderType }
     *     
     */
    public FatturaElettronicaHeaderType getFatturaElettronicaHeader() {
        return fatturaElettronicaHeader;
    }

    /**
     * Imposta il valore della proprietè fatturaElettronicaHeader.
     * 
     * @param value
     *     allowed object is
     *     {@link FatturaElettronicaHeaderType }
     *     
     */
    public void setFatturaElettronicaHeader(FatturaElettronicaHeaderType value) {
        this.fatturaElettronicaHeader = value;
    }

    /**
     * Gets the value of the fatturaElettronicaBody property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fatturaElettronicaBody property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFatturaElettronicaBody().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FatturaElettronicaBodyType }
     * 
     * 
     */
    public List<FatturaElettronicaBodyType> getFatturaElettronicaBody() {
        if (fatturaElettronicaBody == null) {
            fatturaElettronicaBody = new ArrayList<FatturaElettronicaBodyType>();
        }
        return this.fatturaElettronicaBody;
    }

    /**
     * Recupera il valore della proprietè signature.
     * 
     * @return
     *     possible object is
     *     {@link SignatureType }
     *     
     */
    public SignatureType getSignature() {
        return signature;
    }

    /**
     * Imposta il valore della proprietè signature.
     * 
     * @param value
     *     allowed object is
     *     {@link SignatureType }
     *     
     */
    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    /**
     * Recupera il valore della proprietè versione.
     * 
     * @return
     *     possible object is
     *     {@link FormatoTrasmissioneType }
     *     
     */
    public FormatoTrasmissioneType getVersione() {
        return versione;
    }

    /**
     * Imposta il valore della proprietè versione.
     * 
     * @param value
     *     allowed object is
     *     {@link FormatoTrasmissioneType }
     *     
     */
    public void setVersione(FormatoTrasmissioneType value) {
        this.versione = value;
    }

}
