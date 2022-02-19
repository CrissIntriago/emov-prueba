package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLiquidacion;
import com.gestionTributaria.Entities.CatPredio;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.models.BusquedaPredios;
import com.gestionTributaria.models.tipoPagos.PacificoModel;
import com.origami.sigef.common.util.Utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Administrator
 */
@Named(value = "cobroPacificoView")
@ViewScoped
public class CobroPacificoMB extends BusquedaPredios implements Serializable {

    private static final Logger LOG = Logger.getLogger(CobroPacificoMB.class.getName());

    @Inject
    private RecaudacionInteface recaudacionService;

    private PacificoModel pacificoModel;
    private List<PacificoModel> pacificoModelList;
    private List<UploadedFile> files;
    private FinaRenLiquidacion liquidacion;
    private List<FinaRenLiquidacion> liquidaciones;
    private CatPredio predio;

    @PostConstruct
    public void init() {
        pacificoModel = new PacificoModel();
    }

    public void uploadFile(FileUploadEvent event) {
        StringBuilder resultStringBuilder = new StringBuilder();
        String[] campos = null;
        int cnt_2 = 0, cnt = 0, lineas = 0;
        String nombre;
        liquidaciones = new ArrayList<>();
        pacificoModelList = new ArrayList<>();
        try ( BufferedReader br
                = new BufferedReader(new InputStreamReader(event.getFile().getInputstream()))) {
            lineas = Integer.parseInt(br.lines().count() + "");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al leer el fichero _ XD>>>>", e);
        }
        try ( BufferedReader br
                = new BufferedReader(new InputStreamReader(event.getFile().getInputstream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                cnt = cnt + 1;
                resultStringBuilder.append(line).append("\n");
                campos = line.split("\\s+");
                System.out.println("line>>" + line);
                if (cnt > 1 && cnt < lineas) {
                    pacificoModel = new PacificoModel();
                    cnt_2 = 0;
                    nombre = "";
                    for (String st : campos) {
                        cnt_2 = cnt_2 + 1;
                        switch (cnt_2) {
                            case 1:
                                //cadena donde se encuetra el valor de la transferencia
                                pacificoModel.setValorMov(st);
                                break;
                            case 2:
                                break;
                            case 3:
                                //cadena donde esta el tipo de predio y el codigo del mismo
                                pacificoModel.setCodPredio(st);
                                break;
                            case 4:
                                //no se que año sera pero parece de la liquidacion
                                pacificoModel.setAnio(Integer.parseInt(st));
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
                            case 8:
                                //100% seguro que es de la liquidacion
                                pacificoModel.setAnioLiq(Integer.parseInt(st));
                                break;
                            case 9:
                                break;
                            case 10:
                                break;
                            case 11:
                                break;
                        }
                    }
                    pacificoModelList.add(pacificoModel);
                }
            }
            if (Utils.isNotEmpty(pacificoModelList)) {
                for (PacificoModel p : pacificoModelList) {
                    p.armarCadenas();
                    CatPredio predio = recaudacionService.findPredioByTipoAndNumPredio(p.getTipoPredio(), p.getNumPredio());
                    if (predio != null) {
                        List<FinaRenLiquidacion> liqList = recaudacionService.findLiquidacionesByPrediosAndAnio(predio, p.getAnioLiq(), 2L);
                        if (Utils.isNotEmpty(liqList)) {
                            this.liquidaciones.addAll(liqList);
                        }
                    }
                }
            }
            this.calculoTotalPago(this.liquidaciones, null);
            System.out.println("lista de liquidaciones>>" + liquidaciones.size());
            liquidaciones.sort(Comparator.comparing(FinaRenLiquidacion::getAnio));
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al leer el fichero>>>>", e);
        }
    }

    public PacificoModel getPacificoModel() {
        return pacificoModel;
    }

    public void setPacificoModel(PacificoModel pacificoModel) {
        this.pacificoModel = pacificoModel;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public List<PacificoModel> getPacificoModelList() {
        return pacificoModelList;
    }

    public void setPacificoModelList(List<PacificoModel> pacificoModelList) {
        this.pacificoModelList = pacificoModelList;
    }

    public List<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(List<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

}
