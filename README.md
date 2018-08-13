# Custom Kudu Sink and example Spark app 

Example Spark Structured Streaming app where a Kudu table is written by a custom sink using KuduContext of Kudu Spark (see the depenendency in kudu-sink/pom.xml).

*Beware*: 

1) The current KuduContext cannot be used with streaming DataFrame but I am about to create a Kudu patch regarding the fix.
   This app is used for testing that patch.
2) In case of a custom sink checkpoint location must be specified, but socket source does not support offsets, 
   so before each test the /tmp/example-checkpoint directory must be deleted.
3) The sink simply upserts all the rows into the Kudu table.
4) There is no proper error handling, this is just a quick prototype.

Using the custom Kudu sink in your query is quite simple (the object mapping is done by KuduContext):

```
   .writeStream
   .format("kudu")
   .option("kudu.master", kuduMaster)
   .option("kudu.table", tableName)
   .option("checkpointLocation", "/tmp/example-checkpoint")
   .outputMode("update")
   .start()
```


## Prerequisite

Kudu table must be created for example via impala-shell, like:

```
CREATE TABLE test_table (value STRING PRIMARY KEY, count INT) STORED AS KUDU;
```

## Starting Netcat

```
nc -lk 9999
```


## Starting spark app

You can start the Spark app for example in local mode via maven exec plugin, like (`<kuduMaster>` must be replaced):

```
mvn exec:java -Dexec.classpathScope="compile" -pl core -Dexec.mainClass="com.example.SparkStreamingKuduCustomSinkExample" -Dexec.args="local localhost 9999 <kuduMaster> impala::default.test_table"
```
