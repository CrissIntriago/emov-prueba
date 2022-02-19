/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLocalCategoria;
import com.asgard.Entity.FinaRenLocalComercial;
import com.gestionTributaria.Entities.RenLocalComercialFoto;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.util.JsfUtil;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Administrator
 */
public class FotosLocalComercial {

    @Inject
    private ManagerService services;

    protected List<RenLocalComercialFoto> fotosLocalesComerciales;
    protected RenLocalComercialFoto localComercialFotoSeleccionada;
    protected FinaRenLocalComercial local;
    private Integer index;
    private Map<String, Object> parametros;

    protected Long idCategoria;

    public List<FinaRenLocalCategoria> getCategoriasHijas() {
        if (idCategoria != null) {
            return services.getCategoriasLocalesHijas(idCategoria);
        }
        return null;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public List<FinaRenLocalCategoria> getCategorias() {
        return services.getCategoriasLocales();
    }

    public void loadFotosLocalesComerciales() {
        parametros = new HashMap<>();
        parametros.put("localComercial", local.getId());
        if (local != null && local.getId() != null) {
            this.fotosLocalesComerciales = services.findAllQuery("select e from RenLocalComercialFoto e where e.localComercial = :localComercial", parametros);
        }
    }

    public void loadFotosLocalesComerciales(Long idLocal) {
        parametros = new HashMap<>();
        parametros.put("localComercial", idLocal);
        this.fotosLocalesComerciales = services.findAllQuery("select e from RenLocalComercialFoto e where e.localComercial = :localComercial", parametros);
    }

    public void handleFileUpload(FileUploadEvent event) {
//        //Long fileId = fserv.uploadFile(FilesUtil.copyFileServer1(event), event.getFile().getFileName(), event.getFile().getContentType());
//        fotoLocalComercial.setNombre(event.getFile().getFileName());
//        fotoLocalComercial.setContentType(event.getFile().getContentType());
//        if (local != null && local.getId() != null) {
//            fotoLocalComercial.setIdEntity(local.getId());
//        }
//        //   fotoLocalComercial.setFileId(fileId);
//        searchLocales(fotoLocalComercial.saveFotoLocalComercial());
//        //cargarFotos();
        JsfUtil.addInformationMessage("Nota1", "Foto guardada satisfactoriamente");
    }

    public void eliminarFoto(RenLocalComercialFoto localComercialFoto, Integer index) {
        if (services.quitarLocalComercial(localComercialFoto)) {
            loadFotosLocalesComerciales();
            fotosLocalesComerciales.remove(index);
            JsfUtil.update(":frmNew");
            JsfUtil.addInformationMessage("Nota!", "Foto eliminada satisfactoriamente.");
        } else {
            JsfUtil.addWarningMessage("Advertencia!", "No se pudo eliminar la foto seleccionada");
        }
    }

    public void searchLocales(Long idFoto) {
        RenLocalComercialFoto comercialFoto = services.find(RenLocalComercialFoto.class, idFoto);
        this.fotosLocalesComerciales.add(comercialFoto);
    }

    public void loadValueFotoLocal(RenLocalComercialFoto renLocalComercialFoto, Integer index) {
        this.index = index;
        this.localComercialFotoSeleccionada = renLocalComercialFoto;
        JsfUtil.executeJS("PF('dlgFoto').show()");
        JsfUtil.update("frmFotos");

    }

    public List<RenLocalComercialFoto> getFotosLocalesComerciales() {
        return fotosLocalesComerciales;
    }

    public void setFotosLocalesComerciales(List<RenLocalComercialFoto> fotosLocalesComerciales) {
        this.fotosLocalesComerciales = fotosLocalesComerciales;
    }

    public RenLocalComercialFoto getLocalComercialFotoSeleccionada() {
        return localComercialFotoSeleccionada;
    }

    public void setLocalComercialFotoSeleccionada(RenLocalComercialFoto localComercialFotoSeleccionada) {
        this.localComercialFotoSeleccionada = localComercialFotoSeleccionada;
    }

    public FinaRenLocalComercial getLocal() {
        return local;
    }

    public void setLocal(FinaRenLocalComercial local) {
        this.local = local;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }
    
    

}
