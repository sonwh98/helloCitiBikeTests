package com.datayumyum.helloCitiBike

import org.junit.Test

//TODO: checkout https://github.com/jdereg/json-io
class JsonParserTests {

  @Test
  def queryJson() {
    import org.json4s._
    import org.json4s.native.JsonMethods._
    val jsonStr = scala.io.Source.fromURL("http://appservices.citibikenyc.com/data2/stations.php").getLines.mkString
    ("\n")
    val parsedResult = parse(jsonStr)
    val stationIds = for {JArray(stations) <- parsedResult \\ "results"
                          JObject(station) <- stations
                          JField("id", JInt(id)) <- station
    } yield id
    println(stationIds)
  }

  @Test
  def parseStation() {
    import org.json4s._
    import org.json4s.native.JsonMethods._
    implicit val formats = DefaultFormats
    val jsonStr = scala.io.Source.fromURL("http://appservices.citibikenyc.com/data2/stations.php").getLines.mkString
    ("\n")
    val json = parse(jsonStr)

    val stations = (json \ "results").extract[List[Station]]
    println(stations.length)
    println(stations)
  }

  @Test
  def parseStation_ScalaUtilParsingJson {
    import scala.util.parsing.json.JSON
    val jsonStr = scala.io.Source.fromURL("http://appservices.citibikenyc.com/data2/stations.php").getLines.mkString("\n")
    val jsonAST: Option[Any] = JSON.parseFull(jsonStr)
    val parsedMap: Map[String, List[Map[String, Any]]] = jsonAST.get.asInstanceOf[Map[String, List[Map[String, Any]]]]
    val results: List[Map[String, Any]] = parsedMap("results")

    val stations: List[Station] = results.map {
      stationMap =>
        Station(stationMap("id").asInstanceOf[Double].toInt,
          stationMap("label").asInstanceOf[String],
          stationMap("latitude").asInstanceOf[Double],
          stationMap("longitude").asInstanceOf[Double],
          stationMap("availableBikes").asInstanceOf[Double].toInt,
          stationMap("availableDocks").asInstanceOf[Double].toInt)
    }
    println(stations)
  }


  @Test
  def extract() {
    import org.json4s.jackson.JsonMethods._
    import org.json4s._
    implicit val formats = DefaultFormats


    val json = parse( """
                         {"results":[
         { "name": "joe",
           "address": {
             "street": "Bulevard",
             "city": "Helsinki"
           },
           "children": [
             {
               "name": "Mary",
               "age": 5,
               "birthdate": "2004-09-04T18:06:22Z"
             },
             {
               "name": "Mazy",
               "age": 3
             }
           ]
         },
                         { "name": "Jane",
                                   "address": {
                                   "street": "Bulevard",
                                   "city": "Helsinki"
                                   },
                                  "children": [
                                    {
                                     "name": "Mary",
                                       "age": 5,
                                       "birthdate": "2004-09-04T18:06:22Z"
                                     },
                                     {
                                       "name": "Mazy",
                                       "age": 3
                                     }
                                   ]
                                 }           ]  }""")

    val person = (json \ "results").extract[List[Person]]
    println(person)
    println(person(1).name)
  }
}

case class Child(name: String, age: Int, birthdate: Option[java.util.Date])

case class Address(street: String, city: String)

case class Person(name: String, address: Address, children: List[Child])

case class Station(id: Int, label: String, latitude: Double, longitude: Double, availableBikes: Int,
                   availableDocks: Int)