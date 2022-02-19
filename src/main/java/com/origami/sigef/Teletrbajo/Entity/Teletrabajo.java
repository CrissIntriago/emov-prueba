/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Entity;

/**
 *
 * @author DEVELOPER
 */
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "teletrabajo", schema = "public")
@NamedQueries({
    @NamedQuery(name = "Teletrabajo.findAll", query = "SELECT s FROM Teletrabajo s"),
    @NamedQuery(name = "Teletrabajo.findById", query = "SELECT s FROM Teletrabajo s WHERE s.id = :id")})

public class Teletrabajo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha_tarea")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaTarea;

    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "hora_inicio")
    @Temporal(TemporalType.TIME)
    private Date horaInicio;
    @Column(name = "hora_fin")
    @Temporal(TemporalType.TIME)
    private Date horaFin;

    @Column(name = "indicador")
    private String indicador;
    @Column(name = "grado_ejecucion")
    private BigInteger gradoEjecucion;
    @Column(name = "fecha_limita")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaLimite;
    @Column(name = "avance")
    private BigInteger avance;

    @Column(name = "tiempo_ejecucion")
    private String tiempoEjecucion;

    @Column(name = "estado")
    private Boolean estado;
    @Column(name = "observacion")
    private String observacion;
    @JoinColumn(name = "cliente", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente requiriente;
    @JoinColumn(name = "responsable", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente responsable;

    @JoinColumn(name = "persona_solicitada", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cliente personaSolicitada;

    @JoinColumn(name = "unidad", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private UnidadAdministrativa unidad;

    @OneToMany(mappedBy = "teletrabajo", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<HerramientasUtilizadas> listaTeletrabajo;

    @Transient
    private Short anioActual;

    public Teletrabajo() {
        this.avance = new BigInteger("100");
        this.setEstado(Boolean.TRUE);

    }

    public Teletrabajo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaTarea() {
        return fechaTarea;
    }

    public void setFechaTarea(Date fechaTarea) {
        this.fechaTarea = fechaTarea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public String getIndicador() {
        return indicador;
    }

    public void setIndicador(String indicador) {
        this.indicador = indicador;
    }

    public BigInteger getGradoEjecucion() {
        return gradoEjecucion;
    }

    public void setGradoEjecucion(BigInteger gradoEjecucion) {
        this.gradoEjecucion = gradoEjecucion;
    }

    public BigInteger getAvance() {
        return avance;
    }

    public void setAvance(BigInteger avance) {
        this.avance = avance;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public Cliente getRequiriente() {
        return requiriente;
    }

    public void setRequiriente(Cliente requiriente) {
        this.requiriente = requiriente;
    }

    public Cliente getResponsable() {
        return responsable;
    }

    public void setResponsable(Cliente responsable) {
        this.responsable = responsable;
    }

    public List<HerramientasUtilizadas> getListaTeletrabajo() {
        return listaTeletrabajo;
    }

    public void setListaTeletrabajo(List<HerramientasUtilizadas> listaTeletrabajo) {
        this.listaTeletrabajo = listaTeletrabajo;
    }

    public String getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(String tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public Cliente getPersonaSolicitada() {
        return personaSolicitada;
    }

    public void setPersonaSolicitada(Cliente personaSolicitada) {
        this.personaSolicitada = personaSolicitada;
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Short getAnioActual() {

        if (fechaTarea != null) {
            ZoneId timeZone = ZoneId.systemDefault();
            LocalDate getLocalDate = fechaTarea.toInstant().atZone(timeZone).toLocalDate();
            anioActual = Short.valueOf(String.valueOf(getLocalDate.getYear()));
        } else {
            anioActual = Short.valueOf("0");
        }

        return anioActual;
    }

    public void setAnioActual(Short anioActual) {
        this.anioActual = anioActual;
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
        if (!(object instanceof Teletrabajo)) {
            return false;
        }
        Teletrabajo other = (Teletrabajo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.Teletrbajo.Entity.Teletrabajo[ id=" + id + " ]";
    }

}
