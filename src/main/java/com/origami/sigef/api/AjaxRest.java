/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.api;

import com.gestionTributaria.Commons.SisVars;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.origami.sigef.activos.service.OrdenRequisicionService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.converter.ZPLConveter;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.entities.Zebra;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQr;
import com.origami.sigef.common.entities.ws.qr_services.DetalleQrResponse;
import com.origami.sigef.common.model.Imagenes;
import com.origami.sigef.common.model.UsuarioModel;
import com.origami.sigef.common.service.AclLoginService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UsuarioService;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * REST Web Service
 *
 * @author jesus
 */
@Path(value = "/ajax/")
@Produces({"application/Json", "text/xml"})
@ApplicationScope
public class AjaxRest extends BpmnBaseRoot implements Serializable {

    @Inject
    private UsuarioService usuarioService;
    @Inject
    private AclLoginService aclLoginService;
    @Inject
    private OrdenRequisicionService ordenRequisicionService;
    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ClienteService clienteService;

    /**
     * Creates a new instance of InventarioRest
     */
    public AjaxRest() {
    }

    @GET
    @Path("/iniciarSesion/usuario/{user}/contrasenia/{contrasenia}")
    @Consumes(MediaType.APPLICATION_JSON)
    public UsuarioModel iniciarSesion(@PathParam("user") String user,
            @PathParam("contrasenia") String contrasenia) {
        Usuarios usuario = null;
        UsuarioModel usuarioModel = null;
        UsernamePasswordToken token = null;
        try {
            Subject subject = SecurityUtils.getSubject();
            token = new UsernamePasswordToken(user, contrasenia);
            token.setRememberMe(false);
            subject.login(token);
            if (subject.getPrincipal() != null && !subject.getPrincipal().toString().isEmpty()) {
                usuario = usuarioService.findByUsuario(user);
                if (usuario != null) {
                    usuarioModel = new UsuarioModel(usuario.getId(), usuario.getUsuario(), usuario.getContrasenia(),
                            usuario.getFuncionario().getPersona().getIdentificacion(),
                            usuario.getFuncionario().getPersona().getRuc(), usuario.getFuncionario().getPersona().getNombre(),
                            usuario.getFuncionario().getPersona().getApellido());
                }
            }
        } catch (LockedAccountException | UnknownAccountException | IncorrectCredentialsException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (token != null) {
                token.clear();
            }
        }
        return usuarioModel;
    }

    @GET
    @Path("/tramite/{numTramite}")
    public String finalizarTramite(@PathParam("numTramite") Long numTramite)
            throws IOException, SQLException {

        String task = katalinaService.getTaskIdFromNumTramite(numTramite);
        if (!task.isEmpty()) {
            HashMap<String, Object> par = new HashMap<>();
            this.completeTask(task, par);
        }

        return "OK";
    }

    @GET
    @Path("/tramite/{numTramite}/parametro/{parameter}")
    public String finalizarTramiteParam(@PathParam("numTramite") Long numTramite, @PathParam("parameter") Integer aprobado)
            throws IOException, SQLException {

        String task = katalinaService.getTaskIdFromNumTramite(numTramite);
        if (!task.isEmpty()) {
            HashMap<String, Object> par = new HashMap<>();
            par.put("aprobado", aprobado);
            par.put("usuario", clienteService.getrolsUser(RolUsuario.guardaAlmacen));
            this.completeTask(task, par);
        }

        return "OK";
    }

    @GET
    @Path("/resource/pdfImagenes/{archivo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<Imagenes> pdfToImagen(@PathParam("archivo") String archivo) {

        System.out.println("entrooo a pdfToImages");
        Float dpi = 25.4f;
        Float ot = 72f;

//        String pathFile = FilesUtil.replaceRutaArchivo(archivo);
        String pathFile = Utils.urlRuta(archivo);
        System.out.println("pathFile" + pathFile);
        List<Imagenes> files;
        files = new ArrayList<>();
        if (pathFile != null) {
            String fileName, tempName;
            BufferedImage bim;
            File file = new File(pathFile);
            try (final PDDocument document = PDDocument.load(file)) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                for (int page = 0; page < document.getNumberOfPages(); ++page) {
                    PDPage page1 = document.getPage(page);
                    //  Float w = document.getPage(page).getMediaBox().getWidth();
                    //(w * dpi) ;

                    //  Float h = document.getPage(page).getMediaBox().getHeight();
                    //System.out.println(String.format("w: %f, h: %f", w, h));
                    int w = Float.valueOf(page1.getMediaBox().getWidth()).intValue();
                    int h = Float.valueOf(page1.getMediaBox().getHeight()).intValue();
//                    int w = Double.valueOf(((page1.getMediaBox().getWidth() * 25.4 / 72) * 291) / 25.4).intValue();
//                    int h = Double.valueOf(((page1.getMediaBox().getHeight() * 25.4 / 72) * 291) / 25.4).intValue();
                    bim = pdfRenderer.renderImageWithDPI(page, 291, ImageType.RGB);
                    bim = Utils.resize(bim, w, h);
                    tempName = file.getName().replace(".pdf", "") + "_" + page + ".png";
                    fileName = SisVars.RUTA_IMAGES_TEMP + tempName;
                    files.add(new Imagenes("Pagina # " + page, SisVars.appQrService + "imagenTEMP/" + tempName));
                    ImageIOUtil.writeImage(bim, fileName, 291);
                }
                document.close();

            } catch (IOException e) {
                files.add(new Imagenes("Pagina # 1", SisVars.appQrService + "images/sin_resultados.png"));
                System.err.println("Exception while trying to create pdf document - " + e);
            }
        } else {
            files.add(new Imagenes("Pagina # 1", SisVars.appQrService + "images/sin_resultados.png"));
        }
        return files;
    }

    @GET
    @Path("/imagenTEMP/{name}")
    @Produces("image/png")
    public byte[] getImageTem(@PathParam("name") String name) throws FileNotFoundException, IOException {
        File initialFile = new File(SisVars.RUTA_IMAGES_TEMP + name);
        InputStream targetStream = new FileInputStream(initialFile);
        return IOUtils.toByteArray(targetStream);
    }

    @POST
    @Path(value = "/generarQREtiquetas")
    @Consumes(MediaType.APPLICATION_JSON)
    public DetalleQrResponse generarCodigoQr(String json) {
        try {
            System.out.println("json: " + json);
            DetalleQr detalleQr = new Gson().fromJson(json, DetalleQr.class);
            Date date = new Date();
            String nameImage = "";
            if (detalleQr.getNombreImagen() != null && !detalleQr.getNombreImagen().isEmpty()) {
                nameImage = detalleQr.getNombreImagen();
            } else {
                nameImage = detalleQr.getIdAndCodigo() + "-" + date.getTime() + ".png";
            }
            detalleQr.setArchivo(detalleQr.getArchivo() + nameImage);
            if (generate(detalleQr)) {
                DetalleQrResponse response = new DetalleQrResponse(SisVars.appQrService + "imagen/" + nameImage, nameImage, detalleQr.getIdAndCodigo(), detalleQr.getArchivo());
                return response;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    int qrWidth = 290;
    int qrHeight = 260;

    int barcodeWidth = 510;
    int barcodeHeight = 100;

    int posicionQrX = 525; //A LOS LADOS
    int posicionQrY = 15;  //ARRIBA Y ABAJO

    int posicionBarcodeX = 150; //A LOS LADOS
    int posicionBarcodeY = 440;  //ARRIBA Y ABAJO

    int posicionIdX = 260; //A LOS LADOS
    int posicionIdY = 570;  //ARRIBA Y ABAJO

    int positionBodegaX = 40; //A LOS LADOS
    int positionBodegaY = 275; //ARRIBA Y ABAJO

    int lineWidth = 650;

    public Boolean generate(DetalleQr detalleQr) {

        BitMatrix bitMatrix;
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        QRCodeWriter writer = new QRCodeWriter();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        try {
            bitMatrix = writer.encode(detalleQr.getIdAndCodigo(), BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
            BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());
            System.out.println("codigo item " + detalleQr.getCodigoItem());
            System.out.println("detalleQr.getTipo() " + detalleQr.getTipo());
            BufferedImage barcodeImage = generateEAN13BarcodeImage(detalleQr.getCodigoItem(), detalleQr.getTipo().equals("PERCHA") ? barcodeHeight : 90);
            File imageFile = new File(detalleQr.getRutaArchivoPlantilla()); //plantilla.png
            BufferedImage image = ImageIO.read(imageFile);
            Font courier = new Font("Courier New", 1, 47);
            Graphics2D g = (Graphics2D) image.getGraphics();
            g.setColor(Color.BLACK);
            g.setFont(courier);
            g.drawImage(qrImage, posicionQrX, posicionQrY, null);
            if (detalleQr.getTipo().equals("PERCHA")) {
                courier = new Font("Courier New", 1, 25);
                g.setFont(courier);
                drawString(g, convertToTitleCaseSplitting(detalleQr.getBodega()) + "\n"
                        + convertToTitleCaseSplitting(detalleQr.getTitulo()) + "\n"
                        + convertToTitleCaseSplitting(detalleQr.getDescripcion()),
                        positionBodegaX, positionBodegaY);

                g.setColor(Color.BLACK);
                g.setFont(courier);
                g.drawImage(barcodeImage, posicionBarcodeX, posicionBarcodeY, null);
                courier = new Font("Courier New", 1, 24);
                g.setFont(courier);
                g.drawString(detalleQr.getCodigoItem(), posicionIdX, posicionIdY);
            }
            if (detalleQr.getTipo().equals("RFID")) {
                courier = new Font("Courier New", 1, 20);
                g.setFont(courier);
                drawString(g, convertToTitleCaseSplitting(detalleQr.getBodega()) + "\n"
                        + convertToTitleCaseSplitting(detalleQr.getTitulo()) + "\n"
                        + convertToTitleCaseSplitting(detalleQr.getDescripcion()) + "\n"
                        + convertToTitleCaseSplitting(detalleQr.getCodigoItem()),
                        positionBodegaX, positionBodegaY);
            }
            g.dispose();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

            ImageIO.write(image, "png", os);
            Files.copy(new ByteArrayInputStream(os.toByteArray()), Paths.get(detalleQr.getArchivo()), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private MatrixToImageConfig getMatrixConfig() {
        return new MatrixToImageConfig(Colors.BLACK.getArgb(), -1);
    }

    public enum Colors {

        BLUE(0xFF40BAD0),
        RED(0xFFE91C43),
        PURPLE(0xFF8A4F9E),
        ORANGE(0xFFF4B13D),
        WHITE(0xFFFFFFFF),
        BLACK(0xFF000000);

        private final int argb;

        Colors(final int argb) {
            this.argb = argb;
        }

        public int getArgb() {
            return argb;
        }
    }

    public int positionHorizontal(BufferedImage image, FontMetrics fm, String text) {
        return ((image.getWidth() - fm.stringWidth(text)) / 2);
    }

    public int positionVertical(BufferedImage image, FontMetrics fm) {
        return ((image.getHeight() - fm.getHeight()) / 2);
    }

    public BufferedImage generateEAN13BarcodeImage(String barcodeText, int heigh) throws Exception {
        Code128Writer barcodeWriter = new Code128Writer();
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, barcodeWidth, heigh);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private void drawString(Graphics2D g, String text, int x, int y) {
        int res = y;
        boolean primero = true;
        for (String line : text.split("\n")) {
            res = drawStringMultiLine(g, line, lineWidth, x, res) + 40;
        }
    }

    public int drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
        FontMetrics m = g.getFontMetrics();
        if (m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for (int i = 1; i < words.length; i++) {
                if (m.stringWidth(currentLine + words[i]) < lineWidth) {
                    currentLine += " " + words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if (currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
        return y;
    }

    public static String convertToTitleCaseSplitting(String text) {
        String WORD_SEPARATOR = " ";
        if (text == null || text.isEmpty()) {
            return text;
        }

        return Arrays
                .stream(text.split(WORD_SEPARATOR))
                .map(word -> word.isEmpty()
                ? word
                : (word.length() > 4 ? Character.toTitleCase(word.charAt(0)) + word
                .substring(1)
                .toLowerCase() : word.toLowerCase()))
                .collect(Collectors.joining(WORD_SEPARATOR));
    }

    @POST
    @Path(value = "/imprimir")
    @Consumes(MediaType.APPLICATION_JSON)
    public Zebra imprimirQr(String json) {
        try {
            Zebra zebraModel = new Gson().fromJson(json, Zebra.class);
            zebraModel.setEstadoImpresion(imprimir(zebraModel));
            return zebraModel;
        } catch (Exception e) {
            return null;
        }
    }

    public Boolean imprimir(Zebra zebraModel) {
        try {
            descargarImagen(zebraModel);
            BufferedImage orginalImage = ImageIO.read(new File(zebraModel.getArchivoGrabado()));
            ZPLConveter zp = new ZPLConveter();
            zp.setCompressHex(true);
            zp.setBlacknessLimitPercentage(50);
            String g = zp.convertfromImg(orginalImage); //" ^XA^FO250,200^AQN,50,50^FD SAMPLE ARIALI ^FS ^XZ";
            return getPrint(g, zebraModel.getImpresora(), zebraModel.getCantidad());    // name of network printer
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean getPrint(String zpl_data, String printer, Integer numberCopy) throws IOException {
        for (int i = 1; i <= numberCopy; i++) {
            try {
                PrintService service = PrinterOutputStream.getPrintServiceByName(printer);
                DocPrintJob job = service.createPrintJob();
                DocFlavor flvr = DocFlavor.BYTE_ARRAY.AUTOSENSE;
                Doc doc = new SimpleDoc(zpl_data.getBytes(), flvr, null);
                job.print(doc, null);
            } catch (PrintException e) {
                System.out.println(e.getCause());
                return false;
            }
        }
        return true;
    }

    private void descargarImagen(Zebra zebraModel) {
        String nombreArchivo = SisVars.RUTA_IMAGES_QR + zebraModel.getArchivo().substring(48);
        System.out.println("API DOWNLOAD IMAGEN: " + zebraModel.getArchivo());
        try (InputStream in = new URL(zebraModel.getArchivo()).openStream()) {
            Files.copy(in, Paths.get(nombreArchivo), REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        zebraModel.setArchivoGrabado(nombreArchivo);
    }

    @GET
    @Path("/imagen/{name}")
    @Produces("image/png")
    public Response getImage(@PathParam("name") String name) throws IOException {
        File file = new File(SisVars.RUTA_IMAGES_QR + name);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition",
                "attachment; filename=" + name);
        return response.build();
    }

}
