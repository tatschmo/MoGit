import java.net.MalformedURLException;
import java.net.URL;

import Magento.SalesOrderEntity;
import Magento.SalesOrderListEntity;

public class TestMagento {

    public static void main(String[] args) throws MalformedURLException {
        
    	Connector tester = new Connector(new URL("http://telemachea.de/magento/"),"andrejba","dre123_");
    	tester.login();
    }
}