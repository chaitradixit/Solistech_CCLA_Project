ECS_SearchAndFilter Component Release v3.0.8
================================================================================
  Full Release Name: 2012_12_06 v3.0.8 (Build 18619)

      Date: 06/12/2012 01:57 PM
    Author: camli
  Location: http://ecs-svn/stellent/COMPONENTS/ECS_SearchAndFilter/tags/v3.0.8
           
(c) Extended Content Solutions Limited 2012
================================================================================
﻿ECS_SearchAndFilter Component 2009_08_05 (Version 3.0.2)
--------------------------------------------------------

IMPORTANT NOTES: If using search and filter in an 11g environment and searchAndFilter is 
included on a page which is accesses via a service call, please set the service to scriptable 
so that idcToken is not required, thus enabling paging,filtering etc.  

For detailed usage notes on ECS_SearchAndFilter view the documents in:

  \\eigg\home\common\ECS\Stellent-Practice\ECS_SearchAndFilter

Change Log:

12/06/2012 - (Version 3.0.8) - Cam Li
- Fixed issue with search and filter when using custom oracle db and standard ucm providers

18/07/2011 - Trunk - Tom Marchant
-Added new parameter SF_preventRowIdOrdering. Only relevant when using view/table-based
 searching. Can allow paging to be used even if you are getting the ORA-01445 error. 
 Better than SF_PreventPaging, which removes paging functionality altogether to get
 round this issue.
 The only side effect is potentially arbitrary ordering of results, if no sort column
 is explicitly passed in.

27/10/2010 - Trunk - Paul Thomas
-Added sfUseImageForSearchButton. 
 If displaySearchButton=true and sfUseImageForSearchButton is set to a content item 
 dDocName then that image will be displayed as the search button


08/10/2010 (Version 3.0.6) - Tom Marchant
-Added new flag to database search, which will prevent any paging of the results 
 or use of the ROWNUM column in the constructed query. This can be set by passing 
 in a non-empty SF_PreventPaging value before executing the search.
 
 This was done to enable any view to be displayed using S&F. Before it was very 
 restrictive and required all tables to be joined via primary keys only. If the 
 view didn't satisfy this, the DB would throw an ORA-01445 error 'cannot select 
 ROWID...'.

19/05/2010 (Version 3.0.5) - Tom Marchant
-Amended makeSortFields include to have an else clause, invoked when S&F is being
 used without a drop-down list of sort fields. This clause adds two hidden fields,
 called 'SortField' and 'so' (sort order). These will preserve the user's chosen
 sort field/order when applying filters to results.
-To remove this functionality, override the include and remove the new else clause.

27/01/2010 (Version 3.0.4) - Tom Marchant
-Externalized list of selectable result counts. Default list found in component
 config file: 
  SF_RESULT_COUNTS_LIST
 Added ssResultCount to default PARAMETERSLIST, to ensure selected result counts
 are carried over in paging/sorting links.

22/01/2010 (Version 3.0.3)
-(TM) Added blank value search support to DB search. Passing in:
  (xParamName <matches> ``)
 will be converted to
  xParamName IS NULL
 Only works for fields configured for exact string matches.
-(TM) Fixed bug in buildParametersList include, which prevented persistence of from-to
 date filter values when paging/sorting.
-(PT) Added the following as per Atlas NII00082:
 fix next week, month, year filter bug

04/09/2009 (Version 3.0.2)
-Added case insensitive switch to database search

07/08/2009 (Version 3.0.1)
-Extended available QUICKDATE options to include 'today', 'yesterday'.

05/08/2009 (Version 3.0)
-Added support for running against database tables/views (this feature is
dependent on the ECS_BaseUtils component)

29/06/2009 (Version 2.0.3)
-Fixed bug in quick search when Oracle Text Search component is enabled. Ensures
 that numeric fields will be included in a quick search clause only when the quick
 search string is numeric.
-Added cursor:pointer to default calendar button styles

15/06/2009 (Version 2.0.2)
-Added support for numeric searching: use NUMERIC as the field search type.
-Added flag to toggle full-text searching: SF_enableFullTextSearch
-Renamed hideFtxTags flag to SF_hideFtxTags
-Removed bug from quick search function, enabled support for numeric fields

21/05/2009 (Version 2.0.1)
- Added toggle to filter header caching (SF_FILTER_HEADER_CACHING) to prevent
  sorting/filtering bugs
- Costly eval() functions have been replaced by getValue() calls. There is
  still one piece of looping code wrapped inside an eval() statement which
  needs fixing. 

30/03/2009 (Version 2.0)
- Cosmetic updates only

03/12/2008 (Build 11)
- Fixing issue HEI00345 in Atlas, search on date from/to in Hera 10g.

30/10/2008 (Build 10):
1) Changed calling of makeFilterHeader include to use cachInclude idoc function in order to render table header captions from a cache instead of evaluating 
   in on each page. 
2) Added and documented how multilingual support in Search and Filter works.
- All strings in resources and templates have been moved to resources/lang/en/ww_strings.htm where they are defined with English strings. 
  To add a new language support, create a new ww_strings.htm resource file under resources/lang/language_code where language_code corresponds to 
  Oracle language codes (two-letter code, e.g. en for English, fr for French, sp for Spanish, etc.)
- When installing Search and Filter, by default, column captions do not have multilingual support. They are all in English (hard-coded captions in 
  environment file). Full text, title, date, quick date (with hard-coded option list), expiry date and author are default field names defined in 
  environment file. To internationalise these default field captions (and any other additional fields that you may define in your custom component), 
  please follow the steps below:
	1. In your custom component, create an include in a resource file
	2. Add field caption definitions to the include
	3. For example, to allow Title field to be internationalised, please add the following line to your include, which will override the title caption 
	   in Search anf Filter component:
		<$SF_TITLE_caption=lc("wwTitle")$>
	4. If your component already has ww_strings.htm file for multilanguage support, add wwTitle to this file, e.g. 
		<@wwTitle=Title$>
	   To define the title in another language, for example French, define the above string with a language code prefix, such as: 
		<@fr.wwTitle=Titre@>
	   If your custom component doesn't have ww_strings.htm file, create this file and add your caption strings as described above.
	5. Call your caption strings include from a header if you want to include it on various pages in your application, or call it from the templates 
	   where you want to show the search and filter table columns.
  Alternativelly, if you just need to include Search and Filter table on a particular template, instead of creating an include you can just apply the title 
  caption definition directly to your template, i.e. <$SF_TITLE_caption=lc("wwTitle")$>


20/08/2008 (Build 9)
Repair of previous SVN build. No changes

17/04/2008 (Build 8)
Moved images into dedicated ECS/SearchAndFilter folder

14/01/2008 (build 7)
Added 10gR3 support

17/10/2007 (build 6)

Many changes, revolving around accessibility and XHTML 1.0 compliance.

- There is now a switch to use an accessible version of SF:
   
   useAccessibleSF
  
  By default this is null. Give it a non-null value and SF will render 
  in 'accessible' mode - this strips out any javascript functionality,
  changes paging and header structure and uses <label> elements to
  link field captions to their appropriate sort fields.

  Using this accessible mode has its limitations: some SF variables will
  no longer function and others will make parts of SF invalid if used.
  These limitations are dealt with in a separate file.

- The scope='col' declarations have been replaced by id/headers mapping.
  This links the two header cells to their appropriate column, and makes 
  the table far more accessible for screen readers.

- The 'more advanced search fields' link is no longer displayed if you
  do not have any more advanced search fields specified.

- There is now a 'Clear filters' button available, which removes any
  filters that your user may have defined. To display the button, simply
  give the following variable a non-null value:

   displayResetButton

- Many new CSS class hooks in the table/header structure, some of these
  act as replacements for deprecated HTML attributes (such as border,width).
  There is a separate document which outlines all these hooks.
 
- Blank value searches are now available. For text fields, the user must
  enter a special string constant, by default this is '%BLANK'. This can
  be changed by declaring the variable:

   SF_blankSearchString

  on your templates. For select fields, a (none) option will automatically
  be added to the lists. This is the default behaviour, to switch off the
  blank list options, give this variable a non-null value:

  SF_suppressBlankSearchOptions

- You can now specify your own include to handle paging of results. It
  isn't recommended to use this in conjunction with the accessible SF
  mode - there is already a specially-made accessible paginator built-in,
  this will be used when accessible mode is switched on. To use your own
  paging include, place the name of the include inside this variable:

   SF_CustomPaginator

- If you want your users to change the number of search results they see
  per page, you can now display a drop-down select list of different
  values in the advanced search. Just place the following variable, with
  a non-null value, into your template:

  SF_showResultCountSelector

- There is a bug involving the quick-search feature, in particular full-
  text searching. It concerns the Verity syntax used to perform full-text
  searches. If your quick search doesn't appear to work correctly, add
  the following variable to your template with a non-null value:

   hideFtxTags


11/10/2007 (build 5)


12/01/2007 - Tom Marchant

- Fixed bug in javascript, which was cauing runtime errors after clicking
  the 'More Options' link on the Adv. Search panel.

09/01/2007 - Tom Marchant

Minor changes:
- The automatic page refresh that occurs after selecting an item from
  a drop-down list can now be suppressed using the two variables:
  
   autoSearchSimple (applies in 'simple' search mode)
   autoSearchAdv (applies in 'advanced' search mode)
   
  The variables default to true and false, respectively. A value of Y
  means the refresh will take place. A value of N means no refresh takes
  place.
  
- Paging controls no longer display when there are no search results.

13/12/2006 - Tom Marchant

Several modifications:
- All drop-down boxes used as column filters have an onChange() event tied to
  them, which submits the SEARCHFILTER form. I.e. the search results will
  automatically 'refresh' after selecting an option from a drop-down box.
- Internal handling of the first column has been altered to allow the default
  first column behaviour to be replaced by a custom script, if one exists
  for the initial column definition.
- Header section widths have been changed and now allows some customization.
  The header row is a table with 3 cells - the first displays an item count,
  the middle one displays paging info and the last one contains the Search
  button. You can adjust the widths of the left and right cells by editing
  the 'headerSideWidth' variable (defaults to 35%)

Some additions:
- There is now support for the ECS_Paginator component, which provides a
  'prettier' but less functional paging display for your search results.
  In order to use the Paginator in S&F, the component must first be installed.
  Now put the following declarations in your S&F template:
  
    displayPagingOptions = "N"
    displayPaginator = "Y"
    
- An 'inline' custom include is available in the right-most cell of the header
  row. This provides a good place to put any custom search options if you
  are aiming for a compact vertical display. If you wish to use this feature, 
  create your new include and assign its name to the value of the 
  inlineHeaderInclude variable, i.e:
  
    <$inlineHeaderInclude = "DocListing_Header_includes"$>
    
- The HTML attributes for result rows can now be harnessed using the 
  custTableAttrs variable. This is useful for placing event triggers, padding,
  colours and other style settings in the rows of your S&F implementation.
  By default this variable is set to:
  
    "class='IssueBlock' cellpadding='2' cellspacing='4' 
     width='100%' border='0' bgcolor='#F6F6F4'"

- It is now possible to suppress the sort icons for particular columns. This
  is useful if you require the sort functionality for most columns, but its
  use is invalid on others. If you wish to use this, assign a comma-separated
  list of column names to the variable suppressSortLinks, e.g:
  
    <$suppressSortLinks = "CHECKBOX,NAME"$>
 
30/11/2006 - Tom Marchant

Extra changes to custom column include handling:
- If you wish to use a custom include script for one of your S&F columns,
  you need to specify a variable with the name <fieldName>_CustomIncludeScript.
  The value of this variable must match to the name of your custom column
  include.
- E.g. you have a column called AUTHOR and need a custom include script for it.
  You write out your include and call it whatever you want, e.g 'AUTHOR_includeScript'.
  In order to apply this include, make sure you add the following variable to the
  binder:
  
  <$AUTHOR_CustomIncludeScript = "AUTHOR_includeScript"$>


31/10/2006 - Tom Marchant

Made some minor changes:
- A line break tag occuring between the header and search results can now be
  suppressed using the variable 'hideLineBreak'. It is useful to suppress the
  break tag if you need a compact display for the search results.
- Usage Details:

  #hide the line break tag
  <$hideLineBreak="1"$>

- !!! The change below no longer applies. See above !!!
  The code using the <fieldName>_CustomIncludeScript did not work correctly.
  In order to define a custom display for a field's search results, you must
  first declare a non-null variable of the form <fieldName>_CustomIncludeScript.
  If found, a corresponding include will be used instead of the default display.
- Usage Details:

  #notify system that a custom include exists for the AUTHOR field
  <$AUTHOR_CustomIncludeScript="1"$>  
   
  #define the corresponding include
  <@dynamichtml AUTHOR_CustomIncludeScript@>
    <!-- Your custom display code -->
  <@end@>
 
 
29/09/2006 - Hamish

Added support for dropdownlist column type
- This displays a fixed list of values for a column. To use this feature put in the following config entries:
- Usage Details;

	#display type is dropdownlist
	SF_WFSTEP_type=dropdownlist

	#listvalues contains the actual values to be displayed
	SF_WFSTEP_listvalues=EnterDetails,Level1


27/09/2006 - Hamish

Allowed user to override the search service.
- This enables the user to use an alternative service to GET_SEARCH_RESULTS
- Usage Details;

  1. include the variable
  <$customSearchScript="performWFSearch"$>

  2. create the include performWFSearch and perform your own search
  
  3. At the end of the include rename your search results to "SearchResults"
  
  4. Take care of any other variable such as Total Rows

