/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AnticipoRemuneracion;
import com.origami.sigef.common.entities.CaucionServidores;
import com.origami.sigef.common.entities.CuotaAnticipo;
import com.origami.sigef.common.entities.DescuentoRubroValor;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.FondosReserva;
import com.origami.sigef.common.entities.HoraExtraSuplementaria;
import com.origami.sigef.common.entities.LiquidacionRol;
import com.origami.sigef.common.entities.OtroDescuento;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.PrestamoIess;
import com.origami.sigef.common.entities.RetencionesImpuestoRenta;
import com.origami.sigef.common.entities.RolRubro;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.junit.Test;

/**
 *
 * @author ORIGAMI2
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class LiquidacionRolService extends AbstractService<LiquidacionRol> {

    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    @Inject
    private DiasLaboradoService diasService;
    @Inject
    private FondosReservaService fondosService;
    @Inject
    private OtroDescuentoService otroDescuentoService;
    @Inject
    private DescuentoRubroValorService descuentoService;
    @Inject
    private PrestamoIESService prestamoService;
    @Inject
    private RetencionImpuestoRentaService retencionService;
    @Inject
    private AnticipoRemuneracionService anticipoService;
    @Inject
    private CuotaAnticipoService cuotaService;
    @Inject
    private HoraExtraSuplementariaService horasService;
    @Inject
    private DistributivoEscalaService distributivoEscaService;
    @Inject
    private CaucionServidoresService caucionService;
    @Inject
    private ValoresDistributivoService valoresService;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private RolRubroService rolRubroService;

    private BigDecimal valorAportePartronal;
    private RolRubro rolRubro;
    BigDecimal netoRecibir;
    BigDecimal rmuMesGlobal;

    public LiquidacionRolService() {
        super(LiquidacionRol.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<LiquidacionRol> getListaXrol(TipoRol tipoRol) {
        Query query = em.createQuery("SELECT r FROM LiquidacionRol r WHERE r.estado = TRUE AND r.tipoRol = ?1")
                .setParameter(1, tipoRol);
        List<LiquidacionRol> result = (List<LiquidacionRol>) query.getResultList();
        return result;
    }

    public LiquidacionRol liquidacionRegistrada(TipoRol rol, String cedula) {
        try {
            Query query = em.createQuery("SELECT DISTINCT liq from LiquidacionRol liq INNER JOIN liq.rolPago r WHERE liq.estado = true AND r.servidor.estado = TRUE AND liq.tipoRol = ?1 AND r.servidor.persona.identificacion = ?2")
                    .setParameter(1, rol).setParameter(2, cedula);
            LiquidacionRol result = (LiquidacionRol) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public BigDecimal totalIngreso(TipoRol rol) {
        try {
            BigDecimal var = BigDecimal.ZERO;
            var = (BigDecimal) em.createQuery("SELECT SUM(lr.totalIngreso) FROM LiquidacionRol lr WHERE lr.estado = true AND lr.tipoRol = ?1")
                    .setParameter(1, rol).getSingleResult();
            return var;
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalEgreso(TipoRol rol) {
        try {
            BigDecimal var = (BigDecimal) em.createQuery("SELECT SUM(lr.totalEgreso) FROM LiquidacionRol lr WHERE lr.estado = true AND lr.tipoRol = ?1")
                    .setParameter(1, rol).getSingleResult();
            return var;
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        }
    }

    public BigDecimal totalNeto(TipoRol rol) {
        try {
            BigDecimal var = (BigDecimal) em.createQuery("SELECT SUM(lr.netoRecibir) FROM LiquidacionRol lr WHERE lr.estado = true AND lr.tipoRol = ?1")
                    .setParameter(1, rol).getSingleResult();
            return var;
        } catch (NoResultException e) {
            return BigDecimal.ZERO;
        }
    }

    public int actualizarEstadoLiquidacion(Boolean var, TipoRol rol) {
        int executeUpdate = 0;
        Query query = em.createNativeQuery("UPDATE talento_humano.liquidacion_rol\n"
                + "	SET estado=?1\n"
                + "	WHERE tipo_rol = ?2 AND estado = true").setParameter(1, var).setParameter(2, rol.getId());
        executeUpdate = query.executeUpdate();
        return executeUpdate;
    }

    //******************************REGISTRO DEL ROL****************************************************
    private void newRolRubro() {
        rolRubro = new RolRubro();
        rolRubro.setLiquidacionRol(new LiquidacionRol());
        rolRubro.setValorAsignacion(new ValoresRoles());
    }

    public DiasLaborado getDiasLaborado(Servidor s, TipoRol rolSeleccionado) {
        DiasLaborado dias = diasService.diaLaborado(rolSeleccionado, s);
        if (dias != null) {
            return dias;
        }
        return null;
    }

    public BigDecimal getSueldoNeto(DistributivoEscala escala, RolesDePago serv, TipoRol rolSeleccionado) {
        double sueldoNeto = (escala.getRemuneracionDolares().doubleValue() / TalentoHumano.diasCalendarioLaboral)
                * getDiasLaborado(serv.getServidor(), rolSeleccionado).getDias().doubleValue();
        return new BigDecimal(sueldoNeto);
    }

    public FondosReserva getRubroIngresoFondoDecimo(ValoresRoles tipo, RolesDePago s, TipoRol rolSeleccionado) {
        FondosReserva fondo = fondosService.getValorRubrol(rolSeleccionado, s.getServidor(), tipo);
        return fondo;
    }

    public List<DescuentoRubroValor> getListaOtroDescuento(RolesDePago s, ValoresRoles vr, TipoRol rolSeleccionado) {
        OtroDescuento desc = otroDescuentoService.getOtroDescuentos(s.getServidor(), rolSeleccionado);
        List<DescuentoRubroValor> lista = new ArrayList<>();
        if (desc != null) {
            lista = descuentoService.getListaXservidor(desc, vr);
        }
        return lista;
    }

    public CuotaAnticipo getCuotaAnticipo(LiquidacionRol serv, TipoRol rolSeleccionado) {
        AnticipoRemuneracion anticipo = anticipoService.getServidorAnticipoRMU(serv.getRolPago().getServidor(), anticipoService.getEstadoAnticipo("EST_ANTI", (short) 3), rolSeleccionado);
        CuotaAnticipo cuota = null;
        if (anticipo != null) {
            cuota = cuotaService.getCuotaMes(anticipo, rolSeleccionado);
        }
        return cuota;
    }

    public BigDecimal getHorasExtras(RolesDePago s, HoraExtraSuplementaria hora) {
        OpcionBusqueda busqueda = new OpcionBusqueda();
        DistributivoEscala escala = distributivoEscaService.getEscalaDistributivoAnio(s.getServidor().getDistributivo(), busqueda);
        double rmu = escala.getRemuneracionDolares().doubleValue();
        double horasEx, vHora = rmu / 240;
        BigDecimal valorHora = new BigDecimal(vHora).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        if (hora != null) {
            horasEx = ((valorHora.doubleValue() * 2) * hora.getHoraExtras());
            return new BigDecimal(horasEx).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getHorasSuplementaria(RolesDePago s, HoraExtraSuplementaria hora) {
        OpcionBusqueda busqueda = new OpcionBusqueda();
        DistributivoEscala escala = distributivoEscaService.getEscalaDistributivoAnio(s.getServidor().getDistributivo(), busqueda);
        double rmu = escala.getRemuneracionDolares().doubleValue();
        double horasSup, vHora = rmu / 240;
        BigDecimal valorHora = new BigDecimal(vHora).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        if (hora != null) {
            horasSup = ((valorHora.doubleValue() * 1.5) * hora.getHoraSuplementaria());
            return new BigDecimal(horasSup).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
        return BigDecimal.ZERO;
    }
//***********************************************************************************************************************//

    @Transactional
    @Test
    public void setearListRubros(LiquidacionRol liquidacionRol, List<ValoresRoles> listaValoresRoles,
            DistributivoEscala escala, TipoRol rolSeleccionado) {
        int i = 0, size = 100;
        for (ValoresRoles v : listaValoresRoles) {
            if (i > 0 && i % size == 0) {
                em.flush();
                em.clear();
            }
            rmuMes(liquidacionRol, v, escala, rolSeleccionado);
            decimosFondo(liquidacionRol, v, rolSeleccionado);
            otrosEgresos(liquidacionRol, v, rolSeleccionado);
            prestamosIESS(liquidacionRol, v, rolSeleccionado);
            retencionImpRenta(liquidacionRol, v, rolSeleccionado);
            anticipoRMU(liquidacionRol, v, rolSeleccionado);
            horasExtrasSuple(liquidacionRol, v, rolSeleccionado);
            caucionServidor(liquidacionRol, v, rolSeleccionado);
            i++;
        }
        for (ValoresRoles v : listaValoresRoles) {
            if (i > 0 && i % size == 0) {
                em.flush();
                em.clear();
            }
            aportePatronal(liquidacionRol, v, rmuMesGlobal, rolSeleccionado);
            aportePersonalEgreso(liquidacionRol, v, rmuMesGlobal);
            i++;
        }

        for (ValoresRoles v : listaValoresRoles) {
            if (i > 0 && i % size == 0) {
                em.flush();
                em.clear();
            }
            aportePatronalEgreso(liquidacionRol, v, valorAportePartronal);
            i++;
        }
        calcularTotales(liquidacionRol, rolSeleccionado);
        for (ValoresRoles v : listaValoresRoles) {
            if (i > 0 && i % size == 0) {
                em.flush();
                em.clear();
            }
            netoRecibir(liquidacionRol, v);
            i++;
        }
    }

    private void rmuMes(LiquidacionRol liq, ValoresRoles vr, DistributivoEscala escala, TipoRol rolSeleccionado) {
        boolean ban = false;
        BigDecimal rmuMes;
        if ("RAU".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            ban = true;
            rmuMesGlobal = BigDecimal.ZERO;
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorAsignacion(vr);
        }
        if (getDiasLaborado(liq.getRolPago().getServidor(), rolSeleccionado) != null && ban) {
            rolRubro.setValorRubro(getSueldoNeto(escala, liq.getRolPago(), rolSeleccionado));
            rmuMes = rolRubro.getValorRubro();
            rmuMesGlobal = rmuMes;
            em.persist(rolRubro);
        }
    }

    private void decimosFondo(LiquidacionRol liq, ValoresRoles vr, TipoRol rolSeleccionado) {
        boolean var = false;
        if ("DC".equals(vr.getValorParametrizable().getTipo().getCodigo()) && getRubroIngresoFondoDecimo(vr, liq.getRolPago(), rolSeleccionado) != null) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if ("DT".equals(vr.getValorParametrizable().getTipo().getCodigo()) && getRubroIngresoFondoDecimo(vr, liq.getRolPago(), rolSeleccionado) != null) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
//        if ("FR".equals(vr.getValorParametrizable().getTipo().getCodigo()) && getRubroIngresoFondoDecimo(vr, liq.getRolPago()) != null) {
//            newRolRubro();
//            var = true;
//            rolRubro.setValorAsignacion(vr);
//        }
        if (getRubroIngresoFondoDecimo(vr, liq.getRolPago(), rolSeleccionado) != null && var) {
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(getRubroIngresoFondoDecimo(vr, liq.getRolPago(), rolSeleccionado).getValorFondos());
            em.persist(rolRubro);
        }
    }

    private void otrosEgresos(LiquidacionRol liq, ValoresRoles vr, TipoRol rolSeleccionado) {
        List<DescuentoRubroValor> listaOtrosEgerso = new ArrayList<>();
        double acum = 0;
        boolean var = false;
        if ("RET_JUD".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            listaOtrosEgerso = getListaOtroDescuento(liq.getRolPago(), vr, rolSeleccionado);
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if ("OTROS_E".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            listaOtrosEgerso = getListaOtroDescuento(liq.getRolPago(), vr, rolSeleccionado);
            rolRubro.setValorAsignacion(vr);
        }
        if (!listaOtrosEgerso.isEmpty() && var) {
            for (DescuentoRubroValor d : listaOtrosEgerso) {
                acum += d.getValorDescuento().doubleValue();
            }
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(new BigDecimal(acum));
            em.persist(rolRubro);
        }
    }

    private void prestamosIESS(LiquidacionRol liq, ValoresRoles vr, TipoRol rolSeleccionado) {
        List<PrestamoIess> lista = prestamoService.prestamoIESSXserv(rolSeleccionado, liq.getRolPago().getServidor(), vr);
        double acum = 0;
        boolean var = false;
        if ("PRES_QUI".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if ("PRES_HIP".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if (!lista.isEmpty() && var) {
            for (PrestamoIess iess : lista) {
                acum += iess.getValorCuota().doubleValue();
            }
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(new BigDecimal(acum));
            em.persist(rolRubro);
        }
    }

    private void retencionImpRenta(LiquidacionRol liq, ValoresRoles vr, TipoRol rolSeleccionado) {
        RetencionesImpuestoRenta ret = retencionService.getRetencionMensual(rolSeleccionado, liq.getRolPago().getServidor());
        boolean var = false;
        if ("IMP_RENTA".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if (ret != null && var) {
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(ret.getCuotaMensual());
            em.persist(rolRubro);
        }
    }

    private void anticipoRMU(LiquidacionRol liq, ValoresRoles vr, TipoRol rolSeleccionado) {
        boolean var = false;
        if ("ANT_RMU".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if (getCuotaAnticipo(liq, rolSeleccionado) != null && var) {
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(getCuotaAnticipo(liq, rolSeleccionado).getValorCuota());
            em.persist(rolRubro);
        }
    }

    private void horasExtrasSuple(LiquidacionRol liq, ValoresRoles vr, TipoRol rolSeleccionado) {
        boolean ban = false;
        HoraExtraSuplementaria hora = new HoraExtraSuplementaria();
        double totalValorHora = 0;
        if ("HORAS_SUP".equals(vr.getValorParametrizable().getTipo().getCodigo()) && vr.getValorParametrizable().getOrigen().getCodigo().equals("DIS-ANE")) {
            newRolRubro();
            ban = true;
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorAsignacion(vr);
            hora = horasService.getHoraExtraSuplementaria(liq.getRolPago().getServidor(), rolSeleccionado);
        }
        if (ban && hora != null) {
            totalValorHora = getHorasExtras(liq.getRolPago(), hora).doubleValue() + getHorasSuplementaria(liq.getRolPago(), hora).doubleValue();
            rolRubro.setValorRubro(new BigDecimal(totalValorHora).setScale(2, BigDecimal.ROUND_HALF_EVEN));
            em.persist(rolRubro);
        }
    }

    private void caucionServidor(LiquidacionRol liq, ValoresRoles vr, TipoRol rolSeleccionado) {
        boolean var = false;
        CaucionServidores ser = new CaucionServidores();
        if ("CAUCIONES".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            ser = caucionService.caucionRubro(rolSeleccionado, liq.getRolPago().getServidor(), "CAUCIONES");
            rolRubro.setValorAsignacion(vr);
        }
        if (var && ser != null) {
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(ser.getCuotaPropocional());
            em.persist(rolRubro);
        }
    }

    private void aportePatronal(LiquidacionRol liq, ValoresRoles vr, BigDecimal rb, TipoRol rolSeleccionado) {
        boolean var = false, var2 = false;
        ParametrosTalentoHumano valorAporte = new ParametrosTalentoHumano();
        double aporte;
        if ("ALOSEP".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            valorAportePartronal = BigDecimal.ZERO;
            var = true;
            valorAporte = parametroService.valorParametros("ALOSEP", Boolean.TRUE);
            rolRubro.setValorAsignacion(vr);
        }
        if ("ACT".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            valorAportePartronal = BigDecimal.ZERO;
            var = true;
            valorAporte = parametroService.valorParametros("ACT", Boolean.TRUE);
            rolRubro.setValorAsignacion(vr);
        }
        if ("ALEOEP".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            valorAportePartronal = BigDecimal.ZERO;
            var = true;
            valorAporte = parametroService.valorParametros("ALEOEP", Boolean.TRUE);
            rolRubro.setValorAsignacion(vr);
        }
        if (var && valorAporte != null) {
            aporte = (rb.doubleValue() * valorAporte.getValor().doubleValue()) / 100;
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(new BigDecimal(aporte));
            valorAportePartronal = rolRubro.getValorRubro();
            em.persist(rolRubro);
        }
    }

    private void aportePersonalEgreso(LiquidacionRol liq, ValoresRoles vr, BigDecimal rubro) {
        boolean var = false;
        ParametrosTalentoHumano valorAporte = new ParametrosTalentoHumano();

        double valor;
        if ("APO_INDIV_CODIGO".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
            valorAporte = parametroService.valorParametroTipo("APOR_IND_LOSEP");
        }
        if ("APOR_IND_LOSEP".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
            valorAporte = parametroService.valorParametroTipo("APOR_IND_LOSEP");
        }
        if ("APOR_IND_LOEP".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
            valorAporte = parametroService.valorParametroTipo("APOR_IND_LOEP");
        }
        if (var && valorAporte.getId() != null) {
            valor = (rubro.doubleValue() * valorAporte.getValor().doubleValue()) / 100;
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(new BigDecimal(valor));
            em.persist(rolRubro);
        }
    }

    public void aportePatronalEgreso(LiquidacionRol liq, ValoresRoles vr, BigDecimal rubro) {
        boolean var = false;
        if ("APOR_IESS_CT".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if ("APOR_IESS_LOSEP".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if ("APOR_IESS_LOEP".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if (var) {
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(rubro);
            em.persist(rolRubro);

        }
    }

    @Transactional
    @Test
    public void calcularTotales(LiquidacionRol liq, TipoRol rolSeleccionado) {
        netoRecibir = BigDecimal.ZERO;
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        BigDecimal totaIng = BigDecimal.ZERO, totalEg = BigDecimal.ZERO, totalNeto = BigDecimal.ZERO;
        for (RolRubro r : lista) {
            if (r.getValorAsignacion().getValorParametrizable().getClasificacion().getCodigo().equals("I")) {
                totaIng = totaIng.add(r.getValorRubro().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            } else {
                totalEg = totalEg.add(r.getValorRubro().setScale(2, BigDecimal.ROUND_HALF_EVEN));
            }
        }
//        System.out.println("total ingreso " + totaIng + " total egreso " + totalEg);
        totalNeto = totaIng.subtract(totalEg).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        liq.setTotalIngreso(totaIng);
        liq.setTotalEgreso(totalEg);
        liq.setNetoRecibir(totalNeto);
        netoRecibir = liq.getNetoRecibir();
        this.edit(liq);
    }

    public void netoRecibir(LiquidacionRol liq, ValoresRoles vr) {
        boolean var = false;
        if ("SUELDON-EGRESO".equals(vr.getValorParametrizable().getTipo().getCodigo())) {
            newRolRubro();
            var = true;
            rolRubro.setValorAsignacion(vr);
        }
        if (var && netoRecibir.intValue() > 0) {
            rolRubro.setLiquidacionRol(liq);
            rolRubro.setValorRubro(netoRecibir);
            em.persist(rolRubro);
        }
    }
}
