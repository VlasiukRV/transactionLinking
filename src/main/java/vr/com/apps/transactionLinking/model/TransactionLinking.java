package vr.com.apps.transactionLinking.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import vr.com.apps.transactionLinking.EMatchingOptions;
import vr.com.apps.transactionLinking.service.banksTransactions.BankStatement;
import vr.com.apps.transactionLinking.model.entity.BankTransaction;
import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.transactionLinking.model.entity.Operation;
import vr.com.apps.transactionLinking.service.ResourceList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class TransactionLinking {

    private Map<Operation, OperationOwners> transactionsLinking = new HashMap<>();

    @Autowired
    @Qualifier(value = "emptyCustomer")
    private Customer emptyCustomer;

    public TransactionLinking() {

    }

    public Map<Operation, OperationOwners> getTransactionsLinking() {
        return transactionsLinking;
    }

    public Set<Operation> getOperations(){
        return transactionsLinking.keySet();
    }

    public void linkSetTransactionsToCustomers(Set<ResourceList<BankStatement>> setTransactions, Set<Customer> customerList){
        for (ResourceList<BankStatement> bankStatementList : setTransactions) {
            linkTransactionsToCustomers(bankStatementList, customerList);
        }
    }

    public void linkTransactionsToCustomers(ResourceList<BankStatement> bankStatementList, Set<Customer> customerList) {

        for (BankStatement bankStatement : bankStatementList) {
            BankTransaction bankTransaction = getBankTransaction(bankStatement);

            Set<Customer> fullMatchingSet = new HashSet<>();
            Set<Customer> partlyMatchingSet = new HashSet<>();
            for (Customer customer : customerList) {
                EMatchingOptions matchingRes = bankStatement.isBankStatementForCurrentCustomer(customer);
                switch (matchingRes) {
                    case FULL:
                        fullMatchingSet.add(customer);
                        break;
                    case PARTLY:
                        partlyMatchingSet.add(customer);
                        break;
                }
                /*if (fullMatchingSet.size() > 0) {
                    break;
                }*/
            }
            bankTransaction.setCustomer(emptyCustomer);
            if (fullMatchingSet.size() > 0){
                for (Customer customer : fullMatchingSet) {
                    bankTransaction.setCustomer(customer);
                    addRecordToTransactionsLinking(bankTransaction, customer);
                }
            }else if(partlyMatchingSet.size() > 0){
                for (Customer customer : partlyMatchingSet) {
                    addRecordToTransactionsLinking(bankTransaction, customer);
                }
            }else{
                addRecordToTransactionsLinking(bankTransaction, emptyCustomer);
            }
        }

    }

    private BankTransaction getBankTransaction(BankStatement bankStatement){
        BankTransaction bankTransaction = null;

        Set<Operation> operations = transactionsLinking.keySet();
        for (Operation operation : operations) {
            if (operation instanceof BankTransaction){
                BankStatement originalBankStatement = ((BankTransaction) operation).getOriginalBankStatement();
                if (originalBankStatement.equals(bankStatement)){
                    bankTransaction = (BankTransaction) operation;
                }
            }
        }
        if (bankTransaction == null){
            bankTransaction = new BankTransaction();
        }
        bankTransaction.setDate(bankStatement.getDate());
        bankTransaction.setAmount(bankStatement.getAmount());
        bankTransaction.setOriginalBankStatement(bankStatement);

        return bankTransaction;
    }

    public void removeRecordFromTransactionsLinking(Operation operation, Customer customer){
        if (transactionsLinking.containsKey(operation)){
            OperationOwners operationOwners = transactionsLinking.get(operation);
            operationOwners.removeOperationOwner(customer);
        }
    }

    public void addRecordToTransactionsLinking(Operation operation, Customer customer){

        OperationOwners operationOwners;
        if (transactionsLinking.containsKey(operation)) {
            operationOwners = transactionsLinking.get(operation);
        }else {
            operationOwners = new OperationOwners(operation);
            transactionsLinking.put(operation, operationOwners);
        }
        operationOwners.addOperationOwner(customer);
    }

    @Override
    public String toString() {
        return "TransactionLinking:{" +
                ", transactionsLinking: " + transactionsLinking +
                '}';
    }
}
