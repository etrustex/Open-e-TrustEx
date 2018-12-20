
update etr_tb_metadata set md_value = '//*:AwardingNotification/*:ID'
where md_id = 764;

update etr_tb_metadata set md_value = '//*:AwardingNotification/*:IssueDate'
where md_id = 765;

update etr_tb_metadata set md_value = '/eprior/xsd2.1/maindoc/EC-AwardingNotification-0_1.xsd'
where md_id = 769;



update etr_tb_document
set
doc_name = 'AwardingNotificationRequest',
doc_local_name = 'AwardingNotification',
doc_namespace= 'ec:schema:xsd:AwardingNotification-0.1'
where doc_id = 82;

UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_REC_ROL_ID     = 8,
TRA_SEN_ROL_ID     = 7,
TRA_NAME           = 'SubmitAwardingNotification',
TRA_NAMESPACE      = 'ec:services:wsdl:AwardingNotification-2',
TRA_REQ_LOCAL_NAME = 'SubmitAwardingNotificationRequest',
TRA_RES_LOCAL_NAME = 'SubmitAwardingNotificationResponse',
TRA_VERSION = '2.0'
WHERE TRA_ID       = 82;

UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_REC_ROL_ID     = 8,
TRA_SEN_ROL_ID     = 7,
TRA_TYPE_CD        = 'DRC',
TRA_DOC_ID         = 83,
TRA_VERSION = '2.0'
WHERE TRA_ID       = 83;

UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_REC_ROL_ID     = 8,
TRA_SEN_ROL_ID     = 7,
TRA_TYPE_CD        = 'SCO',
TRA_DOC_ID         = 83,
TRA_VERSION = '2.0'
WHERE TRA_ID       = 85;

UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_REC_ROL_ID     = 7,
TRA_SEN_ROL_ID     = 8,
TRA_TYPE_CD        = 'SSC',
TRA_DOC_ID         = 83,
TRA_VERSION = '2.0'
WHERE TRA_ID       = 84;

UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_REC_ROL_ID     = 8,
TRA_SEN_ROL_ID     = 7,
TRA_VERSION = '2.0'
WHERE TRA_ID       = 76;

UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_REC_ROL_ID     = 7,
TRA_SEN_ROL_ID     = 8,
TRA_VERSION = '2.0'
WHERE TRA_ID       = 77;

UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_NAME           = 'SubmitClarification',
TRA_NAMESPACE      = 'ec:services:wsdl:TenderClarification-2',
TRA_REQ_LOCAL_NAME = 'SubmitTenderClarificationRequest',
TRA_RES_LOCAL_NAME = 'SubmitTenderClarificationResponse',
TRA_REC_ROL_ID     = 7,
TRA_SEN_ROL_ID     = 8,
TRA_TYPE_CD        = 'TCL',
TRA_VERSION = '2.0'
WHERE TRA_ID       = 78;


UPDATE ETR_TB_TRANSACTION
SET 
MOD_DT             = sysdate,
MOD_ID             = 'derveol',
TRA_NAME           = 'SubmitClarificationRequest',
TRA_NAMESPACE      = 'ec:services:wsdl:TenderClarificationRequest-2',
TRA_REQ_LOCAL_NAME = 'SubmitTenderClarificationRequestRequest',
TRA_RES_LOCAL_NAME = 'SubmitTenderClarificationRequestResponse',
TRA_REC_ROL_ID     = 8,
TRA_SEN_ROL_ID     = 7,
TRA_TYPE_CD        = 'RTC',
TRA_VERSION = '2.0'
WHERE TRA_ID       = 79;

delete from ETR_TB_PROFILE_TRANSACTION where PTR_TRA_ID in (78,79,76,77,82,83,84,85) and PTR_PRO_ID = 5;  

DELETE FROM ETR_TB_DOCUMENT WHERE DOC_ID IN (84,85);

INSERT INTO ETR_TB_TRANSACTION (    TRA_ID,    CRE_DT,    CRE_ID,    MOD_DT,    MOD_ID,    TRA_NAME,    TRA_NAMESPACE,    TRA_REQ_LOCAL_NAME,    TRA_RES_LOCAL_NAME,    TRA_VERSION,    TRA_CIA_ID,    TRA_DOC_ID,    TRA_REC_ROL_ID,    TRA_SEN_ROL_ID,    TRA_TYPE_CD  )
  VALUES (    87,    sysdate,    'derveol',    sysdate,    'derveol',    'SubmitAdditionalInformation',    'ec:services:wsdl:AdditionalInformation-2',    'SubmitAdditionalInformationRequest',    'SubmitAdditionalInformationResponse',    '2.0',    null,    78,    8,    7,    'AIN'  );

INSERT INTO ETR_TB_TRANSACTION (    TRA_ID,    CRE_DT,    CRE_ID,    MOD_DT,    MOD_ID,    TRA_NAME,    TRA_NAMESPACE,    TRA_REQ_LOCAL_NAME,    TRA_RES_LOCAL_NAME,    TRA_VERSION,    TRA_CIA_ID,    TRA_DOC_ID,    TRA_REC_ROL_ID,    TRA_SEN_ROL_ID,    TRA_TYPE_CD  )
  VALUES (    88,    sysdate,    'derveol',    sysdate,    'derveol',    'SubmitAdditionalInformationRequest',    'ec:services:wsdl:AdditionalInformationRequest-2',    'SubmitAdditionalInformationRequestRequest',    'SubmitAdditionalInformationRequestResponse',    '2.0',    null,    79,    7,    8,    'RAI'  );

insert into etr_tb_profile_transaction (PTR_PRO_ID,PTR_TRA_ID) values (4, 87);
insert into etr_tb_profile_transaction (PTR_PRO_ID,PTR_TRA_ID) values (4, 88);
  
INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3123,     'DOCUMENT_ID_XQUERY',     '//*:TenderingAnswers/*:ID',     NULL,     NULL,     NULL,     87,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3118,     'DOCUMENT_ISSUEDATE_XQUERY',     '//*:TenderingAnswers/*:IssueDate',     NULL,     NULL,     NULL,     87,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3119,     'SENDER_ID_XQUERY',     '//*:Sender/*:Identifier',     NULL,     NULL,     NULL,     87,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3120,     'RECEIVER_ID_XQUERY',     '//*:Receiver/*:Identifier',     NULL,     NULL,     NULL,     87,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3121,     'SYNCH_VALIDATE_XSD',     'false',     NULL,     NULL,     NULL,     87,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3122,     'DOCUMENT_XSD_URL',     '/eprior/xsd2.1/maindoc/EC-TenderingAnswers-0_1.xsd',     NULL,     NULL,     NULL,     87,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3129,     'DOCUMENT_ID_XQUERY',     '//*:TenderingQuestions/*:ID',     NULL,     NULL,     NULL,     88,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3124,     'DOCUMENT_ISSUEDATE_XQUERY',     '//*:TenderingQuestions/*:IssueDate',     NULL,     NULL,     NULL,     88,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3125,     'SENDER_ID_XQUERY',     '//*:Sender/*:Identifier',     NULL,     NULL,     NULL,     88,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3126,     'RECEIVER_ID_XQUERY',     '//*:Receiver/*:Identifier',     NULL,     NULL,     NULL,     88,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3127,     'SYNCH_VALIDATE_XSD',     'false',     NULL,     NULL,     NULL,     88,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3128,     'DOCUMENT_XSD_URL',     '/eprior/xsd2.1/maindoc/EC-TenderingQuestions-0_1.xsd',     NULL,     NULL,     NULL,     88,     NULL,     NULL,     NULL,     NULL,     NULL   );

  
  
  
 

  Insert into ETR_TB_TRANSACTION (TRA_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID,TRA_NAME,TRA_NAMESPACE,TRA_REQ_LOCAL_NAME,TRA_RES_LOCAL_NAME,TRA_VERSION,TRA_CIA_ID,TRA_DOC_ID,TRA_REC_ROL_ID,TRA_SEN_ROL_ID,TRA_TYPE_CD) 
values (86,sysdate,'derveol',sysdate,'derveol','SubmitCallForTenders','ec:services:wsdl:CallForTenders-2.1','SubmitCallForTendersRequest','SubmitCallForTendersResponse','2.1',null,18,4,7,null);




INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   (     3110,     'SENDER_ID_XQUERY',     '//*:Sender/*:Identifier',     NULL,     NULL,     NULL,     86,     NULL,     NULL,     NULL,     NULL,     NULL   );
Insert into ETR_TB_METADATA (MD_ID,MD_TYPE,MD_VALUE,PID_DOC_ID,PID_ICA_ID,PID_PRO_ID,PID_TRA_ID,MD_SENDER_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID) values (3111,'DOCUMENT_XSD_URL','/eprior/xsd2.1/maindoc/UBL-CallForTenders-2.1.xsd',null,null,null,86,null,null,null,null,null);
Insert into ETR_TB_METADATA (MD_ID,MD_TYPE,MD_VALUE,PID_DOC_ID,PID_ICA_ID,PID_PRO_ID,PID_TRA_ID,MD_SENDER_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID) values (3112,'SYNCH_VALIDATE_XSD','false',null,null,null,86,null,null,null,null,null);
Insert into ETR_TB_METADATA (MD_ID,MD_TYPE,MD_VALUE,PID_DOC_ID,PID_ICA_ID,PID_PRO_ID,PID_TRA_ID,MD_SENDER_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID) values (3113,'DOCUMENT_SCEMATRON',null,null,null,null,86,null,null,null,null,null);
Insert into ETR_TB_METADATA (MD_ID,MD_TYPE,MD_VALUE,PID_DOC_ID,PID_ICA_ID,PID_PRO_ID,PID_TRA_ID,MD_SENDER_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID) values (3114,'RECEIVER_ID_XQUERY','//*:Receiver/*:Identifier',null,null,null,86,null,null,null,null,null);

Insert into ETR_TB_METADATA (MD_ID,MD_TYPE,MD_VALUE,PID_DOC_ID,PID_ICA_ID,PID_PRO_ID,PID_TRA_ID,MD_SENDER_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID) values (3116,'DOCUMENT_ISSUEDATE_XQUERY','//*:CallForTenders/*:IssueDate',null,null,null,86,null,null,null,null,null);
Insert into ETR_TB_METADATA (MD_ID,MD_TYPE,MD_VALUE,PID_DOC_ID,PID_ICA_ID,PID_PRO_ID,PID_TRA_ID,MD_SENDER_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID) values (3117,'DOCUMENT_VERSION_XQUERY','//*:CallForTenders/*:ID',null,null,null,86,null,null,null,null,null);

Insert into ETR_TB_METADATA (MD_ID,MD_TYPE,MD_VALUE,PID_DOC_ID,PID_ICA_ID,PID_PRO_ID,PID_TRA_ID,MD_SENDER_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID) values (3115,'DOCUMENT_ID_XQUERY',
'concat(//*:Body/*/*:CallForTenders/*:UUID/text(), ''::'', //*:Body/*/*:CallForTenders/*:ID/text())',null,null,null,86,null,null,null,null,null);



-- attention to metadata DOCUMENT_SCEMATRON: copy from existing metadata 113

Insert into ETR_TB_PROFILE_TRANSACTION (PTR_PRO_ID,PTR_TRA_ID) values (4,86);
Insert into ETR_TB_PROFILE_TRANSACTION (PTR_PRO_ID,PTR_TRA_ID) values (5,86);
  

Insert into ETR_TB_TRANSACTION (TRA_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID,TRA_NAME,TRA_NAMESPACE,TRA_REQ_LOCAL_NAME,TRA_RES_LOCAL_NAME,TRA_VERSION,TRA_CIA_ID,TRA_DOC_ID,TRA_REC_ROL_ID,TRA_SEN_ROL_ID,TRA_TYPE_CD) 
values (89,sysdate,'derveol',sysdate,'derveol','SubmitInboxRequest','ec:services:wsdl:InboxRequest-2.1','SubmitInboxRequestRequest','SubmitInboxRequestResponse','2.1',null,4,4,4,null);

insert into ETR_TB_PROFILE_TRANSACTION (PTR_PRO_ID,PTR_TRA_ID) values (12,89);

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3048,     'SYNCH_VALIDATE_XSD', 'true',     NULL,     NULL,     NULL,     89,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3049,     'DOCUMENT_XSD_URL',     '/etrustex/xsd2.1/maindoc/EC-InboxRequest-2.0.xsd',     NULL,     NULL,     NULL,     89,     NULL,     NULL,     NULL,     NULL,     NULL   );


INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3050,     'SENDER_ID_XQUERY', '//*:Sender/*:Identifier',     NULL,     NULL,     NULL,     89,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3051,     'RECEIVER_ID_XQUERY',     '//*:Receiver/*:Identifier',     NULL,     NULL,     NULL,     89,     NULL,     NULL,     NULL,     NULL,     NULL   );


	
	
Insert into ETR_TB_TRANSACTION (TRA_ID,CRE_DT,CRE_ID,MOD_DT,MOD_ID,TRA_NAME,TRA_NAMESPACE,TRA_REQ_LOCAL_NAME,TRA_RES_LOCAL_NAME,TRA_VERSION,TRA_CIA_ID,TRA_DOC_ID,TRA_REC_ROL_ID,TRA_SEN_ROL_ID,TRA_TYPE_CD) 
values (90,sysdate,'derveol',sysdate,'derveol','SubmitStatusRequest','ec:services:wsdl:StatusRequest-2.2','SubmitStatusRequestRequest','SubmitStatusRequestResponse','2.2',null,10,4,4,null);

insert into ETR_TB_PROFILE_TRANSACTION (PTR_PRO_ID,PTR_TRA_ID) values (12,90);


INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3052,     'SYNCH_VALIDATE_XSD', 'true',     NULL,     NULL,     NULL,     90,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3053,     'DOCUMENT_XSD_URL',     '/etrustex/xsd2.1/maindoc/EC-StatusRequest-2.0.xsd',     NULL,     NULL,     NULL,     90,     NULL,     NULL,     NULL,     NULL,     NULL   );


INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3054,     'SENDER_ID_XQUERY', '//*:Sender/*:Identifier',     NULL,     NULL,     NULL,     90,     NULL,     NULL,     NULL,     NULL,     NULL   );

INSERT INTO ETR_TB_METADATA   (     MD_ID,     MD_TYPE,     MD_VALUE,     PID_DOC_ID,     PID_ICA_ID,     PID_PRO_ID,     PID_TRA_ID,     MD_SENDER_ID,     CRE_DT,     CRE_ID,     MOD_DT,     MOD_ID   )   VALUES   
(     3055,     'RECEIVER_ID_XQUERY',     '//*:Receiver/*:Identifier',     NULL,     NULL,     NULL,     90,     NULL,     NULL,     NULL,     NULL,     NULL   );

