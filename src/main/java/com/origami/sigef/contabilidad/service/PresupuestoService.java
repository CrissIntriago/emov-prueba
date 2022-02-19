/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Model.CedulaPresupuestariaEgreso;
import com.origami.sigef.Presupuesto.Model.CedulaPresupuestariaEgresoConsolidada;
import com.origami.sigef.Presupuesto.Model.ListaItemCedulaPresupuestariaIngreso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.PartidasDistributivo;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Presupuesto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.model.presupuestoModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Luis Suarez
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class PresupuestoService extends AbstractService<Presupuesto> {

    @Inject
    private ManagerService service;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public PresupuestoService() {
        super(Presupuesto.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<presupuestoModel> presupuestoIngresos(Short periodo) {

        Query query = em.createQuery("SELECT p.item.codigo,f.codFuente, p.presupuestoCodificado,p.item,p.fuente FROM ProformaIngreso p inner join p.fuente f   WHERE  p.periodo= :periodo AND p.item.activo = TRUE")
                .setParameter("periodo", periodo);
        List<Object[]> result = query.getResultList();
        if (Utils.isNotEmpty(result)) {
            List<presupuestoModel> list = new ArrayList<>();
            for (Object[] data : result) {
                presupuestoModel ppdto = new presupuestoModel();
                ppdto.setPartida((String) data[0] + data[1]);
                ppdto.setTotalingreso((BigDecimal) data[2]);
                ppdto.setTotalegresos(null);
                ppdto.setItemPresupuestario((PresCatalogoPresupuestario) data[3]);
                ppdto.setFuente((PresFuenteFinanciamiento) data[4]);
                ppdto.setTipo(true);
                list.add(ppdto);
            }
            return list;
        }

        return null;
    }

    public List<presupuestoModel> presupuestoIngresosReformada(Short periodo) {
// p.codigo, ,p.presupuestoInicial ,p.reformaSuplementaria,p.reformaReduccion,p.presupuestoCodificado,p
        Query query = em.createQuery("SELECT p.item.codigo, f.codFuente,p.presupuestoInicial,p.reformaSuplementaria,p.reformaReduccion,p.presupuestoCodificado,p "
                + " FROM  ProformaIngreso p inner join p.fuente f WHERE p.presupuestoInicial IS NOT NULL AND p.item.activo = TRUE");
        List<Object[]> result = query.getResultList();
        if (Utils.isNotEmpty(result)) {
            List<presupuestoModel> list = new ArrayList<>();
            for (Object[] data : result) {
                presupuestoModel ppdto = new presupuestoModel();
                ppdto.setPartida((String) data[0] + data[1]);
                ppdto.setPresupuestoInicial((BigDecimal) data[2]);
                ppdto.setReformaSuplemento((BigDecimal) data[3]);
                ppdto.setReformaReduccion((BigDecimal) data[4]);
                ppdto.setTraspasoIncremento(BigDecimal.ZERO);
                ppdto.setTraspasoReduccion(BigDecimal.ZERO);
                ppdto.setReformaCodificado((BigDecimal) data[5]);

                ppdto.setEstructuraProgramatica(null);

                ProformaIngreso tmp = (ProformaIngreso) data[6];
                if (tmp != null) {
                    ppdto.setItemPresupuestario(tmp.getItem());
                }
                ppdto.setFuente(null);

                ppdto.setTipo(true);
                list.add(ppdto);
            }
            return list;
        }

        return null;
    }

    public List<presupuestoModel> presupuestoEgresos(Short periodo) {

//        String hql = "SELECT p.codigoPresupuestario,SUM(p.monto) FROM Producto p INNER JOIN p.estructuraProgramatica estructura ";
//        List<Object[]> result = (List<Object[]>) em.createQuery("SELECT p.codigoPresupuestario AS partida,SUM(p.monto) AS total,p.estructuraProgramatica AS estructuraProgramatica ,p.itemPresupuestario AS itemPresupuestario,p.fuente AS fuente FROM Producto p  WHERE p.periodo= :periodo GROUP BY p.codigoPresupuestario,p.estructuraProgramatica,p.itemPresupuestario,p.fuente")
//                .setParameter("periodo",periodo)
//                .getResultList();
        Query query = em.createQuery("SELECT p.partidaPresupuestaria AS partida,p.valor AS total,p.estructruaNew AS estructuraProgramatica ,p.itemNew AS itemPresupuestario,p.fuenteNew AS fuente FROM ProformaPresupuestoPlanificado p  WHERE p.periodo= :periodo AND p.codigoReforma is null AND p.codigoReformaTraspaso is null")
                .setParameter("periodo", periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<presupuestoModel> list = new ArrayList<>();
            for (Object[] data : result) {
                presupuestoModel ppdto = new presupuestoModel();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotalegresos((BigDecimal) data[1]);
                ppdto.setTotalingreso(null);
                ppdto.setEstructuraProgramatica((PresPlanProgramatico) data[2]);
                ppdto.setItemPresupuestario((PresCatalogoPresupuestario) data[3]);
                ppdto.setFuente((PresFuenteFinanciamiento) data[4]);
                ppdto.setTipo(false);
                list.add(ppdto);
            }
            return list;
        }

        return null;
    }

    public List<presupuestoModel> presupuestoEgresosReformado(Short periodo) {

//        String hql = "SELECT p.codigoPresupuestario,SUM(p.monto) FROM Producto p INNER JOIN p.estructuraProgramatica estructura ";
//        List<Object[]> result = (List<Object[]>) em.createQuery("SELECT p.codigoPresupuestario AS partida,SUM(p.monto) AS total,p.estructuraProgramatica AS estructuraProgramatica ,p.itemPresupuestario AS itemPresupuestario,p.fuente AS fuente FROM Producto p  WHERE p.periodo= :periodo GROUP BY p.codigoPresupuestario,p.estructuraProgramatica,p.itemPresupuestario,p.fuente")
//                .setParameter("periodo",periodo)
//                .getResultList();
        Query query = em.createQuery("SELECT p.partidaPresupuestaria AS partida,COALESCE(p.valor,0) AS total,COALESCE(p.reformaSuplemento,0),COALESCE(p.reformaReduccion,0),COALESCE(p.traspasoIncremento,0),"
                + "COALESCE(p.traspasoReduccion,0),COALESCE(p.reformaCodificado,0),"
                + "p.estructruaNew AS estructuraProgramatica ,p.itemNew AS itemPresupuestario,p.fuenteNew AS fuente FROM ProformaPresupuestoPlanificado p"
                + "  WHERE p.periodo= :periodo AND p.codigoReforma IS NULL AND p.codigoReformaTraspaso IS NULL")
                .setParameter("periodo", periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<presupuestoModel> list = new ArrayList<>();
            for (Object[] data : result) {
                presupuestoModel ppdto = new presupuestoModel();
                ppdto.setPartida((String) data[0]);
                ppdto.setPresupuestoInicial((BigDecimal) data[1]);
                ppdto.setReformaSuplemento((BigDecimal) data[2]);
                ppdto.setReformaReduccion((BigDecimal) data[3]);
                ppdto.setTraspasoIncremento((BigDecimal) data[4]);
                ppdto.setTraspasoReduccion((BigDecimal) data[5]);
                ppdto.setReformaCodificado((BigDecimal) data[6]);
                ppdto.setEstructuraProgramatica((PresPlanProgramatico) data[7]);
                ppdto.setItemPresupuestario((PresCatalogoPresupuestario) data[8]);
                ppdto.setFuente((PresFuenteFinanciamiento) data[9]);
                ppdto.setTipo(false);
                list.add(ppdto);
            }
            return list;
        }

        return null;
    }

    public List<ProformaPresupuestoPlanificado> getProformaEgreso(Short periodo) {
        Query query = em.createQuery("SELECT pro FROM ProformaPresupuestoPlanificado pro WHERE pro.periodo = ?1 AND pro.codigo <> 'PAPP' AND pro.codigoReforma is null AND pro.codigoReformaTraspaso is null")
                .setParameter(1, periodo);
        List<ProformaPresupuestoPlanificado> result = query.getResultList();
        return result;
    }

    public PlanProgramatico devulveObjeto(Short periodo, Long id) {
        PlanProgramatico plan = (PlanProgramatico) em.createQuery("SELECT p FROM PlanProgramatico p WHERE p.id=:id")
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
        return plan;
    }

    public CatalogoPresupuesto devulveObjetoItem(Long id) {
        CatalogoPresupuesto item = (CatalogoPresupuesto) em.createQuery("SELECT p FROM CatalogoPresupuesto p WHERE p.id=:id")
                .setParameter("id", id)
                .getSingleResult();
        return item;
    }

    public FuenteFinanciamiento devulveObjetoFuente(Short periodo, Long id) {
        FuenteFinanciamiento fuente = (FuenteFinanciamiento) em.createQuery("SELECT ae FROM FuenteFinanciamiento ae WHERE ae.id= :id AND ae.periodo= :periodo")
                .setParameter("id", id)
                .setParameter("periodo", periodo)
                .getSingleResult();
        return fuente;
    }

    public CatalogoItem devulveObjetoFuenteDirecta(Long id) {
        CatalogoItem fuenteDirecta = (CatalogoItem) em.createQuery("SELECT ae FROM CatalogoItem ae WHERE ae.id= :id")
                .setParameter("id", id)
                .getSingleResult();
        return fuenteDirecta;
    }

    public List<ProformaPDTO> muestrameEgresos(Short periodo) {

        Query query = em.createNativeQuery("select p.codigo_presupuestario as partida,sum(p.monto) as total ,p.estructura_new,\n"
                + "p.item_new,p.fuente_new\n"
                + "               from producto p WHERE EXISTS (SELECT * from proforma_presupuesto_planificado pro\n"
                + "               where pro.partida_presupuestaria=p.codigo_presupuestario\n"
                + "               and pro.periodo=?1 and pro.codigo='PAPP')\n"
                + "               AND p.periodo=?2 AND p.codigo_reforma IS NULL AND p.codigo_reforma_traspaso IS NULL\n"
                + "				GROUP BY p.codigo_presupuestario,p.estructura_new,p.item_new,p.fuente_new")
                .setParameter(1, periodo)
                .setParameter(2, periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<ProformaPDTO> list = new ArrayList<>();
            for (Object[] data : result) {
                BigInteger a = (BigInteger) data[2];
                BigInteger b = (BigInteger) data[3];
                BigInteger c = (BigInteger) data[4];
                PresPlanProgramatico plan = service.find(PresPlanProgramatico.class, a.longValue());
                PresCatalogoPresupuestario item = service.find(PresCatalogoPresupuestario.class, b.longValue());
                PresFuenteFinanciamiento fuente = service.find(PresFuenteFinanciamiento.class, c.longValue());
                ProformaPDTO ppdto = new ProformaPDTO();
                ppdto.setPartida((String) data[0]);
                ppdto.setTotal((BigDecimal) data[1]);
                ppdto.setEstructuraNew(plan);
                ppdto.setItemNew(item);
                ppdto.setFuenteNew(fuente);
                list.add(ppdto);
            }
            return list;
        }

        return null;
    }

    public BigDecimal totalIngresoAprobados(Short periodo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.valorIngreso) FROM Presupuesto p WHERE p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalEgresoAprobados(Short periodo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.valorEgreso) FROM Presupuesto p WHERE p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalPresupuestoInicial(Short periodo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.presupuestoInicial) FROM Presupuesto p WHERE  p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalReformaSuplemenataria(Short periodo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemetario) FROM Presupuesto p WHERE  p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalReformaReduccion(Short periodo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReducido) FROM Presupuesto p WHERE p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalPresupuestoCodificado(Short periodo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.codificado) FROM Presupuesto p WHERE p.periodo= :periodo")
                .setParameter("periodo", periodo)
                .getSingleResult();

        return val;
    }

    public List<CatalogoProformaPresupuesto> getProformaAprobadas(short anio) {
        Query query = em.createQuery("SELECT c from CatalogoProformaPresupuesto c WHERE c.aprobado = true AND c.periodo = :anio")
                .setParameter("anio", anio);
        List<CatalogoProformaPresupuesto> result = query.getResultList();
        return result;
    }

    public void eliminarPresupuesto(boolean tipo, Short periodo) {
        String hql = "delete from Presupuesto p WHERE p.tipo= :tipo AND p.periodo= :periodo";
        Query query = em.createQuery(hql)
                .setParameter("tipo", tipo)
                .setParameter("periodo", periodo);

        int rowCount = query.executeUpdate();
    }

    public List<PartidasDistributivo> filtroCodigo(Short periodo) {
        Query query = em.createQuery("SELECT p "
                + "FROM PartidasDistributivo p "
                + "WHERE EXISTS (SELECT pro FROM ProformaPresupuestoPlanificado pro WHERE pro.partidaPresupuestaria = p.partidaRau AND pro.periodo = ?1 AND pro.codigo = 'PD' )"
                + "AND p.periodo= ?2")
                .setParameter(1, periodo)
                .setParameter(2, periodo);

        List<PartidasDistributivo> result = query.getResultList();
        return result;
    }

    public BigDecimal totalPresupuestoInicialFiltrado(Short periodo, boolean tipo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.presupuestoInicial) FROM Presupuesto p WHERE  p.periodo= :periodo AND p.tipo= :tipo")
                .setParameter("periodo", periodo).setParameter("tipo", tipo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalReformaSuplemenatariaFiltrado(Short periodo, boolean tipo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.reformaSuplemetario) FROM Presupuesto p WHERE  p.periodo= :periodo AND p.tipo= :tipo")
                .setParameter("periodo", periodo).setParameter("tipo", tipo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalReformaReduccionFiltrado(Short periodo, boolean tipo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.reformaReducido) FROM Presupuesto p WHERE p.periodo= :periodo AND p.tipo= :tipo")
                .setParameter("periodo", periodo).setParameter("tipo", tipo)
                .getSingleResult();

        return val;
    }

    public BigDecimal totalPresupuestoCodificadoFiltrado(Short periodo, boolean tipo) {

        BigDecimal val = BigDecimal.ZERO;

        val = (BigDecimal) em.createQuery("SELECT SUM(p.codificado) FROM Presupuesto p WHERE p.periodo= :periodo AND p.tipo= :tipo")
                .setParameter("periodo", periodo).setParameter("tipo", tipo)
                .getSingleResult();

        return val;
    }

    public List<Presupuesto> getPartidaPresupuestoIngresosEgresos(Short periodo, boolean tipo) {
        Query query = em.createQuery("SELECT p FROM Presupuesto p WHERE p.periodo = :periodo AND p.tipo = :tipo ORDER BY p.partida")
                .setParameter("periodo", periodo)
                .setParameter("tipo", tipo);
        List<Presupuesto> result = query.getResultList();
        return result;
    }

    public List<Presupuesto> getPresupuestoPeriodo(Short anio) {
        try {
            Query query = em.createQuery("SELECT p FROM Presupuesto p where p.periodo = ?1")
                    .setParameter(1, anio);
            List<Presupuesto> presupuesto = (List<Presupuesto>) query.getResultList();
            return presupuesto;
        } catch (Exception e) {
            return null;
        }
    }

    public Presupuesto findPresupuestoByCodigoAndPerido(String partida, Short periodo) {
        try {
            Query query = em.createQuery("SELECT p FROM Presupuesto p where p.periodo = ?1 AND p.partida = ?2")
                    .setParameter(1, periodo).setParameter(2, partida);
            Presupuesto presupuesto = (Presupuesto) query.getSingleResult();
            return presupuesto;
        } catch (Exception e) {
            return null;
        }
    }

    public List<PresCatalogoPresupuestario> listaItmes() {
        return (List<PresCatalogoPresupuestario>) em.createQuery("SELECT DISTINCT(i) FROM Presupuesto p INNER JOIN p.itemNew i").getResultList();
    }

    public List<PresPlanProgramatico> listaEstructura() {
        return (List<PresPlanProgramatico>) em.createQuery("SELECT DISTINCT(es) FROM Presupuesto p INNER JOIN p.estructruaNew es").getResultList();
    }

    public List<PresFuenteFinanciamiento> listaFuente() {
        return (List<PresFuenteFinanciamiento>) em.createQuery("SELECT DISTINCT(f) FROM Presupuesto p INNER JOIN p.fuenteNew f").getResultList();
    }

    public BigDecimal getReservaDistributivos(Long id, Short periodo) {
        List<BigDecimal> result = (List<BigDecimal>) getEntityManager().createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso d INNER JOIN "
                + "d.refDistributivo di INNER JOIN d.solicitud s INNER JOIN s.estado e WHERE e.codigo in ('LIQUI', 'APRO') AND di.id=:id AND d.periodo=:periodo")
                .setParameter("id", id).setParameter("periodo", periodo).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getReservasPartidasDirectas(Long id, Short periodo) {
        List<BigDecimal> result = (List<BigDecimal>) getEntityManager().createQuery("SELECT COALESCE(SUM(d.montoSolicitado),0) FROM DetalleSolicitudCompromiso d INNER JOIN d.partidasDirecta pdi  INNER JOIN d.solicitud s INNER JOIN s.estado e WHERE e.codigo in ('LIQUI', 'APRO') AND pdi.id=:id AND d.periodo=:periodo")
                .setParameter("id", id).setParameter("periodo", periodo).getResultList();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }
        return BigDecimal.ZERO;

    }

    public List<ListaItemCedulaPresupuestariaIngreso> listReformaCedulaPrespupuestariaIngreso(Date fechaDesde, Date fechaHasta, Short periodo) {
        Query query = em.createNativeQuery("select * from reportes.fct_reporte_cedula_presupuestaria_ingreso(?1,?2,?3)")
                .setParameter(1, fechaDesde).setParameter(2, fechaHasta).setParameter(3, periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<ListaItemCedulaPresupuestariaIngreso> list = new ArrayList<>();
            for (Object[] data : result) {
                ListaItemCedulaPresupuestariaIngreso ppdto = new ListaItemCedulaPresupuestariaIngreso();
                ppdto.setId((BigInteger) data[0]);
                ppdto.setCodigo((String) data[1]);
                ppdto.setDescripcion((String) data[2]);
                ppdto.setPeriodo((Short) data[3]);
                ppdto.setNivel((Integer) data[4]);
                ppdto.setFuente((BigInteger) data[5]);
                ppdto.setNombre((String) data[6]);
                ppdto.setPresupuesto_inicial((BigDecimal) data[7]);
                ppdto.setReforma((BigDecimal) data[8]);
                ppdto.setCodificado((BigDecimal) data[9]);
                ppdto.setDevengado((BigDecimal) data[10]);
                ppdto.setRecaudado((BigDecimal) data[11]);
                ppdto.setMovimiento((Boolean) data[12]);
                ppdto.setPadre((BigInteger) data[13]);
                list.add(ppdto);
            }
            return list;
        }
        return null;
    }

    public List<CedulaPresupuestariaEgreso> listaCedulaPresupuestariaEgresos(Date fechaDesde, Date fechaHasta, Short periodo) {
        Query query = em.createNativeQuery("select * from presupuesto.fct_cedula_presupuestari_egresos(?1,?2,?3)")
                .setParameter(1, fechaDesde).setParameter(2, fechaHasta).setParameter(3, periodo);

        List<Object[]> result = query.getResultList();
        System.out.println("Todo bien ...");
        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<CedulaPresupuestariaEgreso> list = new ArrayList<>();
            for (Object[] data : result) {
                CedulaPresupuestariaEgreso ppdto = new CedulaPresupuestariaEgreso();
                ppdto.setCodigo((String) data[0]);
                ppdto.setItem((String) data[1]);
                ppdto.setNivel((Integer) data[2]);
                ppdto.setEconomico((String) data[3]);
                ppdto.setCodigo_estructura((String) data[4]);
                ppdto.setDescripcion_estructura((String) data[5]);
                ppdto.setTotal_presupuesto_inicial((BigDecimal) data[6]);
                ppdto.setReforma((BigDecimal) data[7]);
                ppdto.setCodificado((BigDecimal) data[8]);
                ppdto.setReservas((BigDecimal) data[9]);
                ppdto.setComprometido((BigDecimal) data[10]);
                ppdto.setDevengado((BigDecimal) data[11]);
                ppdto.setPagado((BigDecimal) data[12]);
                ppdto.setSaldo_xcomprometer((BigDecimal) data[13]);
                ppdto.setSaldo_xdevengar((BigDecimal) data[14]);
                ppdto.setSaldo_xpagar((BigDecimal) data[15]);
                ppdto.setMovimiento((Boolean) data[16]);
                ppdto.setPadre((BigInteger) data[17]);
                System.out.println("PPDTO -->"+ppdto.getCodigo()+" Padre: "+ppdto.getPadre());
                list.add(ppdto);
            }
            return list;
        }
        return null;
    }

    public List<CedulaPresupuestariaEgresoConsolidada> listaCedulaPresupuestariaEgresosConsolidado(Date fechaDesde, Date fechaHasta, Short periodo) {
        Query query = em.createNativeQuery("select * from reportes.reporte_definitivo_cedula_presupuestaria_egresos_consolidados(?1,?2,?3)")
                .setParameter(1, fechaDesde).setParameter(2, fechaHasta).setParameter(3, periodo);

        List<Object[]> result = query.getResultList();

        Map<String, Object> hints = query.getHints();
        if (Utils.isNotEmpty(result)) {
            List<CedulaPresupuestariaEgresoConsolidada> list = new ArrayList<>();
            for (Object[] data : result) {
                CedulaPresupuestariaEgresoConsolidada ppdto = new CedulaPresupuestariaEgresoConsolidada();
                ppdto.setCodigo((String) data[0]);
                ppdto.setDescripcion((String) data[1]);
                ppdto.setPeriodo((Short) data[2]);
                ppdto.setNivel((Integer) data[3]);
                ppdto.setEconomico((String) data[4]);
                ppdto.setPresupuestInicial((BigDecimal) data[5]);
                ppdto.setReforma((BigDecimal) data[6]);
                ppdto.setDevengado((BigDecimal) data[7]);
                ppdto.setReservas((BigDecimal) data[9]);
                ppdto.setComprometido((BigDecimal) data[10]);
                ppdto.setPagado((BigDecimal) data[11]);
                ppdto.setPadre((BigInteger) data[12]);
                ppdto.setMovimiento((Boolean) data[13]);
                ppdto.setCodificado(ppdto.getPresupuestInicial().add(ppdto.getReforma()));
                ppdto.setSaldoXcomprometer(ppdto.getCodificado().subtract(ppdto.getComprometido()));
                ppdto.setSaldoXdevengar(ppdto.getCodificado().subtract(ppdto.getDevengado()));
                ppdto.setSaldoXPagar(ppdto.getDevengado().subtract(ppdto.getPagado()));
                list.add(ppdto);
            }
            return list;
        }
        return null;
    }

}
