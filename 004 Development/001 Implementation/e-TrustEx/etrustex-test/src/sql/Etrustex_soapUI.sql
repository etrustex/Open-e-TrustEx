-- //////////////////////////////////////////////////////
-- The following data is used to configure the platform
-- to be able to send messages
-- //////////////////////////////////////////////////////

-- ********CREDENTIALS********
INSERT INTO etr_tb_credentials VALUES (-33 ,SYSDATE,'guerrpa',SYSDATE , 'guerrpa',NULL,'{SHA-1}9a1j+qydHp3X+BvOF/jWTgN/F1c=',0,0, 'BAM_TRUSTUSR1', 'PARTY');
INSERT INTO etr_tb_credentials VALUES (-34 ,SYSDATE,'guerrpa',SYSDATE,'guerrpa',NULL,'{SHA-1}9a1j+qydHp3X+BvOF/jWTgN/F1c=',0,0,'BAM_TRUSTBCK1', 'PARTY');


-- ********PARTY********
INSERT INTO etr_tb_party VALUES (-33,SYSDATE,'guerrpa',SYSDATE,'guerrpa','BAM_TRUSTSUPPARTY1', NULL, -33, NULL,1 ,0);
INSERT INTO etr_tb_party VALUES (-34,SYSDATE,'guerrpa',SYSDATE,'guerrpa','BAM_TRUSTCUSTPARTY1',NULL, -34, NULL,1 ,0);

-- ********PARTY_ID********
INSERT INTO etr_tb_party_id VALUES (-33,SYSDATE,'guerrpa',SYSDATE,'guerrpa','GLN','BAM_TRUSTSUPPARTY1',-33);
INSERT INTO etr_tb_party_id VALUES (-34,SYSDATE,'guerrpa',SYSDATE,'guerrpa','GLN','BAM_TRUSTCUSTPARTY1',-34);

-- ********PARTY_AGREEMENT********
INSERT INTO etr_tb_partyagreement VALUES (-34,SYSDATE,'guerrpa',SYSDATE,'guerrpa',-34,-33);

-- ********PARTY_ROLE********
INSERT INTO etr_tb_party_role VALUES (-33,SYSDATE,'guerrpa',SYSDATE,'guerrpa',-33,6);
INSERT INTO etr_tb_party_role VALUES (-34,SYSDATE,'guerrpa',SYSDATE,'guerrpa',-34,6);

-- ********ICA********
INSERT INTO etr_tb_interchange_agr VALUES (-31,SYSDATE,'guerrpa',SYSDATE,'guerrpa',NULL,NULL, 3);
INSERT INTO etr_tb_interchange_agr VALUES (-32,SYSDATE,'guerrpa',SYSDATE,'guerrpa',NULL,NULL, 3);
INSERT INTO etr_tb_interchange_agr VALUES (-33,SYSDATE,'guerrpa',SYSDATE,'guerrpa',NULL,NULL, 3);
INSERT INTO etr_tb_interchange_agr VALUES (-34,SYSDATE,'guerrpa',SYSDATE,'guerrpa',NULL,NULL, 3);
INSERT INTO etr_tb_interchange_agr VALUES (-35,SYSDATE,'guerrpa',SYSDATE,'guerrpa',NULL,NULL, 3);

-- ********ICA_PARTY_ROLE********
INSERT INTO etr_tb_ica_partyrole VALUES (4,-33);
INSERT INTO etr_tb_ica_partyrole VALUES (5,-33);
INSERT INTO etr_tb_ica_partyrole VALUES (4,-34);
INSERT INTO etr_tb_ica_partyrole VALUES (7,-34);