/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.servlet;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfBoolean;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DatosGeneralesEntidad;
import com.origami.sigef.common.service.EntityManagerService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.util.MimeTypeUtils;

/**
 *
 * @author origami-idea
 */
@WebServlet(name = "Documento", urlPatterns = {"/Documento"})
public class ViewDocumento extends HttpServlet {

    @Inject
    private ServletSession ss;
    @Inject
    private UserSession session;
    @Inject
    private EntityManagerService service;
    @Inject
    private ValoresService valoresService;
    private String reqURI;
    private Boolean localSave = false, download = false;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        Boolean mostrarFechaImpr = Boolean.valueOf(valoresService.findByCodigo("MOSTRAR_FECHA_IMPRESION"));
        String filename = null;
        Map parametros;
        JasperPrint jasperPrint;
        OutputStream outStream;
        Connection conn = null;
        try {
            response.addHeader("secure-av", "Secure");
            response.addHeader("httponly-av", "HttpOnly");
            response.addHeader("X-UA-Compatible", "IE=edge,chrome=1");
            if (ss.estaVacio() && ss.getNombreReporte() == null) {
                System.out.println("Sin parametros **" + ss.getNombreReporte());
                printError(response, null);
                return;
            }
            request.setCharacterEncoding("UTF-8");
            parametros = ss.getParametros();
            if (parametros == null) {
                parametros = new HashMap();
            }
            filename = ss.getNombreDocumento().concat(".").concat(ss.getContentType());
            filename = filename.replace(" ", "_");
//        System.out.println("parametros " + parametros);
            if (ss.getContentType().equalsIgnoreCase("pdf")) {
                response.setContentType("application/pdf; filename=" + filename + "; filename*= UTF-8''" + filename + "");
//                System.out.println("*********************************************AQUI ENTRO*********************************************");
            } else if (ss.getContentType().equalsIgnoreCase("xlsx")) {
                parametros.put("IS_IGNORE_PAGINATION", ss.getIsIgnorePaginator());
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; name=" + filename);
            } else if (ss.getContentType().equalsIgnoreCase("docx")) {
                response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document; name=" + filename);
            } else if (ss.getContentType().equalsIgnoreCase("png")) {
                response.setContentType(MimeTypeUtils.IMAGE_PNG_VALUE + ";name=" + filename);
            } else if (ss.getContentType().equalsIgnoreCase("jpg")) {
                response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE + ";name=" + filename);
            } else {
                response.setContentType(MimeTypeUtils.ALL_VALUE + ";name=" + filename);
            }
            localSave = Boolean.valueOf(parametros.get("localSave") + "");
            download = Boolean.valueOf(parametros.get("downland") + "");
            if (localSave || download) {
                response.addHeader("Content-Disposition", "attachment; name=title; filename=" + filename + "; file-download=" + filename);
            } else {
                response.addHeader("Content-Disposition", "inline; filename=" + filename + "; file-download=" + filename + "; filename*=UTF-8''" + filename);
            }
//            request.getHeaderNames().asIterator().forEachRemaining(System.out::println);
//            response.addHeader("Content-Disposition", "inline; name=\"title\"; filename=\"" + filename + "\"; file-download=\"" + filename + "\"");
//            String contentDisposition = "inline; filename=" + filename + "; filename*=UTF-8''" + filename;
//            System.out.println("contentDisposition " + contentDisposition);
//            response.addHeader("Content-Disposition", contentDisposition);
            String ruta = getServletContext().getRealPath(rutaReporte(ss.getNombreReporte(), ss.getNombreSubCarpeta()));
            if (session.getUsuario() != null) {
                if (session.getUsuario().getEmpresaId() == null) {
                    DatosGeneralesEntidad en = new DatosGeneralesEntidad();
                    en.setNombreEntidad(this.session.getTitlePage());
                    en.setUrlLogoReporte(getServletContext().getRealPath("/resources/barcelona-layout/images/logo-origami.png"));
                    session.getUsuario().setEmpresaId(en);
                }
            } else {
                System.out.println("Usuario null.");
            }
            parametros.put("MOSTRAR_FECHA_IMPRESION", mostrarFechaImpr);
            parametros.put("MARCA_AGUA", valoresService.findByCodigo("MARCA_AGUA"));
            parametros.put("MINISTERIO_TRABAJO", valoresService.findByCodigo("MINISTERIO_TRABAJO"));
            parametros.put("ALCALDIA", valoresService.findByCodigo("ALCALDIA"));
//            System.out.println("Usuario pm. " + parametros);
            if (!parametros.containsKey("SUBREPORT_DIR")) {
                parametros.put("SUBREPORT_DIR", getServletContext().getRealPath("/reportes/"));
            }
//            System.out.println("Logo " + session.getUsuario().getEmpresaId().getUrlLogoReporte());
            parametros.put("ENTIDAD", session.getUsuario().getEmpresaId());
            if (ss.getDataSource() == null) {
                conn = service.getConnection();
                jasperPrint = JasperFillManager.fillReport(ruta, parametros, conn);
            } else {
                JRDataSource dataSource = new JRBeanCollectionDataSource(ss.getDataSource());
                jasperPrint = JasperFillManager.fillReport(ruta, parametros, dataSource);
            }
            if (ss.getAgregarReporte()) {
                for (Map<String, Object> reporte : ss.getReportes()) {
                    ruta = getServletContext().getRealPath(rutaReporte((String) reporte.get("nombreReporte"), (String) reporte.get("nombreSubCarpeta")));
                    JasperPrint jasperPrint2 = JasperFillManager.fillReport(ruta, reporte, conn);
                    if (jasperPrint2.getPages() != null && jasperPrint2.getPages().size() > 0) {
                        jasperPrint.addPage(jasperPrint2.getPages().get(0));
                    }
                }
            }
            if (ss.getEncuadernacion() != null && ss.getEncuadernacion()) {
                encuadernar(jasperPrint, ss.getMargen());
            }
            if (!ss.getFondoBlanco()) {
//                waterMarkPdf(jasperPrint);
            }
            outStream = response.getOutputStream();
            if (ss.getFirmarDoc()) {
                this.firmar();
            } else {
                exportDoc(outStream, jasperPrint, ss.getImprimir(), ss.getContentType());
            }

            outStream.flush();
            outStream.close();
            ss.borrarDatos();
        } catch (Exception ex) {
            if (conn != null) {
                conn.close();
            }
            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, "Error el ejecutar reporte " + ss.getNombreReporte(), ex);
            printError(response, ex);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        reqURI = request.getContextPath() + "/";
        // reqURI = reqURI.substring(request.getContextPath().length());
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, "Get", ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
//        try {
//            processRequest(request, response);
//        } catch (SQLException ex) {
//            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

//<editor-fold defaultstate="collapsed" desc="Method Utils">
    private void printError(HttpServletResponse response, Exception ex) throws IOException {
        response.setContentType("text/html");
        try (PrintWriter salida = response.getWriter()) {
            salida.println("<!DOCTYPE html>\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:p=\"http://primefaces.org/ui\" >"
                    + "<head id=\"j_id_3\">\n"
                    + "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n"
                    + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n"
                    + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\" />\n"
                    + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
                    + "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/theme.css.xhtml?ln=primefaces-barcelona-green\" />"
                    + "<link type=\"text/css\" rel=\"stylesheet\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/fa/font-awesome.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/components.css.xhtml?ln=primefaces&amp;v=7.0\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/css/layout-blue.css.xhtml?ln=barcelona-layout\" />"
                    + "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + CONFIG.URL_APP + "/javax.faces.resource/css/layout-blue.css.xhtml?ln=barcelona-layout\" />"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/jquery/jquery.js.xhtml?ln=primefaces&amp;v=7.0\"></script>"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/jquery/jquery-plugins.js.xhtml?ln=primefaces&v=7.0\" ></script>"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/core.js.xhtml?ln=primefaces&amp;v=7.0\" ></script>"
                    + "<script type=\"text/javascript\" src=\"" + CONFIG.URL_APP + "/javax.faces.resource/components.js.xhtml?ln=primefaces&amp;v=7.0\" ></script>"
                    + "<title>Documento</title>"
                    + "</head>"
                    + "<body class=\"exception-body\">\n"
                    + "  <div class=\"exception-panel\"><img style=\"width: 100% !important;background: lavender; padding: 20px;\" id=\"j_id_89\" src=\"" + CONFIG.URL_APP + "/resources/barcelona-layout/images/landing/landing-main.png\" alt=\"\" />\n"
                    + "    <div class=\"line\"></div>\n"
                    + (ex == null ? "    <h1>Documento no encontrado</h1>\n" : (ex + "\n"))
                    + "    <p>Documento no se encuentra disponible, contacte al administrador.</p>"
                    //                    + "<p:commandLink value=\"INICIO\" onclick=\"window.open('" + CONFIG.URL_APP + "/inicio','_self')\" /> "
                    + "    <button id=\"j_id_a9\" name=\"j_id_a9\" type=\"button\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-text-only\" "
                    + "            onclick=\"window.open('" + CONFIG.URL_APP + "/inicio','_self')\">"
                    + "     <span class=\"ui-button-text ui-c\" style=\"color: black;\">INICIO</span>"
                    + "    </button> <br/><br/><br/><br/>"
                    //                                        + "    <script id=\"j_id_a_s\" type=\"text/javascript\">$(function(){PrimeFaces.ab(\"Button\",\"widget_j_id_a\",{id:\"j_id_a\"});});</script>\n"
                    + "  </div>"
                    + "</body>\n"
                    + "</html>");
        }
    }

    private void encuadernar(JasperPrint jasperPrint, Integer margen) {
        if (margen == null) {
            margen = 30;
        }
        List<JRPrintPage> pages = jasperPrint.getPages();
        JRPrintPage page;
        List<JRPrintElement> elements;
        for (int i = 1; i < pages.size() + 1; i++) {
            page = (JRPrintPage) pages.get(i - 1);
            elements = page.getElements();
            if (i % 2 != 0) {//IMPAR
                for (JRPrintElement temp : elements) {
                    temp.setX(temp.getX() + margen);
                }
            } else {//PAR
                for (JRPrintElement temp : elements) {
                    temp.setX(temp.getX() - margen);
                }
            }
        }
    }

    private String rutaReporte(String nombreReporte, String nombreSubCarpeta) {
        if (nombreSubCarpeta == null) {
            return "//reportes//" + nombreReporte + ".jasper";
        } else {
            return "//reportes//" + nombreSubCarpeta + "//" + nombreReporte + ".jasper";
        }
    }

    private void exportDoc(OutputStream outStream, JasperPrint jasperPrint, Boolean imprimir, String contentType) throws JRException {
        try {
            if (contentType.equalsIgnoreCase("pdf")) {
                JRPdfExporter pdfExporter = new JRPdfExporter();
                pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
                pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                SimplePdfExporterConfiguration pdfConfiguration = new SimplePdfExporterConfiguration();
                if (imprimir) {
                    pdfConfiguration.setPdfJavaScript("this.print({bUI: true, bSilent: false, bShrinkToFit: true});\r");
                }
                pdfConfiguration.setOverrideHints(Boolean.TRUE);
                pdfExporter.setConfiguration(pdfConfiguration);
                if (ss.getSaveFile() || localSave) {
                    try {
                        System.out.println("dirLocalNameFile >> " + ss.getParametros().get("dirLocalNameFile"));
                        if (ss.existeParametro("dirLocalNameFile")) {
                            JasperExportManager.exportReportToPdfFile(jasperPrint, ss.getParametros().get("dirLocalNameFile") + ".pdf");
                        } else {
                            String nameFile = "";
                            if (ss.getNombreDocumento() != null) {
                                nameFile = ss.getNombreDocumento();
                            } else {
                                nameFile = jasperPrint.getName() + "_" + new Date().getTime() + ".pdf";
                            }
                            System.out.println("File " + ss.getUrlServerFile() + nameFile);
                            JasperExportManager.exportReportToPdfFile(jasperPrint, ss.getUrlServerFile() + nameFile);
                        }
                    } catch (Exception fileNotFoundException) {
                        Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, null, fileNotFoundException);
                    }
                }
                if (ss.getImprimir()) {
//                    JasperPrintManager.printReport(jasperPrint, false);
                }
                pdfExporter.exportReport();
            } else if (contentType.equalsIgnoreCase("xlsx")) {
                JRXlsxExporter exporter = new JRXlsxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
                // Configuraciones del Reporte
                SimpleXlsxReportConfiguration xlsxConf = new SimpleXlsxReportConfiguration();
                xlsxConf.setRemoveEmptySpaceBetweenRows(true);
                xlsxConf.setDetectCellType(true);
                xlsxConf.setWrapText(true);
                xlsxConf.setRemoveEmptySpaceBetweenColumns(true);
                xlsxConf.setCollapseRowSpan(Boolean.TRUE);
                xlsxConf.setIgnoreAnchors(Boolean.TRUE);
                xlsxConf.setMaxRowsPerSheet(1045000);
                xlsxConf.setIgnoreGraphics(Boolean.TRUE);
                xlsxConf.setOverrideHints(Boolean.TRUE);
                xlsxConf.setOnePagePerSheet(ss.getOnePagePerSheet());
                xlsxConf.setWhitePageBackground(ss.getFondoBlanco());
                exporter.setConfiguration(xlsxConf);
                exporter.exportReport();
            } else if (contentType.equalsIgnoreCase("docx")) {
                JRDocxExporter exporter = new JRDocxExporter();
                exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outStream));
                // Configuraciones del Reporte
                SimpleDocxReportConfiguration docxConf = new SimpleDocxReportConfiguration();
                docxConf.setFlexibleRowHeight(true);
                exporter.setConfiguration(docxConf);
                exporter.exportReport();
            } else {
                JasperExportManager.exportReportToPdfStream(jasperPrint, outStream);
            }
        } catch (JRException je) {
            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, null, je);
        }
    }
//</editor-fold>

    private void firmar() {
        //                if (ss.getIdCertficado() == null) {
//                    filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaRepositorioArchivo)
//                            + ss.getNombreReporte() + "(" + new Date().getTime() + ").pdf";
//                    JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
//                    pdfFirmado = fd.tareaFirmaCertificado(filePdf);
//                } else {
//                    filePdf = Utils.createDirectoryIfNotExist(SisVars.rutaRepositorioArchivo)
//                            + ss.getNombreReporte() + ss.getIdCertficado() + ".pdf";
//                    JasperExportManager.exportReportToPdfFile(jasperPrint, filePdf);
//                    pdfFirmado = fd.tareaFirmaCertificado(filePdf, ss.getIdCertficado());
//                }
//                IOUtils.copy(new FileInputStream(pdfFirmado), outStream);
    }

    private void waterMarkPdf(OutputStream baos, InputStream pdfFileLocation) {
        try {
            BaseFont bf = null;
            PdfBoolean pdfBoolean_YES = new PdfBoolean(true);
            PdfReader pdfReader = new PdfReader(pdfFileLocation);
            PdfStamper pdfStamper = new PdfStamper(pdfReader, baos);
            PdfContentByte contentunder = pdfStamper.getUnderContent(1);

            contentunder.saveState();
//            contentunder.setColorFill(new Color(200, 200, 200));
            contentunder.beginText();
            bf = BaseFont.createFont(BaseFont.TIMES_ROMAN, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            contentunder.setFontAndSize(bf, 90);
            contentunder.showTextAligned(Element.ALIGN_MIDDLE, "  WaterMark Content", 200, 400, 45);
            contentunder.endText();
            contentunder.restoreState();

// We could stack those ViewerPreferences using '|' ... :)
            pdfStamper.addViewerPreference(PdfName.HIDETOOLBAR, pdfBoolean_YES);
            pdfStamper.addViewerPreference(PdfName.HIDEMENUBAR, pdfBoolean_YES);
//pdfStamper.addViewerPreference(PdfName.HIDEWINDOWUI, pdfBoolean_YES);

            pdfReader.close();
            pdfStamper.close();
//deleting existing file
//            FileUtil.delete(pdfFileLocation);

//            FileOutputStream fos = new FileOutputStream(pdfFileLocation);
//            baos.writeTo(pdfFileLocation);
//            fos.flush();
//close streams
//            baos.close();
//            fos.close();
        } catch (IOException ex) {
            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DocumentException ex) {
            Logger.getLogger(ViewDocumento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
