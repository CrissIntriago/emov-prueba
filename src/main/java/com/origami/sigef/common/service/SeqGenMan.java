/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service;

import java.math.BigInteger;

/**
 *
 * @author gutya
 */
//@Local
public interface SeqGenMan {

    BigInteger getSecuencia(String code);

    void crearSecuencia(String code);

    public BigInteger getSecuenciaAmbiente(String code);
    
    public void crearSecuenciaAmbinete(String code);
}
