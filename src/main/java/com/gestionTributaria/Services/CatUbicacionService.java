/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.CatUbicacion;
import com.gestionTributaria.Entities.Obra;
import com.gestionTributaria.models.PredioCemCiudadelaManzanaDTO;
import com.gestionTributaria.models.PredioCemDTO;
import com.gestionTributaria.models.predioCemClaveNumPredioDTO;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.util.Utils;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Administrator
 */
@javax.ejb.Stateless
@javax.enterprise.context.Dependent
public class CatUbicacionService extends AbstractService<CatUbicacion> {

    private static final Logger LOG = Logger.getLogger(ObraServices.class.getName());

    @PersistenceContext(unitName = CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    public CatUbicacionService() {
        super(CatUbicacion.class);
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }

    public List<CatUbicacion> getUbicacionesByObra(Obra obra) {
        List<CatUbicacion> ubicaciones = new ArrayList<>();
        try {
            ubicaciones = (List<CatUbicacion>) em.createQuery("select a from CatUbicacion a where a.obra=?1").setParameter(1, obra).getResultList();
            if (ubicaciones.isEmpty()) {
                return null;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al encontrar las ubicaciones", ex);
        }
        return ubicaciones;
    }

    public List<PredioCemDTO> getUbicaciones() {
        List<PredioCemDTO> ubicaciones = new ArrayList<>();
        try {
            Query query = em.createNativeQuery("select b.id,a.sector,b.nombre from catastro.cat_predio as a inner join sgm.cat_ciudadela as b on a.ciudadela=b.id "
                    + "where a.sector<> 0 and a.sector between 1 and 18 group by b.id,a.sector,b.nombre order by a.sector");
            List<Object[]> result = query.getResultList();
            if (Utils.isNotEmpty(result)) {
                ubicaciones = new ArrayList<>();
                for (Object[] data : result) {
                    BigInteger id_ciudadela;
                    Short sector;
                    String ciudadela;
                    id_ciudadela = (BigInteger) data[0];
                    sector = (Short) data[1];
                    ciudadela = (String) data[2];
                    PredioCemDTO cemdto = new PredioCemDTO();
                    cemdto.setId_ciudadela(id_ciudadela.intValue());
                    cemdto.setSector(sector);
                    cemdto.setNombre(ciudadela);
                    ubicaciones.add(cemdto);
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar las ubicaciones", ex);
        }
        return ubicaciones;
    }

    public List<PredioCemCiudadelaManzanaDTO> getUbicacionesCiudadelaMz() {
        List<PredioCemCiudadelaManzanaDTO> ubicaciones = new ArrayList<>();
        try {
            Query query = em.createNativeQuery("select b.id,b.nombre,a.mz from catastro.cat_predio as a \n"
                    + "inner join sgm.cat_ciudadela as b on a.ciudadela=b.id where a.sector<> \n"
                    + "0 and a.sector between 1 and 18 group by b.id,b.nombre,a.mz ");
            List<Object[]> result = query.getResultList();
            if (Utils.isNotEmpty(result)) {
                ubicaciones = new ArrayList<>();
                for (Object[] data : result) {
                    BigInteger id_ciudadela;
                    Short mz, sector;
                    String ciudadela;
                    id_ciudadela = (BigInteger) data[0];
                    ciudadela = (String) data[1];
                    mz = (Short) data[2];
                    PredioCemCiudadelaManzanaDTO cemdto = new PredioCemCiudadelaManzanaDTO();
                    cemdto.setId_ciudadela(id_ciudadela.intValue());
                    cemdto.setManzana(mz);
                    cemdto.setNombre(ciudadela);
                    ubicaciones.add(cemdto);
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar las ubicaciones", ex);
        }
        return ubicaciones;
    }

    public List<predioCemClaveNumPredioDTO> getUbicacionesPredio() {
        List<predioCemClaveNumPredioDTO> ubicaciones = new ArrayList<>();
        try {
            Query query = em.createNativeQuery("select id,clave_Cat,num_predio from catastro.cat_predio where tipo_predio='U' and estado='A' and es_activo='A'");
            List<Object[]> result = query.getResultList();
            if (Utils.isNotEmpty(result)) {
                ubicaciones = new ArrayList<>();
                for (Object[] data : result) {
                    BigInteger id, num_predio;
                    String clave;
                    id = (BigInteger) data[0];
                    clave = (String) data[1];
                    num_predio = (BigInteger) data[2];
                    predioCemClaveNumPredioDTO cemdto = new predioCemClaveNumPredioDTO();
                    cemdto.setId_predio(id.intValue());
                    cemdto.setClaveCat(clave);
                    cemdto.setNum_predio(num_predio.intValue());
                    ubicaciones.add(cemdto);
                }
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error al buscar las ubicaciones predio", ex);
        }
        return ubicaciones;
    }
}
