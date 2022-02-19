
package ec.com.portaltramite.tc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para finalizaSolicitudXReferencia complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="finalizaSolicitudXReferencia">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="codigoservicio" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tipoconsulta" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="codigosolicitud" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="comentario" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "finalizaSolicitudXReferencia", propOrder = {
    "codigoservicio",
    "tipoconsulta",
    "codigosolicitud",
    "comentario",
    "usuario",
    "contrasenia"
})
public class FinalizaSolicitudXReferencia {

    protected String codigoservicio;
    protected String tipoconsulta;
    protected String codigosolicitud;
    protected String comentario;
    protected String usuario;
    protected String contrasenia;

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
     * Obtiene el valor de la propiedad codigosolicitud.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodigosolicitud() {
        return codigosolicitud;
    }

    /**
     * Define el valor de la propiedad codigosolicitud.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodigosolicitud(String value) {
        this.codigosolicitud = value;
    }

    /**
     * Obtiene el valor de la propiedad comentario.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Define el valor de la propiedad comentario.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComentario(String value) {
        this.comentario = value;
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
