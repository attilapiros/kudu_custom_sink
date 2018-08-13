package com.example.kudusink

import org.apache.spark.sql._
import org.apache.spark.sql.sources._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.execution.streaming.Sink

class KuduSinkProvider extends StreamSinkProvider with DataSourceRegister {

	override def createSink(
		sqlContext: SQLContext,
		parameters: Map[String, String],
		partitionColumns: Seq[String],
		outputMode: OutputMode): Sink = new KuduSink(sqlContext, parameters)

	override def shortName(): String = "kudu"
}
