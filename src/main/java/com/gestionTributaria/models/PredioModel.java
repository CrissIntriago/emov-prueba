/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.util.Utils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class PredioModel {

    private static final Long serialVersionUID = 1L;
    private BigInteger numPredio;
    private short parroquiaCod;
    private short sector;
    private short mz;
    private short cdla;
    private short mzDiv;
    private short solar;
    private short div1;
    private short div2;
    private short div3;
    private short div4;
    private short div5;
    private short div6;
    private short div7;
    private short div8;
    private short div9;
    private short phv;
    private short phh;
    //////////////////
    private short zona;
    private short lote;
    private short bloque;
    private short piso;
    private short unidad;
    private short canton;
    private short provincia;
    private short parroquiaShort;
    private String codigoPredial;
    //////////////////
    private String regCatastral;
    private String nombreComprador;
    private String idPredial;
    private String parroquia;
    private String ciudadela;
    private String mzUrb;
    private String slUrb;
    private String urbanizacion;
    private Long tipoConsultaUrbano = 2L;
    private List<DatosRenLiquidacion> datosLiquidacion;
    private String codAnt1;
    private String codAnt2;
    private String codAnt3;
    private String codAnt4;
    private String codAnt5;
    private String codAnt6;
    private String codAnt7;
    private String codAnt8;
    private String contribuyenteConsulta;
    private String propietarioUrbano;
    private Long tipoConsultaRural = 1L;
    private String contribuyenteConsultaRural;
    private Boolean habilitado = Boolean.FALSE;
    private String divisionUrb;
    private String numDepartamento;
    private String predialAnterior;
    //PREDIALANT
    private String provinciaAnt;
    private String cantonAnt;
    private String parroquiaAnt;
    private String manzanaAnt;
    private String zonaAnt;
    private String sectorAnt;
    private String propiedadHorizontalAnt;
    private String solarAnt;

    private String predialAnt;
    private String claveCat;

    private BigDecimal areaSolar;
    private BigDecimal areaConstruccion;
    private BigDecimal zonaGeo;
    private BigDecimal sectorGeo;
    private BigDecimal solarGeo;
    private BigDecimal loteGeo;
    private BigDecimal mzGeo;
    private String calle;
    private String calleS;
    private Boolean temporales = Boolean.FALSE;

    private Integer anioDesde = Utils.getAnio(new Date()) - 1;
    private Integer anioHasta = Utils.getAnio(new Date()) - 1;
    private Boolean esUrbano = Boolean.TRUE;

    private String nombrePropietarios;

    private String direccion;

    public PredioModel() {
        this.provincia = SisVars.PROVINCIA;
        this.canton = SisVars.CANTON;
        this.parroquiaShort = 50;
        this.numPredio = BigInteger.ZERO;
        this.sector = 0;
        this.mz = 0;
        this.cdla = 0;
        this.mzDiv = 0;
        this.solar = 0;
        this.div1 = 0;
        this.div2 = 0;
        this.div3 = 0;
        this.div4 = 0;
        this.div5 = 0;
        this.div6 = 0;
        this.div7 = 0;
        this.div8 = 0;
        this.div9 = 0;
        this.phv = 0;
        this.phh = 0;
        this.parroquia="0";
        this.codAnt1 = "0";
        this.codAnt2 = "0";
        this.codAnt3 = "0";
        this.codAnt4 = "0";
        this.codAnt5 = "0";
        this.codAnt6 = "0";
        this.codAnt7 = "0";
        this.codAnt8 = "0";

        claveCat = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
        predialAnt = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
    }

    public void cambiarTipo(Boolean esUrbano) {
        if (esUrbano) {
            claveCat = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
            predialAnt = String.format("%02d%02d%02d", SisVars.PROVINCIA, SisVars.CANTON, parroquiaShort);
        } else {
            claveCat = null;
            predialAnt = null;
        }
    }

    public PredioModel(BigDecimal areaSolar, BigDecimal areaConstruccion, BigDecimal zonaGeo, BigDecimal sectorGeo, BigDecimal solarGeo, BigDecimal loteGeo, BigDecimal mzGeo) {
        this.areaSolar = areaSolar;
        this.areaConstruccion = areaConstruccion;
        this.zonaGeo = zonaGeo;
        this.sectorGeo = sectorGeo;
        this.solarGeo = solarGeo;
        this.loteGeo = loteGeo;
        this.mzGeo = mzGeo;
    }

    public PredioModel(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public BigInteger getNumPredio() {
        return numPredio;
    }

    public void setNumPredio(BigInteger numPredio) {
        this.numPredio = numPredio;
    }

    public short getSector() {
        return sector;
    }

    public void setSector(short sector) {
        this.sector = sector;
    }

    public short getMz() {
        return mz;
    }

    public void setMz(short mz) {
        this.mz = mz;
    }

    public short getCdla() {
        return cdla;
    }

    public void setCdla(short cdla) {
        this.cdla = cdla;
    }

    public short getMzDiv() {
        return mzDiv;
    }

    public void setMzDiv(short mzDiv) {
        this.mzDiv = mzDiv;
    }

    public short getSolar() {
        return solar;
    }

    public void setSolar(short solar) {
        this.solar = solar;
    }

    public short getDiv1() {
        return div1;
    }

    public void setDiv1(short div1) {
        this.div1 = div1;
    }

    public short getDiv2() {
        return div2;
    }

    public void setDiv2(short div2) {
        this.div2 = div2;
    }

    public short getDiv3() {
        return div3;
    }

    public void setDiv3(short div3) {
        this.div3 = div3;
    }

    public short getDiv4() {
        return div4;
    }

    public void setDiv4(short div4) {
        this.div4 = div4;
    }

    public short getDiv5() {
        return div5;
    }

    public void setDiv5(short div5) {
        this.div5 = div5;
    }

    public short getDiv6() {
        return div6;
    }

    public void setDiv6(short div6) {
        this.div6 = div6;
    }

    public short getDiv7() {
        return div7;
    }

    public void setDiv7(short div7) {
        this.div7 = div7;
    }

    public short getDiv8() {
        return div8;
    }

    public void setDiv8(short div8) {
        this.div8 = div8;
    }

    public short getDiv9() {
        return div9;
    }

    public void setDiv9(short div9) {
        this.div9 = div9;
    }

    public short getPhv() {
        return phv;
    }

    public void setPhv(short phv) {
        this.phv = phv;
    }

    public short getPhh() {
        return phh;
    }

    public void setPhh(short phh) {
        this.phh = phh;
    }

    public short getZona() {
        return zona;
    }

    public void setZona(short zona) {
        this.zona = zona;
    }

    public short getLote() {
        return lote;
    }

    public void setLote(short lote) {
        this.lote = lote;
    }

    public short getBloque() {
        return bloque;
    }

    public void setBloque(short bloque) {
        this.bloque = bloque;
    }

    public short getPiso() {
        return piso;
    }

    public void setPiso(short piso) {
        this.piso = piso;
    }

    public short getUnidad() {
        return unidad;
    }

    public void setUnidad(short unidad) {
        this.unidad = unidad;
    }

    public short getCanton() {
        return canton;
    }

    public void setCanton(short canton) {
        this.canton = canton;
    }

    public short getProvincia() {
        return provincia;
    }

    public void setProvincia(short provincia) {
        this.provincia = provincia;
    }

    public short getParroquiaShort() {
        return parroquiaShort;
    }

    public void setParroquiaShort(short parroquiaShort) {
        this.parroquiaShort = parroquiaShort;
    }

    public String getCodigoPredial() {
        return codigoPredial;
    }

    public void setCodigoPredial(String codigoPredial) {
        this.codigoPredial = codigoPredial;
    }

    public String getRegCatastral() {
        return regCatastral;
    }

    public void setRegCatastral(String regCatastral) {
        this.regCatastral = regCatastral;
    }

    public String getNombreComprador() {
        return nombreComprador;
    }

    public void setNombreComprador(String nombreComprador) {
        this.nombreComprador = nombreComprador;
    }

    public String getIdPredial() {
        return idPredial;
    }

    public void setIdPredial(String idPredial) {
        this.idPredial = idPredial;
    }

    public String getParroquia() {
        return parroquia;
    }

    public void setParroquia(String parroquia) {
        this.parroquia = parroquia;
    }

    public String getCiudadela() {
        return ciudadela;
    }

    public void setCiudadela(String ciudadela) {
        this.ciudadela = ciudadela;
    }

    public String getMzUrb() {
        return mzUrb;
    }

    public void setMzUrb(String mzUrb) {
        this.mzUrb = mzUrb;
    }

    public String getSlUrb() {
        return slUrb;
    }

    public void setSlUrb(String slUrb) {
        this.slUrb = slUrb;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public Long getTipoConsultaUrbano() {
        return tipoConsultaUrbano;
    }

    public void setTipoConsultaUrbano(Long tipoConsultaUrbano) {
        this.tipoConsultaUrbano = tipoConsultaUrbano;
    }

    public List<DatosRenLiquidacion> getDatosLiquidacion() {
        return datosLiquidacion;
    }

    public void setDatosLiquidacion(List<DatosRenLiquidacion> datosLiquidacion) {
        this.datosLiquidacion = datosLiquidacion;
    }

    public String getCodAnt1() {
        return codAnt1;
    }

    public void setCodAnt1(String codAnt1) {
        this.codAnt1 = codAnt1;
    }

    public String getCodAnt2() {
        return codAnt2;
    }

    public void setCodAnt2(String codAnt2) {
        this.codAnt2 = codAnt2;
    }

    public String getCodAnt3() {
        return codAnt3;
    }

    public void setCodAnt3(String codAnt3) {
        this.codAnt3 = codAnt3;
    }

    public String getCodAnt4() {
        return codAnt4;
    }

    public void setCodAnt4(String codAnt4) {
        this.codAnt4 = codAnt4;
    }

    public String getCodAnt5() {
        return codAnt5;
    }

    public void setCodAnt5(String codAnt5) {
        this.codAnt5 = codAnt5;
    }

    public String getCodAnt6() {
        return codAnt6;
    }

    public void setCodAnt6(String codAnt6) {
        this.codAnt6 = codAnt6;
    }

    public String getCodAnt7() {
        return codAnt7;
    }

    public void setCodAnt7(String codAnt7) {
        this.codAnt7 = codAnt7;
    }

    public String getCodAnt8() {
        return codAnt8;
    }

    public void setCodAnt8(String codAnt8) {
        this.codAnt8 = codAnt8;
    }

    public String getContribuyenteConsulta() {
        return contribuyenteConsulta;
    }

    public void setContribuyenteConsulta(String contribuyenteConsulta) {
        this.contribuyenteConsulta = contribuyenteConsulta;
    }

    public String getPropietarioUrbano() {
        return propietarioUrbano;
    }

    public void setPropietarioUrbano(String propietarioUrbano) {
        this.propietarioUrbano = propietarioUrbano;
    }

    public Long getTipoConsultaRural() {
        return tipoConsultaRural;
    }

    public void setTipoConsultaRural(Long tipoConsultaRural) {
        this.tipoConsultaRural = tipoConsultaRural;
    }

    public String getContribuyenteConsultaRural() {
        return contribuyenteConsultaRural;
    }

    public void setContribuyenteConsultaRural(String contribuyenteConsultaRural) {
        this.contribuyenteConsultaRural = contribuyenteConsultaRural;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getDivisionUrb() {
        return divisionUrb;
    }

    public void setDivisionUrb(String divisionUrb) {
        this.divisionUrb = divisionUrb;
    }

    public String getNumDepartamento() {
        return numDepartamento;
    }

    public void setNumDepartamento(String numDepartamento) {
        this.numDepartamento = numDepartamento;
    }

    public String getPredialAnterior() {
        return predialAnterior;
    }

    public void setPredialAnterior(String predialAnterior) {
        this.predialAnterior = predialAnterior;
    }

    public String getProvinciaAnt() {
        return provinciaAnt;
    }

    public void setProvinciaAnt(String provinciaAnt) {
        this.provinciaAnt = provinciaAnt;
    }

    public String getCantonAnt() {
        return cantonAnt;
    }

    public void setCantonAnt(String cantonAnt) {
        this.cantonAnt = cantonAnt;
    }

    public String getParroquiaAnt() {
        return parroquiaAnt;
    }

    public void setParroquiaAnt(String parroquiaAnt) {
        this.parroquiaAnt = parroquiaAnt;
    }

    public String getManzanaAnt() {
        return manzanaAnt;
    }

    public void setManzanaAnt(String manzanaAnt) {
        this.manzanaAnt = manzanaAnt;
    }

    public String getZonaAnt() {
        return zonaAnt;
    }

    public void setZonaAnt(String zonaAnt) {
        this.zonaAnt = zonaAnt;
    }

    public String getSectorAnt() {
        return sectorAnt;
    }

    public void setSectorAnt(String sectorAnt) {
        this.sectorAnt = sectorAnt;
    }

    public String getPropiedadHorizontalAnt() {
        return propiedadHorizontalAnt;
    }

    public void setPropiedadHorizontalAnt(String propiedadHorizontalAnt) {
        this.propiedadHorizontalAnt = propiedadHorizontalAnt;
    }

    public String getSolarAnt() {
        return solarAnt;
    }

    public void setSolarAnt(String solarAnt) {
        this.solarAnt = solarAnt;
    }

    public String getPredialAnt() {
        return predialAnt;
    }

    public void setPredialAnt(String predialAnt) {
        this.predialAnt = predialAnt;
    }

    public String getClaveCat() {
        return claveCat;
    }

    public void setClaveCat(String claveCat) {
        this.claveCat = claveCat;
    }

    public BigDecimal getAreaSolar() {
        return areaSolar;
    }

    public void setAreaSolar(BigDecimal areaSolar) {
        this.areaSolar = areaSolar;
    }

    public BigDecimal getAreaConstruccion() {
        return areaConstruccion;
    }

    public void setAreaConstruccion(BigDecimal areaConstruccion) {
        this.areaConstruccion = areaConstruccion;
    }

    public BigDecimal getZonaGeo() {
        return zonaGeo;
    }

    public void setZonaGeo(BigDecimal zonaGeo) {
        this.zonaGeo = zonaGeo;
    }

    public BigDecimal getSectorGeo() {
        return sectorGeo;
    }

    public void setSectorGeo(BigDecimal sectorGeo) {
        this.sectorGeo = sectorGeo;
    }

    public BigDecimal getSolarGeo() {
        return solarGeo;
    }

    public void setSolarGeo(BigDecimal solarGeo) {
        this.solarGeo = solarGeo;
    }

    public BigDecimal getLoteGeo() {
        return loteGeo;
    }

    public void setLoteGeo(BigDecimal loteGeo) {
        this.loteGeo = loteGeo;
    }

    public BigDecimal getMzGeo() {
        return mzGeo;
    }

    public void setMzGeo(BigDecimal mzGeo) {
        this.mzGeo = mzGeo;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getCalleS() {
        return calleS;
    }

    public void setCalleS(String calleS) {
        this.calleS = calleS;
    }

    public Boolean getTemporales() {
        return temporales;
    }

    public void setTemporales(Boolean temporales) {
        this.temporales = temporales;
    }

    public Integer getAnioDesde() {
        return anioDesde;
    }

    public void setAnioDesde(Integer anioDesde) {
        this.anioDesde = anioDesde;
    }

    public Integer getAnioHasta() {
        return anioHasta;
    }

    public void setAnioHasta(Integer anioHasta) {
        this.anioHasta = anioHasta;
    }

    public Boolean getEsUrbano() {
        return esUrbano;
    }

    public void setEsUrbano(Boolean esUrbano) {
        this.esUrbano = esUrbano;
    }

    public String getNombrePropietarios() {
        return nombrePropietarios;
    }

    public void setNombrePropietarios(String nombrePropietarios) {
        this.nombrePropietarios = nombrePropietarios;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

}
