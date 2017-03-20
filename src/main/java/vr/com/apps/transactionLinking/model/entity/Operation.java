package vr.com.apps.transactionLinking.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Operation {
    protected static int operationCounter;
    @JsonProperty("id")
    protected String id;
    @JsonProperty("date")
    protected Date date;
    @JsonProperty("amount")
    protected Long amount;
    @JsonProperty("operationType")
    private EOperationType operationType;

    public Operation() {
        operationCounter++;
        setId(Integer.toString(operationCounter));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getAmount() {
        return this.amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public EOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(EOperationType operationType) {
        this.operationType = operationType;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        String amountStr = ""+amount;/*operationType == EOperationType.INCOME ? "+" + (amount/100) : "-" + (amount/100);*/
        return "{" +
                "date: " +date+
                ", amount: " +amountStr+
                ", operationType: " +operationType+
                '}';
    }
}
