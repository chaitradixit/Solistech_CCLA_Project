-- Updates Identity Checked attribute statuses to passed
UPDATE ELEMENT_ATTRIBUTE_APPLIED SET attribute_status = 1 WHERE ELEMENT_ID IN
(SELECT PERSON_ID FROM IDENTITY_CHECK idc
INNER JOIN ELEMENT_ATTRIBUTE_APPLIED eaa ON (idc.PERSON_ID = eaa.ELEMENT_ID AND eaa.element_attribute_id = 7)
WHERE IS_AUTHENTICATED = 1 AND eaa.attribute_status = 0)
AND ELEMENT_ATTRIBUTE_ID = 7;