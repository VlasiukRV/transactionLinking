package vr.com.apps.transactionLinking.service.banksTransactions;

import com.fasterxml.jackson.annotation.JsonProperty;
import vr.com.apps.transactionLinking.EMatchingOptions;
import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.utility.Utils;

public class PBBankStatement extends BankStatement {
    @JsonProperty("description")
    private String description;
    private String bankName = "PB";

    public PBBankStatement() {
        super();
    }

    public PBBankStatement(String benchmarkData){
        super(benchmarkData);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EMatchingOptions isBankStatementForCurrentCustomer(Customer customer){
        EMatchingOptions ResMatchingId = Utils.matchId(description.toUpperCase(), customer.getId().toUpperCase());
        if (ResMatchingId == EMatchingOptions.FULL) {
            return EMatchingOptions.FULL;
        }

        if (Utils.matchString(description.toUpperCase(), customer.getLastName().toUpperCase()) == EMatchingOptions.FULL) {
            if (Utils.matchString(description.toUpperCase(), customer.getFirstName().toUpperCase()) == EMatchingOptions.FULL) {
                return EMatchingOptions.PARTLY;

            }
        }

        return ResMatchingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PBBankStatement)) return false;

        PBBankStatement that = (PBBankStatement) o;

        if (date != null ? !date.equals(that.date) : that.date != null) return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
        if (bankName != null ? !bankName.equals(that.bankName) : that.bankName != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;

        return true;
    }

}
