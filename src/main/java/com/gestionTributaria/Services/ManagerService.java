/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.*;
import com.catastro.Entities.AvalDetCobroImpuestoPredios;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Controller.FnResolucionTipo;
import com.gestionTributaria.Entities.*;
import com.gestionTributaria.models.Archivo;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;

import com.gestionTributaria.models.CatPredioModel;
import com.gestionTributaria.models.CatPredioPropieatrioDTO;
import com.gestionTributaria.models.CatPredioRuralDTO;
import com.gestionTributaria.models.CatPredioRusticoDTO;
import com.gestionTributaria.models.EmisionesRuralesExcelDTO;
import com.gestionTributaria.models.InteresRecargoDescuento;
import com.gestionTributaria.models.PredioModelWs;
import com.gestionTributaria.models.ReporteSeguimientoValidadores;
import com.gestionTributaria.models.SolicitudExoneracionEnteDTO;
import com.gestionTributaria.models.ValoracionPredialDTO;
import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;

import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.models.Tramites;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.AccessTimeout;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.apache.commons.math3.util.Precision;
import org.apache.shiro.session.Session;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
//import com.origami.sigef.common.service.AbstractService;

/**
 *
 * @author DEVELOPER
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class ManagerService extends AbstractService<CtlgSalario> {
    
    @Inject
    private UserSession session;
    @Inject
    private FinaRenLiquidacionService liquidacionServices;
    @Inject
    private FinaRenPagoService finaRenPagoService;
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    @Inject
    private FinaRenLiquidacionService renLiquidacionService;
    @Inject
    private RenBalanceService balanceService;
    @Inject
    private FinaRenLiquidacionService finaRenLiquidacionService;
    @Inject
    private FinaRenDetalleLiquidacionService detalleLiquidacionService;
    @Inject
    private RemisionInteresServices remisionInteresServices;
    @Inject
    private RenDetallePlusvaliaServices renDetallePlusvaliaServices;
    @Inject
    private RenValoresPlusvaliaServices renValoresPlusvaliaServices;
    @Inject
    private RecaudacionInteface recaudacionService;
    @Inject
    private CatPredioService predioService;
    @Inject
    private ContRegistroContable registroContableService;
    private String sql;
    private Map<String, Object> param;
    private HistoricoTramites bpmTramite;
    private ArrayList<Archivo> files = new ArrayList<>();
    public static String getRubrosLiquidacionTipoLiqCodRubro = "select e from FinaRenRubrosLiquidacion e where e.tipoLiquidacion.id = :tipo and e.codigoRubro = :rubro and e.estado = true";
    
    public ManagerService() {
        super(CtlgSalario.class);
        param = new HashMap<>();
        
    }
    
    public BigDecimal getSalarioAnio(Integer an) {
        BigInteger anio = BigInteger.valueOf(an.longValue());
        List<CtlgSalario> salario = em.createQuery("SELECT s FROM CtlgSalario s WHERE s.anio=:anio")
                .setParameter("anio", anio).getResultList();
        if (salario != null && !salario.isEmpty()) {
            if (salario.get(0).getValor() != null) {
                return salario.get(0).getValor();
            }
        }
        return BigDecimal.ZERO;
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<CatalogoItem> listaNegocioSeleccionados(String codigosAnidados) {
        
        List<CatalogoItem> catalogos = new ArrayList<>();
        if (codigosAnidados != null) {
            String[] codigosplicitados = codigosAnidados.split(":");
            
            for (int i = 0; i < codigosplicitados.length; i++) {
                
                Long id = Long.valueOf(codigosplicitados[i]);
                CatalogoItem cata = find(CatalogoItem.class, id);
                System.out.println("cata " + cata.toString());
                if (cata != null) {
                    catalogos.add(cata);
                }
            }
        }
        return catalogos;
    }
    
    public BigInteger adminValidos(String user) {
        List<Long> result = (List<Long>) em.createQuery("SELECT COUNT(c.usuarioValidador) FROM Cliente c WHERE c.usuarioValidador.usuario=:usuario")
                .setParameter("usuario", user).getResultList();
        
        if (!result.isEmpty()) {
            return BigInteger.valueOf(result.get(0));
        }
        
        return BigInteger.ZERO;
    }
    
    public FinaRenPago ultimoPago(FinaRenLiquidacion liquidacion) {
        FinaRenPago p;
        try {
            p = (FinaRenPago) find("SELECT p FROM FinaRenPago p WHERE p.liquidacion=:liquidacion AND p.estado=true ORDER BY p.numComprobante DESC",
                    new String[]{"liquidacion"}, new Object[]{liquidacion});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return p;
    }
    
    public boolean ultimaEspecie(FinaRenLiquidacion liquidacion) {
        if (Utils.isNotEmpty((List<?>) liquidacion.getRenDetLiquidacionCollection())) {
            for (FinaRenDetLiquidacion d : liquidacion.getRenDetLiquidacionCollection()) {
                if (d.getRecActasEspeciesDet() != null) {
                    RecActasEspeciesDet acta = getActaDetById(d.getRecActasEspeciesDet());
                    if (!acta.getUltimoVendido().equals(d.getHasta().longValue())) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    public RecActasEspeciesDet getActaDetById(Long idActasEspeciesDet) {
        return find(RecActasEspeciesDet.class, idActasEspeciesDet);
    }
    
    public BigDecimal sueldoBasico(int anio) {
        List<CtlgSalario> sueldo = (List<CtlgSalario>) em.createQuery("SELECT c FROM CtlgSalario c where c.anio=:anio");
        if (sueldo.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return sueldo.get(0).getValor();
    }
    
    public FinaRenPago reversarPago(FinaRenPago pago) {
        try {
            BigDecimal valorRecaudadoEmision = pago.getValor().add(pago.getDescuento()).subtract(pago.getInteres()).subtract(pago.getRecargo());
            pago.getLiquidacion().setSaldo(pago.getLiquidacion().getSaldo().add(valorRecaudadoEmision));
            for (FinaRenDetLiquidacion l : pago.getLiquidacion().getRenDetLiquidacionCollection()) {
                for (FinaRenPagoRubro rp : pago.getRenPagoRubros()) {
                    if (l.getRubro().equals(rp.getRubro().getId())) {
                        l.setValorRecaudado(l.getValorRecaudado().subtract(rp.getValor()));
                        update(l);
                        //REVERSO DE MEJORAS VERIFICAR
                        if (l.getRubro().getId() == 7L && l.getMejDetRubroMejorasCollection() != null && !l.getMejDetRubroMejorasCollection().isEmpty()) {
                            for (DetRubroMejoras rm : l.getMejDetRubroMejorasCollection()) {
                                for (PagoRubroMejora prm : rp.getMejPagoRubroMejoras()) {
                                    if (rm.getUbicacionObra().getId().equals(prm.getUbicacionObra().getId())) {
                                        rm.setSaldo(rm.getSaldo().add(prm.getValor()));
                                        update(rm);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
            pago.getLiquidacion().setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
            update(pago.getLiquidacion());
            pago.setEstado(Boolean.FALSE);
            pago.setFechaAnulacion(new Date());
            pago = (FinaRenPago) save(pago);
            //CONSULTAR TODOS LOS PAGOS ACTIVOS DE LA LIQUIDACION DEL PAGO ANULADO
            List<FinaRenPago> pagosAct = finaRenPagoService.obtenerPagos(pago.getLiquidacion(), true);
            //SI NO TE DEVUELDE NADA SE APLICA LA INACTIVACION DE RENLIQUIDACION (DE LAS PERMITIDAS) - INACTIVAR LIQUIDACION ESTADo 3
            if (Utils.isEmpty(pagosAct) && pago.getLiquidacion().getTipoLiquidacion().getPermiteAnulacion()) {
                if (revertirActaEspecies(pago.getLiquidacion().getRenDetLiquidacionCollection())) {
                    pago.getLiquidacion().setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
                    save(pago.getLiquidacion());
                }
            }
            //SI ES TIPO ESPECIE O CARPETAS ECT VERIFICAR SI ES LA ULTIMA VENDIDA Y REVERSAR EL INVENTARIO,
            //CASO CONTRARIO SE PASA TITULOS DE CREDITOS
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return pago;
    }
    
    private Boolean revertirActaEspecies(List<FinaRenDetLiquidacion> detalle) {
        Boolean actasReversadas = true;
        List<RecActasEspeciesDet> actas = new ArrayList<>();
        for (FinaRenDetLiquidacion d : detalle) {
            if (d.getRecActasEspeciesDet() != null) {
                RecActasEspeciesDet acta = getActaDetById(d.getRecActasEspeciesDet());
                if (acta.getUltimoVendido().equals(d.getHasta().longValue())) {
                    acta.setDisponibles(acta.getDisponibles() + d.getCantidad());
                    acta.setUltimoVendido(d.getDesde().subtract(BigInteger.ONE).longValue());
                    actas.add(acta);
                } else {
                    actasReversadas = false;
                    break;
                }
            }
        }
        if (actasReversadas && actas.size() > 0) {
            saveList(actas);
            
        }
        return actasReversadas;
    }
    
    public boolean saveList(List entities) {
        try {
            //int x = 0;

            for (Object entitie : entities) {
                save(entitie);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    public boolean verificacionSalrios(BigInteger anio, BigDecimal valor) {
        List<CtlgSalario> result = (List<CtlgSalario>) em.createQuery("SELECT c FROM CtlgSalario c where c.anio=:anio and c.valor=:valor")
                .setParameter("anio", anio).setParameter("valor", valor).getResultList();
        
        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public CtlgSalario grabraSalario(CtlgSalario salario) {
        CtlgSalario i;
        try {
            i = (CtlgSalario) save(salario);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return i;
    }
    
    public List<AvalBandaImpositiva> getBandaImpositivaActivas(String estado) {
        
        return (List<AvalBandaImpositiva>) em.createQuery("SELECT cp FROM AvalBandaImpositiva cp WHERE cp.estado = :estado ORDER BY cp.urbano, cp.desdeUs,cp.hastaUs")
                .setParameter("estado", estado).getResultList();
    }
    
    public List<AvalBandaImpositiva> getBandaImpositivaTodas() {
        
        return (List<AvalBandaImpositiva>) em.createQuery("SELECT cp FROM AvalBandaImpositiva cp ORDER BY cp.urbano, cp.desdeUs,cp.hastaUs")
                .getResultList();
    }
    
    public List<FinaRenRubrosLiquidacion> getRubrosLiqui() {
        
        return null;
    }
    
    public boolean verificacionInteresFechaDesdeHasta(Date fecha, boolean desde) {
        List<FinaRenIntereses> result = new ArrayList<>();
        if (desde) {
            result = (List<FinaRenIntereses>) em.createQuery("SELECT f FROM FinaRenIntereses f where f.desde=:desde")
                    .setParameter("desde", fecha).getResultList();
        } else {
            result = (List<FinaRenIntereses>) em.createQuery("SELECT f FROM FinaRenIntereses f where f.hasta=:hasta")
                    .setParameter("hasta", fecha).getResultList();
        }
        
        if (!result.isEmpty()) {
            return true;
        }
        return false;
    }
    
    public FinaRenIntereses grabarInteres(FinaRenIntereses interes) {
        FinaRenIntereses i;
        try {
            i = (FinaRenIntereses) save(interes);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return i;
    }
    
    public Long existeRenEntidadBancaria(String descripcion) {
        return (Long) em.createQuery("SELECT r.id FROM FinaRenEntidadBancaria r where UPPER(r.descripcion) = :descripcion")
                .setParameter("descripcion", descripcion).getResultStream().findFirst().orElse(null);
    }
    
    public FinaRenEntidadBancaria guardarBanco(FinaRenEntidadBancaria banco) {
        FinaRenEntidadBancaria i;
        try {
            if (banco.getId() == null) {
                i = (FinaRenEntidadBancaria) save(banco);
            } else {
                update(banco);
                i = banco;
            }
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return i;
    }
    
    public boolean update(Object o) {
        boolean flag;
        Object ob = null;
        try {
            em.merge(o);
            flag = true;
        } catch (Exception e) {
            flag = false;
            System.err.println(e);
        }
        return flag;
    }
    
    public Documentos documentoGestionTribtaria(String name, Long identificador) {
//        System.out.println("name: " + name + "--d---- identificador: " + identificador);
        List<Documentos> result = (List<Documentos>) em.createQuery("SELECT d FROM Documentos d where d.claseNombre=:name and d.identificador=:id and estado=true")
                .setParameter("name", name).setParameter("id", identificador).getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
    
    public List<Documentos> documentoGestionTribtariaActivos(String name, Long identificador) {
        System.out.println("name: " + name + "------ identificador: " + identificador);
        List<Documentos> result = (List<Documentos>) em.createQuery("SELECT d FROM Documentos d where d.claseNombre=:name and d.identificador=:id and estado=true")
                .setParameter("name", name).setParameter("id", identificador).getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }
    
    public List<Documentos> listaDocumentosGeneral(String name, Long identificador) {
        return (List<Documentos>) em.createQuery("SELECT d FROM Documentos d where d.claseNombre=:name and d.identificador=:id")
                .setParameter("name", name).setParameter("id", identificador).getResultList();
    }
    
    public Object updateEntity(Object o) {
        
        Object ob = null;
        try {
            
            ob = em.merge(o);
        } catch (Exception e) {
            ob = null;
            System.err.println(e);
        }
        return ob;
    }
    
    public <T> T find(Class<T> entity, Object id) {
        T ob = null;
        try {
            ob = em.find(entity, id);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }
    
    public boolean saveTypeLiquidationByUsers(FinaRenTipoLiquidacion tipoLiquidacion, List<Usuarios> aclUsers) {
        boolean isSave = false;
        try {
            RenTipoLiquidacionDepartamento renTipoLiquidacionDepartamento = null;
            if (aclUsers != null && !aclUsers.isEmpty()) {
                for (Usuarios aclUser : aclUsers) {
                    renTipoLiquidacionDepartamento = new RenTipoLiquidacionDepartamento();
                    renTipoLiquidacionDepartamento.setEstado(Boolean.TRUE);
                    renTipoLiquidacionDepartamento.setTipoLiquidacion(tipoLiquidacion);
                    renTipoLiquidacionDepartamento.setFecha(new Date());
                    renTipoLiquidacionDepartamento.setUsuario(aclUser);
                    renTipoLiquidacionDepartamento.setUsuarioIngreso(tipoLiquidacion.getUsuarioIngreso());
                    save(renTipoLiquidacionDepartamento);
                    isSave = true;
                }
            } else {
                isSave = true;
            }
            
        } catch (Exception e) {
            System.err.println(e);
            isSave = false;
        }
        return isSave;
    }
    
    public boolean eliminarTitulo(FinaRenTipoLiquidacion tipoLiq) {
        try {
            List<FinaRenRubrosLiquidacion> renRubrosLiquidacionCollection = tipoLiq.getRenRubrosLiquidacionCollection();
            tipoLiq.setRenRubrosLiquidacionCollection(null);
            if (renRubrosLiquidacionCollection != null) {
                if (deleteList(renRubrosLiquidacionCollection)) {
                    System.out.println("Elimnado >>>> ");
                }
            }
            List<RenSecuenciaNumLiquidacion> renNumLiquidacionCollection = tipoLiq.getRenNumLiquidacionCollection();
            tipoLiq.setRenNumLiquidacionCollection(null);
            if (renNumLiquidacionCollection != null) {
                if (deleteList((List) renNumLiquidacionCollection));
            }
            return delete(tipoLiq);
        } catch (Exception e) {
            System.err.println(e);
        }
        return false;
    }
    
    public boolean deleteList(List entities) {
        
        try {
            for (Object entitie : entities) {
                em.remove(entitie);
                
            }
            
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return true;
    }
    
    public boolean delete(Object entitie) {
        try {
            em.remove(entitie);
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
        return true;
    }
    
    public List<Usuarios> getUser(UnidadAdministrativa geDepartamento) {
        List<Usuarios> users = null;
        
        System.out.println("unidad -> " + geDepartamento.getId());
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("unidadAdministrativa", geDepartamento);
            paramt.put("estado", true);
            List<Rol> findObjectByParameterList = findObjectByParameterList(Rol.class, paramt);
            if (!findObjectByParameterList.isEmpty()) {
                users = new ArrayList<>();
                for (Rol ar : findObjectByParameterList) {
                    if (!ar.getUsuarioRolesList().isEmpty()) {
                        for (UsuarioRol au : ar.getUsuarioRolesList()) {
                            
                            if (!users.contains(au.getUsuario())) {
                                users.add(au.getUsuario());
                                
                            }
                        }
                    }
                }
            }
            for (Usuarios aclUser : users) {
                if (aclUser.getEnte() != null) {
                    Map<String, Object> pm = new HashMap<>();
                    pm.put("ente", aclUser.getEnte());
                    //aclUser.getEnte().setEnteCorreoCollection(findObjectByParameterList(EnteCorreo.class, pm));
                }
            }
            
        } catch (Exception e) {
            System.err.println(e);
        }
        return users;
    }
    
    public <T> List<T> findObjectByParameterList(Class entity, Map<String, Object> paramt) {
        List<T> ob = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
            Root from = cq.from(entity);
            cq.select(from);
            Predicate[] restrictions = new Predicate[paramt.size()];
            int c = 0;
            for (Map.Entry<String, Object> entry : paramt.entrySet()) {
                if (entry.getValue().toString().contains("%")) {
                    restrictions[c] = builder.like(from.get(entry.getKey()), entry.getValue().toString().trim());
                } else {
                    restrictions[c] = builder.equal(from.get(entry.getKey()), entry.getValue());
                }
                c++;
            }
            cq.where(restrictions);
            ob = getEntityManager().createQuery(cq).getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (HibernateException e) {
            System.err.println(e);
        }
        return ob;
    }
    
    public List<FinaRenRubrosLiquidacion> getRubrosByTipoLiquidacionCodRubroASC(Long res) {
        return (List<FinaRenRubrosLiquidacion>) em.createQuery("SELECT r FROM FinaRenRubrosLiquidacion r WHERE r.tipoLiquidacion.id = :tipoLiq and r.estado=true ORDER BY r.codigoRubro ASC")
                .setParameter("tipoLiq", res).getResultList();
    }
    
    public <T> T findByParameter(Class entity, Map<String, Object> paramt) {
        T ob = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            if (paramt != null) {
                Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                if (predicates != null) {
                    query.where(predicates);
                }
            }
            Query q = getEntityManager().createQuery(query);
            try {
                q.setMaxResults(1);
                ob = (T) q.getSingleResult();
            } catch (NoResultException ex) {
                System.out.println("NoResultException " + ex.getMessage());
                return null;
            }
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            e.printStackTrace();
        }
        return ob;
    }
    
    public List findAllQuery(String query, Map<String, Object> paramt) {
        Session sess;
        Query q;
        List l = null;
        try {
            
            q = getEntityManager().createQuery(query);
            if (paramt != null) {
                paramt.entrySet().forEach((entrySet) -> {
                    q.setParameter(entrySet.getKey(), entrySet.getValue());
                });
            }
            l = (List) q.getResultList();
            l.size();
            Hibernate.initialize(l);
        } catch (HibernateException e) {
            System.out.println("Error en findAllQuery> " + e.getMessage());
            e.printStackTrace();
        }
        return l;
    }
    
    public List findAll(Class entity, Map<String, Object> paramt) {
        
        List ob = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            CriteriaQuery query = builder.createQuery(entity);
            Root entityRoot = query.from(entity);
            query.select(entityRoot);
            if (paramt != null) {
                Predicate[] predicates = this.getPredicates(entityRoot, builder, paramt);
                if (predicates != null) {
                    query.where(predicates);
                }
            }
            Query q = getEntityManager().createQuery(query);
            ob = q.getResultList();
            if (ob != null) {
                ob.size();
                Hibernate.initialize(ob);
            }
        } catch (HibernateException e) {
            LOG.log(Level.SEVERE, "No vale el find All", e);
        }
        return ob;
    }
    
    private Predicate[] getPredicates(Root root, CriteriaBuilder builder, Map<String, Object> filters) {
        if (filters == null) {
            return null;
        }
        List<Predicate> predicates = new ArrayList<>();
        filters.entrySet().forEach((filEntry) -> {
            if (filEntry.getValue() instanceof Date) {
                Date date = (Date) filEntry.getValue();
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                date = cal.getTime();
                cal.add(Calendar.DAY_OF_MONTH, 1);
                cal.add(Calendar.SECOND, -1);
                predicates.add(builder.between(root.get(filEntry.getKey()), date, cal.getTime()));
            } else {
                String key = filEntry.getKey();
                Join join = null;
                if (filEntry.getKey().contains(".")) {
                    String[] split = key.split("\\.");
                    int index = 0;
                    try {
                        for (String sp : split) {
                            if (index == 0) { // PRIMER RECORRIDO SETEA CRITERIA PRINCIPAL
                                join = root.join(sp);
                            } else if (index < (split.length - 1)) {
                                join = join.join(sp);
                            } else {
                                if (filEntry.getValue().equals(Date.class)) {
                                    Date date = (Date) filEntry.getValue();
                                    Calendar cal = Calendar.getInstance();
                                    cal.setTime(date);
                                    cal.add(Calendar.DAY_OF_MONTH, 1);
                                    cal.add(Calendar.SECOND, -1);
                                    predicates.add(builder.between(join.get(sp), date, cal.getTime()));
                                } else {
//                                    if (NumberUtils.isNumber(filEntry.getValue().toString())) {
//                                        predicates.add(builder.equal(join.get(sp), ReflexionEntity.instanceConsString(filEntry.getValue().getClass(), filEntry.getValue().toString().trim()).toString()));
//                                    } else
                                    if (filEntry.getValue() instanceof Object[]) {
                                        predicates.add(join.get(sp).in(((Object[]) filEntry.getValue())));
                                    } else {
                                        predicates.add(builder.equal(join.get(sp), filEntry.getValue()));
                                    }
                                }
                            }
                            index++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    predicates.add(builder.equal(root.get(filEntry.getKey()), filEntry.getValue()));
                }
            }
        });
        return toArrayPredicates(predicates);
    }
    
    private Predicate[] toArrayPredicates(List<Predicate> predicates) {
        if (Utils.isNotEmpty(predicates)) {
            Predicate[] result = new Predicate[predicates.size()];
            return predicates.toArray(result);
        }
        return null;
    }
    
    public Boolean guardarRubrosPorTipoLiquidacion(List<FinaRenRubrosLiquidacion> list) {
        Boolean b;
        try {
            b = true;
            for (FinaRenRubrosLiquidacion varTemp : list) {
                save(varTemp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            b = false;
        }
        return b;
    }
    
    public Boolean eliminarRubro(FinaRenRubrosLiquidacion rubro, FinaRenTipoLiquidacion tipoLiquidacion) {
        try {
            rubro.setTipoLiquidacion(null);
            rubro.setCodigoRubro(null);
            return update(rubro);
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public FinaRenRubrosLiquidacion guardarRubroNuevo(FinaRenRubrosLiquidacion rubro, FinaRenTipoLiquidacion tipoLiquidacion) {
        try {
            if (tipoLiquidacion != null) {
                Long lastCodRubro = getLastCodigoRubro(tipoLiquidacion.getId());
                if (lastCodRubro == null) {
                    lastCodRubro = 1L;
                } else {
                    lastCodRubro = lastCodRubro + 1L;
                }
                rubro.setCodigoRubro(lastCodRubro);
            }
            rubro.setTipoLiquidacion(tipoLiquidacion);
            rubro.setEstado(Boolean.TRUE);
            if (rubro.getId() != null) {
                update(rubro);
            } else {
                save(rubro);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return rubro;
    }
    
    public Long getLastCodigoRubro(Long res) {
        List<Long> datos = (List<Long>) em.createQuery("SELECT COALESCE(Max(r.codigoRubro),0) FROM FinaRenRubrosLiquidacion r WHERE r.tipoLiquidacion.id = :tipoLiq AND r.estado = true AND r.codigoRubro IS NOT NULL")
                .setParameter("tipoLiq", res).getResultList();
        if (datos.isEmpty()) {
            return 0L;
        }
        
        return datos.get(0);
    }
    
    public List<FinaRenTipoLiquidacion> getRenTipoLiquidacionList() {
        return (List<FinaRenTipoLiquidacion>) em.createQuery("SELECT  r FROM FinaRenTipoLiquidacion r where r.nombreTransaccion IN (select DISTINCT(m.nombreTransaccion)from "
                + "FinaRenTipoLiquidacion m  ) AND r.estado = TRUE ORDER BY r.transaccionPadre, r.nombreTransaccion ASC")
                .getResultList();
    }
    
    public Boolean editarRenTipoLiquidacion(FinaRenTipoLiquidacion tipoLiq) {
        try {
            if (tipoLiq.getCodigoTituloReporte() == null && tipoLiq.getNombreTitulo() != null && tipoLiq.getPrefijo() != null) {
                tipoLiq.setCodigoTituloReporte(this.generarCodTitRep());
            }
            return update(tipoLiq);
            
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }
    
    public Long generarCodTitRep() {
        
        Long value = (Long) em.createQuery("SELECT r.codigoTituloReporte FROM FinaRenTipoLiquidacion r WHERE r.codigoTituloReporte IS NOT NULL ORDER BY r.codigoTituloReporte DESC")
                .getResultStream().findFirst().orElse(null);
        if (value != null) {
            return value + 1;
        } else {
            return null;
        }
    }
    
    public List<FinaRenTipoLiquidacion> getRenTransaccionesPadres(Long idPadre) {
        
        return (List<FinaRenTipoLiquidacion>) em.createQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre = :idPadre AND r.estado = true ORDER BY r.id, r.transaccionPadre, r.nombreTransaccion ASC")
                .setParameter("idPadre", idPadre).getResultList();
    }
    
    public List<CatCiudadela> ordenamientoNombreCiudadela() {
        try {
            return (List<CatCiudadela>) em.createQuery("SELECT r FROM CatCiudadela r ORDER BY r.nombre ASC").getResultList();
        } catch (Exception ex) {
            System.out.println("Error ordenamientoNombreCiudadela: " + ex.getMessage());
            return null;
        }
    }
    
    public FinaRenLiquidacion getLiqInicialByConvenioAndTipoLiquidacion(Long convenio, Long tipo) {
        
        List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) em.createQuery("SELECT l FROM FinaRenLiquidacion l WHERE l.convenioPago.id = :convenio AND l.tipoLiquidacion.id = :tipo AND l.estadoLiquidacion.id IN (1,2)")
                .setParameter("convenio", convenio).setParameter("tipo", tipo).getResultList();
        
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }
    
    public BigDecimal interesCalculado(FinaRenLiquidacion emision, Date hasta) {
        BigDecimal interes = this.interesAcumulado(emision, hasta);
        BigDecimal div = new BigDecimal("100");
        
        BigDecimal interesValor = BigDecimal.ZERO;
        if (interesValor != null && emision.getTotalPago() != null) {
            interesValor = interes.multiply(emision.getTotalPago()).divide(div).setScale(2, RoundingMode.HALF_UP);
        }
        
        return interesValor;
    }
    
    public BigDecimal interesAcumulado(FinaRenLiquidacion emision, Date hasta) {
        BigDecimal interes = BigDecimal.ZERO;
        RenParametrosInteresMulta parametrosInteresMulta;
        parametrosInteresMulta = this.getParametrosInteresMultaByLiquidacion(emision.getTipoLiquidacion().getId(), "I");
        
        int anioActual = Utils.getAnio(new Date());
        int mesActual = Utils.getMes(new Date());
        int anioLiq = emision.getAnio();
        Calendar calendar = Calendar.getInstance();
        Date fechaInicio = calendar.getTime();
        Date fechafin = new Date();
        
        if (hasta != null) {
            fechafin = hasta;
        }
        boolean calcular = true;
        
        if (parametrosInteresMulta != null) {
            
            if (anioActual == (anioLiq + 1)) {
                if (mesActual >= parametrosInteresMulta.getMes()) {
                    
                    calendar.set(anioActual, (parametrosInteresMulta.getMes() - 1), parametrosInteresMulta.getDia(), 0, 0, 0);
                    fechaInicio = calendar.getTime();
                } else {
                    calcular = false;
                }
            } else {
                if (emision.getAnio() + 1 > anioActual) {
                    interes = BigDecimal.ZERO;
                    calcular = false;
                } else {
                    calendar.set((emision.getAnio() + 1), (parametrosInteresMulta.getMes() - 1), parametrosInteresMulta.getDia(), 0, 0, 0);
                    fechaInicio = calendar.getTime();
                }
            }
        } else {
            calcular = false;
        }
        
        if (calcular) {
            Object[] valores = {
                new SimpleDateFormat("dd-MM-YYYY").format(fechafin),
                new SimpleDateFormat("dd-MM-YYYY").format(fechafin),
                new SimpleDateFormat("dd-MM-YYYY").format(fechafin),
                new SimpleDateFormat("dd-MM-YYYY").format(fechafin),
                new SimpleDateFormat("dd-MM-YYYY").format(fechaInicio),
                new SimpleDateFormat("dd-MM-YYYY").format(fechaInicio),
                new SimpleDateFormat("dd-MM-YYYY").format(fechaInicio),
                new SimpleDateFormat("dd-MM-YYYY").format(fechaInicio)
            };
            interes = (BigDecimal) this.getInteresAnualAcumulado(valores);
            
            if (interes == null) {
                interes = BigDecimal.ZERO;
            }
        }
        
        return interes;
    }
    
    public RenParametrosInteresMulta getParametrosInteresMultaByLiquidacion(Long tipoLiquidacion, String tipo) {
        
        List<RenParametrosInteresMulta> result = (List<RenParametrosInteresMulta>) em.createQuery("SELECT p FROM RenParametrosInteresMulta p WHERE p.tipoLiquidacion.id =:tipoLiquidacion AND p.tipo =:tipo")
                .setParameter("tipoLiquidacion", tipoLiquidacion).setParameter("tipo", tipo).getResultList();
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
        
    }
    
    public BigDecimal getInteresAnualAcumulado(Object[] valores) {
        return (BigDecimal) getNativeQuery("SELECT sum(r.porcentaje) FROM asgard.fina_ren_intereses r WHERE \n"
                + "( r.hasta <= TO_DATE(?,'DD-MM-YYYY') OR (r.hasta >=  TO_DATE(?,'DD-MM-YYYY') \n"
                + "AND DATE_PART('month', r.hasta) = DATE_PART('month', TO_DATE(?,'DD-MM-YYYY')) \n"
                + "AND DATE_PART('YEAR', r.hasta) <= DATE_PART('year', TO_DATE(?,'DD-MM-YYYY')))) \n"
                + "AND r.id IN(SELECT id FROM asgard.fina_ren_intereses i WHERE i.desde >=  TO_DATE(?,'DD-MM-YYYY') \n"
                + "OR (i.desde <=  TO_DATE(?,'DD-MM-YYYY') AND DATE_PART('month', i.desde) = DATE_PART('month', TO_DATE(?,'DD-MM-YYYY')) AND DATE_PART('YEAR', i.desde) >= DATE_PART('year', TO_DATE(?,'DD-MM-YYYY'))));", new String[]{"desde", "hasta"}, valores);
        
    }
    
    public Object findUnique(String query, String[] par, Object[] val) {
        Object ob = null;
        try {
            ob = find(query, par, val);
            Hibernate.initialize(ob);
        } catch (Exception e) {
            System.err.println(e);
        }
        return ob;
    }
    
    public Object getNativeQuery(String query, String[] params, Object[] val) {
        Object ob = null;
        try {
            javax.persistence.Query q = getEntityManager().createNativeQuery(query);
            for (int i = 0; i < val.length; i++) {
                q.setParameter(i + 1, val[i]);
            }
            ob = (Object) q.getSingleResult();
            Hibernate.initialize(ob);
            
        } catch (Exception e) {
            System.err.println(e);
        }
        return ob;
    }
    
    public List<FinaRenLiquidacion> getRenLiquidacionesByConvenioPago(Long convenio, Long estadoLiquidacion) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT li FROM FinaRenLiquidacion li WHERE li.convenioPago.id = :convenio AND li.estadoLiquidacion.id <> :estadoLiquidacion")
                .setParameter("convenio", convenio).setParameter("estadoLiquidacion", estadoLiquidacion).getResultList();
    }
    
    public FinaRenTipoLiquidacion getRenTipoLiquidacionByCodigoReporte(Long codigo) {
        List<FinaRenTipoLiquidacion> result = (List<FinaRenTipoLiquidacion>) em.createQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.estado = true AND r.codigoTituloReporte = :tituloReporte ORDER BY r.nombreTransaccion ASC")
                .setParameter("tituloReporte", codigo).getResultList();
        
        if (!result.isEmpty()) {
            return result.get(0);
        }
        
        return null;
    }
    
    public BigInteger getMaxSecuenciaTipoLiquidacion(Integer anio, Long idTipoLiquidacion) {
        
        RenSecuenciaNumLiquidacion secuenciaTipoLiquidacion = null;

//
        if (anio != null && idTipoLiquidacion != null) {
//
            BigInteger id = (BigInteger) em.createNativeQuery("select id from sgm.secuencia_num_liquidacion s where s.tipo_liquidacion=?1")
                    .setParameter(1, BigInteger.valueOf(idTipoLiquidacion)).getResultStream().findFirst().orElse(null);
            
            if (id == null) {
                id = BigInteger.ZERO;
            }
            secuenciaTipoLiquidacion = find(RenSecuenciaNumLiquidacion.class, id.longValue());
            
            if (secuenciaTipoLiquidacion != null) {
                secuenciaTipoLiquidacion.setSecuencia(secuenciaTipoLiquidacion.getSecuencia().add(BigInteger.ONE));
                update(secuenciaTipoLiquidacion);
            } else {
                secuenciaTipoLiquidacion = new RenSecuenciaNumLiquidacion();
                //secuenciaTipoLiquidacion.setAnio(BigInteger.valueOf(anio));
                secuenciaTipoLiquidacion.setTipoLiquidacion(idTipoLiquidacion);
                secuenciaTipoLiquidacion.setSecuencia(BigInteger.ONE);
                save(secuenciaTipoLiquidacion);
            }
            
        } else {
            secuenciaTipoLiquidacion = null;
        }
        
        return secuenciaTipoLiquidacion != null ? secuenciaTipoLiquidacion.getSecuencia() : null;
    }
    
    public BigInteger getRenLiquidacionComprobanteCuentaPagoInicial(Long convenio) {
        return (BigInteger) em.createQuery("SELECT l.numComprobante FROM FinaRenLiquidacion  l WHERE l.convenioPago.id  = :convenio  AND l.tipoLiquidacion.id = 256")
                .setParameter("convenio", convenio).getResultStream().findFirst().orElse(BigInteger.ZERO);
    }
    
    public List<FinaRenLiquidacion> getLiqInicialByConvenioAndTipoLiquidacionJC(Long convenio) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT l FROM FinaRenLiquidacion l WHERE l.convenioPago.id = :convenio AND l.tipoLiquidacion.id NOT IN( 256, 261 )")
                .setParameter("convenio", convenio).getResultList();
    }
    
    public List<Usuarios> allUserFindById(String codigo) {
        try {
            return (List<Usuarios>) em.createQuery("Select c.usuario from UsuarioRol c where c.rol.nombre = :codigo and c.usuario.funcionario is not null")
                    .setParameter("codigo", codigo).getResultList();
        } catch (Exception ex) {
            System.out.println("Error en allUserFindById: " + ex.getMessage());
            return null;
        }
    }
    
    public List<FinaRenLiquidacion> getPagoAnualByPredioPendientesCoactiva(Long idPredio, Long idEstado) {
        Calendar cal = Calendar.getInstance();
        try {
            if (idEstado == null) {
                return (List<FinaRenLiquidacion>) em.createQuery("select r from FinaRenLiquidacion r where r.estadoCoactiva = 1 and (r.estadoLiquidacion = 1 or r.estadoLiquidacion = 2) "
                        + "and r.anio < :anio and r.predio = :idPredio order by r.anio asc").setParameter("estado", idEstado).setParameter("anio", cal.get(Calendar.YEAR)).setParameter("idPredio", idPredio);

                // return findAll(Querys.getLiquidacionesActivasByPredio, new String[]{"anio", "idPredio"}, new Object[]{cal.get(Calendar.YEAR), idPredio});
            } else {
                return (List<FinaRenLiquidacion>) em.createQuery("select r from FinaRenLiquidacion r where r.estadoCoactiva = 1 and r.tipoLiquidacion.id IN (13,7,325) and r.estadoLiquidacion.id = :estado and r.anio < :anio "
                        + "and r.predio.id = :idPredio order by r.anio asc").setParameter("estado", idEstado).setParameter("anio", cal.get(Calendar.YEAR)).setParameter("idPredio", idPredio);
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    public Boolean aplicaRemision(FinaRenLiquidacion liquidacion) {
        Boolean aplicaRemision = Boolean.FALSE;
        param = new HashMap<>();
        param.put("liquidacion.id", liquidacion.getId());
        param.put("estado", true);
        param.put("exoneracion.estado.id", 1);
        
        FnExoneracionLiquidaciones fnRemisionLiquidacion = (FnExoneracionLiquidaciones) findByParameter(FnExoneracionLiquidaciones.class, param);
        
        if (fnRemisionLiquidacion != null) {
            if (new Date().before(fnRemisionLiquidacion.getExoneracion().getFechaPagoMaximo())) {
                aplicaRemision = Boolean.TRUE;
            }
        } else {
            aplicaRemision = Boolean.FALSE;
        }
        return aplicaRemision;
    }
    
    public FinaRenLiquidacion grabarLiquidacion(FinaRenLiquidacion liquidacion) {
        Integer anio = null;
        List<FinaRenDetLiquidacion> detLiquidacion = new ArrayList<>();
        List<FinaRenDetLiquidacion> detLiquidacionPersist = new ArrayList<>();
        FinaRenDetLiquidacion detalle;
        if (liquidacion.getComprador() != null) {
            liquidacion.setNombreComprador(liquidacion.getComprador().getNombreCompleto());
        }
        liquidacion.setIpUserSession(session.getIpClient());
        liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
        liquidacion.setNumLiquidacion(getMaxSecuenciaTipoLiquidacion(liquidacion.getAnio(), liquidacion.getTipoLiquidacion().getId()));
        if (liquidacion.getTipoLiquidacion() != null) {
            if (liquidacion.getTipoLiquidacion().getPrefijo() != null) {
                liquidacion.setIdLiquidacion(liquidacion.getTipoLiquidacion().getPrefijo() + "-" + Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6));
            }
        } else {
            liquidacion.setIdLiquidacion("00001");
        }
        
        liquidacion.setNumComprobante(BigInteger.ZERO);
        liquidacion.setEstadoCoactiva(1);
        for (FinaRenRubrosLiquidacion r : liquidacion.getTipoLiquidacion().getRenRubrosLiquidacionCollection()) {
            detalle = new FinaRenDetLiquidacion();
            detalle.setRubro(r);
            detalle.setValor(r.getValor());
            if (r.getCantidad() != null) {
                detalle.setCantidad(r.getCantidad());
            }
            ///PERMISOS DE AMBIENTE -ARRIENDO CEMENTERIO
            if (r.getTipoLiquidacion().getTransaccionPadre() != null) {
                if (r.getTipoLiquidacion().getTransaccionPadre().equals(16L) || r.getTipoLiquidacion().getTransaccionPadre().equals(271L)) {
                    if (r.getAnio() != null) {
                        anio = r.getAnio();
                    } else {
                        anio = Utils.getAnio(new Date());
                    }
                }
            }
            
            if (r.getActa() != null) {
                detalle.setRecActasEspeciesDet(r.getActa().getId());
                detalle.setDesde(BigInteger.valueOf(r.getActa().getDesdeTemp()));
                detalle.setHasta(BigInteger.valueOf(r.getActa().getHastaTemp()));
                r.getActa().setUltimoVendido(r.getActa().getHastaTemp());
                if (r.getActa().getDisponibles() == 0) {
                    r.getActa().setEstado("C");
                }
                update(r.getActa());
            }
            detLiquidacion.add(detalle);
        }
        if (anio != null) {
            liquidacion.setAnio(anio);
        }
        
        liquidacion = liquidacionServices.create(liquidacion);
        for (FinaRenDetLiquidacion d : detLiquidacion) {
            d.setLiquidacion(liquidacion);
            
            d = (FinaRenDetLiquidacion) save(d);
            detLiquidacionPersist.add(d);
        }
        liquidacion.setRenDetLiquidacionCollection(detLiquidacionPersist);
        return liquidacion;
    }
    
    public FinaRenLiquidacion grabarLiquidacion(FinaRenLiquidacion liquidacion, FnSolicitudExoneracion solicitud) {
        Integer anio = null;
        List<FinaRenDetLiquidacion> detLiquidacion = new ArrayList<>();
        List<FinaRenDetLiquidacion> detLiquidacionPersist = new ArrayList<>();
        FinaRenDetLiquidacion detalle;
        if (liquidacion.getComprador() != null) {
            if (liquidacion.getComprador().getId() == null) {
                if (liquidacion.getComprador().getIdentificacion() != null) {
                    liquidacion.getComprador().setTipoProv(liquidacion.getComprador().getIdentificacion().length() <= 10 ? new CatalogoItem(10L) : new CatalogoItem(11L));
                    if (liquidacion.getComprador().getIdentificacion().length() <= 10) {
                        //liquidacion.getComprador().setTipoDocumento(new CtlgItem(2780L));
                    } else {
                        //liquidacion.getComprador().setTipoDocumento(new CtlgItem(2781L));
                    }
                }
                Map<String, Object> pm = new HashMap<>();
                pm.put("identificacion", liquidacion.getComprador().getIdentificacion());
                Cliente ve = findByParameter(Cliente.class, pm);
                if (ve != null) {
                    liquidacion.setComprador(ve);
                } else {
                    liquidacion.setComprador((Cliente) save(liquidacion.getComprador()));
                }
                liquidacion.setNombreComprador(liquidacion.getComprador().getNombreCompleto());
            }
        }
        liquidacion.setIpUserSession(session.getIpClient());
        liquidacion.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
        liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
        liquidacion.setNumLiquidacion(getMaxSecuenciaTipoLiquidacion(liquidacion.getAnio(), liquidacion.getTipoLiquidacion().getId()));
        if (liquidacion.getTipoLiquidacion() != null) {
            if (liquidacion.getTipoLiquidacion().getPrefijo() != null) {
                liquidacion.setIdLiquidacion(liquidacion.getTipoLiquidacion().getPrefijo() + "-" + Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6));
            }
        } else {
            liquidacion.setIdLiquidacion("00001");
        }
        
        liquidacion.setNumComprobante(BigInteger.ZERO);
        liquidacion.setEstadoCoactiva(1);
        
        if (anio != null) {
            liquidacion.setAnio(anio);
        }
        
        liquidacion = liquidacionServices.create(liquidacion);
        
        for (FinaRenRubrosLiquidacion item : liquidacion.getTipoLiquidacion().getRenRubrosLiquidacionCollection()) {
            detalle = new FinaRenDetLiquidacion();
            detalle.setCantidad(item.getCantidad());
            detalle.setRubro(item);
            detalle.setValorSinDescuento(item.getValor());
            detalle.setValor(item.getValorTotal());
            detLiquidacion.add(detalle);
        }
        
        solicitud = (FnSolicitudExoneracion) save(solicitud);
        
        for (FinaRenDetLiquidacion d : detLiquidacion) {
            d.setLiquidacion(liquidacion);
            d = (FinaRenDetLiquidacion) save(d);
            detLiquidacionPersist.add(d);
        }
        
        liquidacion.setRenDetLiquidacionCollection(detLiquidacionPersist);
        return liquidacion;
    }
    
    public BigDecimal generarMultas(FinaRenLiquidacion liquidacion, RenParametrosInteresMulta interesMulta) {
        BigDecimal multa;
        try {
            Date fecha = new Date();
            Integer mes = Utils.getDateValues("M", fecha);
            Integer anio = Utils.getDateValues("Y", fecha);
            Integer cantidadMeses;
            cantidadMeses = 12 * (anio - liquidacion.getAnio()) + (mes + 2 - interesMulta.getMes());// +2 : 1 por la funcion que retorna el mes y +1 por la multa corre desde el mes establecido
            multa = (liquidacion.getSaldo().multiply(interesMulta.getPorcentaje())).multiply(new BigDecimal(cantidadMeses + "")).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return multa;
    }
    
    public FinaRenActividadComercial actividadPOrLocal(Long id, Integer anio) {
        Integer anioActual = Utils.getAnio(new Date());
        List<BigInteger> listId = new ArrayList<>();
        
        if (anioActual.equals(anio)) {
            
            listId = (List<BigInteger>) em.createNativeQuery("select c.actividad from " + Utils.SCHEMA_SGM + ".ren_actividad_por_local c where c.local_comercial=?1")
                    .setParameter(1, BigInteger.valueOf(id)).getResultList();
            System.out.println("data 1 " + listId.size());
        } else {
            listId = (List<BigInteger>) em.createNativeQuery("select l.actividad from " + Utils.SCHEMA_ASGARD + ".hi_local_actividades l where l.local=?1 and l.anio=?2")
                    .setParameter(1, BigInteger.valueOf(id)).setParameter(2, anio).getResultList();
            System.out.println("data 2" + listId.size());
        }
        
        if (!listId.isEmpty()) {
            System.out.println("find(FinaRenActividadComercial.class, listId.get(0).longValue()) " + find(FinaRenActividadComercial.class, listId.get(0).longValue()).getValor());
            return find(FinaRenActividadComercial.class, listId.get(0).longValue());
        }
        return null;
    }
    
    public Long getNumComprobante() {
        Long comprobante = null;
        try {
            
            comprobante = new Long(this.getNativeQuery("select nextval('asgard.ren_pago_num_comprobante_seq')").toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return comprobante;
    }
    
    public Object getNativeQuery(String query) {
        Object ob = null;
        try {
            Query q = getEntityManager().createNativeQuery(query);
            ob = (Object) q.getSingleResult();
        } catch (HibernateException eh) {
            System.out.println("error al ejecutar la funcion + " + query + " - " + eh.getMessage());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }
    
    public FinaRenPago realizarPago(FinaRenLiquidacion liquidacion, FinaRenPago pago, Cajero cajero, Boolean isSac) {
        Long numComprobante;
        List<ConsolidacionBanco> listCB;
        // COMPROBANTE DEL SGM
        numComprobante = getNumComprobante();
        
        List<FinaRenPagoDetalle> detallePago;
        BigDecimal valorLiquidacion;
        FinaRenPagoRubro pagoRubro;
        BigDecimal valorRecaudacion;
        PagoRubroMejora pagoRubroMejora;
        BigDecimal valorRecaudacionMejora;
        Map<String, Object> parametros;
        try {
            detallePago = (List<FinaRenPagoDetalle>) pago.getRenPagoDetalles();
            pago.setFechaPago(new Date());
            pago.setEstado(true);
            // pago.setNumComprobante(numComprobante);
            pago.setLiquidacion(liquidacion);
            pago.setCajero(cajero);
            pago.setDescuento(liquidacion.getDescuento());
            pago.setRecargo(liquidacion.getRecargo());
            pago.setContribuyente(liquidacion.getComprador());
            pago.setNombreContribuyente(liquidacion.getNombreComprador());
            pago.setInteres(liquidacion.getInteres());
            pago.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
            pago.setIpUserSession(session.getIpClient());
            pago = (FinaRenPago) save(pago);
            //ACTUALIZACION DE TABLA CONSOLIDACIONBANCO
            for (FinaRenPagoDetalle det : detallePago) {
                det.setPago(pago);
                save(det);
                if (det.getTipoPago() == 5) {
                    if (liquidacion.getTipoLiquidacion().getId() == 13L && liquidacion.getPredio() != null) {
                        parametros = new HashMap<>();
                        CatPredioModel predio = buscarPredio(liquidacion.getPredio().getId());
                        parametros.put("numPredio", predio.getNumPredio());
                        parametros.put("anio", liquidacion.getAnio());
                        listCB = findByParameter(ConsolidacionBanco.class, parametros);
                        if (listCB != null && !listCB.isEmpty()) {
                            for (ConsolidacionBanco cb : listCB) {
                                cb.setEstado("P");
                                cb.setNumComprobante(new BigInteger(numComprobante + ""));
                                save(cb);
                            }
                        }
                    }
                }
            }
            liquidacion.setNumComprobante(new BigInteger(pago.getNumComprobante() + ""));
            liquidacion.setSaldo(liquidacion.getSaldo().subtract(pago.getValor().subtract(pago.getInteres()).subtract(pago.getRecargo()).add(pago.getDescuento())));
            if (liquidacion.getSaldo().compareTo(BigDecimal.ZERO) < 1) {
                liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(1L));
                liquidacion.setSaldo(BigDecimal.ZERO);
                /*Anyelo*/
                if (liquidacion.getEstadoCoactiva() != null && liquidacion.getEstadoCoactiva() == 2) {
                    liquidacion.setEstadoCoactiva(3);
                }
//                pago.setValor(pago.getValor().subtract(pago.getDescuento()));
            }
            //VALOR DE LIQUIDACION
            valorLiquidacion = pago.getValor().subtract(pago.getInteres()).subtract(pago.getRecargo()).add(pago.getDescuento());
            for (FinaRenDetLiquidacion rubro : liquidacion.getRenDetLiquidacionCollection()) {
                //RUBROS QUE ESTAN PENDIENTES DE RECAUDAR
                if (rubro != null) {
                    if (rubro.getValor() != null) {
                        if (!liquidacion.getTipoLiquidacion().getId().equals(13L)) {
                            rubro.setValorRecaudado(BigDecimal.ZERO.setScale(2));
                        }
                        if (rubro.getValor().compareTo(rubro.getValorRecaudado()) > 0) {
                            pagoRubro = new FinaRenPagoRubro();
                            BigDecimal valorRecaudar = rubro.getValor().subtract(rubro.getValorRecaudado());
                            if (valorLiquidacion.compareTo(valorRecaudar) >= 0) {//PAGO TOTAL DEL RUBRO / COMPLETA EL PAGO
                                //SE VERIFICA CUANTO SE VA A MENORAR DEL DINERO RECIBIDO
                                if (rubro.getValorRecaudado().compareTo(new BigDecimal("0.00")) <= 0) {
                                    valorRecaudacion = rubro.getValor();
                                } else {
                                    valorRecaudacion = rubro.getValor().subtract(rubro.getValorRecaudado());
                                }
                                rubro.setValorRecaudado(rubro.getValor());
                                
                            } else {//PAGO PARCIAL DEL RUBRO
                                rubro.setValorRecaudado(rubro.getValorRecaudado().add(valorLiquidacion));
                                valorRecaudacion = valorLiquidacion;
                            }
                            save(rubro);
                            //REGISTRO VALOR RECAUDADO POR RUBRO
                            pagoRubro.setPago(pago);
                            pagoRubro.setRubro(new FinaRenRubrosLiquidacion(rubro.getRubro().getId()));
                            pagoRubro.setValor(valorRecaudacion);
                            pagoRubro = (FinaRenPagoRubro) save(pagoRubro);
                            //ACTUALIZACION EN RUBROS MEJORAS
                            //REGISTRO DE RUBROS DE MEJORAS
                            if (rubro.getRubro().getId() == 7L && rubro.getMejDetRubroMejorasCollection() != null && !rubro.getMejDetRubroMejorasCollection().isEmpty()) {
                                
                                for (DetRubroMejoras m : rubro.getMejDetRubroMejorasCollection()) {
                                    if (m.getSaldo().compareTo(new BigDecimal("0.00")) > 0) {
                                        pagoRubroMejora = new PagoRubroMejora();
                                        if (valorRecaudacion.compareTo(m.getSaldo()) >= 0) {
                                            if (m.getValor().compareTo(m.getSaldo()) <= 0) {
                                                valorRecaudacionMejora = m.getValor();
                                            } else {
                                                valorRecaudacionMejora = m.getSaldo();
                                            }
                                            m.setSaldo(new BigDecimal("0.00"));
                                        } else {
                                            m.setSaldo(m.getSaldo().subtract(valorRecaudacion));
                                            valorRecaudacionMejora = valorRecaudacion;
                                        }
                                        save(m);
                                        pagoRubroMejora.setRubroMejoraPago(pagoRubro);
                                        pagoRubroMejora.setUbicacionObra(m.getUbicacionObra());
                                        pagoRubroMejora.setValor(valorRecaudacionMejora);
                                        save(pagoRubroMejora);
                                        valorRecaudacion = valorRecaudacion.subtract(valorRecaudacionMejora);
                                        if (valorRecaudacion.compareTo(new BigDecimal("0.00")) <= 0) {
                                            break;
                                        }
                                    }
                                }
                            }
                            valorLiquidacion = valorLiquidacion.subtract(valorRecaudar);
                            if (valorLiquidacion.compareTo(new BigDecimal("0.00")) <= 0) {
                                break;
                            }
                        }
                    }
                    
                }
                
            }
            save(liquidacion);
            // OBSERVACION DEL SALDO DE UNA LIQUIDACION
            if (liquidacion.getEstadoLiquidacion().getId() == 2L) {
                pago.setObservacion("Saldo: " + liquidacion.getTotalPago().subtract(pago.getValor()) + ".");
            }
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return pago;
    }
    
    public FinaRenLiquidacion realizarDescuentoRecargaInteresPredial(FinaRenLiquidacion emision, Date fechaPago) {
        Boolean aplicaRemision = this.aplicaRemision(emision);
        Map<String, Object> paramt;
        CtlgDescuentoEmision descuento;
        FinaRenRubrosLiquidacion rubroLiquidacion;
        Date fecha = new Date();
        if (fechaPago != null) {
            fecha = fechaPago;
        }
        Integer dia = Utils.getDateValues("D", fecha);
        Integer mes = Utils.getDateValues("M", fecha);
        Integer anio = Utils.getDateValues("Y", fecha);
        BigDecimal valorImpuesto = BigDecimal.ZERO;
        try {
            //UNA EMISION SIEMPRE TIENE EL RUBRO DE IMPUESTO PREDIAL
            for (FinaRenDetLiquidacion rubro : emision.getFinaRenDetLiquidacionList()) {
                rubroLiquidacion = find(FinaRenRubrosLiquidacion.class, rubro.getRubro());
                if (rubroLiquidacion != null) {
                    if (rubroLiquidacion.getCodigoRubro() != null && rubroLiquidacion.getCodigoRubro().equals(1L)) {
                        valorImpuesto = rubro.getValor();
                        break;
                    }
                }
            }
            //SE REALIZA UNA SOLO VEZ EL RECARGO O EL DESCUENTO
            emision.setRecargo(new BigDecimal("0.00"));
            emision.setDescuento(new BigDecimal("0.00"));
            if (emision.getRenPagoCollection() == null || emision.getRenPagoCollection().isEmpty()) {
                paramt = new HashMap<>();
                if (mes + 1 < 7 && anio.equals(emision.getAnio())) {
                    //SE REALIZA DECUENTO - DEACUERDO AL MES Y QUINCENA ANTES DEL MES DE JULIO
                    paramt.put("numMes", mes + 1);
                    paramt.put("numQuincena", dia > 15 ? 2 : 1);
                    
                    descuento = (CtlgDescuentoEmision) findByParameter(CtlgDescuentoEmision.class, paramt);
                    emision.setDescuento(valorImpuesto.multiply(descuento.getPorcentaje()).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
                } else {
                    // SE REALIZA RECARGO - DESPUES DE JUNIO 10% DEL IMPUESTO
                    if (!aplicaRemision) {
                        if (Objects.equals(emision.getAnio(), Utils.getAnio(new Date()))) {
                            emision.setRecargo(valorImpuesto.multiply(new BigDecimal("10")).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
                        }
                    }
                    
                }
            }
            //INTERES EMISION PREDIAL A�O VENCIDO
            emision.setInteres(new BigDecimal("0.00"));
            if (!aplicaRemision) {
                if (emision.getAnio() < anio) {
                    if (emision.getRenPagoCollection() == null || emision.getRenPagoCollection().isEmpty()) {// CONSULTAR CON UN LSTADO
                        Calendar fechaInteres = Calendar.getInstance();
                        fechaInteres.set(emision.getAnio(), Calendar.JANUARY, 1, 0, 0, 0);
//                        System.out.println("Anio " + emision.getAnio() + " fechaInteres " + fechaInteres.getTime() + " fechaPago " + fechaPago);
                        emision.setInteres(this.generarInteres(emision.getSaldo(), fechaInteres.getTime(), fechaPago));
                        
                    } else {
                        //CONSULTAR ULTIMO PAGO - SI EL ULTIMO PAGO FUE REALIZADO EN EL MISMO ANIO DE EMISION LA FECHA DE INTERES TB DESDE EL PRIMER DIA DE LA EMISION VENCDA
                        emision.setInteres(this.generarInteres(emision.getSaldo(), ((List<FinaRenPago>) emision.getRenPagoCollection()).get(emision.getRenPagoCollection().size() - 1).getFechaPago(), fechaPago));
                    }
                }
            }
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        //return emision;
        return emision;
    }
    
    public BigDecimal generarInteres(BigDecimal valor, Date fecha, Date fechaPago) {
        BigDecimal interes, a;
        BigDecimal interesValor = new BigDecimal("0.00");
        Calendar fechaHasta = Calendar.getInstance();
        if (fechaPago != null) {
            fechaHasta.setTime(fechaPago);
        }
        try {
            
            Object[] valores = {
                new SimpleDateFormat("dd-MM-YYYY").format(fechaHasta.getTime()),
                new SimpleDateFormat("dd-MM-YYYY").format(fechaHasta.getTime()),
                new SimpleDateFormat("dd-MM-YYYY").format(fechaHasta.getTime()),
                new SimpleDateFormat("dd-MM-YYYY").format(fechaHasta.getTime()),
                new SimpleDateFormat("dd-MM-YYYY").format(fecha),
                new SimpleDateFormat("dd-MM-YYYY").format(fecha),
                new SimpleDateFormat("dd-MM-YYYY").format(fecha),
                new SimpleDateFormat("dd-MM-YYYY").format(fecha)
            };
            interes = (BigDecimal) findNativeQuery("SELECT sum(r.porcentaje) FROM sgm_financiero.ren_intereses r WHERE \n"
                    + "( r.hasta <= TO_DATE(?,'DD-MM-YYYY') OR (r.hasta >=  TO_DATE(?,'DD-MM-YYYY') \n"
                    + "AND DATE_PART('month', r.hasta) = DATE_PART('month', TO_DATE(?,'DD-MM-YYYY')) \n"
                    + "AND DATE_PART('YEAR', r.hasta) <= DATE_PART('year', TO_DATE(?,'DD-MM-YYYY')))) \n"
                    + "AND r.id IN(SELECT id FROM sgm_financiero.ren_intereses i WHERE i.desde >=  TO_DATE(?,'DD-MM-YYYY') \n"
                    + "OR (i.desde <=  TO_DATE(?,'DD-MM-YYYY') AND DATE_PART('month', i.desde) = DATE_PART('month', TO_DATE(?,'DD-MM-YYYY')) AND DATE_PART('YEAR', i.desde) >= DATE_PART('year', TO_DATE(?,'DD-MM-YYYY'))))",
                    valores);
            // a = (BigDecimal) manager.getNativeQuery(QuerysFinanciero.getInteresNativo, new Object[]{new SimpleDateFormat("dd-MM-YYYY").format(fecha), new SimpleDateFormat("dd-MM-YYYY").format(fechaHasta.getTime())});
            if (interes != null) {
                if (valor != null) {
                    interesValor = interes.multiply(valor).divide(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
                }
            } else {
                JsfUtil.addErrorMessage("Error", "Verifique en el Mantenimiento de Intereses este agreado el procentaje de interes correspondiente al " + Utils.getAnio(fecha));
            }
            
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
        return interesValor;
    }
    
    public List findNativeQueryList(Class clase, String query, Object[] values) {
        List result = null;
        try {
            Query q = em.createNativeQuery(query);
            int position = 1;
            if (values != null) {
                for (Object value : values) {
                    q.setParameter(position, value);
                    position++;
                }
            }
            if (clase != null) {
                result = (List) Transformers.aliasToBean(clase);
            } else {
                result = q.getResultList();
            }
            Hibernate.initialize(result);
        } catch (HibernateException e) {
            System.out.println("err");
        }
        return result;
    }
    
    public Object findNativeQuery(String query, Object[] val) {
        
        Query q;
        Object ob = null;
        try {
            
            q = em.createNativeQuery(query);
            if (val != null) {
                for (int i = 0; i < val.length; i++) {
                    q.setParameter(i + 1, val[i]);
                }
            }
//            q.setMaxResults(1);

            if (q.getResultList().isEmpty()) {
                return null;
            }
            
            ob = (Object) q.getResultList().get(0);
            Hibernate.initialize(ob);
        } catch (HibernateException e) {
            System.err.println(e);
        }
        return ob;
    }
    
    public List<RenParametrosInteresMulta> getListParametrosInteresMulta(FinaRenLiquidacion liquidacion) {
        Map<String, Object> parametros = new HashMap<>();
        Date fecha = new Date();
        Integer dia = Utils.getDateValues("D", fecha);
        Integer mes = Utils.getDateValues("M", fecha);
        Integer anio = Utils.getDateValues("Y", fecha);
        try {
            parametros.put("tipoLiquidacion", liquidacion.getTipoLiquidacion());
            if (liquidacion.getAnio() < anio) {
                return findAll(RenParametrosInteresMulta.class, parametros);
            } else {
                parametros.put("dia", dia);
                parametros.put("mes", mes + 1);
            }
            //
            return findAllQuery("SELECT p FROM RenParametrosInteresMulta p WHERE p.tipoLiquidacion=:tipoLiquidacion AND p.dia<=:dia AND p.mes<=:mes", parametros);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public List<FinaRenTipoLiquidacion> gettiposLiquidacionByCodTitRep(int tipo) {
        param = new HashMap<>();
        
        switch (tipo) {
            case 1:
                return findAllEasy("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.estado = true AND r.prefijo IN ('PLU','ALC') ORDER BY r.nombreTransaccion ASC");
            case 2:
                return findAllEasy("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.estado = true AND r.prefijo IN ('ACT', 'PAT') ORDER BY r.nombreTransaccion ASC");
            case 3:
                //select *from sgm_financiero.ren_tipo_liquidacion where codigo_titulo_reporte in (195, 191, 16, 27, 175, 176,119, 120, 121, 122, 13, 29, 123, 124, 198, 199, 201, 202, 203, 204, 227, 214, 218, 217);
                return findAllEasy("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.estado = true AND r.prefijo in ('EXC','PLA','PDP','RDP','RPV','VSU','RAL','PLA','DEM','PRO','PER','PIA','APL','LNF','DSL','RPL','PLA','EEE','PAA','TEN','FNS') ORDER BY r.nombreTransaccion ASC");
            default:
                // 225 => tipo APROBACION DE PLANOS E INSCRIPCION DE CONSTRUCCIONES
                param.put("transaccionPadre", 61);
                return findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre = :transaccionPadre", param);
        }
    }
    
    public List findAllEasy(String query) {
        List l = null;
        try {
            l = findFirstAndMaxResult(query, null, null, null, null);
        } catch (Exception e) {
            System.out.println("easy");
        }
        return l;
    }
    
    public List findFirstAndMaxResult(String query, String[] par, Object[] val, Integer first, Integer max) {
        List result = null;
        try {
            javax.persistence.Query cq = getEntityManager().createQuery(query);
            if (first != null) {
                cq.setFirstResult(first);
            }
            if (max != null) {
                cq.setMaxResults(max);
            }
            if (par != null) {
                for (int i = 0; i < par.length; i++) {
                    cq.setParameter(par[i], val[i]);
                }
            }
            result = cq.getResultList();
            Hibernate.initialize(result);
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.err.println(e);
        }
        return result;
    }
    
    public FinaRenPatente buscarCiRucPatentePermiso(String ci) {
        param = new HashMap<>();
        param.put("ciRuc", ci);
        return (FinaRenPatente) findByParameter(FinaRenPatente.class, param);
    }
    
    public Integer existeLiquidacion(FinaRenLiquidacion liquidacion) {
        Object[] parametros = new Object[]{liquidacion.getTipoLiquidacion(), liquidacion.getAnio(), liquidacion.getLocalComercial()};
        Object temp = findNativeQuery("SELECT l.estado_liquidacion FROM asgard.fina_ren_liquidacion l WHERE l.tipo_liquidacion = ? AND l.anio  = ? AND l.local_comercial = ?", parametros);
        
        if (temp == null) {
            return 3;
        } else {
            if (Long.valueOf(temp.toString()).equals(1L)) {
                return 1;
            } else if (Long.valueOf(temp.toString()).equals(2L)) {
                return 2;
            } else {
                return 3;
            }
        }
    }
    
    public FinaRenLiquidacion guardarLiquidacionPatente(FinaRenLiquidacion liq, List<FinaRenDetLiquidacion> rubros, FinaRenTipoLiquidacion tipoLiq, RenBalancePatente balance) {
        Calendar c2 = Calendar.getInstance();
        FinaRenLiquidacion liquidacion = null;
        try {
            
            liquidacion = (FinaRenLiquidacion) (liq);
            if (liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas() != null && liquidacion.getTipoLiquidacion().getNecesitaValidacionRentas().equals(Boolean.TRUE)) {
                liquidacion.setValidada(Boolean.FALSE);
            } else {
                liquidacion.setValidada(Boolean.TRUE);
            }
            liquidacion.setLiquidadorResponsable(session.getNameUser());
            if (liquidacion.getComprador() != null) {
                liquidacion.setNombreComprador(liquidacion.getComprador().getNombreCompleltoSql());
                liquidacion.setIdentificacion(liquidacion.getComprador().getIdentificacionCompleta());
            }
            liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
            liquidacion.setNumComprobante(BigInteger.ZERO);
            if (liquidacion.getLocalComercial() != null) {
                liquidacion.setCodigoLocal(liquidacion.getLocalComercial().getNumLocal());
                if (liquidacion.getLocalComercial().getPredio() != null) {
                    liquidacion.setClaveCatastral(liquidacion.getLocalComercial().getClavePreial());
                    CatPredio temp = em.find(CatPredio.class, liquidacion.getLocalComercial().getPredio());
                    if (temp != null) {
                        liquidacion.setPredio(temp);
                        liquidacion.setAvaluoConstruccion(temp.getAvaluoConstruccion());
                        liquidacion.setAvaluoMunicipal(temp.getAvaluoMunicipal());
                        liquidacion.setAvaluoSolar(temp.getAvaluoSolar());
                    }
                }
            }
            if (liquidacion.getPredio() != null && liquidacion.getPredio().getId() != null) {
                liquidacion.setClaveCatastral(liquidacion.getPredio().getClaveCat());
                liquidacion.setAvaluoConstruccion(liquidacion.getPredio().getAvaluoConstruccion());
                liquidacion.setAvaluoMunicipal(liquidacion.getPredio().getAvaluoMunicipal());
                liquidacion.setAvaluoSolar(liquidacion.getPredio().getAvaluoSolar());
            }
            liquidacion.setSaldo(liquidacion.getTotalPago());
            liquidacion.setCodigoVerificador(Utils.generarCodigoVerificacion());
            liquidacion.setFechaIngreso(new Date());
            liquidacion.setUsuarioIngreso(session.getNameUser());
            liquidacion.setFechaIngreso(new Date());
            liquidacion.setNumLiquidacion(getMaxSecuenciaTipoLiquidacion(c2.get(Calendar.YEAR), tipoLiq.getId()));
            liquidacion.setIdLiquidacion(tipoLiq.getPrefijo().concat("-").concat(Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6)));
            liquidacion = (FinaRenLiquidacion) finaRenLiquidacionService.create(liquidacion);
            
            if (liquidacion != null) {
                for (FinaRenDetLiquidacion temp : rubros) {
                    if (temp.getRubro() != null) {
                        if (temp.getCantidad() == null) {
                            temp.setCantidad(1);
                        }
                        temp.setLiquidacion(liquidacion);
                        temp.setEstado(true);
                        temp.setValorRecaudado(BigDecimal.ZERO);
                        if (temp.getValor() != null && temp.getValor().compareTo(BigDecimal.ZERO) == 1) {
                            detalleLiquidacionService.create(temp);
                        }
                    }
                }
                System.out.println("balance " + balance.toString());
                if (balance != null) {
                    balance.setLiquidacion(liquidacion);
                    balance.setFechaIngreso(new Date());
                    balance.setEstado(Boolean.TRUE);
                    if (liquidacion.getLocalComercial() != null) {
                        balance.setLocalComercial(liquidacion.getLocalComercial());
                        if (liquidacion.getLocalComercial().getPatente() != null) {
                            balance.setPatente(liquidacion.getLocalComercial().getPatente());
                        }
                    }
                    balance.setUsuarioIngreso(session.getNameUser());
                    balanceService.create(balance);
                }
            }
            registroContableService.registroEmisiones(liquidacion);
            
        } catch (NumberFormatException e) {
            System.err.println(e);
        }
        return liquidacion;
    }
    
    public List<String> getResultUserValidadores() {
        return (List<String>) em.createNativeQuery("select a.usuario from (\n"
                + "select c.usuario, sum(c.registro_validados) from migracion.view_reporte_admin_control c \n"
                + "group by c.usuario order by sum(c.registro_validados)  desc) as a").getResultList();
    }
    
    public Integer existeLiquidacionPatente(FinaRenLiquidacion liquidacion) {
        
        Object temp = findNativeQuery("SELECT l.estado_liquidacion FROM asgard.fina_ren_liquidacion l WHERE l.tipo_liquidacion = ? AND l.anio  = ? AND l.patente = ?",
                new Object[]{liquidacion.getTipoLiquidacion().getId(), liquidacion.getAnio(), liquidacion.getPatente().getId()});
        if (temp == null) {
            return 3;
        } else {
            if (Long.valueOf(temp.toString()).equals(1L)) {
                return 1;
            } else if (Long.valueOf(temp.toString()).equals(2L)) {
                return 2;
            } else {
                return 3;
            }
        }
    }
    
    public Object find(String query, String[] par, Object[] val) {
        Object ob = null;
        try {
            javax.persistence.Query cq = getEntityManager().createQuery(query);
            cq.setMaxResults(1);
            if (par != null) {
                for (int i = 0; i < par.length; i++) {
                    cq.setParameter(par[i], val[i]);
                }
            }
            ob = cq.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }
    
    public Map<String, BigDecimal> valoresInteres(FinaRenLiquidacion emision, Date hasta) {
        FinaRenLiquidacion temp = recaudacionService.realizarDescuentoRecargaInteresPredial(emision, new Date());
        Map<String, BigDecimal> valoresMap = new HashMap<>();
        if (temp.getInteres() == null) {
            temp.setInteres(BigDecimal.ZERO);
        }
        if (temp.getDescuento() == null) {
            temp.setDescuento(BigDecimal.ZERO);
        }
        if (temp.getRecargo() == null) {
            temp.setRecargo(BigDecimal.ZERO);
        }
        
        valoresMap.put("interesCalculado", temp.getInteres());
        valoresMap.put("descuento", temp.getDescuento());
        valoresMap.put("recargo", temp.getRecargo());
        System.out.println(" temp.getInteres() " + temp.getInteres());
        return valoresMap;
    }
    
    public BigDecimal interesTemp(Date emision, BigDecimal valor, Long tipoLiqudacion) {
        BigInteger tipoLiqui = BigInteger.valueOf(tipoLiqudacion);
        System.out.println("emision " + emision + " " + valor + " " + tipoLiqudacion);
        param = new HashMap<>();
        param.put("fecha_emision", emision);
        param.put("valor_impuesto", valor);
        param.put("v_tipo_liquidacion", tipoLiqui);
        
        List<BigDecimal> result = (List<BigDecimal>) em.createNativeQuery("select asgard.calcular_interes_temop(?1,?2)")
                .setParameter(1, valor).setParameter(2, tipoLiqudacion).getResultList();
        if (result != null) {
            return result.get(0);
        }
        return BigDecimal.ZERO;
    }
    
    public <T> List<T> findAllOrdered(Class<T> entity, String[] order, Boolean[] prior) {
        List result = null;
        try {
            CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
            javax.persistence.criteria.CriteriaQuery cq = builder.createQuery();
            Root from = cq.from(entity);
            cq.select(from);
            for (int i = 0; i < order.length; i++) {
                if (prior[i] == true) {
                    cq.orderBy(builder.asc(from.get(order[i])));
                } else {
                    cq.orderBy(builder.desc(from.get(order[i])));
                }
            }
            result = getEntityManager().createQuery(cq).getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return result;
    }
    
    public void updateViewMaterializadas() {
        // String ok = (String) em.createNativeQuery("select *from migracion.update_view_materializada()").getResultStream().findFirst().orElse("error");
        System.out.println("ok");
    }
    
    public FinaRenEstadoLiquidacion getEstadoLiquidacionByDesc(Long activo) {
        return find(FinaRenEstadoLiquidacion.class, activo);
    }
    
    public FinaRenLiquidacion guardarLiquidacion(FinaRenLiquidacion liquidacion, List<FinaRenRubrosLiquidacion> rubrosLiquidacion, String prefijo, RenValoresPlusvalia valores) {
        try {
            final Long tipoLiquidacion = liquidacion.getTipoLiquidacion().getId();
            liquidacion.setCodigoVerificador(Utils.generarCodigoVerificacion());
            liquidacion.setSaldo(liquidacion.getTotalPago());
            liquidacion = liquidacionServices.create(liquidacion);
            if (liquidacion != null) {
                for (FinaRenRubrosLiquidacion rl : rubrosLiquidacion) {
                    if (rl.getCobrar()) {
                        FinaRenDetLiquidacion ruL = new FinaRenDetLiquidacion();
                        ruL.setCantidad(1);
                        ruL.setLiquidacion(liquidacion);
                        ruL.setRubro(rl);
                        ruL.setEstado(true);
                        ruL.setValor(rl.getValorTotal());
                        ruL.setValorRecaudado(BigDecimal.ZERO);
                        ruL.setValorSinDescuento(BigDecimal.ZERO);
                        detalleLiquidacionService.create(ruL);
                    }
                }
                if (valores != null) {
                    
                    valores.setLiquidacion(liquidacion);
                    List<RenDetallePlusvalia> detallePlusvalias = (List<RenDetallePlusvalia>) valores.getRenDetallePlusvaliaCollection();
                    valores = (RenValoresPlusvalia) renValoresPlusvaliaServices.create(valores);
                    if (detallePlusvalias != null) {
                        for (RenDetallePlusvalia detallePlusvalia : detallePlusvalias) {
                            detallePlusvalia.setValoresPlusvalia(valores);
                            renDetallePlusvaliaServices.create(detallePlusvalia);
                        }
                    }
                }
            }
            JsfUtil.addInformationMessage("", "La Liquidación se registro con exitó");
            return liquidacion;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Registrar Liquidacion", e);
            return null;
        }
    }
    
    public Boolean generarNumLiquidacion(FinaRenLiquidacion liquidacion, String prefijo) {
        final Long tipoLiquidacion = liquidacion.getTipoLiquidacion().getId();
        try {
            liquidacion.setNumLiquidacion(getMaxSecuenciaTipoLiquidacion(Utils.getAnio(new Date()), tipoLiquidacion));
            if (liquidacion.getNumLiquidacion() != null) {
                liquidacion.setIdLiquidacion(prefijo.concat("-").concat(Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6)));
            }
            liquidacion.setAnio(Utils.getAnio(new Date()));
            update(liquidacion);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, prefijo, e);
            return false;
        }
        
    }
    
    public List<FinaRenRubrosLiquidacion> getRubrosPorLiquidacion(Long idTipo) {
        Map<String, Object> par = new HashMap<>();
        par.put("tipo", idTipo);
        par.put("estado", true);
        return findAllQuery("select r from FinaRenRubrosLiquidacion r where tipoLiquidacion.id=:tipo and r.estado=:estado order by r.prioridad asc ", par);
    }
    
    public Object saveAll(Object entity) {
        
        Object ob = null;
        try {
            
            ob = save(entity); // RETORNA EL ID DEL OBJETO
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        return ob;
    }
    
    public Object save(Object o) {
        Object ob = null;
        try {
            ob = updateEntity(o);
            
        } catch (Exception e) {
            return null;
        }
        return ob;
    }
    
    public Object persist(Object o) {
        o = updateEntity(o);
        return o;
    }
    
    public List<AvalImpuestoPredios> getAvalImpuestoPrediosActivo(String estado) {
        return (List<AvalImpuestoPredios>) em.createQuery("SELECT cp FROM AvalImpuestoPredios cp WHERE cp.estado = :estado ORDER BY cp.parroquia ASC")
                .setParameter("estado", estado).getResultList();
        
    }
    
    public AvalImpuestoPredios saveAvalImpuestoPrediosAndDetCobro(AvalImpuestoPredios avalImpuestoPredios, List<AvalDetCobroImpuestoPredios> cobroImpuestoPrediosList) {
        AvalImpuestoPredios aip = null;
        try {
            if (avalImpuestoPredios.getId() != null) {
                aip = (AvalImpuestoPredios) save(avalImpuestoPredios);
                if (aip != null) {
                    for (AvalDetCobroImpuestoPredios adcip : cobroImpuestoPrediosList) {
                        adcip.setIdAvalImpuestoPredio(aip);
                        if (adcip.getId() != null) {
                            save(adcip);
                        } else {
                            save(adcip);
                        }
                    }
                } else {
                    return aip;
                }
                
            } else {
                aip = (AvalImpuestoPredios) save(avalImpuestoPredios);
                if (aip != null) {
                    for (AvalDetCobroImpuestoPredios adcip : cobroImpuestoPrediosList) {
                        adcip.setIdAvalImpuestoPredio(aip);
                        if (adcip.getId() != null) {
                            save(adcip);
                        } else {
                            save(adcip);
                        }
                    }
                } else {
                    return aip;
                }
                
            }
            
        } catch (Exception e) {
            System.err.println(e);
        }
        return aip;
    }
    
    public BigDecimal deduccionTrasAños(Integer anio, FinaRenPatente patente) {
        
        List<BigDecimal> result = (List<BigDecimal>) em.createNativeQuery("select case when Coalesce(sum(l.base_imponible)) is null then 0 when Coalesce(sum(l.base_imponible)) < 3\n"
                + "		 then 0 else round(Coalesce(sum(l.base_imponible)) / 3,2) end as base from asgard.fina_ren_liquidacion l "
                + "where (l.anio between ?1 and ?2) and l.patente=?3 and l.estado_liquidacion=?4")
                .setParameter(1, anio - 1).setParameter(2, anio - 4).setParameter(3, BigDecimal.valueOf(patente.getId()))
                .setParameter(4, 1).getResultList();
        
        if (!result.isEmpty()) {
            return result.get(0);
        }
        return BigDecimal.ZERO;
    }

//    public Boolean existeAbre(FnResolucionTipo reso){
//        List<FnResolucionTipo> listaReso = null;
//        listaReso = new ArrayList<>();
//        listaReso = fin
//        
//        
//        
//    return true;
//    }
    public Object generateEmisionPredial(List<CatPredio> prediosSeleccionados, Integer anioValorizarInicio, Integer anioValorizarFin, Boolean control, String usuario, boolean consideraConstruccion, Integer condicionUno, Integer condicionDos) {
        Object dep = null;
        try {
            if (!control) {
                for (CatPredio cp : prediosSeleccionados) {
                    param = new HashMap<>();
                    param.put("idPredio", cp.getId());
                    param.put("anio", anioValorizarInicio);
                    param.put("estado", 2L);
                    //dep = manager.ejecutarFuncionAvaluosEmisionPredial(SchemasConfig.APP1 + ".emision_predial_ren_liquidacion", cp.getId(), anioValorizarInicio, anioValorizarFin, usuario);
                    List<FinaRenLiquidacion> liquidaciones = findAllQuery("SELECT r FROM FinaRenLiquidacion r JOIN r.predio p WHERE p.id = :idPredio AND r.anio = :anio AND r.estadoLiquidacion.id = :estado", param);
                    
                    if (liquidaciones != null) {
                        for (FinaRenLiquidacion l : liquidaciones) {
                            l.setEstadoLiquidacion(find(FinaRenEstadoLiquidacion.class, 3L));
                            save(l);
                        }
                        dep = ejecutarFuncionAvaluosEmisionPredial(Utils.SCHEMA_CATASTRO + ".emision_predial_ren_liquidacion", cp.getId(), anioValorizarInicio, anioValorizarFin, usuario, consideraConstruccion, condicionUno, condicionDos);
                    }
                }
            } else {
                dep = ejecutarFuncionAvaluosEmisionPredial(Utils.SCHEMA_CATASTRO + ".emision_predial_ren_liquidacion", 0L, anioValorizarInicio, anioValorizarFin, usuario, consideraConstruccion, condicionUno, condicionDos);
            }
        } catch (Exception e) {
            System.err.println(e);
            
        }
        return dep;
    }
    
    public Object ejecutarFuncionAvaluosEmisionPredial(String function, Long predioId, Integer anioInio, Integer anioFin, String usuario, boolean consideraConstruccion, Integer condicionUno, Integer condicionDos) {
        Object o = null;
        try {
            
            o = executeFunctionPredial(function, predioId, usuario, anioInio, anioFin, consideraConstruccion, false, condicionUno, condicionDos);
            
        } catch (Exception e) {
            System.out.println("error " + e);
        }
        return o;
    }

    //PROGRAMAR WEB SERVICES
    public FinaRenLiquidacion getDeudasPredioAnioActualByAnio(CatPredio predio, Integer anio) {
        FinaRenLiquidacion liquidacion = (FinaRenLiquidacion) find("select e from FinaRenLiquidacion e where e.anio = :anio and e.tipoLiquidacion.id in (2,3) and e.predio.numPredio = :predio and e.estadoLiquidacion.id in (1,2)",
                new String[]{"anio", "predio"}, new Object[]{anio, predio.getNumPredio()});
        if (liquidacion != null) {
            
            if (predio != null) {
                if (predio.getPropiedad() != null) {
                    if (!predio.getPropiedad().equals("PUBLICO") || !predio.getPropiedad().equals("JURISDICCIÓN MUNICIPAL") || !predio.getPropiedad().equals("ESTADO CENTRAL")) {
                        return liquidacion;
                    } else {
                        return null;
                    }
                }
            }
        }
        return liquidacion;
    }
    
    public List<ReporteSeguimientoValidadores> reporteRecord() {
        List<ReporteSeguimientoValidadores> list = new ArrayList<>();
        Query query = em.createNativeQuery("SELECT u.usuario,\n"
                + "            count(c.usuario_validador) AS registro_validados\n"
                + "           FROM cliente c,\n"
                + "            auth.usuarios u\n"
                + "          WHERE  c.usuario_validador IS NOT NULL and u.estado=true"
                + " and c.usuario_validador  not in(411) AND u.id = c.usuario_validador and c.validado=true AND c.valid_admin = false and u.estado=true\n"
                + "          GROUP BY u.id, u.usuario\n"
                + "          ORDER BY (count(c.usuario_validador)) DESC");
        List<Object[]> result = query.getResultList();
        
        if (Utils.isNotEmpty(result)) {
            
            ReporteSeguimientoValidadores report;
            for (Object[] data : result) {
                report = new ReporteSeguimientoValidadores();
                report.setUser((String) data[0]);
                report.setValidados((BigInteger) data[1]);
                report.setAsignados(BigInteger.valueOf(asignados(report.getUser())));
                
                Cliente temp = getNombresUsuarios(report.getUser());
                if (temp != null) {
                    report.setNombres(temp.getNombreCompleto());
                } else {
                    report.setNombres("No Econtrado");
                }
                Double valorUno = report.getValidados().doubleValue();
                Double valorDos = report.getAsignados().doubleValue();
                Double resultado = (valorUno / valorDos) * 100;
                report.setEficiencia(Precision.round(resultado, 2));
                list.add(report);
            }
        }
        return list;
    }
    
    public List<FinaRenPago> getPagosByPredioTipoLiquidacionAnioPagada(Long predio, CatPredioRuralDTO predioRural, FinaRenTipoLiquidacion tipo, Integer desde, Integer hasta) {
        List<FinaRenPago> pagos = null;
        try {//urbano
            if (predio != null) {
                param = new HashMap<>();
                param.put("tipoLiquidacion", new FinaRenTipoLiquidacion(2L));
                param.put("predio", predio);
                param.put("desde", desde);
                param.put("hasta", hasta);
                pagos = findAllQuery("SELECT p FROM FinaRenPago p WHERE p.estado=TRUE AND (p.liquidacion.anio BETWEEN :desde AND :hasta) AND p.liquidacion.tipoLiquidacion=:tipoLiquidacion AND p.liquidacion.predio=:predio AND p.liquidacion.estadoLiquidacion.id IN (1)", param);
            }
            return pagos;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public FinaRenLiquidacion getLiquidacionPorPredioAnioTipoEstado(String cod_local, Integer anio, Long tipoLiquidacion, Long estado) {
        return (FinaRenLiquidacion) em.createQuery("SELECT r FROM FinaRenLiquidacion r WHERE r.codigoLocal = :codLocal AND r.anio = :anio AND r.tipoLiquidacion.id = :idTipoLiquidacion and r.estadoLiquidacion.id = :estadoLiquidacion")
                .setParameter("codLocal", cod_local)
                .setParameter("anio", anio)
                .setParameter("idTipoLiquidacion", tipoLiquidacion)
                .setParameter("estadoLiquidacion", estado)
                .getResultStream().findFirst().orElse(new FinaRenLiquidacion());
        
    }
    //ps.getPredio(), null, null, i, 13L, exoneracionTipo.getId()

    public Boolean validarAplicarExoneracion(String codLocal, CatPredioRusticoDTO predioRustico, EmisionesRuralesExcelDTO predioRustico2017, Integer anio, Long tipoLiquidacion, Long tipoExoneracion) {
        FinaRenLiquidacion liquidacionDadaBaja = null;
        FinaRenLiquidacion liquidacion = null;
        Boolean validador = Boolean.FALSE;
        if (!codLocal.isEmpty()) {
            
            liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(codLocal, anio, tipoLiquidacion, 2L); //el dos es pendiente de pago

        }
        if (liquidacionDadaBaja != null && liquidacionDadaBaja.getExoneracionLiquidacionCollection() != null && !liquidacionDadaBaja.getExoneracionLiquidacionCollection().isEmpty()) {
            for (FnExoneracionLiquidacion exL : liquidacionDadaBaja.getExoneracionLiquidacionCollection()) {
                if (exL.getExoneracion() != null && exL.getExoneracion().getExoneracionTipo() != null && exL.getExoneracion().getExoneracionTipo().getId().equals(tipoExoneracion)) {
                    validador = Boolean.TRUE;
                    break;
                }
            }
            if (!validador) {
                liquidacionDadaBaja = null;
            }
        }
        return liquidacionDadaBaja == null && liquidacion != null;
    }

    /**
     * La liquidacion Original pasa a estado 4, se crea una nueva liquidacion en
     * caso de ser necesario en base a la original en estado 2 y registra un
     * RenSolicitudesLiquidacion
     *
     * @param liquidacion
     * @param solicitud
     * @param generarPosterior
     * @param estadoLiquidacion
     * @return
     */
    public FinaRenLiquidacion clonarRenLiquidacion(FinaRenLiquidacion liquidacion, FnSolicitudExoneracion solicitud, Boolean generarPosterior, Long estadoLiquidacion) {
        FinaRenLiquidacion liquidacion2 = null;
        
        try {
            liquidacion.setEstadoLiquidacion((FinaRenEstadoLiquidacion) find(FinaRenEstadoLiquidacion.class,
                    4L));
            save(liquidacion);
            if (generarPosterior) {
                liquidacion2 = (FinaRenLiquidacion) Utils.clone(liquidacion);
                liquidacion2.setId(null);
                liquidacion2.setComprador(liquidacion.getComprador());
                liquidacion2.setTramite(liquidacion.getTramite());
                liquidacion2.setTipoLiquidacion(liquidacion.getTipoLiquidacion());
                liquidacion2.setPredio(liquidacion.getPredio());
                liquidacion2.setPredioRustico(liquidacion.getPredioRustico());
                liquidacion2.setRuralExcel(liquidacion.getRuralExcel());
                liquidacion2.setLocalComercial(liquidacion.getLocalComercial());
                liquidacion2.setRenValoresPlusvalia(liquidacion.getRenValoresPlusvalia());
                liquidacion2.setVendedor(liquidacion.getVendedor());
                liquidacion2.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(estadoLiquidacion));
                liquidacion2.setAvaluoConstruccion(liquidacion.getAvaluoConstruccion());
                liquidacion2.setAvaluoMunicipal(liquidacion.getAvaluoMunicipal());
                liquidacion2.setAvaluoSolar(liquidacion.getAvaluoSolar());
                liquidacion2.setAreaTotal(liquidacion.getAreaTotal());
                liquidacion2.setFechaIngreso(new Date());
                liquidacion2.setEstaExonerado(Boolean.TRUE);
                liquidacion2.setBombero(Boolean.FALSE);
                liquidacion2.setNombreComprador(liquidacion.getNombreComprador());
                liquidacion2.setCoactiva(liquidacion.getCoactiva());
                liquidacion2.setEstadoCoactiva(liquidacion.getEstadoCoactiva());
                liquidacion2 = (FinaRenLiquidacion) save(liquidacion2);
                em.getTransaction().commit();
            }
            if (solicitud != null) {
                RenSolicitudesLiquidacion solsLiq = new RenSolicitudesLiquidacion();
                solsLiq.setEstado(Boolean.TRUE);
                solsLiq.setSolExoneracion(solicitud);
                solsLiq.setLiquidacion(liquidacion);
                save(solsLiq);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
        
        return liquidacion2;
    }
    
    public BigDecimal generarDetalleRenLiquidacionObtenerTotal(FnSolicitudExoneracion solicitud, FinaRenLiquidacion liquidacionOriginal, BigDecimal valorCalculado, FnSolicitudTipoLiquidacionExoneracion tipoLiquidacionExoneracion, Boolean realizarCalculo, FnExoneracionTipo tipoExoneracion) {
        BigDecimal totalPagoExoneracion = new BigDecimal("0.00");
        
        FinaRenDetLiquidacion detalleExonerado;
        try {
            for (FinaRenDetLiquidacion detalle : liquidacionOriginal.getRenDetLiquidacionCollection()) {
                if (detalle.getRubro().getAplicaExoneracion() == true) {
                    totalPagoExoneracion = totalPagoExoneracion.add(detalle.getValor());
                }
            }
            
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "GENERACION DETALLE EXONERACION", e);
        }
        return totalPagoExoneracion;
    }
    
    public FnExoneracionLiquidacion registrarRelacionLiquidacionDadaBajaExoneracion(FinaRenLiquidacion liquidacionOriginal, FnSolicitudExoneracion solicitud, String usuario) {
        FnExoneracionLiquidacion exoneracionLiquidacion = new FnExoneracionLiquidacion();
        List<CoaJuicioPredio> juicioPredios;
        CoaJuicioPredio jp;
        try {
            exoneracionLiquidacion.setFechaIngreso(new Date());
            exoneracionLiquidacion.setLiquidacionOriginal(liquidacionOriginal);
            exoneracionLiquidacion.setLiquidacionPosterior(null);
            exoneracionLiquidacion.setExoneracion(solicitud);
            exoneracionLiquidacion.setUsuarioIngreso(usuario);
            exoneracionLiquidacion.setEstado(Boolean.TRUE);
            exoneracionLiquidacion.setEsUrbano(Boolean.TRUE);
            save(exoneracionLiquidacion);
            if (liquidacionOriginal != null) {
                param = new HashMap<>();
                param.put("liquidacion", Integer.parseInt(liquidacionOriginal.getId().toString()));
                juicioPredios = findAllQuery("select jp from CoaJuicioPredio jp where jp.liquidacion.id=:liquidacion AND jp.estado=TRUE", param);
                if (juicioPredios != null && !juicioPredios.isEmpty()) {
                    for (CoaJuicioPredio juicioPredio : juicioPredios) {
                        if (juicioPredio.getLiquidacion().getId().equals(liquidacionOriginal.getId())) {
                            jp = new CoaJuicioPredio();
                            jp.setAbogadoJuicio(juicioPredio.getAbogadoJuicio());
                            jp.setAnioDesde(juicioPredio.getAnioDesde());
                            jp.setAnioDeuda(juicioPredio.getAnioDeuda());
                            jp.setAnioHasta(juicioPredio.getAnioHasta());
                            jp.setEstado(Boolean.TRUE);
                            jp.setJuicio(juicioPredio.getJuicio());
                            jp.setLiquidacion(null);
                            jp.setObservacion(juicioPredio.getObservacion());
                            jp.setPredio(juicioPredio.getPredio());
                            jp.setValorDeuda(null);
                            save(jp);
                            juicioPredio.setEstado(Boolean.FALSE);
                            save(juicioPredio);
                            if (juicioPredio.getJuicio().getTotalDeuda() != null) {
                                juicioPredio.getJuicio().setTotalDeuda(juicioPredio.getJuicio().getTotalDeuda().subtract(liquidacionOriginal.getTotalPago()));
                                save(juicioPredio.getJuicio());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            exoneracionLiquidacion = null;
            LOG.log(Level.SEVERE, "GENERACION DETALLE EXONERACION", e);
        }
        return exoneracionLiquidacion;
    }
    
    public BigDecimal obtenerValorParaCalculoExoneracion(FnSolicitudExoneracion solicitudExoneracion, BigDecimal valor, BigDecimal valorLimite) {
        BigDecimal valorCalculado = new BigDecimal("0.00");
        BigDecimal valorPorcentaje;
        try {
            if (solicitudExoneracion.getTipoValor() != null) {
                switch (solicitudExoneracion.getTipoValor().getId().intValue()) {
                    case 14://POR Valor rebaja comercial
                        if (solicitudExoneracion != null) {
                            if (solicitudExoneracion.getExoneracionTipo().getValidaRemuneracion() && solicitudExoneracion.getValor().compareTo(valorLimite) > 0) {
                                valorCalculado = valor.subtract(valorLimite);
                            } else {
                                valorCalculado = valor.subtract(solicitudExoneracion.getValor());
                            }
                        }
                        
                        break;
                    
                    case 13://POR PORCENTAJE REBAJA COMERCIAL
                        valorPorcentaje = valor.multiply(solicitudExoneracion.getValor()).divide(new BigDecimal("100"));
                        if (solicitudExoneracion.getExoneracionTipo().getValidaRemuneracion() && valorPorcentaje.compareTo(valorLimite) > 0) {
                            valorCalculado = valor.subtract(valorLimite);
                        } else {
                            valorCalculado = valor.subtract(valorPorcentaje);
                        }
                        break;
                    
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "CALCULO DE AVALUO PARA EXONERACION", e);
        }
        return valorCalculado.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : valorCalculado;
    }
    
    public FinaRenLiquidacion getLiquidacionPorPredioRuralAnioTipoEstado(CatPredioRusticoDTO predioRustico, Integer anio, Long tipoLiquidacion, Long estado) {
        return (FinaRenLiquidacion) find("SELECT r FROM FinaRenLiquidacion r WHERE r.predioRustico = :predio AND r.anio = :anio AND r.tipoLiquidacion = :idTipoLiquidacion and r.estadoLiquidacion.id = :estadoLiquidacion",
                new String[]{"predio", "anio", "idTipoLiquidacion", "estadoLiquidacion"}, new Object[]{predioRustico, anio, tipoLiquidacion, estado});
    }
    
    public List<CatPredioModel> exoneracionA500Remuneraciones(BigDecimal salarioMax, List<Long> prediosUrbanos) {
        List<CatPredioModel> prediosUrbanosExoneracion = new ArrayList();
        BigDecimal totalesAvaluos = BigDecimal.ZERO;
        for (Long prediosUrbano : prediosUrbanos) {
            CatPredioModel predio = new CatPredioModel();
            predio = buscarPredioUrbano(prediosUrbano);
            totalesAvaluos = totalesAvaluos.add(predio.getAvaluoMunicipal() == null ? BigDecimal.ZERO : predio.getAvaluoMunicipal());
        }
        int res = totalesAvaluos.compareTo(salarioMax);
        if (res == 1) {
            prediosUrbanosExoneracion = prediosQuePuedenSerExonerados(prediosUrbanos, salarioMax);
            return prediosUrbanosExoneracion;
        } else {
            
            for (Long prediosUrbano : prediosUrbanos) {
                CatPredioModel predio = new CatPredioModel();
                predio = buscarPredioUrbano(prediosUrbano);
                predio.setAvaluoMunicipal(BigDecimal.ZERO);
                prediosUrbanosExoneracion.add(predio);
            }
        }
        
        return prediosUrbanosExoneracion;
    }
    
    public List<CatPredioModel> prediosQuePuedenSerExonerados(List<Long> prediosSeleccionados, BigDecimal salarioMax) {
        List<CatPredioModel> listaPrediosSeleccionados = new ArrayList<>();
        
        for (Long pre : prediosSeleccionados) {
            listaPrediosSeleccionados.add(buscarPredio(pre));
        }
        List<CatPredioModel> prediosUrbanosAExonerar = new ArrayList<>();
        BigDecimal totalesAvaluos = BigDecimal.ZERO;
        Collections.sort(listaPrediosSeleccionados, (CatPredioModel p1, CatPredioModel p2)
                -> new Integer(p1.getAvaluoMunicipal().toBigInteger().intValue())
                        .compareTo(p2.getAvaluoMunicipal().toBigInteger().intValue()));
        prediosUrbanosAExonerar = new ArrayList();
        for (CatPredioModel cp : listaPrediosSeleccionados) {
            if (cp.getAvaluoMunicipal().compareTo(salarioMax) == 1) {
                cp.setAvaluoMunicipal(cp.getAvaluoMunicipal().subtract(salarioMax));
                prediosUrbanosAExonerar.add(cp);
                break;
            } else {
                salarioMax = salarioMax.subtract(cp.getAvaluoMunicipal());
                cp.setAvaluoMunicipal(BigDecimal.ZERO);
                prediosUrbanosAExonerar.add(cp);
            }
            
        }
        return prediosUrbanosAExonerar;
    }
    
    public FinaRenLiquidacion clonarRenLiquidacion(FinaRenLiquidacion liquidacion, FnSolicitudExoneracion solicitud) {
        FinaRenLiquidacion liquidacion2;
        FinaRenEstadoLiquidacion estado;
        
        try {
            estado = liquidacion.getEstadoLiquidacion();
            liquidacion.setEstadoLiquidacion((FinaRenEstadoLiquidacion) find(FinaRenEstadoLiquidacion.class,
                    4L));
            save(liquidacion);
            liquidacion2 = (FinaRenLiquidacion) Utils.clone(liquidacion);
            liquidacion2.setId(null);
            liquidacion2.setComprador(liquidacion.getComprador());
            liquidacion2.setTramite(liquidacion.getTramite());
            liquidacion2.setTipoLiquidacion(liquidacion.getTipoLiquidacion());
            liquidacion2.setPredio(liquidacion.getPredio());
            liquidacion2.setLocalComercial(liquidacion.getLocalComercial());
            liquidacion2.setRenValoresPlusvalia(liquidacion.getRenValoresPlusvalia());
            liquidacion2.setVendedor(liquidacion.getVendedor());
            liquidacion2.setPredioRustico(liquidacion.getPredioRustico());
            //liquidacion2.setUsuario(liquidacion.getUsuario());
            liquidacion2.setEstadoLiquidacion(estado);
            liquidacion2.setAvaluoConstruccion(liquidacion.getAvaluoConstruccion());
            liquidacion2.setAvaluoMunicipal(liquidacion.getAvaluoMunicipal());
            liquidacion2.setAvaluoSolar(liquidacion.getAvaluoSolar());
            liquidacion2.setAreaTotal(liquidacion.getAreaTotal());
            liquidacion2.setFechaIngreso(new Date());
            liquidacion2.setEstaExonerado(Boolean.TRUE);
            liquidacion2.setBombero(Boolean.FALSE);
            liquidacion2.setNombreComprador(liquidacion.getNombreComprador());
            liquidacion2 = (FinaRenLiquidacion) save(liquidacion2);
            
            if (solicitud != null) {
                RenSolicitudesLiquidacion solsLiq = new RenSolicitudesLiquidacion();
                solsLiq.setEstado(Boolean.TRUE);
                solsLiq.setSolExoneracion(solicitud);
                solsLiq.setLiquidacion(liquidacion);
                save(solsLiq);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
        
        return liquidacion2;
    }
    
    public void clase8_and_tipo38_or_clase4_or_clase9_tipo39(Long predioId, Integer anioInicio, Integer anioDif, HistoricoTramites ht, FnSolicitudExoneracion solicitud, List<FnExoneracionLiquidacion> exoneraciones, String username, CatPredioRusticoDTO rustico) {
        
        List<FinaRenDetLiquidacion> detalleLiquidacion;
        
        CatPredioModel predio = new CatPredioModel();
        predio = buscarPredio(predioId);
        
        FinaRenLiquidacion liquidacion, liquidacion2 = null;
        FinaRenDetLiquidacion nuevoRubro;
        BigDecimal total = BigDecimal.ZERO;
        CatPredioModel predioRaiz;
        FnExoneracionLiquidacion tablaIntermedia;
        DetRubroMejoras mejoraEncontrada, rubroMejora;
        
        for (int i = 0; i <= anioDif; i++) {
            total = BigDecimal.ZERO;
            liquidacion = (FinaRenLiquidacion) find("SELECT r FROM FinaRenLiquidacion r WHERE r.predio = :predio AND r.anio = :anio AND r.tipoLiquidacion.id = :idTipoLiquidacion and r.estadoLiquidacion.id = :estadoLiquidacion",
                    new String[]{"predio", "anio", "idTipoLiquidacion", "estadoLiquidacion"}, new Object[]{predio, anioInicio, new Long(13L), new Long(4L)});
            if (liquidacion == null) {
                liquidacion = (FinaRenLiquidacion) find("SELECT r FROM FinaRenLiquidacion r WHERE r.predio = :predio AND r.anio = :anio AND r.tipoLiquidacion.id = :idTipoLiquidacion and r.estadoLiquidacion.id = :estadoLiquidacion",
                        new String[]{"predio", "anio", "idTipoLiquidacion", "estadoLiquidacion"}, new Object[]{predio, anioInicio, new Long(13L), new Long(2L)});
                if (liquidacion != null) {
                    liquidacion2 = clonarRenLiquidacion(liquidacion, solicitud);
                    //  liquidacion2.setTramite(ht);
                    liquidacion2.setObservacion("Tiene una exoneración de: " + solicitud.getExoneracionTipo().getDescripcion().toUpperCase()
                            + "\nNúmero de resolución: " + solicitud.getNumResolucionSac());
                    if (anioInicio >= 2014) {
                    }
                    
                    for (FinaRenDetLiquidacion temp : liquidacion.getRenDetLiquidacionCollection()) {
                        nuevoRubro = new FinaRenDetLiquidacion();
                        nuevoRubro.setEstado(true);
                        nuevoRubro.setLiquidacion(liquidacion2);
                        nuevoRubro.setRubro(temp.getRubro());
                        nuevoRubro.setValor(temp.getValor());
                        nuevoRubro.setValorRecaudado(BigDecimal.ZERO);
                        nuevoRubro = (FinaRenDetLiquidacion) save(nuevoRubro);
                        if (nuevoRubro.getRubro().getId() == 7L) {
                            mejoraEncontrada = (DetRubroMejoras) find("SELECT r FROM MejDetRubroMejoras r WHERE r.rubroMejora = :detLiq AND r.estado = true",
                                    new String[]{"detLiq"}, new Object[]{temp});
                            if (mejoraEncontrada != null) {
                                rubroMejora = new DetRubroMejoras();
                                rubroMejora.setRubroMejora(nuevoRubro);
                                rubroMejora.setFechaIngreso(new Date());
                                rubroMejora.setValor(nuevoRubro.getValor());
                                rubroMejora.setSaldo(nuevoRubro.getValor());
                                rubroMejora.setEstado(Boolean.TRUE);
                                rubroMejora.setUbicacionObra(mejoraEncontrada.getUbicacionObra());
                                save(rubroMejora);
                            }
                        }
                        
                        total = total.add(nuevoRubro.getValor());
                    }
                    liquidacion2.setEstaExonerado(Boolean.TRUE);
                    liquidacion2.setTotalPago(total);
                    liquidacion2.setSaldo(total);
                    save(liquidacion2);
                    
                    tablaIntermedia = new FnExoneracionLiquidacion();
                    tablaIntermedia.setFechaIngreso(new Date());
                    tablaIntermedia.setLiquidacionOriginal(liquidacion);
                    tablaIntermedia.setLiquidacionPosterior(liquidacion2);
                    tablaIntermedia.setExoneracion(solicitud);
                    tablaIntermedia.setUsuarioIngreso(username);
                    tablaIntermedia.setEstado(Boolean.TRUE);
                    tablaIntermedia.setEsUrbano(Boolean.TRUE);
                    tablaIntermedia = (FnExoneracionLiquidacion) save(tablaIntermedia);
                    exoneraciones.add(tablaIntermedia);
                }
            }
            anioInicio++;
            
        }

        // PREDIO RUSTICO - EMISION (FALTA)
        if (rustico != null) {
            for (int i = 0; i <= anioDif; i++) {
                total = BigDecimal.ZERO;
                liquidacion = (FinaRenLiquidacion) find("SELECT r FROM FinaRenLiquidacion r WHERE r.predio = :predio AND r.anio = :anio AND r.tipoLiquidacion.id = :idTipoLiquidacion and r.estadoLiquidacion.id = :estadoLiquidacion",
                        new String[]{"predio", "anio", "idTipoLiquidacion", "estadoLiquidacion"}, new Object[]{rustico, anioInicio, new Long(7L), new Long(4L)});
                
                if (liquidacion == null) {
                    liquidacion = (FinaRenLiquidacion) find("SELECT r FROM FinaRenLiquidacion r WHERE r.predio = :predio AND r.anio = :anio AND r.tipoLiquidacion.id = :idTipoLiquidacion and r.estadoLiquidacion.id = :estadoLiquidacion",
                            new String[]{"predio", "anio", "idTipoLiquidacion", "estadoLiquidacion"}, new Object[]{rustico, anioInicio, new Long(7L), new Long(2L)});
                    if (liquidacion != null) {
                        liquidacion2 = this.clonarRenLiquidacion(liquidacion, solicitud);
                        //liquidacion2.setTramite(ht);

                        for (FinaRenDetLiquidacion temp : liquidacion.getRenDetLiquidacionCollection()) {
                            nuevoRubro = new FinaRenDetLiquidacion();
                            nuevoRubro.setEstado(true);
                            nuevoRubro.setLiquidacion(liquidacion2);
                            nuevoRubro.setRubro(temp.getRubro());
                            nuevoRubro.setValorRecaudado(BigDecimal.ZERO);
                            
                            if (temp.getRubro().getId() == 18L) { // RUBROS 1 DEL SAC, 18 de SGM
                                nuevoRubro.setValor(BigDecimal.ZERO);
                            } else {
                                total = total.add(temp.getValor());
                                nuevoRubro.setValor(temp.getValor());
                            }
                            save(nuevoRubro);
                        }
                        liquidacion2.setEstaExonerado(Boolean.TRUE);
                        liquidacion2.setTotalPago(total);
                        liquidacion2.setSaldo(total);
                        save(liquidacion2);
                        
                        tablaIntermedia = new FnExoneracionLiquidacion();
                        tablaIntermedia.setFechaIngreso(new Date());
                        tablaIntermedia.setLiquidacionOriginal(liquidacion);
                        tablaIntermedia.setLiquidacionPosterior(liquidacion2);
                        tablaIntermedia.setExoneracion(solicitud);
                        tablaIntermedia.setUsuarioIngreso(username);
                        tablaIntermedia.setEstado(Boolean.TRUE);
                        tablaIntermedia.setEsUrbano(Boolean.FALSE);
                        tablaIntermedia = (FnExoneracionLiquidacion) save(tablaIntermedia);
                        exoneraciones.add(tablaIntermedia);
                    }
                }
                
                anioInicio++;
                
            }
        }
    }
    
    public List<FnExoneracionLiquidacion> aplicarExoneracion(Map<String, Object> param, FnSolicitudExoneracion sol, String username) {
        FnSolicitudExoneracion solicitud = null;
        List<Long> predios = new ArrayList();
        List<FnExoneracionLiquidacion> exoneraciones = new ArrayList();
        CtlgSalario salario;
        CatPredioModel predioRaiz;
        BigDecimal salarioMax = BigDecimal.ZERO, diferenciaSalarioTemp;
        FnExoneracionClase exoneracionClase;
        FnExoneracionTipo exoneracionTipo;
        CatPredioModel predio = null;
        FinaRenLiquidacion liquidacion = null, liquidacion2 = null;
        Map<BigInteger, SolicitudExoneracionEnteDTO> map = new HashMap();
        Cliente solicitante;
        CatPredioModel predioModel = new CatPredioModel();
        BigDecimal totalPagoExneracion;
        BigDecimal valorCalculado;
        BigDecimal valor = new BigDecimal("0.00");
        Map<String, Object> parametros;
        List<FnSolicitudExoneracionPredios> prediosSolicitud;
        List<FnSolicitudTipoLiquidacionExoneracion> tipoLiquidacionesSolicitud;
        BigDecimal avaluoCalculo = null;
        FinaRenLiquidacion liquidacionExonerada;
        Long tipoPredio = Long.valueOf(param.get("ruralUrbano").toString());
        List<Integer> cantidadSolicitudes = new ArrayList<>();
        cantidadSolicitudes = (List<Integer>) param.get("aniosSeleccionados");
        solicitud = sol;
        if (sol.getPredio() != null) {
//            predios.add(sol.getPredio());
        }
        if (solicitud == null) {
            return null;
        }
        Integer anioDif = solicitud.getAnioFin() - solicitud.getAnioInicio();
        Integer anioInicio = solicitud.getAnioInicio();
        
        try {
            //b = true; --HENRY
            salario = (CtlgSalario) find("SELECT f FROM CtlgSalario f WHERE f.anio =:anio", new String[]{"anio"}, new Object[]{new BigInteger(Utils.getAnio(new Date()).toString())});
            
            salarioMax = salario.getValor().multiply(new BigDecimal(500));
            exoneracionTipo = solicitud.getExoneracionTipo();
            exoneracionClase = exoneracionTipo.getExoneracionClase();
            solicitante = solicitud.getSolicitante();
            ValoracionPredialDTO valoracion;// debo elimiinar
            parametros = new HashMap<>();
            parametros.put("solicitudExoneracion", solicitud.getId());
            prediosSolicitud = findAll(FnSolicitudExoneracionPredios.class,
                    parametros);
            if ((prediosSolicitud == null || prediosSolicitud.isEmpty()) && solicitud.getPredio() != null) {
                FnSolicitudExoneracionPredios ps = new FnSolicitudExoneracionPredios();
//                ps.setPredio(solicitud.getPredio());
                ps.setSolicitudExoneracion(solicitud);
                save(ps);
                prediosSolicitud = findAll(FnSolicitudExoneracionPredios.class,
                        parametros);
            }
            switch (exoneracionClase.getId().intValue()) {
                case 2:
                    //Rebaja del recargo por solar no edificado. SOLO AFECTA EL VALOR DEL RUBRO 9.
                    //EN EL SAC GUARDA ESE VALOR DEL RUBRO EN LA COLUMNA DE EXONERACION DE PU_CATASTRO
                    //E INSERTA UN REGISTRO EN LA TABLA PU_PREDIOS_DADOS_DE_BAJA
                    /*HENRY : TODOS LOS TIPOS QUE PERTENECEN A ESTA CLASE REALIZAN EL MISMO PROCESO
                     NO NECESITA PARAMETRO DE VALORES (NO LOS CONSIDERA)
                     */
                    for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                        for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
                            if (ps.getPredio() != null) {
                                if (ps.getPredio() != null) {
                                    if (validarAplicarExoneracion(ps.getCodLocal(), null, null, i, tipoPredio, exoneracionTipo.getId())) {
                                        liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                        liquidacionExonerada = clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, liquidacion.getEstadoLiquidacion().getId());
                                        totalPagoExneracion = generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, null, null, Boolean.FALSE, exoneracionTipo);
                                        liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
                                        liquidacionExonerada.setTotalPago(totalPagoExneracion);
                                        liquidacionExonerada.setSaldo(totalPagoExneracion);
                                        liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                                        liquidacionExonerada.setValorExoneracion(liquidacion.getTotalPago().subtract(totalPagoExneracion));
                                        liquidacionExonerada.setExoneracionDescripcion("EXONERACIÒN: " + solicitud.getExoneracionTipo().getDescripcion());
                                        save(liquidacionExonerada);
                                        exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                                    }
                                }
                            }
                        }
                    }
                    break;
                case 3:
                    /*HENRY : TODOS LOS TIPOS QUE PERTENECEN A ESTA CLASE REALIZAN EL MISMO PROCESO
                     NO NECESITA PARAMETRO DE VALORES (NO LOS CONSIDERA)
                     */
                    for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                        for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
                            if (ps.getPredio() != null) {
                                if (validarAplicarExoneracion(ps.getCodLocal(), null, null, i, tipoPredio, exoneracionTipo.getId())) {
                                    liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                    liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, liquidacion.getEstadoLiquidacion().getId());
                                    totalPagoExneracion = this.generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, liquidacion.getAvaluoSolar(), null, Boolean.TRUE, exoneracionTipo);
                                    liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
                                    liquidacionExonerada.setBandaImpositiva(liquidacion.getAvaluoSolar());
                                    liquidacionExonerada.setTotalPago(totalPagoExneracion);
                                    liquidacionExonerada.setSaldo(totalPagoExneracion);
                                    liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                                    save(liquidacionExonerada);
                                    exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                                }
                            }
                        }
                    }
                    break;
                
                case 4:
                    //Exoneración definitiva de los impuestos, solo se considera las tasas
                    /*HENRY : TODOS LOS TIPOS QUE PERTENECEN A ESTA CLASE REALIZAN EL MISMO PROCESO
                     NO NECESITA PARAMETRO DE VALORES (SI LOS INGRESA SI LOS CONSIDERA, SI NO REBAJA EL TOTAL 100%)
                     */
                    for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                        for (int i = 0; i < cantidadSolicitudes.size(); i++) {
                            //SIEMPRE LAS EMISIONES
                            if (ps.getCodLocal() != null) {
                                if (validarAplicarExoneracion(ps.getCodLocal(), null, null, i, tipoPredio, exoneracionTipo.getId())) {
                                    //13L TIPO LIQUIDACION 2L ESTADO
                                    liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), cantidadSolicitudes.get(i), tipoPredio, 2L);
                                    
                                    salario = (CtlgSalario) find("SELECT f FROM CtlgSalario f WHERE f.anio =:anio", new String[]{"anio"}, new Object[]{BigInteger.valueOf(i)});
                                    salarioMax = salario != null ? salario.getValor().multiply(new BigDecimal(500)) : new BigDecimal("0.00");
                                    avaluoCalculo = this.obtenerValorParaCalculoExoneracion(solicitud, liquidacion.getAvaluoMunicipal(), salarioMax);//metodo que revisar
//                                    liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, liquidacion.getEstadoLiquidacion().getId());//metodo que revisar

                                    totalPagoExneracion = this.generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, avaluoCalculo, null, Boolean.TRUE, exoneracionTipo);//metodo que revisar
                                    liquidacion.setEstaExonerado(Boolean.TRUE);
                                    liquidacion.setTotalPago(totalPagoExneracion);
                                    liquidacion.setSaldo(totalPagoExneracion);
                                    liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                                    update(liquidacion);
                                    exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                                }
                            }
                        }
                    }
                    break;
                case 5:
                    /*HENRY : TODOS LOS TIPOS QUE PERTENECEN A ESTA CLASE REALIZAN EL MISMO PROCESO
                     NO NECESITA PARAMETRO DE VALORES (NO LOS CONSIDERA)
                     SE DEBE REALIZAR UNA NUEVA EMISION; SE PROCESA CON LOS DATOS DEL NUEVO AVALUO
                     */
                    for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                        for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
                            //SIEMPRE LAS EMISIONES
                            if (ps.getPredio() != null) {
                                predioModel = new CatPredioModel();//consumir web services predio   ps.getPredio()
                                if (validarAplicarExoneracion(ps.getCodLocal(), null, null, i, tipoPredio, exoneracionTipo.getId())) {
                                    liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                    this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.FALSE, liquidacion.getEstadoLiquidacion().getId());
                                    valoracion = getEmisionPredial(username, i, predioModel.getNumPredio(), Boolean.TRUE).get();
                                    registrarLiquidacion(valoracion, username).get();
                                    liquidacionExonerada = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                    liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
                                    liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                                    save(liquidacionExonerada);
                                    exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                                }
                            }
                        }
                    }
                    break;
                case 6: //Porcentaje sobre valor de la Hipoteca
                    /*HENRY : TODOS LOS TIPOS QUE PERTENECEN A ESTA CLASE REALIZAN EL MISMO PROCESO
                     NECESITA PARAMETRO DE VALORES (SI NO LO INGRESA SE EXONERA 100%)
                     */
                    for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                        for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
                            //SIEMPRE LAS EMISIONES
                            if (ps.getPredio() != null) {
                                predioModel = new CatPredioModel();//consumir web services predio   ps.getPredio()
                                if (validarAplicarExoneracion(ps.getCodLocal(), null, null, i, tipoPredio, exoneracionTipo.getId())) {
                                    liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                    salario = (CtlgSalario) find("SELECT f FROM CtlgSalario f WHERE f.anio =:anio", new String[]{"anio"}, new Object[]{i});
                                    salarioMax = salario != null ? salario.getValor().multiply(new BigDecimal(500)) : new BigDecimal("0.00");
                                    avaluoCalculo = this.obtenerValorParaCalculoExoneracion(solicitud, liquidacion.getAvaluoMunicipal(), salarioMax);

//                                    liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, liquidacion.getEstadoLiquidacion().getId());
                                    totalPagoExneracion = this.generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, avaluoCalculo, null, Boolean.TRUE, exoneracionTipo);
                                    if (solicitud.getTipoValor().getId().intValue() == 7) {
                                        liquidacion.setValorHipoteca(solicitud.getValor());
                                    } else {
                                        liquidacion.setValorHipoteca(liquidacion.getAvaluoMunicipal().multiply(solicitud.getValor()).divide(new BigDecimal("100")));
                                    }
                                    liquidacion.setBandaImpositiva(avaluoCalculo);
                                    liquidacion.setEstaExonerado(Boolean.TRUE);
                                    liquidacion.setTotalPago(totalPagoExneracion);
                                    liquidacion.setSaldo(totalPagoExneracion);
                                    ///VALORES PARA REPORTES
                                    liquidacion.setValorExoneracion(liquidacion.getTotalPago().subtract(totalPagoExneracion));
                                    liquidacion.setExoneracionDescripcion("EXONERACIÒN: " + solicitud.getExoneracionTipo().getDescripcion());
                                    liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                                    update(liquidacion);
                                    exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                                }
                            }
                        }
                    }
                    break;
                case 7: //Porcentaje sobre valor de la base imponible ACTUALIZADO EL 25/04/2012 LMS
                    /*HENRY : TODOS LOS TIPOS QUE PERTENECEN A ESTA CLASE REALIZAN EL MISMO PROCESO
                     NECESITA PARAMETRO DE VALORES (SI NO LO INGRESA SE EXONERA 100%)
                     */
                    tipoLiquidacionesSolicitud = findByParameter(FnSolicitudTipoLiquidacionExoneracion.class,
                            parametros);
                    
                    switch (exoneracionTipo.getId().intValue()) {
                        case 48:
                            
                            for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                                for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
                                    //SIEMPRE LAS EMISIONES
                                    if (ps.getPredio() != null) {
                                        predioModel = new CatPredioModel();//consumir web services predio   ps.getPredio()
                                        if (validarAplicarExoneracion(ps.getCodLocal(), null, null, i, tipoPredio, exoneracionTipo.getId())) {
                                            liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                            if (liquidacion != null) {
                                                liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(4L));
                                                save(liquidacion);
                                                valoracion = getEmisionPredial(username, i, predioModel.getNumPredio(), Boolean.TRUE).get();
                                                registrarLiquidacion(valoracion, username).get();
                                                liquidacionExonerada = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                                liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
                                                liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                                                save(liquidacionExonerada);
                                                exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                                            }
                                        }
                                    }
                                }
                            }
                            break;
//                        default:
//                            System.out.println("default:");
//                            List<Long> prediosExoneracion = new ArrayList();
//                            List<CatPredioModel> prediosExoneracionCat = new ArrayList();
//                            for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
//
//                                prediosExoneracion.add(ps.getPredio());
//                            }
//                            salario = (CtlgSalario) find("SELECT f FROM CtlgSalario f WHERE f.anio =:anio", new String[]{"anio"}, new Object[]{new BigInteger(Utils.getAnio(new Date()).toString())});
//                            salarioMax = salario.getValor().multiply(new BigDecimal(500));
//                            prediosExoneracionCat = exoneracionA500Remuneraciones(salarioMax, prediosExoneracion);
//                            for (Long ps : prediosExoneracion) {
//                                CatPredioModel pred = new CatPredioModel(); // ps buscar predio web Services
//                                //for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
//                                for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
//                                    //SIEMPRE LAS EMISIONES
//                                    if (ps != null) {
//                                        System.out.println("validarAplicarExoneracion");
//                                        Long tipoPredio = null;
//                                        if ((param.get("isUrbano").equals("1"))) {
//                                            tipoPredio = 13L;
//                                        } else {
//                                            tipoPredio = 7L;
//                                        }
//                                        if (validarAplicarExoneracion(pred, null, null, i, tipoPredio, exoneracionTipo.getId())) {
//                                            liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(pred, i, tipoPredio, 2L);
//                                            //esto no va
//                                            liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, liquidacion.getEstadoLiquidacion().getId());// DEBOBORRAR
//                                            ///50% PARA LA LEY DEL DISCAPACITADO
//                                            if (exoneracionTipo.getId().intValue() == 37L) {
//                                                System.out.println("exoneracionTipo.getId().intValue() == 37L" + exoneracionTipo.getId().intValue());
//                                               // liquidacionExonerada.setBandaImpositiva(pred.getAvaluoMunicipal().divide(new BigDecimal("2")));
//                                            } else {
//                                              //  liquidacionExonerada.setBandaImpositiva(pred.getAvaluoMunicipal());
//                                            }
//                                            totalPagoExneracion = this.generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, liquidacion, pred.getAvaluoMunicipal(), null, Boolean.TRUE, exoneracionTipo);
//                                            liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
//                                            liquidacionExonerada.setTotalPago(totalPagoExneracion);
//                                            liquidacionExonerada.setSaldo(totalPagoExneracion);
//                                            liquidacionExonerada.setValorExoneracion(liquidacion.getTotalPago().subtract(totalPagoExneracion));
//                                            liquidacionExonerada.setExoneracionDescripcion("EXONERACIÒN: " + solicitud.getExoneracionTipo().getDescripcion());
//                                            liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
//                                            save(liquidacionExonerada);
//                                            exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, liquidacionExonerada, solicitud, username));
//                                        }
//                                    }
//
//                                    //LIQUIDACIONES ADICIONALES
//                                    if (tipoLiquidacionesSolicitud != null && !tipoLiquidacionesSolicitud.isEmpty()) {
//                                        for (FnSolicitudTipoLiquidacionExoneracion tipoLiquidacionesSolicitud1 : tipoLiquidacionesSolicitud) {
//                                            if (validarAplicarExoneracion(pred, null, null, i, tipoLiquidacionesSolicitud1.getTipoLiquidacion().getId(), exoneracionTipo.getId())) {
//                                                liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(pred, i, tipoLiquidacionesSolicitud1.getTipoLiquidacion().getId(), 2L);
//                                                salario = (CtlgSalario) find("SELECT f FROM CtlgSalario f WHERE f.anio =:anio", new String[]{"anio"}, new Object[]{i});
//                                                salarioMax = salario != null ? salario.getValor().multiply(new BigDecimal(500)) : new BigDecimal("0.00");
//                                                for (FinaRenDetLiquidacion d : liquidacion.getRenDetLiquidacionCollection()) {
//                                                    if (d.getRubro().equals(tipoLiquidacionesSolicitud1.getRubro().getId())) {
//                                                        valor = d.getValor();
//                                                        break;
//                                                    }
//                                                }
//                                                valorCalculado = this.obtenerValorParaCalculoExoneracion(solicitud, valor, salarioMax);
//                                                liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, liquidacion.getEstadoLiquidacion().getId());
//                                                totalPagoExneracion = this.generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, liquidacionExonerada, valorCalculado, tipoLiquidacionesSolicitud1, Boolean.TRUE, null);
//                                                liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
//                                                liquidacionExonerada.setTotalPago(totalPagoExneracion);
//                                                liquidacionExonerada.setSaldo(totalPagoExneracion);
//                                                liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
//                                                save(liquidacionExonerada);
//                                                exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, liquidacionExonerada, solicitud, username));
//                                            }
//                                        }
//                                    }
//                                }
//                                //}
//                            }
//                            break;
                    }
                    break;
                case 8: //Prescripcion de titulos o dar de baja / Ley Organica pàra eñ cierre de ña crisis bancaria

                    for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                        for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
                            //SIEMPRE LAS EMISIONES
                            predio = new CatPredioModel();//
                            if (ps.getPredio() != null) {
                                predio = new CatPredioModel();//ps.getPredio() web services buscar predio
                                liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                            }
//                            if (ps.getPredioRural() != null) {
//                                CatPredioRusticoDTO catRustico = new CatPredioRusticoDTO();// web services ps.getPredioRural()
//
//                                liquidacion = getLiquidacionPorPredioRuralAnioTipoEstado(catRustico, i, 7L, 2L);
//                            }
                            if (liquidacion != null) {
                                this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.FALSE, liquidacion.getEstadoLiquidacion().getId());
                                exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                            }
                        }
                    }
                    break;
                case 9: //tipos de rebajas / Ley Organica para el cierre de la crisis bancaria

                    for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
                        for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
                            //SIEMPRE LAS EMISIONES
                            if (ps.getPredio() != null) {
                                predio = new CatPredioModel();// consumir web service ps.getPredio()
                                if (validarAplicarExoneracion(ps.getCodLocal(), null, null, i, tipoPredio, exoneracionTipo.getId())) {
                                    liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(ps.getCodLocal(), i, tipoPredio, 2L);
                                    salario = (CtlgSalario) find("SELECT f FROM CtlgSalario f WHERE f.anio =:anio", new String[]{"anio"}, new Object[]{i});
                                    salarioMax = salario != null ? salario.getValor().multiply(new BigDecimal(500)) : new BigDecimal("0.00");
                                    avaluoCalculo = this.obtenerValorParaCalculoExoneracion(solicitud, liquidacion.getAvaluoMunicipal(), salarioMax);
                                    liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, liquidacion.getEstadoLiquidacion().getId());
                                    totalPagoExneracion = this.generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, avaluoCalculo, null, Boolean.TRUE, exoneracionTipo);
                                    liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
                                    liquidacionExonerada.setTotalPago(totalPagoExneracion);
                                    liquidacionExonerada.setSaldo(totalPagoExneracion);
                                    liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                                    save(liquidacionExonerada);
                                    exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, solicitud, username));
                                }
                            }
                            
                        }
                    }
                    break;
//                case 9: //Prescripcion de titulos o dar de baja / Ley Organica pàra el cierre de la crisis bancaria
//                            //cambio de emision catastral
//                    if (exoneracionTipo.getId() == 39L) {
//                        for (Long predioTemp : predios) {
//                            CatPredioModel prdTem = new CatPredioModel(); //  web services buscar web services
//                            clase8_and_tipo38_or_clase4_or_clase9_tipo39(predioTemp, anioInicio, anioDif, null, solicitud, exoneraciones, username, null);
//                        }
//                    }
//                    ////BAJA DE TITULOS
//                    if (exoneracionTipo.getId() == 40L) {
//                        for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
//                            for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
//                                //SIEMPRE LAS EMISIONES
//
//                                if (ps.getPredio() != null) {
//                                    predio = new CatPredioModel();//ps.getPredio() web services buscar predio
//                                    liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(predio, i, 13L, 2L);
//                                }
//                                if (ps.getPredioRural() != null) {
//                                    CatPredioRusticoDTO catRustico = new CatPredioRusticoDTO();// web services ps.getPredioRural()
//                                    liquidacion = this.getLiquidacionPorPredioRuralAnioTipoEstado(catRustico, i, 7L, 2L);
//                                }
//                                if (liquidacion != null) {
//                                    this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.FALSE, liquidacion.getEstadoLiquidacion().getId());//this.clonarRenLiquidacion(liquidacion, solicitud,Boolean.FALSE);
//                                    exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, null, solicitud, username));
//                                }
//
//                            }
//                        }
//                    }
//                    ////EMISION PREDIAL
//                    if (exoneracionTipo.getId() == 41L) {
//                        for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
//                            for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
//                                //SIEMPRE LAS EMISIONES
//                                if (ps.getPredio() != null) {
//
//                                    //GENERA UNA NUEVA EMISION
//                                    predio = new CatPredioModel();//ps.getPredio() web services buscar predio
//                                    liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(predio, i, 13L, 2L);
//                                    if (liquidacion == null) {
//                                        List<Long> catPredioToAval = new ArrayList();
//                                        catPredioToAval.add(ps.getPredio());
//                                        //avaluos.generateEmisionPredial(catPredioToAval, i, i, false, username);
//                                        //REVISAR JC
//                                        //    avaluos.registrarLiquidacion(ps.getPredio(), i, username);
//                                        predio = new CatPredioModel();//ps.getPredio() web services buscar predio
//                                        liquidacion = this.getLiquidacionPorPredioAnioTipoEstado(predio, i, 13L, 2L);
//                                        exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(null, liquidacion, solicitud, username));
//
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                    break;
//                case 10: // REACTIVA UNA EMISIÓN
//                    //PAGO INDEBIDO
//                    if (exoneracionTipo.getId() == 42L) {
//                        for (FnSolicitudExoneracionPredios ps : prediosSolicitud) {
//                            for (int i = solicitud.getAnioInicio(); i <= solicitud.getAnioFin(); i++) {
//                                if (ps.getPredio() != null) {
//                                    liquidacion = (FinaRenLiquidacion) find("SELECT r FROM RenLiquidacion r WHERE r.predio = :predio AND r.anio = :anio AND r.tipoLiquidacion = :idTipoLiquidacion and r.estadoLiquidacion.id = :estadoLiquidacion ORDER BY r.fechaIngreso DESC",
//                                            new String[]{"predio", "anio", "idTipoLiquidacion", "estadoLiquidacion"}, new Object[]{ps.getPredio(), i, 13L, 1L});
//                                    if (liquidacion != null) {
//                                        liquidacionExonerada = this.clonarRenLiquidacion(liquidacion, solicitud, Boolean.TRUE, 2L);
//                                        totalPagoExneracion = this.generarDetalleRenLiquidacionObtenerTotal(solicitud, liquidacion, liquidacionExonerada, null, null, Boolean.FALSE, null);
//                                        liquidacionExonerada.setEstaExonerado(Boolean.TRUE);
//                                        liquidacionExonerada.setTotalPago(totalPagoExneracion);
//                                        liquidacionExonerada.setSaldo(totalPagoExneracion);
//                                        liquidacionExonerada.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
//                                        save(liquidacionExonerada);
//                                        exoneraciones.add(this.registrarRelacionLiquidacionDadaBajaExoneracion(liquidacion, liquidacionExonerada, solicitud, username));
//                                    }
//                                }
//
//                            }
//                        }
//                    }
//                    break;
            }
            solicitud.setNumSolSac(this.getMaxNumSolicitudExoneracion());
            solicitud.setEstado((FnEstadoExoneracion) find(FnEstadoExoneracion.class,
                    1L));
            save(solicitud);
            return exoneraciones;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "", e);
            return null;
        }
        
    }
    
    public BigInteger getMaxNumSolicitudExoneracion() {
        Object sequence = find("SELECT MAX(cp.numSolSac) FROM FnSolicitudExoneracion cp ", null, null);
        Long l;
        if (sequence == null) {
            l = 1L;
        } else {
            l = Long.parseLong(sequence.toString()) + 1L;
        }
        return new BigInteger(l.toString());
        
    }
    
    public Future<ValoracionPredialDTO> getEmisionPredial(String user, Integer periodo, BigInteger numPredio, Boolean normal) {
//        ResultadoAvaluosDTO datos = null;
//        ValoracionPredialDTO h = null;
//        Query q;
//        try {
//
//            /*
//             (CatPredioModel)find(select e from CatPredio e where e.numPredio = :numPredio and e.estado = 'A', new String[]{"numPredio"}, new Object[]{numPredio});
//
//             */
//            CatPredioModel cp = new CatPredioModel();
//            if (cp != null) {
//                //genera los calculos del predio
//                if (cp.getBloque() == 0 && cp.getPiso() == 0 && cp.getUnidad() == 0) {
//                    //CALCULO DEL PREDIO NORMAL
//                    ValoracionPredialDTO dc = getDatosPredioBase(periodo, numPredio, user);
//                    //manager.getNativeQuery("select "+SchemasConfig.HISTORICO+".emision_individual(" + periodo + "," + dc.getNumVersion() + "," + numPredio + ")");
//                    if (dc != null) {
//                        System.out.println("Version ValoracionPredial: " + dc.getNumVersion() + " Predio " + dc.getNumPredio());
//                    } else {
//                        dc = new ValoracionPredial();
//                        dc.setNumVersion(1);
//                        dc.setNumPredio(numPredio.longValue());
//                        dc.setNumPredio(cp.getId());
//                        System.out.println("Version ValoracionPredial: " + dc);
//                    }
//                    if (normal) {
//                        try {
//                            Object x = sess.createSQLQuery("select " + SchemasConfig.HISTORICO + ".emision_individual(:n_anio,:n_codigo,:n_predio);").setParameter("n_anio", periodo).setParameter("n_codigo", Integer.parseInt(dc.getNumVersion() + "")).setParameter("n_predio", dc.getNumPredio()).uniqueResult();
//                        } catch (HibernateException | NumberFormatException hibernateException) {
//                        }
//                    } else {
//                        try {
//                            Object x = sess.createSQLQuery("select " + SchemasConfig.HISTORICO + ".emision_individual_2(:n_anio,:n_codigo,:n_predio);").setParameter("n_anio", periodo).setParameter("n_codigo", Integer.parseInt(dc.getNumVersion() + "")).setParameter("n_predio", dc.getNumPredio()).uniqueResult();
//                        } catch (HibernateException | NumberFormatException hibernateException) {
//                        }
//                    }
//                    em.getTransaction().commit();
//                    if (dc.getId() != null) {
//                        sess.refresh(dc);
//                    }
//                    if (dc.getSneAct() != null && dc.getSneAct().compareTo(BigDecimal.ZERO) > 0) {
//                        if ((dc.getAreaSolar() != null && dc.getAreaSolar().compareTo(new BigDecimal("3000")) > 0) && (cp.getSector() > 2 && cp.getSector() < 9) && (cp.getSubsector() != null && cp.getSubsector().getServicios() >= 4)) {
//                            dc.setSneAct(BigDecimal.ZERO);
//                            save(dc);
//                        }
//                    }
//                    return new AsyncResult<>(dc);
//                } else {
//                    //BUSQUEDA DEL PREDIO MATRIZ A PARTIR DEL CODIGO DEL HIJO
//
//                    CatPredioModel pm //                    CatPredio pm = (CatPredio)find(QuerysAvaluos.getPredioCodCatastral, new String[]{"sector", "mz", "mzdiv", "solar"},
//                            //                            new Object[]{cp.getSector(),
//                            //                                cp.getMz(), cp.getMzdiv(), cp.getSolar()});
//                    if (pm == null) {
//                        BigInteger codMatriz = (BigInteger) manager.getNativeQuery(QuerysAvaluos.getEnlacePh, new Object[]{numPredio});
//                        if (codMatriz != null) {
//                            pm = (CatPredio)find(QuerysAvaluos.getPredioByNumPredio, new String[]{"numPredio"}, new Object[]{codMatriz});
//                        }
//                    }
//                    if (pm != null) {
//                        if (cp.getAlicuotaUtil().compareTo(BigDecimal.ZERO) > 0) {
//                            ValoracionPredial vpm = getDatosPredioBase(periodo, pm.getNumPredio(), user);
//                            if (vpm != null) {
//                                vpm.setMatriz(Boolean.TRUE);
//                                h = new ValoracionPredial();
//                                h.setNumPredio(numPredio.longValue());
//                                h.setNumVersion(vpm.getNumVersion());
//                                h.setPeriodo(periodo);
//                                h.setNumeroMatriz(new BigInteger(vpm.getNumPredio() + ""));
//                                h.setAlicuota(cp.getAlicuotaUtil().divide(new BigDecimal("100")));
//                                h.setCantServicios(cp.getSubsector().getServicios().intValue());
//                                h.setSubsector(cp.getSubsector().getSector().intValue());
//                                h.setValorM2Subsector(cp.getSubsector().getValorM2());
//                                h.setAreaSolar(pm.getAreaSolar().multiply(cp.getAlicuotaUtil().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
//                                h.setAvaluoSolar(vpm.getAvaluoSolar().multiply(cp.getAlicuotaUtil().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
//                                if (vpm.getAreaConstruccion() != null) {
//                                    h.setAreaConstruccion(vpm.getAreaConstruccion().multiply(cp.getAlicuotaUtil().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
//                                }
//                                try {
//                                    h.setAvaluoEdificacion(vpm.getAvaluoEdificacion().multiply(cp.getAlicuotaUtil().divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP));
//                                } catch (Exception e) {
//                                }
//                                try {
//                                    h.setAvaluoMunicipal(h.getAvaluoSolar().add(h.getAvaluoEdificacion()).setScale(2, RoundingMode.HALF_UP));
//                                } catch (Exception e) {
//                                }
//                                h.setIdPredio(cp.getId());
//                                h.setMatriz(Boolean.FALSE);
//                                h.setClaveMz(cp.getClaveCat());
//                                //h.setContribuyente(cp.getNombrePropietarios());
//                                h.setFecCre(new Date());
//                                h.setUsrCre(user);
//                                h.setCodigoCatastral(cp.getCodigoPredialCompletoFormatoG());
//                                h = (ValoracionPredial) sess.merge(h);
//                                if (h.getId() != null) {
//                                    sess.persist(vpm);
//                                    sess.flush();
//                                    em.getTransaction().commit();
//                                    this.actualizarDatosVersion(h, normal);
//                                    sess.refresh(h);
//                                    em.getTransaction().commit();
//                                    if (h.getSneAct() != null && h.getSneAct().compareTo(BigDecimal.ZERO) > 0) {
//                                        if ((h.getAreaSolar() != null && h.getAreaSolar().compareTo(new BigDecimal("3000")) > 0) && (cp.getSector() > 2 && cp.getSector() < 9) && (cp.getSubsector() != null && cp.getSubsector().getServicios() >= 4)) {
//                                            h.setSneAct(BigDecimal.ZERO);
//                                            save(h);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println(e);
//        }
        return new AsyncResult<>(null);
    }
    
    public List<FinaRenLiquidacion> liquidacionesPermitidas(List<FinaRenLiquidacion> renLiquidacions) throws ParseException {
        List<FinaRenLiquidacion> liquidacions = new ArrayList<>();
        Date fechaMaximaRemision = new SimpleDateFormat("dd-MM-yyyy").parse(SisVars.fechaMaximaRemisionInteres);
        Date periodoLectura;
        String periodo = "";
        //Lectura l = null;
        for (FinaRenLiquidacion rl : renLiquidacions) {
            switch (rl.getTipoLiquidacion().getId().intValue()) {
                case 13:
                    if (rl.getAnio() <= 2017) {
                        liquidacions.add(rl);
                    }
                    break;
                case 275:
                    //      l = (Lectura)find(QuerysAguaPotable.getLecturasByLiquidacion, new String[]{"liquidacion"}, new Object[]{rl.getId()});
                    //   periodo = "30-" + l.getMes().toString() + "-" + l.getAnio().toString();
                    periodoLectura = new SimpleDateFormat("dd-MM-yyyy").parse(periodo);
                    if (periodoLectura.before(fechaMaximaRemision)) {
                        liquidacions.add(rl);
                    }
                    break;
                
                default:
                    if (rl.getAnio() <= 2017) {
                        liquidacions.add(rl);
                    }
                    break;
            }
        }
        
        return liquidacions;
    }

//    public FnSolicitudExoneracion saveFnRemisionSolicitudProceso(List<FinaRenLiquidacion> liquidaciones) {
//        try {
//            liquidaciones = liquidacionesPermitidas(liquidaciones);
//        } catch (ParseException ex) {
//            System.err.println(ex);
//        }
//        BigDecimal valorInteres = BigDecimal.ZERO;
//        BigDecimal valorRecargo = BigDecimal.ZERO;
//        BigDecimal valorMulta = BigDecimal.ZERO;
//        BigDecimal valorTotalPago = BigDecimal.ZERO;
//        BigDecimal valorTotalRemision = BigDecimal.ZERO;
//
//        FnSolicitudExoneracion fnRemisionSolicitud;
//        FnExoneracionLiquidaciones fnRemisionLiquidacion;
//
//        List<FnExoneracionLiquidaciones> fnRemisionInteresLiquidacions;
//        try {
//
//            fnRemisionInteresLiquidacions = new ArrayList<>();
//            fnRemisionLiquidacion = null;
//            for (FinaRenLiquidacion rl : liquidaciones) {
//                fnRemisionLiquidacion = new FnExoneracionLiquidaciones();
//                fnRemisionLiquidacion.setEstado(Boolean.FALSE);
//                fnRemisionLiquidacion.setFechaAplicacion(new Date());
//            //    fnRemisionLiquidacion.setUsuarioAplicacion(new Usuarios(session.getUserId()));
//                fnRemisionLiquidacion.setLiquidacion(rl);
//                fnRemisionLiquidacion.setInteres(rl.getInteres());
//                fnRemisionLiquidacion.setMultas(BigDecimal.ZERO);
//                fnRemisionLiquidacion.setRecargo(rl.getRecargo());
//                fnRemisionInteresLiquidacions.add(fnRemisionLiquidacion);
//                valorInteres = valorInteres.add(rl.getInteres());
//                valorRecargo = valorRecargo.add(rl.getRecargo());
//                valorTotalPago = valorTotalPago.add(rl.getTotalPago());
//            }
//            valorTotalRemision = valorInteres.add(valorRecargo);
//            fnRemisionSolicitud = new FnSolicitudExoneracion();
//            if (liquidaciones.get(liquidaciones.size() - 1).getComprador() != null) {
//                fnRemisionSolicitud.setSolicitante(liquidaciones.get(liquidaciones.size() - 1).getComprador());
//            }
//            fnRemisionSolicitud.setMultas(valorMulta);
//            fnRemisionSolicitud.setInteres(valorInteres);
//            fnRemisionSolicitud.setRecargo(valorRecargo);
//            fnRemisionSolicitud.setTotalRemision(valorTotalRemision);
//            fnRemisionSolicitud.setTotalPago(valorTotalPago);
//            fnRemisionSolicitud.setFechaAprobacion(new Date());
//            fnRemisionSolicitud.setEstado(new FnEstadoExoneracion(2L));
//
//            Date fechaPagoMaximo = new SimpleDateFormat("dd-MM-yyyy").parse(SisVars.fechaPublicacionOrdenanza);
//
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(fechaPagoMaximo);
//            cal.add(Calendar.MONTH, 3);
//
//            fechaPagoMaximo = cal.getTime();
//
//            CtlgItemDTO tipoSolicitud = null;
//
//            if (Utils.isNotEmpty(fnRemisionInteresLiquidacions)) {
//                FinaRenLiquidacion liquidacion = fnRemisionInteresLiquidacions.get(0).getLiquidacion();
//                ///VERIFICA A QUE TIPO SE LE  DARA LA  REMISION : CUENTAS  -  PREDIO - LIQUIDACION EN GENERAL
//            }
//         //   fnRemisionSolicitud.setTramiteTipo(tipoSolicitud);
//            fnRemisionSolicitud.setFechaIngreso(new Date());
//            fnRemisionSolicitud.setUsuarioCreacion(session.getNameUser());
//            fnRemisionSolicitud.setFechaPagoMaximo(fechaPagoMaximo);
//            fnRemisionSolicitud.setExoneracionTipo(new FnExoneracionTipo(60L));
//
//            fnRemisionSolicitud = (FnSolicitudExoneracion) save(fnRemisionSolicitud);
//
//            for (FnExoneracionLiquidaciones remisionLiquidacion : fnRemisionInteresLiquidacions) {
//                remisionLiquidacion.setExoneracion(fnRemisionSolicitud);
//                save(remisionLiquidacion);
//            }
//        } catch (Exception e) {
//            System.err.println(e);
//            return null;
//        }
//        return fnRemisionSolicitud;
//
//    }
    @Asynchronous
    @AccessTimeout(-1)
    public Future<Boolean> registrarLiquidacion(ValoracionPredialDTO v, String user) {
        try {
            if (v == null) {
                
                new AsyncResult<>(Boolean.FALSE);
            }
            if (v.getIdPredio() == null) {
                
                new AsyncResult<>(Boolean.FALSE);
            }
            BigDecimal totalPago = BigDecimal.ZERO;
            FinaRenLiquidacion liq = new FinaRenLiquidacion();
            CatPredioModel predio = buscarPredio(v.getIdPredio());
            liq.setAnio(v.getPeriodo());
            FinaRenTipoLiquidacion tipo = find(FinaRenTipoLiquidacion.class,
                    13L);
//            liq.setPredio(predio);
            liq.setAreaTotal(v.getAreaSolar());
            liq.setAvaluoSolar(v.getAvaluoSolar());
            liq.setAvaluoConstruccion(v.getAvaluoEdificacion());
            liq.setAvaluoMunicipal(v.getAvaluoMunicipal());
            liq.setTipoLiquidacion(tipo);
            liq.setEstadoCoactiva(1);
            liq.setEstadoLiquidacion(find(FinaRenEstadoLiquidacion.class,
                    2L));
            liq.setFechaIngreso(new Date());
            liq.setNombreComprador(predio.getNombrePosesionario());
            liq.setNumComprobante(BigInteger.ZERO);
            liq.setNumLiquidacion(getMaxSecuenciaTipoLiquidacion(v.getPeriodo(), tipo.getId()));
            liq.setIdLiquidacion(tipo.getPrefijo() + "-" + Utils.completarCadenaConCeros(liq.getNumLiquidacion().toString(), 6));
            liq.setBandaImpositiva(v.getBandaImpositiva());
            liq.setEstaExonerado(Boolean.FALSE);
            liq.setCoactiva(Boolean.FALSE);
            liq.setUsuarioIngreso(v.getUsrCre());
            FinaRenLiquidacion c = (FinaRenLiquidacion) save(liq);
            if (c != null) {
                v.setLiquidacion(c.getId());
                
                predio.setAvaluoSolar(v.getAvaluoSolar());
                predio.setAvaluoConstruccion(v.getAvaluoEdificacion());
                predio.setAvaluoMunicipal(v.getAvaluoMunicipal());
                save(predio);
                
                if (v.getIpAct() != null) {
                    FinaRenDetLiquidacion dt = new FinaRenDetLiquidacion(v.getIpAct(), c, ((FinaRenRubrosLiquidacion) find(getRubrosLiquidacionTipoLiqCodRubro, new String[]{"tipo", "rubro"}, new Object[]{tipo.getId(), 1L})).getId());//PREDIOS URBANOS
                    save(dt);
                    totalPago = totalPago.add(v.getIpAct());
                }
                if (v.getEmisionAct() != null) {
                    FinaRenDetLiquidacion em = new FinaRenDetLiquidacion(v.getEmisionAct(), c, ((FinaRenRubrosLiquidacion) find(getRubrosLiquidacionTipoLiqCodRubro, new String[]{"tipo", "rubro"}, new Object[]{tipo.getId(), 10L})).getId());
                    save(em);
                    totalPago = totalPago.add(v.getEmisionAct());
                }
                if (v.getRbAct() != null) {
                    FinaRenDetLiquidacion rb = new FinaRenDetLiquidacion(v.getRbAct(), c, ((FinaRenRubrosLiquidacion) find(getRubrosLiquidacionTipoLiqCodRubro, new String[]{"tipo", "rubro"}, new Object[]{tipo.getId(), 11L})).getId());
                    save(rb);
                    totalPago = totalPago.add(v.getRbAct());
                }
                if (v.getTasaMant() != null) {
                    FinaRenDetLiquidacion tm = new FinaRenDetLiquidacion(v.getTasaMant(), c, ((FinaRenRubrosLiquidacion) find(getRubrosLiquidacionTipoLiqCodRubro, new String[]{"tipo", "rubro"}, new Object[]{tipo.getId(), 17L})).getId());
                    save(tm);
                    totalPago = totalPago.add(v.getTasaMant());
                }
                try {
                    if (v.getAreaSolar() != null && v.getAreaSolar().compareTo(new BigDecimal("3000")) <= 0) {
                        if (v.getAreaConstruccion() == null || v.getAreaConstruccion().compareTo(BigDecimal.ZERO) == 0) {
                            if (v.getSneAct() != null && v.getNumeroMatriz() == null && v.getCantServicios() > 4
                                    && (v.getAvaluoEdificacion() == null || v.getAvaluoEdificacion().compareTo(BigDecimal.ZERO) <= 0) && predio.getSector() > 2
                                    && predio.getSector() < 9 && v.getSneAct().compareTo(BigDecimal.ZERO) > 0) {
                                FinaRenDetLiquidacion sne = new FinaRenDetLiquidacion(v.getSneAct(), c, ((FinaRenRubrosLiquidacion) find(getRubrosLiquidacionTipoLiqCodRubro, new String[]{"tipo", "rubro"}, new Object[]{tipo.getId(), 9L})).getId());
                                save(sne);
                                totalPago = totalPago.add(v.getSneAct());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error al setear sne_act " + e.getMessage());
                }
                HashMap<String, Object> params = new HashMap();
                params.put("idPredio", v.getIdPredio());
                params.put("anio", v.getPeriodo());
                BigDecimal mejoras = (BigDecimal) grabarMejora(params);
                if (mejoras != null) {
                    v.setRmjAct(mejoras);
                    totalPago = totalPago.add(mejoras);
                }
                save(v);
                try {
                    c.setTotalPago(totalPago);
                    c.setSaldo(totalPago);
                    save(c);
                } catch (Exception e) {
                    System.out.println("error al setear el pagoTotal " + e.getMessage());
                }
            }
            //}
        } catch (Exception e) {
            System.out.println("err " + e);
        }
        return new AsyncResult<>(Boolean.FALSE);
    }
    
    public RecActasEspeciesDet getActaByEspecieYUser(Long especie, Long user) {
        try {
            return (RecActasEspeciesDet) findUnique("select es from RecActasEspeciesDet es where es.especie = :idEspecie and es.acta.usuarioAsignado = :idUser and es.disponibles > 0 and es.estado = 'A' order by es.acta.fechaIngreso", new String[]{"idEspecie", "idUser"}, new Object[]{especie, user});
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public List<RenParametrosInteresMulta> getMultasGeneral(String tipo, boolean estado, Long rubro) {
        
        return (List<RenParametrosInteresMulta>) em.createQuery("SELECT r from RenParametrosInteresMulta r where r.estado=:estado and r.tipo=:tipo and r.tipoLiquidacion.id=:id")
                .setParameter("estado", estado).setParameter("tipo", tipo).setParameter("id", rubro)
                .getResultList();
    }
    
    public Object grabarMejora(Map<String, Object> parametros) {
        return executeFunction("mejoras.generar_mejora", parametros, Boolean.FALSE);
    }
    
    public RenDesvalorizacion getDesvalorizacionAnio(Integer anio) {
        Map<String, Object> par = new HashMap<>();
        par.put("anio", anio);
        par.put("estado", true);
        return findByParameter(RenDesvalorizacion.class,
                par);
    }
    
    public Object executeFunction(String function, Map<String, Object> paramt, Boolean tipoVoid) {
        
        Object o = null;
        try {
            String parametros = "";
            if (paramt != null) {
                for (Map.Entry<String, Object> entrySet : paramt.entrySet()) {
                    parametros = parametros + entrySet.getValue() + ",";
                }
                parametros = parametros.substring(0, parametros.length() - 1);
            }
            //System.out.println("/*** PARAMETROS: " + parametros);

            getEntityManager().getTransaction();
            String sq = "SELECT " + function + "(" + parametros + ");";
            System.out.println("query>>" + sq);
            if (tipoVoid) {
                em.createNativeQuery(sql).getResultList();
                
            } else {
                if (em.createNativeQuery(sql).getResultList() != null) {
                    o = em.createNativeQuery(sql).getResultList().get(0);
                }
                
            }
//            getEntityManager().getTransaction().commit();
//            getEntityManager().flush();
        } catch (Exception e) {
            System.out.println(e);
        }
        return o;
    }
    
    public AppDepartamentoDetalleTitulos savePlanificacionTitulo(AppDepartamentoDetalleTitulos catPlanificacionTitulos) {
        AppDepartamentoDetalleTitulos cpt = null;
        try {
            if (catPlanificacionTitulos != null) {
                if (catPlanificacionTitulos.getId() != null) {
                    update(catPlanificacionTitulos);
                    return find(AppDepartamentoDetalleTitulos.class,
                            catPlanificacionTitulos.getId());
                } else {
                    catPlanificacionTitulos.setNumRecibo(getMaxNumReciboCatPlanificacionTitulos());
                    cpt = (AppDepartamentoDetalleTitulos) save(catPlanificacionTitulos);
                    return cpt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cpt;
    }
    
    public Object executeFunction(String function, List<Object> paramt, Boolean tipoVoid) {
        Object o = null;
        try {
            String parametros = "";
            if (paramt != null) {
                for (Object obj : paramt) {
                    parametros = parametros + obj.toString() + ",";
                }
                parametros = parametros.substring(0, parametros.length() - 1);
            }

            //sess.beginTransaction();
            String query = "SELECT " + function + "(" + parametros + ");";

            //      getEntityManager().getTransaction();
            if (tipoVoid) {
                
                em.createNativeQuery(query).getResultStream().findFirst().orElse(null);
            } else {
                o = em.createNativeQuery(query).getResultStream().findFirst().orElse(null);
                
            }
            //sess.getTransaction().commit();
            //sess.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
    
    public Object executeFunctionPredial(String function, Object param1, Object param2, Object param3, Object param4, Object param5, Boolean tipoVoid, Object param6, Object param7) {
        Object o = null;
        try {
            
            String query = "select " + function + "(" + param1 + ",'" + param2 + "'," + param3 + "," + param4 + "," + param5 + "," + param6 + "," + param7 + ")";

            //      getEntityManager().getTransaction();
            if (tipoVoid) {
                
                em.createNativeQuery(query).getResultStream().findFirst().orElse(null);
            } else {
                o = em.createNativeQuery(query).getResultStream().findFirst().orElse(null);
                
            }
            //sess.getTransaction().commit();
            //sess.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
    
    public Object valorExecutefinl(String function, List<Object> paramt, Boolean tipoVoid) {
        Object o = executeFunction(function, paramt, tipoVoid);
        if (o == null) {
            return null;
        } else {
            return o;
        }
    }
    
    public Map<String, Object> licenciaUnicaNewRenovacion(FinaRenLocalComercial fina, FinaRenRubrosLiquidacion rubro) {
        List<FinaRenLiquidacion> result = (List<FinaRenLiquidacion>) em.createQuery("SELECT l FROM FinaRenDetLiquidacion d INNER JOIN d.liquidacion l where d.rubro=:rubro and l.localComercial=:local")
                .setParameter("rubro", rubro).setParameter("local", fina).getResultList();
        boolean tipo = false;
        int vecesMulta = 0;
        if (result.isEmpty()) {
            tipo = false;
        } else {
            tipo = true;
        }
        
        List<FinaRenDetLiquidacion> resultado = (List<FinaRenDetLiquidacion>) em.createQuery("SELECT d FROM FinaRenDetLiquidacion d INNER JOIN d.liquidacion l where d.rubro=:rubro and l.localComercial=:local")
                .setParameter("rubro", rubro).setParameter("local", fina).getResultList();
        for (FinaRenDetLiquidacion item : resultado) {
            if (item.getValor().doubleValue() > 0) {
                vecesMulta++;
            }
            
        }
        
        param = new HashMap<>();
        
        param.put("renovacion", tipo);
        param.put("vecesMulta", vecesMulta);
        return param;
    }

    //REPROGRAMAR CON LOS WEB SERVICES
    public FinaRenLiquidacion getDeudasPredioAnioActual(CatPredioModel predio) {
        sql = "select e from RenLiquidacion e where e.anio = :anio and e.tipoLiquidacion.id = :tipo and e.predio.numPredio = :predio and e.estadoLiquidacion.id in (2)";
        FinaRenLiquidacion liquidacion = (FinaRenLiquidacion) find(sql, new String[]{"anio", "tipo", "predio"}, new Object[]{Utils.getAnio(new Date()), 13L, predio.getNumPredio()});
//        if (liquidacion != null) {
//            if (liquidacion.getPredio() != null) {
//                if (liquidacion.getPredio().getPropiedad() != null) {
//                    if (!liquidacion.getPredio().getPropiedad().getNombre().equals("PUBLICO")
//                            || !liquidacion.getPredio().getPropiedad().getNombre().equals("JURISDICCIÓN MUNICIPAL")
//                            || !liquidacion.getPredio().getPropiedad().getNombre().equals("ESTADO CENTRAL")) {
//                        return liquidacion;
//                    } else {
//                        return null;
//                    }
//                }
//            }
//        }
        return liquidacion;
    }
    
    public BigInteger getMaxNumReciboCatPlanificacionTitulos() {
        BigInteger numRecibo = (BigInteger) em.createQuery("SELECT MAX(cp.numRecibo) from AppDepartamentoDetalleTitulos cp").getResultStream().findFirst().orElse(0L);
        
        if (numRecibo == null) {
            return BigInteger.ONE;
        } else {
            return numRecibo.add(BigInteger.valueOf(1));
        }
        
    }
    
    public Long asignados(String user) {
        return (Long) em.createQuery("SELECT COALESCE(count(c.valiadorAsignado),0) FROM Cliente c where c.valiadorAsignado.usuario=:usuario")
                .setParameter("usuario", user).getResultStream().findFirst().orElse(0L);
    }
    
    public Cliente getNombresUsuarios(String user) {
        List<Cliente> result = (List<Cliente>) em.createQuery("SELECT p from Usuarios u inner join u.funcionario f inner join f.persona p where u.usuario=:usuario")
                .setParameter("usuario", user).getResultList();
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }
    
    public List<?> getCanton(Long idCanton) {
        param = new HashMap<>();
        param.put("idCanton.id", 81L);
        return findAll(CatParroquia.class, param);
    }
    
    public List<?> listBuscarZona(CatParroquia codigoParroquia) {
        sql = "SELECT DISTINCT p.zona FROM CatPredio p WHERE p.parroquia =:parroquia AND p.estado = 'A'";
        param = new HashMap<>();
        param.put("parroquia", codigoParroquia.getCodigoParroquia().shortValue());
        return findAllQuery(sql, param);
        
    }
    
    public List<?> listBuscarSector(String codigoParroquia, String zona) {
        sql = "SELECT DISTINCT p.sector FROM CatPredio p WHERE p.parroquia =:parroquia AND p.zona =:zona AND p.estado = 'A'";
        param = new HashMap<>();
        param.put("parroquia", Short.valueOf(codigoParroquia));
        param.put("zona", Short.valueOf(zona));
        return findAllQuery(sql, param);
        
    }
    
    public List<?> listBuscarMZSector(String codigoParroquia, String zona, String sector) {
        
        sql = "SELECT DISTINCT p.mz FROM CatPredio p WHERE p.parroquia =:parroquia AND p.zona =:zona AND p.sector =:sector AND p.estado = 'A'";
        param = new HashMap<>();
        param.put("parroquia", Short.valueOf(codigoParroquia));
        param.put("zona", Short.valueOf(zona));
        param.put("sector", Short.valueOf(sector));
        return findAllQuery(sql, param);
    }
    
    public List<?> listBuscarLotesMz(String codigoParroquia, String zona, String sector, String mz) {
        // "";
        sql = "SELECT  p.solar FROM CatPredio p WHERE p.parroquia =:parroquia AND p.zona =:zona AND p.sector =:sector AND p.mz =:mz AND p.estado = 'A'";
        param = new HashMap<>();
        param.put("parroquia", Short.valueOf(codigoParroquia));
        param.put("zona", Short.valueOf(zona));
        param.put("sector", Short.valueOf(sector));
        param.put("mz", Short.valueOf(mz));
        return findAllQuery(sql, param);
    }
    
    public CatPredio getPredioNumPredio(Long numPredio) {
        List<CatPredio> p = null;
        try {
            p = (List<CatPredio>) em.createQuery("SELECT cp1 FROM CatPredio cp1 WHERE cp1.numPredio = :numPredio")
                    .setParameter("numPredio", BigInteger.valueOf(numPredio)).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
        if (p != null) {
            return p.get(0);
        }
        return null;
    }
    
    public CatPredio getPredioNumPredio(Long numPredio, String tipo) {
        List<CatPredio> p = new ArrayList<>();
        
        if (tipo.equals("1")) {
            p = predioService.findByNamedQuery("CatPredio.findByCodigoPredioAndTipo", new Object[]{numPredio.toString(), "U"});
            
        } else if (tipo.equals("2")) {
            p = predioService.findByNamedQuery("CatPredio.findByCodigoPredioAndTipo", new Object[]{numPredio.toString(), "R"});
        }
        
        if (p != null && !p.isEmpty()) {
            p.get(0).setNumPredio(BigInteger.valueOf(numPredio));
            return p.get(0);
        }
        return null;
    }
    
    public List<CatPredio> preidosPropietarios(Cliente ente, String estado) {
        try {
            return (List<CatPredio>) em.createQuery("SELECT p.predio FROM CatPredioPropietario p where p.ente=:ente AND p.estado=:estado")
                    .setParameter("ente", ente).setParameter("estado", estado).getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "preidosPropietarios( ente,  estado)", e);
            return null;
        }
        
    }
    
    public CatPredio getPredioByClaveCat(String claveCat) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("claveCat", claveCat);
            return findByParameter(CatPredio.class, paramt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /*
      public FinaRenPatente buscarCiRucPatentePermiso(String ci) {
        param = new HashMap<>();
        param.put("ciRuc", ci);
        return (FinaRenPatente) findByParameter(FinaRenPatente.class, param);
    }

     */
    public FinaRenLiquidacion getLiquiPredio(Long cp) {
        
        try {
            param = new HashMap<>();
            param.put("predio", cp);
            param.put("tipoLiquidacion", 183L);
            return (FinaRenLiquidacion) findByParameter(FinaRenLiquidacion.class, param);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    public List<FnConvenioPago> getConvenios(String s) {
        Pattern pat = Pattern.compile("[a-zA-Z]*$");
        Matcher mat = pat.matcher(s);
        System.out.println("valor de esa wea--->  " + mat.toString());
        if (mat.matches()) {
            System.out.println("entro aqui........para volver como null----->  " + s);
            return null;
        }
        System.out.println("entro aqui........>" + s);
        String[] parts = s.split("\\.");
        
        System.out.println("SECTOR  ---->>  " + parts[1]);
//        p1=parts[1];
//        System.out.println("---->>>  "+p1);
//        String[] parts2 = p1.split(".");
//        p2 = parts[0];
        String x = parts[1];
        short y = Short.parseShort(x);
//        

//        return (List<CatPredio>) em.createQuery("SELECT p.predio FROM CatPredioPropietario p where p.ente=:ente AND p.estado=:estado")
//                    .setParameter("ente", ente).setParameter("estado", estado).getResultList();
        try {
            
            return (List<FnConvenioPago>) em.createQuery("SELECT cv FROM FnConvenioPago cv LEFT JOIN cv.contribuyente c LEFT JOIN cv.predio p WHERE p.sector=:sector")
                    .setParameter("sector", y).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public CatPredio getPredioByClaveCat(String claveCat, String tipo) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            
            if (tipo.equals("1") || tipo.equals("U")) {
                System.out.println("urbano");
                paramt.put("claveCat", claveCat);
                paramt.put("tipoPredio", "U");
                return findByParameter(CatPredio.class, paramt);
            } else if (tipo.equals("R")) {
                System.out.println("rural");
                paramt.put("claveCat", claveCat);
                paramt.put("tipoPredio", "R");
                return findByParameter(CatPredio.class, paramt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public CatPredio getPredioByClaveCatAnt(String claveCatAnt) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("predialant", claveCatAnt);
            
            return findByParameter(CatPredio.class, paramt);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public CatPredio getPredioByClaveCatAnt(String claveCatAnt, String tipo) {
        try {
            Map<String, Object> paramt = new HashMap<>();
            
            if (tipo.equals("1")) {
                paramt.put("predialant", claveCatAnt);
                paramt.put("tipoPredio", "U");
                return findByParameter(CatPredio.class, paramt);
            } else if (tipo.equals("2")) {
                paramt.put("predialant", claveCatAnt);
                paramt.put("tipoPredio", "R");
                return findByParameter(CatPredio.class, paramt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public CatPredioModel buscarPredio(Long id) {
        CatPredio cat = find(CatPredio.class, id);
        CatPredioModel tmp = new CatPredioModel();
        if (tmp != null) {
            tmp.setId(cat.getId());
            
        }
        
        return tmp;
    }
    
    public PredioModelWs buscarPredioWs(String clave, int tipo) {
        return new PredioModelWs();
    }
    
    public FnExoneracionClase guadModExoneracion(FnExoneracionClase exoneracion) {
        try {
            final List<FnExoneracionTipo> list = (List<FnExoneracionTipo>) exoneracion.getFnExoneracionTipoList();
            exoneracion.setFnExoneracionTipoList(null);
            if (exoneracion.getId() == null) {
                exoneracion = (FnExoneracionClase) save(exoneracion);
            } else {
                update(exoneracion);
            }
            if (Utils.isNotEmpty(list)) {
                exoneracion.setFnExoneracionTipoList(new ArrayList<FnExoneracionTipo>());
                for (FnExoneracionTipo l : list) {
                    if (l.getId() == null) {
                        l.setExoneracionClase(exoneracion);
                        exoneracion.getFnExoneracionTipoList().add((FnExoneracionTipo) save(l));
                    } else {
                        update(exoneracion);
                        exoneracion.getFnExoneracionTipoList().add(l);
                    }
                }
            }
            exoneracion.setFnExoneracionTipoList(list);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
            return null;
        }
        return exoneracion;
    }
    
    public List<CatPredioPropietario> buscarPropietarios(Long id) {
        param = new HashMap<>();
        param.put("id", id);
        return findAllQuery("select c from CatPredioPropietario c where c.predio.id=:id", param);
    }
    
    public List<CatPredioPropieatrioDTO> buscarPredioPropietarios(Long id) {
        return new ArrayList<>();
    }
    
    public CatPredioModel buscarPredioUrbano(Long id) {
        return new CatPredioModel();
    }
    
    public CatPredioModel buscarCodigoCatastarl(String codigoCatastral) {
        return new CatPredioModel();
    }
    
    public CatPredioModel buscarNumPredio(BigInteger numPredio) {
        return new CatPredioModel();
    }
    
    public List<CatPredioPropieatrioDTO> buscarPredioUrbanoPropietarios(Long id) {
        return new ArrayList<>();
    }
    
    public List<CatPredioModel> getListPrediosByPropietario(Long idEnte) {
        Map<String, Object> map = new HashMap<>();
        List<CatPredioModel> predios = new ArrayList<>();
        try {
            map.put("idEnte", idEnte);
//consmir web services los predios por el ente o contribuyente
// predios = manager.findNamedQuery(Querys.getPrediosByPropietario, map);
        } catch (Exception e) {
            System.err.println(e);
        }
        return predios;
    }
    
    public List<CatPredioModel> getListPrediosByCodigoPredial(CatPredioModel model) {
        Map<String, Object> map = new HashMap<>();
        List<CatPredioModel> predios = new ArrayList<>();
        try {
            map.put("sector", model.getSector());
            map.put("mz", model.getMz());
            map.put("cdla", model.getCdla());
            map.put("mzdiv", model.getMzDiv());
            map.put("solar", model.getSolar());
            map.put("div1", model.getDiv1());
            map.put("div2", model.getDiv2());
            map.put("div3", model.getDiv3());
            map.put("div4", model.getDiv4());
            map.put("div5", model.getDiv5());
            map.put("div6", model.getDiv6());
            map.put("div7", model.getDiv7());
            map.put("div8", model.getDiv8());
            map.put("div9", model.getDiv9());
            map.put("phv", model.getPhv());
            map.put("phh", model.getPhh());
            map.put("estado", "A");
            /*
            consumir web services por el codigo predial
             */
//   predios =findObjectByParameterList(CatPredio.class, map);
        } catch (Exception e) {
            System.err.println(e);
        }
        return predios;
    }
    
    public Boolean corregirExoneracion(HistoricoTramites ht) {
        Boolean b;
        try {
            b = true;
            if (ht == null) {
                return false;
            }
            FnSolicitudExoneracion solicitud = (FnSolicitudExoneracion) find("SELECT f.id FROM FnSolicitudExoneracion f WHERE f.exoneracionTipo.id = :exoneracionTipo AND f.anioFin = :anioFin AND f.predio.id = :idPredio", new String[]{"idTramite", "estado"}, new Object[]{ht, 2L});
            if (solicitud == null) {
                return false;
            }
            solicitud.setEstado((FnEstadoExoneracion) find(FnEstadoExoneracion.class,
                    1L));
        } catch (Exception e) {
            b = false;
            LOG.log(Level.SEVERE, "", e);
        }
        
        return b;
    }
    
    public Boolean rechazarExoneracion(HistoricoTramites ht) {
        Boolean b;
        try {
            b = true;
            if (ht == null) {
                return false;
            }
            FnSolicitudExoneracion solicitud = (FnSolicitudExoneracion) find("SELECT f.id FROM FnSolicitudExoneracion f WHERE f.exoneracionTipo.id = :exoneracionTipo AND f.anioFin = :anioFin AND f.predio.id = :idPredio", new String[]{"idTramite", "estado"}, new Object[]{ht, 2L});
            if (solicitud == null) {
                return false;
            }
            solicitud.setEstado((FnEstadoExoneracion) find(FnEstadoExoneracion.class,
                    4L));
        } catch (Exception e) {
            b = false;
            LOG.log(Level.SEVERE, "", e);
        }
        
        return b;
    }
    
    public Cliente getCatEnteByParemt(Map paramt) {
        Cliente ente = (Cliente) findByParameter(Cliente.class,
                param);
        
        if (ente != null) {
            return ente;
        } else {
            ente = new Cliente();
            String ident = (String) paramt.get("ciRuc");
        }
        return null;
    }
    
    public List<CatPredioModel> getPrediosByEnte(Cliente solicitante) {
        try {
            List<CatPredioModel> listTemp = new ArrayList<>();
            Map<String, Object> paramt = new HashMap<>();
            paramt.put("ente", solicitante);
            paramt.put("estado", "A");
            List<CatPredioPropietario> pps = findByParameter(CatPredioPropietario.class,
                    paramt);
            if (Utils.isNotEmpty(pps)) {
                for (CatPredioPropietario pp : pps) {
                    if (!listTemp.contains(pp.getPredio())) {
                        listTemp.add(buscarPredio(pp.getId()));
                    }
                }
                return listTemp;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Obtener predio de solicitante", e);
        }
        return null;
    }
    
    public List<UnidadAdministrativa> getDepartamentosById(List<Long> list) {
        List<UnidadAdministrativa> d;
        try {
            d = new ArrayList<>();
            for (Long d1 : list) {
                UnidadAdministrativa de = find(UnidadAdministrativa.class,
                        d1);
                d.add(de);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return d;
    }
    
    public List<FinaRenLiquidacion> getEmisionesByPredio(Long predio, Map<String, Object> parametros) {
        List<FinaRenLiquidacion> emisiones;
        try {
            parametros.put("predio", predio);
            emisiones = findAllQuery("SELECT e FROM FinaRenLiquidacion e WHERE e.tipoLiquidacion.id=13 AND e.estadoLiquidacion.id=2 AND e.predio=:predio AND e.anio<=:anio ORDER BY e.anio ASC", parametros);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return emisiones;
    }
    
    public List<FinaRenLiquidacion> getEmisionesByPredioRustico(Long pr, Map<String, Object> parametros) {
        List<FinaRenLiquidacion> emisiones;
        try {
            parametros.put("predioRustico", pr);
            emisiones = findAllQuery("SELECT e FROM FinaRenLiquidacion e WHERE e.tipoLiquidacion.id=7 AND e.estadoLiquidacion.id=2 AND e.predioRustico=:predioRustico AND e.anio<=:anio ORDER BY e.anio ASC", parametros);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return emisiones;
    }
    
    public ValoracionPredialDTO recalcular(String usuario, Long liquidacion, Boolean normal) throws ExecutionException {
        ValoracionPredialDTO x = null;
        BigDecimal total = BigDecimal.ZERO;
        try {
            FinaRenLiquidacion rl = find(FinaRenLiquidacion.class, liquidacion);
            if (rl != null && rl.getTipoLiquidacion().getId().equals(13L) && rl.getEstadoLiquidacion().getId().equals(2L) && rl.getAnio().equals(2017)) {
                if (rl.getEstaExonerado()) {
                    return null;
                }
                FnExoneracionLiquidacion ex = (FnExoneracionLiquidacion) find("SELECT ce FROM FnExoneracionLiquidacion ce WHERE ce.liquidacionOriginal = :liquidacion",
                        new String[]{"liquidacion"}, new Object[]{rl});
                if (ex != null) {
                    return null;
                }
                FnSolicitudExoneracion exoneracion = (FnSolicitudExoneracion) find("SELECT l FROM FnSolicitudExoneracion l WHERE l.predio = :predio AND l.exoneracionTipo.id IN (17, 18, 37, 44) AND l.estado.id IN(1, 2) ORDER BY l.anioInicio DESC",
                        new String[]{"predio"}, new Object[]{rl.getPredio()});
                if (exoneracion != null) {
                    if (exoneracion.getEstado().equals(1L)) {
                        return null;
                    }
                }
                if (rl.getSaldo().compareTo(rl.getTotalPago()) == 0) {
                    CatPredioModel predio = buscarPredio(rl.getPredio().getId());
                    
                    x = this.getEmisionPredial(usuario, rl.getAnio(), predio.getNumPredio(), normal).get();
                    if (x != null) {
                        rl.setAvaluoSolar(x.getAvaluoSolar());
                        rl.setAvaluoConstruccion(x.getAvaluoEdificacion());
                        rl.setAvaluoMunicipal(x.getAvaluoMunicipal());
                        rl.setBandaImpositiva(x.getBandaImpositiva());
                        rl.setAreaTotal(x.getAreaSolar());
                        save(rl);
                        x.setLiquidacion(rl.getId());
                        save(x);

//                        rl.getPredio().setAvaluoSolar(x.getAvaluoSolar());
//                        rl.getPredio().setAvaluoConstruccion(x.getAvaluoEdificacion());
//                        rl.getPredio().setAvaluoMunicipal(x.getAvaluoMunicipal());
                        save(rl.getPredio());
                        
                        FinaRenDetLiquidacion ip = (FinaRenDetLiquidacion) find("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion and r.rubro = :rubro ORDER BY r.rubro ASC", new String[]{"liquidacion", "rubro"}, new Object[]{rl, 2L});
                        ip.setValor(x.getIpAct());
                        save(ip);
                        total = total.add(ip.getValor());
                        
                        FinaRenDetLiquidacion tm = (FinaRenDetLiquidacion) find("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion and r.rubro = :rubro ORDER BY r.rubro ASC",
                                new String[]{"liquidacion", "rubro"}, new Object[]{rl, 636L});
                        tm.setValor(x.getTasaMant());
                        save(tm);
                        total = total.add(tm.getValor());
                        
                        FinaRenDetLiquidacion rb = (FinaRenDetLiquidacion) find("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion and r.rubro = :rubro ORDER BY r.rubro ASC", new String[]{"liquidacion", "rubro"}, new Object[]{rl, 12L});
                        rb.setValor(x.getRbAct());
                        save(rb);
                        total = total.add(rb.getValor());
                        
                        FinaRenDetLiquidacion rm = (FinaRenDetLiquidacion) find("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion and r.rubro = :rubro ORDER BY r.rubro ASC", new String[]{"liquidacion", "rubro"}, new Object[]{rl, 7L});
                        if (rm != null) {
                            x.setRmjAct(rm.getValor());
                            total = total.add(rm.getValor());
                        }
                        FinaRenDetLiquidacion sne = (FinaRenDetLiquidacion) find("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion and r.rubro = :rubro ORDER BY r.rubro ASC", new String[]{"liquidacion", "rubro"}, new Object[]{rl, 10L});
                        if (sne != null) {
                            sne.setValor(x.getSneAct());
                            save(sne);
                            total = total.add(x.getSneAct());
                        } else if (x.getSneAct() != null && x.getSneAct().compareTo(BigDecimal.ZERO) > 0 && (x.getAreaSolar() != null && x.getAreaSolar().compareTo(new BigDecimal("3000")) <= 0)) {
                            
                            FinaRenDetLiquidacion snen = new FinaRenDetLiquidacion();
                            snen.setLiquidacion(rl);
                            snen.setCantidad(1);
                            snen.setValor(x.getSneAct());
                            snen.getRubro().setId(10L);
                            snen.setDesde(BigInteger.ZERO);
                            snen.setHasta(BigInteger.ZERO);
                            snen.setValorRecaudado(BigDecimal.ZERO);
                            save(snen);
                            total = total.add(x.getSneAct());
                        }
                        FinaRenDetLiquidacion em = (FinaRenDetLiquidacion) find("SELECT r FROM FinaRenDetLiquidacion r WHERE r.liquidacion = :liquidacion and r.rubro = :rubro ORDER BY r.rubro ASC", new String[]{"liquidacion", "rubro"}, new Object[]{rl, 11L});
                        if (em != null) {
                            em.setValor(x.getEmisionAct());
                            save(em);
                            total = total.add(x.getEmisionAct());
                        }
                        try {
                            rl.setTotalPago(total);
                            rl.setSaldo(total);
                            save(rl);
                            save(x);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return x;
    }
    
    public FinaRenLiquidacion realizarPagosCoactiva(List<FinaRenLiquidacion> emisiones, String cajero) {
        //SOLO CUANDO SE PAGA VARIAS EMISIONES
        Map<String, Object> map = new HashMap<>();
        List<FinaRenDetLiquidacion> detalles = new ArrayList<>();
        FinaRenLiquidacion temporal = new FinaRenLiquidacion();
        FinaRenLiquidacion liquidacion = new FinaRenLiquidacion();
        FinaRenDetLiquidacion detalle;
        Boolean coactiva = false;
        BigDecimal total = BigDecimal.ZERO;
        try {
            for (FinaRenLiquidacion li : emisiones) {
                if (li.getEstadoCoactiva() != null) {
                    if (li.getEstadoCoactiva() == 2) {
                        if (li.getTipoLiquidacion().getId() == 13L) {
                            li = this.realizarDescuentoRecargaInteresPredial(li, null);
                        }
                        li.calcularPago();
                        coactiva = true;
                        temporal = li;
                        total = total.add(li.getValorCoactiva());
                    }
                }
                
            }
            if (coactiva) {
                map.put("tipoLiquidacion", new FinaRenTipoLiquidacion(49L));  // TIPO LIQUIDACION COACTIVA
                map.put("codigoRubro", 1L);
                FinaRenRubrosLiquidacion rubro = find(FinaRenRubrosLiquidacion.class, map);
                liquidacion.setComprador(temporal.getComprador());
                liquidacion.setNombreComprador(temporal.getNombreComprador());
                liquidacion.setTotalPago(total);
                liquidacion.setAnio(Calendar.getInstance().get(Calendar.YEAR));
                liquidacion.setPredio(temporal.getPredio());
                //  liquidacion.setCuenta(temporal.getCuenta());
                liquidacion.setSaldo(total);
                liquidacion.setTipoLiquidacion(rubro.getTipoLiquidacion());
                liquidacion.setFechaIngreso(new Date());
                liquidacion.setUsuarioIngreso(cajero);
                liquidacion.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                liquidacion.setNumLiquidacion(getMaxSecuenciaTipoLiquidacion(liquidacion.getAnio(), rubro.getTipoLiquidacion().getId()));
                liquidacion.setIdLiquidacion(Utils.getAnio(new Date()) + "-" + Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6) + "-" + rubro.getTipoLiquidacion().getPrefijo());
                liquidacion.setNumComprobante(BigInteger.ZERO);
                liquidacion.setCoactiva(Boolean.FALSE);
                liquidacion.setEstadoCoactiva(1);
                liquidacion = (FinaRenLiquidacion) save(liquidacion);
                for (FinaRenLiquidacion li : emisiones) {
                    if (li.getEstadoCoactiva() == 2) {
                        detalle = new FinaRenDetLiquidacion();
                        detalle.setCantidad(li.getAnio());
                        detalle.setLiquidacion(liquidacion);
                        detalle.getRubro().setId(rubro.getId());
                        detalle.setValor(li.getValorCoactiva());
                        detalle = (FinaRenDetLiquidacion) save(detalle);
                        detalles.add(detalle);
                    }
                }
                liquidacion.setRenDetLiquidacionCollection(detalles);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return liquidacion;
    }
    
    public FinaRenActividadComercial guardarActividad(FinaRenActividadComercial actividad) {
        return (FinaRenActividadComercial) save(actividad);
    }
    
    public FinaRenLiquidacion realizarUnPagoCoactiva(FinaRenLiquidacion liquidacion, BigDecimal total, String cajero) {
        //SOLO CUANDO SE PAGA UNA SOLA EMISION
        Map<String, Object> map = new HashMap<>();
        FinaRenLiquidacion nueva = new FinaRenLiquidacion();
        FinaRenDetLiquidacion detalle;
        List<FinaRenDetLiquidacion> detalles = new ArrayList<>();
        try {
            map.put("tipoLiquidacion", new FinaRenTipoLiquidacion(49L)); // TIPO LIQUIDACION COACTIVA
            map.put("codigoRubro", 1L);
            FinaRenRubrosLiquidacion rubro = findByParameter(FinaRenRubrosLiquidacion.class, map);
            nueva.setComprador(liquidacion.getComprador());
            nueva.setNombreComprador(liquidacion.getNombreComprador());
            nueva.setTotalPago(total);
            nueva.setTipoLiquidacion(rubro.getTipoLiquidacion());
            nueva.setAnio(Calendar.getInstance().get(Calendar.YEAR));
            nueva.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
            nueva.setNumLiquidacion(getMaxSecuenciaTipoLiquidacion(nueva.getAnio(), rubro.getTipoLiquidacion().getId()));
            nueva.setIdLiquidacion(Utils.getAnio(new Date()) + "-" + Utils.completarCadenaConCeros(liquidacion.getNumLiquidacion().toString(), 6) + "-" + rubro.getTipoLiquidacion().getPrefijo());
            nueva.setNumComprobante(BigInteger.ZERO);
            nueva.setPredio(liquidacion.getPredio());
            //nueva.setCuenta(liquidacion.getCuenta());
            nueva.setSaldo(total);
            nueva.setFechaIngreso(new Date());
            nueva.setUsuarioIngreso(cajero);
            nueva.setCoactiva(Boolean.FALSE);
            nueva.setEstadoCoactiva(1);
            if (liquidacion.getValorCoactiva().compareTo(total) > 0) {
                nueva.setObservacion("ABONO");
            }
            nueva = (FinaRenLiquidacion) save(nueva);
            detalle = new FinaRenDetLiquidacion();
            detalle.setCantidad(liquidacion.getAnio());
            detalle.setLiquidacion(nueva);
            detalle.getRubro().setId(rubro.getId());
            detalle.setValor(total);
            detalle = (FinaRenDetLiquidacion) save(detalle);
            detalles.add(detalle);
            nueva.setRenDetLiquidacionCollection(detalles);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return nueva;
    }
    
    public BigDecimal valorRecaudarCoactiva(BigDecimal valorTotal) {
        BigDecimal valorCoactiva;
        try {
            valorCoactiva = valorTotal.divide(new BigDecimal("11"), 2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return valorCoactiva;
    }
    
    public List<FinaRenLocalCategoria> getCategoriasLocalesHijas(Long id) {
        param = new HashMap<>();
        param.put("padre", id);
        List<FinaRenLocalCategoria> categoria = (List<FinaRenLocalCategoria>) findAllQuery("SELECT rc FROM FinaRenLocalCategoria rc WHERE rc.padre = :padre", param);
        return categoria;
        
    }
    
    public List<FinaRenLocalCategoria> getCategoriasLocales() {
        List<FinaRenLocalCategoria> categoria = (List<FinaRenLocalCategoria>) findAllEasy("SELECT rc FROM FinaRenLocalCategoria rc WHERE rc.padre IS NULL ");
        return categoria;
        
    }
    
    public Boolean quitarLocalComercial(RenLocalComercialFoto f) {
        try {
            if (f != null) {
                f = (RenLocalComercialFoto) save(f);
                return delete(f);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public List<FinaRenLiquidacion> obtenerLiquidacionesPrediales(FinaRenTipoLiquidacion f, Long predio) {
        return (List<FinaRenLiquidacion>) em.createQuery("SELECT r FROM FinaRenLiquidacion r WHERE r.tipoLiquidacion = :tipoLiquidacion AND r.predio = :predio AND r.estadoLiquidacion IN (1, 2, 7) ORDER BY r.anio ASC")
                .setParameter("tipoLiquidacion", f).setParameter("predio", predio).getResultList();
    }
    
    public CatPredioModel getPredioModelWs(BigInteger num, String estado) {
        
        return new CatPredioModel();
    }
    
    public List<CatPredioModel> busquedaPredioDivisiones(String estado, Short sector, Short mz,
            Short provincia, Short canton, Short parroquia, Short zona, Short Solar,
            Short piso, Short unidad, Short bloque) {
        
        return new ArrayList<>();
    }
    
    public List<CatPredioModel> busquedadPredioPocosParametros(String estado, String ciudadela, String mzUrbano, String slUrbano) {
        return new ArrayList<>();
    }
    
    public List<CatPredioModel> buscarCodigoAnterio(String estado, String predioAnterior) {
        return new ArrayList<>();
    }
    
    public List<CatPredioModel> buscarClavAnterior(String estado, String claveCatastral) {
        return new ArrayList<>();
    }
    
    public Boolean verificarPagoBanco(BigInteger predio) {
        try {
            if (predio == null) {
                return Boolean.FALSE;
            }
            int anio = 0;
            param = new HashMap<>();
            param.put("numPredio", predio);
            param.put("anio", Utils.getAnio(new Date()) - 1);
            param.put("anioFin", Utils.getAnio(new Date()));
            List<ConsolidacionBanco> cbs = findAll(ConsolidacionBanco.class, param);
            
            return (cbs != null && !cbs.isEmpty());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "No vale verificar Pago Banco", e);
            return Boolean.FALSE;
        }
    }
    
    public CatPredioRusticoDTO buscarPredioRustico(String clave, Boolean estado) {
        return new CatPredioRusticoDTO();
    }
    
    public List<CatPredioRusticoDTO> buscarPrediosRustico(List<Long> predios) {
        return new ArrayList<>();
    }
    
    public CatPredioRusticoDTO buscarPredioRustico(String clave, String parroquia, boolean estado) {
        return new CatPredioRusticoDTO();
    }
    
    public List<CatPredioRusticoDTO> predioRusticoPropuietarios(String cedula) {
        return new ArrayList<>();
    }
    
    public List<CatPredioModel> buscarPredioList(List<Long> predios) {
        return new ArrayList<>();
    }
    
    public <T> List<T> getSqlQueryParametros(Class<T> clase, String query, String[] params, Object[] values) {
        List<T> result = null;
        try {
            
            org.hibernate.Query q = (org.hibernate.Query) em.createNativeQuery(query);
            for (int i = 0; i < params.length; i++) {
                q.setParameter(params[i], values[i]);
            }
            result = q.setResultTransformer(Transformers.aliasToBean(clase)).list();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public BigDecimal getValorPresupuestoInicialPim(Short anio, PresCatalogoPresupuestario item) {
        return (BigDecimal) em.createQuery("SELECT COALESCE(p.presupuestoInicial,0) FROM ProformaIngreso p WHERE p.item=:item AND p.periodo=:periodo")
                .setParameter("item", item).setParameter("periodo", anio).getResultStream().findFirst().orElse(BigDecimal.ZERO);
    }
    
    public String getPartidaIngreso(Short anio, PresCatalogoPresupuestario item) {
        return (String) em.createQuery("SELECT CONCAT(p.item.codigo,p.fuente.codFuente) FROM ProformaIngreso p WHERE p.item=:item AND p.periodo=:periodo")
                .setParameter("item", item).setParameter("periodo", anio).getResultStream().findFirst().orElse(BigDecimal.ZERO);
    }
    
    public List<RenTipoLiquidacionDepartamento> listaRubrosPadres(String usuario) {
        return (List<RenTipoLiquidacionDepartamento>) em.createQuery("SELECT r FROM RenTipoLiquidacionDepartamento r inner join r.usuario us where us.usuario=:usuarioname AND  r.estado = true AND (r.tipoLiquidacion.transaccionPadre=0 OR r.tipoLiquidacion.transaccionPadre is null)")
                .setParameter("usuarioname", usuario).getResultList();
        
    }
    
    public List<FinaRenTipoLiquidacion> listaRubrosHijos(String usuario, Long padre) {
        return (List<FinaRenTipoLiquidacion>) em.createQuery("SELECT r.tipoLiquidacion FROM RenTipoLiquidacionDepartamento r inner join r.usuario us where us.usuario=:usuarioname AND r.estado = true and r.tipoLiquidacion.transaccionPadre=:idPadre and r.tipoLiquidacion.nombreTransaccion is not null")
                .setParameter("usuarioname", usuario).setParameter("idPadre", padre).getResultList();
    }
    
    public List<FinaRenTipoLiquidacion> listaRubrosHijosAdmin(Long padre) {
        return (List<FinaRenTipoLiquidacion>) em.createQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre=:idPadre AND r.nombreTransaccion IS NOT NULL")
                .setParameter("idPadre", padre).getResultList();
    }
    
    public Boolean isAdmin(String username, String tipo) {
        
        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("select u.id from auth.usuarios u inner join auth.usuario_rol ur \n"
                + "ON ur.usuario = u.id inner join auth.rol r ON r.id = ur.rol\n"
                + "where trim(r.nombre)=?1 and u.usuario=?2").setParameter(1, tipo).setParameter(2, username).getResultList();
        if (result == null && result.isEmpty()) {
            return false;
        } else {
            return true;
        }
        
    }
    
    public Boolean isAdmin(String username) {
        
        List<BigInteger> result = (List<BigInteger>) em.createNativeQuery("select u.id from auth.usuarios u inner join auth.usuario_rol ur \n"
                + "ON ur.usuario = u.id inner join auth.rol r ON r.id = ur.rol\n"
                + "where u.usuario=?1").setParameter(1, username).getResultList();
        if (result == null && result.isEmpty()) {
            return false;
        } else {
            return true;
        }
        
    }
    
    public List<RenTipoLiquidacionDepartamento> listaRubrosPadres(String usuario, List<String> prefijo) {
        return (List<RenTipoLiquidacionDepartamento>) em.createQuery("SELECT r FROM RenTipoLiquidacionDepartamento r inner join r.usuario us where us.usuario=:usuarioname AND r.tipoLiquidacion.prefijo in (:prefi) AND r.estado = true AND (r.tipoLiquidacion.transaccionPadre=0 OR r.tipoLiquidacion.transaccionPadre is null)")
                .setParameter("usuarioname", usuario).setParameter("prefi", prefijo).getResultList();
    }
    
    public List<FnExoneracionTipo> listTipoExoneraciones(Long id) {
        return (List<FnExoneracionTipo>) em.createQuery("SELECT f FROM FnExoneracionTipo f where f.exoneracionClase.id=:idExonearicion")
                .setParameter("idExonearicion", id).getResultList();
    }
    
    public int bajaTitualosPrediosUR(CatPredio cp, Long estadoBaja, Long estadoPendiente, List<Long> tipoLiquidaciones, Integer anioActual, boolean todo) {
        int result = 0;
        Query query;
        if (!todo) {
            query = em.createQuery("UPDATE FinaRenLiquidacion SET estadoLiquidacion.id=:estadoBaja WHERE predio=:predio AND estadoLiquidacion.id=:estadoPendiente "
                    + "and anio=:anio and tipoLiquidacion.id in (:tipoLiquidaciones)")
                    .setParameter("estadoBaja", estadoBaja).setParameter("predio", cp)
                    .setParameter("estadoPendiente", estadoPendiente).setParameter("anio", anioActual)
                    .setParameter("tipoLiquidaciones", tipoLiquidaciones);
            result = query.executeUpdate();
        } else {
            query = em.createQuery("UPDATE FinaRenLiquidacion SET estadoLiquidacion.id=:estadoBaja WHERE  estadoLiquidacion.id=:estadoPendiente "
                    + "and anio=:anio and tipoLiquidacion.id in (:tipoLiquidaciones)")
                    .setParameter("estadoBaja", estadoBaja).setParameter("estadoPendiente", estadoPendiente)
                    .setParameter("anio", anioActual).setParameter("tipoLiquidaciones", tipoLiquidaciones);
            result = query.executeUpdate();
        }
        return result;
    }
    
    public List<FinaRenEstadoLiquidacion> getEstadoLiquidaciones(String[] codigos) {
        
        return (List<FinaRenEstadoLiquidacion>) em.createQuery("SELECT e from  FinaRenEstadoLiquidacion e WHERE e.estado=TRUE AND e.id in (1,2,3,4,5)")
                .getResultList();
    }
    
    public Long secuencialBoletin() {
        
        BigInteger resul = (BigInteger) em.createNativeQuery("SELECT nextval('catastro.boletin_emisiones')").getSingleResult();
        
        return resul.longValue();
    }
    
    
    
}
