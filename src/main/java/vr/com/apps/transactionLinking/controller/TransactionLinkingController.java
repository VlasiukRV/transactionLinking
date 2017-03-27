package vr.com.apps.transactionLinking.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import vr.com.apps.transactionLinking.model.LinkingApp;
import vr.com.apps.transactionLinking.service.banksTransactions.BankStatement;
import vr.com.apps.transactionLinking.model.entity.BankTransaction;
import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.transactionLinking.service.ResourceList;
import vr.com.apps.transactionLinking.service.banksTransactions.EBanksKinds;
import vr.com.apps.utility.Utils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/transactionLinking")
public class TransactionLinkingController {

    @Autowired
    private LinkingApp linkingApp;
    @Autowired
    @Qualifier(value = "emptyCustomer")
    private Customer emptyCustomer;

    @Resource(name="bankStatementsList")
    private Map<EBanksKinds, ResourceList<BankStatement>> bankStatementsList;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.HEAD})
    public String main(HttpSession httpSession, ModelMap model) {
        model.addAttribute("message", "Transaction Linking!" );

        return "transactionLinking";
    }

    @RequestMapping(value = "/settings/setFileResource/{resourceType}", method = RequestMethod.PATCH, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String setLinkingResource(@PathVariable("resourceType")String resourceType,
                                   @RequestParam String filename,
                                   @RequestParam String strBankKind){

        boolean success = true;

        ResourceList currentResource = null;
        if (resourceType.equals("Orders")) {
            currentResource = linkingApp.getOrderList();
        }else if(resourceType.equals("Transactions")){
            EBanksKinds bankKind = EBanksKinds.valueOf(strBankKind);
            if (bankStatementsList.containsKey(bankKind)) {
                currentResource = bankStatementsList.get(bankKind);
                linkingApp.addBankStatement(currentResource);
            }
        }

        if(currentResource != null) {
            success = downloadFileResource(currentResource, filename);
        }

        JSONObject json = new JSONObject();
        json.put("success", success);

        return json.toString();
    }

    @RequestMapping(value = "/resource/banksKinds", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<EBanksKinds> getBanksKinds(){
        return Arrays.asList(EBanksKinds.values());
    }

    @RequestMapping(value = "/resource/customerList", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public Set<Customer> getCustomerList() {
        return linkingApp.getCustomerList();
    }

    @RequestMapping(value = "/result", method = RequestMethod.PATCH, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String updateTransactionLinkingRes() {
        boolean success = true;

        linkingApp.makeTransactionLinking();

        JSONObject json = new JSONObject();
        json.put("success", success);

        return json.toString();
    }

    @RequestMapping(value = "/result", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String refreshTransactionLinkingRes(){
        return Utils.getJSONOfObject(linkingApp.getTransactionLinking().getTransactionsLinking());
    }

    @RequestMapping(value = "/record/{operationId}", method = RequestMethod.PATCH, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String changeRecordInTransactionsLinking(@PathVariable("operationId") String operationId,
                                                     @RequestParam("currentCustomerId") String currentCustomerId,
                                                     @RequestParam("newCustomerId") String newCustomerId){
        boolean success = true;

        BankTransaction operation = (BankTransaction)linkingApp.getOperation(operationId);
        Customer currentCustomer = linkingApp.getCustomer(currentCustomerId);
        Customer newCustomer = linkingApp.getCustomer(newCustomerId);

        if (operation != null) {
            if (newCustomer == null) {
                newCustomer = emptyCustomer;
            }
            operation.setCustomer(newCustomer);
        }

        JSONObject json = new JSONObject();
        json.put("success", success);

        return json.toString();
    }

    @RequestMapping(value = "/getReportTransactionsHistory", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String getReportTransactionsHistory() {
        linkingApp.makeReportTransactionsHistory();
        return Utils.getJSONOfObject(linkingApp.getReportTransactionsHistory());
    }

    private boolean downloadFileResource(ResourceList currentResource, String filename){
        boolean downloadResource = false;

        if (currentResource != null) {
            if (!filename.equals("")) {
                currentResource.setRequestProperty("$FileAddress", filename);
                downloadResource = true;
            }
        }

        if (downloadResource) {
            try {
                currentResource.downloadResource();
            } catch (IOException ex) {
                System.out.println("Error download resource " + currentResource);
                downloadResource = false;
            }
        }

        return downloadResource;
    }
}