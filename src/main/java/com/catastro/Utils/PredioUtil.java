/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catastro.Utils;

import com.catastro.Controller.AppConfig;
import com.catastro.Controller.MainConfig;
import com.catastro.Entities.CatPredioEdificacion;
import com.catastro.Entities.CatPredioLinderos;
import com.catastro.Entities.CatPredioS4;
import com.catastro.Entities.CatPredioS6;
import com.catastro.Services.CatEscrituraServices;
import com.catastro.Services.CatPredioServices;
import com.gestionTributaria.Entities.CatPredio;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author Administrator
 */
public abstract class PredioUtil {

    @Inject
    protected CatPredioServices catas;
    @Inject
    protected CatalogoItemService catalogoItemService;
    @Inject
    private AppConfig appconfig;
    @Inject
    private UserSession sess;
    @Inject
    protected CantonService cantonService;
    @Inject
    protected CatEscrituraServices catEscrituraServices;
    @Inject
    private ClienteService clienteService;
    private CatPredioEdificacion edif = new CatPredioEdificacion();
    private int maxEdif = 1;
    private String usr, obs, predioAnt, fichaEdifAnt, fichaEdifAct, fichaModelAnt, fichaModelAct;
    private Boolean avCalle = false;
    private SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
    private static final long serialVersionUID = 1L;
    protected String idActionListenner;
    private String remotteCommand;
    protected CatPredio predio;
    private CatPredioS4 caracteristicas;
    private CatPredioS6 servicios;
    protected Cliente informante;
    private CatPredio predioColind;
    private String nombreLindero;
    protected MainConfig mainConfig;
    protected Boolean ver = false;
    private List<CatPredioEdificacion> bloques;

    public String completarCeros(Number n, Integer numCaracteres) {
        try {
            return Utils.completarCadenaConCeros(n.toString(), numCaracteres);
        } catch (Exception e) {
            System.out.println("ERROR PredioUtil: " + e);
        }
        return "";
    }

    protected void init() {
        predioAnt = "";
        fichaEdifAnt = "";
        fichaEdifAct = "";
        fichaModelAct = "";
        fichaModelAnt = "";
        this.mainConfig = new MainConfig();
    }

    public void setNamePredioByCiudadela() {
        if (predio.getNombreEdificio() == null) {
            if (predio.getCiudadela() != null) {
                if (predio.getCiudadela().getNombre() != null) {
                    predio.setNombreEdificio(predio.getCiudadela().getNombre());
                }
            }
        } else {
            if (predio.getNombreEdificio().equals("")) {
                if (predio.getCiudadela() != null) {
                    if (predio.getCiudadela().getNombre() != null) {
                        predio.setNombreEdificio(predio.getCiudadela().getNombre());
                    }
                }
            }
        }

    }

    public Cliente getCatEnteByParemt(String ci) {
        Cliente ente = null;
        try {
            if (ci != null) {
                return ente;
            } else {
                ente = clienteService.getFindCliente(ci);
            }
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        }
        return ente;
    }

    public void guardarLinderos() {
        try {
            if (!processMethod()) {
                return;
            }
            obs = "Actualizacion Informacion de Linderos";
            List<CatPredioLinderos> lind = catas.guardarLinderos(predio.getPredioCollection());
            if (Utils.isNotEmpty(lind)) {
                predio.setPredioCollection(lind);
                this.fichaModelAnt = this.fichaModelAct;
                if (saveHistoric(predio, obs, this.fichaEdifAnt, this.fichaEdifAct, this.fichaModelAnt, this.fichaModelAct)) {
                    JsfUtil.addInformationMessage("Nota!", "Linderos prediales actualizadas satisfactoriamente");
                } else {
                    JsfUtil.addWarningMessage("Advertencia!", "Ha ocurrido un error al actualizar el Historico de Linderos prediales");
                }
            } else {
                JsfUtil.addInformationMessage("Advertencia!", "Ha ocurrido un error al actualizar los Linderos prediales, verifique que los campos esten ingresados correctamente");
            }
        } catch (Exception e) {
            System.out.println("Error em predioUtil guardar linderos: " + e.getMessage());
        }
    }

    public Boolean processMethod() {
        if (this.getMainConfig().getFichaPredial().getRenderDialogUploadDocument()) {
//            if (this.getDocumento() == null) {
//                JsfUti.messageInfo(null, "", "Debe de Subir un Documento Antes de Continuar");
//                return false;
//            }
        }
        return true;
    }

    public Boolean saveHistoric(CatPredio pred, String observacion, String fedifAnt, String fedifAct, String fmAnt, String fmAct) {
//        try {
//            if (pred.getId() != null) {
//                catas.edit(pred);
//            } else {
//                catas.create(pred);
//            }
//            if (observacion.equalsIgnoreCase("TAREA")) {
//                eventValoracion.fire(new ValorarPredioPost(this.predio.getClaveCat(), this.predio.getPredialant(), 3, predio.getTipoPredio(), predio.getEstado()));
//            } else {
//                if (observacion.equalsIgnoreCase("ACTUALIZACION CARACTERIZACION Y ELEMENTOS CONSTRUCTIVOS DE LA EDIFICACION")
//                        || observacion.contains("ACTUALIZACION CARACTERIZACION Y ELEMENTOS CONSTRUCTIVOS DE LA EDIFICACION")) {
//                    this.predio = catas.valorarPredio(predio, 2);
//                } else {
//                    if (!this.predio.getPropiedadHorizontal()) {
//                        this.predio = catas.valorarPredio(predio, 1);
//                    }
//                }
//            }
//            pred = catas.getPredioId(pred.getId());
//            this.predio = pred;
//            this.bloques = (List<CatPredioEdificacion>) pred.getCatPredioEdificacionCollection();
//            this.predio = this.catas.guardarPredio(predio);
//            if (Utils.isEmpty(bloques) && !this.predio.getPropiedadHorizontal()
//                    && !this.predio.getFichaMadre()) {
//                pred.setAvaluoConstruccion(BigDecimal.ZERO);
//                pred.setAvaluoMunicipal(pred.getAvaluoSolar());
//                this.predio.setAvaluoConstruccion(BigDecimal.ZERO);
//                this.predio.setAvaluoMunicipal(pred.getAvaluoSolar());
//                this.predio.setBaseImponible(this.predio.getAvaluoMunicipal());
//            }
//
//            String js = generarJson(this.predio);
//            historicoEvent.fire(new GenerarHistoricoPredioPost(getPredioAnt(), js, observacion, usr, predio));
//            catas.guardarHistoricoPredio(pred.getNumPredio().longValue(), getPredioAnt(), js, usr, observacion, null, null, null, null, this.getDocumento());
//            this.setPredioAnt(js);
//            JsfUtil.update(Arrays.asList("tdatos:frmDatosPredio:tvUbicacion:fdsAreas", "tdatos:frmDatosPredio:tvUbicacion:fdsAvaluos"));
//            return true;
//        } catch (Exception e) {
//            System.out.println("ERROR. " + e);
//            JsfUtil.addWarningMessage("Advertencia!", "Ha ocurrido un error al actualizar el Historico de: " + observacion);
        return false;
//        }
    }

//    public String generarJson(CatPredio p) {
//        if (p == null) {
//            return null;
//        }
//        if (p.getTipoConjunto() != null) {
//            p.getTipoConjunto().setCatCiudadelaCollection(null); // No cargar la collection de Ciudadelas.
//        }
//        if (p.getEscrituraLinderos() != null && p.getEscrituraLinderos().getCanton() != null) {
//            p.getEscrituraLinderos().getCanton().setCatParroquiaCollection(null);// no cargar la collection y
//            // parroquias.
//        }
//        if (p.getCatEscrituraCollection() != null) {
//            p.getCatEscrituraCollection().stream().map((catEscritura) -> {
//                if (catEscritura.getCanton() != null) {
//                    catEscritura.getCanton().setCatParroquiaCollection(null);
//                }
//                return catEscritura;
//            }).filter((catEscritura) -> (catEscritura != null))
//                    .filter((catEscritura) -> (catEscritura.getProvincia() != null
//                    && catEscritura.getProvincia().getCatCantonCollection() != null))
//                    .forEachOrdered((catEscritura) -> {
//                        catEscritura.getProvincia().setCatCantonCollection(null);
//                    });
//        }
//        JsonUtils jsonUtils = new JsonUtils();
//        return jsonUtils.generarJson(p);
//    }
    public void verificarRestriccion() {
//        List<RestricionPredio> rest = dataBaseIb.findRestriccionPredio(predio.getClaveCat());
//        if (rest != null) {
//            observacionRestricciones = new ArrayList<>();
//            rest.forEach((rt) -> {
//                observacionRestricciones.add("Tipo de Restricción " + rt.getRestricciones().getDescripcionRestriccion()
//                        + ", " + Utils.isEmpty(rt.getComentario()));
//            });
//        } else {
//            observacionRestriccion = null;
//        }
    }

    //<editor-fold defaultstate="collapsed" desc="get and set">
    public CatPredioEdificacion getEdif() {
        return edif;
    }

    public void setEdif(CatPredioEdificacion edif) {
        this.edif = edif;
    }

    public int getMaxEdif() {
        return maxEdif;
    }

    public void setMaxEdif(int maxEdif) {
        this.maxEdif = maxEdif;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getPredioAnt() {
        return predioAnt;
    }

    public void setPredioAnt(String predioAnt) {
        this.predioAnt = predioAnt;
    }

    public String getFichaEdifAnt() {
        return fichaEdifAnt;
    }

    public void setFichaEdifAnt(String fichaEdifAnt) {
        this.fichaEdifAnt = fichaEdifAnt;
    }

    public String getFichaEdifAct() {
        return fichaEdifAct;
    }

    public void setFichaEdifAct(String fichaEdifAct) {
        this.fichaEdifAct = fichaEdifAct;
    }

    public String getFichaModelAnt() {
        return fichaModelAnt;
    }

    public void setFichaModelAnt(String fichaModelAnt) {
        this.fichaModelAnt = fichaModelAnt;
    }

    public String getFichaModelAct() {
        return fichaModelAct;
    }

    public void setFichaModelAct(String fichaModelAct) {
        this.fichaModelAct = fichaModelAct;
    }

    public Boolean getAvCalle() {
        return avCalle;
    }

    public void setAvCalle(Boolean avCalle) {
        this.avCalle = avCalle;
    }

    public SimpleDateFormat getSdf() {
        return sdf;
    }

    public void setSdf(SimpleDateFormat sdf) {
        this.sdf = sdf;
    }

    public String getIdActionListenner() {
        return idActionListenner;
    }

    public void setIdActionListenner(String idActionListenner) {
        this.idActionListenner = idActionListenner;
    }

    public String getRemotteCommand() {
        return remotteCommand;
    }

    public void setRemotteCommand(String remotteCommand) {
        this.remotteCommand = remotteCommand;
    }

    public CatPredio getPredio() {
        return predio;
    }

    public void setPredio(CatPredio predio) {
        this.predio = predio;
    }

    public CatPredioS4 getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(CatPredioS4 caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public CatPredioS6 getServicios() {
        return servicios;
    }

    public void setServicios(CatPredioS6 servicios) {
        this.servicios = servicios;
    }

    public Cliente getInformante() {
        return informante;
    }

    public void setInformante(Cliente informante) {
        this.informante = informante;
    }

    public CatPredio getPredioColind() {
        return predioColind;
    }

    public void setPredioColind(CatPredio predioColind) {
        this.predioColind = predioColind;
    }

    public String getNombreLindero() {
        return nombreLindero;
    }

    public void setNombreLindero(String nombreLindero) {
        this.nombreLindero = nombreLindero;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public List<CatPredioEdificacion> getBloques() {
        return bloques;
    }

    public void setBloques(List<CatPredioEdificacion> bloques) {
        this.bloques = bloques;
    }

    public MainConfig getMainConfig() {
        if (mainConfig == null) {
            mainConfig = new MainConfig();
        }
        return mainConfig;
    }

    public void setMainConfig(MainConfig mainConfig) {
        this.mainConfig = mainConfig;
    }

    public CatPredioServices getCatas() {
        return catas;
    }

    public void setCatas(CatPredioServices catas) {
        this.catas = catas;
    }
//</editor-fold>

    public String claveCatastral(CatPredio px) {
        String clave = "";
        clave = Utils.completarCadenaConCeros(px.getProvincia().toString(), 2)
                + Utils.completarCadenaConCeros(px.getCanton().toString(), 2)
                + Utils.completarCadenaConCeros(px.getParroquia().toString(), 2)
                + Utils.completarCadenaConCeros(px.getZona().toString(), 1)
                + Utils.completarCadenaConCeros(px.getSector().toString(), 1)
                + Utils.completarCadenaConCeros(px.getMz().toString(), 3)
                + Utils.completarCadenaConCeros(px.getSolar().toString(), 3)
                + Utils.completarCadenaConCeros(px.getBloque().toString(), 3)
                + Utils.completarCadenaConCeros(px.getPiso().toString(), 2)
                + Utils.completarCadenaConCeros(px.getUnidad().toString(), 3);

        return clave;

    }

    public String getPredialant() {
        String clave = String.format("%02d%02d%02d%02d%02d%02d%03d%03d",
                this.predio.getProvincia(), this.predio.getCanton(), this.predio.getParroquia(),
                this.predio.getZona(), this.predio.getSector(), this.predio.getMz(), this.predio.getSolar(),
                this.predio.getBloque());

        return clave;
    }
}
