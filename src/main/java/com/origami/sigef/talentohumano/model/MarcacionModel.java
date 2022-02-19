/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.model;

import java.util.Date;

/**
 *
 * @author OrigamiEC
 */
public class MarcacionModel {

    private String emp_code;
    private String firstName;
    private String lastName;
    private String punchTime;
    private String date;
    private String time;
    private String terminalAlias;
    private String areaAlias;
    private String mobile;
    private String punchState;
    private String eventName;
    private String hora_ingreso;
    private String hora_salida;
    private String ingreso_descanso;
    private String salida_descanso;
    private String total_hora;
    private String total_hora_decanso;
    private String horas_laboras;
    private Date horas_extras;
    private String horas_extras_desc;

    private String checkin;
    private String checkout;

    public MarcacionModel() {
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(String punchTime) {
        this.punchTime = punchTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTerminalAlias() {
        return terminalAlias;
    }

    public void setTerminalAlias(String terminalAlias) {
        this.terminalAlias = terminalAlias;
    }

    public String getAreaAlias() {
        return areaAlias;
    }

    public void setAreaAlias(String areaAlias) {
        this.areaAlias = areaAlias;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPunchState() {
        return punchState;
    }

    public void setPunchState(String punchState) {
        this.punchState = punchState;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getHora_ingreso() {
        return hora_ingreso;
    }

    public void setHora_ingreso(String hora_ingreso) {
        this.hora_ingreso = hora_ingreso;
    }

    public String getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(String hora_salida) {
        this.hora_salida = hora_salida;
    }

    public String getIngreso_descanso() {
        return ingreso_descanso;
    }

    public void setIngreso_descanso(String ingreso_descanso) {
        this.ingreso_descanso = ingreso_descanso;
    }

    public String getSalida_descanso() {
        return salida_descanso;
    }

    public void setSalida_descanso(String salida_descanso) {
        this.salida_descanso = salida_descanso;
    }

    public String getTotal_hora() {
        return total_hora;
    }

    public void setTotal_hora(String total_hora) {
        this.total_hora = total_hora;
    }

    public String getTotal_hora_decanso() {
        return total_hora_decanso;
    }

    public void setTotal_hora_decanso(String total_hora_decanso) {
        this.total_hora_decanso = total_hora_decanso;
    }

    public String getHoras_laboras() {
        return horas_laboras;
    }

    public void setHoras_laboras(String horas_laboras) {
        this.horas_laboras = horas_laboras;
    }

    public Date getHoras_extras() {
        return horas_extras;
    }

    public void setHoras_extras(Date horas_extras) {
        this.horas_extras = horas_extras;
    }

    public String getHoras_extras_desc() {
        return horas_extras_desc;
    }

    public void setHoras_extras_desc(String horas_extras_desc) {
        this.horas_extras_desc = horas_extras_desc;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

}
