
package ec.com.portaltramite.tc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para confirmaReservaturno complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="confirmaReservaturno">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idsolicitud" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="turno" type="{http://www.w3.org/2001/XMLSchema}long"/>
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
@XmlType(name = "confirmaReservaturno", propOrder = {
    "idsolicitud",
    "turno",
    "usuario",
    "contrasenia"
})
public class ConfirmaReservaturno {

    protected long idsolicitud;
    protected long turno;
    protected String usuario;
    protected String contrasenia;

    /**
     * Obtiene el valor de la propiedad idsolicitud.
     * 
     */
    public long getIdsolicitud() {
        return idsolicitud;
    }

    /**
     * Define el valor de la propiedad idsolicitud.
     * 
     */
    public void setIdsolicitud(long value) {
        this.idsolicitud = value;
    }

    /**
     * Obtiene el valor de la propiedad turno.
     * 
     */
    public long getTurno() {
        return turno;
    }

    /**
     * Define el valor de la propiedad turno.
     * 
     */
    public void setTurno(long value) {
        this.turno = value;
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
