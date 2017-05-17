//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.sugarcrm.www.sugarcrm;

import com.sugarcrm.www.sugarcrm.Sugarsoap;
import com.sugarcrm.www.sugarcrm.SugarsoapBindingStub;
import com.sugarcrm.www.sugarcrm.SugarsoapPortType;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class SugarsoapLocator extends Service implements Sugarsoap {
    private static final String PRODUCTION_URL = "https://suite.mygubbi.com/mygubbi_crm/soap.php";
    private String sugarsoapPort_address;
    private String sugarsoapPortWSDDServiceName = "sugarsoapPort";
    private HashSet ports = null;
    private String url;

    private SugarsoapLocator() {
    }

    public SugarsoapLocator(EngineConfiguration config) {
        super(config);
    }

    public SugarsoapLocator(String url) {
        this.sugarsoapPort_address = url;
    }

    public SugarsoapLocator(String wsdlLoc, QName sName) throws ServiceException {
        super(wsdlLoc, sName);
    }

    public String getsugarsoapPortAddress() {
        return this.sugarsoapPort_address;
    }

    public String getsugarsoapPortWSDDServiceName() {
        return this.sugarsoapPortWSDDServiceName;
    }

    public void setsugarsoapPortWSDDServiceName(String name) {
        this.sugarsoapPortWSDDServiceName = name;
    }

    public SugarsoapPortType getsugarsoapPort() throws ServiceException {
        URL endpoint;
        try {
            endpoint = new URL(this.sugarsoapPort_address);
        } catch (MalformedURLException var3) {
            throw new ServiceException(var3);
        }

        return this.getsugarsoapPort(endpoint);
    }

    public SugarsoapPortType getsugarsoapPort(URL portAddress) throws ServiceException {
        try {
            SugarsoapBindingStub e = new SugarsoapBindingStub(portAddress, this);
            e.setPortName(this.getsugarsoapPortWSDDServiceName());
            return e;
        } catch (AxisFault var3) {
            return null;
        }
    }

    public void setsugarsoapPortEndpointAddress(String address) {
        this.sugarsoapPort_address = address;
    }

    public Remote getPort(Class serviceEndpointInterface) throws ServiceException {
        try {
            if(SugarsoapPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                SugarsoapBindingStub t = new SugarsoapBindingStub(new URL(this.sugarsoapPort_address), this);
                t.setPortName(this.getsugarsoapPortWSDDServiceName());
                return t;
            }
        } catch (Throwable var3) {
            throw new ServiceException(var3);
        }

        throw new ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null?"null":serviceEndpointInterface.getName()));
    }

    public Remote getPort(QName portName, Class serviceEndpointInterface) throws ServiceException {
        if(portName == null) {
            return this.getPort(serviceEndpointInterface);
        } else {
            String inputPortName = portName.getLocalPart();
            if("sugarsoapPort".equals(inputPortName)) {
                return this.getsugarsoapPort();
            } else {
                Remote _stub = this.getPort(serviceEndpointInterface);
                ((Stub)_stub).setPortName(portName);
                return _stub;
            }
        }
    }

    public QName getServiceName() {
        return new QName("http://www.sugarcrm.com/sugarcrm", "sugarsoap");
    }

    public Iterator getPorts() {
        if(this.ports == null) {
            this.ports = new HashSet();
            this.ports.add(new QName("http://www.sugarcrm.com/sugarcrm", "sugarsoapPort"));
        }

        return this.ports.iterator();
    }

    public void setEndpointAddress(String portName, String address) throws ServiceException {
        if("sugarsoapPort".equals(portName)) {
            this.setsugarsoapPortEndpointAddress(address);
        } else {
            throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    public void setEndpointAddress(QName portName, String address) throws ServiceException {
        this.setEndpointAddress(portName.getLocalPart(), address);
    }
}
