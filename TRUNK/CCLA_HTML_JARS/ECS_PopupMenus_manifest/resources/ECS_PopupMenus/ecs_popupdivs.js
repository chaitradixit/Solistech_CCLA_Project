// Code required for pop-up div functionality.
// Dependent on the ecs_popup_div_event_register_js include
// ===========================================

// div container
var popUpDiv = document.getElementById("popUpDiv");
// iframe showing content
var popUpDivContent = document.getElementById("popUpDivInner");

// blanket div used to fade background
var blanket = document.getElementById('blanket');

// boolean determining pop-up state
var popupDisplayed = false;
// timer used to control blanket fading
var fadeTimer;

// stores the current popup URL in use
var curPopupUrl;

var curWidth = 0;

// stores the open/closed state of the popup.
// if true: the popup is either open, or in a state of opening.
// if false: the popup is closed/hidden.
var popupOpen = false;

var firstUse = true;

// Adds a load event listener to the pop-up iframe.
// This is used to trigger a callback function to the
// pop-up's parent window after the pop-up content
// has been fully loaded.
$("#popUpDivInner").load(function() {
	window.parent.showPopup(this.contentWindow.document);
});

// Use this function to trigger the pop-up div. The reqString
// parameter should be the URL you wish to show.
// The frame height will be set automatically, based on the
// loaded content of the iframe.
// You may optionally specify a width for the pop-up, if not
// a default width is used
function openPopup(reqString,width) {
	
	if (!reqString)
		throw("openPopup: request URL not defined.");
		
	popupOpen = true;
	
	if (typeof(width) == "undefined" || !width)
		width = defaultPopupWidth;
	
	if (firstUse) {
		// collapse iframe height and move pop-up to
		// centre of the screen.
		resizeElement("popUpDivInner",width,0);
		setWindowPos(width,0);
	
		firstUse = false;
	}
	
	curWidth = width;
	
	togglePageElements();
	
	$("#popUpDivInner").css("visibility","hidden");
	$(popUpDiv).css("visibility","hidden").show();
	
	setBlanketSize();
	
	if (useFadeAnimations) {
		// make the blanket 100% transparant and visible	
		$(blanket).css("opacity",0).css("display","block");
		// trigger fade-in animation
		$(blanket).fadeTo("normal", 0.6, getPopupContent(reqString));
	} else {
		// display semi-transparent blanket over screen
		$(blanket).css("opacity",0.6).css("display","block");
		getPopupContent(reqString);
	}
}

/* Replaces the contents of the iframe pop-up with the
given request URL. 

If reqString starts with 'http://' it is assumed to
be a complete URL. Otherwise, the HttpCgiPath IDOC variable
is prefixed to the given URL.

*/
function getPopupContent(reqString) {
	
	var contentUrl = '';
	
	// build request URL
	if (reqString.indexOf("http://") == 0)
		contentUrl = reqString;
	else
		contentUrl = httpCgiPath + reqString;
		
	popUpDivContent.src = contentUrl;
	curPopupUrl = contentUrl;

}

// Callback function triggered after iframe content
// has finished loading. Requires the document element
// from the loaded page as an argument.
//
// Refreshes the pop-up size and position before displaying
// it to the user via a slide animation
function showPopup(popupDocument) {
	
	if (popUpDivContent.src == "")
		return;
	
	// set width of popup
	$(popUpDivContent).css("width",curWidth);
	
	resizePopup(popupDocument);
	
	// display pop-up container and iframe
	$("#popUpDiv").css("visibility","visible");	
	$("#popUpDivInner").css("visibility","visible");
	
	// animate the pop-up into view
	$("#popUpDivInner").hide().slideDown();
}

// Resizes and re-positions the pop-up iframe, based
// on its content. This must be triggered every time
// the iframe content is changed.
function resizePopup(popupDocument) {
	
	if (isIE) {
		// IE incorrectly reports height of body element,
		// so we take the first child div instead.
		var container = popupDocument.getElementById("popUpContainingDiv");
	} else {
		var container = popupDocument.body;
		//var container = popupDocument.getElementById("popUpContainingDiv");
	}
	
	var contentWidth, contentHeight;
	
	if (container) {
		contentWidth  = getPopupWidth();
		contentHeight = $(container).height();
	} else {
		contentWidth = 0;
		contentHeight = 0;
	}
	
	moveAndSizePopup(contentWidth,contentHeight);
}

// moves and resizes the pop-up to its new dimensions,
// using jQuery animation
function moveAndSizePopup(newWidth, newHeight) {
	
	var newLeft = getPopupCenteredLeft(newWidth);
	var newTop = getPopupCenteredTop(newHeight);
	
	$(popUpDiv).animate(
	 { 
   top: newTop,
   left: newLeft
  }, 300);
	$(popUpDivContent).animate(
	 { 
	height: newHeight,
	width: newWidth
  }, 300);
}

function closePopupAndRedirect(url){
	closePopup();
	window.focus();
	window.location.href=url;
}

function closePopUpAfter3Secs(){
	setTimeout("closePopUp()",3000);
}


// Closes the active popup.
function closePopup() {
	$(popUpDiv).hide();
	
	// reset height of popup
	$(popUpDivContent).css("height",0);
	
	popupOpen = false;
	
	if (useFadeAnimations) {
		// trigger fade-out blanket animation
		$(blanket).fadeOut(function() {togglePageElements();});
	} else {
		$(blanket).css("opacity",0.0).css("display","none");
		togglePageElements();
	}
}

// returns width of popup (in pixels)
function getPopupWidth() {
	return popUpDivContent.offsetWidth;
}

// returns height of popup (in pixels)
function getPopupHeight() {
	return popUpDivContent.offsetHeight;
}

function getPopupContentDocument() {
	
	if (popUpDivContent.document) {
		return popUpDivContent.document;
	} else {		
		return popUpDivContent.contentWindow.document;
	}
}

// Use this function to switch the size/content of the
// popup, while it is currently displayed. 
function switchPopup(reqString,width) {
	
	if (typeof(width) == "undefined" || !width)
		width = defaultPopupWidth;
	
	$(popUpDivContent).css("visibility","hidden");
	$(popUpDivContent).css("width",width);
	
	// Load new content to iframe
	getPopupContent(reqString);
}

// resizes the element with given ID to the given
// height and width (in pixels)
function resizeElement(elemID,width,height) {
	var el = document.getElementById(elemID);
	
	if (width)
		el.style.width = width + "px";
	
	if (height)
		el.style.height = height + "px";
}

// returns the viewable height of the current page
function getViewportHeight() {
	var viewportHeight;
	
	// Get viewport height
	if (typeof window.innerHeight != 'undefined') {
		viewportHeight = window.innerHeight;
	} else if ((typeof document.documentElement.clientWidth != 'undefined') && 
							document.documentElement.clientWidth != 0) {
		viewportHeight = document.documentElement.clientHeight;
	} else {
		viewportHeight = document.getElementsByTagName('body')[0].clientHeight;
	}
	
	return viewportHeight;
}

// returns the vertical scroll offset of the current page
function getVerticalScrollOffset() {
	var scrollY;

	if(document.documentElement && document.documentElement.scrollTop ) {
	scrollY = document.documentElement.scrollTop;
}
else if( document.body && document.body.scrollTop ) {
	scrollY = document.body.scrollTop;
}
else if( window.pageYOffset ) {
	scrollY = window.pageYOffset;
}
else if( window.scrollY ) {
	scrollY = window.scrollY;
}

if (scrollY == undefined)
	scrollY = 0;
	
	return scrollY;
}

// Sets the size of the 'blanket' - the transparent div which
// covers the entire screen when a pop-up div is displayed.
function setBlanketSize() {
	var viewportHeight, fullPageHeight, scrollY, blanketHeight;
	
	// Get viewport height
	viewportHeight = getViewportHeight();
	
	// height of page BODY element		
	fullPageHeight = document.body.scrollHeight;

	blanket.style.height = Math.max(fullPageHeight,viewportHeight) + 'px';
}

/** Sets the position of the pop-up div, so it appears
	centered on the user's screen. */
function setWindowPos(popUpWidth,popUpHeight) {
	//alert("setting window position: " + popUpWidth + "," + popUpHeight);
	
	if (typeof window.innerWidth != 'undefined') {
		viewportwidth = window.innerWidth;
	} else {
		viewportwidth = document.documentElement.clientWidth;
	}
	
	if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)) {
		window_width = viewportwidth;
	} else {
		if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
			window_width = document.body.parentNode.clientWidth;
		} else {
			window_width = document.body.parentNode.scrollWidth;
		}
	}
	
	window_width=window_width/2-(popUpWidth/2);
	popUpDiv.style.left = window_width + 'px';
	
	var scrollY = getVerticalScrollOffset();
	
	var viewportHeight = getViewportHeight();
	
	var popupTop = (viewportHeight/2) - (popUpHeight/2) + scrollY;	
	popUpDiv.style.top = popupTop + 'px';
}

// computes the required pixel size for the css 'left' attribute
// for a popup with the given width.
function getPopupCenteredLeft(popUpWidth) {
	if (typeof window.innerWidth != 'undefined') {
		viewportwidth = window.innerWidth;
	} else {
		viewportwidth = document.documentElement.clientWidth;
	}
	
	if ((viewportwidth > document.body.parentNode.scrollWidth) && (viewportwidth > document.body.parentNode.clientWidth)) {
		window_width = viewportwidth;
	} else {
		if (document.body.parentNode.clientWidth > document.body.parentNode.scrollWidth) {
			window_width = document.body.parentNode.clientWidth;
		} else {
			window_width = document.body.parentNode.scrollWidth;
		}
	}
	
	var popupLeft = window_width/2-(popUpWidth/2);
	return popupLeft;
}

// computes the required pixel size for the css 'top' attribute
// for a popup with the given height.
function getPopupCenteredTop(popUpHeight) {
	
	var scrollY = getVerticalScrollOffset();
	
	var viewportHeight = getViewportHeight();
	var popupTop = (viewportHeight/2) - (popUpHeight/2) + scrollY;

	return popupTop;
}

function setBlanketOpacity(val) {
	blanket.style.opacity = val/10;
	blanket.style.filter = 'alpha(opacity=' + val*10 + ')';
}

// List of elements which are hidden when a pop-up div is displayed.
var hideDivElements = new Array();

// Hides/shows all troublesome HTML page elements, which may clip
// over the pop-up div blanket layer.
function togglePageElements() {
	
	if (!popupOpen) {			
		// Display all previously-hidden elements
		for (var ielem=0; ielem<hideDivElements.length; ielem++) {
			hideDivElements[ielem].style.visibility = 'visible';
		}
		
		hideDivElements.length = 0;
		
		return;
	}
	
	// Collect all select elements for IE 6 and below
	var browser=navigator.appName;
	var bVersion=navigator.appVersion;
	var version=parseFloat(bVersion);
	
	if (browser == 'Microsoft Internet Explorer' && version <= 4) {
		var selects = document.getElementsByTagName("select");
		
		for (var isel=0; isel<selects.length; isel++) {
			hideDivElements.push(selects[isel]);
		}
	}
	
	// Collect all object elements (e.g. applets)
	var objs = document.getElementsByTagName("object");
	for (var iobj=0; iobj<objs.length; iobj++) {
		hideDivElements.push(objs[iobj]);
	}
	
	// Search for Hera preview frame
	var previewFrame = document.getElementById("previewFrame");
	if (previewFrame)
		hideDivElements.push(previewFrame);
	
	// Hook function, see default include ecs_popup_hide_elements_extra
	// used in IrisCore component for a standard implementation
	if (doHideElementsExtra)
		doHideElementsExtra(hideDivElements);
	
	// Make all collected elements invisible.
	for (var ielem=0; ielem<hideDivElements.length; ielem++) {
		hideDivElements[ielem].style.visibility = 'hidden';
	}
}