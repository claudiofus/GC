//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrè persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per X509IssuerSerialType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="X509IssuerSerialType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="X509IssuerName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="X509SerialNumber" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "X509IssuerSerialType", namespace = "http://www.w3.org/2000/09/xmldsig#", propOrder = {
    "x509IssuerName",
    "x509SerialNumber"
})
public class X509IssuerSerialType {

    @XmlElement(name = "X509IssuerName", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected String x509IssuerName;
    @XmlElement(name = "X509SerialNumber", namespace = "http://www.w3.org/2000/09/xmldsig#", required = true)
    protected BigInteger x509SerialNumber;

    /**
     * Recupera il valore della proprietè x509IssuerName.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getX509IssuerName() {
        return x509IssuerName;
    }

    /**
     * Imposta il valore della proprietè x509IssuerName.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setX509IssuerName(String value) {
        this.x509IssuerName = value;
    }

    /**
     * Recupera il valore della proprietè x509SerialNumber.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getX509SerialNumber() {
        return x509SerialNumber;
    }

    /**
     * Imposta il valore della proprietè x509SerialNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setX509SerialNumber(BigInteger value) {
        this.x509SerialNumber = value;
    }

}
