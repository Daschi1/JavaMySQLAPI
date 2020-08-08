# JavaMySQLAPI
## A MySQL api for java.

[![](https://jitpack.io/v/Daschi1/JavaMySQLAPI.svg)](https://jitpack.io/#Daschi1/JavaMySQLAPI)
# Usage (For advice on how to create a sql query, look [here](https://github.com/Daschi1/updated-java-sql-generator))

```java
MySQL.using(new MySQL(/*hostname*/, /*port*/, /*username*/, /*password*/, /*database*/)); //connect to a mySQL
MySQL.disconnect(); //disconnects the mySQL
MySQL.using(/*autoDisconnect*/); //configure autoDisconnect when program terminates

CachedRowSet cachedRowSet = MySQl.query(/*sql*/); //execute a query an recieve an CachedRowSet
MySQL.update(/*sql*/); //execute an update

MySQL.preventSQLInjection(/*parameter*/); //prevents mySQLInjection by adding comments for all ' and `

MySQL mySQL = MySQL.getMySQL(); //get mySQL for more options

Look into the SimpleMySQL class for some query methods
```
