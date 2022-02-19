/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

/**
 *
 * @author Luis Suarez
 */
import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.common.annot.GsonExcludeField;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Formula;

/**
 *
 * @author Luis Suarez
 */
@Entity
@Table(name = "unidad_administrativa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnidadAdministrativa.findAll", query = "SELECT u FROM UnidadAdministrativa u WHERE u.estado = TRUE"),
    @NamedQuery(name = "UnidadAdministrativa.findById", query = "SELECT u FROM UnidadAdministrativa u WHERE u.id = :id"),
    @NamedQuery(name = "UnidadAdministrativa.findByCodigo", query = "SELECT u FROM UnidadAdministrativa u WHERE u.codigo = :codigo"),
    @NamedQuery(name = "UnidadAdministrativa.finPadre", query = "SELECT u FROM UnidadAdministrativa u INNER JOIN u.tipoUnidad.catalogo cc WHERE cc.descripcion = ?1"),
    @NamedQuery(name = "UnidadAdministrativa.Maxpadre", query = "SELECT MAX(u.codigo)FROM UnidadAdministrativa u INNER JOIN u.tipoUnidad cc WHERE cc.texto=?1"),
    @NamedQuery(name = "UnidadAdministrativa.VerificarHijos", query = "SELECT u.padre FROM UnidadAdministrativa u "),
    @NamedQuery(name = "UnidadAdministrativa.UnidadMayor", query = "SELECT u FROM UnidadAdministrativa u where u.padre.id=u.id "),
    @NamedQuery(name = "UnidadAdministrativa.findByHijos", query = "SELECT c FROM UnidadAdministrativa c where c.padre.id= ?1 AND c.estado=TRUE"),
    @NamedQuery(name = "UnidadAdministrativa.findByNombre", query = "SELECT u FROM UnidadAdministrativa u WHERE u.nombre = :nombre"),
    @NamedQuery(name = "UnidadAdministrativa.findByUnidad", query = "SELECT u FROM UnidadAdministrativa u WHERE u.estado = ?1 ORDER BY u.nombre ASC"),
    @NamedQuery(name = "UnidadAdministrativa.TiposUnidades", query = "SELECT u FROM UnidadAdministrativa u INNER JOIN u.tipoUnidad d where d.codigo=?1 AND u.estado=?2"),
    @NamedQuery(name = "UnidadAdministrativa.findByEstado", query = "SELECT u FROM UnidadAdministrativa u WHERE u.estado = TRUE AND u.estadoActivo = true ORDER BY nombre ASC")})

public class UnidadAdministrativa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 500)
    @Column(name = "codigo")
    private String codigo;
    @Basic(optional = false)
    @Size(min = 1, max = 500)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fecha_vigencia")
    @Temporal(TemporalType.DATE)
    private Date fechaVigencia;
    @Column(name = "fecha_caducidad")
    @Temporal(TemporalType.DATE)
    private Date fechaCaducidad;
    @Basic(optional = false)
    @NotNull
    @Column(name = "estado")
    private boolean estado;
    @Column(name = "estado_activo")
    private Boolean estadoActivo;
    @JoinColumn(name = "tipo_unidad", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoUnidad;
    @OneToMany(mappedBy = "padre")
    private List<UnidadAdministrativa> unidadAdministrativaList;
    @JoinColumn(name = "padre", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa padre;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "unidadAdministrativa")
    @GsonExcludeField
    private List<Distributivo> distributivoList;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "unidadRequiriente")
    @GsonExcludeField
    private List<SolicitudReservaCompromiso> listaUnidades;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "unidadAdministrativa")
    @GsonExcludeField
    private List<Kardex> listaUnidadesKardex;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "unidadAdministrativa")
    private List<Cupos> listaCupsUnidades;

    @JoinColumn(name = "ubicacion_institucion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UbicacionInstitucion ubicacionInstitucion;

    @Formula(value = "(select ( CASE WHEN u.padre is null THEN 0 ELSE 1 END ) \n"
            + "from public.unidad_administrativa u where u.id= id ) ")
//    @Transient 
    private Integer hasPadre;

    @Transient
    private String unidad;
    @Transient
    private String direccion;
    @Transient
    private String departamento;

    public UnidadAdministrativa() {
    }

    public UnidadAdministrativa(Long id) {
        this.id = id;
    }

    public UnidadAdministrativa(Long id, String nombre, Date fechaVigencia, boolean estado) {
        this.id = id;
        this.nombre = nombre;
        this.fechaVigencia = fechaVigencia;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
    }

    public Date getFechaCaducidad() {
        return fechaCaducidad;
    }

    public void setFechaCaducidad(Date fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public CatalogoItem getTipoUnidad() {
        return tipoUnidad;
    }

    public void setTipoUnidad(CatalogoItem tipoUnidad) {
        this.tipoUnidad = tipoUnidad;
    }

    public List<UnidadAdministrativa> getUnidadAdministrativaList() {
        return unidadAdministrativaList;
    }

    public void setUnidadAdministrativaList(List<UnidadAdministrativa> unidadAdministrativaList) {
        this.unidadAdministrativaList = unidadAdministrativaList;
    }

    public UnidadAdministrativa getPadre() {
        return padre;
    }

    public void setPadre(UnidadAdministrativa padre) {
        this.padre = padre;
    }

    public List<Distributivo> getDistributivoList() {
        return distributivoList;
    }

    public void setDistributivoList(List<Distributivo> distributivoList) {
        this.distributivoList = distributivoList;
    }

    public List<Kardex> getListaUnidadesKardex() {
        return listaUnidadesKardex;
    }

    public void setListaUnidadesKardex(List<Kardex> listaUnidadesKardex) {
        this.listaUnidadesKardex = listaUnidadesKardex;
    }

    public String getUnidad() {
        return getNomUnidad();
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getDireccion() {
        return getNomDireccion();
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDepartamento() {
        return getNomDepartamento();
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getNomUnidad() {
        String result = "";
        if (padre != null) {
            if (padre.getPadre() != null) {
                result = padre.getPadre().getNombre();
            } else {
                result = padre.getNombre();
            }
        } else {
            result = nombre;
        }
        return result;
    }

    public String getNomDireccion() {
        String result = "";
        if (padre != null) {
            if (padre.getPadre() != null) {
                result = padre.getNombre();
            } else {
                result = nombre;
            }
        }
        return result;
    }

    public String getNomDepartamento() {
        String result = "";
        if (padre != null) {
            if (padre.getPadre() != null) {
                result = nombre;
            }
        }
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public List<SolicitudReservaCompromiso> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(List<SolicitudReservaCompromiso> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public Boolean getEstadoActivo() {
        return estadoActivo;
    }

    public void setEstadoActivo(Boolean estadoActivo) {
        this.estadoActivo = estadoActivo;
    }

    public List<Cupos> getListaCupsUnidades() {
        return listaCupsUnidades;
    }

    public void setListaCupsUnidades(List<Cupos> listaCupsUnidades) {
        this.listaCupsUnidades = listaCupsUnidades;
    }

    public UbicacionInstitucion getUbicacionInstitucion() {
        return ubicacionInstitucion;
    }

    public void setUbicacionInstitucion(UbicacionInstitucion ubicacionInstitucion) {
        this.ubicacionInstitucion = ubicacionInstitucion;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnidadAdministrativa)) {
            return false;
        }
        UnidadAdministrativa other = (UnidadAdministrativa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UnidadAdministrativa{" + "id=" + id + ", codigo=" + codigo + ", nombre=" + nombre + ", fechaVigencia=" + fechaVigencia + ", fechaCaducidad=" + fechaCaducidad + ", estado=" + estado + ", tipoUnidad=" + tipoUnidad + '}';
    }

    public Integer getHasPadre() {
        return hasPadre;
    }

    public void setHasPadre(Integer hasPadre) {
        this.hasPadre = hasPadre;
    }

}
