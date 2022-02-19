/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.entities.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;

/**
 *
 * @author Criss
 */
@Entity
@Table(name = "vista_general_plan_anual")
@NamedQueries({
    @NamedQuery(name = "VistaGeneralPlanAnual.findAll", query = "SELECT v FROM VistaGeneralPlanAnual v")})

public class VistaGeneralPlanAnual implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_")
    @Id
    private BigInteger id;
    @Size(max = 100)
    @Column(name = "eje_plan_nacional")
    private String ejePlanNacional;
    @Column(name = "objetivo_plan_nacional")
    private String objetivoPlanNacional;
    @Column(name = "politica_plan_nacional")
    private String politicaPlanNacional;
    @Column(name = "sistema_plan_local")
    private String sistemaPlanLocal;
    @Column(name = "objetivo_plan_local")
    private String objetivoPlanLocal;
    @Column(name = "politica_plan_local")
    private String politicaPlanLocal;
    @Column(name = "partida")
    private String partida;
    @Column(name = "nombre_plan_local_programa_proyecto")
    private String nombrePlanLocalProgramaProyecto;
    @Column(name = "numero_identificacion_programa_proyecto")
    private BigInteger numeroIdentificacionProgramaProyecto;
    @Column(name = "nombre_programa_proyecto")
    private String nombreProgramaProyecto;
    @Column(name = "nombre_actividad")
    private String nombreActividad;
    @Column(name = "objetivo_operativo")
    private String objetivoOperativo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "medicion_meta")
    private BigDecimal medicionMeta;
    @Column(name = "cuatrimestre1_meta")
    private BigDecimal cuatrimestre1Meta;
    @Column(name = "cuatrimestre2_meta")
    private BigDecimal cuatrimestre2Meta;
    @Column(name = "cuatrimestre3_meta")
    private BigDecimal cuatrimestre3Meta;
    @Column(name = "cuatrimestre1_actividad")
    private BigDecimal cuatrimestre1Actividad;
    @Column(name = "cuatrimestre2_actividad")
    private BigDecimal cuatrimestre2Actividad;
    @Column(name = "cuatrimestre3_actividad")
    private BigDecimal cuatrimestre3Actividad;
    @Column(name = "indicador_meta")
    private String indicadorMeta;
    @Column(name = "unidad_responsable")
    private String unidadResponsable;
    @Column(name = "monto_actividad")
    private BigDecimal montoActividad;
    @Column(name = "nombre_producto")
    private String nombreProducto;
    @Column(name = "monto_producto")
    private BigDecimal montoProducto;
    @Column(name = "periodo_plan_anual_politica_publica")
    private Short periodoPlanAnualPoliticaPublica;
    @Column(name = "periodo_plan_anula_programa_proyecto")
    private Short periodoPlanAnulaProgramaProyecto;
    @Column(name = "periodo_actividad_operativa")
    private Short periodoActividadOperativa;
    @Column(name = "periodo_producto")
    private Short periodoProducto;
    @Column(name = "reserva")
    private BigDecimal reserva;
    @Column(name = "saldo_disponible")
    private BigDecimal saldoDisponible;
    @Column(name = "traspaso_incremento")
    private BigDecimal traspasoIncremento;
    @Column(name = "traspaso_reduccion")
    private BigDecimal traspasoReduccion;
    @Column(name = "suplemento_egreso")
    private BigDecimal suplementoEgreso;
    @Column(name = "monto_reformada")
    private BigDecimal montoReformada;
    @Column(name = "codigo_reforma")
    private BigInteger codigoReforma;
    @Column(name = "numero_orden_id")
    private BigInteger numeroOrdenId;
    @Column(name = "numero_tramite")
    private Short numeroTramite;
    @Column(name = "comprometido")
    private BigDecimal comprometido;
    @Column(name = "reduccion_egreso")
    private BigDecimal reduccionEgreso;
    @Column(name = "estado_papp")
    private BigInteger estadoPapp;
    @Column(name = "estado_plananual")
    private BigInteger estadoPlananual;
    @Column(name = "estado_planpolitica")
    private BigInteger estadoPlanpolitica;
    @Column(name = "estado_planlocal")
    private BigInteger estadoPlanlocal;
    @Column(name = "estado_actividad")
    private BigInteger estadoActividad;
    @Column(name = "codigo_producto")
    private BigInteger codigoProducto;
    @Column(name = "codigo_plananual")
    private BigInteger codigoPlananual;
    @Column(name = "codigo_planpolitica")
    private BigInteger codigoPlanpolitica;
    @Column(name = "codigo_planlocal")
    private BigInteger codigoPlanlocal;
    @Column(name = "codigo_actividad")
    private BigInteger codigoActividad;
    @Column(name = "codigo_producto_traspaso")
    private BigInteger codigoProductoTraspaso;
    @Column(name = "codigo_plananual_traspaso")
    private BigInteger codigoPlananualTraspaso;
    @Column(name = "codigo_planpolitica_traspaso")
    private BigInteger codigoPlanpoliticaTraspaso;
    @Column(name = "codigo_planlocal_traspaso")
    private BigInteger codigoPlanlocalTraspaso;
    @Column(name = "codigo_actividad_trapaso")
    private BigInteger codigoActividadTrapaso;
    @Column(name = "reservado")
    private BigDecimal reservado;
    @Column(name = "comprometido_final")
    private BigDecimal comprometidoFinal;
    @Column(name = "ejecutado_final")
    private BigDecimal ejecutadoFinal;
    @Column(name = "programa")
    private String programa;
    @Column(name = "plan")
    private String plan;
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

//    NUEVO
    @Column(name = "pai")
    private Boolean pai;
    @Column(name = "presupuesto_participacion")
    private Boolean presParticipativo;
    @Column(name = "pres_participacion_anio")
    private Short anioPresParticip;
    @Column(name = "grupo_atencion")
    private Boolean grupAtenPriori;
    @Column(name = "tipo_componente")
    private String tipoComponente;
    @Column(name = "nuevo_arrastre")
    private Boolean nuevoArrastre;
    @Column(name = "descripcion_meta")
    private String descripcionMeta;
    @Column(name = "medicion_porcentaje")
    private Boolean medicionPorcentaje;
    @Column(name = "distribucion_meta")
    private String distribucionMeta;
    @Column(name = "meta_1t")
    private BigDecimal meta1t;
    @Column(name = "meta_2t")
    private BigDecimal meta2t;
    @Column(name = "meta_3t")
    private BigDecimal meta3t;
    @Column(name = "meta_4t")
    private BigDecimal meta4t;
    @Column(name = "tipo_distribucion")
    private String tipoDistribucion;
    @Column(name = "presupuesto_propio")
    private BigDecimal presupuestoPropio;
    @Column(name = "presupuesto_fina")
    private BigDecimal presupuestoFinan;
    @Column(name = "id_producto")
    private Long idProducto;

    public VistaGeneralPlanAnual() {
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getEjePlanNacional() {
        return ejePlanNacional;
    }

    public void setEjePlanNacional(String ejePlanNacional) {
        this.ejePlanNacional = ejePlanNacional;
    }

    public String getObjetivoPlanNacional() {
        return objetivoPlanNacional;
    }

    public void setObjetivoPlanNacional(String objetivoPlanNacional) {
        this.objetivoPlanNacional = objetivoPlanNacional;
    }

    public String getPoliticaPlanNacional() {
        return politicaPlanNacional;
    }

    public void setPoliticaPlanNacional(String politicaPlanNacional) {
        this.politicaPlanNacional = politicaPlanNacional;
    }

    public String getSistemaPlanLocal() {
        return sistemaPlanLocal;
    }

    public void setSistemaPlanLocal(String sistemaPlanLocal) {
        this.sistemaPlanLocal = sistemaPlanLocal;
    }

    public String getObjetivoPlanLocal() {
        return objetivoPlanLocal;
    }

    public void setObjetivoPlanLocal(String objetivoPlanLocal) {
        this.objetivoPlanLocal = objetivoPlanLocal;
    }

    public String getPoliticaPlanLocal() {
        return politicaPlanLocal;
    }

    public void setPoliticaPlanLocal(String politicaPlanLocal) {
        this.politicaPlanLocal = politicaPlanLocal;
    }

    public String getNombrePlanLocalProgramaProyecto() {
        return nombrePlanLocalProgramaProyecto;
    }

    public void setNombrePlanLocalProgramaProyecto(String nombrePlanLocalProgramaProyecto) {
        this.nombrePlanLocalProgramaProyecto = nombrePlanLocalProgramaProyecto;
    }

    public BigInteger getNumeroIdentificacionProgramaProyecto() {
        return numeroIdentificacionProgramaProyecto;
    }

    public void setNumeroIdentificacionProgramaProyecto(BigInteger numeroIdentificacionProgramaProyecto) {
        this.numeroIdentificacionProgramaProyecto = numeroIdentificacionProgramaProyecto;
    }

    public String getNombreProgramaProyecto() {
        return nombreProgramaProyecto;
    }

    public void setNombreProgramaProyecto(String nombreProgramaProyecto) {
        this.nombreProgramaProyecto = nombreProgramaProyecto;
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

    public BigDecimal getCuatrimestre1Meta() {
        return cuatrimestre1Meta;
    }

    public void setCuatrimestre1Meta(BigDecimal cuatrimestre1Meta) {
        this.cuatrimestre1Meta = cuatrimestre1Meta;
    }

    public BigDecimal getCuatrimestre2Meta() {
        return cuatrimestre2Meta;
    }

    public void setCuatrimestre2Meta(BigDecimal cuatrimestre2Meta) {
        this.cuatrimestre2Meta = cuatrimestre2Meta;
    }

    public BigDecimal getCuatrimestre3Meta() {
        return cuatrimestre3Meta;
    }

    public void setCuatrimestre3Meta(BigDecimal cuatrimestre3Meta) {
        this.cuatrimestre3Meta = cuatrimestre3Meta;
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

    public String getIndicadorMeta() {
        return indicadorMeta;
    }

    public void setIndicadorMeta(String indicadorMeta) {
        this.indicadorMeta = indicadorMeta;
    }

    public String getUnidadResponsable() {
        return unidadResponsable;
    }

    public void setUnidadResponsable(String unidadResponsable) {
        this.unidadResponsable = unidadResponsable;
    }

    public BigDecimal getMontoActividad() {
        return montoActividad;
    }

    public void setMontoActividad(BigDecimal montoActividad) {
        this.montoActividad = montoActividad;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public BigDecimal getMontoProducto() {
        return montoProducto;
    }

    public void setMontoProducto(BigDecimal montoProducto) {
        this.montoProducto = montoProducto;
    }

    public Short getPeriodoPlanAnualPoliticaPublica() {
        return periodoPlanAnualPoliticaPublica;
    }

    public void setPeriodoPlanAnualPoliticaPublica(Short periodoPlanAnualPoliticaPublica) {
        this.periodoPlanAnualPoliticaPublica = periodoPlanAnualPoliticaPublica;
    }

    public Short getPeriodoPlanAnulaProgramaProyecto() {
        return periodoPlanAnulaProgramaProyecto;
    }

    public void setPeriodoPlanAnulaProgramaProyecto(Short periodoPlanAnulaProgramaProyecto) {
        this.periodoPlanAnulaProgramaProyecto = periodoPlanAnulaProgramaProyecto;
    }

    public Short getPeriodoActividadOperativa() {
        return periodoActividadOperativa;
    }

    public void setPeriodoActividadOperativa(Short periodoActividadOperativa) {
        this.periodoActividadOperativa = periodoActividadOperativa;
    }

    public Short getPeriodoProducto() {
        return periodoProducto;
    }

    public void setPeriodoProducto(Short periodoProducto) {
        this.periodoProducto = periodoProducto;
    }

    public BigDecimal getReserva() {
        return reserva;
    }

    public void setReserva(BigDecimal reserva) {
        this.reserva = reserva;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public BigDecimal getTraspasoIncremento() {
        return traspasoIncremento;
    }

    public void setTraspasoIncremento(BigDecimal traspasoIncremento) {
        this.traspasoIncremento = traspasoIncremento;
    }

    public BigDecimal getTraspasoReduccion() {
        return traspasoReduccion;
    }

    public void setTraspasoReduccion(BigDecimal traspasoReduccion) {
        this.traspasoReduccion = traspasoReduccion;
    }

    public BigDecimal getSuplementoEgreso() {
        return suplementoEgreso;
    }

    public void setSuplementoEgreso(BigDecimal suplementoEgreso) {
        this.suplementoEgreso = suplementoEgreso;
    }

    public BigDecimal getMontoReformada() {
        return montoReformada;
    }

    public void setMontoReformada(BigDecimal montoReformada) {
        this.montoReformada = montoReformada;
    }

    public BigInteger getCodigoReforma() {
        return codigoReforma;
    }

    public void setCodigoReforma(BigInteger codigoReforma) {
        this.codigoReforma = codigoReforma;
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

    public BigDecimal getComprometido() {
        return comprometido;
    }

    public void setComprometido(BigDecimal comprometido) {
        this.comprometido = comprometido;
    }

    public BigDecimal getReduccionEgreso() {
        return reduccionEgreso;
    }

    public void setReduccionEgreso(BigDecimal reduccionEgreso) {
        this.reduccionEgreso = reduccionEgreso;
    }

    public BigInteger getEstadoPapp() {
        return estadoPapp;
    }

    public void setEstadoPapp(BigInteger estadoPapp) {
        this.estadoPapp = estadoPapp;
    }

    public BigInteger getEstadoPlananual() {
        return estadoPlananual;
    }

    public void setEstadoPlananual(BigInteger estadoPlananual) {
        this.estadoPlananual = estadoPlananual;
    }

    public BigInteger getEstadoPlanpolitica() {
        return estadoPlanpolitica;
    }

    public void setEstadoPlanpolitica(BigInteger estadoPlanpolitica) {
        this.estadoPlanpolitica = estadoPlanpolitica;
    }

    public BigInteger getEstadoPlanlocal() {
        return estadoPlanlocal;
    }

    public void setEstadoPlanlocal(BigInteger estadoPlanlocal) {
        this.estadoPlanlocal = estadoPlanlocal;
    }

    public BigInteger getEstadoActividad() {
        return estadoActividad;
    }

    public void setEstadoActividad(BigInteger estadoActividad) {
        this.estadoActividad = estadoActividad;
    }

    public BigInteger getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(BigInteger codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public BigInteger getCodigoPlananual() {
        return codigoPlananual;
    }

    public void setCodigoPlananual(BigInteger codigoPlananual) {
        this.codigoPlananual = codigoPlananual;
    }

    public BigInteger getCodigoPlanpolitica() {
        return codigoPlanpolitica;
    }

    public void setCodigoPlanpolitica(BigInteger codigoPlanpolitica) {
        this.codigoPlanpolitica = codigoPlanpolitica;
    }

    public BigInteger getCodigoPlanlocal() {
        return codigoPlanlocal;
    }

    public void setCodigoPlanlocal(BigInteger codigoPlanlocal) {
        this.codigoPlanlocal = codigoPlanlocal;
    }

    public BigInteger getCodigoActividad() {
        return codigoActividad;
    }

    public void setCodigoActividad(BigInteger codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public BigInteger getCodigoProductoTraspaso() {
        return codigoProductoTraspaso;
    }

    public void setCodigoProductoTraspaso(BigInteger codigoProductoTraspaso) {
        this.codigoProductoTraspaso = codigoProductoTraspaso;
    }

    public BigInteger getCodigoPlananualTraspaso() {
        return codigoPlananualTraspaso;
    }

    public void setCodigoPlananualTraspaso(BigInteger codigoPlananualTraspaso) {
        this.codigoPlananualTraspaso = codigoPlananualTraspaso;
    }

    public BigInteger getCodigoPlanpoliticaTraspaso() {
        return codigoPlanpoliticaTraspaso;
    }

    public void setCodigoPlanpoliticaTraspaso(BigInteger codigoPlanpoliticaTraspaso) {
        this.codigoPlanpoliticaTraspaso = codigoPlanpoliticaTraspaso;
    }

    public BigInteger getCodigoPlanlocalTraspaso() {
        return codigoPlanlocalTraspaso;
    }

    public void setCodigoPlanlocalTraspaso(BigInteger codigoPlanlocalTraspaso) {
        this.codigoPlanlocalTraspaso = codigoPlanlocalTraspaso;
    }

    public BigInteger getCodigoActividadTrapaso() {
        return codigoActividadTrapaso;
    }

    public void setCodigoActividadTrapaso(BigInteger codigoActividadTrapaso) {
        this.codigoActividadTrapaso = codigoActividadTrapaso;
    }

    public String getPartida() {
        return partida;
    }

    public void setPartida(String partida) {
        this.partida = partida;
    }

    public BigDecimal getReservado() {
        return reservado;
    }

    public void setReservado(BigDecimal reservado) {
        this.reservado = reservado;
    }

    public BigDecimal getComprometidoFinal() {
        return comprometidoFinal;
    }

    public void setComprometidoFinal(BigDecimal comprometidoFinal) {
        this.comprometidoFinal = comprometidoFinal;
    }

    public BigDecimal getEjecutadoFinal() {
        return ejecutadoFinal;
    }

    public void setEjecutadoFinal(BigDecimal ejecutadoFinal) {
        this.ejecutadoFinal = ejecutadoFinal;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
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

    public Boolean getPai() {
        return pai;
    }

    public void setPai(Boolean pai) {
        this.pai = pai;
    }

    public Boolean getPresParticipativo() {
        return presParticipativo;
    }

    public void setPresParticipativo(Boolean presParticipativo) {
        this.presParticipativo = presParticipativo;
    }

    public Short getAnioPresParticip() {
        return anioPresParticip;
    }

    public void setAnioPresParticip(Short anioPresParticip) {
        this.anioPresParticip = anioPresParticip;
    }

    public Boolean getGrupAtenPriori() {
        return grupAtenPriori;
    }

    public void setGrupAtenPriori(Boolean grupAtenPriori) {
        this.grupAtenPriori = grupAtenPriori;
    }

    public String getTipoComponente() {
        return tipoComponente;
    }

    public void setTipoComponente(String tipoComponente) {
        this.tipoComponente = tipoComponente;
    }

    public Boolean getNuevoArrastre() {
        return nuevoArrastre;
    }

    public void setNuevoArrastre(Boolean nuevoArrastre) {
        this.nuevoArrastre = nuevoArrastre;
    }

    public String getDescripcionMeta() {
        return descripcionMeta;
    }

    public void setDescripcionMeta(String descripcionMeta) {
        this.descripcionMeta = descripcionMeta;
    }

    public Boolean getMedicionPorcentaje() {
        return medicionPorcentaje;
    }

    public void setMedicionPorcentaje(Boolean medicionPorcentaje) {
        this.medicionPorcentaje = medicionPorcentaje;
    }

    public String getDistribucionMeta() {
        return distribucionMeta;
    }

    public void setDistribucionMeta(String distribucionMeta) {
        this.distribucionMeta = distribucionMeta;
    }

    public BigDecimal getMeta1t() {
        return meta1t;
    }

    public void setMeta1t(BigDecimal meta1t) {
        this.meta1t = meta1t;
    }

    public BigDecimal getMeta2t() {
        return meta2t;
    }

    public void setMeta2t(BigDecimal meta2t) {
        this.meta2t = meta2t;
    }

    public BigDecimal getMeta3t() {
        return meta3t;
    }

    public void setMeta3t(BigDecimal meta3t) {
        this.meta3t = meta3t;
    }

    public BigDecimal getMeta4t() {
        return meta4t;
    }

    public void setMeta4t(BigDecimal meta4t) {
        this.meta4t = meta4t;
    }

    public String getTipoDistribucion() {
        return tipoDistribucion;
    }

    public void setTipoDistribucion(String tipoDistribucion) {
        this.tipoDistribucion = tipoDistribucion;
    }

    public BigDecimal getPresupuestoPropio() {
        return presupuestoPropio;
    }

    public void setPresupuestoPropio(BigDecimal presupuestoPropio) {
        this.presupuestoPropio = presupuestoPropio;
    }

    public BigDecimal getPresupuestoFinan() {
        return presupuestoFinan;
    }

    public void setPresupuestoFinan(BigDecimal presupuestoFinan) {
        this.presupuestoFinan = presupuestoFinan;
    }

    public Long getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Long idProducto) {
        this.idProducto = idProducto;
    }

}
