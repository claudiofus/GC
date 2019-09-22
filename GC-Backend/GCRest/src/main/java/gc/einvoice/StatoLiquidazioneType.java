//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per StatoLiquidazioneType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="StatoLiquidazioneType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="LS"/>
 *     &lt;enumeration value="LN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "StatoLiquidazioneType")
@XmlEnum
public enum StatoLiquidazioneType {


    /**
     * in liquidazione
     * 
     */
    LS,

    /**
     * non in liquidazione
     * 
     */
    LN;

    public String value() {
        return name();
    }

    public static StatoLiquidazioneType fromValue(String v) {
        return valueOf(v);
    }

}
