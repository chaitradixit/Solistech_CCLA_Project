// Code required for pop-up menu functionality.
// Dependent on the ecs_popup_menu_static__js include
// ===========================================

var curMenu, curButton;
var actionImage,actionImageOver;
var menuDisplayTimer;
var isMenuActive = false; // determines whether or not a popup is already being displayed

/* Shows/hides the popup menu that relates to the given menu ID. The graphic used
on the toggle button must also be altered based on the visible state of the menu */ 
function togglePopup(menuID,btnID,e,justify,custImage) {
  
  if (isMenuActive)
	 closePopupMenu();
	 
  curMenu    = document.getElementById(menuID);
  curButton = document.getElementById(btnID);
  
  var isTextButton = false;
  
  if (!curButton.src)
	isTextButton = true;
  
  if (isTextButton) {    
	// Text-based toggle button
	
	if (curMenu.style.display == "none") {
	  isMenuActive = true;    
	  displayPopupMenu(curButton,curMenu,justify);

	} else {
	  curMenu.style.display="none";
	}
  
  } else {     
	// image-based toggle button     
	actionImage = new Image();
	actionImageOver = new Image();
  
	var img = httpWebRoot + "images/stellent/ActionsIcon.gif";    
	
	if (custImage) {
	   img = custImage;
	}
	
	var ext = img.substring(img.length - 3, img.length);
	var imgOver = img.substring(0, img.length - 4) + "_over." + ext;

	actionImage.src = img;
	actionImageOver.src = imgOver;

	if (curMenu.style.display == "none") {
	  isMenuActive = true;
	  
	  displayPopupMenu(curButton,curMenu,justify);
	  curButton.src = actionImageOver.src;  
	  
	} else {
	  curMenu.style.display="none";
	  curButton.src = actionImage.src; 
	}
  }
  
  // add mouse listener events
	if (document.addEventListener) { // DOM Level 2 Event Model
	  curMenu.addEventListener("mouseout", popupMouseOut, true);
	  curMenu.addEventListener("mouseover", popupMouseOver, true);
	  curButton.addEventListener("mouseout", parentMouseOut, true);
  } else if (document.attachEvent) { // Older event model
		curMenu.onmouseout = popupMouseOut;
	curMenu.onmouseover = popupMouseOver;
		curButton.onmouseout = parentMouseOut;
  }
  
  e.cancelBubble=true;

}

	/* Global array object used to store a set of HTML
	   elements which must be hidden when a menu is displayed
	   (e.g. applets). They are saved in an array so their
	   visiblity can be restored when the menu is closed. */   
var hideElements = new Array();


/*  Before making a popup menu visible, it is neccesary to check
	whether it will all fit on the screen. If not, it must be
	moved so that the entire menu is visible */
function displayPopupMenu(button,menu,justify) {
  
  var btnWidth = button.offsetWidth;
  var btnHeight = button.offsetHeight;
  
  //btnLeft = button.offsetLeft;
  //btnTop = button.offsetTop;
  
  // Render the menu
  menu.style.visibility = "hidden";
	menu.style.display = "block";
	
	// Get the rendered dimensions of the menu   
	var menuWidth = menu.offsetWidth;  
	var menuHeight = menu.offsetHeight;
	
	var newMenuLeft,newMenuTop;
		
	// Get the offset position of the button
	var btnPos = findPos(button);
	var btnLeft = btnPos[0];
	var btnTop = btnPos[1];
	
	var windowWidth,windowHeight;
	
	windowWidth = document.body.clientWidth;
	windowHeight = document.body.clientHeight;
	
	if (justify != null) {
	
	  if (justify == 'bottom right') {
		// Menu should be displayed below the button, with its
		// right side vertically aligned with the right
		// side of the button
		
		newMenuLeft = (btnLeft + btnWidth - menuWidth);
		newMenuTop = (btnTop + btnHeight);
		
	  } else if (justify == 'bottom left') {
		// Menu should be displayed below the button, with its
		// left side vertically aligned with the left
		// side of the button
		
		newMenuLeft = btnLeft;
		newMenuTop = (btnTop + btnHeight);
	  
	  } else if (justify == 'top window') {
		// Menu should be aligned with the top of the current
		// window, with its right side aligned with the left
		// side of the button.
		
		newMenuLeft = (btnLeft - menuWidth);
		newMenuTop = 5;
	   
	  } else {
		 alert("Invalid justification parameter - check displayPopupMenu() call");
	  }
	
	} else {
	
	  // Use default justification - make the menu appear
	  // to the right of the button, with its top edge aligned
	  // with the top edge of the button.
	  
	  // The menu will attempt to position itself in such a way
	  // that none of it is obscured.

	  if (windowWidth < (btnLeft + btnWidth + menuWidth + 5)) {
		//this menu won't fit to the right, make it appear to the
		//left of the button instead
				newMenuLeft = (btnLeft-menuWidth);
	  } else {
		newMenuLeft = (btnLeft + btnWidth);
	  }
	  
	  // Get scroll position of browser window
	  var scrollX = 0;
			var scrollY = 0;
	  
	  if (document.body.scrollLeft | document.body.scrollTop) {
				scrollX = document.body.scrollLeft;
				scrollY = document.body.scrollTop;
			}
			
			else if (document.documentElement.scrollLeft | document.documentElement.scrollTop) {
				scrollX = document.documentElement.scrollLeft;
				scrollY = document.documentElement.scrollTop;
			}
				
			else if (window.pageXOffset | window.pageYOffset) {
				scrollX = window.pageXOffset;
				scrollY = window.pageYOffset;
			}
			
			var viewportWidth = windowWidth;
			var viewportHeight = windowHeight;
					  
			// Get viewport size
			if (typeof window.innerWidth != 'undefined') {
		  viewportWidth = window.innerWidth;
		  viewportHeight = window.innerHeight;
			} else if (typeof document.documentElement != 'undefined'
			&& typeof document.documentElement.clientWidth !=
			'undefined' && document.documentElement.clientWidth != 0) {
		   viewportWidth = document.documentElement.clientWidth;
		   viewportHeight = document.documentElement.clientHeight;
			} else {
		   viewportWidth = document.getElementsByTagName('body')[0].clientWidth;
		   viewportHeight = document.getElementsByTagName('body')[0].clientHeight;
			}      
					  
	  // Move the menu to its final vertical postion.
	  // Check if the menu will overlap the bottom of the screen.
			if ((btnTop + menuHeight) <= (viewportHeight + scrollY))
			{
				// align top of menu with top of button
				newMenuTop = (btnTop - 5);
			} else {
				// align bottom of menu with bottom of button
				newMenuTop = ((btnTop+btnHeight) - menuHeight);
				//menu.style.top = windowHeight + scrollValue_y - menuHeight - 3; // align with bottom edge of window
			}
			
			// Locate any iframes on the page and ensure the
			// menu doesn't overlap them.
			var iframes = document.getElementsByTagName("iframe");
			for (var ifr=0; ifr<iframes.length; ifr++) {
				var thisFrame = iframes[ifr];
				
				// Ignore any hidden iframes, and the iframe used by
				// the pop-up div code (popUpDivInner)
				if (thisFrame.style.display != "none" && thisFrame.id != "popUpDivInner") {
					var thisFramePos = findPos(thisFrame);
					
					// for now, just check if the menu overlaps an iframe
					// positioned below it. If so, align the bottom of the
					// menu with the top of the iframe.
					if (thisFramePos[1] < (btnTop + menuHeight)) {
						
						newMenuTop = (thisFramePos[1] - menuHeight);
						//alert("Found an iframe! id=" + thisFrame.id + ", new top menu pos: " + newMenuTop);
					}
				}	
			}
		
			var menuPos = new Array();
			menuPos.push(newMenuLeft);
			menuPos.push(newMenuTop);
		
			// hide overlapping applets
			var applets = document.getElementsByTagName("object");
			for (var ib=0; ib<applets.length; ib++) {
				var thisApplet = applets[ib];
				var thisAppletPos = findPos(thisApplet);
				
				// if the element is not already inivisible and overlaps
				// the menu, hide it.
				if (!(thisApplet.style.visiblity && thisApplet.style.visiblity == "hidden")) {
					if (isOverlapping(thisApplet,thisAppletPos,menu,menuPos)) {
						thisApplet.style.visibility="hidden";
						hideElements.push(thisApplet);	
					}
				}
			}
	}
	
	// Apply computed x-y position to menu element
	menu.style.left = newMenuLeft + "px";
	menu.style.top = newMenuTop + "px";
	
	menu.style.visibility = "visible";
	
	if (useSlideAnimation)
		$(menu).hide().slideDown("fast");
}

// Finds the absolute position of the given element.
function findPos(obj) {
	var curleft = curtop = 0;
	if (obj.offsetParent) {
		curleft = obj.offsetLeft
		curtop = obj.offsetTop
		while (obj = obj.offsetParent) {
			curleft += obj.offsetLeft
			curtop += obj.offsetTop
		}
	}
	
	return [curleft,curtop];
}

// Determines if obj1 overlaps obj2.
function isOverlapping(obj1,obj1Pos,obj2,obj2Pos) {
	
	var xOverlap = false, yOverlap = false;

		var obj1Left = obj1Pos[0];
		var obj1Top = obj1Pos[1];
		
		var obj2Left = obj2Pos[0];
		var obj2Top = obj2Pos[1];
	
	// Test for x overlap
	if (((obj2Left >= obj1Left) && (obj2Left <= obj1Left+obj1.offsetWidth))
			|
			((obj2Left+obj2.offsetWidth >= obj1Left) && (obj2Left+obj2.offsetWidth <= obj1Left+obj1.offsetWidth))
			|
			(obj2Left <= obj1Left && obj2Left+obj2.offsetWidth > obj1Left)) {
			xOverlap=true;
	}
	
	// Test for y overlap
	if ((obj2Top >= obj1Top) && (obj2Top <= obj1Top+obj1.offsetHeight)
			|
			((obj2Top+obj2.offsetHeight >= obj1Top) && (obj1Top+obj2.offsetHeight <= obj1Top+obj1.offsetHeight))
			|
			(obj2Top <= obj1Top && obj2Top+obj2.offsetHeight > obj1Top)) {
			yOverlap=true;
	}
	
	return (xOverlap && yOverlap);
}

function restoreHiddenElements() {
	while (hideElements.length > 0) {
			
			var thisElement = hideElements.pop();
			
			// Restore visibility to element
			thisElement.style.visibility="visible";
		} 
}

function popupMouseOut(e) {
  
 window.clearTimeout(menuDisplayTimer);
  
  var curNode;
  var newNode;
  
	if (typeof(window.event) != 'undefined') {
		curNode = this;
		newNode = window.event.toElement;
  } else {
		curNode = e.currentTarget;
		newNode = e.relatedTarget;
	}

	if (curNode != newNode && !containsNode(curNode, newNode)) { 
	menuDisplayTimer = window.setTimeout("closePopupMenu()",350);
  } 
  
}

function popupMouseOver(e) {
  //alert("cleared menuDisplayTimer!"); 
  window.clearTimeout(menuDisplayTimer);
}

function parentMouseOut(e) {
  //alert("parentMouseOut!"); 
  //menuDisplayTimer = window.setTimeout("closePopupMenu(curMenu,e)",500); 
  
  var newNode;
  
	if (typeof(window.event) != 'undefined') {
		newNode = window.event.toElement;
  }else{
	newNode = e.relatedTarget;
  }

	if (newNode != curMenu && !containsNode(curMenu, newNode)) {
	menuDisplayTimer = window.setTimeout("closePopupMenu()",350);
  }
}

function containsNode(containerNode, testNode)
{
	if (testNode == null)
		return false;
	while (testNode.parentNode)
	{
		testNode = testNode.parentNode;
		if (testNode == containerNode)
			return true;
	}
	
	return false;
}

// Hides the currently-exposed menu.
function closePopupMenu() {
  if (curMenu != null)
	curMenu.style.display = "none";
	
  if (curButton != null && curButton.src)
	curButton.src = actionImage.src;
  
  curMenu = null;
  curButton = null;
  isMenuActive = false;
  
  restoreHiddenElements();
}

var currentPopupRow;
var currentFadeClass;

function glowPopupRow(obj, glowClass, fadeClass)
{
	// fade the previously-glowed row (if applicable)
	fadePopupRow(currentPopupRow, currentFadeClass);
	
	currentPopupRow = obj;
	currentFadeClass = fadeClass;
	
	currentPopupRow.className = glowClass;

	var linkObj = obj.getElementsByTagName("A")[0];
	if (linkObj != null)
		linkObj.className = linkObj.className + "_over";
}

function fadePopupRow(obj, fadeClass)
{   	
	if (obj != null)
	{
		obj.className = fadeClass;

		var linkObj = obj.getElementsByTagName("A")[0];
		if (linkObj != null)
			linkObj.className = linkObj.className.replace(/_over/, "");
	}
    }
  