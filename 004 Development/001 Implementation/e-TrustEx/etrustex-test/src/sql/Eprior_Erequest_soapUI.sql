-- //////////////////////////////////////////////////////
-- The following data is used to configure the platform
-- to be able to send messages
-- //////////////////////////////////////////////////////

-- ********CREDENTIALS********
INSERT INTO etr_tb_credentials VALUES (-45 ,SYSDATE,'guerrpa',SYSDATE , 'guerrpa',NULL,'{SHA-1}8vFjckXpkRG+cMm1GszzAQOJ/zU=',0,0, 'BAM_EPRIORUSR1', 'PARTY');
INSERT INTO etr_tb_credentials VALUES (-46 ,SYSDATE,'guerrpa',SYSDATE,'guerrpa',NULL,'{SHA-1}KKlL2ddLeQo4PnnqhAHx44GFUMk=',0,0,'BAM_EPRIORUSR2', 'PARTY');

-- ********PARTY********
INSERT INTO etr_tb_party VALUES (-45,SYSDATE,'guerrpa',SYSDATE,'guerrpa','BAM_EPRIORSUP1PARTY', NULL, -45, NULL,1 ,0);
INSERT INTO etr_tb_party VALUES (-46,SYSDATE,'guerrpa',SYSDATE,'guerrpa','BAM_EPRIORCUS1PARTY',NULL, -46, NULL,1 ,0);

-- ********PARTY_ID********
INSERT INTO etr_tb_party_id VALUES (-45,SYSDATE,'guerrpa',SYSDATE,'guerrpa','GLN','BAM_EPRIORSUP1PARTY',-45);
INSERT INTO etr_tb_party_id VALUES (-46,SYSDATE,'guerrpa',SYSDATE,'guerrpa','GLN','BAM_EPRIORCUS1PARTY',-46);

-- ********PARTY_AGREEMENT********
INSERT INTO etr_tb_partyagreement VALUES (-45,SYSDATE,'guerrpa',SYSDATE,'guerrpa',-46,-45);

-- ********PARTY_ROLE********
INSERT INTO etr_tb_party_role VALUES (-45,SYSDATE,'guerrpa',SYSDATE,'guerrpa',-45,1);
INSERT INTO etr_tb_party_role VALUES (-46,SYSDATE,'guerrpa',SYSDATE,'guerrpa',-46,2);

-- ********ICA********
INSERT INTO etr_tb_interchange_agr VALUES (-45,SYSDATE,'guerrpa',SYSDATE,'guerrpa',SYSDATE,2, 13);
INSERT INTO etr_tb_interchange_agr VALUES (-46,SYSDATE,'guerrpa',SYSDATE,'guerrpa',SYSDATE,2, 9);
INSERT INTO etr_tb_interchange_agr VALUES (-47,SYSDATE,'guerrpa',SYSDATE,'guerrpa',SYSDATE,2, 8);
INSERT INTO etr_tb_interchange_agr VALUES (-48,SYSDATE,'guerrpa',SYSDATE,'guerrpa',SYSDATE,2, 12);
INSERT INTO etr_tb_interchange_agr VALUES (-49,SYSDATE,'guerrpa',SYSDATE,'guerrpa',SYSDATE,2, 11);
INSERT INTO etr_tb_interchange_agr VALUES (-50,SYSDATE,'guerrpa',SYSDATE,'guerrpa',SYSDATE,2, 10);

-- ********ICA_PARTY_ROLE********
INSERT INTO etr_tb_ica_partyrole VALUES (-45,-45);
INSERT INTO etr_tb_ica_partyrole VALUES (-45,-46);
INSERT INTO etr_tb_ica_partyrole VALUES (-46,-45);
INSERT INTO etr_tb_ica_partyrole VALUES (-46,-46);
INSERT INTO etr_tb_ica_partyrole VALUES (-47,-45);
INSERT INTO etr_tb_ica_partyrole VALUES (-47,-46);
INSERT INTO etr_tb_ica_partyrole VALUES (-48,-45);
INSERT INTO etr_tb_ica_partyrole VALUES (-48,-46);
INSERT INTO etr_tb_ica_partyrole VALUES (-49,-45);
INSERT INTO etr_tb_ica_partyrole VALUES (-49,-46);
INSERT INTO etr_tb_ica_partyrole VALUES (-50,-45);
INSERT INTO etr_tb_ica_partyrole VALUES (-50,-46);

