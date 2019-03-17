//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2019.01.07 alle 04:02:04 PM CET 
//


package gc.einvoice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ModalitaPagamentoType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="ModalitaPagamentoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;length value="4"/>
 *     &lt;enumeration value="MP01"/>
 *     &lt;enumeration value="MP02"/>
 *     &lt;enumeration value="MP03"/>
 *     &lt;enumeration value="MP04"/>
 *     &lt;enumeration value="MP05"/>
 *     &lt;enumeration value="MP06"/>
 *     &lt;enumeration value="MP07"/>
 *     &lt;enumeration value="MP08"/>
 *     &lt;enumeration value="MP09"/>
 *     &lt;enumeration value="MP10"/>
 *     &lt;enumeration value="MP11"/>
 *     &lt;enumeration value="MP12"/>
 *     &lt;enumeration value="MP13"/>
 *     &lt;enumeration value="MP14"/>
 *     &lt;enumeration value="MP15"/>
 *     &lt;enumeration value="MP16"/>
 *     &lt;enumeration value="MP17"/>
 *     &lt;enumeration value="MP18"/>
 *     &lt;enumeration value="MP19"/>
 *     &lt;enumeration value="MP20"/>
 *     &lt;enumeration value="MP21"/>
 *     &lt;enumeration value="MP22"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ModalitaPagamentoType")
@XmlEnum
public enum ModalitaPagamentoType {


    /**
     * contanti
     * 
     */
    @XmlEnumValue("MP01")
    MP_01("MP01"),

    /**
     * assegno
     * 
     */
    @XmlEnumValue("MP02")
    MP_02("MP02"),

    /**
     * assegno circolare
     * 
     */
    @XmlEnumValue("MP03")
    MP_03("MP03"),

    /**
     * contanti presso Tesoreria
     * 
     */
    @XmlEnumValue("MP04")
    MP_04("MP04"),

    /**
     * bonifico
     * 
     */
    @XmlEnumValue("MP05")
    MP_05("MP05"),

    /**
     * vaglia cambiario
     * 
     */
    @XmlEnumValue("MP06")
    MP_06("MP06"),

    /**
     * bollettino bancario
     * 
     */
    @XmlEnumValue("MP07")
    MP_07("MP07"),

    /**
     * carta di pagamento
     * 
     */
    @XmlEnumValue("MP08")
    MP_08("MP08"),

    /**
     * RID
     * 
     */
    @XmlEnumValue("MP09")
    MP_09("MP09"),

    /**
     * RID utenze
     * 
     */
    @XmlEnumValue("MP10")
    MP_10("MP10"),

    /**
     * RID veloce
     * 
     */
    @XmlEnumValue("MP11")
    MP_11("MP11"),

    /**
     * RIBA
     * 
     */
    @XmlEnumValue("MP12")
    MP_12("MP12"),

    /**
     * MAV
     * 
     */
    @XmlEnumValue("MP13")
    MP_13("MP13"),

    /**
     * quietanza erario
     * 
     */
    @XmlEnumValue("MP14")
    MP_14("MP14"),

    /**
     * giroconto su conti di contabilità speciale
     * 					
     * 
     */
    @XmlEnumValue("MP15")
    MP_15("MP15"),

    /**
     * domiciliazione bancaria
     * 
     */
    @XmlEnumValue("MP16")
    MP_16("MP16"),

    /**
     * domiciliazione postale
     * 
     */
    @XmlEnumValue("MP17")
    MP_17("MP17"),

    /**
     * bollettino di c/c postale
     * 
     */
    @XmlEnumValue("MP18")
    MP_18("MP18"),

    /**
     * SEPA Direct Debit
     * 
     */
    @XmlEnumValue("MP19")
    MP_19("MP19"),

    /**
     * SEPA Direct Debit CORE
     * 
     */
    @XmlEnumValue("MP20")
    MP_20("MP20"),

    /**
     * SEPA Direct Debit B2B
     * 
     */
    @XmlEnumValue("MP21")
    MP_21("MP21"),

    /**
     * Trattenuta su somme già riscosse
     * 					
     * 
     */
    @XmlEnumValue("MP22")
    MP_22("MP22");
    private final String value;

    ModalitaPagamentoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ModalitaPagamentoType fromValue(String v) {
        for (ModalitaPagamentoType c: ModalitaPagamentoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
