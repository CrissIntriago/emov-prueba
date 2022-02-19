/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import com.origami.sigef.common.annot.GsonExcludeField;
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
import javax.validation.constraints.Size;

/**
 *
 * @author ORIGAMI1
 */
@Entity
@Table(name = "tipo_rol", schema = "talento_humano")
@NamedQueries({
    @NamedQuery(name = "TipoRol.findAll", query = "SELECT t FROM TipoRol t")
    ,
    @NamedQuery(name = "TipoRol.findById", query = "SELECT t FROM TipoRol t WHERE t.id = ?1 AND t.estado = true")
    ,
    @NamedQuery(name = "TipoRol.findAllTrue", query = "SELECT t FROM TipoRol t WHERE t.estado = TRUE")})
public class TipoRol implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Long id;
    @Column(name = "anio")
    private Short anio;
    @Size(max = 100)
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "aprobacion")
    private Boolean aprobacion;
    @Column(name = "estado")
    private Boolean estado;
    @Size(max = 100)
    @Column(name = "usuario_creacion", length = 100)
    private String usuarioCreacion;
    @Size(max = 100)
    @Column(name = "usuario_modifica", length = 100)
    private String usuarioModifica;
    @Size(max = 100)
    @Column(name = "usuario_registra_aprueba", length = 100)
    private String usuarioRegistraAprueba;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "dias_laborados")
    private Boolean diasLaborados;
    @Column(name = "hora_ext_supl")
    private Boolean horaExtSupl;
    @JoinColumn(name = "tipo_rol", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoRol;
    @JoinColumn(name = "mes", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem mes;
    @JoinColumn(name = "estado_aprobacion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoAprobacion;
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoRol")
//    private List<RolesDePago> ListaRolesDePago;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tipoRol")
    private List<HoraExtraSuplementaria> horaExtraSuplementariaList;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tipoRol")
    private List<DiasLaborado> diasLaboradoList;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tipoRol")
    private List<PrestamoIess> ListPrestamoIess;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tipoRol")
    private List<CaucionServidores> caucionServidoresList;
//    @OneToMany(mappedBy = "tipoRol")
//    private List<FondosReserva> listFondosReserva;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tipoRol")
    private List<RetencionesImpuestoRenta> retencionesImpuestoRentaList;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tipoRol")
    private List<OtroDescuento> otroDescuentoList;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tipoRol")
    private List<LiquidacionRol> liquidacionRolLista;
    @GsonExcludeField
    @OneToMany(mappedBy = "tipoRol")
    private List<RolHorasExtrasSuplementarias> listRolHorasExtrasSupl;
    @Column(name = "num_tramite")
    private BigInteger numTramite;
    @Column(name = "diario_rol_general")
    private Boolean diarioRolGeneral;
    @Column(name = "diario_fondos_reserva")
    private Boolean diarioFondosReserva;

    @JoinColumn(name = "id_dg_rol_general", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneralRol;
    @JoinColumn(name = "id_dg_fondos_reserva", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral diarioGeneralFondos;
    @Column(name = "estado_decimo_tercero")
    private Boolean estadoDecimoTercero;
    @Column(name = "estado_decimo_cuarto")
    private Boolean estadoDecimoCuarto;
    
    @JoinColumn(name = "id_diario_general_decimo_cuarto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral idDiarioGeneralDecimoCuarto;
    
    @JoinColumn(name = "id_diario_general_decimo_tercero", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private DiarioGeneral idDiarioGeneralDecimoTercero;

    public TipoRol() {
        this.diasLaborados = Boolean.FALSE;
        this.horaExtSupl = Boolean.FALSE;
        this.diarioRolGeneral = Boolean.FALSE;
        this.diarioFondosReserva = Boolean.FALSE;
        this.estadoDecimoCuarto = Boolean.FALSE;
        this.estadoDecimoTercero = Boolean.FALSE;
    }

    public TipoRol(Long id, Short anio, String descripcion, Boolean aprobacion, Boolean estado, String usuarioCreacion, String usuarioModifica, Date fechaCreacion, Date fechaModificacion) {
        this.id = id;
        this.anio = anio;
        this.descripcion = descripcion;
        this.aprobacion = aprobacion;
        this.estado = estado;
        this.usuarioCreacion = usuarioCreacion;
        this.usuarioModifica = usuarioModifica;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public List<OtroDescuento> getOtroDescuentoList() {
        return otroDescuentoList;
    }

    public void setOtroDescuentoList(List<OtroDescuento> otroDescuentoList) {
        this.otroDescuentoList = otroDescuentoList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RolHorasExtrasSuplementarias> getListRolHorasExtrasSupl() {
        return listRolHorasExtrasSupl;
    }

    public void setListRolHorasExtrasSupl(List<RolHorasExtrasSuplementarias> listRolHorasExtrasSupl) {
        this.listRolHorasExtrasSupl = listRolHorasExtrasSupl;
    }

    public String getUsuarioRegistraAprueba() {
        return usuarioRegistraAprueba;
    }

    public void setUsuarioRegistraAprueba(String usuarioRegistraAprueba) {
        this.usuarioRegistraAprueba = usuarioRegistraAprueba;
    }

    public Short getAnio() {
        return anio;
    }

    public void setAnio(Short anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getAprobacion() {
        return aprobacion;
    }

    public void setAprobacion(Boolean aprobacion) {
        this.aprobacion = aprobacion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public CatalogoItem getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(CatalogoItem tipoRol) {
        this.tipoRol = tipoRol;
    }

    public Boolean getDiarioRolGeneral() {
        return diarioRolGeneral;
    }

    public void setDiarioRolGeneral(Boolean diarioRolGeneral) {
        this.diarioRolGeneral = diarioRolGeneral;
    }

    public Boolean getDiarioFondosReserva() {
        return diarioFondosReserva;
    }

    public void setDiarioFondosReserva(Boolean diarioFondosReserva) {
        this.diarioFondosReserva = diarioFondosReserva;
    }

    public CatalogoItem getMes() {
        return mes;
    }

    public void setMes(CatalogoItem mes) {
        this.mes = mes;
    }

    public Boolean getDiasLaborados() {
        return diasLaborados;
    }

    public void setDiasLaborados(Boolean diasLaborados) {
        this.diasLaborados = diasLaborados;
    }

    public Boolean getHoraExtSupl() {
        return horaExtSupl;
    }

    public void setHoraExtSupl(Boolean horaExtSupl) {
        this.horaExtSupl = horaExtSupl;
    }

    public List<LiquidacionRol> getLiquidacionRolLista() {
        return liquidacionRolLista;
    }

    public DiarioGeneral getDiarioGeneralRol() {
        return diarioGeneralRol;
    }

    public void setDiarioGeneralRol(DiarioGeneral diarioGeneralRol) {
        this.diarioGeneralRol = diarioGeneralRol;
    }

    public DiarioGeneral getDiarioGeneralFondos() {
        return diarioGeneralFondos;
    }

    public void setDiarioGeneralFondos(DiarioGeneral diarioGeneralFondos) {
        this.diarioGeneralFondos = diarioGeneralFondos;
    }

    public void setLiquidacionRolLista(List<LiquidacionRol> liquidacionRolLista) {
        this.liquidacionRolLista = liquidacionRolLista;
    }

    public List<HoraExtraSuplementaria> getHoraExtraSuplementariaList() {
        return horaExtraSuplementariaList;
    }

    public void setHoraExtraSuplementariaList(List<HoraExtraSuplementaria> horaExtraSuplementariaList) {
        this.horaExtraSuplementariaList = horaExtraSuplementariaList;
    }

    public List<DiasLaborado> getDiasLaboradoList() {
        return diasLaboradoList;
    }

    public void setDiasLaboradoList(List<DiasLaborado> diasLaboradoList) {
        this.diasLaboradoList = diasLaboradoList;
    }

    public CatalogoItem getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(CatalogoItem estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }

    public List<PrestamoIess> getListPrestamoIess() {
        return ListPrestamoIess;
    }

    public void setListPrestamoIess(List<PrestamoIess> ListPrestamoIess) {
        this.ListPrestamoIess = ListPrestamoIess;
    }

    public List<CaucionServidores> getCaucionServidoresList() {
        return caucionServidoresList;
    }

    public void setCaucionServidoresList(List<CaucionServidores> caucionServidoresList) {
        this.caucionServidoresList = caucionServidoresList;
    }

    public List<RetencionesImpuestoRenta> getRetencionesImpuestoRentaList() {
        return retencionesImpuestoRentaList;
    }

    public void setRetencionesImpuestoRentaList(List<RetencionesImpuestoRenta> retencionesImpuestoRentaList) {
        this.retencionesImpuestoRentaList = retencionesImpuestoRentaList;
    }

    public BigInteger getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(BigInteger numTramite) {
        this.numTramite = numTramite;
    }

    public Boolean getEstadoDecimoTercero() {
        return estadoDecimoTercero;
    }

    public void setEstadoDecimoTercero(Boolean estadoDecimoTercero) {
        this.estadoDecimoTercero = estadoDecimoTercero;
    }

    public Boolean getEstadoDecimoCuarto() {
        return estadoDecimoCuarto;
    }

    public void setEstadoDecimoCuarto(Boolean estadoDecimoCuarto) {
        this.estadoDecimoCuarto = estadoDecimoCuarto;
    }

    public DiarioGeneral getIdDiarioGeneralDecimoCuarto() {
        return idDiarioGeneralDecimoCuarto;
    }

    public void setIdDiarioGeneralDecimoCuarto(DiarioGeneral idDiarioGeneralDecimoCuarto) {
        this.idDiarioGeneralDecimoCuarto = idDiarioGeneralDecimoCuarto;
    }

    public DiarioGeneral getIdDiarioGeneralDecimoTercero() {
        return idDiarioGeneralDecimoTercero;
    }

    public void setIdDiarioGeneralDecimoTercero(DiarioGeneral idDiarioGeneralDecimoTercero) {
        this.idDiarioGeneralDecimoTercero = idDiarioGeneralDecimoTercero;
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
        if (!(object instanceof TipoRol)) {
            return false;
        }
        TipoRol other = (TipoRol) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.TipoRol[ id=" + id + " ]";
    }

}
