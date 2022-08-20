(function(){
  'use strict';
  
  angular.module('common.directives')
      .run(['$templateCache', function($templateCache) {
        $templateCache.put('xyz.html', "" +
            "" +
            "<div ng-repeat='bookmark in bookmarks'>{{bookmark.name}}</div>");
      }])

      .directive('fooBar', function () {
        var bookmarks = [
          {
            id: 1,
            name: 'ччч'
          },
          {
            id: 2,
            name: 'ччч2'
          }
        ];
        return {
          restrict: 'E',
          templateUrl: "xyz.html",
          link: function (scope, element, attrs) {
            scope.bookmarks = bookmarks;

          }
        };
      })
})();