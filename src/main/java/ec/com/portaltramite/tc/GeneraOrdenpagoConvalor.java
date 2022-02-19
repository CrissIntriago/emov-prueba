
package ec.com.portaltramite.tc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para generaOrdenpagoConvalor complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="generaOrdenpagoConvalor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="identificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigoservicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subtotal" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="iva" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="alicuota" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="total" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="usuario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="contrasenia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "generaOrdenpagoConvalor", propOrder = {
    "identificacion",
    "codigoservicio",
    "subtotal",
    "iva",
    "alicuota",
    "total",
    "usuario",
    "contrasenia"
})
public class GeneraOrdenpagoConvalor {

    protected String identificacion;
    protected String codigoservicio;
    protected String subtotal;
    protected String iva;
    protected String alicuota;
    protected String total;
    protected String usuario;
    protected String contrasenia;

    /**
     * Obtiene el valor de la propiedad identificacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentificacion() {
        return identificacion;
    }

    /**
     * Define el valor de la propiedad identificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentificacion(String value) {
        this.identificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad codigoservicio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigoservicio() {
        return codigoservicio;
    }

    /**
     * Define el valor de la propiedad codigoservicio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigoservicio(String value) {
        this.codigoservicio = value;
    }

    /**
     * Obtiene el valor de la propiedad subtotal.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubtotal() {
        return subtotal;
    }

    /**
     * Define el valor de la propiedad subtotal.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubtotal(String value) {
        this.subtotal = value;
    }

    /**
     * Obtiene el valor de la propiedad iva.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIva() {
        return iva;
    }

    /**
     * Define el valor de la propiedad iva.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIva(String value) {
        this.iva = value;
    }

    /**
     * Obtiene el valor de la propiedad alicuota.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlicuota() {
        return alicuota;
    }

    /**
     * Define el valor de la propiedad alicuota.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlicuota(String value) {
        this.alicuota = value;
    }

    /**
     * Obtiene el valor de la propiedad total.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotal() {
        return total;
    }

    /**
     * Define el valor de la propiedad total.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotal(String value) {
        this.total = value;
    }

    /**
     * Obtiene el valor de la propiedad usuario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * Define el valor de la propiedad usuario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuario(String value) {
        this.usuario = value;
    }

    /**
     * Obtiene el valor de la propiedad contrasenia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * Define el valor de la propiedad contrasenia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContrasenia(String value) {
        this.contrasenia = value;
    }

}
