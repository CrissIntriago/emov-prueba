/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.origami.sigef.common.entities.CatalogoItem;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Administrator
 */
@Named(value = "compView")
@ApplicationScoped
public class ComponenteView implements Serializable {

    @Inject
    private ManagerService manager;

    private Integer itemBind;

    @PostConstruct
    public void init() {

    }

    public Integer valorItem(Object obj) {

        if (obj instanceof CatalogoItem) {
            if (obj != null) {
                return ((CatalogoItem) obj).getOrden().intValue();
            }
        }

        return null;
    }

    public List<CatalogoItem> valoresByCatalogo(String catalogo) {
        String catag = catalogo.replace('-', '.');
        Map<String, Object> param = new HashMap<>();
        param.put("catalogo", catag);
        List<CatalogoItem> ctlgItem = (List<CatalogoItem>) manager.findAllQuery("SELECT i1 FROM CatalogoItem i1 WHERE i1.catalogo.codigo=:catalogo ORDER BY i1.orden ASC",
                 param);
        return ctlgItem;
    }

    public List<SelectItem> valoresItemSelect(String catalogo) {
        List<SelectItem> items = new ArrayList<>();
        List<CatalogoItem> ctlgItems = valoresByCatalogo(catalogo);
        ctlgItems.stream().map((v) -> new SelectItem("com.origami.sigef.common.entities.CatalogoItem:" + v.getId() + ":java.lang.Long", v.getValor().toString())).forEachOrdered((i) -> {
            items.add(i);

        });

        return items;
    }

    public String rangoOrdenes(String catalogo) {
        String rango = "";
        List<CatalogoItem> items = valoresByCatalogo(catalogo);
        if (!items.isEmpty()) {
            if (items.size() == 1) {
                rango += items.get(0).getId() + ";" + items.get(0).getOrden();
            } else {
                for (int i = 0; i < items.size() - 1; i++) {
                    rango += items.get(i).getId() + ";" + items.get(i).getOrden() + "-";
                }
                rango += items.get(items.size() - 1).getId() + ";" + items.get(items.size() - 1).getOrden();
            }
        }
        return rango;
    }

    public List<Integer> ordenes(String catalago) {
        List<Integer> ordenes = new ArrayList<>();
        List<CatalogoItem> items = valoresByCatalogo(catalago);
        if (!items.isEmpty()) {
            ordenes.add(items.get(0).getOrden().intValue());
        }
        return ordenes;
    }

    public void update() {

    }

}
