// dependant on jQuery 1.2

$(document).ready(function() {
// DOM ready functions */
 var IEBrowser = (/MSIE ((5\.5)|6)/.test(navigator.userAgent) && navigator.platform == "Win32");
   
    // hide
    $("img.downarrow").addClass("togglelist");
    //$("table.supportdoclist").parent().parent().hide();
    $("table.supportdoclist").hide();
       
    // button  arrow
    $("a.toggle_img").toggle(
        function () {
            //set button image
            $(this).children('.downarrow').removeClass("togglelist");
            $(this).children('.leftarrow').addClass("togglelist");
               
            $(this).parent().parent().next().find("td").removeClass("togglelist");
            $(this).parent().parent().next().find("table").slideDown();
            return false;
        },
        function () {
            //set button image
            $(this).children('.leftarrow').removeClass("togglelist");
            $(this).children('.downarrow').addClass("togglelist");
            if (IEBrowser){
            	//slide not supported in IE6, so just do straight show/hide.
            	$(this).parent().parent().next().find("td").toggleClass("togglelist");
            }
            else{
           	$(this).parent().parent().next().find("table").slideUp(function(){
            	//callback function to allow action to complete before hiding.
            	$(this).parent().toggleClass("togglelist");
            });
            }
                       
            return false;
        }
    );
    
    
});
    
