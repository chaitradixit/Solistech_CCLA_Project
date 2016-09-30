// Common functions used by the Document Approval page.
// -----------
//
// Contains various common functions, including AJAX based saving/loading
// of document metadata. 
//
// This JavaScript file relies on several variables being present.
// These will be instantiated by the include 'doc_approval_resolve_js_vars'

var fieldSplitter = "||*||";

var docInfoUrl = "?IdcService=IRIS_DOC_INFO_BASIC"+idcToken;
var docPanelUrl = "?IdcService=IRIS_BATCH_DOC_PANEL"+idcToken;
var batchUpdateUrl = "?IdcService=IRIS_BATCH_UPDATE"+idcToken;

// batch item index counts
var batchItemCount = 0;
var curBatchItem = 0;

// object used for storing name-value pairs
// as associative array.
var fieldValuePairs = new Object();

// Setting this var to the ID of a form element will cause it to focus
// next time the doc panel is loaded via AJAX. The var is reset after being
// focused.
var focusElementId;

// Register keypress event handling
$(document).ready( function() {
	$(document.body).keypress(function (e) {
		pageKeyPress(e);
	});
});

// KEYSTROKE HANDLING
// ============================================================================

// Triggered after a keyboard key is pressed. Use this
// method to bind shortcut keys.
function pageKeyPress(e) {

	var code;
	
	if (!e) 
		var e = window.event;
		
	if (e.keyCode) 
		code = e.keyCode;
	else if (e.which) 
		code = e.which;
	
	var keyStroke = String.fromCharCode(code);
	
	// Test for 2 key-combos by default:
	// Ctrl + <- : prev thumb
	// Ctrl + -> : next thumb 
	if (e.ctrlKey) {
		if (keyStroke == "[") {
			selPrevThumb();
		} else if (keyStroke == "]") {
			selNextThumb();
		}
	}
	
	// Hook to allow extra keypress handlers
	if (typeof pageKeyPressExtra != "undefined")
		pageKeyPressExtra(e);
}

// IRIS BUTTON HANDLING
// ============================================================================

// Initializes all custom Iris buttons. This includes adding mousedown/up event
// handlers.
// 
// If an elemId argument is passed, only buttons held within elements with a
// matching id value will be initialized.
function initButtons(elemId) {
	
	/*
	alert("initButtons");
	*/
	
	var scope = ".iris_button";
	
	if (elemId)
		scope = "#" + elemId + " " + scope;

	$(scope).bind("mousedown",function() {
		$(this).removeClass("iris_button_rest").addClass("iris_button_press");
	});
	
	$(scope).bind("mouseup mouseout",function() {
		$(this).removeClass("iris_button_press").addClass("iris_button_rest");
	});
									
	$(scope).addClass("iris_button_rest");
	
	/*
	$(scope).each( function() {
		alert($(this).attr("id"));
	});
	*/
}

// Enables/disables an custom Iris button. When disabled,
// an onclick event is added which simply returns a false value -
// this prevents the script/link tied to the button href being
// fired.
// When the button is re-enabled, this click event is removed.
function setButtonEnabled(buttonId, enable) {
	
	$("#" + buttonId).unbind("mousedown");
	$("#" + buttonId).unbind("click");
	
	if (enable) {
		$("#" + buttonId).removeClass("iris_button_disabled").addClass("iris_button_rest");
		
		$("#" + buttonId).fadeTo(0,1);
		
		$("#" + buttonId).bind("mousedown",function() {
			$(this).removeClass("iris_button_rest").addClass("iris_button_press");
		});
		
	}	else {
		$("#" + buttonId).addClass("iris_button_disabled").removeClass("iris_button_rest");
		
		$("#" + buttonId).fadeTo(0,0.6).bind("click", function() {
			return false;
		});
	}
}

// Retrieves document info for the given dDocName and displays it
// in the 'doc_fields' form. Does not perform a full reload of
// the form HTML.
function getDocInfo(docName) {
	
	setDocPanelEnabled(false);
	
	// update dDocName caption in panel header
	document.getElementById("doc_fields_docname").innerHTML = docName;
	
	$.get(docInfoUrl, { dDocName: docName }, function(data) {applyDocInfo(data)} );
}

// Used to update a thumbnail caption while in batch-edit mode.
function updateThumbCaption(docName, caption) {
	if (window.frames.thumbsFrame) {
		window.frames.thumbsFrame.updateThumbCaption(docName, caption);
	}
}

// Used to navigate to the previous thumbnail/bundle item.
//
// This function will be called without the 'saved' argument
// initially. This invokes an autosave operation: if this completes
// successfully (i.e. no validation/save errors), the function
// is called again with the 'saved' argument set to 'true', which
// will actually change the selected thumbnail.
function selPrevThumb(saved) {
	
	if (curBatchItem > 1) {
		if (saved)
			window.frames.thumbsFrame.selPrevThumb();
		else
			autosaveCurrentItem(function() {
				selPrevThumb(true); 
			});
	}
}

// Used to navigate to the next thumbnail/bundle item.
function selNextThumb(saved) {
	
	if (curBatchItem < batchItemCount) {
		if (saved)
			window.frames.thumbsFrame.selNextThumb();
		else
			autosaveCurrentItem(function() {
				selNextThumb(true); 
			});
	}
}

// Loads a specific bundle item by dDocName.
function selThumbByDocName(docName) {
	window.frames.thumbsFrame.selectThumbByDocName(docName);
}

// Used in batch document edit mode. An index counter displays the current
// batch item index, e.g. '2 of 6'. This method is used to update the
// counter display and enable/disable browse buttons.
function updateBatchIndexLabel(curIndex,totalItems) {
	
	batchItemCount = totalItems;
	curBatchItem = curIndex;
	
	if (totalItems > 0) {
		$("#batchItemIndexPanel").show();
		$("#batchItemIndexLabel").html(curBatchItem + " of " + batchItemCount);
		
		// Fetch prev/next clickable div elements
		var prevBtn = $("#batchItemIndex_prev")[0];
		var nextBtn = $("#batchItemIndex_next")[0];
		
		// clear all mouse events
		$(prevBtn).unbind('mouseenter mouseleave');
		$(nextBtn).unbind('mouseenter mouseleave');
		
		if (curIndex == 1) {
			$(prevBtn).removeClass("pn_icon_rest").removeClass("pn_icon_hover").addClass("pn_icon_inactive");
		} else {
			$(prevBtn).removeClass("pn_icon_inactive").addClass("pn_icon_rest");
			
			$(prevBtn).hover(
				function() {add_browse_hilite(this);},
				function() {rem_browse_hilite(this);}
			);
		}
		
		if (curIndex == totalItems) {
			$(nextBtn).removeClass("pn_icon_rest").removeClass("pn_icon_hover").addClass("pn_icon_inactive");
			
		} else {
			$(nextBtn).removeClass("pn_icon_inactive").addClass("pn_icon_rest");

			$(nextBtn).hover(
				function() {add_browse_hilite(this);},
				function() {rem_browse_hilite(this);}
			);
		}
	}
}

// Called when paging through thumbnails. First checks if
// there are any unsaved changes. If so, the saveDocInfo()
// call is invoked. 
//
// The callbackFn argument is optional and should correspond 
// to a function. This function will be passed to the 
// saveDocInfo() call and only executed if the update service
// completes without error.
function autosaveCurrentItem(callbackFn) {
	
	if (checkDocFieldsForChanges()) {
		// Changes detected on current item.
		if (callbackFn)
			saveDocInfo("", callbackFn);
		else
			saveDocInfo("");
	} else {
		// No changes detected, call function immediately.
		if (callbackFn)
			callbackFn();
	}
		
}

/* stores the last time the getDocPanel() was called */
var lastLoadTime;

/* stores the docname of the last doc that was loaded in getDocPanel() */
var lastLoadedDocName;

// Reloads the batch item details panel with fields/data
// corresponding to the given dDocName.
// 
// The attribs argument is an optional string containing
// extra URL attributes you wish to submit when fetching 
// the form.
function getDocPanel(docName,attribs) {
	
	if (!docName || docName.length == 0) {
		// don't fetch the doc panel if no dDocName was specified.
		return;
	}
	
	// Check if method was called in the last 500 millis and for this document, if so return.
	if (lastLoadedDocName && (docName == lastLoadedDocName)) {
		var date2 = new Date();
		if (date2.getTime() - lastLoadTime.getTime() < 500) {
			// Same doc loaded too quickly! Return immediately to
			// prevent useless load.
			return;
		}
	}
	
	lastLoadTime = new Date();
	lastLoadedDocName = docName;
	
	if (typeof doPreFetchDocPanelActions != "undefined")
		doPreFetchDocPanelActions();
	
	setDocPanelEnabled(false);
	var ts = new Date().getTime(); // forces server request in IE
	
	var infoFlag = "1";
	
	if (!isInfo)
		infoFlag = "";
	
	var thisUrl = docPanelUrl;
	if (attribs)
		thisUrl += attribs;
	
	if (bundleRef != '') {
		thisUrl += "&isBatchDoc=1";
	}

	$.get(thisUrl, { 
					dDocName: docName,
					profileName: profileName,
					timestamp: ts,
					isInfo: infoFlag,
					isWorkflowItem: isWorkflowItem
				}, 
				function(data) {setDocPanel(data);} 
	);
}


// Callback function after fetching new panel HTML using getDocPanel().
// Replaces the inner HTML inside the doc panel container div.
function setDocPanel(data) {
	
	$("#doc_meta_panel_container").html(data);
	
	// evaluate any script tags contained in the document fields panel
	evalScriptTags("doc_meta_panel_container");
	
	if (!isInfo) {
		// init calendars
		prepareCalendars();
		
		if (focusElementId) {
			// Apply focus to specific element by ID (no delay)
			focusDocPanelIndexingElement(false, focusElementId);
			focusElementId = "";
			
		} else if (isSaved) {
			// Item was just saved, highlight the save button.
			window.focus();
			/*
			var submitBtn = document.getElementsByName("submitbtn")[0];
			
			if (submitBtn) {
				window.setTimeout(function() {submitBtn.focus(); }, 300);
			}
			*/
			
			// Apply focus to Save button
			focusDocPanelIndexingElement(true, "submitbtn");
			
		} else {
			// Apply focus to first input element
			focusDocPanelIndexingElement(true);
			
			/*
			// Fetch the first user-editable form element from the panel.
			var firstFormElem = 
			 $("#doc_fields").find("input:not([type=hidden]),select,textarea")[0];
			
			//var firstFormElem = $("#xDocumentClass")[0];
			
			// if the element is found, focus it for input.
			if (firstFormElem) {
				//alert("Focusing first form element: " + firstFormElem.innerHTML);
				window.setTimeout(function() {firstFormElem.focus(); }, 300);
			}
			*/
		}
	}
	
	if (typeof doPostFetchDocPanelActions != "undefined")	
		doPostFetchDocPanelActions();
}

// Focuses the given doc panel element, using the passed parameter as an ID lookup.
// If the passed parameter is blank/undefined, the first input/select element is
// focused instead.
//
// If the delay parameter is set, the element will be focused after a few hundred
// milliseconds delay - this is due to the annoying PDF container, which steals the
// browser focus after it finishes loading. If the focus request isn't after a load
// or reload of the content, theres no need for the delay.
function focusDocPanelIndexingElement(delay, elemId) {
	
	var elementToFocus;
	
	if (elemId) {
		// Fetch the form element by its ID
		elementToFocus = $("#" + elemId);
	} else {
		// Fetch the first user-editable form element from the panel.
		elementToFocus = 
		 $("#doc_fields").find("input:not([type=hidden]),select,textarea").eq(0);
	}
	
	//console.log("focusDocPanelIndexingElement: " 
	// + delay + ", " + elemId + ", " + $(elementToFocus).attr("id"));
	
	// if the element is found, focus it for input.
	if (elementToFocus) {
		//alert("Focusing first form element: " + firstFormElem.innerHTML);
		
		if (delay)
			window.setTimeout(function() {$(elementToFocus).focus(); }, 300);
		else
			$(elementToFocus).focus();
	}
}

// Callback function tied to AJAX request for doc info (see getDocInfo() function)
// Responsible for parsing the returned doc metadata and displaying it to the UI.
function applyDocInfo(data) {
	
	var form = document.getElementById("doc_fields");
	
	// Split apart all the name-value pairs
	var fields = data.split(fieldSplitter);
	
	for (var i=0; i<fields.length; i++) {
		var thisPair = fields[i].split("=");
		
		var fieldName = thisPair[0];
		var fieldValue = thisPair[1];
		
		if (form.elements[fieldName])
			form.elements[fieldName].value = fieldValue;
	}
	
	setDocPanelEnabled(true);
	showMsg("");
}

// Saves metadata changes to the document via AJAX call.
//
// customAttribs argument is optional and allows custom
// field-value pairs to be appended to the UPDATE_DOCINFO
// submission. This argument value will be appended directly
// to the request URL, so ensure that it is in the form
// 'field1=value1&field2=value2...'
//
// callbackFn argument is optional and should correspond to
// a function. This will be passed to the saveDocInfoCompleted()
// function and only executed if it completes successfully. 
function saveDocInfo(customAttribs, callbackFn)
{
	if (!validateDocFields()) {
		showMsg("Errors during validation");
		return false;
	}
	
	setDocPanelEnabled(false);
	showMsg('Saving changes..');
	
	var form 		= document.doc_fields;
	var attribs = new Object();
	
	// extract all field-value pairs from the doc_fields form,
	// which contains all document metadata fields.
	for (var i=0; i<form.elements.length; i++) {
		// Add name-value pairs to attribs object
		attribs[form.elements[i].name] = form.elements[i].value;
	}
	
	// Add IsJava=1 to attribs to prevent rendering of IDOCScript in
	// the AJAX response.
	attribs["IsJava"] = "1";
	
	var reqString = "?";
	if (customAttribs)
		reqString += customAttribs;
	
	// prepare call-back function
	var fn;
	
	if (callbackFn)
		fn = function(data) { saveDocInfoCompleted(data, callbackFn); };
	else
		fn = function(data) { saveDocInfoCompleted(data); };
		
	$.post(reqString, attribs, fn);
}

// Reload currently-displayed doc panel, discarding any unsaved
// changes
function reloadDocInfo() {
	
	var form 		= document.doc_fields;
	
	if (form) {
		showMsg("Reloading...");
		getDocPanel(form.elements["dDocName"].value,"&reloaded=1");
	}
}

// Callback function after AJAX doc info update completes
function saveDocInfoCompleted(data, callbackFn) {
	showMsg("");

	var localData 	= getLocalDataFromResponse(data);
	var statusCode  = localData["StatusCode"];
	
	if (statusCode && statusCode <= -1) {
		// something went wrong during UPDATE_DOCINFO call. Display
		// an error message in the message cell.
		
		// Check if this is a DB error first. These generate very long-winded error
		// messages. Try and extract the useful part when this is the case.
		var errorMsgStr = localData["StatusMessage"];
		 
		if (errorMsgStr.indexOf("Unable to execute query") > -1 
			&&
			errorMsgStr.indexOf("ORA-") > -1) {
			errorMsgStr = errorMsgStr.substring(errorMsgStr.indexOf("ORA-"));
		}
		
		// Replace all carriage returns with breaks
		errorMsgStr = errorMsgStr.replace(/\\n/gi, "<br/>");
		
		var errorMsg = createErrorMsg("saveError","Error saving data", errorMsgStr);		
		document.getElementById("statusText").appendChild(errorMsg);

		setDocPanelEnabled(true);
	} else {
		isSaved = true;
		
		var form 		= document.doc_fields;
		
		if (callbackFn) {
			// Execute custom callback function
			callbackFn();
		} else {
			// Execute standard callback (reloading the doc meta panel)
			getDocPanel(form.elements["dDocName"].value,"&saved=1");
		}
	}
}

// Updates a subset of selected values across
// all items in the current bundle.
function applyBatchUpdate() {
	
	showMsg("Applying batch update...");
	setDocPanelEnabled(false);
	
	// fetch all field checkboxes		
	var checks = getBatchUpdateCheckboxes();
	var checkSuffix = "_checkbox";
	
	var fieldsToUpdate 			= "";
	var fieldValPairs    	= "";
	
	for (var i=0; i<checks.length; i++) {
		if (checks[i].checked) {
			
			// extract field name from checkbox name.
			// checkbox name is in form: '<fieldname>_checkbox'
			var thisFieldName = 
			 checks[i].name.substring(0,checks[i].name.length - checkSuffix.length);
			
			if (fieldsToUpdate.length > 0)
				fieldsToUpdate += ",";
			
			fieldsToUpdate += thisFieldName;
			
			// append '&fieldName=fieldValue', encoding the field value
			// to ensure it can be submitted correctly.
			fieldValPairs += "&" + thisFieldName + "=" + 
			 encodeURIComponent(document.doc_fields[thisFieldName].value); 
		}
	}
	
	var ts = new Date().getTime(); // forces server request in IE
	
	$.post(batchUpdateUrl + fieldValPairs, { 
			bundleRef: bundleRef,
			fieldsToUpdate: fieldsToUpdate,
			IsJava: "1",
			timestamp: ts
		}, 
		function(data) {batchUpdateCompleted(data);}
	); 

}

// Callback function after AJAX doc info update completes
function batchUpdateCompleted(data) {
	var localData = getLocalDataFromResponse(data);
	
	if (localData["batchUpdateCount"]) {
	
		var batchUpdateCount = localData["batchUpdateCount"];
		showMsg(batchUpdateCount + " items updated.");
	
	} else {
		showMsg("Error during batch update");
	}
	
	setDocPanelEnabled(true);
}

// Set the enabled state of all form fields/controls
// on the document fields panel
function setDocPanelEnabled(enable) {
	
	var formFields = 
	 $("#doc_fields").find("input, select, textarea");
	
	for (var i=0; i<formFields.length; i++) {
		formFields[i].disabled = !enable;
	}
}

// Called on change event for batch update checkbox elements
function batchUpdateCheckboxChanged(chk) {
	
	// fetch all field checkboxes		
	var checks = getBatchUpdateCheckboxes();
	var foundChecked = false;
	
	// work out if at least 1 checkbox has been checked
	for (var i=0; i<checks.length; i++) {
		if (checks[i].checked)
			foundChecked = true;
	}
	
	// set disabled state of batch update button
	document.doc_fields.batchUpdateBtn.disabled = !foundChecked;
}

// Retrieves all checkboxes used for flagging fields
// for batch updates.
function getBatchUpdateCheckboxes() {
	return $("#doc_fields .doc_meta_checkbox");
}

// Used to fetch and store all loaded document field values. 
// This is used to check for any unsaved changes to the fields.
function collectDocFieldValues(fieldList) {

	// Add any standard field values first, if they exist.
	var stdFieldList = "dDocAuthor,dDocAccount,dDocTitle";
	var stdFields = stdFieldList.split(",");

	for (var i=0; i<stdFields.length; i++) {
		var thisField = stdFields[i];
		
		// attempt to get the field input element
		var fieldElement = document.doc_fields[thisField];
		
		if (fieldElement) {
			// form element found, add new field-value pair
			fieldValuePairs[thisField] = fieldElement.value;
		}
	}
	
	// Now add custom field values
	var fields = fieldList.split(",");
	
	for (var i=0; i<fields.length; i++) {
		var thisField = fields[i];
		
		if (thisField != "div") {
			// attempt to get the field input element
			var fieldElement = document.doc_fields[thisField];
			
			if (fieldElement) {
				// form element found, add new field-value pair
				fieldValuePairs[thisField] = fieldElement.value;
			}
		}
	}
}

// loops through fieldValuePairs objects, checking
// current field values against stored ones.
function checkDocFieldsForChanges() {
		
	for (var field in fieldValuePairs) {
		
		if(document.doc_fields[field]) {
			var newValue = document.doc_fields[field].value;
		
			if (newValue != fieldValuePairs[field])
				return true;
		}
	}

	return false;
}

// -----------------------------
// Content Item locking
// -----------------------------

var itemLockUrl = "?IdcService=IRIS_ACQUIRE_ITEM_LOCK"+idcToken;
var removeItemLockUrl = "?IdcService=IRIS_REMOVE_ITEM_LOCK"+idcToken;

// Acquires a lock on the given document for the
// current user.
function getItemLock(docName) {
	
	var ts = new Date().getTime(); // forces server request in IE
	
	$.get(itemLockUrl, {
			dDocName: docName,
			IsJava: 1,
			ts: ts
		}, function(data) {getItemLockCompleted(data, docName);});
}

// Resolves the dDocName and passes to function below.
// Hard-coded to work for bundle locking only at the moment.
function removeItemLockOnExit() {
	
	var lockUser 	= "";
	var docName		= "";
	
	var bundleForm = document.forms['bundle_fields'];
	if (bundleForm) {
		lockUser 	= bundleForm.bundleLockUser.value;
		docName 	= bundleForm.bundleDocName.value;
	} else {
		var itemLockForm = document.forms['item_lock_form'];
		
		if (itemLockForm) {
			lockUser = itemLockForm.itemLockUser.value;
			docName = itemLockForm.itemDocName.value;
		}
	}
	
	if (lockUser != "") {
		
		// Only perform the unlock if the current user has the lock
		if (lockUser && (lockUser == userName)) {
		
			var ts = new Date().getTime(); // forces server request in IE
			
			// call ajax function here without UI callback. Notice that
			// the lockUserMustMatch flag is also passed, this prevents
			// another user's lock being removed via the service call.
			$.get(removeItemLockUrl, {
				dDocName: docName,
				lockUserMustMatch: 1,
				IsJava: 1,
				ts: ts
			});
		}
	}
}

// Removes a lock on the given document 
function removeItemLock(docName) {
	
	var ts = new Date().getTime(); // forces server request in IE
	
	$.get(removeItemLockUrl, {
			dDocName: docName,
			IsJava: 1,
			ts: ts
		}, function(data) {removeItemLockCompleted(data, docName);});
}

function getItemLockCompleted(data, docName) {
	var localData = getLocalDataFromResponse(data);
	
	if (localData['itemLockAcquired']) {
		// item locked successfully. Update lock panel text
		
		$("#iris_batch_lock_panel").removeClass("item_unlocked").addClass("item_locked");
		
		$("#iris_batch_lock_info").html("You have acquired the lock.");
		
		$("#iris_batch_lock_link").html("Remove lock");
		$("#iris_batch_lock_link").attr("href","javascript:removeItemLock('" + docName + "');");
		
		isInfo = false;
		
		// Enable the workflow panel, providing the user has approval rights.
		if (allowWorkflowActions)
			setWorkflowPanelEnabled(true);
			
	} else {
		// assume error message.
		alert(localData['StatusMessage']);
	}
	
	reloadDocInfo();
}

function removeItemLockCompleted(data, docName) {
	
	var localData = getLocalDataFromResponse(data);
	
	if (localData['itemLockRemoved']) {
		// item unlocked successfully. Update lock panel text
		
		$("#iris_batch_lock_panel").addClass("item_unlocked").removeClass("item_locked");

		$("#iris_batch_lock_info").html("You have removed the lock.");
		
		$("#iris_batch_lock_link").html("Acquire lock");
		$("#iris_batch_lock_link").attr("href","javascript:getItemLock('" + docName + "');");
		
		// Disable the workflow panel, providing the user previously had approval rights.
		if (allowWorkflowActions)
			setWorkflowPanelEnabled(false);
		
		isInfo = true;
	}
	
	reloadDocInfo();
}

function updateMissingFieldCount(numMissing) {
	
	if (bundleRef == "") {
		if (numMissing == 0) {
			$("#missing_field_count").html("");
			
			// Disable the workflow panel, providing the user has approval rights.
			if (allowWorkflowActions)
				setWorkflowPanelEnabled(true);
		} else {
			$("#missing_field_count").html(numMissing + " required field value(s) are missing.");
			
			// Disable the workflow panel, providing the user has approval rights.
			if (allowWorkflowActions)
				setWorkflowPanelEnabled(false);
		}
	}
}

// -----------------------------
// General AJAX helper functions
// -----------------------------

// Called after inserting new HTML content via AJAX call. Any
// new script addded to the page will not be executable yet - this
// function evaluates the content of the new tags to get round
// the problem.
function evalScriptTags(containerId) {
	
	// select all newly-inserted script tags
	var scriptTags = $("#" + containerId + " script");
	
	// evaluate the content of each script tag, i.e. execute
	// the JavaScript contained inside
	for (var i=0; i<scriptTags.length; i++) {
		//alert("evaluating script " + i + "\n\n" + scriptTags[i].innerHTML);
		eval(scriptTags[i].innerHTML);
	}
}

// For a given server reponse text, this method extracts
// all name-value pairs from the LocalData properties and
// returns them as an Object. 
function getLocalDataFromResponse(data) {
	
	var localData = data.split("\n"); 
	var inLocalData = false;
	
	var localDataAttribs = new Object();
	
	for (var i=0; i<localData.length; i++) {
		
		if (inLocalData & localData[i] == "@end") {
			inLocalData = false;
			break;
		}
		
		if (inLocalData) {
			// Now we are inside the LocalData properties - collect
			// the name-value pairs.
			var eqPosition = localData[i].indexOf("=");
			
			if (eqPosition > -1) {
				var thisVarName = localData[i].substring(0, eqPosition);
				var thisVarValue = localData[i].substring(eqPosition+1);

				localDataAttribs[thisVarName] = thisVarValue;
			}
		}
		
		if (!inLocalData & localData[i] == "@Properties LocalData")
			inLocalData = true;
	}
	
	return localDataAttribs;
}

// Checks the LocalData section of the passed JSON data instance.
// If the StatusCode value is -1, return the StatusMessage. Otherwise
// return false
function getJsonErrorMsg(jsonData) {

	var statusCode = jsonData.LocalData.StatusCode;
	
	if (typeof(statusCode) != 'undefined') {
		var statusCodeInt = parseInt(statusCode);

		if (statusCodeInt < 0)
			return jsonData.LocalData.StatusMessage;
	} else
		return false;
}

// Fetches a field value from the passed JSON ResultSet instance.
// rowIndex is optional, if not specified, the first row value
// is taken.
function getJsonResultSetValue(jsonResultSet, fieldName, rowIndex) {
	
	var fieldIndex = -1;
	
	if (typeof(rowIndex) == 'undefined')
		rowIndex = 0;
	
	if (jsonResultSet.rows.length < rowIndex)
		return false;
	
	for (var i=0; i<jsonResultSet.fields.length; i++) {
		if (jsonResultSet.fields[i].name == fieldName)
			fieldIndex = i;
	}

	if (fieldIndex == -1)
		return false;
	
	return jsonResultSet.rows[rowIndex][fieldIndex];
}

function getJsonResultSetNumRows(jsonResultSet) {
	
	return jsonResultSet.rows.length;
}

// All functions below were originally hard-scripted into the doc_approval_js
// include.
// ==========================================================================

// Currently-selected tab
var curSel = 'tab_image';

// Sets the content of the status text shown on the workflow panel.
// Pass in an empty string to remove any existing text.
function setWorkflowStatusText(htmlContent) {
	if (htmlContent == "")
		htmlContent = "&nbsp;";
		
	$("#wfStatusText").html(htmlContent);
}

// Highlights the first editable field when the page loads
function highlightFirstField()
{

	if (navigator.appName == 'Netscape') {
		var iframes = document.getElementsByTagName("iframe");
		
		for (var ifr=0; ifr<iframes.length; ifr++) {
			if (iframes[ifr].id == 'contentFrame') {
				iframe.blur();
				break;
			}
		}
	}

	window.focus();
	
	// Focus first input text/select element on doc meta panel.
	var firstField = $("#doc_fields").find("input[type='text'], select")[0];
	if (firstField)
		firstField.focus();
}

// Checks if any metadata field values have been changed,
// but not saved. If there are changes, prompt the user
// to save them
function checkForChanges()
{
	if (isInfo)
		return false;
	
	return checkDocFieldsForChanges();
}

// Hooked into the onbeforeunload event for the doc approval page.
// Notifies the user if they have unsaved changes before they
// navigate away.
function checkForChangesOnExit()
{
	if (!isSaved) {
		if (checkForChanges()) {
			return "You have unsaved changes to this item. Clicking OK will discard your changes.";
			
				
			if (!conf)
				return false;
		}
	}
}

// Peforms approve/reject workflow action. 
//
// The 'action' argument can be either 'Approve' or 'Reject'.
// A workflow comment must be present if the user chooses to
// reject the item.
//
// This method runs various validation checks before finally
// submitting the workflow form.
function wfAction(action)
{
	setWorkflowPanelEnabled(false);
	
	if (checkChangesOnExit) {
		changes = checkForChanges();
	
		// ensure there are no recent changes
		if (changes == true) {
			conf = confirm("You have unsaved changes to this item. Continue without saving?");
			if (!conf) {
				setWorkflowPanelEnabled(true);
				return;
			} else {
				isSaved = true;
			}
		}
	}
	
	// Checks for a comment when rejecting
	if (!checkForWfComment(action)) {
		setWorkflowPanelEnabled(true);
		return;
	}
	
	// If validateWfAction() returns true, it means that an AJAX validation
	// request is pending. The callback function will handle submission of
	// the workflow form.
	if (validateWfAction(action))
		return;
	
	prepareSubmitWfAction(action);
}

// Submits the workflow form. It is assumed all validation is completed
// at this point.
//
// The form service parameter may change based on the passed action value.
function submitWfAction(action) {

	setWorkflowStatusText("Submitting...");
		
	var wfForm = document.getElementById("approve_reject_form");
	
	wfForm = prepareWfFormForSubmission(wfForm, action);

	wfForm.submit();
}

// Returns the workflow form for manually submission purposes. It is assumed 
// all validation is completed at this point.
//
// The form service parameter may change based on the passed action value.
function submitWfActionGetForm(action) {
	var wfForm = document.getElementById("approve_reject_form");
	
	wfForm = prepareWfFormForSubmission(wfForm, action);

	return wfForm;
}

function prepareWfFormForSubmission(wfForm, action){
	var comment = "";
	
	if (wfForm.elements['wfComments']) {
		comment = wfForm.elements['wfComments'].value;
	}
	
	wfForm.elements['lMessage'].value = comment;

	if (action == 'Approve') {
		wfForm.elements['lAction'].value = "APPROVE";
		
		if (useAsyncApprovals)
			wfForm.elements['IdcService'].value = "WF_APPROVE_AND_AUDIT_ASYNC";
		else
			wfForm.elements['IdcService'].value = approveService;

		wfForm.elements['wfApproveMessage'].value = comment;
	} else {
		wfForm.elements['lAction'].value = "REJECT";
		
		if (useAsyncApprovals)
			wfForm.elements['IdcService'].value = "WF_REJECT_AND_AUDIT_ASYNC";
		else
			wfForm.elements['IdcService'].value = "IRIS_WF_REJECT";
		
		wfForm.elements['wfRejectMessage'].value = comment;
	}
	
	return wfForm;
}

// Performs custom validation check before executing workflow action,
// if the profile definition has a workflow validation class set.
// 
// Returns true if a validation class was found and the page is pending
// on an AJAX response, false otherwise.
function validateWfAction(action) {
	
	if (validationClass != '') {
		setWorkflowStatusText("Validating...");
		
		var ts = new Date().getTime(); // forces server request in IE
		
		$.get(validationUrl,
					{action: action,
					 ts: ts,
					 IsJava: "1"},
					function(data) { validationWfActionCompleted(data, action); });
					
		// AJAX request pending, return true			
		return true;
	}
	
	return false;
}

// Callback function for workflow action validation request. This
// method checks the returned data to see if it inidicates there
// were validation errors.
function validationWfActionCompleted(data, action) {
	var localData = getLocalDataFromResponse(data);
	
	setWorkflowStatusText("&nbsp;");
	
	if (localData['validationFailed']) {
		// validation checks have failed. open a pop-up window
		// giving detailed validation report
		var reqUrl = validationUrl + "&action=" + action;
		openPopup(reqUrl,500);
		
		setWorkflowPanelEnabled(true);
		
	} else {
		// assume validation checks have passed.
		prepareSubmitWfAction(action);
	}
}

// Checks the contents of the workflow comment
function checkForWfComment(action)
{
	wfComment = document.getElementById("wfComments");
	
	if (wfComment && wfComment.value == '') {

		if (action == 'Reject') {
			// Warn the user if they are rejecting a document without leaving
			// a comment
			alert('You must leave a workflow comment when rejecting an item.');

			wfComment.focus();
			return false;
		}
	}

	return true;
}

// Used to toggle the enabled state of workflow form
// elements.
function setWorkflowPanelEnabled(enable) {
	var form = document.getElementById("approve_reject_form");
	
	if (form) {
		// approve/reject buttons
		form.elements['approve_btn'].disabled = !enable;
		if (form.elements['reject_btn'])
			form.elements['reject_btn'].disabled = !enable;
			
		// workflow comment text box
		if (form.elements['wfComments'])
			form.elements['wfComments'].disabled = !enable;
	}
}

// Shows/hides the contents of a document indexing panel.
function togglePanel(linkObj) {

	var panel = document.getElementById((linkObj.id).replace('toggle_',''));
	var img = linkObj;

	if (panel.style.display == 'none') {

		panel.style.display = 'block';
		img.className = 'toggle_img_collapse';

	} else {

		panel.style.display = 'none';
		img.className = 'toggle_img_expand';

	}
}

// Shows/hides the contents of a document indexing panel.
function togglePanelGoToLink(linkObj, anchor) {

	var panel = document.getElementById((linkObj.id).replace('toggle_',''));
	var img = linkObj;

	if (panel.style.display == 'none') {

		panel.style.display = 'block';
		img.className = 'toggle_img_collapse';

	} else {

		panel.style.display = 'none';
		img.className = 'toggle_img_expand';

	}
	
	window.location.hash=anchor;
}	

// Used when submitting non-workflow comments. Checks for the
// presence of a comment before submitting it
function checkForComment()
{
	if (document.getElementById("commentbox").value == "") {
		alert("You must enter a comment in the textbox.");
	} else {
		document.getElementById("commentform").submit();
	}
}

// Displays the given message on the document field panel.
function showMsg(msg)
{

	if (msg.length == 0)
		msg = "&nbsp;";
	
	if (document.getElementById('statusText'))
		document.getElementById('statusText').innerHTML=msg;
}

var curTab = "tab_image";

// Called after clicking a tab. The table cell containing the tab must
// appear 'selected' while the previous tab must revert to an 'unselected' state
function openTab(sel)
{
	// Context frame - the contents change depending on which tab is clicked
	contextFrame = frames['contextFrame'];

	switchTabs(curTab,sel);

	curTab = sel;

	var recentNotes = document.getElementById('recentnotes');
	var iFrameTable = document.getElementById('previewtable');
	var recentNotesHeader = document.getElementById('recentnotes_span');

	// now change the source of the context iframe
	if (sel == 'tab_notes') {

		// display notes for this record. The Recent notes table below the iframe must be
		// hidden so we do not show redundant information
		recentNotes.style.display = "none";
		iFrameTable.style.height = "94%";
		recentNotesHeader.style.display = "inline";

		// Switch contents of iframe
		contextFrame.location.href = 
		 httpCgiPath + '?IdcService=IRIS_LINKED_AUDIT_ENTRIES&dDocName=' + approvalDocName +idcToken+ 
		 '&ts=' + new Date().getTime();

	} else {
		// Recent notes area must be restored
		recentNotes.style.display = "block";
		iFrameTable.style.height = "85%";
		recentNotesHeader.style.display = "inline";

	}

	if (sel == 'tab_image') {

		// display the scanned image (content item)
		contextFrame.location.href = getContentUrl();

		setHeights();
	} else if (sel == 'tab_docs') {

		// display related documents
		contextFrame.location.href = httpCgiPath + 
		 '?IdcService=IRIS_LINKED_DOCS&dDocName=' + approvalDocName+idcToken;

		setHeights();
	}
	
	contextFrame.src = contextFrame.location.href;
	
	// set the URL hash to the current tab name.
	// i.e. 'tab_docs' will be converted to the hash: #_docs.
	document.location.hash = sel.substr(3);
}

function switchTabs(oldTabID,newTabID) {

	oldTab = document.getElementById(oldTabID);
	newTab = document.getElementById(newTabID);

	oldTab.className="tabOff";
	newTab.className="tabOn";
}

var isSaved = false;

// Saves metadata changes to the document
function saveChanges()
{
	if (!validateDocFields())
		return false;

	isSaved=true;
	
	if (thumbnailMode && batchEditMode == 'document') {
		// batch document editing in use. Save via AJAX.
		// TODO: this doesn't work, currently.
		saveDocInfo();
		
	} else {
		// standard form submission
		document.doc_fields.submitbtn.disabled=true;
		
		showMsg('Saving changes..');

		if (passStatusInUrl) {
			if (document.forms["doc_fields"].elements[docStatusField]) {
				document.doc_fields.RedirectUrl.value += "&prevStatus="
				 + document.forms["doc_fields"].elements[docStatusField].value 
				 + "&prevAction=update";
			}
		}

		document.doc_fields.submit();
	}
}

// Reloads the page to restore saved data.
function discardChanges()
{
	isSaved=true;
	
	document.doc_fields.submitbtn.disabled=true;
	showMsg("Reloading item data..");
	
	window.location.reload(true);
}

function adjustNumDocs(num)
{
	numDocs = numDocs + num;
	document.getElementById('tab_docs_caption').innerHTML = 'Associated Docs(' + numDocs + ')';
}

function adjustNumNotes(num)
{
	numNotes = numNotes + num;
	document.getElementById('tab_notes_caption').innerHTML = 'Notes/Activity(' + numNotes + ')';
}

// Sets the percentage heights of the preview and panel tables
function setHeights()
{

	var mainTable = document.getElementById('preview_container');
	if (!mainTable)
		return;
	
	var mainDiv = document.getElementById('preview_div');

	var tabHeight = 30;

	var header = document.getElementById('irisheader');
	
	if (fixedPreviewHeight != '') {
		fixedHeight = fixedPreviewHeight;
	} else {
		fixedHeight = document.body.clientHeight - header.clientHeight - 56 - tabHeight;
	}

	// Adjust the height of the preview/panels div to a constant,
	// allowing it to occupy the entire screen
	mainTable.style.height=fixedHeight;

	mainDiv.style.height=fixedHeight+tabHeight;

	// Check the user's resolution: lower resolutions only have
	// space to show 1 most recent note rather than 3

	scrWidth = screen.width;

	if (hasRecentNotes) {

		recentNotes = document.getElementById('recentnotes');

		if (navigator.appName == 'Netscape') {
			divHeight = 1.65;
		} else {
			divHeight = 2.05;
		}

		if (scrWidth >= 1280) {
			divHeight = divHeight*3
		} else if (scrWidth >= 1024) {
			divHeight = divHeight*2
		}

		recentNotes.style.height = divHeight + "em";
	}

	iframe 			= document.getElementById('contextFrame');
	notesRegion 	= document.getElementById('recentnotes_region');
	previewTable 	= document.getElementById('previewtable');
	
	if (fixedPreviewHeight != '') {
		previewTable.style.height = (fixedPreviewHeight - 100) + "px";
	} else {
		previewTable.style.height = (mainDiv.offsetHeight - notesRegion.offsetHeight - 35) + "px";
	}
	
	if (curTab == 'tab_image') {
		// Load the image last, so the plugin appears the correct size
		if (!thumbnailMode) {
			// TM: this load is uneccessary since IE7+
			//iframe.src = getContentUrl();
		}
	}
}


