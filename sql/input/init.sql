-- sql/init.sql
CREATE DATABASE IF NOT EXISTS database_read;
USE database_read;
-- disable ONLY_FULL_GROUP_BY
SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
SET PERSIST sql_mode=(SELECT REPLACE(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
