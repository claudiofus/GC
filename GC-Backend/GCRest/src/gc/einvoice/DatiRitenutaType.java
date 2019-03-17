//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
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
 * <p>Classe Java per DatiRitenutaType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiRitenutaType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoRitenuta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}TipoRitenutaType"/>
 *         &lt;element name="ImportoRitenuta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount2DecimalType"/>
 *         &lt;element name="AliquotaRitenuta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RateType"/>
 *         &lt;element name="CausalePagamento" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}CausalePagamentoType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiRitenutaType", propOrder = {
    "tipoRitenuta",
    "importoRitenuta",
    "aliquotaRitenuta",
    "causalePagamento"
})
public class DatiRitenutaType {

    @XmlElement(name = "TipoRitenuta", required = true)
    @XmlSchemaType(name = "string")
    protected TipoRitenutaType tipoRitenuta;
    @XmlElement(name = "ImportoRitenuta", required = true)
    protected BigDecimal importoRitenuta;
    @XmlElement(name = "AliquotaRitenuta", required = true)
    protected BigDecimal aliquotaRitenuta;
    @XmlElement(name = "CausalePagamento", required = true)
    @XmlSchemaType(name = "string")
    protected CausalePagamentoType causalePagamento;

    /**
     * Recupera il valore della proprietà tipoRitenuta.
     * 
     * @return
     *     possible object is
     *     {@link TipoRitenutaType }
     *     
     */
    public TipoRitenutaType getTipoRitenuta() {
        return tipoRitenuta;
    }

    /**
     * Imposta il valore della proprietà tipoRitenuta.
     * 
     * @param value
     *     allowed object is
     *     {@link TipoRitenutaType }
     *     
     */
    public void setTipoRitenuta(TipoRitenutaType value) {
        this.tipoRitenuta = value;
    }

    /**
     * Recupera il valore della proprietà importoRitenuta.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImportoRitenuta() {
        return importoRitenuta;
    }

    /**
     * Imposta il valore della proprietà importoRitenuta.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImportoRitenuta(BigDecimal value) {
        this.importoRitenuta = value;
    }

    /**
     * Recupera il valore della proprietà aliquotaRitenuta.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAliquotaRitenuta() {
        return aliquotaRitenuta;
    }

    /**
     * Imposta il valore della proprietà aliquotaRitenuta.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAliquotaRitenuta(BigDecimal value) {
        this.aliquotaRitenuta = value;
    }

    /**
     * Recupera il valore della proprietà causalePagamento.
     * 
     * @return
     *     possible object is
     *     {@link CausalePagamentoType }
     *     
     */
    public CausalePagamentoType getCausalePagamento() {
        return causalePagamento;
    }

    /**
     * Imposta il valore della proprietà causalePagamento.
     * 
     * @param value
     *     allowed object is
     *     {@link CausalePagamentoType }
     *     
     */
    public void setCausalePagamento(CausalePagamentoType value) {
        this.causalePagamento = value;
    }

}
