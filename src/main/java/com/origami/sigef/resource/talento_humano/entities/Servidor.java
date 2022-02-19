package com.origami.sigef.resource.talento_humano.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.entities.AcumulacionFondoReserva;
import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CaucionServidores;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.CuotaAnticipo;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.entities.DetalleRegistro;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.GastoPersonal;
import com.origami.sigef.common.entities.HoraExtraSuplementaria;
import com.origami.sigef.common.entities.PrestamoIess;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.entities.Vacaciones;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Formula;

/**
 *
 * @author Criss Intriago
 */
@Entity
@Table(name = "servidor", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "Servidor.findAll", query = "SELECT s FROM Servidor s"),
    @NamedQuery(name = "Servidor.findById", query = "SELECT s FROM Servidor s WHERE s.id = :id"),
    @NamedQuery(name = "Servidor.findByFechaIngreso", query = "SELECT s FROM Servidor s WHERE s.fechaIngreso = :fechaIngreso"),
    @NamedQuery(name = "Servidor.findByFechaSalida", query = "SELECT s FROM Servidor s WHERE s.fechaSalida = :fechaSalida"),
    @NamedQuery(name = "Servidor.findByPersona", query = "SELECT s FROM Servidor s WHERE s.persona = :persona"),
    @NamedQuery(name = "Servidor.findByIdentificacionServ", query = "SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = ?1 AND s.estado = TRUE"),
    @NamedQuery(name = "Servidor.findByServidorIdentificacion", query = "SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = ?1 AND p.estado = TRUE AND s.estado = TRUE"),
    @NamedQuery(name = "Servidor.findByPersonaIdentificacion", query = "SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = ?1 AND p.estado = TRUE AND s.estado = TRUE"),
    @NamedQuery(name = "Servidor.findByPersonaIdentificacionRol", query = "SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = ?1 AND (s.estado = TRUE OR (s.estado = true AND s.fechaSalida BETWEEN ?2 AND ?3))"),
    @NamedQuery(name = "Servidor.findByPersonaIdentificacionProv", query = "SELECT s FROM Servidor s JOIN s.persona p WHERE p.identificacion = ?1 AND s.estado = TRUE and s.fechaSalida IS NULL"),
    @NamedQuery(name = "Servidor.findByServidorInventario", query = "SELECT s FROM Servidor s where s.estado = TRUE and s.distributivo is not null"),
    @NamedQuery(name = "Servidor.findByEstado", query = "SELECT s FROM Servidor s WHERE s.estado = :estado")})
@XmlRootElement
public class Servidor implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.DATE)
    private Date fechaIngreso;
    @Column(name = "fecha_salida")
    @Temporal(TemporalType.DATE)
    private Date fechaSalida;
    @JoinColumn(name = "persona", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente persona;
    @Column(name = "estado")
    private boolean estado;
    @JoinColumn(name = "distributivo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    @GsonExcludeField
    private Distributivo distributivo;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modifica")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModifica;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;
    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @JoinColumn(name = "estado_civil", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoCivil;
    @JoinColumn(name = "etnia", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem etnia;
    @Size(max = 500)
    @Column(name = "email_institucion")
    private String emailInstitucion;
    @Size(max = 20)
    @Column(name = "telefono_oficina")
    private String telefonoOficina;
    @Size(max = 500)
    @Column(name = "actividades")
    private String actividades;
    @Size(max = 4)
    @Column(name = "extencion")
    private String extencion;
    @Size(min = 1, max = 20)
    @Column(name = "cedula_conyugue")
    private String cedulaConyugue;
    @Size(min = 1, max = 500)
    @Column(name = "nombre_conyugue")
    private String nombreConyugue;
    @GsonExcludeField
    @OneToMany(mappedBy = "supervisor")
    private Collection<DetalleRegistro> detalleRegistroCollection1;
    @GsonExcludeField
    @OneToOne(fetch = javax.persistence.FetchType.LAZY, mappedBy = "funcionario")
    private Usuarios usuario;
    @GsonExcludeField
    @OneToMany(mappedBy = "servidorPublico")
    private List<Distributivo> distributivoList;
    @GsonExcludeField
    @OneToMany(mappedBy = "servidorPublico")
    private List<DetalleBanco> detalleBancoList;
    @GsonExcludeField
    @OneToMany(mappedBy = "servidorPublico")
    private List<GastoPersonal> gastoPersonalList;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<AnticipoRemuneracion> anticipoRemuneracionList;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<AcumulacionFondoReserva> acumulacionFondoReservaList;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<HoraExtraSuplementaria> listHoraExtraSupl;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<DiasLaborado> listDiasLaborado;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<PrestamoIess> listPrestamoIess;
    @JoinColumn(name = "tipo_sangre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoSangre;
    @Size(max = 15)
    @Column(name = "telefono_emergencia")
    private String telefonoEmergencia;
    @Size(min = 1, max = 500)
    @Column(name = "nombres_emergencia")
    private String nombresEmergencia;
    @Size(min = 1, max = 500)
    @Column(name = "apellido_emergencia")
    private String apellidoEmergencia;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<CaucionServidores> caucionServidoresList;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<CuotaAnticipo> listCuotaAnticipoServidor;
    @OneToMany(mappedBy = "aprobado")
    @GsonExcludeField
    private List<Vacaciones> vacacionesList;
    @OneToMany(mappedBy = "servidor")
    @GsonExcludeField
    private List<Vacaciones> vacacionesList1;
    @Column(name = "cod_biometrico")
    private String codBiometrico;
    @Column(name = "realiza_horas_extras")
    private Boolean realizaHorasExtras;
    @Column(name = "maxio_horas_extras")
    private Integer maximoHorasExtras;
    @Column(name = "punto_marcacion")
    private String puntoMarcacion;
    @Column(name = "jubilado")
    private Boolean jubilado;
    @Column(name = "dias_acumulados_vacaciones")
    private Integer diasAcumuladosVacaciones;
    @Column(name = "descripcion_salida")
    private String descripcionSalida;
    @Column(name = "decimo_tercero")
    private Boolean decimoTercero;
    @Column(name = "decimo_cuarto")
    private Boolean decimoCuarto;
    @Column(name = "fondos_reserva")
    private Boolean fondosReserva;

    @Formula("(SELECT (CASE WHEN COUNT(t.id) > 0 THEN true ELSE false END) FROM talento_humano.th_servidor_cargo t WHERE t.id_servidor = id AND t.activo = true)")
    private Boolean cargoAsignado;

    public Servidor() {
        this.realizaHorasExtras = Boolean.TRUE;
        this.maximoHorasExtras = 168;
        this.diasAcumuladosVacaciones = 0;
        this.estado = true;
        this.decimoTercero = true;
        this.decimoCuarto = true;
        this.fondosReserva = true;
    }

    public Servidor(Long id) {
        this.id = id;
    }

    public Servidor(Long id, Date fechaIngreso, Date fechaSalida, boolean estado, Date fechaCreacion, String usuarioCreacion, String usuarioModifica, Date fechaModifica, String nombreConyugue, String cedulaConyugue) {
        this.id = id;
        this.fechaIngreso = fechaIngreso;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaModifica = fechaModifica;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModifica = usuarioModifica;
        this.nombreConyugue = nombreConyugue;
        this.cedulaConyugue = cedulaConyugue;

    }

    public List<DiasLaborado> getListDiasLaborado() {
        return listDiasLaborado;
    }

    public void setListDiasLaborado(List<DiasLaborado> listDiasLaborado) {
        this.listDiasLaborado = listDiasLaborado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Cliente getPersona() {
        return persona;
    }

    public void setPersona(Cliente persona) {
        this.persona = persona;
    }

    public List<Distributivo> getDistributivoList() {
        return distributivoList;
    }

    public void setDistributivoList(List<Distributivo> distributivoList) {
        this.distributivoList = distributivoList;
    }

    public Usuarios getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuarios usuario) {
        this.usuario = usuario;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public CatalogoItem getEstadoCivil() {
        return estadoCivil;
    }

    public List<PrestamoIess> getListPrestamoIess() {
        return listPrestamoIess;
    }

    public void setListPrestamoIess(List<PrestamoIess> listPrestamoIess) {
        this.listPrestamoIess = listPrestamoIess;
    }

    public void setEstadoCivil(CatalogoItem estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public List<AnticipoRemuneracion> getAnticipoRemuneracionList() {
        return anticipoRemuneracionList;
    }

    public void setAnticipoRemuneracionList(List<AnticipoRemuneracion> anticipoRemuneracionList) {
        this.anticipoRemuneracionList = anticipoRemuneracionList;
    }

    public List<AcumulacionFondoReserva> getAcumulacionFondoReservaList() {
        return acumulacionFondoReservaList;
    }

    public void setAcumulacionFondoReservaList(List<AcumulacionFondoReserva> acumulacionFondoReservaList) {
        this.acumulacionFondoReservaList = acumulacionFondoReservaList;
    }

    public String getEmailInstitucion() {
        return emailInstitucion;
    }

    public void setEmailInstitucion(String emailInstitucion) {
        this.emailInstitucion = emailInstitucion;
    }

    public String getTelefonoOficina() {
        return telefonoOficina;
    }

    public void setTelefonoOficina(String telefonoOficina) {
        this.telefonoOficina = telefonoOficina;
    }

    public String getActividades() {
        return actividades;
    }

    public void setActividades(String actividades) {
        this.actividades = actividades;
    }

    public CatalogoItem getEtnia() {
        return etnia;
    }

    public void setEtnia(CatalogoItem etnia) {
        this.etnia = etnia;
    }

    public List<DetalleBanco> getDetalleBancoList() {
        return detalleBancoList;
    }

    public void setDetalleBancoList(List<DetalleBanco> detalleBancoList) {
        this.detalleBancoList = detalleBancoList;
    }

    public String getExtencion() {
        return extencion;
    }

    public void setExtencion(String extencion) {
        this.extencion = extencion;
    }

    public String getCedulaConyugue() {
        return cedulaConyugue;
    }

    public void setCedulaConyugue(String cedulaConyugue) {
        this.cedulaConyugue = cedulaConyugue;
    }

    public String getNombreConyugue() {
        return nombreConyugue;
    }

    public void setNombreConyugue(String nombreConyugue) {
        this.nombreConyugue = nombreConyugue;
    }

    public Collection<DetalleRegistro> getDetalleRegistroCollection1() {
        return detalleRegistroCollection1;
    }

    public void setDetalleRegistroCollection1(Collection<DetalleRegistro> detalleRegistroCollection1) {
        this.detalleRegistroCollection1 = detalleRegistroCollection1;
    }

    public List<GastoPersonal> getGastoPersonalList() {
        return gastoPersonalList;
    }

    public void setGastoPersonalList(List<GastoPersonal> gastoPersonalList) {
        this.gastoPersonalList = gastoPersonalList;
    }

    public String getPuntoMarcacion() {
        return puntoMarcacion;
    }

    public void setPuntoMarcacion(String puntoMarcacion) {
        this.puntoMarcacion = puntoMarcacion;
    }

    public CatalogoItem getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(CatalogoItem tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    public String getTelefonoEmergencia() {
        return telefonoEmergencia;
    }

    public void setTelefonoEmergencia(String telefonoEmergencia) {
        this.telefonoEmergencia = telefonoEmergencia;
    }

    public String getNombresEmergencia() {
        return nombresEmergencia;
    }

    public void setNombresEmergencia(String nombresEmergencia) {
        this.nombresEmergencia = nombresEmergencia;
    }

    public String getApellidoEmergencia() {
        return apellidoEmergencia;
    }

    public void setApellidoEmergencia(String apellidoEmergencia) {
        this.apellidoEmergencia = apellidoEmergencia;
    }

    public List<HoraExtraSuplementaria> getListHoraExtraSupl() {
        return listHoraExtraSupl;
    }

    public List<CuotaAnticipo> getListCuotaAnticipoServidor() {
        return listCuotaAnticipoServidor;
    }

    public void setListCuotaAnticipoServidor(List<CuotaAnticipo> listCuotaAnticipoServidor) {
        this.listCuotaAnticipoServidor = listCuotaAnticipoServidor;
    }

    public void setListHoraExtraSupl(List<HoraExtraSuplementaria> listHoraExtraSupl) {
        this.listHoraExtraSupl = listHoraExtraSupl;
    }

    public List<CaucionServidores> getCaucionServidoresList() {
        return caucionServidoresList;
    }

    public void setCaucionServidoresList(List<CaucionServidores> caucionServidoresList) {
        this.caucionServidoresList = caucionServidoresList;
    }

    public List<Vacaciones> getVacacionesList() {
        return vacacionesList;
    }

    public void setVacacionesList(List<Vacaciones> vacacionesList) {
        this.vacacionesList = vacacionesList;
    }

    public List<Vacaciones> getVacacionesList1() {
        return vacacionesList1;
    }

    public void setVacacionesList1(List<Vacaciones> vacacionesList1) {
        this.vacacionesList1 = vacacionesList1;
    }

    public String getCodBiometrico() {
        return codBiometrico;
    }

    public void setCodBiometrico(String codBiometrico) {
        this.codBiometrico = codBiometrico;
    }

    public Boolean getRealizaHorasExtras() {
        return realizaHorasExtras;
    }

    public void setRealizaHorasExtras(Boolean realizaHorasExtras) {
        this.realizaHorasExtras = realizaHorasExtras;
    }

    public Integer getMaximoHorasExtras() {
        return maximoHorasExtras;
    }

    public void setMaximoHorasExtras(Integer maximoHorasExtras) {
        this.maximoHorasExtras = maximoHorasExtras;
    }

    public Boolean getCargoAsignado() {
        return cargoAsignado;
    }

    public void setCargoAsignado(Boolean cargoAsignado) {
        this.cargoAsignado = cargoAsignado;
    }

    public Boolean getJubilado() {
        return jubilado;
    }

    public void setJubilado(Boolean jubilado) {
        this.jubilado = jubilado;
    }

    public Integer getDiasAcumuladosVacaciones() {
        return diasAcumuladosVacaciones;
    }

    public void setDiasAcumuladosVacaciones(Integer diasAcumuladosVacaciones) {
        this.diasAcumuladosVacaciones = diasAcumuladosVacaciones;
    }

    public String getDescripcionSalida() {
        return descripcionSalida;
    }

    public void setDescripcionSalida(String descripcionSalida) {
        this.descripcionSalida = descripcionSalida;
    }

    public Boolean getDecimoTercero() {
        return decimoTercero;
    }

    public void setDecimoTercero(Boolean decimoTercero) {
        this.decimoTercero = decimoTercero;
    }

    public Boolean getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(Boolean decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
    }

    public Boolean getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(Boolean fondosReserva) {
        this.fondosReserva = fondosReserva;
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
        if (!(object instanceof Servidor)) {
            return false;
        }
        Servidor other = (Servidor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.resource.talento_humano.entities.Servidor[ id=" + id + " ]";
    }

}
