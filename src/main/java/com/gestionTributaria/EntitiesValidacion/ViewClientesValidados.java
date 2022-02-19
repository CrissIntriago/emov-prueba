/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.EntitiesValidacion;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "view_clientes_validados")
@XmlRootElement
public class ViewClientesValidados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "id")
    private BigInteger id;
    @Size(max = 250)
    @Column(name = "identificacion")
    private String identificacion;
    @Size(max = 3)
    @Column(name = "ruc")
    private String ruc;
    @Column(name = "tipo_identificacion")
    private BigInteger tipoIdentificacion;
    @Size(max = 250)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    
    @Size(max = 250)
    @Column(name = "direccion")
    private String direccion;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 500)
    @Column(name = "email")
    private String email;
    @Size(max = 250)
    @Column(name = "telefono")
    private String telefono;
    @Size(max = 250)
    @Column(name = "celular")
    private String celular;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "genero")
    private BigInteger genero;
    @Column(name = "discapacidad")
    private Boolean discapacidad;
    @Size(max = 10)
    @Column(name = "num_conadis")
    private String numConadis;
    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "identificacion_generada")
    private Boolean identificacionGenerada;
    @Size(max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "clasificacion_prov")
    private BigInteger clasificacionProv;
    @Column(name = "tipo_prov")
    private BigInteger tipoProv;
    @Column(name = "contribuyente_especial")
    private Boolean contribuyenteEspecial;
    @Size(max = 255)
    @Column(name = "resolucion_sri")
    private String resolucionSri;
    @Size(max = 255)
    @Column(name = "tipo_de_negocio")
    private String tipoDeNegocio;
    @Column(name = "canton")
    private BigInteger canton;
    @Column(name = "porcentaje_discapacidad")
    private Short porcentajeDiscapacidad;
    @Column(name = "enfermedad_catastrofica")
    private Boolean enfermedadCatastrofica;
    @Size(max = 2147483647)
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "es_persona")
    private Boolean esPersona;
    @Size(max = 255)
    @Column(name = "nombre_comercial")
    private String nombreComercial;
    @Size(max = 255)
    @Column(name = "razon_social")
    private String razonSocial;
    @Size(max = 255)
    @Column(name = "titulo_prof")
    private String tituloProf;
    @Size(max = 250)
    @Column(name = "codigo")
    private String codigo;
    @Column(name = "contribuyente_cementerio")
    private Boolean contribuyenteCementerio;
    @Size(max = 250)
    @Column(name = "apellido_casada")
    private String apellidoCasada;
    @Column(name = "interno")
    private Boolean interno;
    @Column(name = "sexo")
    private Boolean sexo;
    @Size(max = 10)
    @Column(name = "tipo_persona")
    private String tipoPersona;
    @Column(name = "validado")
    private Boolean validado;
    @Size(max = 2147483647)
    @Column(name = "contribuyente_cementerio_nombre")
    private String contribuyenteCementerioNombre;
    @Column(name = "tiene_ruc")
    private Boolean tieneRuc;
    @Column(name = "usuario_validador")
    private BigInteger usuarioValidador;
    @Size(max = 500)
    @Column(name = "consolidado")
    private String consolidado;
    @Column(name = "multi_propietario")
    private Boolean multiPropietario;
    @Column(name = "valiador_asignado")
    private BigInteger valiadorAsignado;
    @Column(name = "valid_nodos")
    private Boolean validNodos;
    @Size(max = 2147483647)
    @Column(name = "id_referencias_consolidados")
    private String idReferenciasConsolidados;
    @Column(name = "valid_admin")
    private Boolean validAdmin;
    @Size(max = 2147483647)
    @Column(name = "representante_legal")
    private String representanteLegal;
    @Column(name = "id_representante_legal")
    private BigInteger idRepresentanteLegal;

    public ViewClientesValidados() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public BigInteger getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(BigInteger tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public BigInteger getGenero() {
        return genero;
    }

    public void setGenero(BigInteger genero) {
        this.genero = genero;
    }

    public Boolean getDiscapacidad() {
        return discapacidad;
    }

    public void setDiscapacidad(Boolean discapacidad) {
        this.discapacidad = discapacidad;
    }

    public String getNumConadis() {
        return numConadis;
    }

    public void setNumConadis(String numConadis) {
        this.numConadis = numConadis;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Boolean getIdentificacionGenerada() {
        return identificacionGenerada;
    }

    public void setIdentificacionGenerada(Boolean identificacionGenerada) {
        this.identificacionGenerada = identificacionGenerada;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public BigInteger getClasificacionProv() {
        return clasificacionProv;
    }

    public void setClasificacionProv(BigInteger clasificacionProv) {
        this.clasificacionProv = clasificacionProv;
    }

    public BigInteger getTipoProv() {
        return tipoProv;
    }

    public void setTipoProv(BigInteger tipoProv) {
        this.tipoProv = tipoProv;
    }

    public Boolean getContribuyenteEspecial() {
        return contribuyenteEspecial;
    }

    public void setContribuyenteEspecial(Boolean contribuyenteEspecial) {
        this.contribuyenteEspecial = contribuyenteEspecial;
    }

    public String getResolucionSri() {
        return resolucionSri;
    }

    public void setResolucionSri(String resolucionSri) {
        this.resolucionSri = resolucionSri;
    }

    public String getTipoDeNegocio() {
        return tipoDeNegocio;
    }

    public void setTipoDeNegocio(String tipoDeNegocio) {
        this.tipoDeNegocio = tipoDeNegocio;
    }

    public BigInteger getCanton() {
        return canton;
    }

    public void setCanton(BigInteger canton) {
        this.canton = canton;
    }

    public Short getPorcentajeDiscapacidad() {
        return porcentajeDiscapacidad;
    }

    public void setPorcentajeDiscapacidad(Short porcentajeDiscapacidad) {
        this.porcentajeDiscapacidad = porcentajeDiscapacidad;
    }

    public Boolean getEnfermedadCatastrofica() {
        return enfermedadCatastrofica;
    }

    public void setEnfermedadCatastrofica(Boolean enfermedadCatastrofica) {
        this.enfermedadCatastrofica = enfermedadCatastrofica;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Boolean getEsPersona() {
        return esPersona;
    }

    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTituloProf() {
        return tituloProf;
    }

    public void setTituloProf(String tituloProf) {
        this.tituloProf = tituloProf;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getContribuyenteCementerio() {
        return contribuyenteCementerio;
    }

    public void setContribuyenteCementerio(Boolean contribuyenteCementerio) {
        this.contribuyenteCementerio = contribuyenteCementerio;
    }

    public String getApellidoCasada() {
        return apellidoCasada;
    }

    public void setApellidoCasada(String apellidoCasada) {
        this.apellidoCasada = apellidoCasada;
    }

    public Boolean getInterno() {
        return interno;
    }

    public void setInterno(Boolean interno) {
        this.interno = interno;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public String getContribuyenteCementerioNombre() {
        return contribuyenteCementerioNombre;
    }

    public void setContribuyenteCementerioNombre(String contribuyenteCementerioNombre) {
        this.contribuyenteCementerioNombre = contribuyenteCementerioNombre;
    }

    public Boolean getTieneRuc() {
        return tieneRuc;
    }

    public void setTieneRuc(Boolean tieneRuc) {
        this.tieneRuc = tieneRuc;
    }

    public BigInteger getUsuarioValidador() {
        return usuarioValidador;
    }

    public void setUsuarioValidador(BigInteger usuarioValidador) {
        this.usuarioValidador = usuarioValidador;
    }

    public String getConsolidado() {
        return consolidado;
    }

    public void setConsolidado(String consolidado) {
        this.consolidado = consolidado;
    }

    public Boolean getMultiPropietario() {
        return multiPropietario;
    }

    public void setMultiPropietario(Boolean multiPropietario) {
        this.multiPropietario = multiPropietario;
    }

    public BigInteger getValiadorAsignado() {
        return valiadorAsignado;
    }

    public void setValiadorAsignado(BigInteger valiadorAsignado) {
        this.valiadorAsignado = valiadorAsignado;
    }

    public Boolean getValidNodos() {
        return validNodos;
    }

    public void setValidNodos(Boolean validNodos) {
        this.validNodos = validNodos;
    }

    public String getIdReferenciasConsolidados() {
        return idReferenciasConsolidados;
    }

    public void setIdReferenciasConsolidados(String idReferenciasConsolidados) {
        this.idReferenciasConsolidados = idReferenciasConsolidados;
    }

    public Boolean getValidAdmin() {
        return validAdmin;
    }

    public void setValidAdmin(Boolean validAdmin) {
        this.validAdmin = validAdmin;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public BigInteger getIdRepresentanteLegal() {
        return idRepresentanteLegal;
    }

    public void setIdRepresentanteLegal(BigInteger idRepresentanteLegal) {
        this.idRepresentanteLegal = idRepresentanteLegal;
    }
    
}
