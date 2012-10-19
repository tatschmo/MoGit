package MagMo;

import javax.xml.namespace.QName;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

public class TestClient {

	public static void main(String[] args) {
		try {
			String endpoint = "http://telemachea.de/magento/index.php/api/soap/?wsdl=1";

			Service service = new Service();
			Call call = (Call) service.createCall();
			call.setTargetEndpointAddress(new java.net.URL(endpoint));
			
			String ret = (String) call.invoke(new Object[] { "Hello World" });
			System.out.println(ret);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
}