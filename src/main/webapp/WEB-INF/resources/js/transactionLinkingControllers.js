
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

