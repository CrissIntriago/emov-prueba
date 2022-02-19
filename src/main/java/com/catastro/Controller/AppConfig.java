/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Controller;

import com.catastro.Models.EstadosPredio;
import com.catastro.Models.ModelLockPredio;
import org.apache.commons.codec.binary.Hex;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Named
@ApplicationScoped
public class AppConfig {

    private Map<String, ModelLockPredio> prediosedicion = new HashMap<>();
    private Map<String, String> catalogosGen = new HashMap<>();
    private MainConfig mainConfig = new MainConfig();

    @PostConstruct
    public void init() {
        catalogosGen.put("U", "URBANO");
        catalogosGen.put("R", "RURAL");
        catalogosGen.put(EstadosPredio.ACTIVO, "ACTIVO");
        catalogosGen.put(EstadosPredio.HISTORICO, "HISTORICO");
        catalogosGen.put(EstadosPredio.INACTIVO, "INACTIVO");
        catalogosGen.put(EstadosPredio.PENDIENTE, "PENDIENTE");
        catalogosGen.put(EstadosPredio.TEMPORAL, "TEMPORAL");
    }

    public boolean isLocked(String user, Long idPredio) {
        ModelLockPredio lock = new ModelLockPredio();
        lock.setFechaBloqueo(new Date());
        lock.setIdpredio(idPredio);
        ModelLockPredio get = this.prediosedicion.get(user);
        if (get != null) {
            if (get.equals(lock)) {
                this.lockPredio(user, idPredio);
                return false;
            }
        }

        return this.prediosedicion.containsValue(lock);
    }

    public void lockPredio(String user, Long idPredio) {
        ModelLockPredio lock = new ModelLockPredio();
        lock.setFechaBloqueo(new Date());
        lock.setIdpredio(idPredio);
        this.prediosedicion.put(user, lock);
    }

    public String retornarValor(String cadena) {
        if (cadena == null) {
            return "";
        }
        return catalogosGen.get(cadena.toUpperCase());
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public Map<String, ModelLockPredio> getPrediosedicion() {
        return prediosedicion;
    }

    public void setPrediosedicion(Map<String, ModelLockPredio> prediosedicion) {
        this.prediosedicion = prediosedicion;
    }

    public Map<String, String> getCatalogosGen() {
        return catalogosGen;
    }

    public void setCatalogosGen(Map<String, String> catalogosGen) {
        this.catalogosGen = catalogosGen;
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public void setMainConfig(MainConfig mainConfig) {
        this.mainConfig = mainConfig;
    }

    //</editor-fold>
    public AppConfig() {
    }

    public String encodeHexString(String text) {
        return Hex.encodeHexString(text.getBytes(StandardCharsets.UTF_8));
    }
}
