package ec.com.trafficcontrol;

import com.origami.sigef.common.config.CONFIG;
import java.net.URL;
import javax.ejb.Stateless;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.2.8 Generated source
 * version: 2.2
 *
 */
@Stateless
@WebServiceClient(name = "IntegracionPortalWS", targetNamespace = "http://trafficcontrol.com.ec", wsdlLocation = "/META-INF/wsdl/IntegracionPortalWS.wsdl")
public class IntegracionPortalWS
        extends Service {

    private final static URL INTEGRACIONPORTALWS_WSDL_LOCATION;
    private final static WebServiceException INTEGRACIONPORTALWS_EXCEPTION;
    private final static QName INTEGRACIONPORTALWS_QNAME = new QName("http://trafficcontrol.com.ec", "IntegracionPortalWS");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
//            url = new URL("/META-INF/wsdl/IntegracionPortalWS.wsdl");
            url = IntegracionPortalWS.class.getResource(CONFIG.IP_TC_WEB1);
        } catch (Exception ex) {
            e = new WebServiceException(ex);
        }
        INTEGRACIONPORTALWS_WSDL_LOCATION = url;
        INTEGRACIONPORTALWS_EXCEPTION = e;
    }

    public IntegracionPortalWS() {
        super(__getWsdlLocation(), INTEGRACIONPORTALWS_QNAME);
    }

    public IntegracionPortalWS(WebServiceFeature... features) {
        super(__getWsdlLocation(), INTEGRACIONPORTALWS_QNAME, features);
    }

    public IntegracionPortalWS(URL wsdlLocation) {
        super(wsdlLocation, INTEGRACIONPORTALWS_QNAME);
    }

    public IntegracionPortalWS(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, INTEGRACIONPORTALWS_QNAME, features);
    }

    public IntegracionPortalWS(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public IntegracionPortalWS(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return returns MetodosPort
     */
    @WebEndpoint(name = "MetodosPort")
    public MetodosPort getMetodosPort() {
        return super.getPort(new QName("http://trafficcontrol.com.ec", "MetodosPort"), MetodosPort.class);
    }

    /**
     *
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to
     * configure on the proxy. Supported features not in the
     * <code>features</code> parameter will have their default values.
     * @return returns MetodosPort
     */
    @WebEndpoint(name = "MetodosPort")
    public MetodosPort getMetodosPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://trafficcontrol.com.ec", "MetodosPort"), MetodosPort.class, features);
    }

    private static URL __getWsdlLocation() {
        if (INTEGRACIONPORTALWS_EXCEPTION != null) {
            throw INTEGRACIONPORTALWS_EXCEPTION;
        }
        return INTEGRACIONPORTALWS_WSDL_LOCATION;
    }

}
