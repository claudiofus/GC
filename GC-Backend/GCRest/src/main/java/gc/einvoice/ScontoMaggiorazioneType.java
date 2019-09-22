//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrè persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ScontoMaggiorazioneType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ScontoMaggiorazioneType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Tipo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}TipoScontoMaggiorazioneType"/>
 *         &lt;element name="Percentuale" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RateType" minOccurs="0"/>
 *         &lt;element name="Importo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount2DecimalType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ScontoMaggiorazioneType", propOrder = {
    "tipo",
    "percentuale",
    "importo"
})
public class ScontoMaggiorazioneType {

    @XmlElement(name = "Tipo", required = true)
    @XmlSchemaType(name = "string")
    protected TipoScontoMaggiorazioneType tipo;
    @XmlElement(name = "Percentuale")
    protected BigDecimal percentuale;
    @XmlElement(name = "Importo")
    protected BigDecimal importo;

    /**
     * Recupera il valore della proprietè tipo.
     * 
     * @return
     *     possible object is
     *     {@link TipoScontoMaggiorazioneType }
     *     
     */
    public TipoScontoMaggiorazioneType getTipo() {
        return tipo;
    }

    /**
     * Imposta il valore della proprietè tipo.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoScontoMaggiorazioneType }
     *     
     */
    public void setTipo(TipoScontoMaggiorazioneType value) {
        this.tipo = value;
    }

    /**
     * Recupera il valore della proprietè percentuale.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPercentuale() {
        return percentuale;
    }

    /**
     * Imposta il valore della proprietè percentuale.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPercentuale(BigDecimal value) {
        this.percentuale = value;
    }

    /**
     * Recupera il valore della proprietè importo.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImporto() {
        return importo;
    }

    /**
     * Imposta il valore della proprietè importo.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImporto(BigDecimal value) {
        this.importo = value;
    }

}
