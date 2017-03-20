////////////////////////////////////
// MODEL
////////////////////////////////////

function resourceService(_transactionLinkingService
    , _reportTransactionsHistoryService
    , _resources
    , _transactionLinkingEditService
    , _transactionSettingsService) {

    var transactionLinkingService           = _transactionLinkingService;
    var reportTransactionsHistoryService    = _reportTransactionsHistoryService;
    var resources                           = _resources;
    var transactionLinkingEditService       = _transactionLinkingEditService;
    var transactionSettingsService          = _transactionSettingsService;

    return{
        getTransactionLinkingService: function(){
            return transactionLinkingService;
        },
        getReportTransactionsHistoryService: function(){
            return reportTransactionsHistoryService;
        },
        getResources: function(){
            return resources;
        },
        getTransactionLinkingEditService: function(){
            return transactionLinkingEditService;
        },
        getTransactionSettingsService: function(){
            return transactionSettingsService;
        }
    }
}

function dataStorage(resourceService) {

    var utils = new Utils();
    var reportTransactionHistory = {};
    var transactionsLinking = {};
    var customerList = {};

    return {
        getUtils: function(){
            return utils;
        },

        setReportTransactionHistory: function (_data) {
            reportTransactionHistory = _data;
        },
        getReportTransactionHistory: function () {
            return reportTransactionHistory;
        },

        setTransactionsLinking: function (_data) {
            transactionsLinking = _data;
        },
        getTransactionsLinking: function () {
            return transactionsLinking;
        },

        setCustomerList: function(_date){
            customerList = _date;
        },
        getCustomerList: function(){
            return customerList;
        }
    }
}

function Customer(){
    this.id = "";
    this.firstName = "";
    this.lastName = "";

    Object.defineProperty(this, "customerPresentation", {
        enumerable: true,
        get: function() {
            return "" + this.firstName + " " + this.lastName + " (" + this.id + ") ";
        }
    });

}

function BankKinds(dataStorage){
    this.dataStorage = dataStorage;
    this.bankKinds = [];

    this.get = function(){
        return this.bankKinds;
    };
    this.update = function(resourceService, f){
        resourceService.getResources().query({resourceName: 'banksKinds'}, updateBankKinds.bind(this, f))
    };

    updateBankKinds = function(f, data) {
        data.forEach(function (element, i, arr) {
            this.bankKinds.push(element);
        }, this);
        f(this);
    }
}

function CustomerList(dataStorage){
    this.dataStorage = dataStorage;
    this.customerList = [];

    this.get = function(){
        return this.customerList;
    };

    this.update = function (resourceService, f){
        resourceService.getResources().query({resourceName:'customerList'}, updateCustomerList.bind(this, f));
        return this.customerList;
    };

    updateCustomerList = function(){
        f = arguments[0];
        data = arguments[1];

        data.forEach(function(element, i, arr){
            var customer = new Customer();
            this.dataStorage.getUtils().fillValuesProperty(element, customer);
            this.customerList.push(customer);
        }, this);
        f(this);
    }

}

function TransactionsLinkingLine(dataStorage){
    this.dataStorage = dataStorage;

    this.operationId = "";
    this.operation = {
        date: "",
        amount: 0,
        customer: {}
    };
    this.benchmarkData = "";

    this.operationOwners = [];

    this.numberOfPossibleOwners = 0;
    this.editCustomer = false;
    this.levelLinking = 0;

    this.updateByServer = function(resourceService){

    };
    this.updateByServerData = function(resourceService, data){
        this.operationId = data.operation.id;
        this.operation.date = this.dataStorage.getUtils().makeDate(data.operation.date);
        this.operation.amount = data.operation.amount / 100;
        this.benchmarkData = data.operation.originalBankStatement.benchmarkData;
        var customer = new Customer();
        this.dataStorage.getUtils().fillValuesProperty(data.operation.customer, customer);
        this.operation.customer = customer;

        this.numberOfPossibleOwners = 0;
        this.operationOwners = [];
        data.operationOwners.forEach(function(owner, i, arr){
            if(owner.id == "P00000"){

            }else {
                var customer = new Customer();
                this.dataStorage.getUtils().fillValuesProperty(owner, customer);
                this.operationOwners.push(customer);
                this.numberOfPossibleOwners++;
            }
        }, this);


        if(customer.id != "P00000"){
            this.levelLinking = 1;
        }else if(this.numberOfPossibleOwners > 0){
            this.levelLinking = 2;
        }else{
            this.levelLinking = 3;
            this.operationOwners = this.dataStorage.getCustomerList().customerList;
        }

    };
}

function TransactionsLinking (dataStorage) {

    this.dataStorage = dataStorage;
    this.operations = [];

    this.updateTransactions = function(resourceService, f) {
        resourceService.getTransactionLinkingService()
            .updateResult({}, {}, updateTransactions.bind(this, f));
    };
    updateTransactions = function(){
        f = arguments[0];
        f(this);
    }

    this.getTransactions = function(resourceService, f) {
        this.operations = [];
        resourceService.getTransactionLinkingService()
            .getResult({}, {}, getTransactions.bind(this, resourceService, f));
        return this;
    };

    getTransactions = function(){
        resourceService = arguments[0];
        f = arguments[1];
        data = arguments[2];
        for (var key in data) {
            if (key.indexOf("$") == 0) {
                continue;
            }
            var originalOperation = data[key];
            var transactionsLinkingLine = new TransactionsLinkingLine(this.dataStorage);
            transactionsLinkingLine.updateByServerData.call(transactionsLinkingLine, resourceService, originalOperation);
            this.operations.push(transactionsLinkingLine);
        }
        f(this);
    }
}

function Operation(){
    this.operationId    = "";
    this.date           = "";
    this.amount         = 0;
    this.operationType  = "";
    this.customer       = {};
}

function ReportTransactionHistoryLine(){
    this.customer = {};
    this.totalAmount = 0;
    this.operations = [];
}

function ReportTransactionHistory(dataStorage){
    this.dataStorage = dataStorage;

    this.reportName = "";
    this.reportLines = [];

    this.updateReport = function(resourceService, f){
        resourceService.getReportTransactionsHistoryService().get({}, updateReport.bind(this, resourceService, f));
    };

    updateReport = function(){
        var resourceService = arguments[0];
        var f = arguments[1];
        var data = arguments[2];

        this.reportName = data.reportName;
        for(var key in data.customerSale){
            var originalReportLine = data.customerSale[key];

            var customer = new Customer();
            this.dataStorage.getUtils().fillValuesProperty(originalReportLine.customer, customer);
            var reportTransactionHistoryLine =  new ReportTransactionHistoryLine();
            reportTransactionHistoryLine.customer = customer;
            reportTransactionHistoryLine.totalAmount = originalReportLine.totalAmount / 100;

            reportTransactionHistoryLine.operations = [];
            originalReportLine.operations.forEach(function (item, i, arr) {
                var operation = new Operation();
                this.dataStorage.getUtils().fillValuesProperty(item, operation);
                operation.date = dataStorage.getUtils().makeDate(operation.date);
                operation.amount = operation.amount / 100;
                customer = new Customer();
                dataStorage.getUtils().fillValuesProperty(item.customer, customer);
                operation.customer = customer;
                reportTransactionHistoryLine.operations.push(operation);
            }, this);

            this.reportLines.push(reportTransactionHistoryLine);
        }
        f(this);
    };
}

function Utils() {
    var dateFormat = 'M/d/yy h:mm:ss a';

    return {
        makeDate: function(milisecond){
            return new Date(milisecond)
        },
        fillValuesProperty: function(source, receiver){
            for (var key in source) {
                if(!(key in receiver)){
                    continue;
                }
                receiver[key] = source[key];
            }
            return receiver;
        }
    }
}
