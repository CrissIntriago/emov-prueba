/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.talento_humano.interfaces;

import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThCargo;
import com.origami.sigef.resource.talento_humano.entities.ThCargoRubros;
import com.origami.sigef.resource.talento_humano.entities.ThConfLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThConfig;
import com.origami.sigef.resource.talento_humano.entities.ThDiasLaborados;
import com.origami.sigef.resource.talento_humano.entities.ThEscalaSalarial;
import com.origami.sigef.resource.talento_humano.entities.ThPrestamoIess;
import com.origami.sigef.resource.talento_humano.entities.ThRegimenLaboral;
import com.origami.sigef.resource.talento_humano.entities.ThRubro;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.List;
import javax.ejb.Local;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Criss Intriago
 */
@Local
public interface ThInterfaces {

    public List<Short> getPeriodos();

    public List<ThTipoRol> tipoRol(Short anio);

    public DefaultStreamedContent docDownload(String name_formato) throws Exception;

    public List<ThPrestamoIess> loadData(FileUploadEvent event, Boolean tipo1, Boolean tipo2, ThTipoRol thTipoRol, String codeRubro);

    public Servidor findByServidor(String identificacion);

    public ThConfig findThConfig(String codigo);

    public void edit(ThConfig thConfig);

    public List<CatalogoItem> listaRubrosIngresos();

    public List<CatalogoItem> listaRubrosEgreso();

    public List<ThRubro> rubros(String codConfig);

    public Long findCtaContable(ThRubro idRubro, Servidor servidor);

    public List<ThDiasLaborados> loadDataDiasLaborados(FileUploadEvent event, Boolean FALSE, Boolean TRUE, ThTipoRol thTipoRol);

    public Integer getDiasTalentoHumano();

    public String getUser();

    public List<CatalogoItem> getListMes();

    public ThServidorCargo setServidorCargo(Servidor servidor);

    public ThRubro getIdThRubro(String code);

    public List<Canton> getCantones(Provincia provincia);

    public List<Provincia> getProvincias();

    public Integer valorPorcentaje();

    public List<ThTipoRol> getRoles(Servidor servidor, Short anio);

    public List<String> getCargosActivos();

    public List<UnidadAdministrativa> getUnidadesAdministrativas();

    public List<ThEscalaSalarial> getThEscalaSalarial();

    public List<ThRegimenLaboral> getThRegimenLaboral();

    public List<ThCargo> findCargos(String thCargoSeleccionado, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion, UnidadAdministrativa unidadFind);

    public Integer getValorStatico();

    public ThDiasLaborados getdiasLaborados(ThServidorCargo idCargoServidor, ThTipoRol thTipoRolSeleccionado);

    public boolean getvalidarServidorHorasExtras(ThServidorCargo idCargoServidor, ThTipoRol thTipoRolSeleccionado);

    public void getUpdateRubros(String thCargoSeleccionado, List<ThCargoRubros> thCargoRubrosList, Boolean accion, Short anio, UnidadAdministrativa unidadFind, CatalogoItem filtroContrato, CatalogoItem filtroClasificacion);

    public Integer getCantidadServidores(Boolean modoCalculo, ThTipoRol thtipoRol);

    public List<ThTipoRol> getRolesAprobados(Short periodo);

    public ThTipoRol getThTipoRol(Long item);

    public void editThTipoRol(ThTipoRol tr);

    public boolean getCuentaPartidaRubro(ThCargoRubros thCargoRubros);

    public List<ThConfLiquidacionRol> getCuentasRubroEgreso(ThServidorCargo thServidorCargo, Short anio);

    public List<ThRegimenLaboral> getRegimenList();

    public void getUpdateRubroCuenta(ThRubro idRubro, ContCuentas idCuenta, Short anio, CatalogoItem contrato, CatalogoItem clasificacion, ThRegimenLaboral regimen);

    public void editThConfLiquidacionRol(ThConfLiquidacionRol item);

}
