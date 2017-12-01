-- //////////////////////////////////////////////////////
--  Creates a new user for etrustex Database as 
--  configured on the Datasource and grants
--  him all the rights 
-- //////////////////////////////////////////////////////

CREATE USER 'etrustex'@'localhost' IDENTIFIED BY 'etrustex01!';

GRANT ALL ON etrustex.* TO 'etrustex'@'localhost';
GRANT FILE ON *.* TO 'etrustex'@'localhost';