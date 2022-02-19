/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.AcumulacionFondoReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.FondosReserva;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
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
public class FondosReservaService extends AbstractService<FondosReserva> {
    
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    
    @Inject
    private UserSession userSession;
    @Inject
    private DiasLaboradoService diasService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private DistributivoEscalaService distributivoEscalaService;
    @Inject
    private AcumulacionFondoReservaService acumulacionFondoService;
    @Inject
    private ValoresDistributivoService valorService;
    
    public FondosReservaService() {
        super(FondosReserva.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    public List<FondosReserva> getListFondos(TipoRol rol, CatalogoItem item) {
        List<FondosReserva> result = (List<FondosReserva>) em.createQuery("SELECT f FROM FondosReserva f INNER JOIN f.acumulacionFondos a WHERE f.estado = TRUE AND f.tipoRol = ?1 AND a.tipoAcumulacion = ?2")
                .setParameter(1, rol)
                .setParameter(2, item)
                .getResultList();
        return result;
    }
    
    public List<FondosReserva> getListFondosReserv(TipoRol rol, CatalogoItem item, Date ini, Date fin) {
        List<FondosReserva> result = (List<FondosReserva>) em.createQuery("SELECT f FROM FondosReserva f INNER JOIN f.acumulacionFondos a WHERE f.estado = TRUE AND f.tipoRol = ?1 AND a.tipoAcumulacion = ?2 AND a.fechaInicio BETWEEN ?3 AND ?4")
                .setParameter(1, rol)
                .setParameter(2, item)
                .setParameter(3, ini).setParameter(4, fin)
                .getResultList();
        return result;
    }
    
    public List<AcumulacionFondoReserva> getListAcumulacion(TipoRol rol) {
        List<AcumulacionFondoReserva> result = (List<AcumulacionFondoReserva>) em.createQuery("SELECT DISTINCT f.acumulacionFondos FROM FondosReserva f WHERE f.estado = TRUE AND f.tipoRol = ?1")
                .setParameter(1, rol)
                .getResultList();
        return result;
    }
    
    public FondosReserva getValorRubrol(TipoRol t, Servidor s, ValoresRoles tipo) {
        try {
            Query query = em.createQuery("SELECT f FROM FondosReserva f JOIN f.acumulacionFondos af WHERE f.acumulacionFondos = af AND f.estado = TRUE AND f.tipoRol = ?1 AND af.servidor = ?2 AND af.valorParametrizado =?3 AND af.estado = true AND af.derecho = true AND af.acumula = false")
                    .setParameter(1, t)
                    .setParameter(2, s)
                    .setParameter(3, tipo.getValorParametrizable());
            FondosReserva result = (FondosReserva) query.getSingleResult();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }
    
    public List<FondosReserva> findAllFondosReservaByTipoAcumulacion(CatalogoItem tipoAcumulacion) {
        try {
            return em.createQuery("SELECT f FROM FondosReserva f JOIN f.acumulacionFondos acu WHERE acu.tipoAcumulacion = ?1")
                    .setParameter(1, tipoAcumulacion)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Transactional
    @Test
    public void addList(List<AcumulacionFondoReserva> agregar, TipoRol rolSeleccionado, BigDecimal salarioBasico, Short periodo) {
        int i = 0, BATCH_SIZE = 100;
        if (!agregar.isEmpty()) {
            Boolean var = Boolean.FALSE;
            if (salarioBasico != null) {
                var = Boolean.TRUE;
            }
            if (rolSeleccionado.getEstadoAprobacion().equals(catalogoItemService.getEstadoRol("registrado-rol"))) {
                for (AcumulacionFondoReserva a : agregar) {
                    if (i > 0 && i % BATCH_SIZE == 0) {
                        em.flush();
                        em.clear();
                    }
                    FondosReserva decimo = new FondosReserva();
                    decimo.setAcumulacionFondos(new AcumulacionFondoReserva());
                    decimo.setDiasLaborado(new DiasLaborado());
                    decimo.setDistributivoEscala(new DistributivoEscala());
                    decimo.setTipoRol(rolSeleccionado);
                    if (validarDiasLab(a.getServidor(), rolSeleccionado)) {
                        decimo.setFechaCreacion(new Date());
                        decimo.setUsuarioCreacion(userSession.getNameUser());
                        if (salarioBasico != null) {
                            decimo.setSalarioBasico(salarioBasico);
                        }
                        decimo.setAcumulacionFondos(a);
                        decimo.setDiasLaborado(diasService.diaLaborado(rolSeleccionado, a.getServidor()));
                        decimo.setDistributivoEscala(distributivoEscalaService.getRMU(a.getServidor().getDistributivo(), periodo));
                        decimo.setValorFondos(valor(decimo, var));
                        if (TalentoHumano.validarFechaInicio(a.getFechaInicio(), rolSeleccionado)) {
                            decimo = this.create(decimo);
                            em.persist(decimo);
                        }
                    }
                    i++;
                }
            }
        }
    }
    
    @Transactional
    @Test
    public void addListFR(List<AcumulacionFondoReserva> agregar, TipoRol rolSeleccionado, Short periodo) {
        int i = 0, BATCH_SIZE = 100;
        if (!agregar.isEmpty()) {
            if (rolSeleccionado.getEstadoAprobacion().equals(catalogoItemService.getEstadoRol("registrado-rol"))) {
                for (AcumulacionFondoReserva a : agregar) {
                    if (i > 0 && i % BATCH_SIZE == 0) {
                        em.flush();
                        em.clear();
                    }
                    FondosReserva fondosReserva = new FondosReserva();
                    fondosReserva.setAcumulacionFondos(new AcumulacionFondoReserva());
                    fondosReserva.setDiasLaborado(new DiasLaborado());
                    fondosReserva.setDistributivoEscala(new DistributivoEscala());
                    fondosReserva.setTipoRol(rolSeleccionado);
                    if (validarDiasLabFR(a.getServidor(), rolSeleccionado)) {
                        fondosReserva.setFechaCreacion(new Date());
                        fondosReserva.setUsuarioCreacion(userSession.getNameUser());
                        fondosReserva.setAcumulacionFondos(a);
                        fondosReserva.setDiasLaborado(diasService.diaLaboradoFR(rolSeleccionado, a.getServidor()));
                        fondosReserva.setDistributivoEscala(distributivoEscalaService.getRMU(a.getServidor().getDistributivo(), periodo));
                        fondosReserva.setValorFondos(valorFondos(fondosReserva, rolSeleccionado));
                        if (a.getFechaInicio() != null && a.getFechaFin() == null) {
                            if (TalentoHumano.validarFechaInicio(a.getFechaInicio(), rolSeleccionado)) {
                                fondosReserva = this.create(fondosReserva);
                            }
                        }
                        if (a.getFechaInicio() != null && a.getFechaFin() != null) {
                            if (TalentoHumano.validarFechaInicio(a.getFechaInicio(), rolSeleccionado) && TalentoHumano.validarFechaFin2(a.getFechaFin(), rolSeleccionado)) {
                                fondosReserva = this.create(fondosReserva);
                                a.setEstadoVigente(Boolean.FALSE);
                                acumulacionFondoService.edit(a);
                            }
                        }
                    }
                    em.persist(fondosReserva);
                    i++;
                }
            }
        }
    }
    
    @Transactional
    @Test
    public void eliminarList(List<FondosReserva> delete) {
        if (!delete.isEmpty()) {
            for (FondosReserva f : delete) {
                f.setEstado(Boolean.FALSE);
                this.edit(f);
            }
        }
    }
    
    @Transactional
    @Test
    public void actualizarValores(TipoRol rolSeleccionado, CatalogoItem decimo, BigDecimal salarioBasico) {
        List<FondosReserva> fondoActual = this.getListFondos(rolSeleccionado, decimo);
        Boolean var = Boolean.FALSE;
        if (salarioBasico != null) {
            var = Boolean.TRUE;
        }
        if (!fondoActual.isEmpty()) {
            for (FondosReserva f : fondoActual) {
                f.setSalarioBasico(salarioBasico);
                f.setValorFondos(valor(f, var));
                this.edit(f);
            }
        }
    }
    
    @Transactional
    @Test
    public boolean validarDiasLab(Servidor s, TipoRol rolSeleccionado) {
        List<DiasLaborado> lista = diasService.getDiasxTipoRol(rolSeleccionado);
        for (DiasLaborado d : lista) {
            if (s.equals(d.getServidor())) {
                return true;
            }
        }
        return false;
    }
    
    @Transactional
    @Test
    public boolean validarDiasLabFR(Servidor s, TipoRol rolSeleccionado) {
        if (diasService.diaLaboradoFR(rolSeleccionado, s) != null) {
            return true;
        }
        return false;
    }
    
    public BigDecimal valor(FondosReserva fondos, Boolean var) {
        BigDecimal diasLaborado;
        double aux, aux2;
        diasLaborado = new BigDecimal(fondos.getDiasLaborado().getDias());
        if (var) {
            aux = fondos.getSalarioBasico().doubleValue() / TalentoHumano.diasCalendarioLaboral;
        } else {
            aux = fondos.getDistributivoEscala().getRemuneracionDolares().doubleValue() / TalentoHumano.diasCalendarioLaboral;
        }
        aux2 = (aux * (diasLaborado.doubleValue())) / 12;
        return new BigDecimal(aux2).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Transactional
    @Test
    public List<AcumulacionFondoReserva> filtrarList(List<AcumulacionFondoReserva> lista, TipoRol rolSeleccionado) {
        List<AcumulacionFondoReserva> aux = new ArrayList<>();
        lista.stream().filter((a) -> (TalentoHumano.validarFechaInicio(a.getFechaInicio(), rolSeleccionado))).forEachOrdered((a) -> {
            aux.add(a);
        });
        return aux;
    }
    
    @Transactional
    @Test
    public void actualizarValoresFR(TipoRol rolSeleccionado, CatalogoItem fondoReservaItem) {
        List<FondosReserva> fondoActual = this.getListFondos(rolSeleccionado, fondoReservaItem);
        if (!fondoActual.isEmpty()) {
            for (FondosReserva f : fondoActual) {
                f.setValorFondos(valorFondos(f, rolSeleccionado));
                this.edit(f);
            }
        }
    }
    
    public BigDecimal valorFondos(FondosReserva fondos, TipoRol rolSeleccionado) {
        BigDecimal valorFondo, diasLaborado;
        double aux, aux2, aux3, valorAux, valorAux2 = 0;
        List<ValoresDistributivo> valores = new ArrayList<>();
        valores = valorService.findvaloresDistributivoXperiodo(fondos.getDistributivoEscala().getDistributivo(), rolSeleccionado.getAnio());
        ValoresDistributivo valorDistributivo = new ValoresDistributivo();
        if (!valores.isEmpty()) {
            for (ValoresDistributivo item : valores) {
                if ("FR".equals(item.getValoresParametrizados().getTipo().getCodigo())) {
                    valorDistributivo = item;
                    break;
                }
            }
        }
        if (valorDistributivo.getId() == null) {
            JsfUtil.addWarningMessage("Verifique Ditrivutivo, rubro Fondo de Reserva no encontrado", fondos.getAcumulacionFondos().getServidor().getPersona().getNombreCompleltoSql());
            return BigDecimal.ZERO;
        }
        diasLaborado = new BigDecimal(fondos.getDiasLaborado().getDias());
        double remuneracion = getRemuneracionRolHistorico(rolSeleccionado, fondos.getAcumulacionFondos().getServidor());
        Boolean accion = false;
        if (remuneracion == 0) {
            remuneracion = fondos.getDistributivoEscala().getRemuneracionDolares().doubleValue();
            accion = true;
        }
        if (valorDistributivo.getValoresParametrizados().getMedicionPorcentaje()) {
            if (accion) {
                aux = remuneracion / TalentoHumano.getUltimoDiaMes(rolSeleccionado).doubleValue();
                aux = remuneracion / TalentoHumano.diasCalendarioLaboral;
                aux2 = (aux * (diasLaborado.doubleValue())) * (valorDistributivo.getValoresParametrizados().getValor().doubleValue());
                aux3 = aux2 / 100;
                valorAux = aux3;
            } else {
                valorAux = (remuneracion * valorDistributivo.getValoresParametrizados().getValor().doubleValue()) / 100;
            }
            
        } else {
            if (accion) {
                aux = remuneracion / (TalentoHumano.getUltimoDiaMes(rolSeleccionado).doubleValue());
                aux = remuneracion / TalentoHumano.diasCalendarioLaboral;
                aux2 = (aux * (diasLaborado.doubleValue())) * (valorDistributivo.getValoresParametrizados().getValor().doubleValue());
                valorAux = aux2;
            } else {
                valorAux = (remuneracion * valorDistributivo.getValoresParametrizados().getValor().doubleValue()) / 100;
            }
        }
        fondos.setSalarioBasico(new BigDecimal(remuneracion).setScale(2, BigDecimal.ROUND_HALF_EVEN));
        if (diasDiferencia(fondos, rolSeleccionado) > 0) {
            valorAux2 = valorAux / TalentoHumano.diasCalendarioLaboral;
            valorAux = valorAux2 * diasDiferencia(fondos, rolSeleccionado);
            return new BigDecimal(valorAux).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        } else {
            return new BigDecimal(valorAux).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }
    }
    
    public int diasDiferencia(FondosReserva fondo, TipoRol rolSeleccionado) {
        int anioInicio = Utils.getAnio(fondo.getAcumulacionFondos().getFechaInicio()) - 1;
        int anioIngresoServ = Utils.getAnio(fondo.getAcumulacionFondos().getServidor().getFechaIngreso());
        int mesInicio = Utils.getMes(fondo.getAcumulacionFondos().getFechaInicio()) + 1;
        int diasRol = TalentoHumano.diasCalendarioLaboral;
        int diasFondos = Utils.getDia(fondo.getAcumulacionFondos().getFechaInicio());
        int diasDiferencia = 0;
        if (anioInicio == anioIngresoServ && rolSeleccionado.getMes().getOrden().intValue() == mesInicio) {
            if (fondo.getAcumulacionFondos().getFechaFin() != null) {
                int diafechafin = Utils.getDia(fondo.getAcumulacionFondos().getFechaFin());
                diasDiferencia = (diafechafin - diasFondos) + 1;
            } else {
                if (diasFondos == 1) {
                    diasDiferencia = TalentoHumano.diasCalendarioLaboral;
                    return diasDiferencia;
                } else {
                    diasDiferencia = (diasRol - diasFondos);
                    return diasDiferencia + 1;
                }
            }
        } else {
            if (fondo.getAcumulacionFondos().getFechaFin() != null) {
                int diafechafin = Utils.getDia(fondo.getAcumulacionFondos().getFechaFin());
                diasDiferencia = diafechafin + 1;
            } else {
                if (diasFondos > 1 && rolSeleccionado.getMes().getOrden().intValue() == mesInicio && anioInicio == anioIngresoServ) {
                    diasDiferencia = (diasRol - diasFondos) + 1;
                }
            }
        }
        return diasDiferencia - 1;
    }
    
    private double getRemuneracionRolHistorico(TipoRol tipoRol, Servidor servidor) {
        BigDecimal valor = (BigDecimal) em.createNativeQuery("select rr.valor_rubro from talento_humano.liquidacion_rol lr\n"
                + "INNER JOIN talento_humano.tipo_rol tr ON lr.tipo_rol = tr.id\n"
                + "INNER JOIN talento_humano.rol_rubro rr ON rr.liquidacion_rol = lr.id\n"
                + "INNER JOIN talento_humano.roles_de_pago rdp ON rdp.id = lr.rol_pago\n"
                + "INNER JOIN talento_humano.valores_roles vr ON rr.valor_asignacion = vr.id\n"
                + "INNER JOIN conf.parametros_talento_humano vp ON vr.valor_parametrizable = vp.id\n"
                + "INNER JOIN public.catalogo_item ci ON vp.valores=ci.id\n"
                + "WHERE tr.estado = true AND tr.anio=?2 AND tr.mes = ?1 AND lr.estado= true \n"
                + "AND rr.estado=true AND rdp.servidor = ?3 AND rdp.estado=true AND ci.codigo='RAU'")
                .setParameter(1, tipoRol.getMes().getId())
                .setParameter(2, tipoRol.getAnio())
                .setParameter(3, servidor.getId())
                .getSingleResult();
        return valor.doubleValue();
    }
    
}
