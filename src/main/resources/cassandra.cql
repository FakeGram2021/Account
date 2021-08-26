CREATE KEYSPACE IF NOT EXISTS account
  WITH REPLICATION = {
    'class' : 'SimpleStrategy',
    'replication_factor': 1
};

USE account;

CREATE TABLE account_info_by_account_id (
  account_id UUID,
  username TEXT,
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

CREATE TABLE follower (
  followee_id UUID,
  request_status TEXT,
  follower_id UUID,
  PRIMARY KEY(followee_id, follower_id)
);

CREATE TABLE following (
  follower_id UUID,
  request_status TEXT,
  followee_id UUID,
  PRIMARY KEY(follower_id, followee_id)
);

CREATE CUSTOM INDEX account_username_index ON account_info_by_account_id (username) USING 'org.apache.cassandra.index.sasi.SASIIndex';