/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.lazy;

import com.origami.sigef.common.entities.DetalleConciliacionBancaria;
import java.lang.reflect.Field;
import java.util.Comparator;
import org.primefaces.model.SortOrder;

/**
 *
 * @author jesus
 */
public class LazySorterConciliacion implements Comparator<DetalleConciliacionBancaria> {

    private String sortField;

    private SortOrder sortOrder;

    public LazySorterConciliacion(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    @Override
    public int compare(DetalleConciliacionBancaria detalle1, DetalleConciliacionBancaria detalle2) {
        try {
            Field field1 = detalle1.getClass().getDeclaredField(this.sortField);
            Field field2 = detalle2.getClass().getDeclaredField(this.sortField);
            field1.setAccessible(true);
            field2.setAccessible(true);
            Object value1 = field1.get(detalle1);
            Object value2 = field2.get(detalle2);

            int value = ((Comparable) value1).compareTo(value2);
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            throw new RuntimeException();
        }
    }
}
