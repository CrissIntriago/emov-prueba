/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asgard.Entity;

import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
 * @author DEVELOPER
 */
@Entity
@Table(name = "bita_reg_bitacora", schema = Utils.SCHEMA_ASGARD)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BitaRegBitacora.findAll", query = "SELECT b FROM BitaRegBitacora b"),
    @NamedQuery(name = "BitaRegBitacora.findById", query = "SELECT b FROM BitaRegBitacora b WHERE b.id = :id"),
    @NamedQuery(name = "BitaRegBitacora.findByFecha", query = "SELECT b FROM BitaRegBitacora b WHERE b.fecha = :fecha"),
    @NamedQuery(name = "BitaRegBitacora.findByFechaHora", query = "SELECT b FROM BitaRegBitacora b WHERE b.fechaHora = :fechaHora"),
    @NamedQuery(name = "BitaRegBitacora.findByIdUsuario", query = "SELECT b FROM BitaRegBitacora b WHERE b.idUsuario = :idUsuario"),
    @NamedQuery(name = "BitaRegBitacora.findByCodUsuario", query = "SELECT b FROM BitaRegBitacora b WHERE b.codUsuario = :codUsuario"),
    @NamedQuery(name = "BitaRegBitacora.findByCodPrograma", query = "SELECT b FROM BitaRegBitacora b WHERE b.codPrograma = :codPrograma"),
    @NamedQuery(name = "BitaRegBitacora.findByActividad", query = "SELECT b FROM BitaRegBitacora b WHERE b.actividad = :actividad"),
    @NamedQuery(name = "BitaRegBitacora.findByIdMovimiento", query = "SELECT b FROM BitaRegBitacora b WHERE b.idMovimiento = :idMovimiento"),
    @NamedQuery(name = "BitaRegBitacora.findByCodLibro", query = "SELECT b FROM BitaRegBitacora b WHERE b.codLibro = :codLibro"),
    @NamedQuery(name = "BitaRegBitacora.findByNumInscripcion", query = "SELECT b FROM BitaRegBitacora b WHERE b.numInscripcion = :numInscripcion"),
    @NamedQuery(name = "BitaRegBitacora.findByFechaInscripcion", query = "SELECT b FROM BitaRegBitacora b WHERE b.fechaInscripcion = :fechaInscripcion"),
    @NamedQuery(name = "BitaRegBitacora.findByIndice", query = "SELECT b FROM BitaRegBitacora b WHERE b.indice = :indice"),
    @NamedQuery(name = "BitaRegBitacora.findByRepertorioOrdenTramite", query = "SELECT b FROM BitaRegBitacora b WHERE b.repertorioOrdenTramite = :repertorioOrdenTramite"),
    @NamedQuery(name = "BitaRegBitacora.findByIdFicha", query = "SELECT b FROM BitaRegBitacora b WHERE b.idFicha = :idFicha"),
    @NamedQuery(name = "BitaRegBitacora.findByTipFicha", query = "SELECT b FROM BitaRegBitacora b WHERE b.tipFicha = :tipFicha"),
    @NamedQuery(name = "BitaRegBitacora.findByNumFicha", query = "SELECT b FROM BitaRegBitacora b WHERE b.numFicha = :numFicha"),
    @NamedQuery(name = "BitaRegBitacora.findByTipCliente", query = "SELECT b FROM BitaRegBitacora b WHERE b.tipCliente = :tipCliente"),
    @NamedQuery(name = "BitaRegBitacora.findByCedRuc", query = "SELECT b FROM BitaRegBitacora b WHERE b.cedRuc = :cedRuc"),
    @NamedQuery(name = "BitaRegBitacora.findByTipServicio", query = "SELECT b FROM BitaRegBitacora b WHERE b.tipServicio = :tipServicio"),
    @NamedQuery(name = "BitaRegBitacora.findByAnio", query = "SELECT b FROM BitaRegBitacora b WHERE b.anio = :anio"),
    @NamedQuery(name = "BitaRegBitacora.findByIdCertificado", query = "SELECT b FROM BitaRegBitacora b WHERE b.idCertificado = :idCertificado"),
    @NamedQuery(name = "BitaRegBitacora.findByIpCliente", query = "SELECT b FROM BitaRegBitacora b WHERE b.ipCliente = :ipCliente"),
    @NamedQuery(name = "BitaRegBitacora.findByMacCliente", query = "SELECT b FROM BitaRegBitacora b WHERE b.macCliente = :macCliente")})
public class BitaRegBitacora implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Column(name = "fecha")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Column(name = "id_usuario")
    private BigInteger idUsuario;
    @Column(name = "cod_usuario")
    private BigInteger codUsuario;
    @Size(max = 10)
    @Column(name = "cod_programa")
    private String codPrograma;
    @Size(max = 2147483647)
    @Column(name = "actividad")
    private String actividad;
    @Column(name = "id_movimiento")
    private BigInteger idMovimiento;
    @Column(name = "cod_libro")
    private BigInteger codLibro;
    @Column(name = "num_inscripcion")
    private BigInteger numInscripcion;
    @Column(name = "fecha_inscripcion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaInscripcion;
    @Column(name = "indice")
    private BigInteger indice;
    @Column(name = "repertorio_orden_tramite")
    private BigInteger repertorioOrdenTramite;
    @Column(name = "id_ficha")
    private BigInteger idFicha;
    @Column(name = "tip_ficha")
    private BigInteger tipFicha;
    @Column(name = "num_ficha")
    private BigInteger numFicha;
    @Size(max = 1)
    @Column(name = "tip_cliente")
    private String tipCliente;
    @Size(max = 30)
    @Column(name = "ced_ruc")
    private String cedRuc;
    @Column(name = "tip_servicio")
    private BigInteger tipServicio;
    @Column(name = "anio")
    private BigInteger anio;
    @Column(name = "id_certificado")
    private BigInteger idCertificado;
    @Size(max = 100)
    @Column(name = "ip_cliente")
    private String ipCliente;
    @Size(max = 100)
    @Column(name = "mac_cliente")
    private String macCliente;

    public BitaRegBitacora() {
    }

    public BitaRegBitacora(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public BigInteger getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(BigInteger idUsuario) {
        this.idUsuario = idUsuario;
    }

    public BigInteger getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(BigInteger codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getCodPrograma() {
        return codPrograma;
    }

    public void setCodPrograma(String codPrograma) {
        this.codPrograma = codPrograma;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public BigInteger getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(BigInteger idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public BigInteger getCodLibro() {
        return codLibro;
    }

    public void setCodLibro(BigInteger codLibro) {
        this.codLibro = codLibro;
    }

    public BigInteger getNumInscripcion() {
        return numInscripcion;
    }

    public void setNumInscripcion(BigInteger numInscripcion) {
        this.numInscripcion = numInscripcion;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public BigInteger getIndice() {
        return indice;
    }

    public void setIndice(BigInteger indice) {
        this.indice = indice;
    }

    public BigInteger getRepertorioOrdenTramite() {
        return repertorioOrdenTramite;
    }

    public void setRepertorioOrdenTramite(BigInteger repertorioOrdenTramite) {
        this.repertorioOrdenTramite = repertorioOrdenTramite;
    }

    public BigInteger getIdFicha() {
        return idFicha;
    }

    public void setIdFicha(BigInteger idFicha) {
        this.idFicha = idFicha;
    }

    public BigInteger getTipFicha() {
        return tipFicha;
    }

    public void setTipFicha(BigInteger tipFicha) {
        this.tipFicha = tipFicha;
    }

    public BigInteger getNumFicha() {
        return numFicha;
    }

    public void setNumFicha(BigInteger numFicha) {
        this.numFicha = numFicha;
    }

    public String getTipCliente() {
        return tipCliente;
    }

    public void setTipCliente(String tipCliente) {
        this.tipCliente = tipCliente;
    }

    public String getCedRuc() {
        return cedRuc;
    }

    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }

    public BigInteger getTipServicio() {
        return tipServicio;
    }

    public void setTipServicio(BigInteger tipServicio) {
        this.tipServicio = tipServicio;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public BigInteger getIdCertificado() {
        return idCertificado;
    }

    public void setIdCertificado(BigInteger idCertificado) {
        this.idCertificado = idCertificado;
    }

    public String getIpCliente() {
        return ipCliente;
    }

    public void setIpCliente(String ipCliente) {
        this.ipCliente = ipCliente;
    }

    public String getMacCliente() {
        return macCliente;
    }

    public void setMacCliente(String macCliente) {
        this.macCliente = macCliente;
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
        if (!(object instanceof BitaRegBitacora)) {
            return false;
        }
        BitaRegBitacora other = (BitaRegBitacora) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.educacion.Models.BitaRegBitacora[ id=" + id + " ]";
    }
    
}
