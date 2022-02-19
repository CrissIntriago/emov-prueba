package com.origami.sigef.common.service.interfaces;

import com.origami.sigef.common.entities.doc.DocumentoBlob;
import com.origami.sigef.common.models.Documento;
import com.origami.sigef.resource.procesos.entities.HistoricoTramites;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import org.primefaces.model.UploadedFile;

@Local
public interface FileUploadDoc {

    public void upload(HistoricoTramites tramite, UploadedFile file);

    public void upload(HistoricoTramites tramite, List<UploadedFile> files);

    public List<Documento> getTramiteFile(String descripcion, Long numTramite);

    public int viewDocumento(OutputStream outStream, Long oid);

    public InputStream viewDocumento(Long oid);

    public void upload(Object entitiRelationFile, List<UploadedFile> files);

    public List<Documento> fileEntiti(Object entiti);

    public boolean eliminar(Documento d);
    
    public boolean uploadRepositorio(Object entitiRelationFile, List<UploadedFile> files, Map<String, Object> mp);

    public int viewDocumentoRepositorio(OutputStream out, DocumentoBlob blob);
    
    public List<DocumentoBlob> fileEntitiRepositorio(Object entiti);
    
    public boolean eliminarRepositorio(DocumentoBlob d);

}
