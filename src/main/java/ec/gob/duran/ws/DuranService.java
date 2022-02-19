/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.duran.ws;
import java.util.List;
import javax.ejb.Local;
/**
 *
 * @author Arturo
 */
@Local
public interface DuranService {
    public Object methodGET(String url, Class resultClazz);

    public List methodListGET(String url, Class resultClazz);

    public Object methodPOST(Object data, String url, Class resultClass);

    public List methodListPOST(Object data, String url, Class resultClass);

    public List methodListPOST(List data, String url, Class resultClass);

    public Object methodPUT(Object data, String url, Class resultClass);

    public List methodListPUT(List data, String url, Class resultClass);

    public Object methodPOST(String url, Class resultClass);

    public List methodListPOST(String url, Class resultClass);

    public String autenticate(String user, String pass);
}
