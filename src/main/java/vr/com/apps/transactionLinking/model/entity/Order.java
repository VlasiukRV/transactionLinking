package vr.com.apps.transactionLinking.model.entity;

public class Order extends Operation {
    private Customer customer;

    public Order() {
        super();
        setOperationType(EOperationType.ORDER);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
