package vr.com.apps.transactionLinking.model;

import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.transactionLinking.model.entity.Operation;

import java.util.HashSet;
import java.util.Set;

public class OperationOwners {

    private Operation operation;
    private Set<Customer> operationOwners = new HashSet<>();

    public OperationOwners() {
    }

    public OperationOwners(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Set<Customer> getOperationOwners() {
        return operationOwners;
    }

    public void setOperationOwners(Set<Customer> operationOwners) {
        this.operationOwners = operationOwners;
    }

    public void addOperationOwner(Customer customer){
        operationOwners.add(customer);
    }

    public void removeOperationOwner(Customer customer){
        operationOwners.remove(customer);
    }
}
