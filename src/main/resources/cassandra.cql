CREATE KEYSPACE IF NOT EXISTS account
  WITH REPLICATION = {
    'class' : 'SimpleStrategy',
    'replication_factor': 1
};

USE account;

CREATE TABLE account_info_by_account_id (
  account_id UUID,
  username TEXT,
  avatar TEXT,
  password TEXT,
  first_name TEXT,
  last_name TEXT,
  phone_number TEXT,
  email TEXT,
  gender TEXT,
  date_of_birth TEXT,
  web_url TEXT,
  biography TEXT,
  privacy TEXT,
  PRIMARY KEY(account_id)
);

CREATE TABLE relation_by_subject (
  subject_id UUID,
  relation_type TEXT,
  object_id UUID,
  PRIMARY KEY(subject_id, relation_type, object_id)
);

CREATE TABLE relation_by_object (
  object_id UUID,
  relation_type TEXT,
  subject_id UUID,
  PRIMARY KEY(object_id, relation_type, subject_id)
);

CREATE CUSTOM INDEX account_username_index ON account_info_by_account_id (username) USING 'org.apache.cassandra.index.sasi.SASIIndex';