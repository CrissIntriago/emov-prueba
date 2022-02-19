package com.origami.sigef.tesoreria.comprobantelectronico.sri.util;

import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.model.Rdep;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.ComprobanteSRI;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.FirmaDocElectronico;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.RespuestaSolicitudSRI;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.logic.EnvioComprobantesWs;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Autorizacion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.RespuestaComprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.factura.Factura;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notacredito.NotaCredito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.notadebito.NotaDebito;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.retencion.ComprobanteRetencion;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.Comprobante;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.Mensaje;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.model.ws.RespuestaSolicitud;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.io.FilenameUtils;

public class DocumentosUtil {

    private static final String INIT_FORMAT = "yyyy-MM-dd";
    private static final Logger LOG = Logger.getLogger(DocumentosUtil.class.getName());

    public static String fechaEmision(String fechaEmision, String newFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(INIT_FORMAT);
        Date d = null;
        try {
            d = simpleDateFormat.parse(fechaEmision);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        simpleDateFormat.applyPattern(newFormat);
        return simpleDateFormat.format(d);
    }

    public static String generarClaveAcceso(FirmaDocElectronico firmaDocElectronico,
            ComprobanteElectronico comprobanteElectronico, String secuencialComprobante) {
        Modulo11 mo = new Modulo11();
        String claveAcceso;
        try {
            Random r = new Random();
            int numero = r.nextInt(firmaDocElectronico.getEntidad().getValorMax())
                    + firmaDocElectronico.getEntidad().getValorMin();

            claveAcceso = fechaEmision(comprobanteElectronico.getCabecera().getFechaEmision(), "ddMMyyyy")
                    + firmaDocElectronico.getComprobantes().getCodigo()
                    + firmaDocElectronico.getEntidad().getRucEntidad()
                    + firmaDocElectronico.getAmbiente().getCodigo()
                    + firmaDocElectronico.getCajero().getEntidad().getEstablecimiento()
                    + firmaDocElectronico.getCajero().getPuntoEmision()
                    + secuencialComprobante
                    + numero
                    + firmaDocElectronico.getTipoEmision().getCodigo();
            System.out.println("claveAcceso: " + claveAcceso);
            String verificador = mo.digitoVerificador(claveAcceso).toString();
            claveAcceso = claveAcceso + verificador;

            if (claveAcceso.length() == 49) {
                return claveAcceso;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean validarPasswordCertificado(FirmaDocElectronico firmaDocElectronico) {
        try {
            KeyStore p12 = KeyStore.getInstance("pkcs12");
            FileInputStream fileInputStream = new FileInputStream(firmaDocElectronico.getFirma().getArchivo());
            p12.load(fileInputStream, firmaDocElectronico.getFirma().getPassword().toCharArray());
            return true;
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            JsfUtil.addWarningMessage("Error en Validación de Firma...", firmaDocElectronico.getFirma().getArchivo());
            return false;
        }
    }

    public static Boolean crearArchivo(Object comprobante, String pathArchivo) {
        if ((comprobante instanceof Factura)) {
            return Java2XML.xmlArchivoFactura((Factura) comprobante, pathArchivo);
        } else if ((comprobante instanceof NotaCredito)) {
            return Java2XML.xmlArchivoNotaDeCredito((NotaCredito) comprobante, pathArchivo);
        } else if ((comprobante instanceof NotaDebito)) {
            return Java2XML.xmlArchivoNotaDebito((NotaDebito) comprobante, pathArchivo);
        } else if ((comprobante instanceof ComprobanteRetencion)) {
            return Java2XML.xmlArchivoComprobanteRetencion((ComprobanteRetencion) comprobante, pathArchivo);
        } else if ((comprobante instanceof Rdep)) {
            return Java2XML.xmlArchivoFormulario107((Rdep) comprobante, pathArchivo);
        } else if ((comprobante instanceof IVA)) {
            return Java2XML.xmlArchivoATS((IVA) comprobante, pathArchivo);
        }
        return Boolean.FALSE;
    }

    @SuppressWarnings("unused")
    public static RespuestaSolicitud firmarXMLRecepcion(String pathJarFirmarXML, String pathCompletoArchivoAFirmar,
            String pathDirFirmados, FirmaDocElectronico firmaDocElectronico, String claveAcceso, Boolean isOnline) {

        System.out.println("cmd ");

        String cmd = "java -jar " + valueSeparator(pathJarFirmarXML) + "firmar_xml.jar "
                + valueSeparator(firmaDocElectronico.getFirma().getArchivo()) + " "
                + firmaDocElectronico.getFirma().getPassword() + " " + valueSeparator(pathCompletoArchivoAFirmar) + " "
                + pathDirFirmados + " " + claveAcceso + ".xml";
//        System.out.println("cmd " + cmd);
        try {
            /*
             * java -jar C:\sriComprobantes\firma_java\firmar_xml.jar
             * C:\sriComprobantes\comprobantes\generados\
             * 1905201701136008641000120010040000000374811277912.xml
             * C:\sriComprobantes\comprobantes\firmados\
             * C:\app_server\jsignpdf\jose_moises_mejia_salazar.p12
             * 1905201701136008641000120010040000000374811277912.xml
             */
            int resultCMD = Runtime.getRuntime().exec(cmd).waitFor();
//            System.out.println("resultCMD>>"+resultCMD);
            if (resultCMD == 0) {
                System.out.println("resultCMD " + resultCMD);
//                String archivoFirmado = valueSeparator(pathDirFirmados) + File.separator + claveAcceso + ".xml";
                String archivoFirmado = valueSeparator(pathDirFirmados) + claveAcceso + ".xml";
                /*
                 * C:\sriComprobantes\comprobantes\firmados\
                 * 1905201701136008641000120010040000000374811277912.xml
                 * https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantes?
                 * wsdl
                 */
                RespuestaSolicitud respuestaSolicitud;
                try {
                    System.out.println("isOnline: " + isOnline);
                    respuestaSolicitud = EnvioComprobantesWs.enviarArchivoXMLSRI(archivoFirmado,
                            firmaDocElectronico.getAmbiente().getWsUrlRecepcion(), isOnline);
                    System.out.println("respuestaSolicitud " + respuestaSolicitud);
                    if (respuestaSolicitud != null) {
                        try {
                            System.out.println("respuestaSolicitud try {" + respuestaSolicitud);
                            if (respuestaSolicitud.getComprobantes() != null && respuestaSolicitud.getComprobantes().getComprobante() != null) {
                                if (!respuestaSolicitud.getComprobantes().getComprobante().isEmpty() && respuestaSolicitud.getComprobantes().getComprobante().get(0) != null) {
                                    if (respuestaSolicitud.getComprobantes().getComprobante().get(0).getMensajes() != null) {
                                        if (!respuestaSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().isEmpty()
                                                && respuestaSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje() != null) {
                                            if (!respuestaSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().isEmpty()
                                                    && respuestaSolicitud.getComprobantes().getComprobante().get(0).getMensajes().getMensaje().get(0) != null) {
                                                respuestaSolicitud.setCodigoError(respuestaSolicitud.getComprobantes()
                                                        .getComprobante().get(0)
                                                        .getMensajes().getMensaje().get(0).getIdentificador());
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    respuestaSolicitud = new RespuestaSolicitud();
                    respuestaSolicitud.setEstado("SIN_CONEXION");
                }

                File file = new File(valueSeparator(pathCompletoArchivoAFirmar));
                /*if (file.exists()) {
					file.delete();
				//	file = null;
				}*/
                return respuestaSolicitud;
            } else {
                return null;
            }
        } catch (Throwable e) {

            LOG.log(Level.SEVERE, "Error firmando XML", e);
            return null;
        }
    }

    public static RespuestaComprobante autorizacionXMLSRI(String claveacceso, String pathDirAutorizados,
            String pathDirRechazados, String wsUrlAutorizacion, Boolean isOnline) {
        try {
            /*
             * 1905201701136008641000120010040000000374811277912
             * C:\sriComprobantes\comprobantes\autorizacion\
             * 1905201701136008641000120010040000000374811277912.xml
             * C:\sriComprobantes\comprobantes\enviados\rechazados\
             * 1905201701136008641000120010040000000374811277912.xml
             * https://celcer.sri.gob.ec/comprobantes-electronicos-ws/
             * AutorizacionComprobantes?wsdl
             *
             */
            return EnvioComprobantesWs.autorizarArchivoXMLSRI(claveacceso, pathDirAutorizados + claveacceso + ".xml",
                    pathDirRechazados + claveacceso + ".xml", wsUrlAutorizacion, isOnline);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String valueSeparator(String value) {
        return FilenameUtils.separatorsToSystem(value);
    }

    public static String archivoToString(String rutaArchivo) {
        StringBuilder buffer = new StringBuilder();
        try {
            FileInputStream fis = new FileInputStream(rutaArchivo);
            InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
            Reader in = new BufferedReader(isr);
            int ch;
            while ((ch = in.read()) > -1) {
                buffer.append((char) ch);
            }
            in.close();
            return buffer.toString().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] convertirArchivoAByteArray(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        InputStream ios = null;
        try {
            ios = new FileInputStream(file);
            if (ios.read(buffer) == -1) {
                throw new IOException("EOF reached while trying to read the whole file");
            }
        } catch (Exception ex) {
            buffer = null;
        } finally {
            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }
        return buffer;
    }

    public static List<RespuestaSolicitudSRI> loadValuesRespuestaSolicitudSRI(RespuestaSolicitud respuestaSolicitud) {
        List<RespuestaSolicitudSRI> respuestaSolicitudSRIList = new ArrayList<>();
        RespuestaSolicitudSRI respuestaSolicitudSRI = null;
        List<Comprobante> lstComprobantes = respuestaSolicitud.getComprobantes().getComprobante();
        if (lstComprobantes != null && !lstComprobantes.isEmpty()) {
            for (Comprobante comprobante : lstComprobantes) {
                List<Mensaje> lstMensajes = comprobante.getMensajes().getMensaje();
                for (Mensaje mensaje : lstMensajes) {
                    respuestaSolicitudSRI = new RespuestaSolicitudSRI();
                    respuestaSolicitudSRI.setEstado(respuestaSolicitud.getEstado());
                    respuestaSolicitudSRI.setIdentificador(mensaje.getIdentificador());
                    respuestaSolicitudSRI.setInformacionAdicional(mensaje.getInformacionAdicional());
                    respuestaSolicitudSRI.setMensaje(mensaje.getMensaje());
                    respuestaSolicitudSRI.setTipo(mensaje.getTipo());
                }
            }
        } else {
            respuestaSolicitudSRI = new RespuestaSolicitudSRI();
            respuestaSolicitudSRI.setEstado(respuestaSolicitud.getEstado());
        }
        respuestaSolicitudSRIList.add(respuestaSolicitudSRI);
        return respuestaSolicitudSRIList;
    }

    public static List<RespuestaSolicitudSRI> loadValuesRespuestaComprobanteSRI(
            RespuestaComprobante respuestaComprobante) {
        List<RespuestaSolicitudSRI> respuestaSolicitudSRIList = new ArrayList<>();
        RespuestaSolicitudSRI respuestaSolicitudSRI = null;
        List<Autorizacion> listaAutorizaciones = respuestaComprobante.getAutorizaciones().getAutorizacion();
        List<com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Mensaje> lstMensajes;
        for (Autorizacion autorizacion : listaAutorizaciones) {
            lstMensajes = autorizacion.getMensajes().getMensaje();

            if (!lstMensajes.isEmpty()) {
                for (com.origami.sigef.tesoreria.comprobantelectronico.sri.model.autorizacion.Mensaje mensaje : lstMensajes) {
                    respuestaSolicitudSRI = new RespuestaSolicitudSRI();
                    respuestaSolicitudSRI.setNumeroComprobantes(respuestaComprobante.getNumeroComprobantes());
                    respuestaSolicitudSRI.setEstado(autorizacion.getEstado());
                    respuestaSolicitudSRI.setIdentificador(mensaje.getIdentificador());
                    respuestaSolicitudSRI.setInformacionAdicional(mensaje.getInformacionAdicional());
                    respuestaSolicitudSRI.setMensaje(mensaje.getMensaje());
                    respuestaSolicitudSRI.setTipo(mensaje.getTipo());
                    respuestaSolicitudSRIList.add(respuestaSolicitudSRI);
                }
            } else {
                respuestaSolicitudSRI = new RespuestaSolicitudSRI();
                respuestaSolicitudSRI.setNumeroComprobantes(respuestaComprobante.getNumeroComprobantes());
                respuestaSolicitudSRI.setEstado(autorizacion.getEstado());
                respuestaSolicitudSRIList.add(respuestaSolicitudSRI);
            }
        }
        return respuestaSolicitudSRIList;
    }

    /// CREA RIDE
    @SuppressWarnings("rawtypes")
    public static void generarPDFFacturacionElectronica(ComprobanteSRI comprobanteSRI, String rutaReporte) {
        try {
            Map<String, Object> parametros = new HashMap<>();
            String nombreReporte = null;
            List dataSource = null;
            System.out.println("nota credito "+comprobanteSRI.getCodigoTipoComprobante());
            switch (comprobanteSRI.getCodigoTipoComprobante()) {
                case "01":
                    nombreReporte = "factura";
                    dataSource = comprobanteSRI.getDetalleFactura();
                    parametros.put("PAGOS", comprobanteSRI.getPagoDetalle());
                    parametros.put("RMUNICIPIO", comprobanteSRI.getRubrosMunicipio());
                    parametros.put("RTERCERO", comprobanteSRI.getRubrosTercero());
                    parametros.put("MES", Utils.convertirMesALetra(Utils.getMes(new Date())) + " " + Utils.getAnio(new Date()));
                    break;
                case "04":
                    nombreReporte = "notaCredito";
                    dataSource = comprobanteSRI.getDetalleFactura();
                    parametros.put("DOC_MODIFICADO", comprobanteSRI.getDescripcionComprobanteModifica());
                    parametros.put("NUM_DOC_MODIFICADO", comprobanteSRI.getNumComprobanteModifica());
                    parametros.put("FECHA_EMISION_DOC_SUSTENTO", comprobanteSRI.getFechaEmisionDocumentoModifica());
                    parametros.put("RAZON_MODIF", comprobanteSRI.getMotivoNotaCredito());
                    break;
                case "05":
                    nombreReporte = "notaDebito";
                    dataSource = comprobanteSRI.getMotivosNotaDebito();
                    parametros.put("DOC_MODIFICADO", comprobanteSRI.getDescripcionComprobanteModifica());
                    parametros.put("NUM_DOC_MODIFICADO", comprobanteSRI.getNumComprobanteModifica());
                    parametros.put("FECHA_EMISION_DOC_SUSTENTO", comprobanteSRI.getFechaEmisionDocumentoModifica());
                    parametros.put("PAGOS", comprobanteSRI.getPagoDetalle());
                    break;
                case "07":
                    nombreReporte = "comprobanteRetencion";
                    parametros.put("EJERCICIO_FISCAL", comprobanteSRI.getPeriodo());
                    dataSource = comprobanteSRI.getImpuestoComprobanteRetencion();
                    break;
                default:
                    break;

            }
            System.out.println("ruta "+rutaReporte+nombreReporte);
            parametros.put("RUC", comprobanteSRI.getEntidad().getIdentificacion());
            parametros.put("NUM_FACT", comprobanteSRI.getNumFacturaFormato());
            parametros.put("NUM_AUT", comprobanteSRI.getNumAutorizacion());
            parametros.put("FECHA_AUT", comprobanteSRI.getFechaAutorizacion());
            parametros.put("AMBIENTE", comprobanteSRI.getAmbiente());
            parametros.put("TIPO_EMISION", comprobanteSRI.getEmision());
            parametros.put("CLAVE_ACC", comprobanteSRI.getClaveAcceso());
            parametros.put("RAZON_SOCIAL", comprobanteSRI.getEntidad().getNombres());
            parametros.put("NOM_COMERCIAL", comprobanteSRI.getEntidad().getNombreComercial());
            parametros.put("DIR_MATRIZ", comprobanteSRI.getEntidad().getDireccionMatriz());
            parametros.put("DIR_SUCURSAL", comprobanteSRI.getEntidad().getDireccionSucursal());
            parametros.put("CONT_ESPECIAL", comprobanteSRI.getEntidad().getContribuyenteEspecial());
            parametros.put("LLEVA_CONTABILIDAD", comprobanteSRI.getEntidad().getObligadoContabilidad());
            parametros.put("RS_COMPRADOR", comprobanteSRI.getContribuyente().getNombres());
            parametros.put("RUC_COMPRADOR", comprobanteSRI.getContribuyente().getIdentificacion());
            parametros.put("FECHA_EMISION", comprobanteSRI.getFechaEmision());
            parametros.put("DIRECCION", comprobanteSRI.getContribuyente().getDireccion());
            parametros.put("TELEFONO", comprobanteSRI.getContribuyente().getTelefono());
            parametros.put("CORREO", comprobanteSRI.getContribuyente().getEmail());
            parametros.put("TRAMITE", comprobanteSRI.getTramite());
            parametros.put("LOGO", comprobanteSRI.getEntidad().getLogo());
            parametros.put("subTotal12", comprobanteSRI.getSubTotal12());
            parametros.put("subTotalIva", comprobanteSRI.getSubTotalIva());
            parametros.put("subTotalNoObjetoIva", comprobanteSRI.getSubTotalNoObjetoIva());
            parametros.put("subTotalExcentoIva", comprobanteSRI.getSubTotalExcentoIva());
            parametros.put("subTotalSinImpuetos", comprobanteSRI.getSubTotalSinImpuetos());
            parametros.put("descuento", comprobanteSRI.getDescuento());
            parametros.put("ice", comprobanteSRI.getIce());
            parametros.put("iva", comprobanteSRI.getIva());
            parametros.put("irbpnr", comprobanteSRI.getIrbpnr());
            parametros.put("propina", comprobanteSRI.getPropina());
            parametros.put("valorTotal", comprobanteSRI.getValorTotal());
            parametros.put("valorSinSubSidio", comprobanteSRI.getValorSinSubSidio());
            parametros.put("ahorroPorSubSidio", comprobanteSRI.getAhorroPorSubSidio());
            //parametros.put("descuentoAdicional", comprobanteSRI.getDescuentoAdicional());
            // ss.setTieneDatasource(Boolean.FALSE);
            try {
                /// GERENERA EL REPORTE
                JRDataSource jrDataSource = new JRBeanCollectionDataSource(dataSource);
                JasperPrint jasperPrint = JasperFillManager.fillReport(rutaReporte + nombreReporte + ".jasper",
                        parametros, jrDataSource);
                OutputStream output = new FileOutputStream(new File(comprobanteSRI.getPdfPath()));
                JasperExportManager.exportReportToPdfStream(jasperPrint, output);

            } finally {

            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

}
