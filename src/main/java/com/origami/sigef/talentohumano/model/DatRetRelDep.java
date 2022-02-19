/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author jesus
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "datRetRelDep", propOrder = {"empleado", "suelSal", "sobSuelComRemu", "partUtil", "intGrabGen", "impRentEmpl", "decimTer", "decimCuar", "fondoReserva",
    "salarioDigno", "otrosIngRenGrav", "ingGravConEsteEmpl", "sisSalNet", "apoPerIess", "aporPerIessConOtrosEmpls", "deducVivienda", "deducSalud",
    "deducEducartcult", "deducAliement", "deducVestim", "exoDiscap", "exoTerEd", "basImp", "impRentCaus", "valRetAsuOtrosEmpls", "valImpAsuEsteEmpl", "valRet"})
public class DatRetRelDep {

    private Empleado empleado;
    private Double suelSal;
    private Double sobSuelComRemu;
    private Double partUtil;
    private Double intGrabGen;
    private Double impRentEmpl;
    private Double decimTer;
    private Double decimCuar;
    private Double fondoReserva;
    private Double salarioDigno;
    private Double otrosIngRenGrav;
    private Double ingGravConEsteEmpl;
    private Integer sisSalNet;
    private Double apoPerIess;
    private Double aporPerIessConOtrosEmpls;
    private Double deducVivienda;
    private Double deducSalud;
    private Double deducEducartcult;
    private Double deducAliement;
    private Double deducVestim;
    private Double exoDiscap;
    private Double exoTerEd;
    private Double basImp;
    private Double impRentCaus;
    private Double valRetAsuOtrosEmpls;
    private Double valImpAsuEsteEmpl;
    private Double valRet;

    public DatRetRelDep() {
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Double getSuelSal() {
        return suelSal;
    }

    public void setSuelSal(Double suelSal) {
        this.suelSal = suelSal;
    }

    public Double getSobSuelComRemu() {
        return sobSuelComRemu;
    }

    public void setSobSuelComRemu(Double sobSuelComRemu) {
        this.sobSuelComRemu = sobSuelComRemu;
    }

    public Double getPartUtil() {
        return partUtil;
    }

    public void setPartUtil(Double partUtil) {
        this.partUtil = partUtil;
    }

    public Double getIntGrabGen() {
        return intGrabGen;
    }

    public void setIntGrabGen(Double intGrabGen) {
        this.intGrabGen = intGrabGen;
    }

    public Double getImpRentEmpl() {
        return impRentEmpl;
    }

    public void setImpRentEmpl(Double impRentEmpl) {
        this.impRentEmpl = impRentEmpl;
    }

    public Double getDecimTer() {
        return decimTer;
    }

    public void setDecimTer(Double decimTer) {
        this.decimTer = decimTer;
    }

    public Double getDecimCuar() {
        return decimCuar;
    }

    public void setDecimCuar(Double decimCuar) {
        this.decimCuar = decimCuar;
    }

    public Double getFondoReserva() {
        return fondoReserva;
    }

    public void setFondoReserva(Double fondoReserva) {
        this.fondoReserva = fondoReserva;
    }

    public Double getSalarioDigno() {
        return salarioDigno;
    }

    public void setSalarioDigno(Double salarioDigno) {
        this.salarioDigno = salarioDigno;
    }

    public Double getOtrosIngRenGrav() {
        return otrosIngRenGrav;
    }

    public void setOtrosIngRenGrav(Double otrosIngRenGrav) {
        this.otrosIngRenGrav = otrosIngRenGrav;
    }

    public Double getIngGravConEsteEmpl() {
        return ingGravConEsteEmpl;
    }

    public void setIngGravConEsteEmpl(Double ingGravConEsteEmpl) {
        this.ingGravConEsteEmpl = ingGravConEsteEmpl;
    }

    public Integer getSisSalNet() {
        return sisSalNet;
    }

    public void setSisSalNet(Integer sisSalNet) {
        this.sisSalNet = sisSalNet;
    }

    public Double getApoPerIess() {
        return apoPerIess;
    }

    public void setApoPerIess(Double apoPerIess) {
        this.apoPerIess = apoPerIess;
    }

    public Double getAporPerIessConOtrosEmpls() {
        return aporPerIessConOtrosEmpls;
    }

    public void setAporPerIessConOtrosEmpls(Double aporPerIessConOtrosEmpls) {
        this.aporPerIessConOtrosEmpls = aporPerIessConOtrosEmpls;
    }

    public Double getDeducVivienda() {
        return deducVivienda;
    }

    public void setDeducVivienda(Double deducVivienda) {
        this.deducVivienda = deducVivienda;
    }

    public Double getDeducSalud() {
        return deducSalud;
    }

    public void setDeducSalud(Double deducSalud) {
        this.deducSalud = deducSalud;
    }

    public Double getDeducEducartcult() {
        return deducEducartcult;
    }

    public void setDeducEducartcult(Double deducEducartcult) {
        this.deducEducartcult = deducEducartcult;
    }

    public Double getDeducAliement() {
        return deducAliement;
    }

    public void setDeducAliement(Double deducAliement) {
        this.deducAliement = deducAliement;
    }

    public Double getDeducVestim() {
        return deducVestim;
    }

    public void setDeducVestim(Double deducVestim) {
        this.deducVestim = deducVestim;
    }

    public Double getExoDiscap() {
        return exoDiscap;
    }

    public void setExoDiscap(Double exoDiscap) {
        this.exoDiscap = exoDiscap;
    }

    public Double getExoTerEd() {
        return exoTerEd;
    }

    public void setExoTerEd(Double exoTerEd) {
        this.exoTerEd = exoTerEd;
    }

    public Double getBasImp() {
        return basImp;
    }

    public void setBasImp(Double basImp) {
        this.basImp = basImp;
    }

    public Double getImpRentCaus() {
        return impRentCaus;
    }

    public void setImpRentCaus(Double impRentCaus) {
        this.impRentCaus = impRentCaus;
    }

    public Double getValRetAsuOtrosEmpls() {
        return valRetAsuOtrosEmpls;
    }

    public void setValRetAsuOtrosEmpls(Double valRetAsuOtrosEmpls) {
        this.valRetAsuOtrosEmpls = valRetAsuOtrosEmpls;
    }

    public Double getValImpAsuEsteEmpl() {
        return valImpAsuEsteEmpl;
    }

    public void setValImpAsuEsteEmpl(Double valImpAsuEsteEmpl) {
        this.valImpAsuEsteEmpl = valImpAsuEsteEmpl;
    }

    public Double getValRet() {
        return valRet;
    }

    public void setValRet(Double valRet) {
        this.valRet = valRet;
    }

}
