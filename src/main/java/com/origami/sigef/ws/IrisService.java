/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.ws;

import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Local
public interface IrisService {

    public Object methodGET(String url, Class resultClazz);

    public List methodListGET(String url, Class resultClazz);

    public Object methodPOST(Object data, String url, Class resultClass);

    public List methodListPOST(Object data, String url, Class resultClass);

    public List methodListPOST(List data, String url, Class resultClass);

    public Object methodPUT(Object data, String url, Class resultClass);

    public List methodListPUT(List data, String url, Class resultClass);

    public Object methodPOST(String url, Class resultClass);

    public List methodListPOST(String url, Class resultClass);
}
