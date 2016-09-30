ECS_PopupMenus Component
========================
Tom Marchant

This component serves 2 purposes:
-displaying pop-up menus, similar to those used in the standard Stellent
 interface
-displaying modal pop-up divs as a replacement to new browser windows, as 
 seen on many Web 2.0 applications

Version History
===============

Build 6 - 4th Aug 2008
----------------------
-Added support for 'top window' menu justification.

Version 1 - 12th Aug 2008
-------------------------
-Marked start of official versioning.
-Renamed from PopupMenus to ECS_PopupMenus
-Added support for 'iframe dodging' and hiding overlapping applets
-Added resources and example page for pop-up divs

Version 1.0.1 - 19th Aug 2008
-----------------------------
-Blanket layer for div pop-ups now takes up full length of page.

Version 1.0.2 - 3rd Sept 2008
-----------------------------
-Fixed compatibility issue between pop-up menus/divs.
-Fixed vertical positioning bug in IE
-<object> elements are now hidden when a pop-up div is displayed.

Version 1.0.3 - 8th Sept 2008
-----------------------------
-Select elements only hidden in IE 6 and below
-Fixed vertical positioning bug in IE

Version 1.0.4 - 18th Nov 2008
-----------------------------
-Added a name attribute to the pop-up iframe element to support
 Selenium test cases
 
Version 1.0.5 - 26th Nov 2008
-----------------------------
-Added switchPopup() function, allowing the contents/size of the
 pop-up to be changed without closing and re-opening it.

Version 1.1.0 - 21st April 2009
-------------------------------
-Overhaul of all pop-up div script, now uses jQuery selectors
 and animation functions. jQuery code included with component.
-Popup width/size no longer need to be specified, pop-ups will
 automatically resize based on content
 
Version 1.1.1 - 8th June 2009
-----------------------------
-Added popupOpen flag, a global JavaScript var which indicates
 whether a popup window is open or not.
-Added error handling if openPopup() is called without a request
 URL.

Version 1.1.2 - 12th Dec 2009
-----------------------------
-Added new env. var:
 ECS_PopupMenus_use_fade_animations
 This determines whether the background of popup divs is faded in/out
 (1 by default)

Version 1.1.3 - 21st Dec 2009
-----------------------------
- Added javascript function closePopupAndRedirect to resources file

Version 1.1.4 - 19th April 2009
-------------------------------
- Added env. var switch to enable JQuery slide animations when a popup
  menu is displayed: ECS_PopupMenus_use_menu_slide_animations. Off by
  default.

Version 1.1.5 - 8th Aug 2011
----------------------------
- Moved script/JS into external files to reduce page bloat  
  
Pop-up div functionality
=========================
Purpose
-------
Provides javascript/HTML/CSS includes for pop-up divs.

Use
---
The content of the pop-up div will be displayed in an iframe. So, your
pop-up content must be accessible through an URL.

Any pop-up content must be encaplsulated by a div with id=
"popUpContainingDiv", i.e.

<html>
	<body>
		<div id="popUpContainingDiv">
		
			<!-- your popup content goes here -->
		
		</div>
	</body>
</html>

This special div is required so IE can compute the height of all page
content correctly. It also makes it easier to give a consistent look
to all your application popups, by defining CSS relative to this div.

To invoke a modal pop-up on your page, ensure you have included the
CSS and javascript resources in the page header. Now add the following
javscript call to your link/button:

	openPopup(<URL>,[<width>])

The URL parameter should contain the URL you want displayed in the pop-up.
The optional width parameter defines the width of the pop-up frame. If width
isn't specified, a default width value is taken from the component env. file.

The popup will set its height automatically, based on the loaded content.

To close the popup, you can either use the 'Close this window' link
included in the top of the default pop-up div, or by invoking a method
on your pop-up content page.

Any close links in your pop-up content must call the following method:

	window.parent.closePopup()

Note: you cannot display more than one pop-up div at one time. If you need to
switch the popup (i.e. display a different popup via a link on a loaded popup),
simply call the function:

	window.parent.switchPopup(<URL>,[<width>])

To reset the size of the popup at any time (perhaps if you load extra content 
to the popup page via AJAX request) call the function: 

	resizePopup(<document>)

where <document> is the DOM document element of the current popup content.

Pop-up menu functionality
=========================

Purpose
-------
Provides javascript/HTML/CSS includes for pop-up menus. Effectively a
stripped-down version of the Stellent pop-up menu code found in:

	weblayout\common\cmu\common.js
	
Note: does not currently support nested/recursive menu structures.

Use
---
Place the include 'float_menu_header' in any templates where
you intend to use pop-up menus. This contains the required
javascript and CSS that allow the menus to function and 
display correctly.

The 'action_menu' include is used to display the popup menu and
it's parent element. The parent element can be an image, HTML
button or anchor. The popup menu is toggled by clicking the
parent element.

Before calling 'action_menu', you'll want to declare a few
variables in IDOC script to configure the menu to your needs..

Configuration
------------- 

IDOC variable							|	Use
-------------------------------------------------------------------------

menu_id										Value of the 'id' attribute for this menu.
													By default this is set to 'dID', ensuring that
													each menu will have a different ID when used
													in standard search results listings.
													Use this variable to make sure all your menus
													are discrete and do not interfere with each other
													
menu_img_src							A link to an image for use as the parent element. If
													you do not set this value, a default image is used
													(images/stellent/ActionsIcon.gif). You must also have
													an existing 'roll-over' image, with its name in the
													following form: '<menu_img_src>_over.gif'
										
menu_button_text					Blank by default. This is the text that will 
													appear on your parent element. Giving this variable
													a value means that the parent element will be
													an HTML anchor rather than an image.
									
menu_use_html_button			If menu_button_text and this variable have values,
													the parent element will be an HTML button, rather
													than an anchor. The menu_button_text determines the 
													buttons' value/label. Blank by default.
													
menu_button_attr					Any text you place here will be inserted inside the
													tags of the parent HTML element. Useful for defining
													custom styles/attributes for your parent element.
													Blank by default.

menu_justify							This determines where the menu will pop-up in relation
													to its parent element. By default, the menu appears
													on the right of the parent element, with its top side
													aligned with the top of the parent. The default position
													will adapt to the screen boundaries to ensure the entire
													menu is always visible.
													
													Possible values for 'menu_justify':								

													'bottom right' - menu displayed below the parent, with
	        								its right side vertically aligned with the right side of 
	        								the parent.
	        								
	        								'bottom left' - menu displayed below the parent, with its
	        								left side vertically aligned with the left side of the 
	        								parent.
	        								
	        								'top window' - menu displayed with right-hand side aligned
	        								with left-hand side of button, and top side aligned with
	        								the top of the current window.
	        								
menu_style_prefix					The menus use around 4 lengthy style class declarations
													to display correctly. These are outlined below:
													
													<prefix>PopupContainer: the div element that contains the
													pop-up menu. Use margin/padding attributes to fine-tune
													the menu's position.
													
													<prefix>PopupTable: the table element that contains the
													menu entries.
													
													<prefix>PopupLink: table cells containing menu entries
													
													<prefix>PopupLink_over: mouseover'd table cell containing
													menu entry
													
													By default, <prefix> is 'xui'. This corresponds to the
													standard Stellent pop-up menu styles. 
													
													You can easily create your own menu styles by copy-
													pasting the existing 'xui' styles. Replace 'xui' with your
													own custom prefix and change the styles as needed. Now
													set the 'menu_style_prefix' variable to your custom
													prefix.
													
																				
actionMenu_CustomInclude	Point this variable towards the name of your include
													containing your menu entries. See below for an example.


Defining your menu entries
--------------------------

Use the 'actionMenu_CustomInclude' to point towards a custom include containing
your menu entries. Each menu entry should be in the following form:

	<tr>
  	<td class="<$menu_style_prefix$>PopupLink" nowrap onMouseOver="glowPopupRow(this,'<$menu_style_prefix$>PopupLink_over','<$menu_style_prefix$>PopupLink')"	onMouseOut="fadePopupRow(this,'<$menu_style_prefix$>PopupLink')">
  			<a href="<$myLink$>"><$myCaption$></a>		
		</td>
	</tr>
	
Change 'myLink' to point towards an URL/javascript function which this menu entry
relates to.

Change 'myCaption' to a relevant piece of text that describes the menu action.

Example - basic Search and Filter document menu
-----------------------------------------------

If you want standard image-based pop-up menus to be available for every document
listed on a SearchAndFilter template, use the following code:

	<$actionMenu_CustomInclude="SearchAndFilter_menu"$>
	<$include action_menu$>

If you only need a DOC_INFO link in your pop-up menu, your custom include will
look like this:

	<@dynamichtml SearchAndFilter_menu@>
	
		<tr>
  		<td class="<$menu_style_prefix$>PopupLink" nowrap onMouseOver="glowPopupRow(this,'<$menu_style_prefix$>PopupLink_over','<$menu_style_prefix$>PopupLink')"	onMouseOut="fadePopupRow(this,'<$menu_style_prefix$>PopupLink')">
  			<a href="<$HttpCgiPath & '?IdcService=DOC_INFO&dID=' & dID$>">Standard info</a>		
			</td>
		</tr>
	
	<@end@>
