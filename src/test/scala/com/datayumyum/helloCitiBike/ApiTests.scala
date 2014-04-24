package com.datayumyum.helloCitiBike

import org.junit.Test
import org.json4s._
import org.json4s.native.JsonMethods._

class ApiTests {

  @Test
  def queryJson() {
    val jsonStr = scala.io.Source.fromURL("http://appservices.citibikenyc.com/data2/stations.php").getLines.mkString("\n")
    val parsedResult = parse(jsonStr)
    val stationIds = for {JArray(stations) <- parsedResult \\ "results"
                          JObject(station) <- stations
                          JField("id", JInt(id)) <- station
    } yield id
    println(stationIds)
  }

  @Test
  def parseStation() {
    implicit val formats = DefaultFormats
    val jsonStr = scala.io.Source.fromURL("http://appservices.citibikenyc.com/data2/stations.php").getLines.mkString("\n")
    val json = parse(jsonStr)

    val stations = (json \ "results").extract[List[Station]]
    println(stations.length)
    println(stations)
  }

  @Test
  def extract() {
    import org.json4s.jackson.JsonMethods._

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

case class Station(id: Int, label: String, latitude: Double, longitude: Double, availableBikes: Int, availableDocks: Int)