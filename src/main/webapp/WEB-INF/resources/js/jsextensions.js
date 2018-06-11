"use strict";

Array.prototype.indexes = function() {

	var indexList = [ ];

	this.forEach(function(e, i, a) {

		indexList.push(i);

	});

	return indexList;

};