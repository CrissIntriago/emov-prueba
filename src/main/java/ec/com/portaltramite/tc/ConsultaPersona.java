
package ec.com.portaltramite.tc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para consultaPersona complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="consultaPersona">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="tipoidentificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroidentificacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoconsulta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "consultaPersona", propOrder = {
    "tipoidentificacion",
    "numeroidentificacion",
    "tipoconsulta",
    "usuario",
    "contrasenia"
})
public class ConsultaPersona {

    protected String tipoidentificacion;
    protected String numeroidentificacion;
    protected String tipoconsulta;
    protected String usuario;
    protected String contrasenia;

    /**
     * Obtiene el valor de la propiedad tipoidentificacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoidentificacion() {
        return tipoidentificacion;
    }

    /**
     * Define el valor de la propiedad tipoidentificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoidentificacion(String value) {
        this.tipoidentificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad numeroidentificacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroidentificacion() {
        return numeroidentificacion;
    }

    /**
     * Define el valor de la propiedad numeroidentificacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroidentificacion(String value) {
        this.numeroidentificacion = value;
    }

    /**
     * Obtiene el valor de la propiedad tipoconsulta.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoconsulta() {
        return tipoconsulta;
    }

    /**
     * Define el valor de la propiedad tipoconsulta.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoconsulta(String value) {
        this.tipoconsulta = value;
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
