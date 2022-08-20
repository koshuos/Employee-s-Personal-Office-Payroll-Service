(function () {

    'use strict';

    angular.module('common.services')

        .factory('menu', [
            '$location',
            '$rootScope',
            function ($location) {

                var sections = [
                  /*  name: '1. виписка з банку',
                    type: 'toggle',
                    pages: [{
                        name: '1.1 Введення виписки',
                        type: 'link',
                        state: 'home.d.ipas'
                    },*/
                        {
                            name: 'Табуляграма',
                            state: 'home.d.wheat',
                            type: 'link'
                        }
                    ]


             sections.push({
                 name: 'xxxxx',
                 state: 'home.d.wheat2',
                 type: 'link'
             })
  /*
                sections.push({
                    name: '3. Бухгалтерія',
                    type: 'toggle',
                    pages: [{
                        name: '3.1 Реєстр платників ПДВ',
                        type: 'link',
                        state: 'home.d.pdv'
                    }]
                })*/
                var self;

                return self = {
                    sections: sections,

                    toggleSelectSection: function (section) {
                        self.openedSection = (self.openedSection === section ? null : section);
                    },
                    isSectionSelected: function (section) {
                        return self.openedSection === section;
                    },

                    selectPage: function (section, page) {
                        page && page.url && $location.path(page.url);
                        self.currentSection = section;
                        self.currentPage = page;
                    }
                };

                function sortByHumanName(a, b) {
                    return (a.humanName < b.humanName) ? -1 :
                        (a.humanName > b.humanName) ? 1 : 0;
                }


            }])

})();