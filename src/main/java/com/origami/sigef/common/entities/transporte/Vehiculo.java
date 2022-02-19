/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.transporte;

import com.origami.sigef.common.annot.GsonExcludeField;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Pais;
import java.io.Serializable;
import java.math.BigInteger;
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

/**
 *
 * @author ANGEL NAVARRO
 */
@Entity
@Table(name = "vehiculo", schema = "transporte")
@NamedQueries({
    @NamedQuery(name = "Vehiculo.findAll", query = "SELECT v FROM Vehiculo v")})
public class Vehiculo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Size(max = 20)
    @Column(name = "placa_actual", length = 20)
    private String placaActual;
    @Size(max = 20)
    @Column(name = "placa_anterior", length = 20)
    private String placaAnterior;
    @Size(max = 20)
    @Column(length = 20)
    private String vin;
    @Size(max = 20)
    @Column(length = 20)
    private String motor;
    @Size(max = 30)
    @Column(length = 30)
    private String rmvcpn;
    @JoinColumn(name = "marca", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem marca;
    @JoinColumn(name = "modelo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem modelo;
    private Short anio;
    @JoinColumn(name = "pais_fabricacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Pais paisFabricacion;
    @Size(max = 10)
    @Column(length = 10)
    private String clilindraje;
    @JoinColumn(name = "clase_vehiculo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem claseVehiculo;
    @JoinColumn(name = "tipo_vehiculo", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoVehiculo;
    @Column(name = "num_pasajero")
    private Short numPasajero;
    @Size(max = 10)
    @Column(name = "tonelada_peso", length = 10)
    private String toneladaPeso;
    @Size(max = 10)
    @Column(name = "capacidad_carga", length = 10)
    private String capacidadCarga;
    @JoinColumn(name = "tipo_combustible", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoCombustible;
    @JoinColumn(name = "carroceria", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem carroceria;
    @JoinColumn(name = "color_principal", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem colorPrincipal;
    @JoinColumn(name = "color_secundario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem colorSecundario;
    @JoinColumn(name = "color_3", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem color3;
    @Size(max = 500)
    @Column(length = 500)
    private String observaciones;
    @JoinColumn(name = "propietario", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Cliente propietario;
    @JoinColumn(name = "canton_circulacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Canton cantonCirculacion;
    @JoinColumn(name = "canton_domicilio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private Canton cantonDomicilio;
    @JoinColumn(name = "clase_transporte", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem claseTransporte;
    @JoinColumn(name = "tipo_transporte", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoTransporte;
    @JoinColumn(name = "ambito", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem ambito;
    @Size(max = 20)
    @Column(length = 20)
    private String disco;
    @Size(max = 3)
    @Column(length = 3)
    private String estado;
    @JoinColumn(name = "estado_veh", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoVeh;
    @Size(max = 10)
    @Column(length = 10)
    private String ancho;
    @Size(max = 10)
    @Column(length = 10)
    private String largo;
    @Size(max = 10)
    @Column(length = 10)
    private String capacidad;
    @Size(max = 10)
    @Column(length = 10)
    private String cargautil;
    @Size(max = 10)
    @Column(length = 10)
    private String kilometraje;
    @Size(max = 10)
    @Column(length = 10)
    private String numeroejes;
    @Size(max = 10)
    @Column(length = 10)
    private String numeroruedas;
    @JoinColumn(name = "tipo_servicio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoServicio;
    @Size(max = 25)
    @Column(length = 25)
    private String codigoant;
    @Size(max = 25)
    @Column(length = 25)
    private String contrato;
    @Column(name = "peso_vehiculo")
    private BigInteger pesoVehiculo;
    @JoinColumn(name = "clase_servicio", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem claseServicio;
    @NotNull
    @Column(name = "fecha_cre", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCre;
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "usuario_cre", nullable = false, length = 2147483647)
    private String usuarioCre;
    @NotNull
    @Column(name = "fecha_mod", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaMod;
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "usuario_mod", nullable = false, length = 2147483647)
    private String usuarioMod;
    @OneToMany(mappedBy = "vehiculo")
    private List<VehiculoExtras> vehiculoExtrasList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehiculo")
    @GsonExcludeField
    private List<CooperativaVehiculo> cooperativaVehiculoList;
    @Column(name = "nuevo")
    @Transient
    private Boolean nuevo = false;

    public Vehiculo() {
    }

    public Vehiculo(Long id) {
        this.id = id;
    }

    public Vehiculo(Long id, Date fechaCre, String usuarioCre, Date fechaMod, String usuarioMod) {
        this.id = id;
        this.fechaCre = fechaCre;
        this.usuarioCre = usuarioCre;
        this.fechaMod = fechaMod;
        this.usuarioMod = usuarioMod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlacaActual() {
        return placaActual;
    }

    public void setPlacaActual(String placaActual) {
        this.placaActual = placaActual;
    }

    public String getPlacaAnterior() {
        return placaAnterior;
    }

    public void setPlacaAnterior(String placaAnterior) {
        this.placaAnterior = placaAnterior;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    public String getRmvcpn() {
        return rmvcpn;
    }

    public void setRmvcpn(String rmvcpn) {
        this.rmvcpn = rmvcpn;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public String getClilindraje() {
        return clilindraje;
    }

    public void setClilindraje(String clilindraje) {
        this.clilindraje = clilindraje;
    }

    public Short getNumPasajero() {
        return numPasajero;
    }

    public void setNumPasajero(Short numPasajero) {
        this.numPasajero = numPasajero;
    }

    public String getToneladaPeso() {
        return toneladaPeso;
    }

    public void setToneladaPeso(String toneladaPeso) {
        this.toneladaPeso = toneladaPeso;
    }

    public String getCapacidadCarga() {
        return capacidadCarga;
    }

    public void setCapacidadCarga(String capacidadCarga) {
        this.capacidadCarga = capacidadCarga;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDisco() {
        return disco;
    }

    public void setDisco(String disco) {
        this.disco = disco;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAncho() {
        return ancho;
    }

    public void setAncho(String ancho) {
        this.ancho = ancho;
    }

    public String getLargo() {
        return largo;
    }

    public void setLargo(String largo) {
        this.largo = largo;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getCargautil() {
        return cargautil;
    }

    public void setCargautil(String cargautil) {
        this.cargautil = cargautil;
    }

    public String getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(String kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getNumeroejes() {
        return numeroejes;
    }

    public void setNumeroejes(String numeroejes) {
        this.numeroejes = numeroejes;
    }

    public String getNumeroruedas() {
        return numeroruedas;
    }

    public void setNumeroruedas(String numeroruedas) {
        this.numeroruedas = numeroruedas;
    }

    public String getCodigoant() {
        return codigoant;
    }

    public void setCodigoant(String codigoant) {
        this.codigoant = codigoant;
    }

    public String getContrato() {
        return contrato;
    }

    public void setContrato(String contrato) {
        this.contrato = contrato;
    }

    public BigInteger getPesoVehiculo() {
        return pesoVehiculo;
    }

    public void setPesoVehiculo(BigInteger pesoVehiculo) {
        this.pesoVehiculo = pesoVehiculo;
    }

    public CatalogoItem getClaseServicio() {
        return claseServicio;
    }

    public void setClaseServicio(CatalogoItem claseServicio) {
        this.claseServicio = claseServicio;
    }

    public Date getFechaCre() {
        return fechaCre;
    }

    public void setFechaCre(Date fechaCre) {
        this.fechaCre = fechaCre;
    }

    public String getUsuarioCre() {
        return usuarioCre;
    }

    public void setUsuarioCre(String usuarioCre) {
        this.usuarioCre = usuarioCre;
    }

    public Date getFechaMod() {
        return fechaMod;
    }

    public void setFechaMod(Date fechaMod) {
        this.fechaMod = fechaMod;
    }

    public String getUsuarioMod() {
        return usuarioMod;
    }

    public void setUsuarioMod(String usuarioMod) {
        this.usuarioMod = usuarioMod;
    }

    public List<VehiculoExtras> getVehiculoExtrasList() {
        return vehiculoExtrasList;
    }

    public void setVehiculoExtrasList(List<VehiculoExtras> vehiculoExtrasList) {
        this.vehiculoExtrasList = vehiculoExtrasList;
    }

    public List<CooperativaVehiculo> getCooperativaVehiculoList() {
        return cooperativaVehiculoList;
    }

    public void setCooperativaVehiculoList(List<CooperativaVehiculo> cooperativaVehiculoList) {
        this.cooperativaVehiculoList = cooperativaVehiculoList;
    }

    public CatalogoItem getMarca() {
        return marca;
    }

    public void setMarca(CatalogoItem marca) {
        this.marca = marca;
    }

    public CatalogoItem getModelo() {
        return modelo;
    }

    public void setModelo(CatalogoItem modelo) {
        this.modelo = modelo;
    }

    public Pais getPaisFabricacion() {
        return paisFabricacion;
    }

    public void setPaisFabricacion(Pais paisFabricacion) {
        this.paisFabricacion = paisFabricacion;
    }

    public CatalogoItem getClaseVehiculo() {
        return claseVehiculo;
    }

    public void setClaseVehiculo(CatalogoItem claseVehiculo) {
        this.claseVehiculo = claseVehiculo;
    }

    public CatalogoItem getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(CatalogoItem tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public CatalogoItem getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(CatalogoItem tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public CatalogoItem getCarroceria() {
        return carroceria;
    }

    public void setCarroceria(CatalogoItem carroceria) {
        this.carroceria = carroceria;
    }

    public CatalogoItem getColorPrincipal() {
        return colorPrincipal;
    }

    public void setColorPrincipal(CatalogoItem colorPrincipal) {
        this.colorPrincipal = colorPrincipal;
    }

    public CatalogoItem getColorSecundario() {
        return colorSecundario;
    }

    public void setColorSecundario(CatalogoItem colorSecundario) {
        this.colorSecundario = colorSecundario;
    }

    public CatalogoItem getColor3() {
        return color3;
    }

    public void setColor3(CatalogoItem color3) {
        this.color3 = color3;
    }

    public Cliente getPropietario() {
        return propietario;
    }

    public void setPropietario(Cliente propietario) {
        this.propietario = propietario;
    }

    public Canton getCantonCirculacion() {
        return cantonCirculacion;
    }

    public void setCantonCirculacion(Canton cantonCirculacion) {
        this.cantonCirculacion = cantonCirculacion;
    }

    public Canton getCantonDomicilio() {
        return cantonDomicilio;
    }

    public void setCantonDomicilio(Canton cantonDomicilio) {
        this.cantonDomicilio = cantonDomicilio;
    }

    public CatalogoItem getClaseTransporte() {
        return claseTransporte;
    }

    public void setClaseTransporte(CatalogoItem claseTransporte) {
        this.claseTransporte = claseTransporte;
    }

    public CatalogoItem getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(CatalogoItem tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public CatalogoItem getAmbito() {
        return ambito;
    }

    public void setAmbito(CatalogoItem ambito) {
        this.ambito = ambito;
    }

    public CatalogoItem getEstadoVeh() {
        return estadoVeh;
    }

    public void setEstadoVeh(CatalogoItem estadoVeh) {
        this.estadoVeh = estadoVeh;
    }

    public CatalogoItem getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(CatalogoItem tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public Boolean getNuevo() {
        return nuevo;
    }

    public void setNuevo(Boolean nuevo) {
        this.nuevo = nuevo;
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
        if (!(object instanceof Vehiculo)) {
            return false;
        }
        Vehiculo other = (Vehiculo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "id=" + id + ", placaActual=" + placaActual + ", placaAnterior=" + placaAnterior + ", vin=" + vin + ", motor=" + motor + ", rmvcpn=" + rmvcpn + ", modelo=" + modelo + ", anio=" + anio + ", clilindraje=" + clilindraje + ", numPasajero=" + numPasajero + ", toneladaPeso=" + toneladaPeso + ", capacidadCarga=" + capacidadCarga + ", observaciones=" + observaciones + ", cantonDomicilio=" + cantonDomicilio + ", cantonCirculacion=" + cantonCirculacion + ", tipoTransporte=" + tipoTransporte + ", disco=" + disco + ", estado=" + estado + ", ancho=" + ancho + ", largo=" + largo + ", capacidad=" + capacidad + ", cargautil=" + cargautil + ", kilometraje=" + kilometraje + ", numeroejes=" + numeroejes + ", numeroruedas=" + numeroruedas + ", codigoant=" + codigoant + ", contrato=" + contrato + ", fechaCre=" + fechaCre + ", usuarioCre=" + usuarioCre + ", fechaMod=" + fechaMod + ", usuarioMod=" + usuarioMod + '}';
    }

}
