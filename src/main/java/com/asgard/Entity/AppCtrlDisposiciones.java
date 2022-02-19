/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "app_ctrl_disposiciones", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AppCtrlDisposiciones.findAll", query = "SELECT a FROM AppCtrlDisposiciones a"),
    @NamedQuery(name = "AppCtrlDisposiciones.findById", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.id = :id"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByTuristico", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.turistico = :turistico"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByHospedaje", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.hospedaje = :hospedaje"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByEstadoUtensilio", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.estadoUtensilio = :estadoUtensilio"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByVentilacion", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.ventilacion = :ventilacion"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByEstractorOlores", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.estractorOlores = :estractorOlores"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByExtintor", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.extintor = :extintor"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByIluminacionNatural", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.iluminacionNatural = :iluminacionNatural"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByCampana", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.campana = :campana"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByDucto", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.ducto = :ducto"),
    @NamedQuery(name = "AppCtrlDisposiciones.findBySgPqsNo", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.sgPqsNo = :sgPqsNo"),
    @NamedQuery(name = "AppCtrlDisposiciones.findBySgPqsKg", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.sgPqsKg = :sgPqsKg"),
    @NamedQuery(name = "AppCtrlDisposiciones.findBySgCoNo", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.sgCoNo = :sgCoNo"),
    @NamedQuery(name = "AppCtrlDisposiciones.findBySgCoKg", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.sgCoKg = :sgCoKg"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMobiliarioMadera", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.mobiliarioMadera = :mobiliarioMadera"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMobiliarioPlastico", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.mobiliarioPlastico = :mobiliarioPlastico"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMobiliarioMetal", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.mobiliarioMetal = :mobiliarioMetal"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMobiliarioVidrio", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.mobiliarioVidrio = :mobiliarioVidrio"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMaterialPiso", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.materialPiso = :materialPiso"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMaterialParedes", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.materialParedes = :materialParedes"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMaterialPintura", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.materialPintura = :materialPintura"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByMaterialTumbado", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.materialTumbado = :materialTumbado"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByAlmaceCuberteria", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.almaceCuberteria = :almaceCuberteria"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByAlmcenaCuberteriaBueno", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.almcenaCuberteriaBueno = :almcenaCuberteriaBueno"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByNHabitaciones", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.nHabitaciones = :nHabitaciones"),
    @NamedQuery(name = "AppCtrlDisposiciones.findByIluminacionArtificial", query = "SELECT a FROM AppCtrlDisposiciones a WHERE a.iluminacionArtificial = :iluminacionArtificial")})
public class AppCtrlDisposiciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "turistico")
    private Boolean turistico;
    @Column(name = "hospedaje")
    private BigInteger hospedaje;
    @Column(name = "estado_utensilio")
    private Boolean estadoUtensilio;
    @Column(name = "ventilacion")
    private Boolean ventilacion;
    @Column(name = "estractor_olores")
    private Boolean estractorOlores;
    @Column(name = "extintor")
    private Boolean extintor;
    @Column(name = "iluminacion_natural")
    private Boolean iluminacionNatural;
    @Column(name = "campana")
    private Boolean campana;
    @Column(name = "ducto")
    private Boolean ducto;
    @Column(name = "sg_pqs_no")
    private Integer sgPqsNo;
    @Column(name = "sg_pqs_kg")
    private Integer sgPqsKg;
    @Column(name = "sg_co_no")
    private Integer sgCoNo;
    @Column(name = "sg_co_kg")
    private Integer sgCoKg;
    @Column(name = "mobiliario_madera")
    private Boolean mobiliarioMadera;
    @Column(name = "mobiliario_plastico")
    private Boolean mobiliarioPlastico;
    @Column(name = "mobiliario_metal")
    private Boolean mobiliarioMetal;
    @Column(name = "mobiliario_vidrio")
    private Boolean mobiliarioVidrio;
    @Size(max = 100)
    @Column(name = "material_piso")
    private String materialPiso;
    @Size(max = 100)
    @Column(name = "material_paredes")
    private String materialParedes;
    @Column(name = "material_pintura")
    private Boolean materialPintura;
    @Column(name = "material_tumbado")
    private Boolean materialTumbado;
    @Column(name = "almace_cuberteria")
    private Boolean almaceCuberteria;
    @Column(name = "almcena_cuberteria_bueno")
    private Boolean almcenaCuberteriaBueno;
    @Column(name = "n_habitaciones")
    private Integer nHabitaciones;
    @Column(name = "iluminacion_artificial")
    private Boolean iluminacionArtificial;
    @JoinColumn(name = "ctrl_sanitario", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private AppControlSanitario ctrlSanitario;
    @OneToMany(mappedBy = "ctrlDisposiciones", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<AppServiciosSanitarios> appServiciosSanitariosList;

    public AppCtrlDisposiciones() {
    }

    public AppCtrlDisposiciones(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getTuristico() {
        return turistico;
    }

    public void setTuristico(Boolean turistico) {
        this.turistico = turistico;
    }

    public BigInteger getHospedaje() {
        return hospedaje;
    }

    public void setHospedaje(BigInteger hospedaje) {
        this.hospedaje = hospedaje;
    }

    public Boolean getEstadoUtensilio() {
        return estadoUtensilio;
    }

    public void setEstadoUtensilio(Boolean estadoUtensilio) {
        this.estadoUtensilio = estadoUtensilio;
    }

    public Boolean getVentilacion() {
        return ventilacion;
    }

    public void setVentilacion(Boolean ventilacion) {
        this.ventilacion = ventilacion;
    }

    public Boolean getEstractorOlores() {
        return estractorOlores;
    }

    public void setEstractorOlores(Boolean estractorOlores) {
        this.estractorOlores = estractorOlores;
    }

    public Boolean getExtintor() {
        return extintor;
    }

    public void setExtintor(Boolean extintor) {
        this.extintor = extintor;
    }

    public Boolean getIluminacionNatural() {
        return iluminacionNatural;
    }

    public void setIluminacionNatural(Boolean iluminacionNatural) {
        this.iluminacionNatural = iluminacionNatural;
    }

    public Boolean getCampana() {
        return campana;
    }

    public void setCampana(Boolean campana) {
        this.campana = campana;
    }

    public Boolean getDucto() {
        return ducto;
    }

    public void setDucto(Boolean ducto) {
        this.ducto = ducto;
    }

    public Integer getSgPqsNo() {
        return sgPqsNo;
    }

    public void setSgPqsNo(Integer sgPqsNo) {
        this.sgPqsNo = sgPqsNo;
    }

    public Integer getSgPqsKg() {
        return sgPqsKg;
    }

    public void setSgPqsKg(Integer sgPqsKg) {
        this.sgPqsKg = sgPqsKg;
    }

    public Integer getSgCoNo() {
        return sgCoNo;
    }

    public void setSgCoNo(Integer sgCoNo) {
        this.sgCoNo = sgCoNo;
    }

    public Integer getSgCoKg() {
        return sgCoKg;
    }

    public void setSgCoKg(Integer sgCoKg) {
        this.sgCoKg = sgCoKg;
    }

    public Boolean getMobiliarioMadera() {
        return mobiliarioMadera;
    }

    public void setMobiliarioMadera(Boolean mobiliarioMadera) {
        this.mobiliarioMadera = mobiliarioMadera;
    }

    public Boolean getMobiliarioPlastico() {
        return mobiliarioPlastico;
    }

    public void setMobiliarioPlastico(Boolean mobiliarioPlastico) {
        this.mobiliarioPlastico = mobiliarioPlastico;
    }

    public Boolean getMobiliarioMetal() {
        return mobiliarioMetal;
    }

    public void setMobiliarioMetal(Boolean mobiliarioMetal) {
        this.mobiliarioMetal = mobiliarioMetal;
    }

    public Boolean getMobiliarioVidrio() {
        return mobiliarioVidrio;
    }

    public void setMobiliarioVidrio(Boolean mobiliarioVidrio) {
        this.mobiliarioVidrio = mobiliarioVidrio;
    }

    public String getMaterialPiso() {
        return materialPiso;
    }

    public void setMaterialPiso(String materialPiso) {
        this.materialPiso = materialPiso;
    }

    public String getMaterialParedes() {
        return materialParedes;
    }

    public void setMaterialParedes(String materialParedes) {
        this.materialParedes = materialParedes;
    }

    public Boolean getMaterialPintura() {
        return materialPintura;
    }

    public void setMaterialPintura(Boolean materialPintura) {
        this.materialPintura = materialPintura;
    }

    public Boolean getMaterialTumbado() {
        return materialTumbado;
    }

    public void setMaterialTumbado(Boolean materialTumbado) {
        this.materialTumbado = materialTumbado;
    }

    public Boolean getAlmaceCuberteria() {
        return almaceCuberteria;
    }

    public void setAlmaceCuberteria(Boolean almaceCuberteria) {
        this.almaceCuberteria = almaceCuberteria;
    }

    public Boolean getAlmcenaCuberteriaBueno() {
        return almcenaCuberteriaBueno;
    }

    public void setAlmcenaCuberteriaBueno(Boolean almcenaCuberteriaBueno) {
        this.almcenaCuberteriaBueno = almcenaCuberteriaBueno;
    }

    public Integer getNHabitaciones() {
        return nHabitaciones;
    }

    public void setNHabitaciones(Integer nHabitaciones) {
        this.nHabitaciones = nHabitaciones;
    }

    public Boolean getIluminacionArtificial() {
        return iluminacionArtificial;
    }

    public void setIluminacionArtificial(Boolean iluminacionArtificial) {
        this.iluminacionArtificial = iluminacionArtificial;
    }

    public AppControlSanitario getCtrlSanitario() {
        return ctrlSanitario;
    }

    public void setCtrlSanitario(AppControlSanitario ctrlSanitario) {
        this.ctrlSanitario = ctrlSanitario;
    }

    
    public List<AppServiciosSanitarios> getAppServiciosSanitariosList() {
        return appServiciosSanitariosList;
    }

    public void setAppServiciosSanitariosList(List<AppServiciosSanitarios> appServiciosSanitariosList) {
        this.appServiciosSanitariosList = appServiciosSanitariosList;
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
        if (!(object instanceof AppCtrlDisposiciones)) {
            return false;
        }
        AppCtrlDisposiciones other = (AppCtrlDisposiciones) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.AppCtrlDisposiciones[ id=" + id + " ]";
    }
    
}
