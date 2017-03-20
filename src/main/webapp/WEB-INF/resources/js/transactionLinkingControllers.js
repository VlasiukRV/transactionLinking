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

////////////////////////////////////
// DIRECTIVEs
////////////////////////////////////

function directiveButton(){
    return{
        restrict: 'E',
        compile: function(element, attrs){
            element.addClass('btn');
            if(attrs.type==='submit'){
                element.addClass('btn-primary');
            }else{
                element.addClass('btn-default');
            }
            if(attrs.size){
                element.addClass('btn-' + attrs.size);
            }
        }
    }
}

////////////////////////////////////
// CONTROLLERs
////////////////////////////////////

function transactionLinkingSettingsController($scope, dataStorage, resourceService){

    $scope.ordersFilename = "E:\\Work\\oders.txt";
    $scope.transactionsFilename = "E:\\Work\\transactions.txt";
    $scope.banksKinds = [];
    /*$scope.bankKind = "PB";*/

    $scope.setLinkingResourceOrders = function(){
        setFileResource('Orders', $scope.ordersFilename, "_");
    };
    $scope.setLinkingResourceTransactions = function(){
        setFileResource('Transactions', $scope.transactionsFilename, $scope.bankKind);
    };

    function setBankKinds(resourceService){
        var banksKinds = new BankKinds();
        banksKinds.update.call(banksKinds, resourceService, function(banksKinds){
            $scope.banksKinds = banksKinds.get.call(banksKinds);
        });
    };

    function setFileResource(resourceType, filename, bankKind){
        resourceService.getTransactionSettingsService()
            .setLinkingResource({
                section: "setFileResource",
                resourceType: resourceType,
                filename: filename,
                strBankKind: bankKind
                },
                {

                },
            function(data){

            });
    }

    setBankKinds(resourceService);
}

function transactionLinkingController($scope, dataStorage, resourceService) {

    $scope.changeRecordInTransactionsLinking = function(operationID, currentCustomerId, newCustomer){
        changeRecordInTransactionsLinking(operationID, currentCustomerId, newCustomer);
    };
    $scope.refreshTransactionLinkingRes = function(){
        refreshTransactionLinkingRes();
    };
    $scope.updateTransactionLinkingRes = function(){
        updateTransactionLinkingRes();
    };
    $scope.editOperationCustomer = function(fEditOperationCustomer){
        editOperationCustomer(fEditOperationCustomer)
    };

    $scope.showMatchingFull = 1;
    $scope.showHideMatchingFull = function(){
        if($scope.showMatchingFull == 0){
            $scope.showMatchingFull = 1;
        }else{
            $scope.showMatchingFull = 0;
        }
    };
    $scope.showMatchingPartly = 2;
    $scope.showHideMatchingPartly = function(){
        if($scope.showMatchingPartly == 0){
            $scope.showMatchingPartly = 2;
        }else{
            $scope.showMatchingPartly = 0;
        }
    };
    $scope.showMatchingNo = 3;
    $scope.showHideMatchingNo = function(){
        if($scope.showMatchingNo == 0){
            $scope.showMatchingNo = 3;
        }else{
            $scope.showMatchingNo = 0;
        }
    };
    $scope.showOperation = function(operation){
        return  operation.levelLinking == $scope.showMatchingFull ||
                operation.levelLinking == $scope.showMatchingPartly ||
                operation.levelLinking == $scope.showMatchingNo;
    };

    function refreshTransactionLinkingRes(){
        var customerList = new CustomerList(dataStorage);
        customerList.update.call(customerList, resourceService, function(customerList){
            dataStorage.setCustomerList(customerList);
        });

        var transactionsLinking = dataStorage.getTransactionsLinking();
        if (transactionsLinking != undefined) {
            transactionsLinking.getTransactions.call(transactionsLinking, resourceService, function(transactionsLinking){
                $scope.transactionsLinking = transactionsLinking;
            });
        }
    }

    function updateTransactionLinkingRes(){
        var transactionsLinking = new TransactionsLinking(dataStorage);
        transactionsLinking.updateTransactions.call(transactionsLinking, resourceService, function(transactionsLinking){
            dataStorage.setTransactionsLinking(transactionsLinking);
            refreshTransactionLinkingRes();
        });
    }

    function changeRecordInTransactionsLinking(operationID, currentCustomerId, newCustomer){
        var newCustomerId = '';
        if (newCustomer != null){
            newCustomerId = newCustomer.id;
        }
        if (currentCustomerId == undefined){
            currentCustomerId = "";
        }
        resourceService.getTransactionLinkingEditService()
            .change({
                operationId: operationID,
                currentCustomerId: currentCustomerId,
                newCustomerId:newCustomerId
            },{}, function(date) {
                refreshTransactionLinkingRes();
            }
        );
    }

    function editOperationCustomer(operation){
        if(operation == undefined){
            return;
        }
        operation.editCustomer = !operation.editCustomer;
    }

}

function saleReportController($scope, dataStorage, resourceService) {

    $scope.filterCustomer = "";

    $scope.refreshSaleReport = function(){
        updateTransactionHistory();
    };

    function updateTransactionHistory(){
        var reportTransactionHistory = new ReportTransactionHistory(dataStorage);
        reportTransactionHistory.proba = "Proba";
        reportTransactionHistory.updateReport.call(reportTransactionHistory, resourceService, function(reportTransactionHistory){
            dataStorage.setReportTransactionHistory(reportTransactionHistory);
            $scope.saleReport = reportTransactionHistory;
        });
    }
}

