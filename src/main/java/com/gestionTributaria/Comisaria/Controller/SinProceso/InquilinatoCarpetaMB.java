/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package com.gestionTributaria.Comisaria.Controller.SinProceso;

import com.gestionTributaria.Comisaria.Service.InquilinatoCarpetaService;
import com.gestionTributaria.Entities.InquilinatoCarpeta;
import com.origami.sigef.common.lazy.LazyModel;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 *
 * @author ORIGAMIEC
 */
@Named(value = "inquilinatoCarpetaMB")
@ViewScoped
public class InquilinatoCarpetaMB implements Serializable {

    @Inject
    private InquilinatoCarpetaService inquilinatoCarpetaService;

    private LazyModel<InquilinatoCarpeta> lazyInquilinato;
    private InquilinatoCarpeta inquilinatoCarpeta;

    @PostConstruct
    public void init() {
    }



}
