/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.service;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Luis Suarez
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class UnidadAdministrativaService extends AbstractService<UnidadAdministrativa> {

    @Inject
    private ManagerService services;
    private HashMap<String, Object> param;
    private static final long serialVersionUID = 1L;
    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;
    private List<UnidadAdministrativa> listaUnidadesHijas;

    public UnidadAdministrativaService() {
        super(UnidadAdministrativa.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatalogoItem> getCatalogoTipoUnidad(String codigo) {
        List<CatalogoItem> result = (List<CatalogoItem>) em.createQuery(
                "SELECT c FROM CatalogoItem c join c.catalogo cc where cc.codigo= :codigo")
                .setParameter("codigo", codigo)
                .getResultList();

        return result;

    }

    public List<UnidadAdministrativa> listaDeUnidades() {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT u FROM Servidor s "
                + "INNER JOIN s.distributivo d "
                + "INNER JOIN d.unidadAdministrativa u "
                + "WHERE s.estado = TRUE")
                .getResultList();
        return result;
    }

    public List<UnidadAdministrativa> getPadreUnidad(Long codigo) {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery(
                "SELECT c FROM UnidadAdministrativa c JOIN c.tipoUnidad.catalogo cc where cc.id=:codigo ")
                .setParameter("codigo", codigo)
                .getResultList();

        return result;

    }

    public Short getMaxDireccion(boolean padre, String codigo) {
        Short val = 0;
        if (padre) {
            val = (Short) em.createQuery("SELECT MAX(u.codigo) FROM UnidadAdministrativa u INNER JOIN u.tipoUnidad cc WHERE cc.texto= :codigo")
                    .setParameter("codigo", codigo)
                    .getSingleResult();
        }
        return val;
    }

    public List<UnidadAdministrativa> getAllPadre() {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery(
                "SELECT c FROM UnidadAdministrativa c where c.padre IS NULL and c.estado = true")
                .getResultList();
        return result;
    }

    public List<UnidadAdministrativa> getPadreUnidad(UnidadAdministrativa padre) {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery(
                "SELECT c FROM UnidadAdministrativa c where c.padre=:padre AND c.estado = true")
                .setParameter("padre", padre)
                .getResultList();

        return result;
    }

    public List<UnidadAdministrativa> getUnidadActualizar() throws ParseException {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT c FROM UnidadAdministrativa c where c.fechaCaducidad < ?1 AND c.estadoActivo=true AND c.estado = true")
                .setParameter(1, Utils.devolverFecha(new Date(), "yyyy-MM-dd"))
                .getResultList();

        return result;
    }

    @Schedule(dayOfWeek = "*", month = "*", hour = "00", dayOfMonth = "*", year = "*", minute = "05", second = "0", persistent = false)
    @AccessTimeout(value = -1)
    public void buscarUnidadesCaducadas() throws ParseException {
        List<UnidadAdministrativa> unidadList = this.getUnidadActualizar();
        if (Utils.isNotEmpty(unidadList)) {
            for (UnidadAdministrativa u : unidadList) {
                if (u.getFechaCaducidad().before(Utils.devolverFecha(new Date(), "yyyy-MM-dd"))) {
                    u.setEstadoActivo(Boolean.FALSE);
                    this.edit(u);
                }
            }
        }
    }

    /**
     * Retorna el objeto que puede ser Direccion, departamento, Unidad
     *
     * @param codigo ess para catalogoItem
     * @param codigo2 hacer referencia a catalogo tipo_unidad
     * @return
     */
    public CatalogoItem getCatalogoTipo(String codigo, String codigo2) {

        CatalogoItem result = (CatalogoItem) em.createQuery("SELECT ci FROM CatalogoItem ci INNER JOIN ci.catalogo c WHERE c.codigo= :catalogo and ci.codigo= :codigo")
                .setParameter("catalogo", codigo)
                .setParameter("codigo", codigo2).getSingleResult();
        return result;
    }

    public List<UnidadAdministrativa> getUnidadesDistributivo() {
        try {
            List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT d.unidadAdministrativa FROM Distributivo d WHERE d.estado = TRUE ")
                    .getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<CatalogoItem> getTiposUnidades() {

        List<CatalogoItem> result = (List<CatalogoItem>) em.createQuery("SELECT DISTINCT(u.tipoUnidad) FROM UnidadAdministrativa u").getResultList();
        return result;
    }

    public List<UnidadAdministrativa> getUnidadesEstado(boolean estado) {

        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT(p) FROM UnidadAdministrativa u INNER JOIN u.padre p where u.estado= :estado").setParameter("estado", estado).getResultList();
        return result;
    }

    public List<UnidadAdministrativa> getlListaUnidades() {

        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u  WHERE u.estado=true ORDER BY u.nombre ASC ").getResultList();
        return result;
    }

    public List<String> getListaPadresAdministrativas(UnidadAdministrativa u) {
        List<String> dataList = new ArrayList<>();
        UnidadAdministrativa unidad = new UnidadAdministrativa();
        List<UnidadAdministrativa> lista = new ArrayList<>();
        unidad = u;
        List<UnidadAdministrativa> unidadList = (List<UnidadAdministrativa>) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE ORDER BY u.id ASC").getResultList();
        for (UnidadAdministrativa data : unidadList) {
            if (data.equals(unidad)) {
                if (unidad.getPadre() != null) {
                    UnidadAdministrativa unidadLista = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado=TRUE AND u.id= :id ")
                            .setParameter("id", data.getPadre().getId()).getSingleResult();
                    if (unidad != null) {
                        lista.add(unidadLista);
                        unidad = unidadLista;
                    }
                }
            }
        }
        lista.add(u);

        for (UnidadAdministrativa data : lista) {

            dataList.add(data.getNombre());
        }

        return dataList;
    }

    public List<UnidadAdministrativa> padreUnidades(UnidadAdministrativa u) {

        System.out.println("unidad principal" + u.getNombre());
        UnidadAdministrativa unidad = new UnidadAdministrativa();
        unidad = u;
        UnidadAdministrativa unidadView = new UnidadAdministrativa();
        unidadView = u;
        List<UnidadAdministrativa> unidades = new ArrayList<>();
        while (unidadView.getPadre() != null) {

            unidad = (UnidadAdministrativa) em.createQuery("SELECT u FROM UnidadAdministrativa u where u.id=:unidad")
                    .setParameter("unidad", unidadView.getPadre().getId()).getResultStream().findFirst().orElse(null);
            if (unidad != null) {
                unidades.add(unidad);
            } else {
                unidad = new UnidadAdministrativa();
            }
            unidadView = unidad;

            System.out.println("unidad " + unidad.getNombre());
        }

        return unidades;

    }

    public List<UnidadAdministrativa> hijasUnidadesRecursivo(Usuarios u) {
        param = new HashMap<>();
        param.put("usuario", u);
        UnidadAdministrativa unidad = null;
        List<UsuarioRol> usuarioRoles = services.findAll(UsuarioRol.class, param);
        unidad = usuarioRoles.get(0).getRol().getUnidadAdministrativa();
        listaUnidadesHijas = new ArrayList<>();
        listaUnidadesHijas.add(unidad);        
        listaUnidadesAdministrativasHijas(unidad);
        return listaUnidadesHijas;
    }

    public List<UnidadAdministrativa> listaUnidadesAdministrativasHijas(UnidadAdministrativa u) {
        System.out.println("lista padre " + u.getNombre().toUpperCase());
        param = new HashMap<>();
        param.put("padre", u);
        List<UnidadAdministrativa> lista = services.findAll(UnidadAdministrativa.class, param);

        for (UnidadAdministrativa i : lista) {
            System.out.println("PADRE hija" + i);
            listaUnidadesHijas.add(i);
            listaUnidadesAdministrativasHijas(i);
        }

        return listaUnidadesHijas;
    }

    public List<Rol> rolDetinatario(UnidadAdministrativa u) {
        List<Rol> result = (List<Rol>) em.createQuery("SELECT r FROM Rol r where r.unidadAdministrativa=:unidad")
                .setParameter("unidad", u).getResultList();

        return result;
    }

    public Usuarios getUsuarioDestino(Rol r) {
        List<Usuarios> user = (List<Usuarios>) em.createQuery("SELECT u.usuario FROM UsuarioRol u WHERE u.rol=:rol AND u.usuario.estado=TRUE")
                .setParameter("rol", r).getResultList();

        if (!user.isEmpty()) {
            return user.get(0);
        }
        return new Usuarios();

    }

    /**
     * *
     * UnidadAdministrativaController se usa
     *
     * @param u para sacar el orden
     * @return todas las unidades administrativas padres a las cuales la unidad
     * que pasa como parametro puede denominarsela como hija
     */
    public List<UnidadAdministrativa> findUnidadesAdministrativasPadres(UnidadAdministrativa u) {
        return em.createQuery("SELECT u FROM UnidadAdministrativa u  WHERE u.tipoUnidad.orden < ?1 and u.estado = true and u.estadoActivo = true ORDER BY u.nombre asc")
                .setParameter(1, u.getTipoUnidad().getOrden()).getResultList();
    }

    public List<UnidadAdministrativa> findHijoMayor(UnidadAdministrativa u) {
        return em.createQuery("SELECT u FROM UnidadAdministrativa u  WHERE u.padre = ?2 and u.tipoUnidad.orden <= ?1 and u.estado = true and u.estadoActivo = true ORDER BY u.nombre asc")
                .setParameter(1, u.getTipoUnidad().getOrden()).setParameter(2, u).getResultList();
    }
    
    public List<UnidadAdministrativa> listaDeUnidadesXPAPP(Short periodo) {
        List<UnidadAdministrativa> result = (List<UnidadAdministrativa>) em.createQuery("SELECT DISTINCT u FROM ActividadOperativa a "
                + "INNER JOIN a.unidadResponsable u "
                + "WHERE a.estado = TRUE and a.periodo= :periodo").setParameter("periodo", periodo)
                .getResultList();
        return result;
    }

}
