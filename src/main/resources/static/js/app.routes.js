(function () {

    'use strict';

    angular.module('myApp', [
        'myApp.controllers',
        'ngRoute',
        'ngAnimate',
        'ui.router',
        'ngMaterial',
        'ngAria',
        'md.data.table',
        'ngMessages',
        'fixed.table.header',
        'ngCookies',
        'material.svgAssetsCache',
        'ngMaterialCollapsible',
    ])
        .config(function ($mdAriaProvider) {
            $mdAriaProvider.disableWarnings();
        })

        .config(function ($mdIconProvider) {
            $mdIconProvider
                .icon('logou', 'images/logoup.svg')
                .icon('info2', 'images/info2.svg')
                .icon('down_arrow', 'images/down_arrow.svg')
                .icon('info', 'images/info.svg')
                .icon('exit', 'images/exit.svg')
                .icon('exit2', 'images/exit2.svg');
            //  .iconSet("social", 'img/icons/sets/social-icons.svg', 24);
        })

        .config(function ($mdDateLocaleProvider) {

            //$mdDateLocaleProvider.firstRenderableDate = new Date(1776, 6, 4);
            //  $mdDateLocaleProvider.lastRenderableDate = new Date();

            $mdDateLocaleProvider.firstDayOfWeek = 1;

            $mdDateLocaleProvider.formatDate = function (date) {
                return date ? moment(date).format('DD-MM-YYYY') : '';
            };

            $mdDateLocaleProvider.parseDate = function (dateString) {
                var m = moment(dateString, 'DD-MM-YYYY', true);
                return m.isValid() ? m.toDate() : new Date(NaN);
            };

            $mdDateLocaleProvider.isDateComplete = function (dateString) {
                dateString = dateString.trim();
                // Look for two chunks of content (either numbers or text) separated by delimiters.
                var re = /^(([a-zA-Z]{3,}|[0-9]{1,4})([ .,]+|[/-]))([a-zA-Z]{3,}|[0-9]{1,4})/;
                return re.test(dateString);
            };
        })

        .config(function ($mdDateLocaleProvider) {

            $mdDateLocaleProvider.months = ['Січень', 'Лютий', 'Березень', 'Квітень', 'Травень', 'Червень', 'Липень', 'Серпень', 'Вересень', 'Жовтень', 'Листопад', 'Грудень'];
            $mdDateLocaleProvider.shortMonths = ['Січ', 'Лют', 'Бер', 'Кв', 'Трав', 'Чер', 'Лип', 'Серп', 'Вер', 'Жовт', 'Лист', 'Груд'];
            $mdDateLocaleProvider.days = ['Понеділок', 'Вівторок', 'Середа', 'Четвер', 'П"ятниця', 'Субота', 'Неділя'];
            $mdDateLocaleProvider.shortDays = ['Нд', 'Пн', 'Вт', 'Ср', 'Чт', 'Сб', 'Нд'];
            $mdDateLocaleProvider.firstDayOfWeek = 1;

        })

        .config(['$stateProvider', '$urlRouterProvider', '$logProvider',
            function ($stateProvider, $urlRouterProvider) {

                $urlRouterProvider.otherwise("/");

                $stateProvider
                    .state('home', {
                        url: '/',

                        views: {

                            '@': {
                                templateUrl: '/ng-templates/home.view.html',
                                controller: 'HomeCtrl as vm'
                            }
                        }
                    })
                    .state('home.gettingstarted', {
                        url: 'start',

                        views: {

                            'content@home': {
                                templateUrl: '/ng-templates/start.view.html'
                            }
                        }
                    })
                    .state('home.d', {
                        url: 'd',
                        abstract: true
                    })
                    .state('home.d.ipas', {
                        url: '/ipas',

                        views: {

                            'content@home': {
                                templateUrl: '/ng-templates/beers.ipa.view.html'
                            }
                        }
                    })
                    .state('home.d.porters', {
                        url: '/porters',

                        views: {

                            'content@home': {
                                templateUrl: '/ng-templates/admin_tabulagram.html'
                            }
                        }
                    })
                    .state('home.d.pdv', {
                        url: '/pdv',

                        views: {

                            'content@home': {
                                templateUrl: '/ng-templates/pdv.view.html'
                            }
                        }
                    })

                    .state('home.d.wheat', {
                        url: '/wheat',

                        views: {

                            'content@home': {
                                templateUrl: '/ng-templates/beers.wheat.view.html'
                            }
                        }
                    })

                    .state('home.d.wheat2', {
                        url: '/wheat2',

                        views: {

                            'content@home': {
                                templateUrl: '/ng-templates/beers.wheat.view2.html'
                            }
                        }
                    })
            }])
        //take all whitespace out of string
        .filter('nospace', function () {
            return function (value) {
                return (!value) ? '' : value.replace(/ /g, '');
            };
        })

        .filter('split', function () {
            return function (input, splitChar, splitIndex) {
                return input.split(splitChar)[splitIndex];
            }
        })

        .filter('capitalize', function () {
            return function (input, scope) {
                if (input != null)
                    input = input.toLowerCase();
                return input.substring(0, 1).toUpperCase() + input.substring(1);
            }
        })

        .filter('cut', function () {
            return function (value, wordwise, max, tail) {
                if (!value) return '';

                max = parseInt(max, 10);
                if (!max) return value;
                if (value.length <= max) return value;

                value = value.substr(0, max);
                if (wordwise) {
                    var lastspace = value.lastIndexOf(' ');
                    if (lastspace !== -1) {
                        //Also remove . and , so its gives a cleaner result.
                        if (value.charAt(lastspace - 1) === '.' || value.charAt(lastspace - 1) === ',') {
                            lastspace = lastspace - 1;
                        }
                        value = value.substr(0, lastspace);
                    }
                }

                return value + (tail || ' …');
            };
        })

        .directive('repComa', function () {
            return {
                restrict: 'A',
                link: function (scope, element, attrs) {
                    scope.$watch(attrs.ngModel, function (value) {
                       debugger
                        if (value !== undefined) {
                            element.val(value.toString().replace(',', '.'))
                        }

                    });
                }
            };
        })

        /*     .directive('addModel', function ($compile) {
                 return {
                     restrict: 'A',
                     link: function (scope, element, attrs) {
                         scope.$watch(
                             function (scope) {
                                 return scope.$eval(attrs.compile);
                             },
                             function (value) {
                                 element.attr('ng-model', attrs.addModel);
                                 element.removeAttr('add-model');  // avoid infinite loop
                                 $compile(element)(scope);
                             }
                         );
                     },
                     replace: true
                 };
             })*/

        .directive('inputClear', function () {
                return {
                    restrict: 'A',
                    compile: function (element, attrs) {
                        var color = attrs.inputClear;
                        var style = color ? "color:" + color + ";" : "";
                        var action = attrs.ngModel + " = ''";
                        element.after(
                            '<md-button class="animate-show md-icon-button md-accent"' +
                            'ng-show="' + attrs.ngModel + '" ng-click="' + action + '"' +
                            'style="position: absolute; top: 0px; right: -6px;">' +
                            '<div style="' + style + '">x</div>' +
                            '</md-button>');
                    }
                }
            }
        )

        //replace uppercase to regular case
        .filter('humanizeDoc', function () {
            return function (doc) {
                if (!doc) return;
                if (doc.type === 'directive') {
                    return doc.name.replace(/([A-Z])/g, function ($1) {
                        return '-' + $1.toLowerCase();
                    });
                }

                return doc.label || doc.name;
            };
        });

})();