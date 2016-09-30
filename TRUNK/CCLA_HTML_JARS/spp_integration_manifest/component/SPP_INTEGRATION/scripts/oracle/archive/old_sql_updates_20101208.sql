/* re-reference field in documentclasses to match the actual meaning UCI00058 */
REM Altering table DOCUMENT_CLASSES to reference actual meaning

ALTER TABLE DOCUMENT_CLASSES
RENAME COLUMN IS_TRANSACTION TO SUBMIT_TO_SPP;

ALTER TABLE DOCUMENT_CLASSES 
ADD IS_TRANSACTION VARCHAR2 (1 CHAR);

UPDATE DOCUMENT_CLASSES 
SET IS_TRANSACTION='N' 
WHERE SCHPRIMARYKEY IN (
	'1','2','3','5','6','7','8','25','26','28','32','34','42','43','44','45','47','48',
	'49','50','51','52','53','54','55','62','63','64','65','66','68','73','82','83',
	'85','86','87','88','89','90','93','94','95','96','97','98','102','104','105',
	'106','107','108','109','110','111','112','113','114','115'
);

UPDATE DOCUMENT_CLASSES 
SET IS_TRANSACTION='Y' 
WHERE SCHPRIMARYKEY IN (
	'4','9','10','11','12','13','14','15','16','17','18','19','20','21','22','23',
	'24','27','29','30','31','33','35','36','37','38','39','40','41','46','56',
	'57','58','59','60','61','67','69','70','71','72','74','75','76','77','78',
	'79','80','81','84','91','92','100','101','103'
);

COMMIT;