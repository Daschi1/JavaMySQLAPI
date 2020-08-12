# JavaMySQLAPI
## A MySQL api for java.

[![](https://jitpack.io/v/Daschi1/JavaMySQLAPI.svg)](https://jitpack.io/#Daschi1/JavaMySQLAPI)
# Usage (For advice on how to create a sql query, look [here](https://github.com/Daschi1/updated-java-sql-generator))

```java
MySQL.using(/*id*/, new MySQL(/*hostname*/, /*port*/, /*username*/, /*password*/, /*database*/)); //connect to a mySQL
MySQL.disconnect(/*id*/); //disconnects the mySQL
MySQL.using(/*id*/, /*autoDisconnect*/); //configure autoDisconnect when program terminates

CachedRowSet cachedRowSet = MySQl.query(/*id*/, /*sql*/); //execute a query an recieve an CachedRowSet
MySQL.update(/*id*/, /*sql*/); //execute an update

MySQL.preventSQLInjection(/*id*/, /*parameter*/); //prevents mySQLInjection by adding comments for all ' and `

MySQL mySQL = MySQL.getMySQL(/*id*/); //get mySQL for more options

You can also save your own MySQL object
```
