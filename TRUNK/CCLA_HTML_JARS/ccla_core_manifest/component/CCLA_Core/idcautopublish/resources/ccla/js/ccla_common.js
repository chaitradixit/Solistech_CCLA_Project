/* Generic helper/utility functions used throughout CCLA.
   
   Should be included on every CCLA page, via the include ccla_common_js.
*/

		
// Adds commas to a numeric string.
// Taken from here: http://www.mredkj.com/javascript/numberFormat.html#addcommas
function addCommas(nStr) {
	nStr += '';
	x = nStr.split('.');
	x1 = x[0];
	x2 = x.length > 1 ? '.' + x[1] : '';
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(x1)) {
		x1 = x1.replace(rgx, '$1' + ',' + '$2');
	}
	return x1 + x2;
}

// Takes a Date object and formats it in the short date style
// (dd/MM/yy)
function formatDateWithShortPattern(date) {

	var twoDigitDate = date.getDate().toString();
	
	if (twoDigitDate.length == 1)
		twoDigitDate = "0" + twoDigitDate;
	
	var twoDigitMonth = (date.getMonth() + 1).toString();
	
	if (twoDigitMonth.length == 1)
		twoDigitMonth = "0" + twoDigitMonth;
		
	var twoDigitYear = date.getFullYear().toString().substring(2);
	
	return (twoDigitDate + "/" + twoDigitMonth + "/" + twoDigitYear);
};