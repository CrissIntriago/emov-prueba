/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.lazy;

import com.origami.sigef.common.entities.DetalleConciliacionBancaria;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 *
 * @author jesus
 */
public class LazyConciliacionModel extends LazyDataModel<DetalleConciliacionBancaria> {

    private List<DetalleConciliacionBancaria> datasource;

    public LazyConciliacionModel(List<DetalleConciliacionBancaria> datasource) {
        this.datasource = datasource;
    }

    @Override
    public DetalleConciliacionBancaria getRowData(String rowKey) {
        for (DetalleConciliacionBancaria detalle : datasource) {
            if (detalle.getId().equals(Long.parseLong(rowKey))) {
                return detalle;
            }
        }
        return null;
    }

    @Override
    public Object getRowKey(DetalleConciliacionBancaria d) {
        return d.getId();
    }

    @Override
    public List<DetalleConciliacionBancaria> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        List<DetalleConciliacionBancaria> data = new ArrayList<>();
        //filter
        for (DetalleConciliacionBancaria detalle : datasource) {
            boolean match = true;
            if (filters != null) {
                for (Map.Entry<String, Object> it : filters.entrySet()) {
                    try {
                        if (it.getKey().equalsIgnoreCase("beneficiario")) {
                            match = detalle.getBeneficiario().toLowerCase().contains(it.getValue().toString().toLowerCase().trim());
                        } else if (it.getKey().equalsIgnoreCase("detalle")) {
                            match = detalle.getDetalle().toLowerCase().contains(it.getValue().toString().toLowerCase().trim());
                        } else if (it.getKey().equalsIgnoreCase("numDiarioGeneral")) {
                            match = detalle.getNumDiarioGeneral().toString().contains(it.getValue().toString().trim());
                        } else if (it.getKey().equalsIgnoreCase("numComprobantePago")) {
                            match = detalle.getNumComprobantePago().toString().contains(it.getValue().toString().trim());
                        } else if (it.getKey().equalsIgnoreCase("spi")) {
                            match = detalle.getNumComprobantePago().toString().contains(it.getValue().toString().trim());
                        }else if (it.getKey().equalsIgnoreCase("estado")) {
                            match = detalle.getEstado().toLowerCase().contains(it.getValue().toString().toLowerCase().trim());
                        }
                    } catch (Exception e) {
                        match = false;
                    }
                }
            }
            if (match) {
                data.add(detalle);
            }
        }
        //sort
        if (sortField != null) {
            Collections.sort(data, new LazySorterConciliacion(sortField, sortOrder));
        }

        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);

        //paginate
        if (dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            } catch (IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        } else {
            return data;
        }
    }
}
