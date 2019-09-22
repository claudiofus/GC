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
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Classe Java per DatiRiepilogoType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiRiepilogoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AliquotaIVA" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}RateType"/>
 *         &lt;element name="Natura" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}NaturaType" minOccurs="0"/>
 *         &lt;element name="SpeseAccessorie" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount2DecimalType" minOccurs="0"/>
 *         &lt;element name="Arrotondamento" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount8DecimalType" minOccurs="0"/>
 *         &lt;element name="ImponibileImporto" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount2DecimalType"/>
 *         &lt;element name="Imposta" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}Amount2DecimalType"/>
 *         &lt;element name="EsigibilitaIVA" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}EsigibilitaIVAType" minOccurs="0"/>
 *         &lt;element name="RiferimentoNormativo" type="{http://ivaservizi.agenziaentrate.gov.it/docs/xsd/fatture/v1.2}String100LatinType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiRiepilogoType", propOrder = {
    "aliquotaIVA",
    "natura",
    "speseAccessorie",
    "arrotondamento",
    "imponibileImporto",
    "imposta",
    "esigibilitaIVA",
    "riferimentoNormativo"
})
public class DatiRiepilogoType {

    @XmlElement(name = "AliquotaIVA", required = true)
    protected BigDecimal aliquotaIVA;
    @XmlElement(name = "Natura")
    @XmlSchemaType(name = "string")
    protected NaturaType natura;
    @XmlElement(name = "SpeseAccessorie")
    protected BigDecimal speseAccessorie;
    @XmlElement(name = "Arrotondamento")
    protected BigDecimal arrotondamento;
    @XmlElement(name = "ImponibileImporto", required = true)
    protected BigDecimal imponibileImporto;
    @XmlElement(name = "Imposta", required = true)
    protected BigDecimal imposta;
    @XmlElement(name = "EsigibilitaIVA")
    @XmlSchemaType(name = "string")
    protected EsigibilitaIVAType esigibilitaIVA;
    @XmlElement(name = "RiferimentoNormativo")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    @XmlSchemaType(name = "normalizedString")
    protected String riferimentoNormativo;

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
     * Recupera il valore della proprietè speseAccessorie.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSpeseAccessorie() {
        return speseAccessorie;
    }

    /**
     * Imposta il valore della proprietè speseAccessorie.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSpeseAccessorie(BigDecimal value) {
        this.speseAccessorie = value;
    }

    /**
     * Recupera il valore della proprietè arrotondamento.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getArrotondamento() {
        return arrotondamento;
    }

    /**
     * Imposta il valore della proprietè arrotondamento.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setArrotondamento(BigDecimal value) {
        this.arrotondamento = value;
    }

    /**
     * Recupera il valore della proprietè imponibileImporto.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImponibileImporto() {
        return imponibileImporto;
    }

    /**
     * Imposta il valore della proprietè imponibileImporto.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImponibileImporto(BigDecimal value) {
        this.imponibileImporto = value;
    }

    /**
     * Recupera il valore della proprietè imposta.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImposta() {
        return imposta;
    }

    /**
     * Imposta il valore della proprietè imposta.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImposta(BigDecimal value) {
        this.imposta = value;
    }

    /**
     * Recupera il valore della proprietè esigibilitaIVA.
     * 
     * @return
     *     possible object is
     *     {@link EsigibilitaIVAType }
     *     
     */
    public EsigibilitaIVAType getEsigibilitaIVA() {
        return esigibilitaIVA;
    }

    /**
     * Imposta il valore della proprietè esigibilitaIVA.
     * 
     * @param value
     *     allowed object is
     *     {@link EsigibilitaIVAType }
     *     
     */
    public void setEsigibilitaIVA(EsigibilitaIVAType value) {
        this.esigibilitaIVA = value;
    }

    /**
     * Recupera il valore della proprietè riferimentoNormativo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiferimentoNormativo() {
        return riferimentoNormativo;
    }

    /**
     * Imposta il valore della proprietè riferimentoNormativo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiferimentoNormativo(String value) {
        this.riferimentoNormativo = value;
    }

}
