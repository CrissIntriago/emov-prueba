package com.origami.sigef.common.service;

import com.gestionTributaria.Commons.SisVars;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.origami.sigef.common.config.RutasSystemContr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQrResponse;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.models.TareasActivas;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Comprobantes;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.FormaPago;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ComprobanteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.FormaPagoService;
import com.origami.sigef.tesoreria.entities.Rubro;
import com.origami.sigef.tesoreria.service.RubroService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Singleton // @javax.enterprise.context.Dependent
@ApplicationScoped
public class KatalinaEjb implements KatalinaService {

    private static final Logger LOG = Logger.getLogger(KatalinaEjb.class.getName());

    @Inject
    private UserSession userSession;
    @Inject
    private CajeroService cajeroService;
    @Inject
    private ComprobanteService comprobanteService;
    @Inject
    private FormaPagoService formaPagoService;
    @Inject
    private RubroService rubroService;
    @Inject
    private TareasActivasService tareasActivasService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private RutasSystemContr rutas;
    @Inject
    private EntityManagerService entityManagerService;

    private HttpClient httpClient;
    private HttpPost httpPost;
    private HttpGet httpGet;
    private HttpPut httpPut;
    private Gson gson;
    private ExecutorService executorService;
    private Future<HttpResponse> futureResponse;
    private HttpResponse httpResponse;
    private Client client;
    private RestTemplate restTemplate;

//    private final String appQrServiceDesarrollo = "http://172.27.44.60:8770/qrservices/atlantis/api/";
////    private final String appQrServiceDesarrollo = "http://192.168.100.211:8726/atlantis/api/";
////    private final String appQrServiceDesarrollo = "http://186.101.220.187:8726/atlantis/api/";
////    private final String appQrServiceDesarrollo = "https://webemot.online/qrservices/atlantis/api/";
////    private final String appAtlantisUrlDesarrollo = "http://192.168.100.6:8727/atlantis/email/api/";
//    private final String appAtlantisUrlDesarrollo = "http://172.27.44.60:8770/qrservices/atlantis/email/api/";
    public KatalinaEjb() {
        gson = new Gson();
        client = ClientBuilder.newClient();
    }

    @Override
    public Cajero findCajero() {
        return cajeroService.findByCajero(userSession.getNameUser());
    }

    @Override
    public List<Comprobantes> findComprobantes() {
        return comprobanteService.findAll();
    }

    @Override
    public Comprobantes findComprobante(String codigo) {
        return comprobanteService.findByCodigo(codigo);
    }

    @Override
    public List<Comprobantes> findComprobantesRetienen() {
        return comprobanteService.findComprobantesRetienen();
    }

    @Override
    public List<FormaPago> findAllFormaPago() {
        return formaPagoService.findAll();
    }

    ;

    @Override
    public Rubro findPredeterminadoTipo(Integer idRubroTipo, Boolean venta) {
        return rubroService.findRubroPredeterminadoByTipo(idRubroTipo, venta);
    }

    @Override
    public Rubro findBaseRetencionIVA(Integer idRubroTipo, Boolean venta, Double valor) {
        return rubroService.findRubroBaseRetencionIVA(idRubroTipo, venta, valor);
    }

    @Override
    public Rubro findRubroPredeterminadoByTipoCompra(Integer idRubroTipo, Boolean compra) {
        return rubroService.findRubroPredeterminadoByTipo(idRubroTipo, compra);
    }

//    @Override
//    public DetalleItemWS generarCodigoQR(DetalleItemWS detallews) {
//        httpClient = HttpClientBuilder.create().build();
//        httpPost = new HttpPost(CONFIG.appQrServiceDesarrollo + "codigoqr/detalleItem/generar");
//        httpPost.setEntity(new StringEntity(gson.toJson(detallews), "UTF-8"));
//        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
//        httpPost.setHeader("Authorization", "null");
//        executorService = Executors.newSingleThreadExecutor();
//        futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
//        try {
//            httpResponse = futureResponse.get(60, TimeUnit.SECONDS);
//            if (httpResponse != null) {
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(httpResponse.getEntity().getContent()));
//                String inputLine;
//                StringBuilder sb = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    sb.append(inputLine);
//                }
//                in.close();
//                return gson.fromJson(sb.toString(), DetalleItemWS.class);
//            } else {
//                return null;
//            }
//        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
    @Override
    public DetalleQrResponse generarCodigoQR(DetalleQr detalleQr) {
        return (DetalleQrResponse) methodPOST(detalleQr, SisVars.appQrService + "generarQREtiquetas", DetalleQrResponse.class);
//        httpClient = HttpClientBuilder.create().build();
//        httpPost = new HttpPost(SisVars.appQrService + "generarQREtiquetas");
//        httpPost.setEntity(new StringEntity(gson.toJson(detalleQr), "UTF-8"));
//        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
//        httpPost.setHeader("Authorization", "null");
//        executorService = Executors.newSingleThreadExecutor();
//        futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
//        try {
//            httpResponse = futureResponse.get(60, TimeUnit.SECONDS);
//            if (httpResponse != null) {
//                BufferedReader in = new BufferedReader(
//                        new InputStreamReader(httpResponse.getEntity().getContent()));
//                String inputLine;
//                StringBuilder sb = new StringBuilder();
//                while ((inputLine = in.readLine()) != null) {
//                    sb.append(inputLine);
//                }
//                in.close();
//                return gson.fromJson(sb.toString(), DetalleQrResponse.class);
//            } else {
//                return null;
//            }
//        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
//            ex.printStackTrace();
//        }
//        return null;
    }

    @Override
    public void enviarCorreo(Correo correo) {
        if (correo.getDestinatario() == null) {
            System.out.println("********* No se encontro destinatario para enviar correo.");
            return;
        }
        Thread data = new Thread() {
            @Override
            public void run() {
                String creds = String.format("%s:%s", "atlantis", "atlantis");
                String auth = "Basic " + Base64.getEncoder()
                        .encodeToString(creds.getBytes());
                httpClient = HttpClientBuilder.create().build();
                httpPost = new HttpPost(SisVars.wsEmail + "enviarCorreo");
                httpPost.setEntity(new StringEntity(gson.toJson(correo), "UTF-8"));
                httpPost.setHeader("Content-type", "application/json; charset=utf-8");
                httpPost.setHeader("Authorization", auth);
                try {
                    httpClient.execute(httpPost);

                } catch (IOException ex) {
                    Logger.getLogger(KatalinaEjb.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };

        data.start();
    }
    
    @Override
    public Correo reenviarCorreo(Correo correo) {
        return (Correo)  methodPOST(correo, SisVars.wsEmail + "correo/reenviar", Correo.class);
    }

    @Override
    public String getTaskIdFromNumTramite(Long tramite) {
        String taskId;
        try {
            TareasActivas tareasActivas = tareasActivasService.findTaskIdByNumTramite(tramite);
            if (tareasActivas == null) {
                taskId = "";
            } else {
                taskId = tareasActivas.getTaskId();
            }
        } catch (Exception e) {
            //Logger.getLogger(RegistroPropiedadEjb.class.getName()).log(Level.SEVERE, null, e);
            taskId = "";
        }
        return taskId;
    }

    /**
     *
     * @param id --> Id de la Entidad servira para el nombre del reporte
     * @param reporte --> nombre del reporte x ejemplo:
     * "activos/actaEntregaRecepInventario"
     * @param parametros
     * @return
     * @throws SQLException
     */
    @Override
    public String buildJasper(Long id, String reporte, Map parametros) throws SQLException {
        Connection conn = null;
        String filePdf = null;
        try {
            JasperPrint jasperPrint;
            String ruta = rutas.getRootPath() + "reportes" + reporte + ".jasper";
            filePdf = Utils.createDirectoryIfNotExist(valoresService.findByCodigo("REPOSITORIO_ARCHIVO")) + reporte + "_" + id.toString() + ".pdf";
            conn = entityManagerService.getConnection();
            jasperPrint = JasperFillManager.fillReport(ruta, parametros, conn);
            JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
            return filePdf;
        } catch (JRException je) {
            Logger.getLogger(KatalinaEjb.class.getName()).log(Level.SEVERE, "File " + filePdf, je);
            return null;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * METHODS APIS
     */
    @Override
    public Object methodPOST(Object data, String url, Class resultClass) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(new StringEntity(gson.toJson(data), "UTF-8"));
        httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        httpPost.setHeader("Authorization", null);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                StringBuilder sb;
                try ( BufferedReader in = new BufferedReader(
                        new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"))) {
                    String inputLine;
                    sb = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        sb.append(inputLine);
                    }
                }
                try {
                    return gson.fromJson(sb.toString(), resultClass);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    Logger.getLogger(KatalinaEjb.class.getName()).log(Level.SEVERE, null, e);
                    return sb.toString();
                }
            } else {
                return null;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException | IOException ex) {
            ex.printStackTrace();
            Logger.getLogger(KatalinaEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List methodListGET(String url, Class resultClazz) {
        try {
            restTemplate = new RestTemplate(Utils.getClientHttpRequestFactory("", ""));
            Object[] obj = (Object[]) restTemplate.getForObject(new URI(url), resultClazz);
            if (obj != null) {
                return Arrays.asList(obj);
            } else {
                return null;
            }
        } catch (URISyntaxException | RestClientException e) {
            LOG.log(Level.SEVERE, "", e);
        }
        return null;
    }

    @Override
    public Boolean descargarEtiqueta(String url) {
        boolean accion = false;
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(url);
        Future<HttpResponse> futureResponse = executorService.submit(() -> httpClient.execute(httpPost));
        try {
            HttpResponse httpResponse = futureResponse.get(30, TimeUnit.SECONDS);
            if (httpResponse != null) {
                accion = true;
            } else {
                accion = false;
            }
        } catch (InterruptedException | ExecutionException | TimeoutException ex) {
            ex.printStackTrace();
            Logger.getLogger(KatalinaEjb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return accion;
    }

    @Override
    public String addZIP(List<File> files, String etiqueta) {
        try {
//            File fzip = new File(valoresService.findByCodigo("FILE_ZIP") + etiqueta + ".zip");
            File fzip = new File(SisVars.RUTA_IMAGES_QR + etiqueta + ".zip");
            if (!files.isEmpty()) {
                try ( // Crear archivo zip
                         ZipOutputStream out = new ZipOutputStream(new FileOutputStream(fzip))) {
                    for (File file : files) {
                        out.putNextEntry(new ZipEntry(file.getName()));
                        out.write(IOUtils.toByteArray(file.toURI()));
                        out.closeEntry();
                    }
                }
                return fzip.getAbsolutePath();
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "create zip etiquetas", e);
        }
        return null;
    }
}
