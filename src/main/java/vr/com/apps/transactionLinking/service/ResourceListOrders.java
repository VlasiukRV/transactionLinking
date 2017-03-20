package vr.com.apps.transactionLinking.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.transactionLinking.model.entity.Operation;
import vr.com.apps.transactionLinking.model.entity.Order;
import vr.com.apps.utility.Utils;
import vr.com.apps.utility.resourceReader.ResourceLoader;
import vr.com.apps.utility.resourceReader.ResourceProperty;
import vr.com.apps.utility.resourceReader.ResourceReader;

import java.io.IOException;

public class ResourceListOrders<T extends Operation> extends ResourceList<T> {

    public ResourceListOrders(ResourceProperty resourceProperty) {
        super(resourceProperty);
    }

    @Override
    public void downloadResource() throws IOException {
        super.downloadResource();
        ResourceReader orderResourceReader = ResourceLoader.getResource(resourceProperty);
        parseResource(orderResourceReader);
    }

    private void parseResource(ResourceReader resourceReader){
        if (!resourceReader.exists()){
            return;
        }
        String resourceStr = resourceReader.getString("UTF-8");

        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(resourceStr);
            JSONArray arrayValue = (JSONArray) obj;
            for (Object anArrayValue : arrayValue) {
                JSONObject order_json = (JSONObject) anArrayValue;
                JSONObject customer_json = (JSONObject) order_json.get("customer");

                Customer customer = new Customer();
                customer.setId((String)customer_json.get("id"));
                customer.setFirstName((String)customer_json.get("firstName"));
                customer.setLastName((String)customer_json.get("lastName"));

                Order order = new Order();
                order.setDate(Utils.parseDate((String) order_json.get("date"), "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
                order.setAmount((long) Math.round(new Float(new Float(String.valueOf(order_json.get("amount"))) * 100)));
                order.setCustomer(customer);

                add((T)order);
            }

        }catch (ParseException ignored){

        }

    }

}
