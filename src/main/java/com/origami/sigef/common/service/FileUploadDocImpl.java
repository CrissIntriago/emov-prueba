/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import com.gestionTributaria.Commons.SisVars;
import com.itextpdf.text.pdf.PdfReader;
import com.origami.sigef.common.entities.doc.DocumentoBlob;
import com.origami.sigef.common.models.Archivador;
import com.origami.sigef.common.models.ArchivadorMetadata;
import com.origami.sigef.common.models.Documento;
import com.origami.sigef.common.service.doc.DocumentoBlobService;
import com.origami.sigef.common.service.doc.DocumentoService;
import com.origami.sigef.common.service.interfaces.DatabaseLocal;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsonUtil;
import com.origami.sigef.common.util.ReflexionEntity;
import com.origami.sigef.common.util.SqlUtils;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc.PgConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ANGEL NAVARRO
 */
@Stateless
@javax.enterprise.context.Dependent
public class FileUploadDocImpl implements FileUploadDoc {

    private static final Logger LOG = Logger.getLogger(FileUploadDocImpl.class.getName());
    @Inject
    private DatabaseLocal conexionPool;
    @Inject
    private ValoresService valoresService;
    @Inject
    private UserSession session;
    @Inject
    private DocumentoBlobService documentoBlobService;
    @Inject
    private DocumentoService documentoService;

    private final String findDoc = "SELECT a.id, a.descripcion, a.archivador_padre FROM archivo.archivador a WHERE a.descripcion = ?;";
    private final String findDocMeta = "SELECT a.descripcion, am.id, am.campo_descripcion, am.campo_tipo, am.campo_orden, am.campo_key  "
            + "FROM archivo.archivador a INNER JOIN archivo.archivador_metadata am ON am.archivador = a.id WHERE a.id = ? ORDER BY am.campo_orden; ";
    private final String findDocTranite = "SELECT a.id, a.descripcion, a.archivador_padre FROM archivo.archivador a "
            + "LEFT JOIN archivo.archivador ap ON ap.id = a.archivador_padre WHERE a.descripcion = ? AND ap.id = ?";
    private final String newArchivador = "INSERT INTO archivo.archivador VALUES (DEFAULT, ?, ?, DEFAULT, now());";
    private final String findFileTramite = "SELECT d.id, d.descripcion, d.metadata, d.archivador, db.paginas, d.padre, d.size_doc, d.cre_usuario, d.cre_fecha, "
            + "db.page_size, db.\"extension\", db.archivo_nombre, db.blob_data, db.page_height, db.page_width "
            + "FROM archivo.documento d INNER JOIN archivo.documento_blob db ON db.documento = d.id "
            + "WHERE UPPER(regexp_replace(d.metadata, E'[\\\\n\\\\r]+', ' ', 'g' )) LIKE UPPER('%\"TIPO TRAMITE\": \":tipoTramite\"%\"NUMERO TRAMITE\": :numTramite %') "
            + " AND d.estado = 'ACT'";
    private final String findFiles = "SELECT d.id, d.descripcion, d.metadata, d.archivador, db.paginas, d.padre, d.size_doc, db.cre_usuario, db.cre_fecha, "
            + "db.page_size, db.\"extension\", db.archivo_nombre, db.blob_data, db.page_height, db.page_width "
            + "FROM archivo.documento d INNER JOIN archivo.documento_blob db ON db.documento = d.id  WHERE d.estado = 'ACT' AND EXTRACT(YEAR from db.cre_fecha) = " + Utils.getAnio(new Date()) + " AND d.descripcion = ?";
    private final String fDoc = "SELECT id FROM archivo.documento WHERE descripcion = ?";
    private final String newDoc = "INSERT INTO archivo.documento VALUES(DEFAULT, ?,?,?,?,?,NULL,?,DEFAULT,?,DEFAULT,DEFAULT,DEFAULT);";
    private final String newDocBlob = "INSERT INTO archivo.documento_blob(documento, blob_data, extension, archivo_nombre, archivo_orden, cre_usuario, ult_mod_usuario, page_size, paginas) VALUES(?,?,?,?,?,?,?,?,?);";
    private final String deleteBlob = "DELETE FROM archivo.documento_blob WHERE blob_data = ?";

    @Override
    public void upload(HistoricoTramites tramite, UploadedFile file) {
        try {
            Connection c = conexionPool.getConnection(null);
            if (c == null) {
                throw new RuntimeException("No se puedo conectar a la base de datos Documental");
            }
            if (tramite == null) {
                throw new RuntimeException("Tramite no debe ser nulo");
            }
            processFile(c, tramite.getTipoTramite().getCarpeta(), tramite.getNumTramite(),
                    Arrays.asList(file), tramite.getTipoTramite().getDescripcion(),
                    ReflexionEntity.getIdEntity(tramite));
            c.close();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Upload File Service", e);
        }
    }

    @Override
    public void upload(HistoricoTramites tramite, List<UploadedFile> files) {
        try {
            try ( Connection c = conexionPool.getConnection(null)) {
                if (c == null) {
                    throw new RuntimeException("No se puedo conectar a la base de datos Documental");
                }
                if (tramite == null) {
                    throw new RuntimeException("Tramite no debe ser nulo");
                }
                processFile(c, tramite.getTipoTramite().getCarpeta(), tramite.getNumTramite(),
                        files, tramite.getTipoTramite().getDescripcion(),
                        ReflexionEntity.getIdEntity(tramite));
                c.close();
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Upload File Service", e);
        }
    }

    private void processFile(Connection c, String carpeta, Long numTramite, List<UploadedFile> files, String tipoTramite, String descripcion) throws Exception {
        String repoTramite = valoresService.findByCodigo("REPOSITORIO_TRAMITE");
        Archivador a = new Archivador();
        a = SqlUtils.find(c, findDoc, Arrays.asList(repoTramite), a);
        List<ArchivadorMetadata> metadatas = null;
        metadatas = SqlUtils.findAll(c, findDocMeta, Arrays.asList(a.getId()), ArchivadorMetadata.class);
        if (metadatas != null) {
            Long id = a.getId();
            if (carpeta != null) {
                a = SqlUtils.find(c, findDocTranite, Arrays.asList(carpeta, a.getId()), a);
                if (a == null) {
                    id = SqlUtils.insert(c, newArchivador, Arrays.asList(a.getId(), repoTramite));
                } else {
                    id = a.getId();
                }
            }
            List pms = null;
            // verificamos si existe un registro con esee identificador
            Long idDoc = SqlUtils.find(c, fDoc, Arrays.asList(descripcion), null);
            if (idDoc == null) {
                JsonUtil js = new JsonUtil();
                Map<String, Object> mp = new HashMap<>();
                for (ArchivadorMetadata meta : metadatas) {
                    if (meta.getCampoKey().trim().equalsIgnoreCase("numTramite")) {
                        mp.put(meta.getCampoDescripcion(), numTramite);
                    } else if (meta.getCampoKey().equalsIgnoreCase("tipoTramite")) {
                        mp.put(meta.getCampoDescripcion(), tipoTramite);
                    }
                }
                pms = Arrays.asList(descripcion, js.toJson(mp), id, 0, 0, session.getName(), session.getName());
                idDoc = SqlUtils.insert(c, newDoc, pms);
            }
            if (idDoc != null) {
                int count = 1;
                int paginas = 1;
                for (UploadedFile file : files) {
                    if (file.getFileName().toLowerCase().endsWith("pdf")) {
                        PdfReader reader = new PdfReader(file.getInputstream());
                        paginas = reader.getNumberOfPages();
                    } else if (file.getFileName().toLowerCase().endsWith(".xls")) {
                        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputstream());
                        paginas = workbook.getNumberOfSheets();
                    } else if (file.getFileName().toLowerCase().endsWith(".xlsx")) {
                        XSSFWorkbook xwb = new XSSFWorkbook(file.getInputstream());
                        paginas = xwb.getNumberOfSheets();
                    } else if (file.getFileName().toLowerCase().endsWith(".docx")) {
                        XWPFDocument docx = new XWPFDocument(file.getInputstream());
                        paginas = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
                    } else if (file.getFileName().toLowerCase().endsWith(".pptx")) {
//                        XSLFSlideShow xdocument = new XSLFSlideShow(POIXMLDocument.openPackage(file.getContentType()));
                        XMLSlideShow xslideShow = new XMLSlideShow(file.getInputstream());
                        paginas = xslideShow.getSlides().size();
                    }/*else if (file.getFileName().toLowerCase().endsWith(".doc")) {
                        Document wordDoc = new Document(file.getInputstream());
                        return wordDoc.getSummaryInformation().getPageCount();
                    } else if (file.getFileName().toLowerCase().endsWith(".ppt")) {
                        HSLFSlideShow document = new HSLFSlideShow(file.getInputstream());
                        SlideShow slideShow = new SlideShow(document);
                        return slideShow.getSlides().length;
                    }*/
                    Long oid = uploadFile(c, file.getInputstream());
                    pms = Arrays.asList(idDoc, oid, file.getContentType(), file.getFileName(), count, session.getName(), session.getName(), file.getSize(), paginas);
                    SqlUtils.insert(c, newDocBlob, pms);
                    count++;
                }
            }
        } else {
            throw new Exception("Reporsitorio " + repoTramite + " no tiene metadatos");
        }
    }

    public Long uploadFile(Connection conn, InputStream stream) {
        Long oid = null;
        try {
            // All LargeObject API calls must be within a transaction block
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            PgConnection pgConn = null;
            if (conn.isWrapperFor(BaseConnection.class
            )) {
                pgConn = (PgConnection) ((BaseConnection) conn);
            } else {
                pgConn = (PgConnection) conn;
            }
            LargeObjectManager lobj = pgConn.getLargeObjectAPI();
            // Create a new large object
            oid = lobj.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
            // Open the large object for writing
            LargeObject obj = lobj.open(oid, LargeObjectManager.WRITE);
            // Copy the data from the file to the large object
            byte buf[] = new byte[2048];
            int s, tl = 0;
            while ((s = stream.read(buf, 0, 2048)) > 0) {
                obj.write(buf, 0, s);
                tl += s;
            }
            // Close the large object
            obj.close();
            stream.close();
            // Finally, commit the transaction.
            conn.commit();
//            conn.close();
        } catch (SQLException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return oid;
    }

    @Override
    public List<Documento> getTramiteFile(String tipoTramite, Long numTramite) {
//        String repoTramite = valoresService.findByCodigo("REPOSITORIO_TRAMITE");
        Connection c = conexionPool.getConnection(null);
        if (c != null) {
            try {
                String sql = findFileTramite.replace(":tipoTramite", tipoTramite);
                sql = sql.replace(":numTramite", tipoTramite);
                List<Documento> files = SqlUtils.findAll(c, sql, null, Documento.class
                );
                c.close();
                return files;
            } catch (SQLException ex) {
                Logger.getLogger(FileUploadDocImpl.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    @Override
    public int viewDocumento(OutputStream out, Long oid) {
        LargeObjectManager lobj;
        LargeObject obj = null;
        Connection conn = conexionPool.getConnection(null);
        int size = 0;
        try {
            if (conn == null) {
                return size;
            }
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            try {
                obj = lobj.open(oid, LargeObjectManager.READ);
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, null, e);
            }
            byte buf[] = new byte[2048];
            int s, tl = 0;
            while ((s = obj.read(buf, 0, 2048)) > 0) {
                out.write(buf, 0, s);
                tl += s;
            }
            size = obj.size();
            out.flush();
            obj.close();
            conn.commit();
        } catch (SQLException | IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return size;
    }

    @Override
    public InputStream viewDocumento(Long oid) {
        LargeObjectManager lobj;
        LargeObject obj = null;
        Connection conn = conexionPool.getConnection(null);
        InputStream buffer = null;
        try {
            if (conn == null) {
                return null;
            }
            conn.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
            try {
                obj = lobj.open(oid, LargeObjectManager.READ);
            } catch (SQLException e) {
                LOG.log(Level.SEVERE, null, e);
            }
            buffer = obj.getInputStream();
            obj.close();
            conn.commit();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
        return buffer;
    }

    @Override
    public void upload(Object entitiRelationFile, List<UploadedFile> files) {
        try {
            Connection c = conexionPool.getConnection(null);
            if (c == null) {
                throw new RuntimeException("No se puedo conectar a la base de datos Documental");
            }
            if (entitiRelationFile == null) {
                throw new RuntimeException("Entity no debe ser nulo");
            }
            processFile(c, "REPOSITORIO_GENERAL", entitiRelationFile, files);
            c.close();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Upload File Service", e);
        }
    }

    private void processFile(Connection c, String repoGeneral, Object entiti, List<UploadedFile> files) throws Exception {
        Archivador a = new Archivador();
        a = SqlUtils.find(c, findDoc, Arrays.asList(repoGeneral), a);
        Long id;
        if (a == null) {
            id = SqlUtils.insert(c, newArchivador, Arrays.asList(a.getId(), repoGeneral));
        } else {
            id = a.getId();
        }
        List pms = null;
        String desc = ReflexionEntity.getIdEntity(entiti);
        // verificamos si existe un registro con esee identificador
        Long idDoc = SqlUtils.find(c, fDoc, Arrays.asList(desc), null);
        if (idDoc == null) {
            JsonUtil js = new JsonUtil();
            Map<String, Object> mp = new HashMap<>();
            mp.put("ENTIDAD", entiti.getClass().getName());
            mp.put("IDENTIFICADOR", ReflexionEntity.getIdFromEntity(entiti));
            pms = Arrays.asList(desc, js.toJson(mp), id, 0, 0, session.getName(), session.getName());
            idDoc = SqlUtils.insert(c, newDoc, pms);
        }
        if (idDoc != null) {
            int count = 1;
            int paginas = 1;
            // Realizamos la subida de cada uno de los archivos a la base de datos se guarda como oid
            for (UploadedFile file : files) {
                if (file.getFileName().toLowerCase().endsWith("pdf")) {
                    PdfReader reader = new PdfReader(file.getInputstream());
                    paginas = reader.getNumberOfPages();
                } else if (file.getFileName().toLowerCase().endsWith(".xls")) {
                    HSSFWorkbook workbook = new HSSFWorkbook(file.getInputstream());
                    paginas = workbook.getNumberOfSheets();
                } else if (file.getFileName().toLowerCase().endsWith(".xlsx")) {
                    XSSFWorkbook xwb = new XSSFWorkbook(file.getInputstream());
                    paginas = xwb.getNumberOfSheets();
                } else if (file.getFileName().toLowerCase().endsWith(".docx")) {
                    XWPFDocument docx = new XWPFDocument(file.getInputstream());
                    paginas = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
                } else if (file.getFileName().toLowerCase().endsWith(".pptx")) {
//                        XSLFSlideShow xdocument = new XSLFSlideShow(POIXMLDocument.openPackage(file.getContentType()));
                    XMLSlideShow xslideShow = new XMLSlideShow(file.getInputstream());
                    paginas = xslideShow.getSlides().size();
                }/*else if (file.getFileName().toLowerCase().endsWith(".doc")) {
                        Document wordDoc = new Document(file.getInputstream());
                        return wordDoc.getSummaryInformation().getPageCount();
                    } else if (file.getFileName().toLowerCase().endsWith(".ppt")) {
                        HSLFSlideShow document = new HSLFSlideShow(file.getInputstream());
                        SlideShow slideShow = new SlideShow(document);
                        return slideShow.getSlides().length;
                    }*/
                Long oid = uploadFile(c, file.getInputstream());
                pms = Arrays.asList(idDoc, oid, file.getContentType(), file.getFileName(), 1, session.getName(), session.getName(), file.getSize(), paginas);
                SqlUtils.insert(c, newDocBlob, pms);
            }
        }
    }

    @Override
    public List<Documento> fileEntiti(Object entiti) {
        Connection c = conexionPool.getConnection(null);
        if (c != null) {
            try {
                String identificador = ReflexionEntity.getIdEntity(entiti);
                List<Documento> files = SqlUtils.findAll(c, findFiles, Arrays.asList(identificador), Documento.class);
                c.close();
                return files;
            } catch (SQLException ex) {
                Logger.getLogger(FileUploadDocImpl.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }

    /**
     *
     * @param d
     * @return
     */
    @Override
    public boolean eliminar(Documento d) {
        boolean deleteFile = deleteFile(d.getBlobData());
        try {
            Connection c = conexionPool.getConnection(null);
            if (deleteFile) {
                Long o = SqlUtils.execute(c, deleteBlob, Arrays.asList(d.getBlobData()));
            }
            c.close();
        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return deleteFile;
    }

    private boolean deleteFile(Long blobData) {
        try {
            Connection c = conexionPool.getConnection(null);
            // All LargeObject API calls must be within a transaction block
            c.setAutoCommit(false);
            // Get the Large Object Manager to perform operations with
            PgConnection pgConn = null;
            if (c.isWrapperFor(BaseConnection.class
            )) {
                pgConn = (PgConnection) ((BaseConnection) c);
            } else {
                pgConn = (PgConnection) c;
            }
            LargeObjectManager lobj = pgConn.getLargeObjectAPI();
            // Create a new large object
            lobj.delete(blobData);

            // Finally, commit the transaction.
            c.commit();

        } catch (SQLException ex) {
            LOG.log(Level.SEVERE, ex.getSQLState(), ex); //42704
            if (ex.getSQLState().equals("42704")) { //  No existe enviamos true
                return true;
            } else {
                return false;
            }
        }
        return true;

    }

    /*MODIFICAR DOCUMENTOS POR EL REPOSITORIO POR LA RUTA >> Sisvars.RUTA_REPOSITORIO*/
    /**
     *
     * @param entiti
     * @param files
     * @param mp
     * @throws Exception
     */
    private void processFile(Object entiti, List<UploadedFile> files, Map<String, Object> mp) throws Exception {
        try {
            String desc = ReflexionEntity.getIdEntity(entiti);
            // verificamos si existe un registro con esee identificador
            com.origami.sigef.common.entities.doc.Documento doc = new com.origami.sigef.common.entities.doc.Documento();
            doc.setDescripcion(desc);
            List<com.origami.sigef.common.entities.doc.Documento> allDoc = documentoService.findByExample(doc);
            if (Utils.isEmpty(allDoc)) {
                JsonUtil js = new JsonUtil();
                doc = new com.origami.sigef.common.entities.doc.Documento();
                doc.setBloqueado(Boolean.TRUE);
                /*Para q es está variable? 
                era para cuando elimino el archivo solo le cambio el estado creo que era para eso
                Entonces ahorita va True, está bien? Si*/
                doc.setCreFecha(new Date());
                doc.setCreUsuario(session.getNameUser());
                doc.setDescripcion(desc);
                doc.setEstado("ACT");
                if (mp != null) {
                    doc.setMetadata(js.toJson(mp));
                }
                doc.setPaginas(0);
                doc.setSizeDoc(0l);
                doc.setUltModFecha(new Date());
                doc.setUltModUsuario(session.getNameUser());
                doc = documentoService.create(doc);
            } else {
                doc = allDoc.get(0);
            }
            if (doc != null) {
                int count = 1;
                int paginas = 1;
                int counts = 0;
                int allPaginas = 0;
                // Realizamos la subida de cada uno de los archivos a la base de datos se guarda como oid
                for (UploadedFile file : files) {
//                    File fileTemp = file.getInputstream();
                    DocumentoBlob blob = new DocumentoBlob();
                    if (file.getFileName().toLowerCase().endsWith("pdf")) {
                        PdfReader reader = new PdfReader(file.getInputstream());
                        paginas = reader.getNumberOfPages();
                    } else if (file.getFileName().toLowerCase().endsWith(".xls")) {
                        HSSFWorkbook workbook = new HSSFWorkbook(file.getInputstream());
                        paginas = workbook.getNumberOfSheets();
                    } else if (file.getFileName().toLowerCase().endsWith(".xlsx")) {
                        XSSFWorkbook xwb = new XSSFWorkbook(file.getInputstream());
                        paginas = xwb.getNumberOfSheets();
                    } else if (file.getFileName().toLowerCase().endsWith(".docx")) {
                        XWPFDocument docx = new XWPFDocument(file.getInputstream());
                        paginas = docx.getProperties().getExtendedProperties().getUnderlyingProperties().getPages();
                    } else if (file.getFileName().toLowerCase().endsWith(".pptx")) {
                        XMLSlideShow xslideShow = new XMLSlideShow(file.getInputstream());
                        paginas = xslideShow.getSlides().size();
                    } else {
                        paginas = 1;
                    }
                    allPaginas += paginas;
                    UUID name = UUID.randomUUID();
//                    String fname = name.toString();
//                    String fname = (name.toString() + "." + FilenameUtils.getExtension(file.getFileName())).concat(".gz");
                    String fname = (name.toString() + ".gz");
                    String rutaDoc = SisVars.RUTA_REPOSITORIO + fname.concat(".ogt"); // extension temporal
                    counts += 1;
                    File gzipFile = gzipFile(file.getInputstream(), rutaDoc);
                    if (gzipFile != null) {
                        if (gzipFile.exists()) {
                            blob.setDocumento(doc);
                            blob.setArchivoNombre(file.getFileName());
                            blob.setArchivoOrden(Short.valueOf(count + ""));
                            blob.setBloqueado(Boolean.TRUE);
                            /*Que eso el ultimo , No entendi que se estado tambien iba a dar error El estado si está bien, si aqui en postgres es bool*/
                            blob.setCreUsuario(session.getNameUser());
                            blob.setCreFecha(new Date());
                            blob.setEstado("ACT");
                            blob.setExtension(file.getContentType());
                            blob.setPageHeight(0);
                            blob.setPageSize(0);
//                            blob.setPageSize(fileTemp.length());
                            blob.setPageWidth(0);
                            blob.setPaginas(paginas);
                            blob.setUltModFecha(new Date());
                            blob.setUltModUsuario(session.getNameUser());
                            blob.setUrlData(gzipFile.getAbsolutePath());
                            documentoBlobService.create(blob);
                        } else {
                            //System.out.println("No se pudo subir archivo.");
                        }
                    }
                    doc.setPaginas(allPaginas);
                    doc.setSizeDoc(Long.valueOf(counts + ""));
                    documentoService.edit(doc);
                }
            }
        } catch (NumberFormatException | IOException nfe) {
            LOG.log(Level.SEVERE, "", nfe);
        }
    }

    private File gzipFile(InputStream file, String destinatonZipFilepath) {
        byte[] buffer = new byte[1024];
        try {
            File fout = new File(destinatonZipFilepath);
            try ( GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(fout));  InputStream fis = file) {
                // copy file
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    gos.write(buffer, 0, len);
                }
            }
            return fout;
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Compimir Archivo", ex);
            return null;
        }
    }

    private void unGunzipFile(String compressedFile, OutputStream fileOutputStream) {
        byte[] buffer = new byte[1024];
        try {
            FileInputStream fileIn = new FileInputStream(compressedFile);
            try ( GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn)) {
                int bytes_read;
                while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, bytes_read);
                }
            }
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean uploadRepositorio(Object entitiRelationFile, List<UploadedFile> files, Map<String, Object> mp) {
        try {
            if (entitiRelationFile == null) {
                throw new RuntimeException("Entity no debe ser nulo");
            }
            processFile(entitiRelationFile, files, mp);
            return true;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Upload File Service", e);
            return false;
        }
    }

    @Override
    public int viewDocumentoRepositorio(OutputStream out, DocumentoBlob blob) {
        try {
            unGunzipFile(blob.getUrlData(), out);
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return blob.getPageSize().intValue();
    }

    @Override
    public List<DocumentoBlob> fileEntitiRepositorio(Object entiti) {
        try {
            String desc = ReflexionEntity.getIdEntity(entiti);
            com.origami.sigef.common.entities.doc.Documento d = new com.origami.sigef.common.entities.doc.Documento();
            d.setDescripcion(desc);
            List<com.origami.sigef.common.entities.doc.Documento> docs = documentoService.findByExample(d);
            List<DocumentoBlob> files = null;
            if (Utils.isNotEmpty(docs)) {
                if (files == null) {
                    files = new ArrayList<>();
                }
                for (com.origami.sigef.common.entities.doc.Documento doc : docs) {
                    files.addAll(doc.getDocumentoBlobs());
                }
            }
            return files;
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Archvo.", ex);
        }
        return null;
    }

    /**
     *
     * @param d
     * @return
     */
    @Override
    public boolean eliminarRepositorio(DocumentoBlob d) {
        boolean deleteFile = false;
        try {
            File f = new File(d.getUrlData());
            if (f.delete()) {
                documentoBlobService.remove(d);
                deleteFile = true;
            }
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        return deleteFile;
    }

}
