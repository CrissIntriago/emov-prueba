/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Formula;

/**
 *
 * @author kriiz and Luis Suárez
 */
@Entity
@Table(name = "actividad_operativa")
@NamedQueries({
    @NamedQuery(name = "ActividadOperativa.findAll", query = "SELECT a FROM ActividadOperativa a"),
    @NamedQuery(name = "ActividadOperativa.findById", query = "SELECT a FROM ActividadOperativa a WHERE a.id = :id"),
    @NamedQuery(name = "ActividadOperativa.findByNombreActividad", query = "SELECT a FROM ActividadOperativa a WHERE a.nombreActividad = :nombreActividad"),
    @NamedQuery(name = "ActividadOperativa.findByObjetivoOperativo", query = "SELECT a FROM ActividadOperativa a WHERE a.objetivoOperativo = :objetivoOperativo"),
    @NamedQuery(name = "ActividadOperativa.findByMedicionMeta", query = "SELECT a FROM ActividadOperativa a WHERE a.medicionMeta = :medicionMeta"),
    @NamedQuery(name = "ActividadOperativa.findByMedicionPorcentaje", query = "SELECT a FROM ActividadOperativa a WHERE a.medicionPorcentaje = :medicionPorcentaje"),
    @NamedQuery(name = "ActividadOperativa.findByDescripcionMeta", query = "SELECT a FROM ActividadOperativa a WHERE a.descripcionMeta = :descripcionMeta"),
    @NamedQuery(name = "ActividadOperativa.findByCuatrimestre1Meta", query = "SELECT a FROM ActividadOperativa a WHERE a.cuatrimestre1Meta = :cuatrimestre1Meta"),
    @NamedQuery(name = "ActividadOperativa.findByCuatrimestre1Logrado", query = "SELECT a FROM ActividadOperativa a WHERE a.cuatrimestre1Logrado = :cuatrimestre1Logrado"),
    @NamedQuery(name = "ActividadOperativa.findByCuatrimestre2Meta", query = "SELECT a FROM ActividadOperativa a WHERE a.cuatrimestre2Meta = :cuatrimestre2Meta"),
    @NamedQuery(name = "ActividadOperativa.findByCuatrimestre2Logrado", query = "SELECT a FROM ActividadOperativa a WHERE a.cuatrimestre2Logrado = :cuatrimestre2Logrado"),
    @NamedQuery(name = "ActividadOperativa.findByCuatrimestre3Meta", query = "SELECT a FROM ActividadOperativa a WHERE a.cuatrimestre3Meta = :cuatrimestre3Meta"),
    @NamedQuery(name = "ActividadOperativa.findByCuatrimestre3Logrado", query = "SELECT a FROM ActividadOperativa a WHERE a.cuatrimestre3Logrado = :cuatrimestre3Logrado"),
    @NamedQuery(name = "ActividadOperativa.findByIndicadorMeta", query = "SELECT a FROM ActividadOperativa a WHERE a.indicadorMeta = :indicadorMeta"),
    @NamedQuery(name = "ActividadOperativa.findByMonto", query = "SELECT a FROM ActividadOperativa a WHERE a.monto = :monto"),
    @NamedQuery(name = "ActividadOperativa.findByEstado", query = "SELECT a FROM ActividadOperativa a WHERE a.estado = :estado"),
    @NamedQuery(name = "ActividadOperativa.findByFechaCreacion", query = "SELECT a FROM ActividadOperativa a WHERE a.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "ActividadOperativa.findByUsuarioCreacion", query = "SELECT a FROM ActividadOperativa a WHERE a.usuarioCreacion = :usuarioCreacion"),
    @NamedQuery(name = "ActividadOperativa.findByFechaModificacion", query = "SELECT a FROM ActividadOperativa a WHERE a.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "ActividadOperativa.findByUsuarioModifica", query = "SELECT a FROM ActividadOperativa a WHERE a.usuarioModifica = :usuarioModifica"),
    @NamedQuery(name = "ActividadOperativa.findByVerificarHijos", query = "SELECT a FROM ActividadOperativa a WHERE a.planProgramaProyecto.id = ?1"),
    @NamedQuery(name = "ActividadOperativa.findByPeriodoActual", query = "SELECT a FROM ActividadOperativa a WHERE a.periodo = ?1 AND a.estado=TRUE AND a.codigoReforma IS NULL AND a.codigoReformaTraspaso IS NULL"),
    @NamedQuery(name = "ActividadOperativa.findByPeriodo", query = "SELECT a FROM ActividadOperativa a WHERE a.periodo = :periodo AND a.codigoReforma is null and a.codigoReformaTraspaso is null")})
public class ActividadOperativa implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Size(max = 2147483647)
    @Column(name = "nombre_actividad")
    private String nombreActividad;
    @Size(max = 2147483647)
    @Column(name = "objetivo_operativo")
    private String objetivoOperativo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "medicion_meta")
    private BigDecimal medicionMeta;
    @Column(name = "medicion_porcentaje")
    private Boolean medicionPorcentaje;
    @Size(max = 2147483647)
    @Column(name = "descripcion_meta")
    private String descripcionMeta;
    @Column(name = "cuatrimestre1_meta")
    private BigDecimal cuatrimestre1Meta;
    @Column(name = "cuatrimestre1_logrado")
    private BigDecimal cuatrimestre1Logrado;
    @Column(name = "cuatrimestre2_meta")
    private BigDecimal cuatrimestre2Meta;
    @Column(name = "cuatrimestre2_logrado")
    private BigDecimal cuatrimestre2Logrado;
    @Column(name = "cuatrimestre3_meta")
    private BigDecimal cuatrimestre3Meta;
    @Column(name = "cuatrimestre3_logrado")
    private BigDecimal cuatrimestre3Logrado;
    @Size(max = 255)
    @Column(name = "indicador_meta")
    private String indicadorMeta;
    @Column(name = "monto")
    private BigDecimal monto;

    @Column(name = "estado")
    private boolean estado;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Size(min = 1, max = 100)
    @Column(name = "usuario_creacion")
    private String usuarioCreacion;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Size(min = 1, max = 100)
    @Column(name = "usuario_modifica")
    private String usuarioModifica;
    @Column(name = "periodo")
    private Short periodo;
    @Column(name = "cuatrimestre1_actividad")
    private BigDecimal cuatrimestre1Actividad;
    @Column(name = "cuatrimestre2_actividad")
    private BigDecimal cuatrimestre2Actividad;
    @Column(name = "cuatrimestre3_actividad")
    private BigDecimal cuatrimestre3Actividad;
    @Column(name = "codigo_reforma")
    private BigInteger codigoReforma;
    @Column(name = "numero_orden_id")
    private BigInteger numeroOrdenId;
    @Column(name = "numero_tramite")
    private Short numeroTramite;
    @Column(name = "codigo_reforma_traspaso")
    private BigInteger codigoReformaTraspaso;
    @Column(name = "registro_nuevo_referencia")
    private BigInteger registroNuevoReferencia;
    @Column(name = "monto_reformado")
    private BigDecimal monotReformado;
    @Column(name = "cr_1")
    private BigDecimal cr1;
    @Column(name = "cr_2")
    private BigDecimal cr2;
    @Column(name = "cr_3")
    private BigDecimal cr3;
    @Column(name = "enero")
    private BigDecimal enero;
    @Column(name = "febrero")
    private BigDecimal febrero;
    @Column(name = "marzo")
    private BigDecimal marzo;
    @Column(name = "abril")
    private BigDecimal abril;
    @Column(name = "mayo")
    private BigDecimal mayo;
    @Column(name = "junio")
    private BigDecimal junio;
    @Column(name = "julio")
    private BigDecimal julio;
    @Column(name = "agosto")
    private BigDecimal agosto;
    @Column(name = "septiembre")
    private BigDecimal septiembre;
    @Column(name = "octubre")
    private BigDecimal octubre;
    @Column(name = "noviembre")
    private BigDecimal noviembre;
    @Column(name = "diciembre")
    private BigDecimal diciembre;
    @Column(name = "pai")
    private Boolean pai;
    @Column(name = "presupuesto_participacion")
    private Boolean presupuestoParticipacion;
    @Column(name = "grupo_atencion")
    private Boolean grupoAtencion;
    @Column(name = "arrastre")
    private Boolean arrastre;
    @Column(name = "presupuesto_propio")
    private BigDecimal presupuestoPropio = BigDecimal.ZERO;
    @Column(name = "presupuesto_fina")
    private BigDecimal presupuestoFinanciamiento = BigDecimal.ZERO;
    @Column(name = "enero_meta")
    private BigDecimal eneroMeta;
    @Column(name = "febreo_meta")
    private BigDecimal febreroMeta;
    @Column(name = "marzo_meta")
    private BigDecimal marzoMeta;
    @Column(name = "abril_meta")
    private BigDecimal abrilMeta;
    @Column(name = "mayo_meta")
    private BigDecimal mayoMeta;
    @Column(name = "junio_meta")
    private BigDecimal junioMeta;
    @Column(name = "julio_meta")
    private BigDecimal julioMeta;
    @Column(name = "agosto_meta")
    private BigDecimal agostoMeta;
    @Column(name = "septiembre_meta")
    private BigDecimal septiembreMeta;
    @Column(name = "octubre_meta")
    private BigDecimal octubreMeta;
    @Column(name = "noviembre_meta")
    private BigDecimal noviembreMeta;
    @Column(name = "diciembre_meta")
    private BigDecimal diciembreMeta;
    @Column(name = "anio_pres_par")
    private Integer anioPresPar;
    @JoinColumn(name = "estado_papp", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem estadoPapp;
    @JoinColumn(name = "plan_programa_proyecto", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private PlanAnualProgramaProyecto planProgramaProyecto;
    @JoinColumn(name = "unidad_responsable", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private UnidadAdministrativa unidadResponsable;
    @JoinColumn(name = "tipo_distribucion", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoDistribuccion;
    @JoinColumn(name = "tipo_componete", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem tipoComponete;
    @JoinColumn(name = "distribucion_meta", referencedColumnName = "id")
    @ManyToOne(fetch = javax.persistence.FetchType.LAZY)
    private CatalogoItem distribucionMeta;

    @Column(name = "enero_reforma")
    private BigDecimal eneroReforma;
    @Column(name = "febrero_reforma")
    private BigDecimal febreroReforma;
    @Column(name = "marzo_reforma")
    private BigDecimal marzoReforma;
    @Column(name = "abril_reforma")
    private BigDecimal abrilReforma;
    @Column(name = "mayo_reforma")
    private BigDecimal mayoReforma;
    @Column(name = "junio_reforma")
    private BigDecimal junioReforma;
    @Column(name = "julio_reforma")
    private BigDecimal julioReforma;
    @Column(name = "agosto_reforma")
    private BigDecimal agostoReforma;
    @Column(name = "septiembre_reforma")
    private BigDecimal septiembreReforma;
    @Column(name = "octubre_reforma")
    private BigDecimal octubreReforma;
    @Column(name = "noviembre_reforma")
    private BigDecimal noviembreReforma;
    @Column(name = "diciembre_reforma")
    private BigDecimal diciembreReforma;

    @Column(name = "enero_meta_reforma")
    private BigDecimal eneroMetaReforma;

    @Column(name = "febreo_meta_reforma")
    private BigDecimal febreoMetaReforma;

    @Column(name = "marzo_meta_reforma")
    private BigDecimal marzoMetaReforma;

    @Column(name = "abril_meta_reforma")
    private BigDecimal abrilMetaReforma;

    @Column(name = "mayo_meta_reforma")
    private BigDecimal mayoMetaReforma;

    @Column(name = "junio_meta_reforma")
    private BigDecimal junioMetaReforma;

    @Column(name = "julio_meta_reforma")
    private BigDecimal julioMetaReforma;

    @Column(name = "agosto_meta_reforma")
    private BigDecimal agostoMetaReforma;

    @Column(name = "septiembre_meta_reforma")
    private BigDecimal septiembreMetaReforma;

    @Column(name = "octubre_meta_reforma")
    private BigDecimal octubreMetaReforma;

    @Column(name = "noviembre_meta_reforma")
    private BigDecimal noviembreMetaReforma;

    @Column(name = "diciembre_meta_reforma")
    private BigDecimal diciembreMetaReforma;
    
    @Column(name = "pres_participacion_anio")
    private Short presParticipacionAnio;

    @Transient
    private BigDecimal semestreUno = BigDecimal.ZERO;
    @Transient
    private BigDecimal semestrDos = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreUno = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreDos = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreTres = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreCuatro = BigDecimal.ZERO;
    @Transient
    private BigDecimal cuatrimestreUno = BigDecimal.ZERO;
    @Transient
    private BigDecimal cuatrimestreDos = BigDecimal.ZERO;
    @Transient
    private BigDecimal cuatrimestreTres = BigDecimal.ZERO;

    //META
    @Transient
    private BigDecimal semestreUnoMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal semestrDosMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreUnoMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreDosMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreTresMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal trimestreCuatroMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal cuatrimestreUnoMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal cuatrimestreDosMeta = BigDecimal.ZERO;
    @Transient
    private BigDecimal cuatrimestreTresMeta = BigDecimal.ZERO;
    
    //@Formula("(SELECT CASE WHEN COALESCE(sum(p.monto_reformada), 0.00::numeric) > monotReformado THEN true ELSE false END from producto p where p.actividad_operativa = id and p.codigo_reforma is null and p.codigo_reforma_traspaso is null)")
   // private BigDecimal esDesequilibrado;
    //AGREGAR PARA EL MONTO DE LA ACTIVIDAD UN SELECT
//    @Transient
//    private Boolean esDesequilibrado;

    public ActividadOperativa() {
        this.medicionMeta = BigDecimal.ZERO;
        this.cuatrimestre1Meta = BigDecimal.ZERO;
        this.cuatrimestre2Meta = BigDecimal.ZERO;
        this.cuatrimestre3Meta = BigDecimal.ZERO;
        this.cuatrimestre1Logrado = BigDecimal.ZERO;
        this.cuatrimestre2Logrado = BigDecimal.ZERO;
        this.cuatrimestre3Logrado = BigDecimal.ZERO;
        this.cuatrimestre1Actividad = BigDecimal.ZERO;
        this.cuatrimestre2Actividad = BigDecimal.ZERO;
        this.cuatrimestre3Actividad = BigDecimal.ZERO;
        this.cr1 = BigDecimal.ZERO;
        this.cr2 = BigDecimal.ZERO;
        this.cr3 = BigDecimal.ZERO;
        this.cuatrimestre1Actividad = BigDecimal.ZERO;
        this.monto = BigDecimal.ZERO;
        this.monotReformado = BigDecimal.ZERO;
        this.enero = BigDecimal.ZERO;
        this.febrero = BigDecimal.ZERO;
        this.marzo = BigDecimal.ZERO;
        this.abril = BigDecimal.ZERO;
        this.mayo = BigDecimal.ZERO;
        this.junio = BigDecimal.ZERO;
        this.julio = BigDecimal.ZERO;
        this.agosto = BigDecimal.ZERO;
        this.septiembre = BigDecimal.ZERO;
        this.octubre = BigDecimal.ZERO;
        this.noviembre = BigDecimal.ZERO;
        this.diciembre = BigDecimal.ZERO;
        encerarValores();
    }

    public ActividadOperativa(Long id) {
        this.id = id;
    }

    public ActividadOperativa(Long id, boolean estado, Date fechaCreacion, String usuarioCreacion, Date fechaModificacion, String usuarioModifica) {
        this.id = id;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.usuarioCreacion = usuarioCreacion;
        this.fechaModificacion = fechaModificacion;
        this.usuarioModifica = usuarioModifica;
    }
    
//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">

//    public BigDecimal getEsDesequilibrado() {
//        return esDesequilibrado;
//    }
//
//    public void setEsDesequilibrado(BigDecimal esDesequilibrado) {
//        this.esDesequilibrado = esDesequilibrado;
//    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getObjetivoOperativo() {
        return objetivoOperativo;
    }

    public void setObjetivoOperativo(String objetivoOperativo) {
        this.objetivoOperativo = objetivoOperativo;
    }

    public BigDecimal getMedicionMeta() {
        return medicionMeta;
    }

    public void setMedicionMeta(BigDecimal medicionMeta) {
        this.medicionMeta = medicionMeta;
    }

    public Boolean getMedicionPorcentaje() {
        return medicionPorcentaje;
    }

    public void setMedicionPorcentaje(Boolean medicionPorcentaje) {
        this.medicionPorcentaje = medicionPorcentaje;
    }

    public String getDescripcionMeta() {
        return descripcionMeta;
    }

    public void setDescripcionMeta(String descripcionMeta) {
        this.descripcionMeta = descripcionMeta;
    }

    public BigDecimal getCuatrimestre1Meta() {
        return cuatrimestre1Meta;
    }

    public void setCuatrimestre1Meta(BigDecimal cuatrimestre1Meta) {
        this.cuatrimestre1Meta = cuatrimestre1Meta;
    }

    public BigDecimal getCuatrimestre1Logrado() {
        return cuatrimestre1Logrado;
    }

    public void setCuatrimestre1Logrado(BigDecimal cuatrimestre1Logrado) {
        this.cuatrimestre1Logrado = cuatrimestre1Logrado;
    }

    public BigDecimal getCuatrimestre2Meta() {
        return cuatrimestre2Meta;
    }

    public void setCuatrimestre2Meta(BigDecimal cuatrimestre2Meta) {
        this.cuatrimestre2Meta = cuatrimestre2Meta;
    }

    public BigDecimal getCuatrimestre2Logrado() {
        return cuatrimestre2Logrado;
    }

    public void setCuatrimestre2Logrado(BigDecimal cuatrimestre2Logrado) {
        this.cuatrimestre2Logrado = cuatrimestre2Logrado;
    }

    public BigDecimal getCuatrimestre3Meta() {
        return cuatrimestre3Meta;
    }

    public void setCuatrimestre3Meta(BigDecimal cuatrimestre3Meta) {
        this.cuatrimestre3Meta = cuatrimestre3Meta;
    }

    public BigDecimal getCuatrimestre3Logrado() {
        return cuatrimestre3Logrado;
    }

    public void setCuatrimestre3Logrado(BigDecimal cuatrimestre3Logrado) {
        this.cuatrimestre3Logrado = cuatrimestre3Logrado;
    }

    public String getIndicadorMeta() {
        return indicadorMeta;
    }

    public void setIndicadorMeta(String indicadorMeta) {
        this.indicadorMeta = indicadorMeta;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public BigDecimal getCuatrimestre1Actividad() {
        return cuatrimestre1Actividad;
    }

    public void setCuatrimestre1Actividad(BigDecimal cuatrimestre1Actividad) {
        this.cuatrimestre1Actividad = cuatrimestre1Actividad;
    }

    public BigDecimal getCuatrimestre2Actividad() {
        return cuatrimestre2Actividad;
    }

    public void setCuatrimestre2Actividad(BigDecimal cuatrimestre2Actividad) {
        this.cuatrimestre2Actividad = cuatrimestre2Actividad;
    }

    public BigDecimal getCuatrimestre3Actividad() {
        return cuatrimestre3Actividad;
    }

    public void setCuatrimestre3Actividad(BigDecimal cuatrimestre3Actividad) {
        this.cuatrimestre3Actividad = cuatrimestre3Actividad;
    }

    public PlanAnualProgramaProyecto getPlanProgramaProyecto() {
        return planProgramaProyecto;
    }

    public void setPlanProgramaProyecto(PlanAnualProgramaProyecto planProgramaProyecto) {
        this.planProgramaProyecto = planProgramaProyecto;
    }

    public UnidadAdministrativa getUnidadResponsable() {
        return unidadResponsable;
    }

    public void setUnidadResponsable(UnidadAdministrativa unidadResponsable) {
        this.unidadResponsable = unidadResponsable;
    }

    public BigInteger getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(BigInteger codigoReforma) {
        this.codigoReforma = codigoReforma;
    }

    public BigInteger getCodigoReformaTraspaso() {
        return codigoReformaTraspaso;
    }

    public void setCodigoReformaTraspaso(BigInteger codigoReformaTraspaso) {
        this.codigoReformaTraspaso = codigoReformaTraspaso;
    }

    public BigInteger getNumeroOrdenId() {
        return numeroOrdenId;
    }

    public void setNumeroOrdenId(BigInteger numeroOrdenId) {
        this.numeroOrdenId = numeroOrdenId;
    }

    public Short getNumeroTramite() {
        return numeroTramite;
    }

    public void setNumeroTramite(Short numeroTramite) {
        this.numeroTramite = numeroTramite;
    }

    public CatalogoItem getEstadoPapp() {
        return estadoPapp;
    }

    public void setEstadoPapp(CatalogoItem estadoPapp) {
        this.estadoPapp = estadoPapp;
    }

    public BigInteger getRegistroNuevoReferencia() {
        return registroNuevoReferencia;
    }

    public void setRegistroNuevoReferencia(BigInteger registroNuevoReferencia) {
        this.registroNuevoReferencia = registroNuevoReferencia;
    }

    public BigDecimal getMonotReformado() {
        return monotReformado;
    }

    public void setMonotReformado(BigDecimal monotReformado) {
        this.monotReformado = monotReformado;
    }

    public BigDecimal getCr1() {
        return cr1;
    }

    public void setCr1(BigDecimal cr1) {
        this.cr1 = cr1;
    }

    public BigDecimal getCr2() {
        return cr2;
    }

    public void setCr2(BigDecimal cr2) {
        this.cr2 = cr2;
    }

    public BigDecimal getCr3() {
        return cr3;
    }

    public void setCr3(BigDecimal cr3) {
        this.cr3 = cr3;
    }

    public BigDecimal getEnero() {
        return enero;
    }

    public void setEnero(BigDecimal enero) {
        this.enero = enero;
    }

    public BigDecimal getFebrero() {
        return febrero;
    }

    public void setFebrero(BigDecimal febrero) {
        this.febrero = febrero;
    }

    public BigDecimal getMarzo() {
        return marzo;
    }

    public void setMarzo(BigDecimal marzo) {
        this.marzo = marzo;
    }

    public BigDecimal getAbril() {
        return abril;
    }

    public void setAbril(BigDecimal abril) {
        this.abril = abril;
    }

    public BigDecimal getMayo() {
        return mayo;
    }

    public void setMayo(BigDecimal mayo) {
        this.mayo = mayo;
    }

    public BigDecimal getJunio() {
        return junio;
    }

    public void setJunio(BigDecimal junio) {
        this.junio = junio;
    }

    public BigDecimal getJulio() {
        return julio;
    }

    public void setJulio(BigDecimal julio) {
        this.julio = julio;
    }

    public BigDecimal getAgosto() {
        return agosto;
    }

    public void setAgosto(BigDecimal agosto) {
        this.agosto = agosto;
    }

    public BigDecimal getSeptiembre() {
        return septiembre;
    }

    public void setSeptiembre(BigDecimal septiembre) {
        this.septiembre = septiembre;
    }

    public BigDecimal getOctubre() {
        return octubre;
    }

    public void setOctubre(BigDecimal octubre) {
        this.octubre = octubre;
    }

    public BigDecimal getNoviembre() {
        return noviembre;
    }

    public void setNoviembre(BigDecimal noviembre) {
        this.noviembre = noviembre;
    }

    public BigDecimal getDiciembre() {
        return diciembre;
    }

    public void setDiciembre(BigDecimal diciembre) {
        this.diciembre = diciembre;
    }

    public CatalogoItem getTipoDistribuccion() {
        return tipoDistribuccion;
    }

    public void setTipoDistribuccion(CatalogoItem tipoDistribuccion) {
        this.tipoDistribuccion = tipoDistribuccion;
    }

    public Boolean getPai() {
        return pai;
    }

    public void setPai(Boolean pai) {
        this.pai = pai;
    }

    public Boolean getPresupuestoParticipacion() {
        return presupuestoParticipacion;
    }

    public void setPresupuestoParticipacion(Boolean presupuestoParticipacion) {
        this.presupuestoParticipacion = presupuestoParticipacion;
    }

    public Boolean getGrupoAtencion() {
        return grupoAtencion;
    }

    public void setGrupoAtencion(Boolean grupoAtencion) {
        this.grupoAtencion = grupoAtencion;
    }

    public Boolean getArrastre() {
        return arrastre;
    }

    public void setArrastre(Boolean arrastre) {
        this.arrastre = arrastre;
    }

    public CatalogoItem getTipoComponete() {
        return tipoComponete;
    }

    public void setTipoComponete(CatalogoItem tipoComponete) {
        this.tipoComponete = tipoComponete;
    }

//</editor-fold>
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ActividadOperativa)) {
            return false;
        }
        ActividadOperativa other = (ActividadOperativa) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.origami.sigef.common.entities.ActividadOperativa[ id=" + id + " ]";
    }

    public BigDecimal getSemestreUno() {
        return semestreUno;
    }

    public void setSemestreUno(BigDecimal semestreUno) {
        this.semestreUno = semestreUno;
    }

    public BigDecimal getSemestrDos() {
        return semestrDos;
    }

    public void setSemestrDos(BigDecimal semestrDos) {
        this.semestrDos = semestrDos;
    }

    public BigDecimal getTrimestreUno() {
        return trimestreUno;
    }

    public void setTrimestreUno(BigDecimal trimestreUno) {
        this.trimestreUno = trimestreUno;
    }

    public BigDecimal getTrimestreDos() {
        return trimestreDos;
    }

    public void setTrimestreDos(BigDecimal trimestreDos) {
        this.trimestreDos = trimestreDos;
    }

    public BigDecimal getTrimestreTres() {
        return trimestreTres;
    }

    public void setTrimestreTres(BigDecimal trimestreTres) {
        this.trimestreTres = trimestreTres;
    }

    public BigDecimal getTrimestreCuatro() {
        return trimestreCuatro;
    }

    public void setTrimestreCuatro(BigDecimal trimestreCuatro) {
        this.trimestreCuatro = trimestreCuatro;
    }

    public BigDecimal getCuatrimestreUno() {
        return cuatrimestreUno;
    }

    public void setCuatrimestreUno(BigDecimal cuatrimestreUno) {
        this.cuatrimestreUno = cuatrimestreUno;
    }

    public BigDecimal getCuatrimestreDos() {
        return cuatrimestreDos;
    }

    public void setCuatrimestreDos(BigDecimal cuatrimestreDos) {
        this.cuatrimestreDos = cuatrimestreDos;
    }

    public BigDecimal getCuatrimestreTres() {
        return cuatrimestreTres;
    }

    public void setCuatrimestreTres(BigDecimal cuatrimestreTres) {
        this.cuatrimestreTres = cuatrimestreTres;
    }

    public BigDecimal getPresupuestoPropio() {
        return presupuestoPropio;
    }

    public void setPresupuestoPropio(BigDecimal presupuestoPropio) {
        this.presupuestoPropio = presupuestoPropio;
    }

    public BigDecimal getPresupuestoFinanciamiento() {
        return presupuestoFinanciamiento;
    }

    public void setPresupuestoFinanciamiento(BigDecimal presupuestoFinanciamiento) {
        this.presupuestoFinanciamiento = presupuestoFinanciamiento;
    }

    public BigDecimal getEneroMeta() {
        return eneroMeta;
    }

    public void setEneroMeta(BigDecimal eneroMeta) {
        this.eneroMeta = eneroMeta;
    }

    public BigDecimal getFebreroMeta() {
        return febreroMeta;
    }

    public void setFebreroMeta(BigDecimal febreroMeta) {
        this.febreroMeta = febreroMeta;
    }

    public BigDecimal getMarzoMeta() {
        return marzoMeta;
    }

    public void setMarzoMeta(BigDecimal marzoMeta) {
        this.marzoMeta = marzoMeta;
    }

    public BigDecimal getAbrilMeta() {
        return abrilMeta;
    }

    public void setAbrilMeta(BigDecimal abrilMeta) {
        this.abrilMeta = abrilMeta;
    }

    public BigDecimal getMayoMeta() {
        return mayoMeta;
    }

    public void setMayoMeta(BigDecimal mayoMeta) {
        this.mayoMeta = mayoMeta;
    }

    public BigDecimal getJunioMeta() {
        return junioMeta;
    }

    public void setJunioMeta(BigDecimal junioMeta) {
        this.junioMeta = junioMeta;
    }

    public BigDecimal getJulioMeta() {
        return julioMeta;
    }

    public void setJulioMeta(BigDecimal julioMeta) {
        this.julioMeta = julioMeta;
    }

    public BigDecimal getAgostoMeta() {
        return agostoMeta;
    }

    public void setAgostoMeta(BigDecimal agostoMeta) {
        this.agostoMeta = agostoMeta;
    }

    public BigDecimal getSeptiembreMeta() {
        return septiembreMeta;
    }

    public void setSeptiembreMeta(BigDecimal septiembreMeta) {
        this.septiembreMeta = septiembreMeta;
    }

    public BigDecimal getOctubreMeta() {
        return octubreMeta;
    }

    public void setOctubreMeta(BigDecimal octubreMeta) {
        this.octubreMeta = octubreMeta;
    }

    public BigDecimal getNoviembreMeta() {
        return noviembreMeta;
    }

    public void setNoviembreMeta(BigDecimal noviembreMeta) {
        this.noviembreMeta = noviembreMeta;
    }

    public BigDecimal getDiciembreMeta() {
        return diciembreMeta;
    }

    public void setDiciembreMeta(BigDecimal diciembreMeta) {
        this.diciembreMeta = diciembreMeta;
    }

    public BigDecimal getSemestreUnoMeta() {
        return semestreUnoMeta;
    }

    public void setSemestreUnoMeta(BigDecimal semestreUnoMeta) {
        this.semestreUnoMeta = semestreUnoMeta;
    }

    public BigDecimal getSemestrDosMeta() {
        return semestrDosMeta;
    }

    public void setSemestrDosMeta(BigDecimal semestrDosMeta) {
        this.semestrDosMeta = semestrDosMeta;
    }

    public BigDecimal getTrimestreUnoMeta() {
        return trimestreUnoMeta;
    }

    public void setTrimestreUnoMeta(BigDecimal trimestreUnoMeta) {
        this.trimestreUnoMeta = trimestreUnoMeta;
    }

    public BigDecimal getTrimestreDosMeta() {
        return trimestreDosMeta;
    }

    public void setTrimestreDosMeta(BigDecimal trimestreDosMeta) {
        this.trimestreDosMeta = trimestreDosMeta;
    }

    public BigDecimal getTrimestreTresMeta() {
        return trimestreTresMeta;
    }

    public void setTrimestreTresMeta(BigDecimal trimestreTresMeta) {
        this.trimestreTresMeta = trimestreTresMeta;
    }

    public BigDecimal getTrimestreCuatroMeta() {
        return trimestreCuatroMeta;
    }

    public void setTrimestreCuatroMeta(BigDecimal trimestreCuatroMeta) {
        this.trimestreCuatroMeta = trimestreCuatroMeta;
    }

    public BigDecimal getCuatrimestreUnoMeta() {
        return cuatrimestreUnoMeta;
    }

    public void setCuatrimestreUnoMeta(BigDecimal cuatrimestreUnoMeta) {
        this.cuatrimestreUnoMeta = cuatrimestreUnoMeta;
    }

    public BigDecimal getCuatrimestreDosMeta() {
        return cuatrimestreDosMeta;
    }

    public void setCuatrimestreDosMeta(BigDecimal cuatrimestreDosMeta) {
        this.cuatrimestreDosMeta = cuatrimestreDosMeta;
    }

    public BigDecimal getCuatrimestreTresMeta() {
        return cuatrimestreTresMeta;
    }

    public void setCuatrimestreTresMeta(BigDecimal cuatrimestreTresMeta) {
        this.cuatrimestreTresMeta = cuatrimestreTresMeta;
    }

    public CatalogoItem getDistribucionMeta() {
        return distribucionMeta;
    }

    public void setDistribucionMeta(CatalogoItem distribucionMeta) {
        this.distribucionMeta = distribucionMeta;
    }

    public Integer getAnioPresPar() {
        return anioPresPar;
    }

    public void setAnioPresPar(Integer anioPresPar) {
        this.anioPresPar = anioPresPar;
    }

    public BigDecimal getJunioReforma() {
        return junioReforma;
    }

    public void setJunioReforma(BigDecimal junioReforma) {
        this.junioReforma = junioReforma;
    }
    
    public Short getPresParticipacionAnio() {
        return presParticipacionAnio;
    }

    public void setPresParticipacionAnio(Short presParticipacionAnio) {
        this.presParticipacionAnio = presParticipacionAnio;
    }
    
    

    public boolean sumaDsitribuccion() {

        BigDecimal calculoUno = BigDecimal.ZERO;

        BigDecimal calculoDos = BigDecimal.ZERO;

        BigDecimal calculoTres = BigDecimal.ZERO;

        BigDecimal result = BigDecimal.ZERO;
        encerarValores();
        System.out.println("distribucionMeta.getCodigo() " + tipoDistribuccion.getCodigo());
        if (tipoDistribuccion != null) {
            switch (tipoDistribuccion.getCodigo()) {
                case "S":
                    result = semestreUno.setScale(2, RoundingMode.HALF_UP).add(semestrDos.setScale(2, RoundingMode.HALF_UP));
                    break;
                case "T":
                    calculoUno = trimestreUno.setScale(2, RoundingMode.HALF_UP).add(trimestreDos.setScale(2, RoundingMode.HALF_UP));
                    calculoDos = trimestreTres.setScale(2, RoundingMode.HALF_UP).add(trimestreCuatro.setScale(2, RoundingMode.HALF_UP));
                    result = calculoUno.add(calculoDos);
                    break;
                case "C":
                    result = cuatrimestreUno.setScale(2, RoundingMode.HALF_UP).add(cuatrimestreDos.setScale(2, RoundingMode.HALF_UP)).add(cuatrimestreTres.setScale(2, RoundingMode.HALF_UP));
                    break;
                case "M":
                    calculoUno = enero.add(febrero).add(marzo).add(abril).setScale(2, RoundingMode.HALF_UP);
                    calculoDos = mayo.add(junio).add(julio).add(agosto).setScale(2, RoundingMode.HALF_UP);
                    calculoTres = septiembre.add(octubre).add(noviembre).add(diciembre).setScale(2, RoundingMode.HALF_UP);

                    result = calculoUno.add(calculoDos).add(calculoTres).setScale(2, RoundingMode.HALF_UP);
                    break;
            }
        }

        System.out.println("medicion meta" + monto.setScale(2, RoundingMode.HALF_UP));
        System.out.println("result" + result.setScale(2, RoundingMode.HALF_UP));

        if (monto.setScale(2, RoundingMode.HALF_UP).doubleValue() == result.setScale(2, RoundingMode.HALF_UP).doubleValue()) {
            return true;
        }

        return false;
    }

    public boolean sumaDsitribuccionReformado() {

        BigDecimal calculoUno = BigDecimal.ZERO;

        BigDecimal calculoDos = BigDecimal.ZERO;

        BigDecimal calculoTres = BigDecimal.ZERO;

        BigDecimal result = BigDecimal.ZERO;
        encerarValores();

        if (tipoDistribuccion != null) {
            switch (tipoDistribuccion.getCodigo()) {
                case "S":
                    result = semestreUno.setScale(2, RoundingMode.HALF_UP).add(semestrDos.setScale(2, RoundingMode.HALF_UP));
                    break;
                case "T":
                    calculoUno = trimestreUno.setScale(2, RoundingMode.HALF_UP).add(trimestreDos.setScale(2, RoundingMode.HALF_UP));
                    calculoDos = trimestreTres.setScale(2, RoundingMode.HALF_UP).add(trimestreCuatro.setScale(2, RoundingMode.HALF_UP));
                    result = calculoUno.add(calculoDos);
                    break;
                case "C":
                    result = cuatrimestreUno.setScale(2, RoundingMode.HALF_UP).add(cuatrimestreDos.setScale(2, RoundingMode.HALF_UP)).add(cuatrimestreTres.setScale(2, RoundingMode.HALF_UP));
                    break;
                case "M":
                    calculoUno = enero.add(febrero).add(marzo).add(abril).setScale(2, RoundingMode.HALF_UP);
                    calculoDos = mayo.add(junio).add(julio).add(agosto).setScale(2, RoundingMode.HALF_UP);
                    calculoTres = septiembre.add(octubre).add(noviembre).add(diciembre).setScale(2, RoundingMode.HALF_UP);

                    result = calculoUno.add(calculoDos).add(calculoTres).setScale(2, RoundingMode.HALF_UP);
                    break;
            }
        }

        if (monotReformado.setScale(2, RoundingMode.HALF_UP).doubleValue() == result.setScale(2, RoundingMode.HALF_UP).doubleValue()) {
            return true;
        }

        return false;
    }

    public void distribucionMesesReforma() {
        this.eneroReforma = enero;
        this.febreroReforma = febrero;
        this.marzoReforma = marzo;
        this.abrilReforma = abril;
        this.mayoReforma = mayo;
        this.junioReforma = junio;
        this.julioReforma = julio;
        this.agostoReforma = agosto;
        this.septiembreReforma = septiembre;
        this.octubreReforma = octubre;
        this.noviembreReforma = noviembre;
        this.diciembreReforma = diciembre;
    }

    public void encerarMesesOriginales() {
        this.enero = BigDecimal.ZERO;
        this.febrero = BigDecimal.ZERO;
        this.marzo = BigDecimal.ZERO;
        this.abril = BigDecimal.ZERO;
        this.mayo = BigDecimal.ZERO;
        this.junio = BigDecimal.ZERO;
        this.julio = BigDecimal.ZERO;
        this.agosto = BigDecimal.ZERO;
        this.septiembre = BigDecimal.ZERO;
        this.octubre = BigDecimal.ZERO;
        this.noviembre = BigDecimal.ZERO;
        this.diciembre = BigDecimal.ZERO;
    }

    public boolean sumaDsitribuccionMeta() {
        BigDecimal calculoUno = BigDecimal.ZERO;

        BigDecimal calculoDos = BigDecimal.ZERO;

        BigDecimal calculoTres = BigDecimal.ZERO;

        BigDecimal result = BigDecimal.ZERO;
        encerarValores();
        if (distribucionMeta != null) {
            switch (distribucionMeta.getCodigo()) {

                case "S":
                    result = semestreUnoMeta.setScale(2, RoundingMode.HALF_UP).add(semestrDosMeta.setScale(2, RoundingMode.HALF_UP));
                    break;
                case "T":
                    calculoUno = trimestreUnoMeta.setScale(2, RoundingMode.HALF_UP).add(trimestreDosMeta.setScale(2, RoundingMode.HALF_UP));
                    calculoDos = trimestreTresMeta.setScale(2, RoundingMode.HALF_UP).add(trimestreCuatroMeta.setScale(2, RoundingMode.HALF_UP));
                    result = calculoUno.add(calculoDos);
                    break;
                case "C":
                    result = cuatrimestreUnoMeta.setScale(2, RoundingMode.HALF_UP).add(cuatrimestreDosMeta.setScale(2, RoundingMode.HALF_UP)).add(cuatrimestreTresMeta.setScale(2, RoundingMode.HALF_UP));
                    break;
                case "M":
                    calculoUno = eneroMeta.add(febreroMeta).add(marzoMeta).add(abrilMeta).setScale(2, RoundingMode.HALF_UP);
                    calculoDos = mayoMeta.add(junioMeta).add(julioMeta).add(agostoMeta).setScale(2, RoundingMode.HALF_UP);
                    calculoTres = septiembreMeta.add(octubreMeta).add(noviembreMeta).add(diciembreMeta).setScale(2, RoundingMode.HALF_UP);

                    result = calculoUno.add(calculoDos).add(calculoTres).setScale(2, RoundingMode.HALF_UP);
                    break;
            }
        }

        if (medicionMeta.setScale(2, RoundingMode.HALF_UP).doubleValue() == result.setScale(2, RoundingMode.HALF_UP).doubleValue()) {
            return true;
        }

        return false;
    }

    public void distribuirMeta() {
        if (distribucionMeta != null) {
            switch (distribucionMeta.getCodigo()) {
                case "S":
                    junioMeta = semestreUnoMeta;
                    diciembreMeta = semestrDosMeta;
                    break;
                case "T":
                    marzoMeta = trimestreUnoMeta;
                    junioMeta = trimestreDosMeta;
                    septiembreMeta = trimestreTresMeta;
                    diciembreMeta = trimestreCuatroMeta;
                    break;
                case "C":
                    abrilMeta = cuatrimestreUnoMeta;
                    agostoMeta = cuatrimestreDosMeta;
                    diciembreMeta = cuatrimestreTresMeta;
                    break;

            }
        }
    }

    public void distribuirMonto() {
        if (tipoDistribuccion != null) {
            switch (tipoDistribuccion.getCodigo()) {
                case "S":
                    junio = semestreUno;
                    diciembre = semestrDos;
                    break;
                case "T":
                    marzo = trimestreUno;
                    junio = trimestreDos;
                    septiembre = trimestreTres;
                    diciembre = trimestreCuatro;
                    break;
                case "C":
                    abril = cuatrimestreUno;
                    agosto = cuatrimestreDos;
                    diciembre = cuatrimestreTres;
                    break;

            }
        }
    }

    public void reversarDistribucionMeta() {
        if (distribucionMeta != null) {
            switch (distribucionMeta.getCodigo()) {
                case "S":
                    semestreUnoMeta = junioMeta;
                    semestrDosMeta = diciembreMeta;
                    break;
                case "T":
                    trimestreUnoMeta = marzoMeta;
                    trimestreDosMeta = junioMeta;
                    trimestreTresMeta = septiembreMeta;
                    trimestreCuatroMeta = diciembreMeta;
                    break;
                case "C":
                    cuatrimestreUnoMeta = abrilMeta;
                    cuatrimestreDosMeta = agostoMeta;
                    cuatrimestreTresMeta = diciembreMeta;
                    break;

            }
        }

    }

    public void reformandoValores() {
        if (tipoDistribuccion != null) {
            switch (tipoDistribuccion.getCodigo()) {
                case "S":
                    junioReforma = semestreUno;
                    diciembreReforma = semestrDos;
                    break;
                case "T":
                    marzoReforma = trimestreUno;
                    junioReforma = trimestreDos;
                    septiembreReforma = trimestreTres;
                    diciembreReforma = trimestreCuatro;
                    break;
                case "C":
                    abrilReforma = cuatrimestreUno;
                    agostoReforma = cuatrimestreDos;
                    diciembreReforma = cuatrimestreTres;
                    break;

            }
        }

    }

    public void reversandoReformandoValores() {
        if (tipoDistribuccion != null) {
            switch (tipoDistribuccion.getCodigo()) {
                case "S":
                    semestreUno = junioReforma;
                    semestrDos = diciembreReforma;
                    break;
                case "T":
                    trimestreUno = marzoReforma;
                    trimestreDos = junioReforma;
                    trimestreTres = septiembreReforma;
                    trimestreCuatro = diciembreReforma;
                    break;
                case "C":
                    cuatrimestreUno = abrilReforma;
                    cuatrimestreDos = agostoReforma;
                    cuatrimestreTres = diciembreReforma;
                    break;

            }
        }
    }

    public void reversarDistribuirMonto() {
        if (tipoDistribuccion != null) {
            switch (tipoDistribuccion.getCodigo()) {
                case "S":
                    semestreUno = junio;
                    semestrDos = diciembre;
                    break;
                case "T":
                    trimestreUno = marzo;
                    trimestreDos = junio;
                    trimestreTres = septiembre;
                    trimestreCuatro = diciembre;
                    break;
                case "C":
                    cuatrimestreUno = abril;
                    cuatrimestreDos = agosto;
                    cuatrimestreTres = diciembre;
                    break;

            }
        }
    }

    public void encerarValores() {

        if (enero == null) {
            enero = BigDecimal.ZERO;
        }
        if (eneroMeta == null) {
            eneroMeta = BigDecimal.ZERO;
        }
        if (febrero == null) {
            febrero = BigDecimal.ZERO;
        }
        if (febreroMeta == null) {
            febreroMeta = BigDecimal.ZERO;
        }
        if (marzo == null) {
            marzo = BigDecimal.ZERO;
        }
        if (marzoMeta == null) {
            marzoMeta = BigDecimal.ZERO;
        }
        if (abril == null) {
            abril = BigDecimal.ZERO;
        }
        if (abrilMeta == null) {
            abrilMeta = BigDecimal.ZERO;
        }
        if (mayo == null) {
            mayo = BigDecimal.ZERO;
        }
        if (mayoMeta == null) {
            mayoMeta = BigDecimal.ZERO;
        }
        if (junio == null) {
            junio = BigDecimal.ZERO;
        }
        if (junioMeta == null) {
            junioMeta = BigDecimal.ZERO;
        }
        if (julio == null) {
            julio = BigDecimal.ZERO;
        }
        if (julioMeta == null) {
            julioMeta = BigDecimal.ZERO;
        }
        if (agosto == null) {
            agosto = BigDecimal.ZERO;
        }
        if (agostoMeta == null) {
            agostoMeta = BigDecimal.ZERO;
        }
        if (septiembre == null) {
            septiembre = BigDecimal.ZERO;
        }
        if (septiembreMeta == null) {
            septiembreMeta = BigDecimal.ZERO;
        }
        if (octubre == null) {
            octubre = BigDecimal.ZERO;
        }
        if (octubreMeta == null) {
            octubreMeta = BigDecimal.ZERO;
        }
        if (noviembre == null) {
            noviembre = BigDecimal.ZERO;
        }
        if (noviembreMeta == null) {
            noviembreMeta = BigDecimal.ZERO;
        }
        if (diciembre == null) {
            diciembre = BigDecimal.ZERO;
        }
        if (diciembreMeta == null) {
            diciembreMeta = BigDecimal.ZERO;
        }

        if (cuatrimestreUno == null) {
            cuatrimestreUno = BigDecimal.ZERO;
        }

        if (cuatrimestreUnoMeta == null) {
            cuatrimestreUnoMeta = BigDecimal.ZERO;
        }

        if (cuatrimestreDos == null) {
            cuatrimestreDos = BigDecimal.ZERO;
        }

        if (cuatrimestreDosMeta == null) {
            cuatrimestreDosMeta = BigDecimal.ZERO;
        }

        if (cuatrimestreTres == null) {
            cuatrimestreTres = BigDecimal.ZERO;
        }

        if (cuatrimestreTresMeta == null) {
            cuatrimestreTresMeta = BigDecimal.ZERO;
        }

        if (trimestreUno == null) {
            trimestreUno = BigDecimal.ZERO;
        }

        if (trimestreUnoMeta == null) {
            trimestreUnoMeta = BigDecimal.ZERO;
        }

        if (trimestreDos == null) {
            trimestreDos = BigDecimal.ZERO;
        }

        if (trimestreDosMeta == null) {
            trimestreDosMeta = BigDecimal.ZERO;
        }

        if (trimestreTres == null) {
            trimestreTres = BigDecimal.ZERO;
        }
        if (trimestreTresMeta == null) {
            trimestreTresMeta = BigDecimal.ZERO;
        }
        if (trimestreCuatro == null) {
            trimestreCuatro = BigDecimal.ZERO;

        }
        if (trimestreCuatroMeta == null) {
            trimestreCuatroMeta = BigDecimal.ZERO;
        }

        if (semestreUno == null) {
            semestreUno = BigDecimal.ZERO;
        }
        if (semestreUnoMeta == null) {
            semestreUnoMeta = BigDecimal.ZERO;
        }
        if (semestrDos == null) {
            semestrDos = BigDecimal.ZERO;
        }

        if (semestrDosMeta == null) {
            semestrDosMeta = BigDecimal.ZERO;
        }

        if (eneroMetaReforma == null) {
            eneroMetaReforma = BigDecimal.ZERO;
        }

        if (eneroReforma == null) {
            eneroReforma = BigDecimal.ZERO;
        }
        if (febreoMetaReforma == null) {
            febreoMetaReforma = BigDecimal.ZERO;

        }
        if (febreroReforma == null) {
            febreroReforma = BigDecimal.ZERO;
        }
        if (marzoMetaReforma == null) {
            marzoMetaReforma = BigDecimal.ZERO;
        }
        if (marzoReforma == null) {
            marzoReforma = BigDecimal.ZERO;

        }
        if (abrilMetaReforma == null) {
            abrilMetaReforma = BigDecimal.ZERO;

        }
        if (abrilReforma == null) {
            abrilReforma = BigDecimal.ZERO;
        }
        if (mayoMetaReforma == null) {
            mayoMetaReforma = BigDecimal.ZERO;
        }
        if (mayoReforma == null) {
            mayoReforma = BigDecimal.ZERO;

        }
        if (junioMetaReforma == null) {
            junioMetaReforma = BigDecimal.ZERO;

        }
        if (junioReforma == null) {
            junioReforma = BigDecimal.ZERO;

        }
        if (julioMetaReforma == null) {
            julioMetaReforma = BigDecimal.ZERO;

        }
        if (julioReforma == null) {
            julioReforma = BigDecimal.ZERO;

        }

        if (agostoMetaReforma == null) {
            agostoMetaReforma = BigDecimal.ZERO;

        }
        if (agostoReforma == null) {
            agostoReforma = BigDecimal.ZERO;

        }
        if (septiembreMetaReforma == null) {
            septiembreMetaReforma = BigDecimal.ZERO;

        }
        if (septiembreReforma == null) {
            septiembreReforma = BigDecimal.ZERO;

        }

        if (octubreMetaReforma == null) {
            octubreMetaReforma = BigDecimal.ZERO;

        }

        if (octubreReforma == null) {
            octubreReforma = BigDecimal.ZERO;

        }

        if (noviembreMetaReforma == null) {
            noviembreMetaReforma = BigDecimal.ZERO;

        }

        if (noviembreReforma == null) {
            noviembreReforma = BigDecimal.ZERO;

        }

        if (diciembreMetaReforma == null) {
            diciembreMetaReforma = BigDecimal.ZERO;

        }

        if (diciembreReforma == null) {
            diciembreReforma = BigDecimal.ZERO;

        }

    }

    public void distribuirMontoReforma() {
        if (tipoDistribuccion != null) {
            switch (tipoDistribuccion.getCodigo()) {
                case "S":
                    junioReforma = semestreUno;
                    diciembreReforma = semestrDos;
                    break;
                case "T":
                    marzoReforma = trimestreUno;
                    junioReforma = trimestreDos;
                    septiembreReforma = trimestreTres;
                    diciembreReforma = trimestreCuatro;
                    break;
                case "C":
                    abrilReforma = cuatrimestreUno;
                    agostoReforma = cuatrimestreDos;
                    diciembreReforma = cuatrimestreTres;
                    break;

            }
        }
    }
    
    
    public BigDecimal getEneroReforma() {
        return eneroReforma;
    }

    public void setEneroReforma(BigDecimal eneroReforma) {
        this.eneroReforma = eneroReforma;
    }

    public BigDecimal getFebreroReforma() {
        return febreroReforma;
    }

    public void setFebreroReforma(BigDecimal febreroReforma) {
        this.febreroReforma = febreroReforma;
    }

    public BigDecimal getMarzoReforma() {
        return marzoReforma;
    }

    public void setMarzoReforma(BigDecimal marzoReforma) {
        this.marzoReforma = marzoReforma;
    }

    public BigDecimal getAbrilReforma() {
        return abrilReforma;
    }

    public void setAbrilReforma(BigDecimal abrilReforma) {
        this.abrilReforma = abrilReforma;
    }

    public BigDecimal getMayoReforma() {
        return mayoReforma;
    }

    public void setMayoReforma(BigDecimal mayoReforma) {
        this.mayoReforma = mayoReforma;
    }

    public BigDecimal getJunioreforma() {
        return junioReforma;
    }

    public void setJunioreforma(BigDecimal junioReforma) {
        this.junioReforma = junioReforma;
    }

    public BigDecimal getJulioReforma() {
        return julioReforma;
    }

    public void setJulioReforma(BigDecimal julioReforma) {
        this.julioReforma = julioReforma;
    }

    public BigDecimal getAgostoReforma() {
        return agostoReforma;
    }

    public void setAgostoReforma(BigDecimal agostoReforma) {
        this.agostoReforma = agostoReforma;
    }

    public BigDecimal getSeptiembreReforma() {
        return septiembreReforma;
    }

    public void setSeptiembreReforma(BigDecimal septiembreReforma) {
        this.septiembreReforma = septiembreReforma;
    }

    public BigDecimal getOctubreReforma() {
        return octubreReforma;
    }

    public void setOctubreReforma(BigDecimal octubreReforma) {
        this.octubreReforma = octubreReforma;
    }

    public BigDecimal getNoviembreReforma() {
        return noviembreReforma;
    }

    public void setNoviembreReforma(BigDecimal noviembreReforma) {
        this.noviembreReforma = noviembreReforma;
    }

    public BigDecimal getDiciembreReforma() {
        return diciembreReforma;
    }

    public void setDiciembreReforma(BigDecimal diciembreReforma) {
        this.diciembreReforma = diciembreReforma;
    }

    public BigDecimal getEneroMetaReforma() {
        return eneroMetaReforma;
    }

    public void setEneroMetaReforma(BigDecimal eneroMetaReforma) {
        this.eneroMetaReforma = eneroMetaReforma;
    }

    public BigDecimal getFebreoMetaReforma() {
        return febreoMetaReforma;
    }

    public void setFebreoMetaReforma(BigDecimal febreoMetaReforma) {
        this.febreoMetaReforma = febreoMetaReforma;
    }

    public BigDecimal getMarzoMetaReforma() {
        return marzoMetaReforma;
    }

    public void setMarzoMetaReforma(BigDecimal marzoMetaReforma) {
        this.marzoMetaReforma = marzoMetaReforma;
    }

    public BigDecimal getAbrilMetaReforma() {
        return abrilMetaReforma;
    }

    public void setAbrilMetaReforma(BigDecimal abrilMetaReforma) {
        this.abrilMetaReforma = abrilMetaReforma;
    }

    public BigDecimal getMayoMetaReforma() {
        return mayoMetaReforma;
    }

    public void setMayoMetaReforma(BigDecimal mayoMetaReforma) {
        this.mayoMetaReforma = mayoMetaReforma;
    }

    public BigDecimal getJunioMetaReforma() {
        return junioMetaReforma;
    }

    public void setJunioMetaReforma(BigDecimal junioMetaReforma) {
        this.junioMetaReforma = junioMetaReforma;
    }

    public BigDecimal getJulioMetaReforma() {
        return julioMetaReforma;
    }

    public void setJulioMetaReforma(BigDecimal julioMetaReforma) {
        this.julioMetaReforma = julioMetaReforma;
    }

    public BigDecimal getAgostoMetaReforma() {
        return agostoMetaReforma;
    }

    public void setAgostoMetaReforma(BigDecimal agostoMetaReforma) {
        this.agostoMetaReforma = agostoMetaReforma;
    }

    public BigDecimal getSeptiembreMetaReforma() {
        return septiembreMetaReforma;
    }

    public void setSeptiembreMetaReforma(BigDecimal septiembreMetaReforma) {
        this.septiembreMetaReforma = septiembreMetaReforma;
    }

    public BigDecimal getOctubreMetaReforma() {
        return octubreMetaReforma;
    }

    public void setOctubreMetaReforma(BigDecimal octubreMetaReforma) {
        this.octubreMetaReforma = octubreMetaReforma;
    }

    public BigDecimal getNoviembreMetaReforma() {
        return noviembreMetaReforma;
    }

    public void setNoviembreMetaReforma(BigDecimal noviembreMetaReforma) {
        this.noviembreMetaReforma = noviembreMetaReforma;
    }

    public BigDecimal getDiciembreMetaReforma() {
        return diciembreMetaReforma;
    }

    public void setDiciembreMetaReforma(BigDecimal diciembreMetaReforma) {
        this.diciembreMetaReforma = diciembreMetaReforma;
    }

}
