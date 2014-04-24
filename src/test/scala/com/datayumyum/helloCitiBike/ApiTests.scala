package com.datayumyum.helloCitiBike

import org.junit.Test
import org.json4s._
import org.json4s.native.JsonMethods._

class ApiTests {

  @Test
  def json() {
    val jsonStr = scala.io.Source.fromURL("http://appservices.citibikenyc.com/data2/stations.php").getLines.mkString("\n")
    val parsedResult = parse(jsonStr)
    val stationIds = for {JArray(stations) <- parsedResult \\ "results"
                   JObject(station) <- stations
                   JField("id", JInt(id)) <- station
    } yield id
    println(stationIds)
  }

}