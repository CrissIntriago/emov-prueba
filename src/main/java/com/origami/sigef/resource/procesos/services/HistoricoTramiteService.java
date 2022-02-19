/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.procesos.services;

import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.resource.conf.models.QUERY;
import com.origami.sigef.resource.conf.services.QueryService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import com.origami.sigef.resource.procesos.models.Tramites;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author gutya
 */
@Stateless
@javax.enterprise.context.Dependent
public class HistoricoTramiteService extends AbstractService<HistoricoTramites> {

    @Inject
    private QueryService confQuerysServices;

    private static final Logger LOG = Logger.getLogger(HistoricoTramiteService.class.getName());

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public HistoricoTramiteService() {
        super(HistoricoTramites.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public HistoricoTramites findByProcessId(String processInstanceId) {
        try {
            return this.findByNamedQuery1("HistoricoTramites.IdProceso", processInstanceId);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Buscando proceso: " + processInstanceId, e);
        }
        return null;
    }

    public HistoricoTramites findByProcessIdTemp(String processInstanceId) {
        try {
            return this.findByNamedQuery1("HistoricoTramites.IdProcesoTemp", processInstanceId);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Buscando proceso: " + processInstanceId, e);
        }
        return null;
    }

    public String findReferencia(HistoricoTramites tramite) {
        String nombreBeneficiario = null;
        try {
            if (tramite.getIdReferencia() != null) {
                try {
                    Object d = getEntityManager().find(Class.forName(tramite.getNameReferencia()), tramite.getIdReferencia().longValue());
                    if (d instanceof Servidor) {
                        Servidor serv = (Servidor) d;
                        nombreBeneficiario = "<b>Beneficiario: </b><b>Cedula/RUC:</b> " + serv.getPersona().getIdentificacionCompleta()
                                + "<p:spacer width=\"50\" /> <b>Nombres:</b> " + serv.getPersona().getNombreCompleto();
                    } else if (d instanceof Cliente) {
                        Cliente cl = (Cliente) d;
                        nombreBeneficiario = "<b>Beneficiario: </b><b>Cedula/RUC:</b>" + cl.getIdentificacionCompleta()
                                + "<p:spacer width=\"50\" /> <b>Nombres:</b> " + cl.getNombreCompleto();
                    } else if (d instanceof Proveedor) {
                        Proveedor cl = (Proveedor) d;
                        nombreBeneficiario = "<b>Beneficiario: </b><b>Cedula/RUC:</b>" + cl.getCliente().getIdentificacionCompleta()
                                + "<p:spacer width=\"50\" /> <b>Nombres:</b> " + cl.getCliente().getNombreCompleto();
                    } else if (d instanceof Adquisiciones) {
                        Adquisiciones cl = (Adquisiciones) d;
                        nombreBeneficiario = "<b>N° Contrato:</b>" + cl.getNumeroContrato()
                                + "<p:spacer width=\"50\" /> <b>Descripción:</b> " + cl.getDescripcion()
                                + "<p:spacer width=\"50\" /> <b>Proveedor:</b> " + cl.getProveedor().getCliente().getNombreCompleto();
                    } else {
                        nombreBeneficiario = "<b>Identificador Seleccionado:</b> " + tramite.getIdReferencia();
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(BpmnBaseRoot.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Buscando Referencia del trámite : " + tramite.getNumTramite(), e);
        }
        return nombreBeneficiario;
    }

    public List<Tramites> findTareasUser(String nameUser, Short anio) {
        List<Tramites> result = (List<Tramites>) em.createQuery(confQuerysServices.getQuerySql(QUERY.FIND_TRAMITE_USER))
                .setParameter("user", "%" + nameUser + "%")
                .setParameter("periodo", anio)
                .getResultList();
        return result;
    }
    
    public List<Short> findPeriodoDisponible() {
        List<Short> result = (List<Short>) em.createQuery("Select periodo from HistoricoTramites group by periodo order by periodo")
                .getResultList();
        return result;
    }

}
