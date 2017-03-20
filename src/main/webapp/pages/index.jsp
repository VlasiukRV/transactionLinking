<!DOCTYPE html>

<html ng-app="app">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Transaction linking</title>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css" rel="stylesheet">

    <!-- Vendor libraries -->
    <script type="text/javascript" src="/resources/lib/jquery-3.1.1.js"></script>
    <script type="text/javascript" src="/resources/lib/angular-v1.2.22.js"></script>
    <script type="text/javascript" src="/resources/lib/angular-route-v1.2.22.js"></script>
    <script type="text/javascript" src="/resources/lib/angular-sanitize-v1.2.22.js"></script>
    <script type="text/javascript" src="/resources/lib/angular-resource.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.10.0/ui-bootstrap.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-bootstrap/0.10.0/ui-bootstrap-tpls.js"></script>


    <script type="text/javascript" src="//netdna.bootstrapcdn.com/bootstrap/3.3.2/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/resources/css/transactionLinking.css"/>

</head>

<body>

<h1>${message}</h1>

<br>

<div ng-controller="transactionLinkingSettingsController">

    <span class="tableName">
        <b> Transactions settings: </b>
    </span>

    <table class="dataTable">
        <tr>
            <td> Orders: </td>
            <td>
                <input title="file order" type="text" ng-model="ordersFilename"/>
                <button ng-click="setLinkingResourceOrders()" type="button" size="sm" title="update">
                    <span class="glyphicon glyphicon-download"></span>
                </button>
            </td>
        </tr>
        <tr>
            <td>Transactions:</td>
            <td>
                <select ng-model="bankKind"
                        ng-options="o as o for o in banksKinds">
                </select>
                <input title="bank transactions" type="text" ng-model="transactionsFilename"/>
                <button ng-click="setLinkingResourceTransactions()" type="button" size="sm" title="update">
                    <span class="glyphicon glyphicon-download"></span>
                </button>
            </td>
        </tr>
    </table>

    <br>

</div>

<div ng-controller="transactionLinkingController">

    <span class="tableName">
        <button ng-click="updateTransactionLinkingRes()" type="button" size="sm" title="update">
            <span class="glyphicon glyphicon-download"></span>
        </button>
        <button ng-click="refreshTransactionLinkingRes()" type="button" size="sm" title="refresh">
            <span class="glyphicon glyphicon-refresh"></span>
        </button>
        <b> Transactions linking: </b>
    </span>

    <span>
        <table width="900" border="0" class="dataTable">
            <tr class="tableHeader dataTableRow">
                <td width="200" align="center"> <b> date: </b> </td>
                <td width="100" align="center"> <b> amount: </b> </td >
                <td> <b> benchmark data: </b> </td>
                <td width="200" align="center"> <b>link transaction: </b> </td>
                <td width="150" align="center"> <b>  </b> </td>
            </tr>

            <tr ng-repeat="operation in transactionsLinking.operations | filter:showOperation"
                ng-class="{matchingFull: operation.levelLinking == 1, matchingPartly: operation.levelLinking == 2, matchingNo: operation.levelLinking == 3}"
                bgcolor="#FFFDD0"
                class="dataTableRow">

                <td title="operation date" width="200" align="left" valign="top"> {{operation.operation.date | date:'yyyy-MM-dd HH:mm:ss'}} </td>
                <td title="amount" width="100" align="right"> {{operation.operation.amount | currency}} </td>
                <td title="benchmark data"> {{operation.benchmarkData}} </td>
                <td width=200>
                    <span ng-hide="operation.editCustomer" ng-click="editOperationCustomer(operation)" title="customer of transaction">
                        {{operation.operation.customer.customerPresentation}}
                    </span>
                    <span ng-hide="!operation.editCustomer">
                        <select ng-model="newCustomer"
                                ng-change="changeRecordInTransactionsLinking(operation.operationId, operation.operation.customer.id, newCustomer)"
                                ng-options="newCustomer.customerPresentation for newCustomer in operation.operationOwners">
                        </select>
                    </span>
                </td>

                <td width="150">
                    <button ng-click="editOperationCustomer(operation)" type="button" size="sm" title="edit customer">
                        <span class="glyphicon glyphicon-pencil"></span>
                    </button>
                    <button ng-click="changeRecordInTransactionsLinking(operation.operationId, operation.operation.customer.id, null)" type="button" size="sm" title="del customer">
                        <span class="glyphicon glyphicon-remove"></span>
                    </button>
                </td>

            </tr>
            <tr>
                <label>Filter: <%--<input ng-model="filterLevelLinking">--%>
                    <button ng-click="showHideMatchingFull()" ng-class="{matchingFull: showMatchingFull == 1}" title="show/hide full matching"></button>
                    <button ng-click="showHideMatchingPartly()" ng-class="{matchingPartly: showMatchingPartly == 2}" title="show/hide partly matching"></button>
                    <button ng-click="showHideMatchingNo()" ng-class="{matchingNo: showMatchingNo == 3}" title="show/hide no matching"></button>
                </label>
            </tr>
        </table>

    </span>

    <br>

</div>

<div ng-controller="saleReportController">

    <span class="tableName">
        <button ng-click="refreshSaleReport()" type="button" size="sm" title="refresh report">
            <span class="glyphicon glyphicon-refresh"></span>
        </button>
        <b>Transactions history:</b> <%--<b>{{saleReport.reportName}}:</b>--%>
    </span>

    <span>

        <table width="900" class="dataTable">
            <tr class="tableHeader dataTableRow">
                <td width="10%" align="center"> <b> name: </b></td>
                <td>
                    <table width="100%" >
                        <td width="100" align="center"> <b> amount: </b> </td>
                        <td width="200" align="center"> <b> date: </b> </td >
                        <td align="center"> <b> operation: </b> </td>
                    </table>
                </td>
            </tr>

            <%--<tr ng-repeat="reportLine in saleReport.reportLines | filter:{customer: {id: filterCustomer}}" bgcolor="#FFFDD0" class="dataTableRow">--%>
            <tr ng-repeat="reportLine in saleReport.reportLines | filter:{customer: {customerPresentation: filterCustomer}}" bgcolor="#FFFDD0" class="dataTableRow">
                <td title="customer" width="10%" align="left" valign="top">
                    <b> {{reportLine.customer.customerPresentation}} </b>
                </td>
                <td>
                    <table width="100%">
                        <tr>
                            <td title="total amount" width="100" align="right"> <b> {{reportLine.totalAmount | currency}} </b> </td>
                            <td width="200" align="left"> </td>
                            <td align="left"> </td>
                        </tr>
                    </table>
                    <table width="100%">
                        <tr ng-repeat="operation in reportLine.operations">
                            <td title="amount" width="100" align="right"> {{operation.amount | currency}} </td>
                            <td title="operation date" width="200" align="left"> {{operation.date | date:'yyyy-MM-dd HH:mm:ss'}} </td>
                            <td title="operation type" align="left"> {{operation.operationType}} </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <label>Filter:<input title="filter by customer" type="text" ng-model="filterCustomer"/></label>
            </tr>
        </table>

    </span>

</div>

<!-- Application Files -->
<script type="text/javascript" src="/resources/js/transactionLinkingControllers.js"></script>
<script type="text/javascript" src="/resources/js/transactionLinking.js"></script>

</body>
</html>