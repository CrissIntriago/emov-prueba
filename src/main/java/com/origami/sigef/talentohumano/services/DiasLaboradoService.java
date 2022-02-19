/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.services;

import com.google.gson.Gson;
import com.origami.sigef.ConnectSQLite.ConnectionSQLite;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.view.biotime.MarcacionDiaria;
import com.origami.sigef.common.service.AbstractService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.model.MarcacionModel;
import com.origami.sigef.talentohumano.services.biotime.MarcacionDiariaService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

/**
 *
 * @author ORIGAMI2
 */
@Stateless
@javax.enterprise.context.Dependent
public class DiasLaboradoService extends AbstractService<DiasLaborado> {

    private static final long serialVersionUID = 1L;
    @javax.persistence.PersistenceContext(unitName = com.origami.sigef.common.config.CONFIG.PERSISTENCE_UNIT)
    private EntityManager em;

    @Inject
    private MarcacionDiariaService marcacionDiariaService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private UserSession userSession;

    public DiasLaboradoService() {
        super(DiasLaborado.class);
    }

    @Override
    public EntityManager getEntityManager() {
        //    EntityManagerFactory emf = Persistence.createEntityManagerFactory("persistence-sigef");
        return em;
    }

    public List<DiasLaborado> getListaDiasLaborado(short anio, String mes) {
        try {
            Query query = getEntityManager().createQuery("SELECT d FROM DiasLaborado d WHERE d.estado = TRUE AND d.periodo = ?1 AND d.mes = ?2 ORDER BY d.servidor.persona.identificacion")
                    .setParameter(1, anio)
                    .setParameter(2, mes);
            List<DiasLaborado> result = (List<DiasLaborado>) query.getResultList();
            return result;
        } catch (Exception e) {
            return null;
        }

    }

    public List<DiasLaborado> getDiasxTipoRol(TipoRol tipo) {
        try {
            Query query = getEntityManager().createQuery("SELECT d FROM DiasLaborado d WHERE d.estado = TRUE AND d.tipoRol = ?1")
                    .setParameter(1, tipo);
            List<DiasLaborado> result = (List<DiasLaborado>) query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<DiasLaborado> getDiasxTipoRolByFR(TipoRol tipo) {
        try {
            Query query = getEntityManager().createQuery("SELECT d FROM DiasLaborado d WHERE d.estado = TRUE AND d.tipoRol.estado = true AND d.tipoRol.mes = ?1 AND d.tipoRol.anio = ?2")
                    .setParameter(1, tipo.getMes())
                    .setParameter(2, tipo.getAnio());
            List<DiasLaborado> result = (List<DiasLaborado>) query.getResultList();
            return result;
        } catch (NoResultException e) {
            return null;
        }
    }

    public DiasLaborado diaLaborado(TipoRol rol, Servidor servidor) {
        try {
            Query query = getEntityManager().createQuery("SELECT d FROM DiasLaborado d WHERE d.estado = TRUE AND d.tipoRol = ?1 AND d.servidor = ?2")
                    .setParameter(1, rol)
                    .setParameter(2, servidor);
            DiasLaborado dia = (DiasLaborado) query.getSingleResult();
            return dia;
        } catch (Exception ex) {
            return null;
        }
    }

    public DiasLaborado diaLaboradoFR(TipoRol rol, Servidor servidor) {
        try {
            Query query = getEntityManager().createQuery("SELECT d FROM DiasLaborado d WHERE d.estado = TRUE AND d.tipoRol.estado = true AND d.tipoRol.mes = ?1 AND d.tipoRol.tipoRol.codigo IN ('rol_general','rol_adicional')  AND d.servidor = ?2 AND d.tipoRol.anio = ?3")
                    .setParameter(1, rol.getMes())
                    .setParameter(2, servidor)
                    .setParameter(3, rol.getAnio());
            DiasLaborado dia = (DiasLaborado) query.getSingleResult();
            return dia;
        } catch (Exception ex) {
            return null;
        }
    }

    public DiasLaborado diaLaboradoHoras(TipoRol rol, Servidor servidor) {
        try {
            Query query = getEntityManager().createQuery("SELECT d FROM DiasLaborado d WHERE d.estado = TRUE AND UPPER(d.mes) = UPPER(?1) AND d.servidor = ?2 AND d.periodo = ?3")
                    .setParameter(1, rol.getMes().getCodigo())
                    .setParameter(2, servidor).setParameter(3, rol.getAnio());
            DiasLaborado dia = (DiasLaborado) query.getSingleResult();
            return dia;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<DiasLaborado> diaLaboradoAnioServidor(short anio, Servidor servidor) {
        try {
            Query query = em.createQuery("SELECT d FROM DiasLaborado d JOIN d.tipoRol t JOIN t.estadoAprobacion e WHERE d.estado = TRUE AND d.periodo = ?1 AND d.servidor = ?2 AND (e.codigo = 'aprobado-rol' OR e.codigo = 'pagado-rol')")
                    .setParameter(1, anio)
                    .setParameter(2, servidor);
            List<DiasLaborado> dia = query.getResultList();
            return dia;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<DiasLaborado> mesesLaboradoAnioServidor(Date desde, Date hasta, Servidor servidor) {
        String desdef, hastaf;

        desdef = new SimpleDateFormat("YYYY-MM").format(desde);
        hastaf = new SimpleDateFormat("YYYY-MM").format(hasta);
        try {
//            Query query = em.createQuery("SELECT d FROM DiasLaborado d JOIN d.tipoRol t JOIN t.estadoAprobacion e JOIN t.mes m WHERE d.estado = TRUE AND d.servidor = ?2 AND (e.codigo = 'aprobado-rol' OR e.codigo = 'pagado-rol') AND CONCAT(d.periodo,' - ',m.orden) <= ?3 AND  CONCAT(d.periodo,' - ',m.orden)  >= ?1 ")
            Query query = em.createNativeQuery("SELECT  *  FROM talento_humano.dias_laborado d \n"
                    + "INNER JOIN talento_humano.tipo_rol t ON t.id = d.tipo_rol  \n"
                    + "INNER JOIN catalogo_item e ON e.id = t.estado_aprobacion\n"
                    + "INNER JOIN catalogo_item m ON m.id = t.mes\n"
                    + "WHERE d.estado = TRUE AND d.servidor = ?2 AND (e.codigo = 'aprobado-rol' OR e.codigo = 'pagado-rol') \n"
                    + "	AND  CONCAT(d.periodo,'-',lpad(CAST(m.orden AS TEXT),2,'0')) >= ?1 AND CONCAT(d.periodo,'-',lpad(CAST(m.orden AS TEXT),2,'0')) <= ?3  ", DiasLaborado.class)
                    .setParameter(1, desdef)
                    .setParameter(3, hastaf)
                    .setParameter(2, servidor);
            List<DiasLaborado> dia = query.getResultList();
            return dia;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public BigInteger diasLaboradoAnioServidor(Date desde, Date hasta, Servidor servidor) {
        String desdef, hastaf;

        desdef = new SimpleDateFormat("YYYY-MM").format(desde);
        hastaf = new SimpleDateFormat("YYYY-MM").format(hasta);
        try {
//            Query query = em.createQuery("SELECT d FROM DiasLaborado d JOIN d.tipoRol t JOIN t.estadoAprobacion e JOIN t.mes m WHERE d.estado = TRUE AND d.servidor = ?2 AND (e.codigo = 'aprobado-rol' OR e.codigo = 'pagado-rol') AND CONCAT(d.periodo,' - ',m.orden) <= ?3 AND  CONCAT(d.periodo,' - ',m.orden)  >= ?1 ")
            Query query = em.createNativeQuery("SELECT  SUM(d.dias)  FROM talento_humano.dias_laborado d \n"
                    + "INNER JOIN talento_humano.tipo_rol t ON t.id = d.tipo_rol  \n"
                    + "INNER JOIN catalogo_item e ON e.id = t.estado_aprobacion\n"
                    + "INNER JOIN catalogo_item m ON m.id = t.mes\n"
                    + "WHERE d.estado = TRUE AND d.servidor = ?2 AND (e.codigo = 'aprobado-rol' OR e.codigo = 'pagado-rol') \n"
                    + "	AND  CONCAT(d.periodo,'-',lpad(CAST(m.orden AS TEXT),2,'0')) >= ?1 AND CONCAT(d.periodo,'-',lpad(CAST(m.orden AS TEXT),2,'0')) <= ?3")
                    .setParameter(1, desdef)
                    .setParameter(3, hastaf)
                    .setParameter(2, servidor);
            BigInteger dia = (BigInteger) query.getSingleResult();
            return dia;
        } catch (Exception ex) {
            ex.printStackTrace();
            return BigInteger.ZERO;
        }
    }

    public void actualizarDiasDesdeBiometrico(TipoRol tipoRol, Short anio) {
        try {
            DiasLaborado find = new DiasLaborado();
            find.setEstado(Boolean.TRUE);
            find.setTipoRol(tipoRol);
            find.setPeriodo(anio);
            List<DiasLaborado> diasMarcados = findByExample(find);
            if (Utils.isNotEmpty(diasMarcados)) {
                for (DiasLaborado dm : diasMarcados) {
                    if (dm.getServidor().getCodBiometrico() != null && dm.getServidor().getCodBiometrico().trim().length() > 0) {
                        MarcacionDiaria dmFind = new MarcacionDiaria();
                        dmFind.setEmpCode(dm.getServidor().getCodBiometrico());
                        List<MarcacionDiaria> marcaciones = marcacionDiariaService.findByExample(dmFind);
                        System.out.println("Marcacion >> " + dm + " dias " + marcaciones);

                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(DiasLaboradoService.class.getName()).log(Level.SEVERE, "Error", e);
        }
    }

    public List<MarcacionModel> getListMarcacion(String empCode, String mes, String anio) {
        HttpClient httpClient;
        HttpGet httpGet;
        Gson gson;
        ExecutorService executorService;
        Future<HttpResponse> futureResponse;
        HttpResponse httpResponse;
        String urlService = "";

        try {
            if (empCode == null || empCode == "") {
                return null;
            }
            System.out.println("entro-->");
            urlService = valoresService.getValor("URL_ZKTIMENET_WS").getValorString();
            System.out.println("url---> " + urlService);
            httpClient = HttpClientBuilder.create().build();
            URI uri = new URI(String.format(urlService, empCode, mes, anio));
            httpGet = new HttpGet(uri);
            executorService = Executors.newSingleThreadExecutor();
            futureResponse = executorService.submit(() -> httpClient.execute(httpGet));
            httpResponse = futureResponse.get(60, TimeUnit.SECONDS);
            if (httpResponse != null) {
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent()));
                String inputLine;
                StringBuilder sb = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    sb.append(inputLine);
                }
                in.close();
                gson = new Gson();
                if (sb.toString() != null) {
                    List<MarcacionModel> listModel = new ArrayList<>(Arrays.asList(gson.fromJson(sb.toString(), MarcacionModel[].class)));
                    return listModel;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException | URISyntaxException ex) {
            Logger.getLogger(ConnectionSQLite.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Transactional
    @Test
    public void guardarDiasLaborados(List<Servidor> listaServidores, TipoRol tipoRol) {
        int i = 0;
        int BATCH_SIZE = 25;
        for (Servidor s : listaServidores) {
            if (i > 0 && i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
            DiasLaborado diaLaborado = new DiasLaborado();
            diaLaborado.setTipoRol(new TipoRol());
            diaLaborado.setServidor(new Servidor());
            diaLaborado.setFechaCreacion(new Date());
            diaLaborado.setUsuarioCreacion(userSession.getNameUser());
            diaLaborado.setServidor(s);
            diaLaborado.setPeriodo(tipoRol.getAnio());
            diaLaborado.setTipoRol(tipoRol);
            diaLaborado.setMes(tipoRol.getMes().getDescripcion());
            if (s.getFechaSalida() == null) {
                if (TalentoHumano.validarFechaInicio(s.getFechaIngreso(), tipoRol)) {
                    if (getVerificarRegistro(tipoRol, s)) {
                        em.persist(diaLaborado);
                    }
                }
            }
            if (s.getFechaIngreso() != null && s.getFechaSalida() != null) {
                if (TalentoHumano.validarFechaInicio(s.getFechaIngreso(), tipoRol) && TalentoHumano.validarFechaFin(s.getFechaSalida(), tipoRol)) {
                    if (getVerificarRegistro(tipoRol, s)) {
                        em.persist(diaLaborado);
                    }
                }
            }
            i++;
        }
    }

    public boolean getVerificarRegistro(TipoRol tipoRol, Servidor servidor) {
        try {
//        System.out.println("SERVIDOR: "+servidor);
        Query query = getEntityManager().createQuery("SELECT rp.servidor FROM LiquidacionRol lr INNER JOIN lr.rolPago rp INNER JOIN lr.tipoRol tr WHERE lr.estado=true AND tr.mes=?1 AND tr.anio=?3 AND tr.estado=true AND rp.servidor=?2")
                .setParameter(1, tipoRol.getMes())
                .setParameter(2, servidor)
                .setParameter(3, tipoRol.getAnio());
        Servidor result = (Servidor) query.getSingleResult();
        if (result == null) {
            return true;
        } else {
            return false;
        }
        } catch (Exception ex) {
            return true;
        }
    }

}
