/*
 * $.inJSON utility
 *
 * Searches arbitrary JSON for a key and returns an array of all matches.
 * Intended for use with jQuery 1.4.2
 *
 * Copyright (c) 2010 Dan Connor
 * www.danconnor.com
 *
 * Dual-licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
*/

$.extend($, {
    inJSON: function(json, key){
        var hit, hits = [];
        $.each(json, function(k,v){
            if (k === key)
                hits.push(v);
            if (typeof(v) === "string"){
                return true;
            } else if ($.isArray(v) || $.isPlainObject(v)) {
                var r = $.inJSON(v, key);
                if (r.length > 0)
                    hits = hits.concat(r);
            }
        });
        return hits;
    }
});
