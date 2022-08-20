(function () {
    'use strict';

    angular.module('myApp.controllers')

        .controller('HomeCtrl', [
            '$rootScope',
            '$log',
            '$state',
            '$timeout',
            '$location',
            'menu',
            '$interval',
            '$http',
            '$scope',
            '$window',
            '$mdDialog',
            function ($rootScope, $log, $state, $timeout, $location, menu, $interval, $http, $scope, $window, $mdDialog) {

                var vm = this;

                //functions for menu-link and menu-toggle
                vm.isOpen = isOpen;
                vm.toggleOpen = toggleOpen;
                vm.autoFocusContent = false;
                vm.menu = menu;

                var timeout = 1;
                $scope.counter = timeout * 60;
                $scope.minutes = timeout;
                $scope.seconds = 0;

                var mytimeout = null;

                $scope.onTimeout = function () {
                    if ($scope.counter === 0) {
                        $scope.$broadcast('timer-stopped', 0);
                        $timeout.cancel(mytimeout);
                        return;
                    }
                    $scope.counter--;
                    $scope.seconds = Math.floor(($scope.counter) % 60);
                    $scope.minutes = Math.floor((($scope.counter / (60)) % 60));
                    mytimeout = $timeout($scope.onTimeout, 1000);
                };

                function startTimer() {
                    mytimeout = $timeout($scope.onTimeout, 1000);
                }

                function resetTimer() {
                    $scope.counter = timeout * 60;
                    $scope.minutes = timeout;
                    $scope.seconds = 0;
                    $timeout.cancel(mytimeout);
                    startTimer();
                }


                $scope.$on('timer-stopped', function (event, remaining) {
                    if (remaining === 0) {
                        $http({
                            method: 'GET',
                            url: './api/refreshsession'
                        }).then(function successCallback(value) {
                            if (value.data.success === true) {
                                resetTimer();
                            } else {
                                openDialog()
                            }
                        }, function errorCallback(value) {
                            openDialog()
                        });
                    }
                });

                startTimer();


                function openDialog() {
                    $mdDialog.show({
                        parent: angular.element(document.body),
                        clickOutsideToClose: false,
                        template:
                            '<md-dialog-content class="md-dialog-content" role="document" tabindex="-1">\n' +
                            '<h2 class="md-title ng-binding">Ваша сесія завершена</h2>\n' +
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

                vm.status = {
                    isFirstOpen: true,
                    isFirstDisabled: false
                };


                function isOpen(section) {
                    return menu.isSectionSelected(section);
                }

                function toggleOpen(section) {
                    menu.toggleSelectSection(section);
                }


            }])

        .controller('main', function ($scope, $mdSidenav, $mdDialog, $cookies, httpService) {

            httpService.get('./api/getusername').then(
                function (value) {
                    $scope.username = value.pib;
                })

            if ($cookies.get('xxxx') === undefined) {
                $scope.xxxx = true
                $cookies.put('xxxx', $scope.xxxx);
            } else {
                $scope.xxxx = ($cookies.get('xxxx')) === 'true';
            }


            $scope.toggleLeft = function () {
                $scope.xxxx = true
                $cookies.put('xxxx', $scope.xxxx);
                $mdSidenav("left")
                    .toggle();
            };

            $scope.toggleLeft2 = function () {
                $scope.xxxx = !$scope.xxxx;
                $cookies.put('xxxx', $scope.xxxx);
            };

            var originatorEv;

            $scope.openMenu = function ($mdMenu, ev) {
                originatorEv = ev;
                $mdMenu.open(ev);
            };

            $scope.info = function () {
                $mdDialog.show(
                    $mdDialog.alert()
                        .parent(angular.element(document.querySelector('#popupContainer')))
                        .clickOutsideToClose(true)
                        .title('Пропозиції та зауваження')
                        .textContent('o.koshurnikov@zpep.com.ua')
                        .ok('OK')
                        .targetEvent(originatorEv)
                );
            };


            $scope.redial = function () {
                $mdDialog.show(
                    $mdDialog.alert()
                        .targetEvent(originatorEv)
                        .clickOutsideToClose(true)
                        .parent('body')
                        .title('')
                        .textContent('')
                        .ok('')
                );

                originatorEv = null;
            };
        })

        .controller('load_tab', function ($scope, $mdSidenav, $mdDialog, httpService, $http, $window, $mdToast) {
            $scope.period = '';
            $scope.activated = false;
            $scope.enable_sent_email = false;

            $scope.load_tab_init = function () {
                httpService.post("./api/getperiod", null).then(
                    function (data) {
                        $scope.states = data;
                    })
            }

            $scope.download_files = function () {
                $scope.activated = true;
                var file_spr = document.getElementById('file_spr').files[0];
                var file_data = document.getElementById('file_data').files[0];

                if (file_spr === undefined || file_data === undefined || $scope.period === '') {
                    $scope.activated = false;
                    displayToast("error-toast", "Оберіть всі параметри");
                    return;
                }

                read_file(file_spr, file_data);
            }


            function read_file(file_spr, file_data) {
                var data_data = undefined;
                var data_spr = undefined;

                var reader = new FileReader();
                reader.onload = function (e) {
                    try {
                        data_data = reader.result

                        var reader2 = new FileReader();
                        reader2.onload = function (e) {
                            try {
                                data_spr = reader2.result
                                upload(data_spr, data_data)

                            } catch (e) {
                                $window.alert("failed: " + e);
                            }
                        };
                        reader2.readAsDataURL(file_spr);

                    } catch (e) {
                        $window.alert("failed: " + e);
                    }
                };
                reader.readAsDataURL(file_data);

            }


            function upload(data_spr, data_data) {
                $http({
                    method: 'POST',
                    url: 'files',
                    data: {'data_data': data_data, 'data_spr': data_spr, 'period': $scope.period, 'email': $scope.enable_sent_email}
                })
                    .then(function success(response) {
                        document.getElementById("file_spr").value = '';
                        document.getElementById("file_data").value = '';
                        $scope.activated = false;

                        if (response.data.message === "success") {
                            displayToast("success-toast", "Завантажено успішно");
                        }

                        if (response.data.message === "spr_fail") {
                            displayToast("error-toast", "Помилка в файлі spr.DBF");
                        }

                        if (response.data.message === "data_fail") {
                            displayToast("error-toast", "Помилка в файлі data.DBF");
                        }

                    }, function errorCallback(response) {
                        $scope.activated = false;
                        displayToast("error-toast", "Сталася помилка");
                    });
            }

            var displayToast = function (type, msg) {
                $mdToast.show(
                    $mdToast.simple()
                        .textContent(msg)
                        .position('top')
                        .theme(type)
                        .hideDelay(30000)
                );
            };

        })

        .controller('tab', function ($scope, $mdSidenav, $mdDialog, httpService) {
            $scope.b_table_r1 = false;
            $scope.tab_init = function () {

                httpService.get('./api/getusername').then(
                    function (value) {

                        $scope.showadmintab = value.tabadmin;


                    })
                httpService.post("./api/getperiod", null).then(
                    function (data) {
                        $scope.states = data;
                    })
            }


            $scope.change_period = function () {
                $scope.b_table_r1 = true;
                $scope.info = false;

                httpService.get('./gettublist/' + $scope.period.id).then(
                    function (value) {

                        if (value.length === 0) {
                            $scope.b_table_r1 = false;
                            $scope.info = true;
                            $scope.alert = 'По данному періоду дані не представлені';
                        } else {
                            $scope.datas = value;
                            $scope.b_table_r1 = true;
                            $scope.info = false;

                        }


                    })
            }



        })

})
();