package vr.com.apps.transactionLinking.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import vr.com.apps.transactionLinking.EMatchingOptions;

public abstract class BankStatement extends Operation {

    @JsonProperty("benchmarkData")
    protected String benchmarkData;

    public BankStatement() {
        super();
        setOperationType(EOperationType.BANK_TRANSACTION);
    }

    public BankStatement(String benchmarkData) {
        this();
        this.benchmarkData = benchmarkData;
    }

    public String getBenchmarkData() {
        return benchmarkData;
    }

    public void setBenchmarkData(String benchmarkData) {
        this.benchmarkData = benchmarkData;
    }

    public abstract EMatchingOptions isBankStatementForCurrentCustomer(Customer customer);
}
