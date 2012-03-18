/* http://keith-wood.name/countdown.html
* Turkish initialisation for the jQuery countdown extension
* Written by Bekir Ahmetoğlu (bekir@cerek.com) Aug 2008. */
(function($) {
	$.countdown.regional['tr'] = {
		labels: ['Y\u0131l', 'Ay', 'Hafta', 'G\u00fcn', 'Saat', 'Dakika', 'Saniye'],
		labels1: ['Y\u0131l', 'Ay', 'Hafta', 'G\u00fcn', 'Saat', 'Dakika', 'Saniye'],
		compactLabels: ['y', 'a', 'h', 'g'],
		whichLabels: null,
		timeSeparator: ':', isRTL: false};
	$.countdown.setDefaults($.countdown.regional['tr']);
})(jQuery);
