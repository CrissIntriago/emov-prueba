/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.implInterfaces;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.ProvinciaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.services.ContCuentaPartidaService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThConfLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThDiasLaborados;
import com.origami.sigef.resource.talento_humano.entities.ThEscalaSalarial;
import com.origami.sigef.resource.talento_humano.entities.ThHorasExtras;
import com.origami.sigef.resource.talento_humano.entities.ThPrestamoIess;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThCargoRubrosService;
import com.origami.sigef.resource.talento_humano.services.ThCargoService;
import com.origami.sigef.resource.talento_humano.services.ThConfLiquidacionRolService;
import com.origami.sigef.resource.talento_humano.services.ThConfigService;
import com.origami.sigef.resource.talento_humano.services.ThDiasLaboradosService;
import com.origami.sigef.resource.talento_humano.services.ThEscalaSalarialService;
import com.origami.sigef.resource.talento_humano.services.ThHorasExtrasService;
import com.origami.sigef.resource.talento_humano.services.ThPrestamoIessService;
import com.origami.sigef.resource.talento_humano.services.ThRegimenLaboralService;
import com.origami.sigef.resource.talento_humano.services.ThRubroService;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
import com.origami.sigef.resource.talento_humano.services.ThTipoRolService;
import com.origami.sigef.talentohumano.services.ServidorService;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Criss Intriago
 */
@Singleton
@ApplicationScoped
public class ImpleThInterfaces implements ThInterfaces {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ThTipoRolService thTipoRolService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private ThConfigService thConfigService;
    @Inject
    private ThRubroService thRubroService;
    @Inject
    private ThPrestamoIessService prestamoIessService;
    @Inject
    private ThDiasLaboradosService thDiasLaboradosService;
    @Inject
    private UserSession userSession;
    @Inject
    private ThServidorCargoService thServidorCargoService;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private CantonService cantonService;
    @Inject
    private ThCargoService thCargoService;
    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;
    @Inject
    private ThEscalaSalarialService thEscalaSalarialService;
    @Inject
    private ThRegimenLaboralService thRegimenLaboralService;
    @Inject
    private ThHorasExtrasService thHorasExtrasService;
    @Inject
    private ThCargoRubrosService thCargoRubrosService;
    @Inject
    private ContCuentaPartidaService contCuentaPartidaService;
    @Inject
    private ThConfLiquidacionRolService thConfLiquidacionRolService;

    @Override
    public List<Short> getPeriodos() {
        return catalogoItemService.getPeriodo();
    }

    @Override
    public List<ThTipoRol> tipoRol(Short anio) {
        return thTipoRolService.getRolAnio(anio);
    }

    @Override
    public DefaultStreamedContent docDownload(String name_formato) throws Exception {
        System.out.println("file TH: " + valoresService.findByCodigo(name_formato));
        File file = new File(valoresService.findByCodigo(name_formato));
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        return new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
    }

    @Override
    public List<ThPrestamoIess> loadData(FileUploadEvent event, Boolean tipo1, Boolean tipo2, ThTipoRol thTipoRol, String codeRubro) {
        List<ThPrestamoIess> result = new ArrayList<>();
        try {
            InputStream input = event.getFile().getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(input);
            DataFormatter dataFormatter = new DataFormatter();
            XSSFSheet sheet = worbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            int next = 0;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                if (next > 0) {//PARA NO TOMAR EL ENCABEZADO DE LAS COLUMNAS
                    if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue().trim() != null) {
                        ThPrestamoIess prestamo = new ThPrestamoIess();
                        prestamo.setServidor(servidorService.findByIdentificacion(dataFormatter.formatCellValue(row.getCell(0)).replace("'", "")));
                        prestamo.setIdTipoRol(thTipoRol);
                        prestamo.setPh(tipo1);
                        prestamo.setPq(tipo2);
                        prestamo.setValidado(false);
                        prestamo.setIdRubro(thRubroService.findByNamedQuery1("ThRubro.findByClasificacion", codeRubro));
                        prestamo.setValor(new BigDecimal(Double.valueOf(dataFormatter.formatCellValue(row.getCell(2)).replace(",", "."))));
                        ThPrestamoIess temp = prestamoIessService.findByNamedQuery1("ThPrestamoIess.findByServidor", prestamo.getServidor(), thTipoRol);
                        if (temp == null && prestamo.getServidor() != null && prestamo.getIdRubro() != null) {
                            result.add(prestamo);
                        }
                    }
                }
                next++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error: " + e);
            JsfUtil.addWarningMessage("INFO!!!", "Cargue un archivo con el mismo formato y tipo de la plantilla");
        }
        return result;
    }

    @Override
    public Servidor findByServidor(String identificacion) {
        return servidorService.findByIdentificacion(identificacion, getFechaInicio(), getFechaFin());
    }

    public Date getFechaInicio() {
        OpcionBusqueda opcionBusqueda = new OpcionBusqueda();
        Calendar calendar = Calendar.getInstance();
        int mes = Utils.getMes(new Date()) - 1;
        calendar.set(opcionBusqueda.getAnio(), mes, 1);
        return calendar.getTime();
    }

    public Date getFechaFin() {
        return new Date();
    }

    @Override
    public ThConfig findThConfig(String codigo) {
        return thConfigService.findCode(codigo);
    }

    @Override
    public void edit(ThConfig thConfig) {
        thConfigService.edit(thConfig);
    }

    @Override
    public List<CatalogoItem> listaRubrosIngresos() {
        return catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_ingreso");
    }

    @Override
    public List<CatalogoItem> listaRubrosEgreso() {
        return catalogoItemService.findByNamedQuery("CatalogoItem.findCatalogoClasificacion1", "valor_talento_egreso");
    }

    @Override
    public List<ThRubro> rubros(String codConfig) {
        return thRubroService.findByNamedQuery("ThRubro.findByClasificacion", codConfig);
    }

    @Override
    public Long findCtaContable(ThRubro idRubro, Servidor servidor) {
        return thRubroService.findIdCuenta(idRubro.getId(), servidor.getId()).longValue();
    }

    @Override
    public List<ThDiasLaborados> loadDataDiasLaborados(FileUploadEvent event, Boolean FALSE, Boolean TRUE, ThTipoRol thTipoRol) {
        List<ThDiasLaborados> result = new ArrayList<>();
        try {
            InputStream input = event.getFile().getInputstream();
            XSSFWorkbook worbook = new XSSFWorkbook(input);
            DataFormatter dataFormatter = new DataFormatter();
            XSSFSheet sheet = worbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            Row row;
            int next = 0;
            while (rowIterator.hasNext()) {
                row = rowIterator.next();
                if (next > 0) {//PARA NO TOMAR EL ENCABEZADO DE LAS COLUMNAS
                    if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue().trim() != null) {
                        ThDiasLaborados diasLaborados = new ThDiasLaborados();
                        diasLaborados.setServidor(servidorService.findByIdentificacion(dataFormatter.formatCellValue(row.getCell(0)).replace("'", "")));
                        if (thDiasLaboradosService.findByNamedQuery1("ThDiasLaborados.findByServidor", diasLaborados.getServidor(), thTipoRol)) {
                            diasLaborados.setIdTipoRol(thTipoRol);
                            diasLaborados.setDiasLaborados(Integer.parseInt(dataFormatter.formatCellValue(row.getCell(3))));
                            if (dataFormatter.formatCellValue(row.getCell(2)).replace("'", "").toUpperCase().equals("NO")) {
                                diasLaborados.setAparecerRol(Boolean.FALSE);
                            }
                            diasLaborados.setUsuarioCreacion(userSession.getNameUser());
                            diasLaborados.setFechaCreacion(new Date());
                            result.add(diasLaborados);
                            thDiasLaboradosService.create(diasLaborados);
                        }
                    }
                }
                next++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsfUtil.addWarningMessage("INFO!!!", "Cargue un archivo con el mismo formato y tipo de la plantilla");
        }
        return result;
    }

    @Override
    public Integer getDiasTalentoHumano() {
        return Integer.parseInt(valoresService.findByCodigo("DIAS_TALENTO_HUMANO"));
    }

    @Override
    public String getUser() {
        return userSession.getNameUser();
    }

    @Override
    public List<CatalogoItem> getListMes() {
        return catalogoItemService.findByNamedQuery("CatalogoItem.findByMeses", "meses_anio");
    }

    @Override
    public ThServidorCargo setServidorCargo(Servidor servidor) {
        ThServidorCargo thcargo = thServidorCargoService.findByNamedQuery1("ThServidorCargo.findByIdServidor", servidor);
        return thcargo;
    }

    @Override
    public ThRubro getIdThRubro(String code) {
        return thRubroService.findByNamedQuery1("ThRubro.findByCode", code);
    }

    @Override
    public List<Canton> getCantones(Provincia provincia) {
        return cantonService.getCantones(provincia);
    }

    @Override
    public List<Provincia> getProvincias() {
        return provinciaService.getProvincias();
    }

    @Override
    public Integer valorPorcentaje() {
        return Integer.parseInt(valoresService.findByCodigo(CONFIG.MAX_PORCENJATE_RETENCION));
    }

    @Override
    public List<ThTipoRol> getRoles(Servidor servidor, Short anio) {
        return thTipoRolService.getServidorPublico(servidor, anio);
    }

    @Override
    public List<String> getCargosActivos() {
        return thCargoService.getCargosActivos();
    }

    @Override
    public List<ThCargo> findCargos(String thCargoSeleccionado, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion, UnidadAdministrativa unidadFind) {
        return thCargoService.getCargos(thCargoSeleccionado, filtroContrato, filtroClasificacion, unidadFind);
    }

    @Override
    public List<UnidadAdministrativa> getUnidadesAdministrativas() {
        return unidadAdministrativaService.findByNamedQuery("UnidadAdministrativa.findByUnidad", true);
    }

    @Override
    public List<ThEscalaSalarial> getThEscalaSalarial() {
        return thEscalaSalarialService.findByNamedQuery("ThEscalaSalarial.findByEscala", true);
    }

    @Override
    public List<ThRegimenLaboral> getThRegimenLaboral() {
        return thRegimenLaboralService.findByNamedQuery("ThRegimenLaboral.findByRegimen", true);
    }

    @Override
    public Integer getValorStatico() {
        return Integer.parseInt(valoresService.findByCodigo(CONFIG.VALOR_STATIC_HORAS));
    }

    @Override
    public ThDiasLaborados getdiasLaborados(ThServidorCargo idCargoServidor, ThTipoRol thTipoRolSeleccionado) {
        return thDiasLaboradosService.findByNamedQuery1("ThDiasLaborados.findByServidorData", idCargoServidor.getIdServidor(), thTipoRolSeleccionado);
    }

    @Override
    public boolean getvalidarServidorHorasExtras(ThServidorCargo idCargoServidor, ThTipoRol thTipoRolSeleccionado) {
        List<ThHorasExtras> result = thHorasExtrasService.findByNamedQuery("ThHorasExtras.findByServidor", idCargoServidor, thTipoRolSeleccionado);
        return !result.isEmpty();
    }

    @Override
    public void getUpdateRubros(String thCargoSeleccionado, List<ThCargoRubros> thCargoRubrosList, Boolean accion, Short anio, UnidadAdministrativa unidadFind, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion) {
        for (ThCargoRubros item : thCargoRubrosList) {
            System.out.println("ACCION: " + accion);
            if (accion) {
                thCargoRubrosService.getUpdateRubroPartida(thCargoSeleccionado, item, anio, unidadFind, filtroContrato, filtroClasificacion);
            } else {
                thCargoRubrosService.getUpdateRubroCuenta(thCargoSeleccionado, item, anio, unidadFind, filtroContrato, filtroClasificacion);
            }
        }
    }

    @Override
    public Integer getCantidadServidores(Boolean modoCalculo, ThTipoRol thtipoRol) {
        if (modoCalculo) {
            return thServidorCargoService.getCount();
        } else {
            return thTipoRolService.getCount(thtipoRol);
        }
    }

    @Override
    public List<ThTipoRol> getRolesAprobados(Short periodo) {
        return thTipoRolService.findByNamedQuery("ThTipoRol.findByRolGeneralAdicional", periodo);
    }

    @Override
    public ThTipoRol getThTipoRol(Long item) {
        return thTipoRolService.find(item);
    }

    @Override
    public void editThTipoRol(ThTipoRol tr) {
        thTipoRolService.edit(tr);
    }

    @Override
    public boolean getCuentaPartidaRubro(ThCargoRubros thCargoRubros) {
        ContCuentas cuenta = contCuentaPartidaService.getCuenta(thCargoRubros.getIdPresupuesto());
        if (cuenta != null) {
            thCargoRubros.setIdCuenta(cuenta);
            thCargoRubrosService.edit(thCargoRubros);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ThConfLiquidacionRol> getCuentasRubroEgreso(ThServidorCargo thServidorCargo, Short anio) {
        return thConfLiquidacionRolService.getCuentasRubroEgreso(thServidorCargo, anio);
    }

    @Override
    public List<ThRegimenLaboral> getRegimenList() {
        return thRegimenLaboralService.getRegimenActivo();
    }

    @Override
    public void getUpdateRubroCuenta(ThRubro idRubro, ContCuentas idCuenta, Short anio, CatalogoItem contrato, CatalogoItem clasificacion, ThRegimenLaboral regimen) {
        thConfLiquidacionRolService.getUpdateRubroCuenta(idRubro, idCuenta, anio, contrato, clasificacion, regimen);
    }
    @Override
     public void editThConfLiquidacionRol(ThConfLiquidacionRol item){
         thConfLiquidacionRolService.edit(item);
     }
}
