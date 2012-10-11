package MagMo;

import java.util.List;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

class Connector {

    OMFactory fac;
    OMNamespace noNs;
    OMNamespace mag;
    OMNamespace soapEnc;
    OMNamespace soapXml;
    OMNamespace xsi; 
    OMNamespace xsd;

    
    private static final String SESSION_ID="sessionId";
    private static final String RESOURCE_PATH="resourcePath";
    private static final String ARGUMENTS="args";
    
    Connector(){
        fac = OMAbstractFactory.getOMFactory();
        
        noNs = fac.createOMNamespace("", "");
        mag = fac.createOMNamespace("urn:Magento","mag");
        xsi = fac.createOMNamespace("http://www.w3.org/2001/XMLSchema-instance","xsi");
        xsd = fac.createOMNamespace("http://www.w3.org/2001/XMLSchema","xsd");
        soapXml = fac.createOMNamespace("http://xml.apache.org/xml-soap","SOAP-XML");
        soapEnc = fac.createOMNamespace("http://schemas.xmlsoap.org/soap/encoding/","SOAP-ENC");
    }
    
    OMElement makeCreateCall(String sessionId, String methodPath, List<Object> args){
        OMElement method = fac.createOMElement(Method.CORE_CALL, mag);
        
        method.declareNamespace(soapEnc);
        method.declareNamespace(soapXml);
        method.declareNamespace(xsi);
        method.declareNamespace(xsd);
        
        OMElement paramSession = fac.createOMElement(SESSION_ID, noNs);
        paramSession.addChild(fac.createOMText(paramSession, sessionId));
        method.addChild(paramSession);
        
        OMElement resourcePath = fac.createOMElement(RESOURCE_PATH, noNs);
        resourcePath.addChild(fac.createOMText(resourcePath, methodPath));
        method.addChild(resourcePath);
        
        
        OMElement paramArgs = fac.createOMElement(ARGUMENTS, noNs);
        
        paramArgs.addAttribute("arrayType", xsd.getPrefix()+":ur-type["+args.size()+"]", soapEnc);
        paramArgs.addAttribute("type",soapEnc.getPrefix()+":Array",xsi);

        for(Object arg:args) {
            paramArgs.addChild(typedElement(noNs,"item", arg));
        }
        //paramArgs.addChild(itemArg);
        method.addChild(paramArgs);

        return method;
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    private OMElement typedElement(OMNamespace elementNs, String name, Object value) {
        if (value instanceof String) {
            return this.typedElement(elementNs, name, (String) value, xsd.getPrefix()+":string");
        } else if(value instanceof Integer) {
            return this.typedElement(elementNs, name, ((Integer) value).toString(), xsd.getPrefix()+":int");
        } else if(value instanceof Double) {
            return this.typedElement(elementNs, name, ((Double) value).toString(), xsd.getPrefix()+":float");
        } else if(value instanceof Map) {
            Map<String,Object> argMap = (Map<String,Object>) value;
            
            OMElement mapArg = fac.createOMElement(name, elementNs);
            mapArg.addAttribute("type", soapXml.getPrefix()+":Map", xsi);
            for(String key:argMap.keySet()) {
                mapArg.addChild(keyValue(key,argMap.get(key)));
            }
            return mapArg;
        }
        throw new RuntimeException("keyValue not implemented for " + value.getClass().toString());
    }
    
    private OMElement typedElement(OMNamespace elementNs, String name, String value, String valueType) {
        OMElement element = fac.createOMElement(name, elementNs);
        element.addAttribute("type",valueType,xsi);
        element.addChild(fac.createOMText(element, value));
        return element;
    }
    
    
    private OMElement keyValue(String key, Object value) {
        OMElement item = fac.createOMElement("item",noNs);
        item.addChild(typedElement(noNs, "key", key));
        item.addChild(typedElement(noNs, "value", value));
        return item;
    }
    
} 