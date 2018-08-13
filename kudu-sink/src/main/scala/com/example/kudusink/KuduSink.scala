package com.example.kudusink

import org.apache.spark.sql._
import org.apache.spark.sql.sources._
import org.apache.spark.sql.streaming.OutputMode
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.kudu.spark.kudu._
import org.apache.kudu.client._
import org.slf4j.LoggerFactory
import scala.util.Try

/**
  * A simple Structured Streaming sink which writes the data frame to Kudu.
  */
class KuduSink(sqlContext: SQLContext, parameters: Map[String, String]) extends Sink {
  private val logger = LoggerFactory.getLogger(classOf[KuduSink])

  private val kuduContext = new KuduContext(parameters("kudu.master"), sqlContext.sparkContext)

  private val tablename = parameters("kudu.table")

  override def addBatch(batchId: Long, data: DataFrame): Unit = {
    kuduContext.upsertRows(data, tablename)
  }
}

