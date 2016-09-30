-- Run the 2 create view statements in WCC tablespace.

CREATE OR REPLACE FORCE VIEW "V_DOCUMENT_STATUS" ("DID", "XAMOUNT", "XBANKACCOUNTNUMBER", "XUNITS", "XFUND", "XACCOUNTNUMBER", "XCLIENTNUMBER", "XJOBID", "XPARENTID", "DOUTDATE", "DSTATUS", "DREVRANK", "DDOCTYPE", "DDOCNAME", "DINDATE", "DCREATEDATE", "PARKING_LOT_DOCNAME", "DWFNAME", "DWFID", "XBUNDLEREF", "XDOCUMENTCLASS", "XSCANDATE", "XSOURCE", "XSTATUS", "XCOMPANY", "XCOMMENTS", "XBATCHNUMBER", "BUNDLE_STATUS", "BUNDLE_DOCNAME", "XWORKFLOWDATE", "XPROCESSDATE", "STATUS_DESC")
AS
  SELECT DM.DID,
    DM.XAMOUNT,
    DM.XBANKACCOUNTNUMBER,
    DM.XUNITS,
    DM.XFUND,
    DM.XACCOUNTNUMBER,
    DM.XCLIENTNUMBER,
    DM.XJOBID,
    DM.XPARENTID,
    RV.DOUTDATE,
    RV.DSTATUS,
    RV.DREVRANK,
    RV.DDOCTYPE,
    RV.DDOCNAME,
    RV.DINDATE,
    RV.DCREATEDATE,
    WFD.DDOCNAME AS PARKING_LOT_DOCNAME,
    WF.DWFNAME,
    WFD.DWFID,
    DM.XBUNDLEREF,
    DM.XDOCUMENTCLASS,
    DM.XSCANDATE,
    DM.XSOURCE,
    DM.XSTATUS,
    DM.XCOMPANY,
    DM.XCOMMENTS,
    DM.XBATCHNUMBER,
    bundleData.BUNDLE_STATUS,
    bundleData.BUNDLE_DOCNAME,
    DM.XWORKFLOWDATE,
    DM.XPROCESSDATE,
    CASE
      WHEN DM.XWORKFLOWDATE IS NOT NULL
      THEN 'Job Started'
      WHEN DM.XSTATUS = 'Archived'
      THEN 'Archived'
      WHEN DM.XSTATUS = 'Duplicate'
      THEN 'Duplicate'
      WHEN (bundleData.BUNDLE_STATUS = 'Flagged')
      THEN 'Flagged'
      WHEN DM.XSTATUS = 'Failed SPP release'
      THEN 'Job Not Started (ERROR)'
      WHEN (WF.DWFNAME = 'VerifyMandate')
      THEN 'In Parking Lot'
      WHEN (DM.XSTATUS            = 'Pending'
      OR DM.XSTATUS               = 'Validation')
      AND (DM.XBUNDLEREF         IS NOT NULL)
      AND BUNDLEDATA.BUNDLE_STATUS='Completed'
      THEN 'Bundle/Document Mismatch'
      WHEN (bundleData.BUNDLE_STATUS = 'EnterDetails')
      AND (DM.XBUNDLEREF            IS NOT NULL)
      THEN 'Pending In Iris'
      WHEN (bundleData.BUNDLE_STATUS = 'Validation')
      AND (DM.XBUNDLEREF            IS NOT NULL)
      THEN 'Validation'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      AND (DM.XSOURCE          = 'User Upload')
      THEN 'Job Not Started (User Uploaded)'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      AND (RV.DINDATE          < TO_DATE('01/04/2009', 'DD/MM/YYYY'))
      THEN 'Migrated from Sharepoint'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      AND (DM.XSTATUS          = 'Pending')
      AND (DM.XBUNDLEREF      IS NOT NULL)
      THEN 'Pending In Iris'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      THEN 'Job Not Started (ERROR)'
      WHEN (DM.XDOCUMENTCLASS   IS NULL
      OR NOT (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND')))
      AND (DM.XSOURCE            = 'User Upload')
      THEN 'Job Not Started (User Uploaded)'
      WHEN (DM.XDOCUMENTCLASS IN ('XXX', 'MULTIDOC'))
      THEN 'Job Not Started (Multi-doc)'
      WHEN DM.XBUNDLEREF IS NOT NULL AND
      (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS   !='MAND'
      AND DM.XDOCUMENTCLASS   != 'AUTOMAND'))
      AND (RV.DINDATE         IS NOT NULL
      AND RV.DINDATE           > TO_DATE('06/09/2009', 'DD/MM/YYYY'))
      AND (DM.XWORKFLOWDATE   IS NULL
      OR (DM.XWORKFLOWDATE    IS NOT NULL
      AND DM.XWORKFLOWDATE     < TO_DATE('01/01/1980', 'DD/MM/YYYY')))
      THEN 'Pending In Iris'
      WHEN DM.XBUNDLEREF IS NULL 
      AND (DM.XBATCHNUMBER = 0 OR DM.XBATCHNUMBER IS NULL)
      AND (DM.XSOURCE IS NULL OR DM.XSOURCE <> 'User Upload')
      AND (RV.DINDATE         IS NOT NULL
      AND RV.DINDATE           > TO_DATE('06/09/2009', 'DD/MM/YYYY'))
      THEN 'Missing Index Values (ERROR)'
      WHEN (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS   !='MAND'
      AND DM.XDOCUMENTCLASS   != 'AUTOMAND'))
      AND (DM.XSCANDATE       IS NOT NULL)
      AND (DM.XSCANDATE       <= TO_DATE('05/03/2009 23:59', 'DD/MM/YYYY HH24:MI'))
      THEN 'In 05/03/2009 Parking Lot'
      WHEN (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS   != 'MAND'
      AND DM.XDOCUMENTCLASS   != 'AUTOMAND'))
      AND (DM.XSCANDATE       IS NOT NULL)
      AND (DM.XSCANDATE        > TO_DATE('05/03/2009 23:59', 'DD/MM/YYYY HH24:MI'))
      THEN 'Job Not Started (ERROR)'
      WHEN (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS   != 'MAND'
      AND DM.XDOCUMENTCLASS   != 'AUTOMAND'))
      AND (DM.XSCANDATE       IS NULL)
      THEN 'Job Not Started (ERROR - No Scan Date)'
      ELSE 'Unknown!!'
    END AS STATUS_DESC
  FROM DOCMETA DM
  INNER JOIN REVISIONS RV
  ON (DM.dID = RV.dID)
  LEFT JOIN WORKFLOWDOCUMENTS WFD
  ON (RV.DDOCNAME = WFD.DDOCNAME)
  LEFT JOIN WORKFLOWS WF
  ON (WFD.DWFID = WF.DWFID)
  LEFT JOIN
    (SELECT bDM.xStatus AS BUNDLE_STATUS,
      bDM.xBundleRef    AS BUNDLE_REF,
      bRV.dDocName      AS BUNDLE_DOCNAME,
      bRV.dID           AS BUNDLE_DID
    FROM DOCMETA bDM
    INNER JOIN Revisions bRV
    ON (bDM.dID                    = bRV.dID
    AND bRV.dDocType               = 'Bundle'
    AND bRV.dRevRank               =0)
    ) bundleData ON (DM.XBUNDLEREF = bundleData.BUNDLE_REF)
  WHERE DM.DID                     = RV.DID
  AND RV.DDOCTYPE                 IN ('Document','ChildDocument')
  AND RV.DREVRANK                  = 0
  AND RV.DSTATUS                   = 'RELEASED'
  AND RV.DSECURITYGROUP			   = 'Public';
  
CREATE OR REPLACE FORCE VIEW "V_DOCUMENT_STATUS_WF" ("DID", "XAMOUNT", "XBANKACCOUNTNUMBER", "XUNITS", "XFUND", "XACCOUNTNUMBER", "XCLIENTNUMBER", "XJOBID", "XPARENTID", "DOUTDATE", "DSTATUS", "DREVRANK", "DDOCTYPE", "DDOCNAME", "DINDATE", "DCREATEDATE", "PARKING_LOT_DOCNAME", "DWFNAME", "DWFID", "XBUNDLEREF", "XDOCUMENTCLASS", "XSCANDATE", "XSOURCE", "XSTATUS", "XCOMPANY", "XCOMMENTS", "XBATCHNUMBER", "BUNDLE_STATUS", "BUNDLE_DOCNAME", "XWORKFLOWDATE", "XPROCESSDATE", "ENTERDETAILS_APPROVE_DATE", "ENTERDETAILS_APPROVE_USER", "VALIDATION_APPROVE_DATE", "VALIDATION_APPROVE_USER", "STATUS_DESC")
AS
  SELECT DM.DID,
    DM.XAMOUNT,
    DM.XBANKACCOUNTNUMBER,
    DM.XUNITS,
    DM.XFUND,
    DM.XACCOUNTNUMBER,
    DM.XCLIENTNUMBER,
    DM.XJOBID,
    DM.XPARENTID,
    RV.DOUTDATE,
    RV.DSTATUS,
    RV.DREVRANK,
    RV.DDOCTYPE,
    RV.DDOCNAME,
    RV.DINDATE,
    RV.DCREATEDATE,
    WFD.DDOCNAME AS PARKING_LOT_DOCNAME,
    WF.DWFNAME,
    WFD.DWFID,
    DM.XBUNDLEREF,
    DM.XDOCUMENTCLASS,
    DM.XSCANDATE,
    DM.XSOURCE,
    DM.XSTATUS,
    DM.XCOMPANY,
    DM.XCOMMENTS,
    DM.XBATCHNUMBER,
    bundleData.BUNDLE_STATUS,
    bundleData.BUNDLE_DOCNAME,
    DM.XWORKFLOWDATE,
    DM.XPROCESSDATE,
    wfh_ed.dActionDate AS ENTERDETAILS_APPROVE_DATE,
    wfh_ed.dUser       AS ENTERDETAILS_APPROVE_USER,
    wfh_v.dActionDate  AS VALIDATION_APPROVE_DATE,
    wfh_v.dUser        AS VALIDATION_APPROVE_USER,
    CASE
      WHEN DM.XWORKFLOWDATE IS NOT NULL
      THEN 'Job Started'
      WHEN DM.XSTATUS = 'Archived'
      THEN 'Archived'
      WHEN DM.XSTATUS = 'Duplicate'
      THEN 'Duplicate'
      WHEN (bundleData.BUNDLE_STATUS = 'Flagged')
      THEN 'Flagged'
      WHEN DM.XSTATUS = 'Failed SPP release'
      THEN 'Job Not Started (ERROR)'
      WHEN (WF.DWFNAME = 'VerifyMandate')
      THEN 'In Parking Lot'
      WHEN (DM.XSTATUS            = 'Pending'
      OR DM.XSTATUS               = 'Validation')
      AND (DM.XBUNDLEREF         IS NOT NULL)
      AND BUNDLEDATA.BUNDLE_STATUS='Completed'
      THEN 'Bundle/Document Mismatch'
      WHEN (bundleData.BUNDLE_STATUS = 'EnterDetails')
      AND (DM.XBUNDLEREF            IS NOT NULL)
      THEN 'Pending In Iris'
      WHEN (bundleData.BUNDLE_STATUS = 'Validation')
      AND (DM.XBUNDLEREF            IS NOT NULL)
      THEN 'Validation'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      AND (DM.XSOURCE          = 'User Upload')
      THEN 'Job Not Started (User Uploaded)'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      AND (RV.DINDATE          < TO_DATE('01/04/2009', 'DD/MM/YYYY'))
      THEN 'Migrated from Sharepoint'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      AND (DM.XSTATUS          = 'Pending')
      AND (DM.XBUNDLEREF      IS NOT NULL)
      THEN 'Pending In Iris'
      WHEN (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND'))
      THEN 'Job Not Started (ERROR)'
      WHEN (DM.XDOCUMENTCLASS   IS NULL
      OR NOT (DM.XDOCUMENTCLASS IN ('MAND','AUTOMAND')))
      AND (DM.XSOURCE            = 'User Upload')
      THEN 'Job Not Started (User Uploaded)'
      WHEN (DM.XDOCUMENTCLASS IN ('XXX', 'MULTIDOC'))
      THEN 'Job Not Started (Multi-doc)'
      WHEN DM.XBUNDLEREF     IS NOT NULL
      AND (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS  !='MAND'
      AND DM.XDOCUMENTCLASS  != 'AUTOMAND'))
      AND (RV.DINDATE        IS NOT NULL
      AND RV.DINDATE          > TO_DATE('06/09/2009', 'DD/MM/YYYY'))
      AND (DM.XWORKFLOWDATE  IS NULL
      OR (DM.XWORKFLOWDATE   IS NOT NULL
      AND DM.XWORKFLOWDATE    < TO_DATE('01/01/1980', 'DD/MM/YYYY')))
      THEN 'Pending In Iris'
      WHEN DM.XBUNDLEREF  IS NULL
      AND (DM.XBATCHNUMBER = 0
      OR DM.XBATCHNUMBER  IS NULL)
      AND (DM.XSOURCE     IS NULL
      OR DM.XSOURCE       <> 'User Upload')
      AND (RV.DINDATE     IS NOT NULL
      AND RV.DINDATE       > TO_DATE('06/09/2009', 'DD/MM/YYYY'))
      THEN 'Missing Index Values (ERROR)'
      WHEN (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS   !='MAND'
      AND DM.XDOCUMENTCLASS   != 'AUTOMAND'))
      AND (DM.XSCANDATE       IS NOT NULL)
      AND (DM.XSCANDATE       <= TO_DATE('05/03/2009 23:59', 'DD/MM/YYYY HH24:MI'))
      THEN 'In 05/03/2009 Parking Lot'
      WHEN (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS   != 'MAND'
      AND DM.XDOCUMENTCLASS   != 'AUTOMAND'))
      AND (DM.XSCANDATE       IS NOT NULL)
      AND (DM.XSCANDATE        > TO_DATE('05/03/2009 23:59', 'DD/MM/YYYY HH24:MI'))
      THEN 'Job Not Started (ERROR)'
      WHEN (DM.XDOCUMENTCLASS IS NULL
      OR (DM.XDOCUMENTCLASS   != 'MAND'
      AND DM.XDOCUMENTCLASS   != 'AUTOMAND'))
      AND (DM.XSCANDATE       IS NULL)
      THEN 'Job Not Started (ERROR - No Scan Date)'
      ELSE 'Unknown!!'
    END AS STATUS_DESC
  FROM DOCMETA DM
  INNER JOIN REVISIONS RV
  ON (DM.dID = RV.dID)
  LEFT JOIN WORKFLOWDOCUMENTS WFD
  ON (RV.DDOCNAME = WFD.DDOCNAME)
  LEFT JOIN WORKFLOWS WF
  ON (WFD.DWFID = WF.DWFID)
  LEFT JOIN
    (SELECT bDM.xStatus AS BUNDLE_STATUS,
      bDM.xBundleRef    AS BUNDLE_REF,
      bRV.dDocName      AS BUNDLE_DOCNAME,
      bRV.dID           AS BUNDLE_DID
    FROM DOCMETA bDM
    INNER JOIN Revisions bRV
    ON (bDM.dID                    = bRV.dID
    AND bRV.dDocType               = 'Bundle'
    AND bRV.dRevRank               =0)
    ) bundleData ON (DM.XBUNDLEREF = bundleData.BUNDLE_REF)
  LEFT JOIN WORKFLOWHISTORY wfh_ed
  ON (wfh_ed.dID         = bundleData.BUNDLE_DID
  AND wfh_ed.dWfStepName = 'EnterDetails'
  AND wfh_ed.dAction     = 'Approve'
  AND wfh_ed.dWfName     = 'CCLA_MailHandling')
  LEFT JOIN WORKFLOWHISTORY wfh_v
  ON (wfh_v.dID         = bundleData.BUNDLE_DID
  AND wfh_v.dWfStepName = 'Validation'
  AND wfh_v.dAction     = 'Approve'
  AND wfh_v.dWfName     = 'CCLA_MailHandling')
  WHERE DM.DID          = RV.DID
  AND RV.DDOCTYPE      IN ('Document','ChildDocument')
  AND RV.DREVRANK       = 0
  AND RV.DSTATUS        = 'RELEASED'
  AND RV.DSECURITYGROUP	= 'Public';
  