/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ConnectSQLite;

import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.model.MarcacionModel;
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 *
 * @author OrigamiEC
 */
public class ConnectionSQLite implements Serializable {

//    @EJB
//    private ValoresService valoresService;
//
//    public static final String URL_SERVICE = "http://192.168.100.203:8485/access/files/api/ZKTimeNet/emp_code/%s/mes/%s/anio/%s";
//    //public static final String URL_SERVICE = "http://172.27.44.34:8485/access/files/api/ZKTimeNet/emp_code/%s/mes/%s/anio/%s";
//    private HttpClient httpClient;
//    private HttpGet httpGet;
//    private Gson gson;
//    private ExecutorService executorService;
//    private Future<HttpResponse> futureResponse;
//    private HttpResponse httpResponse;

    private String urlService;

    public ConnectionSQLite() {
        System.out.println("constructor");
        urlService = "";
    }

    public List<MarcacionModel> calcularHoras(List<MarcacionModel> lista) {
        if (!lista.isEmpty()) {
            int horas, minutos;
            for (MarcacionModel m : lista) {
                horas = 0;
                minutos = 0;
                Calendar cal = Calendar.getInstance();
                if (m.getHoras_laboras() != null) {
                    cal.setTime(TalentoHumano.convertStringToTimestamp(m.getHoras_laboras(), "HH:mm"));
                    horas = cal.get(Calendar.HOUR_OF_DAY) - TalentoHumano.HORAS_LABORABLES;
                    minutos = cal.get(Calendar.MINUTE);
                }
                if (horas > 0 && minutos > 0) {
                    m.setHoras_extras(TalentoHumano.convertStringToTimestamp(horas + ":" + minutos, "HH:mm"));
                }
            }
        }
        return lista;
    }

}
