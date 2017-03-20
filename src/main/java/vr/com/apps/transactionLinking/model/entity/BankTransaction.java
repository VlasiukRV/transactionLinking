package vr.com.apps.transactionLinking.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BankTransaction extends Operation {
    @JsonProperty("customer")
    private Customer customer;

    @JsonProperty("originalBankStatement")
    private BankStatement originalBankStatement;

    public BankTransaction() {
        super();
        setOperationType(EOperationType.BANK_TRANSACTION);
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BankStatement getOriginalBankStatement() {
        return originalBankStatement;
    }

    public void setOriginalBankStatement(BankStatement originalBankStatement) {
        this.originalBankStatement = originalBankStatement;
    }
}
