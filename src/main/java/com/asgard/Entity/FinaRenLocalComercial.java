/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.gestionTributaria.Entities.RenLocalCantidadAccesorios;
import com.gestionTributaria.Entities.RenLocalComercialFoto;
import com.gestionTributaria.Entities.RenLocalComercialHorario;
import com.gestionTributaria.Entities.RenTasaTurismo;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.common.entities.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Where;

/**
 *
 * @author DEVELOPER
 */
@Entity
@Table(name = "fina_ren_local_comercial", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FinaRenLocalComercial.findAll", query = "SELECT f FROM FinaRenLocalComercial f"),
    @NamedQuery(name = "FinaRenLocalComercial.findById", query = "SELECT f FROM FinaRenLocalComercial f WHERE f.id = :id")})
public class FinaRenLocalComercial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "area")
    private BigDecimal area;
    @Size(max = 2147483647)
    @Column(name = "actividad_comercial")
    private String actividadComercial;
    @Size(max = 150)
    @Column(name = "cadena")
    private String cadena;
    @Column(name = "contabilidad")
    private Boolean contabilidad;
    @Column(name = "es_propio")
    private Boolean esPropio;
    @Column(name = "estado")
    private Boolean estado;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "estado_local")
    private Long estadoLocalComercial;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Invalid phone/fax format, should be as xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 25)
    @Column(name = "fax")
    private String fax;
    @Column(name = "fecha_ingreso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaIngreso;
    @Column(name = "inicio_actividad")
    @Temporal(TemporalType.DATE)
    private Date inicioActividad;
    @Column(name = "matriz")
    private Boolean matriz;
    @Size(max = 150)
    @Column(name = "nombre_local")
    private String nombreLocal;
    @Size(max = 50)
    @Column(name = "num_local")
    private String numLocal;
    @Column(name = "num_predio")
    private Long predio;
    @Size(max = 150)
    @Column(name = "pagina_web")
    private String paginaWeb;
    @JoinColumn(name = "propietario", referencedColumnName = "id")
    @ManyToOne
    private Cliente propietario;
    @Column(name = "razon_social")
    private Long razonSocial;
    @Column(name = "turismo")
    private Boolean turismo;
    @Size(max = 20)
    @Column(name = "usuario_ingreso")
    private String usuarioIngreso;
    @Size(max = 2147483647)
    @Column(name = "codigo_propietario")
    private String codigoPropietario;
    @JoinColumn(name = "categoria", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLocalCategoria categoria;
    @JoinColumn(name = "ubicacion", referencedColumnName = "id")
    @ManyToOne
    private FinaRenLocalUbicacion ubicacion;
    @JoinColumn(name = "patente", referencedColumnName = "id")
    @ManyToOne
    private FinaRenPatente patente;
    @JoinColumn(name = "tipo_local", referencedColumnName = "id")
    @ManyToOne
    private FinaRenTipoLocalComercial tipoLocal;
    @JoinTable(name = "ren_actividad_por_local", schema = Utils.SCHEMA_SGM, joinColumns = {
        @JoinColumn(name = "local_comercial", updatable = true, referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "actividad", updatable = true, referencedColumnName = "id")})
    //  @ManyToMany(cascade = {CascadeType.ALL})
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FinaRenActividadComercial> renActividadComercialCollection;
    @OneToMany(mappedBy = "localComercial", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenLocalCantidadAccesorios> cantidadAccesoriosCollection;

    @OneToMany(mappedBy = "localComercial", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<RenLocalComercialFoto> fotosLocalesComerciales;
    @OneToMany(mappedBy = "localComercial", fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.TRUE)
    private List<FinaRenLiquidacion> renLiquidacionCollection;
    @OneToOne(mappedBy = "renLocalComercial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private RenLocalComercialHorario localComercialHorarios;
    @JoinColumn(name = "destino", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem destino;

    @JoinColumn(name = "factor_tamanio", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem factorTamanio;

    @JoinColumn(name = "factor_ubicacion", referencedColumnName = "id")
    @ManyToOne
    private CatalogoItem factorUbicacion;
    @Column(name = "avenida")
    private Boolean avenida;

    @Column(name = "mediterraneo")
    private Boolean mediterraneo;

    @Column(name = "n_empleado")
    private Long numEmpelado;

    @Column(name = "n_habitaciones")
    private Long numHabitaciones;

    @Column(name = "n_mesas")
    private Long numMesas;

    @Column(name = "tipo_negocio")
    private String tipoNegocio;
    @Column(name = "ancho")
    private BigDecimal ancho;
    @Column(name = "altura")
    private BigDecimal altura;
    @Formula("(select p.clave_cat from catastro.cat_predio p where p.id = num_predio)")
    private String clavePreial;
    @Formula("(select p.num_predio from catastro.cat_predio p where p.id=num_predio)")
    private String codigoPredio;
    @Formula("(select p.tipo_predio from catastro.cat_predio p where p.id=num_predio)")
    private String tipoPredio;
    @Column(name = "id_tramite")
    private Long idTramite;

    public FinaRenLocalComercial() {
        this.ancho = BigDecimal.ZERO;
        this.altura = BigDecimal.ZERO;
        this.area = BigDecimal.ZERO;
    }

    public FinaRenLocalComercial(Long id) {
        this.id = id;
        this.ancho = BigDecimal.ZERO;
        this.altura = BigDecimal.ZERO;
        this.area = BigDecimal.ZERO;
    }

    public FinaRenLocalComercial(Long id, Long estadoLocalComercial) {
        this.id = id;
        this.estadoLocalComercial = estadoLocalComercial;
        this.ancho = BigDecimal.ZERO;
        this.altura = BigDecimal.ZERO;
        this.area = BigDecimal.ZERO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getActividadComercial() {
        return actividadComercial;
    }

    public void setActividadComercial(String actividadComercial) {
        this.actividadComercial = actividadComercial;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public Boolean getContabilidad() {
        return contabilidad;
    }

    public void setContabilidad(Boolean contabilidad) {
        this.contabilidad = contabilidad;
    }

    public Boolean getEsPropio() {
        return esPropio;
    }

    public void setEsPropio(Boolean esPropio) {
        this.esPropio = esPropio;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getEstadoLocalComercial() {
        return estadoLocalComercial;
    }

    public void setEstadoLocalComercial(Long estadoLocalComercial) {
        this.estadoLocalComercial = estadoLocalComercial;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getInicioActividad() {
        return inicioActividad;
    }

    public void setInicioActividad(Date inicioActividad) {
        this.inicioActividad = inicioActividad;
    }

    public Boolean getMatriz() {
        return matriz;
    }

    public void setMatriz(Boolean matriz) {
        this.matriz = matriz;
    }

    public String getNombreLocal() {
        return nombreLocal;
    }

    public void setNombreLocal(String nombreLocal) {
        this.nombreLocal = nombreLocal;
    }

    public String getNumLocal() {
        return numLocal;
    }

    public void setNumLocal(String numLocal) {
        this.numLocal = numLocal;
    }

    public Long getNumPredio() {
        return predio;
    }

    public void setNumPredio(Long predio) {
        this.predio = predio;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public Cliente getPropietario() {
        if (patente != null && patente.getId() != null) {
            return patente.getPropietario();
        }
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public Long getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(Long razonSocial) {
        this.razonSocial = razonSocial;
    }

    public Boolean getTurismo() {
        return turismo;
    }

    public void setTurismo(Boolean turismo) {
        this.turismo = turismo;
    }

    public String getUsuarioIngreso() {
        return usuarioIngreso;
    }

    public void setUsuarioIngreso(String usuarioIngreso) {
        this.usuarioIngreso = usuarioIngreso;
    }

    public String getCodigoPropietario() {
        return codigoPropietario;
    }

    public void setCodigoPropietario(String codigoPropietario) {
        this.codigoPropietario = codigoPropietario;
    }

    public FinaRenLocalCategoria getCategoria() {
        return categoria;
    }

    public void setCategoria(FinaRenLocalCategoria categoria) {
        this.categoria = categoria;
    }

    public FinaRenLocalUbicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(FinaRenLocalUbicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public FinaRenPatente getPatente() {
        return patente;
    }

    public void setPatente(FinaRenPatente patente) {
        this.patente = patente;
    }

    public FinaRenTipoLocalComercial getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(FinaRenTipoLocalComercial tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public List<FinaRenActividadComercial> getRenActividadComercialCollection() {
        return renActividadComercialCollection;
    }

    public void setRenActividadComercialCollection(List<FinaRenActividadComercial> renActividadComercialCollection) {
        this.renActividadComercialCollection = renActividadComercialCollection;
    }

    public List<RenLocalComercialFoto> getFotosLocalesComerciales() {
        return fotosLocalesComerciales;
    }

    public void setFotosLocalesComerciales(List<RenLocalComercialFoto> fotosLocalesComerciales) {
        this.fotosLocalesComerciales = fotosLocalesComerciales;
    }

    public List<RenLocalCantidadAccesorios> getCantidadAccesoriosCollection() {
        return cantidadAccesoriosCollection;
    }

    public void setCantidadAccesoriosCollection(List<RenLocalCantidadAccesorios> cantidadAccesoriosCollection) {
        this.cantidadAccesoriosCollection = cantidadAccesoriosCollection;
    }

    public List<FinaRenLiquidacion> getRenLiquidacionCollection() {
        return renLiquidacionCollection;
    }

    public void setRenLiquidacionCollection(List<FinaRenLiquidacion> renLiquidacionCollection) {
        this.renLiquidacionCollection = renLiquidacionCollection;
    }

    public RenLocalComercialHorario getLocalComercialHorarios() {
        return localComercialHorarios;
    }

    public void setLocalComercialHorarios(RenLocalComercialHorario localComercialHorarios) {
        this.localComercialHorarios = localComercialHorarios;
    }

    public CatalogoItem getDestino() {
        return destino;
    }

    public void setDestino(CatalogoItem destino) {
        this.destino = destino;
    }

    public CatalogoItem getFactorTamanio() {
        return factorTamanio;
    }

    public void setFactorTamanio(CatalogoItem factorTamanio) {
        this.factorTamanio = factorTamanio;
    }

    public CatalogoItem getFactorUbicacion() {
        return factorUbicacion;
    }

    public void setFactorUbicacion(CatalogoItem factorUbicacion) {
        this.factorUbicacion = factorUbicacion;
    }

    public Boolean getAvenida() {
        return avenida;
    }

    public void setAvenida(Boolean avenida) {
        this.avenida = avenida;
    }

    public Boolean getMediterraneo() {
        return mediterraneo;
    }

    public void setMediterraneo(Boolean mediterraneo) {
        this.mediterraneo = mediterraneo;
    }

    public Long getNumEmpelado() {
        return numEmpelado;
    }

    public void setNumEmpelado(Long numEmpelado) {
        this.numEmpelado = numEmpelado;
    }

    public Long getNumHabitaciones() {
        return numHabitaciones;
    }

    public void setNumHabitaciones(Long numHabitaciones) {
        this.numHabitaciones = numHabitaciones;
    }

    public Long getNumMesas() {
        return numMesas;
    }

    public void setNumMesas(Long numMesas) {
        this.numMesas = numMesas;
    }

    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public Long getPredio() {
        return predio;
    }

    public void setPredio(Long predio) {
        this.predio = predio;
    }

    public BigDecimal getAncho() {
        return ancho;
    }

    public void setAncho(BigDecimal ancho) {
        this.ancho = ancho;
    }

    public BigDecimal getAltura() {
        return altura;
    }

    public void setAltura(BigDecimal altura) {
        this.altura = altura;
    }

    public String getClavePreial() {
        return clavePreial;
    }

    public void setClavePreial(String clavePreial) {
        this.clavePreial = clavePreial;
    }

    public String getCodigoPredio() {
        return codigoPredio;
    }

    public void setCodigoPredio(String codigoPredio) {
        this.codigoPredio = codigoPredio;
    }

    public String getTipoPredio() {
        return tipoPredio;
    }

    public void setTipoPredio(String tipoPredio) {
        this.tipoPredio = tipoPredio;
    }

    public Long getIdTramite() {
        return idTramite;
    }

    public void setIdTramite(Long idTramite) {
        this.idTramite = idTramite;
    }

    public String getDescripcionCategoria() {
        String nom = "";
        if (categoria != null && categoria.getFinaRenLocalComercialList() != null) {
            for (RenTasaTurismo item : categoria.getRenTasaTurismoLsit()) {
                nom = item.getDescripcion();
            }
        }
        return nom;
    }

    public String estadoDescriptivo() {
        if (estadoLocalComercial == 1) {
            return "HABILITADO";
        } else if (estadoLocalComercial == 2) {
            return "INHABILITADO";
        } else if (estadoLocalComercial == 3) {
            return "CLAUSURADO";
        }
        return "";
    }

    public String tipoNegociosAnidados(List<CatalogoItem> listTipoNegocio) {
        String tipo = null;
        for (CatalogoItem item : listTipoNegocio) {
            tipo = item.getId() + ":";
        }

        return tipo;
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
        if (!(object instanceof FinaRenLocalComercial)) {
            return false;
        }
        FinaRenLocalComercial other = (FinaRenLocalComercial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.FinaRenLocalComercial[ id=" + id + " ]";
    }

}
