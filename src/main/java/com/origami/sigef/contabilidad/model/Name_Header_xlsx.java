/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Criss Intriago
 */
public class Name_Header_xlsx {
    int codeSheet;
    String nameSheet;
    String [] heardSheet;   
    
    
    public int getCodeSheet() {
        return codeSheet;
    }

    public void setCodeSheet(int codeSheet) {
        this.codeSheet = codeSheet;
    }

    public String getNameSheet() {
        return nameSheet;
    }

    public void setNameSheet(String nameSheet) {
        this.nameSheet = nameSheet;
    }

    public String[] getHeardSheet() {
        return heardSheet;
    }

    public void setHeardSheet(String[] heardSheet) {
        this.heardSheet = heardSheet;
    }
    
    
}
