package vr.com.apps.transactionLinking.model.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.transactionLinking.model.entity.Operation;

import java.util.HashSet;
import java.util.Set;

public class OperationsList {
    @JsonProperty("customer")
    private Customer customer;
    @JsonProperty("totalAmount")
    private Long totalAmount = (long) 0;
    @JsonProperty("operations")
    private Set<Operation> operations = new HashSet<>();

    public OperationsList() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public boolean addIncomingOperation(Operation operation){
        boolean res = false;
        if (!operations.contains(operation)){
            this.operations.add(operation);
            this.totalAmount = this.totalAmount + (long) Math.round(operation.getAmount());
            res = true;
        }
        return res;
    }

    public boolean addOutcomeOperation(Operation operation){
        boolean res = false;
        if (!operations.contains(operation)) {
            this.operations.add(operation);
            this.totalAmount = this.totalAmount - (long) Math.round(operation.getAmount());
            res = true;
        }
        return res;
    }

    @Override
    public String toString() {
        return "{" +
                " totalAmount: " + totalAmount +
                ", operations: " + operations + "" +
                '}';
    }
}
