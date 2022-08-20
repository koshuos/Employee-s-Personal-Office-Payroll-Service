(function () {

    'use strict';

    angular.module('common.services')

        .factory('httpService', [
            '$http',
            '$window',
            '$mdDialog',
            function ($http, $window, $mdDialog) {
                return {
                    post: function (url, data) {
                        return $http({
                            method: 'POST',
                            url: url,
                            data: data
                        })
                            .then(function successCallback(response) {
                                return response.data;
                            }, function errorCallback(response) {
                                openDialog()
                            });
                    },
                    get: function (url) {
                        return $http({
                            method: 'GET',
                            url: url
                        }).then(function successCallback(response) {
                            return response.data;
                        }, function errorCallback(response) {
                            openDialog()
                        });
                    },
                    del: function (url) {
                        return $http({
                            method: 'DELETE',
                            url: url
                        }).then(function successCallback(response) {
                            return response.data;
                        }, function errorCallback(response) {
                            openDialog()
                        });
                    }
                };

                function openDialog() {
                    $mdDialog.show({
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        template:
                            '<md-dialog-content class="md-dialog-content" role="document" tabindex="-1">\n' +
                            '<h2 class="md-title ng-binding" style="color: #ff0000">Сталася помилка</h2>\n' +
                            '<p class="md-title ng-binding">Додаток буде перезавантажено</p>\n' +
                            '</md-dialog-content>\n' +
                            '<md-dialog-actions>\n' +
                            '<md-button class="md-raised md-primary"  ng-click="closeDialog()">OK</md-button>\n' +
                            '</md-dialog-actions>',
                        locals: {},
                        controller: DialogController
                    });

                    function DialogController($scope, $mdDialog, $window) {
                        $scope.closeDialog = function () {
                            $mdDialog.hide();
                            $window.location.reload();
                        };
                    }
                }
            }])
})();