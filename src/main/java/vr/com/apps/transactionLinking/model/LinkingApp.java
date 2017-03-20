package vr.com.apps.transactionLinking.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vr.com.apps.transactionLinking.model.entity.*;
import vr.com.apps.transactionLinking.model.report.ReportTransactionsHistory;
import vr.com.apps.transactionLinking.service.ResourceList;
import vr.com.apps.transactionLinking.service.ResourceListOrders;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class LinkingApp {

    // Model
    private Set<Customer> customerList = new HashSet<>();
    private Set<ResourceList<BankStatement>> bankStatementsList = new HashSet<>();
    @Autowired
    private ResourceListOrders<Order> orderList;

    // Service
    @Autowired
    private TransactionLinking transactionLinking;
    @Autowired
    private ReportTransactionsHistory reportTransactionsHistory;

    public LinkingApp() {
    }

    public void makeCustomersList(){
        for (Order order : orderList) {
            customerList.add(order.getCustomer());
        }
    }

    public Set<Customer> getCustomerList() {
        return customerList;
    }

    public Customer getCustomer(String customerId){
        for (Customer currentCustomer : customerList) {
            if (currentCustomer.getId().equals(customerId)){
                return currentCustomer;
            }
        }
        return null;
    }

    public Operation getOperation(String operationId){
        Set<Operation> operations = transactionLinking.getOperations();
        for (Operation currentOperation : operations) {
            if (currentOperation.getId().equals(operationId)){
                return currentOperation;
            }
        }
        return null;
    }

    public ResourceListOrders getOrderList() {
        return orderList;
    }

    public void addBankStatement(ResourceList<BankStatement> bankStatement){
        if (bankStatement == null){
            return;
        }

        if (!bankStatementsList.contains(bankStatement)) {
            bankStatementsList.add(bankStatement);
        }
    }

    public Set<ResourceList<BankStatement>> getBankStatementsList() {
        return bankStatementsList;
    }

    public TransactionLinking getTransactionLinking() {
        return transactionLinking;
    }

    public ReportTransactionsHistory getReportTransactionsHistory() {
        return reportTransactionsHistory;
    }

    public void makeReportTransactionsHistory(){
        reportTransactionsHistory.clear();

        for (Customer customer : customerList) {
            for (Order order : orderList) {
                if (order.getCustomer().equals(customer)) {
                    reportTransactionsHistory.addIncomingCustomerOperation(order.getCustomer(), order);
                }
            }
        }

        Map<Operation, OperationOwners> transactionsLinking = this.transactionLinking.getTransactionsLinking();
        Set<Operation> operationSet = transactionsLinking.keySet();
        for (Operation operation : operationSet) {
            BankTransaction bt = (BankTransaction)operation;
            reportTransactionsHistory.addOutcomeCustomerOperation(bt.getCustomer(), operation);
        }
    }

    public void makeTransactionLinking(){
        makeCustomersList();
        transactionLinking.linkSetTransactionsToCustomers(bankStatementsList, customerList);
        makeReportTransactionsHistory();
    }

}
