/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.asgard.Entity.FinaRenLocalComercial;
import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.SaldoDebeHaberDTO;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Where;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "cliente")
@SqlResultSetMapping(name = "clienteMapping",
        classes = @ConstructorResult(targetClass = SaldoDebeHaberDTO.class,
                columns = {
                    @ColumnResult(name = "id", type = Long.class),
                    @ColumnResult(name = "identificacion", type = String.class),
                    @ColumnResult(name = "ruc", type = String.class),
                    @ColumnResult(name = "nombre", type = String.class),
                    @ColumnResult(name = "apellido", type = String.class),
                    @ColumnResult(name = "email", type = String.class)
                })
)
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c"),
    @NamedQuery(name = "Cliente.findById", query = "SELECT c FROM Cliente c WHERE c.id = ?1"),
    @NamedQuery(name = "Cliente.findByClienteCedulaRucOtros", query = "SELECT c FROM Cliente c WHERE c.identificacion = ?1 AND c.estado = true AND c.tipoIdentificacion = ?2"),
    @NamedQuery(name = "Cliente.findByIdCliente", query = "SELECT c FROM Cliente c WHERE c.identificacion = ?1 AND c.estado= TRUE AND c.tipoIdentificacion=?2"),
    @NamedQuery(name = "Cliente.findByIdCliente_2", query = "SELECT c FROM Cliente c WHERE c.identificacion = ?1 AND c.estado= TRUE AND c.tipoIdentificacion=?2"),
    @NamedQuery(name = "Cliente.findByIdCliente_3", query = "SELECT c FROM Cliente c WHERE c.identificacion = ?1 AND c.estado= TRUE"),
    @NamedQuery(name = "Cliente.findByIdentificacion", query = "SELECT c FROM Cliente c WHERE c.identificacion = :identificacion"),
    @NamedQuery(name = "Cliente.findByRuc", query = "SELECT c FROM Cliente c WHERE c.ruc = :ruc"),
    @NamedQuery(name = "Cliente.findByNombre", query = "SELECT c FROM Cliente c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "Cliente.findByApellido", query = "SELECT c FROM Cliente c WHERE c.apellido = :apellido"),
    @NamedQuery(name = "Cliente.findByDireccion", query = "SELECT c FROM Cliente c WHERE c.direccion = :direccion"),
    @NamedQuery(name = "Cliente.findByEmail", query = "SELECT c FROM Cliente c WHERE c.email = :email"),
    @NamedQuery(name = "Cliente.findByTelefono", query = "SELECT c FROM Cliente c WHERE c.telefono = :telefono"),
    @NamedQuery(name = "Cliente.findByCelular", query = "SELECT c FROM Cliente c WHERE c.celular = :celular"),
    @NamedQuery(name = "Cliente.findByFechaNacimiento", query = "SELECT c FROM Cliente c WHERE c.fechaNacimiento = :fechaNacimiento"),
    @NamedQuery(name = "Cliente.findByDiscapacidad", query = "SELECT c FROM Cliente c WHERE c.discapacidad = :discapacidad"),
    @NamedQuery(name = "Cliente.findByNumConadis", query = "SELECT c FROM Cliente c WHERE c.numConadis = :numConadis"),
    @NamedQuery(name = "Cliente.findByEstado", query = "SELECT c FROM Cliente c WHERE c.estado = :estado"),
    @NamedQuery(name = "Cliente.findByIdentificacionGenerada", query = "SELECT c FROM Cliente c WHERE c.identificacionGenerada = :identificacionGenerada"),
    @NamedQuery(name = "Cliente.findByUsuarioCreacion", query = "SELECT c FROM Cliente c WHERE c.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "Cliente.findByFechaCreacion", query = "SELECT c FROM Cliente c WHERE c.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Cliente.findByUsuarioModificacion", query = "SELECT c FROM Cliente c WHERE c.usuarioModificacion = :usuarioModificacion"),
    @NamedQuery(name = "Cliente.findByFechaModificacion", query = "SELECT c FROM Cliente c WHERE c.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Cliente.findByContribuyenteEspecial", query = "SELECT c FROM Cliente c WHERE c.contribuyenteEspecial = :contribuyenteEspecial"),
    @NamedQuery(name = "Cliente.findByResolucionSri", query = "SELECT c FROM Cliente c WHERE c.resolucionSri = :resolucionSri"),
    @NamedQuery(name = "Cliente.findByTipoDeNegocio", query = "SELECT c FROM Cliente c WHERE c.tipoDeNegocio = :tipoDeNegocio")})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "identificacion")
    private String identificacion;
    @Column(name = "ruc")
    private String ruc;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellido")
    /**
     * Nombre comercial del ruc
     */
    private String apellido;

    @Column(name = "razon_social")
    private String razonSocial;
    @Column(name = "nombre_comercial")
    private String nombreComercial;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "email")
    private String email;
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "celular")
    private String celular;
    @Column(name = "fecha_nacimiento")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    @Column(name = "discapacidad")
    private Boolean discapacidad;
    @Column(name = "num_conadis")
    private String numConadis;
    @Basic(optional = false)
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "identificacion_generada")
    private Boolean identificacionGenerada;
    @Basic(optional = false)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Basic(optional = false)
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "usuario_modificacion")
    private String usuarioModificacion;
    @Column(name = "estado_civil")
    private String estadoCivil;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "contribuyente_especial")
    private Boolean contribuyenteEspecial;
    @Column(name = "resolucion_sri")
    private String resolucionSri;
    @Column(name = "tipo_de_negocio")
    private String tipoDeNegocio;
    @JoinColumn(name = "canton", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Canton canton;
    @JoinColumn(name = "clasificacion_prov", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem clasificacionProv;
    @JoinColumn(name = "tipo_prov", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoProv;
    @JoinColumn(name = "genero", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem genero;
    @JoinColumn(name = "tipo_identificacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoIdentificacion;
    @OneToMany(mappedBy = "contacto")
    @GsonExcludeField
    private List<Proveedor> proveedorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente")
    @GsonExcludeField
    private List<Proveedor> proveedorList1;
    @Column(name = "validado")
    private Boolean validado;
    @Column(name = "es_persona")
    private Boolean esPersona;
    @Column(name = "tiene_ruc")
    private Boolean tieneRuc;
    @Formula("(select (case when ci.codigo = 'RUC' then concat(c.identificacion, c.ruc) else c.identificacion end) from public.cliente c inner join catalogo_item ci on c.tipo_identificacion = ci.id where c.id=id)")
    private String identificacionCompleta;
    @Column(name = "multi_propietario")
    private Boolean multiPropietario;
    @Formula("(select (case when ci.codigo = 'RUC' then c.nombre else concat(c.apellido,' ',c.nombre) end) from public.cliente c inner join catalogo_item ci on c.tipo_identificacion = ci.id where c.id=id)")
    private String nombreCompleltoSql;
    @Column(name = "tipo_persona")
    private String tipoPersona;
    @Column(name = "valid_nodos")
    private Boolean validNodos;
    @JoinColumn(name = "usuario_validador", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Usuarios usuarioValidador;
    @JoinColumn(name = "valiador_asignado", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Usuarios valiadorAsignado;
    @Formula("coalesce(nombre,'')||' '||coalesce(apellido,'')")
    private String nombreCompleto;
    @Column(name = "porcentaje_discapacidad")
    private Short porcentajeDiscapacidad;
    @Column(name = "enfermedad_catastrofica")
    private Boolean enfermedadCatastrofica;
    @Column(name = "sexo")
    private Boolean sexo;
    @Column(name = "valid_admin")
    private Boolean validAdmin;
    @Column(name = "condicion_ciudadano")
    private String condicionCiudadano;
    @Column(name = "nuevo")
    @Transient
    private Boolean nuevo = false;
    @Column(name = "consolidado")
    private String consolidado;
    @Column(name = "id_referencias_consolidados")
    private String idReferenciasConsolidados;
    @Transient
    private String nombreConyuge;
    @Transient
    private String apellidoConyuge;
    @Transient
    private String profesion;
    @Transient
    private String actaDefuncion;
    @Transient
    private String fechaDefuncion;
    @Transient
    private String fechaInscripcionDefuncion;
    @Transient
    private Integer edad;
    @Transient
    private String fechaExpiracion;
    @Transient
    private String fechaExpedicion;
    @Column(name = "representante_legal")
    private String representanteLegal;
    @Transient
    private String actividadGeneral;
    @Transient
    private String identificacionRepresentanteLegal;
    @Transient
    private String apellidosRepresentanteLegal;
    @Transient
    private String nombresRepresentanteLegal;
    @Transient
    private String cargoRepresentanteLegal;
    @Transient
    private String identificacionContador;
    @Transient
    private String apellidosContador;
    @Transient
    private String nombresContador;
    @Transient
    private String calificacionArtesanal;
    @Transient
    private String obligadoContabilidad;
    @Transient
    private String nombreAgenteRetencion;
    @Transient
    private String fechaAltaParaEspecial;
    @Transient
    private String fechaCalificacionArtesanal;
    @Transient
    private String fechaCambioObligado;
    @Transient
    private Boolean poseeRuc;

    @Column(name = "id_representante_legal")
    private Long idRepresentanteLegal;

    @OneToMany(mappedBy = "propietario", fetch = FetchType.LAZY)
    @Where(clause = "estado")
    private List<FinaRenLocalComercial> localesComercialesCollection;

    @Transient
    private Boolean tipoBeneficiario;

    public Cliente() {
        this.estado = Boolean.TRUE;
        this.validado = Boolean.TRUE;
        this.porcentajeDiscapacidad = 0;
        this.enfermedadCatastrofica = Boolean.FALSE;
    }

    public Cliente(Long id) {
        this.id = id;
    }

    public Cliente(String identificacion) {
        this.identificacion = identificacion;
    }

    public Cliente(String identificacion, String ruc,
            String nombre, String apellido,
            String direccion, String email, String celular, String telefono,
            Date fechaCreacion, Boolean estado) {
        this.identificacion = identificacion;
        this.ruc = ruc;
        this.nombre = nombre;
        this.apellido = apellido;
        this.direccion = direccion;
        this.email = email;
        this.celular = celular;
        this.telefono = telefono;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Cliente(Long id, String identificacion, String nombre, boolean estado, String usuarioCreacion, Date fechaCreacion) {
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.estado = estado;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaCreacion = fechaCreacion;
    }

    public Cliente(String identificacion, CatalogoItem tipoIdentificacion) {
        this.identificacion = identificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.estado = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    /**
     * Razon social del ruc
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Razon social del ruc
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Nombre comercial del ruc
     *
     * @return
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Nombre comercial del ruc
     *
     * @param apellido
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
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

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        this.canton = canton;
    }

    public CatalogoItem getClasificacionProv() {
        return clasificacionProv;
    }

    public void setClasificacionProv(CatalogoItem clasificacionProv) {
        this.clasificacionProv = clasificacionProv;
    }

    public CatalogoItem getTipoProv() {
        return tipoProv;
    }

    public void setTipoProv(CatalogoItem tipoProv) {
        this.tipoProv = tipoProv;
    }

    public CatalogoItem getGenero() {
        return genero;
    }

    public void setGenero(CatalogoItem genero) {
        this.genero = genero;
    }

    public CatalogoItem getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(CatalogoItem tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public List<Proveedor> getProveedorList() {
        return proveedorList;
    }

    public void setProveedorList(List<Proveedor> proveedorList) {
        this.proveedorList = proveedorList;
    }

    public List<Proveedor> getProveedorList1() {
        return proveedorList1;
    }

    public void setProveedorList1(List<Proveedor> proveedorList1) {
        this.proveedorList1 = proveedorList1;
    }

    public String getNombreCompleto() {

        return nombreCompleto;

    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getIdentificacionCompleta() {
        return identificacionCompleta;
    }

    public void setIdentificacionCompleta(String identificacionCompleta) {
        this.identificacionCompleta = identificacionCompleta;
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

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
    }

    public String getNombreCompleltoSql() {
        return nombreCompleltoSql;
    }

    public void setNombreCompleltoSql(String nombreCompleltoSql) {
        this.nombreCompleltoSql = nombreCompleltoSql;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getNombreConyuge() {
        return nombreConyuge;
    }

    public void setNombreConyuge(String nombreConyuge) {
        this.nombreConyuge = nombreConyuge;
    }

    public String getApellidoConyuge() {
        return apellidoConyuge;
    }

    public void setApellidoConyuge(String apellidoConyuge) {
        this.apellidoConyuge = apellidoConyuge;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getCondicionCiudadano() {
        return condicionCiudadano;
    }

    public void setCondicionCiudadano(String condicionCiudadano) {
        this.condicionCiudadano = condicionCiudadano;
    }

    public String getActaDefuncion() {
        return actaDefuncion;
    }

    public void setActaDefuncion(String actaDefuncion) {
        this.actaDefuncion = actaDefuncion;
    }

    public String getFechaDefuncion() {
        return fechaDefuncion;
    }

    public void setFechaDefuncion(String fechaDefuncion) {
        this.fechaDefuncion = fechaDefuncion;
    }

    public String getFechaInscripcionDefuncion() {
        return fechaInscripcionDefuncion;
    }

    public void setFechaInscripcionDefuncion(String fechaInscripcionDefuncion) {
        this.fechaInscripcionDefuncion = fechaInscripcionDefuncion;
    }

    public Integer getEdad() {
        try {
            return Utils.getAnio(new Date()) - Utils.getAnio(fechaNacimiento);

        } catch (Exception e) {
            return 0;
        }
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    /**
     * Fecha de incio de actividad del ruc
     *
     * @return
     */
    public String getFechaExpedicion() {
        return fechaExpedicion;
    }

    /**
     * Fecha de incio de actividad del ruc
     *
     * @param fechaExpedicion
     */
    public void setFechaExpedicion(String fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public String getActividadGeneral() {
        return actividadGeneral;
    }

    public void setActividadGeneral(String actividadGeneral) {
        this.actividadGeneral = actividadGeneral;
    }

    public String getIdentificacionRepresentanteLegal() {
        return identificacionRepresentanteLegal;
    }

    public void setIdentificacionRepresentanteLegal(String identificacionRepresentanteLegal) {
        this.identificacionRepresentanteLegal = identificacionRepresentanteLegal;
    }

    public String getApellidosRepresentanteLegal() {
        return apellidosRepresentanteLegal;
    }

    public void setApellidosRepresentanteLegal(String apellidosRepresentanteLegal) {
        this.apellidosRepresentanteLegal = apellidosRepresentanteLegal;
    }

    public String getNombresRepresentanteLegal() {
        return nombresRepresentanteLegal;
    }

    public void setNombresRepresentanteLegal(String nombresRepresentanteLegal) {
        this.nombresRepresentanteLegal = nombresRepresentanteLegal;
    }

    public String getCargoRepresentanteLegal() {
        return cargoRepresentanteLegal;
    }

    public void setCargoRepresentanteLegal(String cargoRepresentanteLegal) {
        this.cargoRepresentanteLegal = cargoRepresentanteLegal;
    }

    public String getIdentificacionContador() {
        return identificacionContador;
    }

    public void setIdentificacionContador(String identificacionContador) {
        this.identificacionContador = identificacionContador;
    }

    public String getApellidosContador() {
        return apellidosContador;
    }

    public void setApellidosContador(String apellidosContador) {
        this.apellidosContador = apellidosContador;
    }

    public String getNombresContador() {
        return nombresContador;
    }

    public void setNombresContador(String nombresContador) {
        this.nombresContador = nombresContador;
    }

    public String getCalificacionArtesanal() {
        return calificacionArtesanal;
    }

    public void setCalificacionArtesanal(String calificacionArtesanal) {
        this.calificacionArtesanal = calificacionArtesanal;
    }

    public String getObligadoContabilidad() {
        return obligadoContabilidad;
    }

    public void setObligadoContabilidad(String obligadoContabilidad) {
        this.obligadoContabilidad = obligadoContabilidad;
    }

    public String getNombreAgenteRetencion() {
        return nombreAgenteRetencion;
    }

    public void setNombreAgenteRetencion(String nombreAgenteRetencion) {
        this.nombreAgenteRetencion = nombreAgenteRetencion;
    }

    public String getFechaAltaParaEspecial() {
        return fechaAltaParaEspecial;
    }

    public void setFechaAltaParaEspecial(String fechaAltaParaEspecial) {
        this.fechaAltaParaEspecial = fechaAltaParaEspecial;
    }

    public String getFechaCalificacionArtesanal() {
        return fechaCalificacionArtesanal;
    }

    public void setFechaCalificacionArtesanal(String fechaCalificacionArtesanal) {
        this.fechaCalificacionArtesanal = fechaCalificacionArtesanal;
    }

    public String getFechaCambioObligado() {
        return fechaCambioObligado;
    }

    public void setFechaCambioObligado(String fechaCambioObligado) {
        this.fechaCambioObligado = fechaCambioObligado;
    }

    public Boolean getValidado() {
        return validado;
    }

    public void setValidado(Boolean validado) {
        this.validado = validado;
    }

    public String getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(String tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public Boolean getTieneRuc() {
        return tieneRuc;
    }

    public void setTieneRuc(Boolean tieneRuc) {
        this.tieneRuc = tieneRuc;
    }

    public Boolean getSexo() {
        return sexo;
    }

    public void setSexo(Boolean sexo) {
        this.sexo = sexo;
    }

    public Usuarios getUsuarioValidador() {
        return usuarioValidador;
    }

    public void setUsuarioValidador(Usuarios usuarioValidador) {
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

    public Usuarios getValiadorAsignado() {
        return valiadorAsignado;
    }

    public void setValiadorAsignado(Usuarios valiadorAsignado) {
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) {
        this.nombreComercial = nombreComercial;
    }

    public Long getIdRepresentanteLegal() {
        return idRepresentanteLegal;
    }

    public void setIdRepresentanteLegal(Long idRepresentanteLegal) {
        this.idRepresentanteLegal = idRepresentanteLegal;
    }

    public void changeRuc() {
        if (getTipoIdentificacion() != null) {
            if (getTipoIdentificacion().getCodigo().equals("RUC")) {
                setIdentificacion(getIdentificacion().substring(0, 10));
            }
        }
    }

    public List<FinaRenLocalComercial> getLocalesComercialesCollection() {
        return localesComercialesCollection;
    }

    public void setLocalesComercialesCollection(List<FinaRenLocalComercial> localesComercialesCollection) {
        this.localesComercialesCollection = localesComercialesCollection;
    }

    public Boolean getEsPersona() {
        return esPersona;
    }

    public void setEsPersona(Boolean esPersona) {
        this.esPersona = esPersona;
    }

    public Boolean getTipoBeneficiario() {
        return tipoBeneficiario;
    }

    public void setTipoBeneficiario(Boolean tipoBeneficiario) {
        this.tipoBeneficiario = tipoBeneficiario;
    }

    public Boolean getPoseeRuc() {
        if (this.ruc != null && this.ruc.isEmpty()) {
            poseeRuc = Boolean.TRUE;
        } else {
            poseeRuc = Boolean.FALSE;
        }
        return poseeRuc;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cliente)) {
            return false;
        }
        Cliente other = (Cliente) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", identificacion=" + identificacion + ", profesion=" + profesion + '}';
    }

}
