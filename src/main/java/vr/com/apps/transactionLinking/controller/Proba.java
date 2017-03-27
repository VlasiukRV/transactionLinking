package vr.com.apps.transactionLinking.controller;

import vr.com.apps.transactionLinking.service.banksTransactions.BankStatement;
import vr.com.apps.transactionLinking.service.ResourceList;
import vr.com.apps.transactionLinking.service.banksTransactions.PBResourceListBankStatement;
import vr.com.apps.transactionLinking.service.ResourceListOrders;
import vr.com.apps.transactionLinking.model.TransactionLinking;
import vr.com.apps.utility.resourceReader.ResourceProperty;
import vr.com.apps.utility.resourceReader.EResourceType;

import java.io.IOException;

public class Proba {

    public Proba() {

    }

    public static void main(String[] args) throws IOException{
        Proba test = new Proba();
        test.test();

    }

    public void test() throws IOException{


        ResourceList<BankStatement> bankStatementList = new PBResourceListBankStatement(new ResourceProperty(EResourceType.FILE));
        ResourceListOrders orderList = new ResourceListOrders<>(new ResourceProperty(EResourceType.FILE));

        orderList.setRequestProperty("$FileAddress", "E:\\Work\\oders.txt");
        orderList.downloadResource();
        bankStatementList.setRequestProperty("$FileAddress", "E:\\Work\\transactions.txt");
        bankStatementList.downloadResource();

        TransactionLinking transactionLinking1 = new TransactionLinking();
    }

}
