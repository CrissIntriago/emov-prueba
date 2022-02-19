/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.ComprobantePago;
import com.origami.sigef.common.entities.ControlCuentaContable;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.CuotaAnticipo;
import com.origami.sigef.common.entities.DetalleSolicitudCompromiso;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.common.entities.Garantias;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PartidasDistributivoAnexo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.Recaudacion;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.TipoRolBeneficios;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.CobrosEmisionesModel;
import com.origami.sigef.contabilidad.model.CuentaContablePresupuestoModel;
import com.origami.sigef.contabilidad.model.ServidorMontoModel;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Criss Intriago
 */
@Stateless
public class DiarioGeneralService extends AbstractService<DiarioGeneral> {
    
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    public DiarioGeneralService() {
        super(DiarioGeneral.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public ContDiarioGeneral getDiarioGeneralRetencionByNumTransaccion(Integer numeroTransaccion, Boolean retencion, Boolean retenido, short periodo) {
        try {
            ContDiarioGeneral diario = (ContDiarioGeneral) em.createQuery("SELECT d FROM ContDiarioGeneral d WHERE d.numRegistro = ?1 AND d.retencion = ?2 AND d.retenido = ?3 AND d.periodo = ?4")
                    .setParameter(1, numeroTransaccion)
                    .setParameter(2, retencion)
                    .setParameter(3, retenido)
                    .setParameter(4, periodo)
                    .getSingleResult();
            return diario;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<ContDiarioGeneralDetalle> findAllRetencionDiarioGeneral(ContDiarioGeneral diarioGeneral) {
        return em.createQuery("SELECT detalle FROM ContDiarioGeneralDetalle detalle JOIN detalle.idContDiarioGeneral diario WHERE EXISTS(SELECT cuentaRet FROM CuentaContableRetencion cuentaRet WHERE cuentaRet.cuentaContable = detalle.idContCuentas AND detalle.idContDiarioGeneral = ?1) ")
                .setParameter(1, diarioGeneral)
                .getResultList();
    }
    
    public DiarioGeneral numTramiteDiarioGeneral(Long numTramite) {
        try {
            DiarioGeneral diario = (DiarioGeneral) em.createQuery("SELECT d FROM DiarioGeneral d WHERE d.numTramite = ?1 AND d.comprobantePago=FALSE AND d.totalDebe>0 AND d.periodo=?2 ")
                    .setParameter(1, numTramite)
                    .setParameter(2, Utils.getAnio(new Date()).shortValue())
                    .getSingleResult();
            return diario;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public ComprobantePago getComprobanteProcess(Long numTramite) {
        try {
            ComprobantePago diario = (ComprobantePago) em.createQuery("SELECT d FROM ComprobantePago d WHERE d.numeroTramite = ?1 AND d.estado = 'REGISTRADO' AND d.periodo=?2 ")
                    .setParameter(1, BigInteger.valueOf(numTramite))
                    .setParameter(2, Utils.getAnio(new Date()).shortValue())
                    .getSingleResult();
            return diario;
        } catch (NoResultException e) {
            return null;
        }
    }

    /*ELIMINAR*/
    public Adquisiciones getAdquisicion(SolicitudReservaCompromiso reserva) {
        try {
            Adquisiciones resultado = (Adquisiciones) em.createQuery("SELECT a.contrato FROM ContratosReservaEjecuion a WHERE a.reserva=:reserva")
                    .setParameter("reserva", reserva)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<Adquisiciones> getAdquisicionList(SolicitudReservaCompromiso reserva) {
        List<Adquisiciones> resultado = (List<Adquisiciones>) em.createQuery("SELECT a.contrato FROM ContratosReservaEjecuion a WHERE a.reserva=:reserva")
                .setParameter("reserva", reserva)
                .getResultList();
        return resultado;
    }
    
    public List<BienesMovimiento> getBienesAdquisicion(Adquisiciones adquisicion, Short periodo) {
        List<BienesMovimiento> resultado = (List<BienesMovimiento>) em.createQuery("SELECT b FROM BienesMovimiento b "
                + "WHERE b.adquisiciones=:adquisicion AND b.periodo=:periodo AND b.estado= TRUE AND b.contabilizado=FALSE")
                .setParameter("adquisicion", adquisicion)
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;
    }
    
    public List<Inventario> getInventarioAdquisicion(Adquisiciones adquisicion, Short periodo) {
        List<Inventario> resultado = (List<Inventario>) em.createQuery("SELECT m FROM InventarioRegistro i INNER JOIN i.movimiento m "
                + "WHERE i.adquisiciones=:adquisicion AND m.anio=:periodo AND m.estado=TRUE AND m.contabilizado=FALSE ")
                .setParameter("adquisicion", adquisicion)
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;
    }
    
    public UnidadAdministrativa getUnidad(String nombreUnidad) {
        try {
            UnidadAdministrativa resultado = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.nombre=:nombreUnidad AND u.estado=TRUE")
                    .setParameter("nombreUnidad", nombreUnidad)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public CatalogoItem getEstadoAprobado(String codigo) {
        try {
            CatalogoItem resultado = (CatalogoItem) em.createQuery("SELECT ci FROM CatalogoItem ci WHERE ci.codigo=:codigo")
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public DiarioGeneral findAllDiarioGeneralClaseAndTipoAndPeriodo(Long idClase, Long idTipo, Short periodo) {
        try {
            List<DiarioGeneral> list = new ArrayList<>();
            list = em.createQuery("SELECT d FROM DiarioGeneral d WHERE d.clase.id = ?1 AND d.tipo.id = ?2 AND  d.periodo = ?3")
                    .setParameter(1, idClase)
                    .setParameter(2, idTipo)
                    .setParameter(3, periodo)
                    .getResultList();
            if (!list.isEmpty()) {
                return list.get(0);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public List<DiarioGeneral> showReservaCompromisoEjecutadas(SolicitudReservaCompromiso r) {
        List<DiarioGeneral> result = (List<DiarioGeneral>) em.createQuery("SELECT d FROM DiarioGeneral d WHERE d.certificacionesPresupuestario =:reserva")
                .setParameter("reserva", r).getResultList();
        return result;
    }
    
    public List<ContDiarioGeneralDetalle> showDetalleTransaccion(SolicitudReservaCompromiso d) {
        List<ContDiarioGeneralDetalle> result = (List<ContDiarioGeneralDetalle>) em.createQuery("SELECT d FROM ContDiarioGeneralDetalle  d INNER JOIN d.idContDiarioGeneral di where di.idDiarioGeneral is null and d.idDetalleReservaCompromiso=:reserva")
                .setParameter("reserva", BigInteger.valueOf(d.getId().longValue())).getResultList();
        return result;
    }
    
    public PartidasDistributivo getPartidaDistributiva(String partidaDistributiva, Short periodo, Servidor servidor) {
        try {
            PartidasDistributivo resultado = (PartidasDistributivo) em.createQuery("SELECT pd FROM PartidasDistributivo pd "
                    + "INNER JOIN pd.distributivo dis "
                    + "INNER JOIN dis.distributivo d "
                    + "INNER JOIN d.servidorPublico s "
                    + "INNER JOIN s.persona p "
                    + "WHERE p.identificacion=:servidor AND pd.periodo=:periodo "
                    + "AND pd.partidaAp=:partidaDistributiva AND pd.codigoReforma is null "
                    + "AND pd.codigoReformaTraspaso is null")
                    .setParameter("partidaDistributiva", partidaDistributiva)
                    .setParameter("servidor", servidor.getPersona().getIdentificacion())
                    .setParameter("periodo", periodo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public ProformaPresupuestoPlanificado getTipoPartida(String partidaPresupuestaria, Short periodo) {
        try {
            ProformaPresupuestoPlanificado resultado = (ProformaPresupuestoPlanificado) em.createQuery("SELECT ppp FROM ProformaPresupuestoPlanificado ppp "
                    + "WHERE ppp.estado=TRUE AND ppp.partidaPresupuestaria=:partidaPresupuestaria "
                    + "AND ppp.periodo=:periodo AND ppp.codigoReforma is null "
                    + "AND ppp.codigoReformaTraspaso is null")
                    .setParameter("partidaPresupuestaria", partidaPresupuestaria)
                    .setParameter("periodo", periodo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public PartidasDistributivoAnexo getPartidaDistributivoAnexo(String partidaPresupuestaria, Short periodo) {
        try {
            PartidasDistributivoAnexo resultado = (PartidasDistributivoAnexo) em.createQuery("SELECT pda FROM PartidasDistributivoAnexo pda "
                    + "WHERE pda.partidaAp=:partidaPresupuestaria "
                    + "AND pda.periodo=:periodo "
                    + "AND pda.codigoReforma is null "
                    + "AND pda.codigoReformaTraspaso is null")
                    .setParameter("partidaPresupuestaria", partidaPresupuestaria)
                    .setParameter("periodo", periodo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public Boolean getPeriodoAbierto(Integer periodo, String mes) {
        try {
            ControlCuentaContable resultado = (ControlCuentaContable) em.createQuery("SELECT c FROM ControlCuentaContable c WHERE c.estado=TRUE AND c.periodo=:periodo AND UPPER(c.nombreMes) =:mes")
                    .setParameter("periodo", periodo.shortValue())
                    .setParameter("mes", mes)
                    .getSingleResult();
            return resultado != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    public DiarioGeneral getUltimaTransaccion(Short periodo) {
        try {
            DiarioGeneral resultado = (DiarioGeneral) em.createQuery("SELECT d FROM DiarioGeneral d WHERE d.periodo=:periodo ORDER BY d.id DESC")
                    .setParameter("periodo", periodo)
                    .setMaxResults(1)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<InventarioItems> getListInventarioItems(Inventario inventario) {
        List<InventarioItems> resultado = (List<InventarioItems>) em.createQuery("SELECT i FROM InventarioItems i WHERE i.inventario=:inventario")
                .setParameter("inventario", inventario)
                .getResultList();
        return resultado;
    }
    
    public CatalogoItem getClaseTipo(String codigo) {
        try {
            CatalogoItem resultado = (CatalogoItem) em.createQuery("SELECT c FROM CatalogoItem c WHERE c.codigo=:codigo")
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CatalogoMovimiento> getMotivoMovimientos(String tipoInventario) {
        List<CatalogoMovimiento> resultado = new ArrayList<>();
        switch (tipoInventario) {
            case "SALIDAS":
                resultado = (List<CatalogoMovimiento>) em.createQuery("SELECT c FROM CatalogoMovimiento c "
                        + "INNER JOIN c.tipoMovimientos t "
                        + "WHERE t.codigo='SALINV' AND c.estado=TRUE")
                        .getResultList();
                return resultado;
            case "ENTRADAS":
                resultado = (List<CatalogoMovimiento>) em.createQuery("SELECT c FROM CatalogoMovimiento c "
                        + "INNER JOIN c.tipoMovimientos t "
                        + "WHERE t.codigo='INGINV' AND c.codigo='SIN-FLUJ' AND c.texto<>'SALDO INICIAL' AND c.estado=TRUE")
                        .getResultList();
                return resultado;
            case "INGRESO":
                resultado = (List<CatalogoMovimiento>) em.createQuery("SELECT c FROM CatalogoMovimiento c "
                        + "INNER JOIN c.tipoMovimientos t WHERE t.codigo='BIEN-ING' AND c.estado=TRUE AND c.texto<> 'COMODATO'")
                        .getResultList();
                return resultado;
            default:
                break;
        }
        return resultado;
    }
    
    public List<CobrosEmisionesModel> getCobrosCajasList(CorteOrdenCobro corteOrdenCobro, Boolean accion, String tipoTesoria) {
        List<Object[]> result;
        String sql = "";
        Boolean registrarItem = Boolean.FALSE;
        if (accion && tipoTesoria.equals("EMISION")) {
            sql = "SELECT \n"
                    + "SUM(dcoc.total),\n"
                    + "it.cuenta_contable,\n"
                    + "it.item_presupuesto\n"
                    + "FROM tesoreria.detalle_corte_orden_cobro dcoc\n"
                    + "INNER JOIN tesoreria.item_tarifario it\n"
                    + "ON dcoc.item_tarifa = it.id\n"
                    + "WHERE dcoc.corte_orden_cobro=?1\n"
                    + "GROUP by 2,3";
            registrarItem = Boolean.TRUE;
        } else if (!accion && tipoTesoria.equals("EMISION")) {
            sql = "SELECT \n"
                    + "SUM(dcoc.total) As montototal,\n"
                    + "it.contra_cuenta AS cuentacontable\n"
                    + "FROM tesoreria.detalle_corte_orden_cobro dcoc\n"
                    + "INNER JOIN tesoreria.item_tarifario it\n"
                    + "ON dcoc.item_tarifa = it.id\n"
                    + "WHERE dcoc.corte_orden_cobro=?1\n"
                    + "GROUP BY 2";
        } else if (accion && tipoTesoria.equals("COBROS")) {
            sql = "SELECT \n"
                    + "SUM(dcoc.total) As montototal,\n"
                    + "dcoc.cuenta_caja AS cuentacontable\n"
                    + "FROM tesoreria.detalle_corte_orden_cobro dcoc\n"
                    + "WHERE dcoc.corte_orden_cobro=?1\n"
                    + "GROUP by dcoc.id_banco, dcoc.cuenta_caja";
        } else if (!accion && tipoTesoria.equals("COBROS")) {
            sql = "SELECT \n"
                    + "SUM(dcoc.total),\n"
                    + "it.contra_cuenta,\n"
                    + "it.item_presupuesto\n"
                    + "FROM tesoreria.detalle_corte_orden_cobro dcoc\n"
                    + "INNER JOIN tesoreria.item_tarifario it\n"
                    + "ON dcoc.item_tarifa = it.id\n"
                    + "WHERE dcoc.corte_orden_cobro=?1\n"
                    + "GROUP by 2,3";
            registrarItem = Boolean.TRUE;
        }
        Query query = em.createNativeQuery(sql).setParameter(1, corteOrdenCobro.getId());
        result = query.getResultList();
        if (result != null) {
            List<CobrosEmisionesModel> list = new ArrayList<>();
            for (Object[] objecto : result) {
                CobrosEmisionesModel data = new CobrosEmisionesModel();
                data.setMontototal((BigDecimal) objecto[0]);
                data.setCuentacontable(getCuentaContableById((BigInteger) objecto[1]));
                if (registrarItem) {
                    data.setItempresupuestario(getItemPresupuestarioById((BigInteger) objecto[2]));
                }
                list.add(data);
            }
            return list;
        } else {
            return null;
        }
    }
    
    private CuentaContable getCuentaContableById(BigInteger idCuentaContable) {
        CuentaContable resultado = (CuentaContable) em.createQuery("SELECT c FROM CuentaContable c WHERE c.id=:idCuentaContable")
                .setParameter("idCuentaContable", idCuentaContable.longValue())
                .getSingleResult();
        return resultado;
    }
    
    private Presupuesto getItemPresupuestarioById(BigInteger idItemPresupestario) {
        Presupuesto resultado = (Presupuesto) em.createQuery("SELECT p FROM Presupuesto p WHERE p.id=:idItemPresupestario")
                .setParameter("idItemPresupestario", idItemPresupestario.longValue())
                .getSingleResult();
        return resultado;
    }
    
    public CuentaContable getCuentaContableTesoreria(Recaudacion recaudacion) {
        BigInteger result = null;
        result = (BigInteger) em.createNativeQuery("SELECT dcoc.cuenta_caja FROM tesoreria.recaudacion_cobro rc \n"
                + "INNER JOIN tesoreria.detalle_corte_orden_cobro dcoc ON rc.corte = dcoc.corte_orden_cobro\n"
                + "WHERE rc.recaudacion = ?2 AND dcoc.id_banco = ?1\n"
                + "GROUP BY 1")
                .setParameter(1, recaudacion.getId_banco())
                .setParameter(2, recaudacion)
                .getSingleResult();
        return getCuentaContableById(result);
    }
    
    public List<TipoRolBeneficios> getTiposRolesBeneficios(Short periodo, String codigo) {
        List<TipoRolBeneficios> resultado = (List<TipoRolBeneficios>) em.createQuery("SELECT rb FROM TipoRolBeneficios rb INNER JOIN rb.estadoAprobacionBen ea "
                + "WHERE rb.estado=TRUE AND rb.periodo=:periodo AND ea.codigo=:codigo AND rb.diarioRolBeneficios=FALSE")
                .setParameter("periodo", periodo)
                .setParameter("codigo", codigo)
                .getResultList();
        return resultado;
    }
    
    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoRolGeneral(TipoRol tipoRol, Short periodo) {
        String sql = "SELECT \n"
                + "vr.cuenta_contable,vr.item_ap,\n"
                + "vr.estructura_ap,vr.fuente_directa,\n"
                + "vr.partida_ap, SUM(rr.valor_rubro)\n"
                + "FROM talento_humano.rol_rubro rr\n"
                + "INNER JOIN talento_humano.valores_roles vr\n"
                + "ON rr.valor_asignacion = vr.id\n"
                + "INNER JOIN talento_humano.liquidacion_rol lr\n"
                + "ON rr.liquidacion_rol = lr.id\n"
                + "INNER JOIN talento_humano.tipo_rol tr\n"
                + "ON lr.tipo_rol = tr.id\n"
                + "WHERE rr.estado=TRUE AND lr.estado=TRUE\n"
                + "AND tr.id=?1\n"
                + "GROUP BY 1,2,3,4,5\n"
                + "ORDER BY 5 asc";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRol.getId());
        List<Object[]> result = query.getResultList();
        return añadirDatosListaCuentaContablePresupuestoModel(result, periodo);
    }
    
    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoRolHorasExtras(TipoRol tipoRol, Short periodo) {
        String sql = "SELECT \n"
                + "vr.cuenta_contable,vr.item_ap,\n"
                + "vr.estructura_ap,vr.fuente_directa,\n"
                + "vr.partida_ap, SUM(rhv.valor_hora)\n"
                + "FROM talento_humano.rol_horas_extras_suplementarias rh\n"
                + "INNER JOIN talento_humano.rol_horas_valores rhv ON rhv.rol_hora = rh.id\n"
                + "INNER JOIN talento_humano.valores_roles vr\n"
                + "ON rhv.valores_roles = vr.id\n"
                + "WHERE rh.estado=TRUE \n"
                + "AND rhv.estado=TRUE \n"
                + "AND rh.tipo_rol=?1\n"
                + "GROUP BY 1,2,3,4,5\n"
                + "ORDER BY 5 ASC";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRol.getId());
        List<Object[]> result = query.getResultList();
        return añadirDatosListaCuentaContablePresupuestoModel(result, periodo);
    }
    
    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoFondosReservaAcumulados(TipoRol tipoRol, Short periodo, String estadoFondoReserva, Boolean acumula) {
        String sql = "SELECT\n"
                + "vr.cuenta_contable, vr.item_ap,\n"
                + "vr.estructura_ap,vr.fuente_directa,\n"
                + "vr.partida_ap, sum(fr.valor_fondos) as fondos\n"
                + "FROM talento_humano.fondos_reserva fr \n"
                + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr ON fr.acumulacion_fondos = afr.id\n"
                + "INNER JOIN talento_humano.roles_de_pago rp ON rp.servidor = afr.servidor\n"
                + "INNER JOIN talento_humano.valores_roles vr ON (vr.rol_pago = rp.id AND vr.valor_parametrizable=afr.valor_parametrizado)\n"
                + "WHERE fr.estado= true AND fr.tipo_rol= ?1 AND afr.derecho = true AND afr.acumula= ?3\n"
                + "AND afr.estado=true AND rp.estado=true AND vr.periodo = ?2\n"
                + "GROUP BY 1,2,3,4,5";
        Query query = em.createNativeQuery(sql)
                .setParameter(1, tipoRol.getId())
                .setParameter(2, periodo)
                .setParameter(3, acumula);
        List<Object[]> result = query.getResultList();
        return añadirDatosListaCuentaContablePresupuestoModel(result, periodo);
    }
    
    private List<CuentaContablePresupuestoModel> añadirDatosListaCuentaContablePresupuestoModel(List<Object[]> result, Short periodo) {
        if (result != null) {
            List<CuentaContablePresupuestoModel> list = new ArrayList<>();
            for (Object[] object : result) {
                CuentaContablePresupuestoModel data = new CuentaContablePresupuestoModel();
                data.setIdTemporal(list.size());
                data.setCuentaContable(getCuentaContableById((BigInteger) object[0]));
                if (object[1] != null && object[2] != null && object[3] != null && object[4] != null) {
                    data.setCatalogoPresupuesto(getCatalogoPresupuestoById((BigInteger) object[1]));
                    data.setPlanProgramatico(getPlanProgramaticoById((BigInteger) object[2]));
                    data.setFuenteDirecta(getFuenteById((BigInteger) object[3]));
                    data.setPartidaPresupuestaria((String) object[4]);
                    data.setMonto_2((BigDecimal) object[5]);
                    BigDecimal valor1 = getValorPresupuestado(data.getPartidaPresupuestaria(), periodo);
                    BigDecimal valor2 = getValorSolitado(data.getPartidaPresupuestaria(), periodo, "APRO");
                    data.setMonto_1(valor1.subtract(valor2));
                    if (data.getMonto_1().doubleValue() < data.getMonto_2().doubleValue()) {
                        data.setMonto_3(new BigDecimal(data.getMonto_1().doubleValue() - data.getMonto_2().doubleValue()));
                    } else {
                        data.setMonto_3(data.getMonto_2());
                    }
                } else {
                    data.setMonto_2((BigDecimal) object[5]);
                }
                list.add(data);
            }
            return list;
        } else {
            return null;
        }
    }
    
    private CatalogoPresupuesto getCatalogoPresupuestoById(BigInteger idCP) {
        CatalogoPresupuesto result = (CatalogoPresupuesto) em.createQuery("SELECT cp FROM CatalogoPresupuesto cp WHERE cp.id=:idCP")
                .setParameter("idCP", idCP.longValue())
                .getSingleResult();
        return result;
    }
    
    private PlanProgramatico getPlanProgramaticoById(BigInteger diPP) {
        PlanProgramatico result = (PlanProgramatico) em.createQuery("SELECT pp FROM PlanProgramatico pp WHERE pp.id=:diPP")
                .setParameter("diPP", diPP.longValue())
                .getSingleResult();
        return result;
    }
    
    private CatalogoItem getFuenteById(BigInteger idF) {
        CatalogoItem result = (CatalogoItem) em.createQuery("SELECT f FROM CatalogoItem f WHERE f.id=:idF")
                .setParameter("idF", idF.longValue())
                .getSingleResult();
        return result;
    }
    
    public BigDecimal getValorPresupuestado(String partidaPresupuestaria, Short periodo) {
        try {
            BigDecimal resultado = (BigDecimal) em.createQuery("SELECT p.codificado FROM Presupuesto p WHERE p.partida=:partidaPresupuestaria AND p.periodo=:periodo")
                    .setParameter("partidaPresupuestaria", partidaPresupuestaria)
                    .setParameter("periodo", periodo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    
    public Presupuesto getPresupuesto(String partidaPresupuestaria, Short periodo) {
        try {
            Presupuesto resultado = (Presupuesto) em.createQuery("SELECT p FROM Presupuesto p WHERE p.partida=:partidaPresupuestaria AND p.periodo=:periodo")
                    .setParameter("partidaPresupuestaria", partidaPresupuestaria)
                    .setParameter("periodo", periodo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public BigDecimal getValorSolitado(String partidaPresupuestaria, Short periodo, String codigo) {
        try {
            BigDecimal resultado = (BigDecimal) em.createQuery("SELECT SUM(dsc.montoSolicitado) FROM DetalleSolicitudCompromiso dsc "
                    + "INNER JOIN dsc.presupuesto p INNER JOIN dsc.solicitud sol INNER JOIN sol.estado es "
                    + "WHERE dsc.estado=TRUE AND dsc.periodo=:periodo AND p.partida=:partidaPresupuestaria AND es.codigo=:codigo")
                    .setParameter("partidaPresupuestaria", partidaPresupuestaria)
                    .setParameter("periodo", periodo)
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            if (resultado != null) {
                return resultado;
            } else {
                return BigDecimal.ZERO;
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
    
    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoDecimoTercero(TipoRolBeneficios tipoRolBeneficios, Short periodo) {
        String sql = "SELECT vr.cuenta_contable,vr.item_ap, vr.estructura_ap,vr.fuente_directa, vr.partida_ap,\n"
                + "sum(bdt.decimo_tercer_ganado + (CASE  WHEN bdt.ajuste is null THEN 0 ELSE bdt.ajuste END)) AS monto\n"
                + "FROM talento_humano.beneficios_decimo_tercero bdt\n"
                + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr ON bdt.acumulacion_fondos_reserva = afr.id\n"
                + "INNER JOIN conf.parametros_talento_humano pth ON afr.valor_parametrizado = pth.id\n"
                + "INNER JOIN talento_humano.valores_roles vr ON vr.valor_parametrizable = pth.id\n"
                + "INNER JOIN talento_humano.roles_de_pago rp ON (rp.id = vr.rol_pago AND rp.servidor = afr.servidor)\n"
                + "WHERE bdt.tipo_rol_beneficio = ?1 AND vr.periodo =?2\n"
                + "AND afr.estado = true AND vr.estado= true GROUP by 1,2,3,4,5\n"
                + "UNION\n"
                + "SELECT vr.cuenta_contable,vr.item_ap, vr.estructura_ap,vr.fuente_directa, vr.partida_ap,\n"
                + "sum(CASE  WHEN bdt.descuento is null THEN 0 ELSE bdt.descuento END) AS monto\n"
                + "FROM talento_humano.beneficios_decimo_tercero bdt\n"
                + "INNER JOIN talento_humano.valores_roles vr ON bdt.valor_rol = vr.id\n"
                + "WHERE bdt.estado=TRUE AND bdt.tipo_rol_beneficio=?1\n"
                + "AND vr.estado=TRUE AND vr.periodo=?2 GROUP BY 1,2,3,4,5\n"
                + "ORDER BY 5";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRolBeneficios.getId()).setParameter(2, periodo);
        List<Object[]> result = query.getResultList();
        return añadirDatosListaCuentaContablePresupuestoModel(result, periodo);
    }
    
    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoDecimoCuarto(TipoRolBeneficios tipoRolBeneficios, Short periodo) {
        String sql = "SELECT vr.cuenta_contable,vr.item_ap, vr.estructura_ap,vr.fuente_directa, vr.partida_ap,\n"
                + "sum(bdc.base_imponible  + (CASE  WHEN bdc.ajuste is null THEN 0 ELSE bdc.ajuste END) - bdc.cobrardo_rol) AS monto\n"
                + "FROM talento_humano.beneficios_decimo_cuarto bdc\n"
                + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr ON bdc.acumulacion_fondos = afr.id\n"
                + "INNER JOIN conf.parametros_talento_humano pth ON afr.valor_parametrizado = pth.id\n"
                + "INNER JOIN talento_humano.valores_roles vr ON vr.valor_parametrizable = pth.id\n"
                + "INNER JOIN talento_humano.roles_de_pago rp ON (rp.id = vr.rol_pago AND rp.servidor = afr.servidor)\n"
                + "WHERE bdc.tipo_rol_beneficio = ?1 AND vr.periodo = ?2 \n"
                + "AND afr.estado = true AND vr.estado= true GROUP by 1,2,3,4,5\n"
                + "UNION\n"
                + "SELECT vr.cuenta_contable,vr.item_ap,vr.estructura_ap,vr.fuente_directa,vr.partida_ap,\n"
                + "sum(CASE WHEN bdc.descuento is null THEN 0 ELSE bdc.descuento END) AS monto\n"
                + "FROM talento_humano.beneficios_decimo_cuarto bdc\n"
                + "INNER JOIN talento_humano.valores_roles vr ON bdc.valor_rol = vr.id\n"
                + "WHERE bdc.estado=TRUE AND bdc.tipo_rol_beneficio=?1 AND vr.estado=TRUE AND vr.periodo=?2\n"
                + "GROUP BY 1,2,3,4,5\n"
                + "ORDER BY 5";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRolBeneficios.getId()).setParameter(2, periodo);
        List<Object[]> result = query.getResultList();
        return añadirDatosListaCuentaContablePresupuestoModel(result, periodo);
    }
    
    public List<CuentaContablePresupuestoModel> getCuentaContablePresupuestoBeneficiosSindicales(TipoRolBeneficios tipoRolBeneficios, Short periodo) {
        String sql = "SELECT \n"
                + "vr.cuenta_contable,vr.item_ap,\n"
                + "vr.estructura_ap,vr.fuente_directa,\n"
                + "vr.partida_ap,\n"
                + "sum(bs.valor_beneficio)\n"
                + "FROM talento_humano.beneficios_sindicales bs\n"
                + "INNER JOIN talento_humano.roles_de_pago rdp\n"
                + "ON bs.servidor = rdp.servidor\n"
                + "INNER JOIN talento_humano.valores_roles vr\n"
                + "ON vr.rol_pago = rdp.id\n"
                + "INNER JOIN public.cuenta_contable cc\n"
                + "ON vr.cuenta_contable=cc.id\n"
                + "WHERE bs.estado=TRUE AND bs.tipo_rol_beneficios=1\n"
                + "AND vr.estado=TRUE AND vr.periodo=?2\n"
                + "AND cc.codigo=?3\n"
                + "GROUP BY 1,2,3,4,5\n"
                + "ORDER BY 5";
        Query query = em.createNativeQuery(sql).setParameter(1, tipoRolBeneficios.getId()).setParameter(2, periodo).setParameter(3, "6330512");
        List<Object[]> result = query.getResultList();
        return añadirDatosListaCuentaContablePresupuestoModel(result, periodo);
    }
    
    public List<ServidorMontoModel> getServidorRolGeneralFondoReserva(String cuentaPartidaPresupuestaria, TipoRol tipoRolSeleccionado, Short periodo, Boolean accion) {
        String sql;
        if (accion) {
            sql = "SELECT afr.servidor, fr.valor_fondos\n"
                    + "FROM talento_humano.fondos_reserva fr\n"
                    + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr ON fr.acumulacion_fondos = afr.id\n"
                    + "INNER JOIN public.catalogo_item ci ON afr.tipo_acumulacion = ci.id\n"
                    + "WHERE fr.tipo_rol=?3 AND fr.estado=true AND afr.acumula= TRUE\n"
                    + "AND ci.codigo='ACU-FONDOS-RESERVA' AND afr.periodo=?2";
        } else {
            sql = "SELECT rdp.servidor, rr.valor_rubro\n"
                    + "FROM talento_humano.rol_rubro rr\n"
                    + "INNER JOIN talento_humano.valores_roles vr ON rr.valor_asignacion = vr.id\n"
                    + "INNER JOIN talento_humano.liquidacion_rol lr ON rr.liquidacion_rol = lr.id\n"
                    + "INNER JOIN talento_humano.roles_de_pago rdp ON lr.rol_pago = rdp.id\n"
                    + "WHERE rr.estado = true AND lr.tipo_rol = ?3 AND vr.periodo = ?2 AND vr.partida_ap =?1";
        }
        Query query = em.createNativeQuery(sql).setParameter(1, cuentaPartidaPresupuestaria).setParameter(2, periodo).setParameter(3, tipoRolSeleccionado.getId());
        List<Object[]> result = query.getResultList();
        return getListadoBeneficiarioMonto(result);
    }
    
    public List<ServidorMontoModel> getServidorBeneficios(String cuentaPartidaPresupuestaria, TipoRolBeneficios tipoRolBeneficiosSeleccionado, Short periodo) {
        String sql = "";
        List<Object[]> result = null;
        Query query;
        switch (tipoRolBeneficiosSeleccionado.getTipo().getCodigo()) {
            case "ROL_TIPO_DEC_TERCERO":
                sql = "SELECT \n"
                        + "afr.servidor, (bdt.decimo_tercer_ganado + CASE  WHEN bdt.ajuste is null THEN 0 ELSE bdt.ajuste END) AS monto\n"
                        + "FROM talento_humano.beneficios_decimo_tercero bdt\n"
                        + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr ON bdt.acumulacion_fondos_reserva = afr.id\n"
                        + "WHERE bdt.tipo_rol_beneficio = ?3 AND bdt.estado=TRUE AND afr.periodo=?2";
                query = em.createNativeQuery(sql).setParameter(2, periodo).setParameter(3, tipoRolBeneficiosSeleccionado.getId());
                result = query.getResultList();
                break;
            case "ROL_TIPO_DEC_CUARTO":
                sql = "SELECT afr.servidor, (bdc.base_imponible + CASE  WHEN bdc.ajuste is null THEN 0 ELSE bdc.ajuste END) AS monto\n"
                        + "FROM talento_humano.beneficios_decimo_cuarto bdc\n"
                        + "INNER JOIN talento_humano.acumulacion_fondo_reserva afr ON bdc.acumulacion_fondos = afr.id\n"
                        + "WHERE bdc.tipo_rol_beneficio = ?3 AND bdc.estado=TRUE AND afr.periodo=?2";
                query = em.createNativeQuery(sql).setParameter(2, periodo).setParameter(3, tipoRolBeneficiosSeleccionado.getId());
                result = query.getResultList();
                break;
            case "ROL_TIPO_BEN_SIN":
                sql = " SELECT bs.servidor, bs.valor_beneficio\n"
                        + " FROM talento_humano.beneficios_sindicales bs \n"
                        + " WHERE bs.tipo_rol_beneficios= ?3 AND bs.estado=TRUE";
                query = em.createNativeQuery(sql).setParameter(3, tipoRolBeneficiosSeleccionado.getId());
                result = query.getResultList();
                break;
        }
        return getListadoBeneficiarioMonto(result);
    }
    
    private List<ServidorMontoModel> getListadoBeneficiarioMonto(List<Object[]> result) {
        if (result != null) {
            List<ServidorMontoModel> list = new ArrayList<>();
            for (Object[] object : result) {
                ServidorMontoModel data = new ServidorMontoModel();
                data.setServidor(getServidorById((BigInteger) object[0]));
                data.setMonto((BigDecimal) object[1]);
                list.add(data);
            }
            return list;
        } else {
            return null;
        }
    }
    
    private Servidor getServidorById(BigInteger idServidor) {
        Servidor result = (Servidor) em.createQuery("SELECT s FROM Servidor s WHERE s.id=:idServidor")
                .setParameter("idServidor", idServidor.longValue())
                .getSingleResult();
        return result;
    }
    
    public List<SolicitudReservaCompromiso> getAdquisiciones(String estado, Short periodo, CatalogoItem subtipo, Date fechaDG) {
        List<SolicitudReservaCompromiso> resultado = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT DISTINCT(b) FROM ContratosReservaEjecuion a "
                + "INNER JOIN a.reserva b "
                + "INNER JOIN a.contrato c "
                + "INNER JOIN b.estado d "
                + "WHERE d.codigo=:estado AND b.periodo=:periodo AND b.contabilizado= FALSE AND b.comprometido=TRUE "
                + "AND c.subTipoAdquisicion=:subtipo AND cast(b.fechaAprobacion as date) <=:fechaDG ORDER BY b.secuencial ASC")
                .setParameter("estado", estado)
                .setParameter("periodo", periodo)
                .setParameter("subtipo", subtipo)
                .setParameter("fechaDG", fechaDG)
                .getResultList();
        return resultado;
    }
    
    public List<SolicitudReservaCompromiso> getAdquisicionesProcess(String estado, Short periodo, String subtipo1, String subtipo2, Date fechaDG) {
        List<SolicitudReservaCompromiso> resultado = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT DISTINCT(b) FROM ContratosReservaEjecuion a "
                + "INNER JOIN a.reserva b "
                + "INNER JOIN a.contrato c "
                + "INNER JOIN b.estado d "
                + "WHERE d.codigo=:estado AND b.periodo=:periodo AND b.contabilizado= FALSE AND b.comprometido=TRUE "
                + "AND (c.subTipoAdquisicion.codigo=:subtipo1 or c.subTipoAdquisicion.codigo=:subtipo2) AND cast(b.fechaAprobacion as date) <=:fechaDG ORDER BY b.secuencial ASC")
                .setParameter("estado", estado)
                .setParameter("periodo", periodo)
                .setParameter("subtipo1", subtipo1)
                .setParameter("subtipo2", subtipo2)
                .setParameter("fechaDG", fechaDG)
                .getResultList();
        return resultado;
    }
    
    public List<SolicitudReservaCompromiso> getListadoReservaCompromiso(String codigo, Short periodo, Date fechaDG) {
        List<SolicitudReservaCompromiso> resultado = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT src FROM SolicitudReservaCompromiso src "
                + "INNER JOIN src.estado ci WHERE ci.codigo=:codigo AND src.contabilizado=FALSE AND src.comprometido=TRUE "
                + "AND cast(src.fechaAprobacion as date) <= :fechaDG AND src.periodo=:periodo ORDER BY src.secuencial ASC")
                .setParameter("codigo", codigo)
                .setParameter("periodo", periodo)
                .setParameter("fechaDG", fechaDG)
                .getResultList();
        return resultado;
    }
    
    public List<CuentaContablePresupuestoModel> getDetalleReservaCompromiso(SolicitudReservaCompromiso reservaCompromiso, Short periodo) {
        String sql = "SELECT \n"
                + "(CASE WHEN dsc.actividad_producto IS NULL THEN pre.item ELSE pro.item_presupuestario END)  AS item_presupuestario,\n"
                + "(CASE WHEN dsc.actividad_producto IS NULL THEN pre.estructura ELSE pro.estructura_programatica END)  AS estructura_programatica,\n"
                + "(CASE WHEN dsc.actividad_producto IS NULL THEN pre.fuente_directa ELSE pro.fuente_directa END)  AS fuente_directa,\n"
                + "(CASE WHEN dsc.actividad_producto IS NULL THEN pre.partida ELSE pro.codigo_presupuestario END)  AS partida_presupuestaria,\n"
                + "dsc.monto_comprometido, dsc.id \n"
                + "FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso dsc\n"
                + "LEFT JOIN public.presupuesto pre ON dsc.presupuesto=pre.id\n"
                + "LEFT JOIN public.producto pro ON dsc.actividad_producto= pro.id\n"
                + "WHERE dsc.solicitud=?1\n"
                + "ORDER BY 4";
        Query query = em.createNativeQuery(sql).setParameter(1, reservaCompromiso.getId());
        List<Object[]> result = query.getResultList();
        if (result != null) {
            List<CuentaContablePresupuestoModel> list = new ArrayList<>();
            for (Object[] object : result) {
                CuentaContablePresupuestoModel data = new CuentaContablePresupuestoModel();
                if (object[0] != null && object[1] != null && object[2] != null && object[3] != null) {
                    data.setIdTemporal(list.size());
                    data.setCatalogoPresupuesto(getCatalogoPresupuestoById((BigInteger) object[0]));
                    data.setPlanProgramatico(getPlanProgramaticoById((BigInteger) object[1]));
                    data.setFuenteDirecta(getFuenteById((BigInteger) object[2]));
                    data.setPartidaPresupuestaria((String) object[3]);
                    data.setParcialTotal(getTipoDevengado("devengado_parcial"));
                    data.setDevengadoTotal(Boolean.FALSE);
                    data.setMonto_1(Utils.bigdecimalTo2Decimals((BigDecimal) object[4]));
                    BigInteger idDetalleReserva = (BigInteger) object[5];
                    data.setIdDetalleReserva(new DetalleSolicitudCompromiso(idDetalleReserva.longValue()));
                    BigDecimal bigDecimal = totalPartidaRegistrada(reservaCompromiso, data.getPartidaPresupuestaria(), data.getIdDetalleReserva());
                    if (bigDecimal != null) {
                        double diferencia = data.getMonto_1().doubleValue() - bigDecimal.doubleValue();
                        data.setMonto_2(new BigDecimal(diferencia));
                    } else {
                        data.setMonto_2(data.getMonto_1());
                    }
                    if (data.getMonto_2().doubleValue() > 0) {
                        data.setMonto_3(BigDecimal.ZERO);
                        list.add(data);
                    }
                }
            }
            return list;
        } else {
            return null;
        }
    }
    
    public BigDecimal totalPartidaRegistrada(SolicitudReservaCompromiso reservaCompromiso, String partidaPresupuestaria, DetalleSolicitudCompromiso idDetalleReserva) {
        try {
            BigDecimal resultado = (BigDecimal) em.createQuery("SELECT SUM(d.devengado) FROM DetalleTransaccion d "
                    + "INNER JOIN d.diarioGeneral g  "
                    + "WHERE g.certificacionesPresupuestario=:reservaCompromiso "
                    + "AND g.estado = TRUE AND d.idDetalleReserva=:idDetalleReserva "
                    + "AND d.cedulaPresupuestaria=:partidaPresupuestaria")
                    .setParameter("reservaCompromiso", reservaCompromiso)
                    .setParameter("partidaPresupuestaria", partidaPresupuestaria)
                    .setParameter("idDetalleReserva", idDetalleReserva)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public CatalogoItem getTipoDevengado(String codigo) {
        try {
            CatalogoItem resultado = (CatalogoItem) em.createQuery("SELECT ci FROM CatalogoItem ci WHERE ci.codigo=:codigo")
                    .setParameter("codigo", codigo)
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public List<CuentaContable> getCuentasContables(CatalogoPresupuesto catalogoPresupuesto, Short periodo) {
        String sql = "SELECT c.id,c.debito FROM cuenta_contable c\n"
                + "WHERE c.debito=?1 AND c.periodo=?2 AND c.estado=TRUE\n";
        Query query = em.createNativeQuery(sql).setParameter(1, catalogoPresupuesto.getId()).setParameter(2, periodo);
        List<Object[]> result = query.getResultList();
        if (!result.isEmpty()) {
            List<CuentaContable> list = new ArrayList<>();
            for (Object[] object : result) {
                if (object[0] != null) {
                    CuentaContable data = getCuentaContableById((BigInteger) object[0]);
                    list.add(data);
                }
            }
            return list;
        } else {
            return new ArrayList<>();
        }
    }
    
    public CatalogoPresupuesto getRelacionPresupuestaria(CuentaContable cuentaContable, Boolean accion) {
        try {
            String sql = "";
            if (accion) {
                sql = "SELECT cc.debito FROM CuentaContable cc WHERE cc.id=:cuentaContable";
            } else {
                sql = "SELECT cc.credito FROM CuentaContable cc WHERE cc.id=:cuentaContable";
            }
            CatalogoPresupuesto resultado = (CatalogoPresupuesto) em.createQuery(sql)
                    .setParameter("cuentaContable", cuentaContable.getId())
                    .getSingleResult();
            return resultado;
        } catch (Exception e) {
            return null;
        }
    }
    
    public BigDecimal getSaldoReservas(String partida, Short periodo, String estado) {
        String tipoMonto = "";
        if (estado.equals("APRO")) {
            tipoMonto = "dsc.montoSolicitado";
        } else {
            tipoMonto = "dsc.liquidado";
        }
        String sql_1 = "SELECT SUM(" + tipoMonto + ") FROM DetalleSolicitudCompromiso dsc "
                + "INNER JOIN dsc.solicitud s INNER JOIN s.estado e INNER JOIN dsc.actividadProducto p "
                + "WHERE s.periodo=:periodo AND e.codigo=:estado AND p.codigoPresupuestario=:partida";
        String sql_2 = "SELECT SUM(" + tipoMonto + ") FROM DetalleSolicitudCompromiso dsc "
                + "INNER JOIN dsc.solicitud s INNER JOIN s.estado e INNER JOIN dsc.presupuesto p "
                + "WHERE s.periodo=:periodo AND e.codigo=:estado AND p.partida=:partida";
        BigDecimal resultado1 = (BigDecimal) em.createQuery(sql_1)
                .setParameter("partida", partida)
                .setParameter("periodo", periodo)
                .setParameter("estado", estado)
                .getSingleResult();
        if (resultado1 == null) {
            resultado1 = BigDecimal.ZERO;
        }
        BigDecimal resultado2 = (BigDecimal) em.createQuery(sql_2)
                .setParameter("partida", partida)
                .setParameter("periodo", periodo)
                .setParameter("estado", estado)
                .getSingleResult();
        if (resultado2 == null) {
            resultado2 = BigDecimal.ZERO;
        }
        return resultado1.add(resultado2);
    }
    
    public BigDecimal getsaldoDevengado(String partida, Short periodo) {
        BigDecimal resultado = (BigDecimal) em.createQuery("SELECT SUM(dt.devengado) FROM DetalleTransaccion dt "
                + "INNER JOIN dt.diarioGeneral d INNER JOIN d.tipo t "
                + "WHERE d.periodo=:periodo AND dt.cedulaPresupuestaria=:partida "
                + "AND d.certificacionesPresupuestario IS NULL AND t.codigo<>'tipo_apertura' AND t.codigo<>'tipo_cierre' ")
                .setParameter("partida", partida)
                .setParameter("periodo", periodo)
                .getSingleResult();
        if (resultado == null) {
            return BigDecimal.ZERO;
        } else {
            return resultado;
        }
    }
    
    public BigDecimal getSaldoCobroAjuste(Recaudacion recaudacion, Boolean accion) {
        BigDecimal resultado;
        if (accion) {
            resultado = (BigDecimal) em.createQuery("SELECT SUM(dc.total) FROM DetalleCorteOrdenCobro dc WHERE dc.cobroAjuste=:recaudacion")
                    .setParameter("recaudacion", recaudacion)
                    .getSingleResult();
        } else {
            resultado = (BigDecimal) em.createQuery("SELECT SUM(dc.total) FROM DetalleCorteOrdenCobro dc WHERE dc.cobroAjuste=:recaudacion AND dc.valorAjuste=:accion")
                    .setParameter("recaudacion", recaudacion)
                    .setParameter("accion", accion)
                    .getSingleResult();
        }
        if (resultado == null) {
            return BigDecimal.ZERO;
        } else {
            return resultado;
        }
    }
    
    public List<CatalogoPresupuesto> getListadoCatalogoPresupuesto(CuentaContable cuentaContable) {
        List<CatalogoPresupuesto> resultado = (List<CatalogoPresupuesto>) em.createQuery("SELECT cc.catalogoPresupuesto FROM CuentaContablecatalogoPresupuesto cc "
                + "WHERE cc.cuentaContable=:cuentaContable")
                .setParameter("cuentaContable", cuentaContable)
                .getResultList();
        return resultado;
    }
    
    public List<Presupuesto> getListadoPresupuesto(CatalogoPresupuesto catalogoPresupuesto, Short periodo) {
        List<Presupuesto> resultado = (List<Presupuesto>) em.createQuery("SELECT p FROM Presupuesto p WHERE p.item=:catalogoPresupuesto AND p.periodo=:periodo ORDER BY p.partida ASC")
                .setParameter("catalogoPresupuesto", catalogoPresupuesto)
                .setParameter("periodo", periodo)
                .getResultList();
        return resultado;
    }
    
    public int getActualizarCobrosXAjuste(Recaudacion recaudacion) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("UPDATE tesoreria.detalle_corte_orden_cobro SET valor_ajuste=TRUE WHERE cobro_ajuste= ?1 AND valor_ajuste=FALSE")
                .setParameter(1, recaudacion.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }
    
    public List<String> getPeriodosFiscales(Short periodo) {
        List<String> resultado = (List<String>) em.createQuery("SELECT l.periodo FROM Liquidacion l WHERE l.estado=TRUE AND l.anio=:periodo GROUP BY l.periodo,l.mes ORDER BY l.mes ASC")
                .setParameter("periodo", periodo.intValue())
                .getResultList();
        return resultado;
    }
    
    public List<String> getTiposRetenciones(String tipo) {
        List<String> resultado = (List<String>) em.createQuery("SELECT rt.descripcion FROM RubroTipo rt WHERE rt.tipo=:tipo AND rt.estado=TRUE")
                .setParameter("tipo", tipo)
                .getResultList();
        return resultado;
    }
    
    public List<CuentaContablePresupuestoModel> getListPartidasSaldos(CuentaContable cuentaContable, DiarioGeneral diarioGeneral) {
        String sql = "SELECT partida_presupuestaria,estructura_programatica,fuente,cedula_presupuestaria,\n"
                + "(sum(monto_1) - sum(monto_2)) AS diferencia FROM "
                + "(SELECT dt.partida_presupuestaria, dt.estructura_programatica, dt.fuente, dt.cedula_presupuestaria,sum(dt.devengado) AS monto_1 ,sum(dt.ejecutado) AS monto_2\n"
                + "FROM contabilidad.detalle_transaccion dt INNER JOIN contabilidad.diario_general dg ON dt.diario_general = dg.id\n"
                + "INNER JOIN public.catalogo_item ci ON dt.tipo_transaccion=ci.id INNER JOIN public.catalogo_presupuesto cp ON dt.partida_presupuestaria = cp.id\n"
                + "WHERE dg.id=?2 AND ci.codigo='diario_general_devengado' AND cp.codigo LIKE (?1||'%') GROUP BY 1,2,3,4\n"
                + "UNION ALL\n"
                + "SELECT dt.partida_presupuestaria, dt.estructura_programatica, dt.fuente, dt.cedula_presupuestaria,sum(dt.devengado) AS monto_1,sum(dt.ejecutado) AS monto_2\n"
                + "FROM contabilidad.detalle_transaccion dt INNER JOIN contabilidad.diario_general dg ON dt.diario_general = dg.id\n"
                + "INNER JOIN public.catalogo_item ci ON dt.tipo_transaccion=ci.id INNER JOIN public.catalogo_presupuesto cp ON dt.partida_presupuestaria = cp.id\n"
                + "WHERE dg.id=?2 AND ci.codigo='diario_general_ejecucion' AND cp.codigo LIKE (?1||'%') GROUP BY 1,2,3,4\n"
                + "UNION ALL\n"
                + "SELECT dcp.partida_presupuestaria, dcp.estructura_programatica, dcp.fuente, dcp.cedula_presupuestaria,sum(dcp.haber) AS monto_1,sum(dcp.ejecutado) AS monto_2\n"
                + "FROM contabilidad.detalle_comprobante_pago dcp INNER JOIN contabilidad.comprobante_pago cp ON dcp.comprobante_pago = cp.id\n"
                + "INNER JOIN contabilidad.diario_general dg ON cp.diario_general = dg.id INNER JOIN public.catalogo_presupuesto p ON dcp.partida_presupuestaria=p.id\n"
                + "WHERE dg.id=?2 AND dcp.tipo_pago is not null AND p.codigo LIKE (?1||'%') GROUP BY 1,2,3,4) agrupacion GROUP BY 1,2,3,4 ORDER BY 5";
        Query query = em.createNativeQuery(sql).setParameter(1, cuentaContable.getCuentaNivel1().toString()).setParameter(2, diarioGeneral.getId());
        List<Object[]> result = query.getResultList();
        if (result != null) {
            List<CuentaContablePresupuestoModel> list = new ArrayList<>();
            for (Object[] object : result) {
                CuentaContablePresupuestoModel data = new CuentaContablePresupuestoModel();
                data.setIdTemporal(list.size());
                data.setCatalogoPresupuesto(getCatalogoPresupuestoById((BigInteger) object[0]));
                data.setPlanProgramatico(getPlanProgramaticoById((BigInteger) object[1]));
                data.setFuenteDirecta(getFuenteById((BigInteger) object[2]));
                data.setPartidaPresupuestaria((String) object[3]);
                data.setMonto_3((BigDecimal) object[4]);
                BigDecimal retencionRegistrada = getRetencionRegistrada(diarioGeneral, data.getPartidaPresupuestaria());
                data.setMonto_3(new BigDecimal(data.getMonto_3().doubleValue() - retencionRegistrada.doubleValue()));
                if (data.getMonto_3().doubleValue() > 0) {
                    list.add(data);
                }
            }
            return list;
        } else {
            return null;
        }
    }
    
    private BigDecimal getRetencionRegistrada(DiarioGeneral diarioGeneral, String partidaPresupuestaria) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT rr.monto FROM RetencionesRegistradas rr WHERE rr.diarioGeneral.id=:diarioGeneral AND rr.partidaPresupuestaria=:partidaPresupuestaria")
                .setParameter("diarioGeneral", diarioGeneral.getId())
                .setParameter("partidaPresupuestaria", partidaPresupuestaria)
                .getResultStream()
                .findFirst()
                .orElse(BigDecimal.ZERO);
        return result;
    }
    
    public List<LiquidacionDetalle> getListDetalleRetencion(Short periodo, String tipoRetencionSeleccionado, String periodoSeleccionado, String recibidaautorizado) {
        List<LiquidacionDetalle> resultado = (List<LiquidacionDetalle>) em.createQuery("SELECT ld FROM LiquidacionDetalle ld INNER JOIN ld.liquidacion l "
                + "INNER JOIN ld.rubro r INNER JOIN r.rubroTipo rt WHERE l.estadoWs=:recibidaautorizado "
                + "AND rt.descripcion=:tipoRetencionSeleccionado AND l.periodo=:periodo AND l.estado=TRUE AND l.anio=:anio AND ld.contabilizado=FALSE")
                .setParameter("anio", periodo.intValue())
                .setParameter("tipoRetencionSeleccionado", tipoRetencionSeleccionado)
                .setParameter("periodo", periodoSeleccionado)
                .setParameter("recibidaautorizado", recibidaautorizado)
                .getResultList();
        return resultado;
    }
    
    public BigDecimal retornarValorCpago(DiarioGeneral d) {
        BigDecimal result = (BigDecimal) em.createQuery("SELECT COALESCE(SUM(d.haber),0) FROM DetalleTransaccion d INNER JOIN d.diarioGeneral di WHERE d.comprobantePago=TRUE AND di.id= :id")
                .setParameter("id", d.getId()).getResultStream().findFirst().orElse(BigDecimal.ZERO);
        return result;
    }
    
    public List<Garantias> getGarantiasList(String adquisicion) {
        String sql = "";
        switch (adquisicion) {
            case "1":
                sql = "SELECT g FROM Garantias g WHERE g.estado=TRUE AND g.devolucion=FALSE AND now () < g.fechaHasta AND g.diarioGeneralVigente IS NULL";
                break;
            case "2":
                sql = "SELECT g FROM Garantias g WHERE g.estado=TRUE AND g.devolucion=FALSE AND now () > g.fechaHasta AND g.diarioGeneralVencido IS NULL";
                break;
            case "3":
                sql = "SELECT g FROM Garantias g WHERE g.estado=TRUE AND g.devolucion=TRUE AND g.diarioGeneralDevuelto IS NULL";
                break;
        }
        List<Garantias> resultado = (List<Garantias>) em.createQuery(sql)
                .getResultList();
        return resultado;
    }
    
    public List<SolicitudReservaCompromiso> getInfimasCuantias(String estado, Short periodo, String tipoAdquisicion, String tipoProceso, Date fechaDG) {
        List<SolicitudReservaCompromiso> resultado = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT DISTINCT(b) FROM ContratosReservaEjecuion a "
                + "INNER JOIN a.reserva b "
                + "INNER JOIN a.contrato c "
                + "INNER JOIN b.estado d "
                + "WHERE d.codigo=:estado AND b.periodo=:periodo AND b.contabilizado= FALSE AND b.comprometido=TRUE "
                + "AND c.tipoAdquisicion.codigo=:tipoAdquisicion AND c.tipoProceso.codigo=:tipoProceso AND cast(b.fechaAprobacion as date) <=:fechaDG ORDER BY b.secuencial ASC")
                .setParameter("estado", estado)
                .setParameter("periodo", periodo)
                .setParameter("tipoAdquisicion", tipoAdquisicion)
                .setParameter("tipoProceso", tipoProceso)
                .setParameter("fechaDG", fechaDG)
                .getResultList();
        return resultado;
    }
    
    public Cliente enviarNotificacionBeneficiario(BigInteger num, Short periodo) {
        Cliente result = (Cliente) em.createQuery("SELECT b FROM DiarioGeneral d INNER JOIN d.beneficiario b WHERE d.numTramite=:num AND d.periodo=:periodo")
                .setParameter("num", num.longValue()).setParameter("periodo", periodo).getSingleResult();
        return result;
    }
    
    public Boolean getDiarioGeneralTramite(Long numTramite, Short periodo) {
        DiarioGeneral result = (DiarioGeneral) em.createQuery("SELECT d FROM DiarioGeneral d WHERE d.numTramite=:numTramite and d.periodo=:periodo")
                .setParameter("numTramite", numTramite).setParameter("periodo", periodo).getResultStream().findFirst().orElse(null);
        return result != null;
    }
    
    public List<SolicitudReservaCompromiso> getContractosResrrvas(Adquisiciones a) {
        List<SolicitudReservaCompromiso> resultado = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT r FROM ContratosReservaEjecuion a INNER JOIN a.reserva r WHERE a.contrato=:contracto AND r.estado.codigo='APRO' AND r.contabilizado=false AND r.comprometido=true")
                .setParameter("contracto", a).getResultList();
        return resultado;
    }
    
    public void getActualizarAnticipo(TipoRol tipoRolSeleccionado, List<BeneficiarioSolicitudReserva> beneficiarioReservaCompromisoList, DiarioGeneral diarioGeneral) {
        for (BeneficiarioSolicitudReserva servidor : beneficiarioReservaCompromisoList) {
            CuotaAnticipo anticipo = getCuota(tipoRolSeleccionado.getAnio(), tipoRolSeleccionado.getMes().getCodigo(), servidor.getBeneficiario());
            if (anticipo.getId() != null) {
                Query query = getEntityManager().createNativeQuery("UPDATE talento_humano.cuota_anticipo ac SET ac.referencia_contable = ?1, ac.fecha_pago = ?2, ac.estado_cuota = ?3, ac.periodo=?4 WHERE ac.anticipoRemuneracion=?5 AND ac.anticipoRemuneracion.estado = true")
                        .setParameter(1, diarioGeneral.getNumeroTransaccion())
                        .setParameter(2, diarioGeneral.getFechaElaboracion())
                        .setParameter(3, true)
                        .setParameter(4, tipoRolSeleccionado.getAnio())
                        .setParameter(5, anticipo.getId());
                int executeUpdate = query.executeUpdate();
            }
        }
    }
    
    private CuotaAnticipo getCuota(Short periodo, String mes, Cliente servidor) {
        try {
            CuotaAnticipo anticipo = (CuotaAnticipo) em.createQuery("SELECT ca FROM CuotaAnticipo ca INNER JOIN ca.anticipoRemuneracion ar INNER JOIN ar.servidor s WHERE s.persona=:servidor AND UPPER(ca.mes)=:mesBusqueda AND ar.periodo=:periodoBusqueda AND ac.estado = true ")
                    .setParameter("periodoBusqueda", periodo)
                    .setParameter("mesBusqueda", mes)
                    .setParameter("servidor", servidor)
                    .getSingleResult();
            return anticipo;
        } catch (Exception e) {
            return new CuotaAnticipo();
        }
    }
    
    public boolean getComprobanteRegistrado(DiarioGeneral diario) {
        List<ComprobantePago> resultado = (List<ComprobantePago>) em.createQuery("SELECT c FROM ComprobantePago c WHERE c.diarioGeneral=:diario AND c.estado not like '%ANULAD%'")
                .setParameter("diario", diario)
                .getResultList();
        return !resultado.isEmpty();
    }

    //<editor-fold defaultstate="collapsed" desc="Determinar el saldo de una partida presupuestaria">
    public BigDecimal getSaldoPresupuesto(Presupuesto presupuesto, Date fecha) {
        String sql = "select (sum(devengado) - sum(ejecutado)) as monto_disponible from ( \n"
                + "SELECT 0 as id, ds.monto_solicitado, ds.monto_comprometido,0.00 as devengado, 0.00 as ejecutado\n"
                + "FROM certificacion_presupuestaria_anual.detalle_solicitud_compromiso ds\n"
                + "INNER JOIN certificacion_presupuestaria_anual.solicitud_reserva_compromiso sr ON ds.solicitud = sr.id\n"
                + "INNER JOIN catalogo_item ca ON ca.id = sr.estado\n"
                + "WHERE ds.estado = true AND cast(sr.fecha_aprobacion as DATE) BETWEEN ?2 AND ?3 AND ca.codigo IN ('APRO','LIQUI')\n"
                + "AND (CASE WHEN ds.actividad_producto IS NOT NULL THEN (SELECT pro.codigo_presupuestario FROM producto pro \n"
                + "													   WHERE pro.id = ds.actividad_producto)\n"
                + "	 ELSE (SELECT pre.partida FROM presupuesto pre WHERE pre.id = ds.presupuesto) END) = ?1\n"
                + "AND (CASE WHEN ds.actividad_producto IS NOT NULL THEN (SELECT cpre.flujo_ingreso FROM producto pro\n"
                + "													   INNER JOIN catalogo_presupuesto cpre ON cpre.id = pro.item_presupuestario\n"
                + "													   WHERE pro.id = ds.actividad_producto)\n"
                + "	 ELSE (SELECT pre.tipo FROM presupuesto pre WHERE pre.id = ds.presupuesto) END) = false\n"
                + "UNION ALL\n"
                + "SELECT 0 as id, 0.00 as monto_solicitado, 0.00 as monto_comprometido, dt.devengado, dt.ejecutado as ejecutado\n"
                + "FROM contabilidad.detalle_transaccion dt\n"
                + "INNER JOIN contabilidad.diario_general dg ON dg.id = dt.diario_general\n"
                + "INNER JOIN certificacion_presupuestaria_anual.solicitud_reserva_compromiso sc ON sc.id = dg.certificaciones_presupuestario\n"
                + "INNER JOIN certificacion_presupuestaria_anual.detalle_solicitud_compromiso dsc ON dsc.solicitud = sc.id\n"
                + "INNER JOIN catalogo_presupuesto cprs ON cprs.id = dt.partida_presupuestaria\n"
                + "INNER JOIN plan_programatico ppro ON ppro.id = dt.estructura_programatica\n"
                + "WHERE dg.estado = true AND cast(dg.fecha_elaboracion as DATE) BETWEEN ?2 AND  ?3\n"
                + "AND dt.cedula_presupuestaria = ?1 AND dt.id_detalle_reserva = dsc.id AND cprs.flujo_ingreso = false\n"
                + "AND (CASE WHEN dsc.actividad_producto IS NOT NULL THEN (SELECT pro.codigo_presupuestario FROM producto pro\n"
                + "														WHERE pro.id = dsc.actividad_producto)\n"
                + "	 ELSE (SELECT pre.partida FROM presupuesto pre WHERE pre.id = dsc.presupuesto) END) = ?1\n"
                + "UNION ALL\n"
                + "SELECT 0 as id, 0.00 as monto_solicitado, dt.comprometido as monto_comprometido,dt.devengado, dt.ejecutado as ejecutado\n"
                + "FROM contabilidad.detalle_transaccion dt\n"
                + "INNER JOIN contabilidad.diario_general dg ON dg.id = dt.diario_general\n"
                + "INNER JOIN catalogo_presupuesto cprs ON cprs.id = dt.partida_presupuestaria\n"
                + "INNER JOIN plan_programatico ppro ON ppro.id = dt.estructura_programatica\n"
                + "WHERE dg.estado = true AND dg.enlace IS NULL\n"
                + "AND cast(dg.fecha_elaboracion as DATE) BETWEEN ?2 AND  ?3\n"
                + "AND dt.cedula_presupuestaria = ?1 AND cprs.flujo_ingreso = false \n"
                + "AND dg.certificaciones_presupuestario IS NULL AND\n"
                + "((dt.devengado >0 OR dt.devengado <0) OR (dt.ejecutado >0 OR dt.ejecutado <0))\n"
                + "UNION ALL\n"
                + "SELECT DISTINCT(dcp.id) as id, 0.00 as monto_solicitado, 0.00 as monto_comprometido,0.00 as devengado, dcp.ejecutado\n"
                + "FROM contabilidad.detalle_comprobante_pago dcp\n"
                + "INNER JOIN contabilidad.comprobante_pago cp ON cp.id = dcp.comprobante_pago\n"
                + "INNER JOIN contabilidad.diario_general dg ON cp.diario_general = dg.id\n"
                + "INNER JOIN contabilidad.detalle_transaccion dgt ON dgt.diario_general = dg.id\n"
                + "INNER JOIN certificacion_presupuestaria_anual.solicitud_reserva_compromiso sc ON sc.id = cp.reserva_compromiso\n"
                + "INNER JOIN certificacion_presupuestaria_anual.detalle_solicitud_compromiso dsc ON dsc.solicitud = sc.id\n"
                + "INNER JOIN catalogo_presupuesto cprs ON cprs.id = dgt.partida_presupuestaria\n"
                + "INNER JOIN plan_programatico ppro ON ppro.id = dgt.estructura_programatica\n"
                + "WHERE cast(cp.fecha_comprobante as DATE) BETWEEN ?2 AND ?3\n"
                + "AND dgt.cedula_presupuestaria = ?1 AND cprs.flujo_ingreso = false AND dgt.id_detalle_reserva = dsc.id\n"
                + "AND (CASE WHEN dsc.actividad_producto IS NOT NULL THEN (SELECT pro.codigo_presupuestario FROM producto pro\n"
                + "														WHERE pro.id = dsc.actividad_producto)\n"
                + "	 ELSE (SELECT pre.partida FROM presupuesto pre WHERE pre.id = dsc.presupuesto) END)= ?1\n"
                + "AND dcp.cedula_presupuestaria = ?1\n"
                + "UNION ALL\n"
                + "SELECT 0 as id, 0.00 as monto_solicitado, 0.00 as monto_comprometido, 0.00 as devengado, dcp.ejecutado\n"
                + "FROM contabilidad.detalle_comprobante_pago dcp\n"
                + "INNER JOIN contabilidad.comprobante_pago cp ON cp.id = dcp.comprobante_pago\n"
                + "INNER JOIN catalogo_presupuesto cprs ON cprs.id = dcp.partida_presupuestaria\n"
                + "INNER JOIN plan_programatico ppro ON ppro.id = dcp.estructura_programatica\n"
                + "LEFT JOIN catalogo_item fu ON fu.id = dcp.fuente\n"
                + "WHERE cast(cp.fecha_comprobante as DATE) BETWEEN ?2 AND ?3\n"
                + "AND cprs.flujo_ingreso = false\n"
                + "AND dcp.cedula_presupuestaria = ?1\n"
                + "AND cp.reserva_compromiso IS NULL AND cp.diario_general IS NULL ) valores ";
        try {
            Query query = em.createNativeQuery(sql)
                    .setParameter(1, presupuesto.getPartida())
                    .setParameter(2, Utils.getPrimerDiaAnio(Utils.getAnio(fecha)))
                    .setParameter(3, fecha);
            return (BigDecimal) query.getSingleResult();
        } catch (ParseException e) {
            return BigDecimal.ZERO;
        }
    }
//</editor-fold>

    public List<SolicitudReservaCompromiso> getReservaBeneficiario(String codigo, Short periodo, Date fechaDG, Cliente clienteAux) {
        List<SolicitudReservaCompromiso> resultado = (List<SolicitudReservaCompromiso>) em.createQuery("SELECT s FROM SolicitudReservaCompromiso s\n"
                + "INNER JOIN s.estado ci WHERE ci.codigo=:codigo AND s.contabilizado=FALSE AND s.comprometido=TRUE AND s.beneficiario=:cliente\n"
                + "AND cast(s.fechaAprobacion as date) <= :fechaDG AND s.periodo=:periodo ORDER BY s.secuencial ASC")
                .setParameter("codigo", codigo)
                .setParameter("periodo", periodo)
                .setParameter("fechaDG", fechaDG)
                .setParameter("cliente", clienteAux)
                .getResultList();
        return resultado;
    }
    
    public List<DetalleTransaccion> getDepreciaciones(Depreciacion depreciacion) {
        Query query = em.createNativeQuery("SELECT id_cuenta, sum(depreciacion_acumulada) as haber  FROM (\n"
                + "SELECT (CASE WHEN cc.periodo = ?2 THEN dd.cuenta_contable ELSE \n"
                + "(SELECT c.id from cuenta_contable c WHERE c.codigo = cc.codigo AND c.estado = true AND c.periodo = ?2)\n"
                + "END) as id_cuenta, dd.depreciacion_acumulada\n"
                + "FROM activos.depreciacion_detalle dd \n"
                + "INNER JOIN public.cuenta_contable cc ON dd.cuenta_contable = cc.id\n"
                + "WHERE dd.depreciacion = ?1 AND dd.estado=true\n"
                + ")detalle_diario\n"
                + "GROUP BY 1 ")
                .setParameter(1, depreciacion.getId())
                .setParameter(2, depreciacion.getPeriodo());
        List<Object[]> result = query.getResultList();
        if (result != null) {
            List<DetalleTransaccion> list = new ArrayList<>();
            int cont = 1;
            for (Object[] data : result) {
                DetalleTransaccion d = new DetalleTransaccion();
                d.setCuentaContable(getCuentaContableById((BigInteger) data[0]));
                d.setContador(BigInteger.valueOf(cont));
                d.setHaber((BigDecimal) data[1]);
                d.setDatoCargado(true);
                list.add(d);
                cont += 1;
            }
            return list;
        }
        return new ArrayList<>();
    }
    
    public List<DetalleTransaccion> getBienesIngreso(List<Long> listId) {
        Query query = em.createNativeQuery("SELECT cuenta_contable, sum(debe) as debe, sum(haber) as haber FROM (\n"
                + "SELECT bi.cuenta_contable, sum(bi.valor_total) as debe, 0 as haber,\n"
                + "cc.codigo, cc.periodo FROM activos.bienes_item bi \n"
                + "INNER JOIN activos.bienes_movimiento b ON bi.bienes_movimiento = b.id\n"
                + "INNER JOIN contabilidad.cont_cuentas cc ON bi.cuenta_contable = cc.id\n"
                + "WHERE bi.estado= true AND bi.item_bien_boolean=true AND bi.bienes_movimiento in (?1)\n"
                + "GROUP BY 1,4,5 \n"
                + "UNION\n"
                + "SELECT v.cta_depreciacion,0 as debe, sum(bie.depreciacion_acumulada) as haber,\n"
                + "c.codigo, c.periodo FROM activos.bienes_item bie\n"
                + "INNER JOIN activos.bienes_movimiento b ON bie.bienes_movimiento = b.id\n"
                + "INNER JOIN activos.bien_vida_util v ON bie.vida_util_bien = v.id\n"
                + "INNER JOIN contabilidad.cont_cuentas c ON v.cta_depreciacion = c.id\n"
                + "WHERE bie.estado= true AND bie.item_bien_boolean=true AND bie.depreciacion_acumulada is not null AND bie.bienes_movimiento in (?1)\n"
                + "GROUP BY 1,4,5 \n"
                + ") detalle GROUP BY 1 ORDER BY 2 DESC")
                .setParameter(1, listId);
        List<Object[]> result = query.getResultList();
        if (result != null) {
            List<DetalleTransaccion> list = new ArrayList<>();
            int cont = 1;
            for (Object[] data : result) {
                DetalleTransaccion d = new DetalleTransaccion();
                d.setCuentaContable(getCuentaContableById((BigInteger) data[0]));
                d.setContador(BigInteger.valueOf(cont));
                d.setDebe((BigDecimal) data[1]);
                d.setHaber((BigDecimal) data[2]);
                d.setDatoCargado(true);
                list.add(d);
                cont += 1;
            }
            return list;
        }
        return new ArrayList<>();
    }
}
