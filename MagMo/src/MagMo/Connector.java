package MagMo;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMMetaFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.llom.factory.OMLinkedListMetaFactory;
//import org.apache.commons.beanutils.PropertyUtils;
//import org.mozilla.javascript.Scriptable;

//import com.google.common.collect.Iterables;

public class Connector {

	private OMFactory fac;

	private OMNamespace noNs;

	private OMNamespace mag;

	private OMNamespace soapEnc;

	private OMNamespace soapXml;

	private OMNamespace xsi;

	private OMNamespace xsd;

	private static final String CORE_LOGIN = "login";

	private static final String CORE_CALL = "call";

	private static final String CORE_MULTI_CALL = "multiCall";

	private static final String CORE_LOGOUT = "endSession";

	private static final String SESSION_ID = "sessionId";

	private static final String RESOURCE_PATH = "resourcePath";

	private static final String ARGUMENTS = "args";

	private static final String MULTI_CALLS = "calls";

	private static final String MULTI_CALL_OPTIONS = "options";

	/**
	 * 
	 * @param omMetaFactory
	 *            Useful if you want to pass it from OSGi Service Reference i.e.
	 *            using:
	 * 
	 *            <pre>
	 * &lt;reference interface="org.apache.axiom.om.OMMetaFactory"/&gt;
	 * </pre>
	 */
	public Connector(OMMetaFactory omMetaFactory) {
		super();
		fac = omMetaFactory.getOMFactory();

		// Blank namespace for factory methods which require a namespace as
		// argument
		// but no namespace markers should be added to attributes or tags
		noNs = fac.createOMNamespace("", "");

		// Magento namespace
		mag = fac.createOMNamespace("urn:Magento", "mag");

		// General namespaces, needed for soap
		xsi = fac.createOMNamespace(
				"http://www.w3.org/2001/XMLSchema-instance", "xsi");
		xsd = fac.createOMNamespace("http://www.w3.org/2001/XMLSchema", "xsd");
		soapXml = fac.createOMNamespace("http://xml.apache.org/xml-soap",
				"SOAP-XML");
		soapEnc = fac.createOMNamespace(
				"http://schemas.xmlsoap.org/soap/encoding/", "SOAP-ENC");
	}

	public Connector() {
		this(new OMLinkedListMetaFactory());
	}

	/**
	 * Creates a Soap method for login
	 * 
	 * @param user
	 * @param password
	 * @return the created method as axiom element
	 */
	public OMElement createLoginCall(String user, String password) {
		OMElement method = fac.createOMElement(CORE_LOGIN, mag);

		OMElement paramUser = fac.createOMElement("username", mag);
		paramUser.addChild(fac.createOMText(paramUser, user));
		method.addChild(paramUser);

		OMElement paramKey = fac.createOMElement("apiKey", mag);
		paramKey.addChild(fac.createOMText(paramKey, password));
		method.addChild(paramKey);
		return method;
	}

}