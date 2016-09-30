// -----------------------------
// General AJAX helper functions
// -----------------------------
// Tom Marchant
//
// Description
// -----------
// A collection of helper functions for handling AJAX response data 
// from UCM. Most of these are centered around JSON-formatted response
// messages from the server, including functions for reading JSON 
// ResultSets.
// 
// Dependencies
// ------------
// jQuery


// Use this function to dispatch requests to UCM, if you require
// an HDA-formatted response. There is no need to include the IsJava
// switch in the attribs object.
//
// A timestamp attribute is added to the request to ensure the request
// is always unique (prevents returned cached responses in some browsers)
function getUCM_hda(idcService, attribs, responseFunc) {
	
	// Unique timestamp to avoid cached response data in IE
	attribs.ts = new Date().getTime();
	// IsJSON flag to ensure JSON-formatted response from UCM
	attribs.IsJava = "1";
	
	$.get("?IdcService=" + idcService, attribs, 
						function(jsonData) { responseFunc(jsonData); });
}

// Use this function to dispatch requests to UCM, if you require
// a JSON-formatted response. There is no need to include the IsJson
// switch in the attribs object.
//
// A timestamp attribute is added to the request to ensure the request
// is always unique (prevents returned cached responses in some browsers)
function getUCM_json(idcService, attribs, responseFunc) {
	
	// Unique timestamp to avoid cached response data in IE
	attribs.ts = new Date().getTime();
	// IsJSON flag to ensure JSON-formatted response from UCM
	attribs.IsJson = "1";
	
	$.getJSON("?IdcService=" + idcService, attribs, 
						function(jsonData) { responseFunc(jsonData); });
}


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
		eval(scriptTags[i].innerHTML);
	}
}

// Designed for use with the IsJava=1 switch (HDA-formatted
// response data)
//
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
			var thisPair = localData[i].split("=");	
			localDataAttribs[thisPair[0]] = thisPair[1];
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

	if (jsonData == null)
		return false;
		
	var localData = jsonData.LocalData;
	if (localData == null)
		return false;
		
	var statusCode = localData.StatusCode;
	
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

// Returns number of rows in a given JSON ResultSet
function getJsonResultSetNumRows(jsonResultSet) {
	return jsonResultSet.rows.length;
}

// Returns number of fields in a given JSON ResultSet
function getJsonResultSetNumRows(jsonResultSet) {
	return jsonResultSet.fields.length;
}