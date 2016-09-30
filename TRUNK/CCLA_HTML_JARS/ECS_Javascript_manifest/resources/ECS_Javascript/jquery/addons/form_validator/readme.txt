ECS_Clientside_Form_Validator Version 1.00

This folder contains all the necessary files for the jquery client side form validator to work. 

See the example.html to see how it is set up. 

See the following website for more details on customisation.
http://www.position-absolute.com/articles/jquery-form-validator-because-form-validation-is-a-mess/



You can add custom validation to the jquery.validationEngine.js file, 
under allRules property you can add a custom validation like so

          "newDateCheker":{
           	"regex":"/^[0-9]{1,2}\[- //.]\[0-9]{1,2}\[- //.]\[0-9]{4}$/",
          	"alertText":"* Invalid date, must be in DD/MM/YYYY format"},
          	
          	
Then add the rule to the class definition on the html input field

				<input class="validate[custom[nlDate]]"  type="text" name="NAME" id="ID"/>					


IMPORTANT. 

Forms must NOT be placed within tables. tables must sit within the form itself otherwise the validator does not work.