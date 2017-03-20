
var appAddress = "http://localhost:8080/transactionLinking";
var app = angular.module('app', ['ui.bootstrap', 'ngResource', 'ngRoute'])

        .factory('transactionSettingsService', function($resource){
            return $resource(appAddress+'/settings/:section/:resourceType', {
                    section:        "@section",
                    resourceType:   "@resourceType"
            },
                {
                    setLinkingResource:{
                        method: "PATCH"
                    }
                })
        })
        .factory('transactionLinkingService', function($resource){
            return $resource(appAddress+'/result', {

            },
            {
                updateResult:{
                    method: "PATCH"
                },
                getResult:{
                    method: "GET"
                }
            });
        })
        .factory('transactionLinkingEditService', function($resource){
            return $resource(appAddress+'/record/:operationId', {
                    operationId: "@operationId"
                },
                {
                    change: {
                        method: "PATCH"
                    }
                }
            );
        })
        .factory('reportTransactionsHistoryService', function($resource){
            return $resource(appAddress+'/getReportTransactionsHistory', {});
        })
        .factory('resources', function($resource){
            return $resource(appAddress+'/resource/:resourceName', {});
        })

        .service('resourceService', function(transactionLinkingService, reportTransactionsHistoryService, resources, transactionLinkingEditService, transactionSettingsService) {
            return resourceService(transactionLinkingService, reportTransactionsHistoryService, resources, transactionLinkingEditService, transactionSettingsService)
        })
        .service('dataStorage', function(resourceService) {
            return dataStorage(resourceService);
        })

        .directive('button', function () {
            return directiveButton();
        })

        .config(['$provide', function($provide) {
            $provide.decorator('$locale', ['$delegate', function($delegate) {
                $delegate.NUMBER_FORMATS.PATTERNS[1].negPre = '-\u00A4';
                $delegate.NUMBER_FORMATS.PATTERNS[1].negSuf = '';
                return $delegate;
            }]);
        }]);
;

// Controllers

app.controller('transactionLinkingSettingsController', ['$scope', 'dataStorage', 'resourceService', transactionLinkingSettingsController($scope, dataStorage, resourceService)]);

app.controller('transactionLinkingController', ['$scope', 'dataStorage', 'resourceService', transactionLinkingController($scope, dataStorage, resourceService)]);

app.controller('saleReportController', ['$scope', 'dataStorage', 'resourceService', saleReportController($scope, dataStorage, resourceService)]);