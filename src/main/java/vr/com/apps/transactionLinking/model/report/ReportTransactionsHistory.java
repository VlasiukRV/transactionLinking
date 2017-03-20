package vr.com.apps.transactionLinking.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.transactionLinking.model.entity.Operation;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReportTransactionsHistory {

    @JsonProperty("reportName")
    public String name;
    @JsonProperty("customerSale")
    Map<Customer, OperationsList> customerSale = new HashMap<>();

    public ReportTransactionsHistory() {
    }

    public ReportTransactionsHistory(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void clear(){
        customerSale.clear();
    }

    public boolean addIncomingCustomerOperation(Customer customer, Operation operation){
        boolean res = true;
        OperationsList operationsList = getCustomerSales(customer);
        operationsList.addIncomingOperation(operation);
        return res;
    }

    public boolean addOutcomeCustomerOperation(Customer customer, Operation operation){
        boolean res = true;
        OperationsList operationsList = getCustomerSales(customer);
        operationsList.addOutcomeOperation(operation);
        return res;
    }

    private OperationsList getCustomerSales(Customer customer){
        OperationsList operationsList = new OperationsList();
        if (customerSale.containsKey(customer)){
            operationsList = customerSale.get(customer);
        }else {
            operationsList.setCustomer(customer);
            customerSale.put(customer, operationsList);
        }
        return operationsList;
    }

    @Override
    public String toString() {
        String customerSaleStr = "";
        Set<Customer> customerSet = customerSale.keySet();
        for (Customer customer : customerSet) {
            OperationsList operationsList = customerSale.get(customer);
            String customerStr = "null";
            if (customer != null){
                customerStr = customer.toString();
            }
            customerSaleStr = customerSaleStr + ", {customer: " + customerStr +
                    ", sale: " + operationsList.toString() +
                    "}";
        }
        if (customerSaleStr.length() > 1){
            customerSaleStr = customerSaleStr.substring(1, customerSaleStr.length());
        }
        return "{" +
                "name: '" + name + '\'' +
                ", customerSale: [" + customerSaleStr + "]" +
                '}';
    }
}
