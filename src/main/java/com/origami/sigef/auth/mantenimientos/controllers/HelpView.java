/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.auth.mantenimientos.controllers;

import com.origami.sigef.common.service.ValoresService;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author ANGEL NAVARRO
 */
@Named
@ViewScoped
public class HelpView implements Serializable {

    /**
     *
     * 
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private ValoresService valoresService;

    private String data;
    private Boolean esVideo;
    private StreamedContent media;

    @PostConstruct
    public void initView() {
        data = "/resources/sigef/ma/MANUAL_DE_USUARIO_SISTEMA_ORIGAMI_GT.pdf";
        esVideo = false;
    }

    public void mediaFile(String data, Boolean esVideo) {
        if (esVideo) {
            setData(valoresService.findByCodigo("ATLANTIS_URL_MEDIA_VIDEOS_CAP") + data);
        } else {
            setData(data);
        }
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getEsVideo() {
        return esVideo;
    }

    public void setEsVideo(Boolean esVideo) {
        this.esVideo = esVideo;
    }

    public StreamedContent getMedia() {
        return media;
    }

    public void setMedia(StreamedContent media) {
        this.media = media;
    }

}
