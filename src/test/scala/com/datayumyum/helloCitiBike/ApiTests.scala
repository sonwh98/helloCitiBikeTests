package com.datayumyum.HelloCitiBike

import argonaut._, Argonaut._
import scalaz._, Scalaz._


import org.junit.Test

class ApiTests {

  @Test
  def json() {
    val jsonStr = scala.io.Source.fromURL("http://appservices.citibikenyc.com/data2/stations.php").getLines.mkString("\n")
    println(jsonStr)

  }

  @Test
  def quickstart() {
    val input = """
    [
      { "name": "Mark", "age": 191 },
      { "name": "Fred", "age": 33, "greeting": "hey ho, lets go!" },
      { "name": "Barney", "age": 35, "address": {
        "street": "rock street", "number": 10, "post_code": 2039
      }}
    ]
                """

    // parse the string as json, attempt to decode it to a list of person,
    // otherwise just take it as an empty list.
    val people = input.decodeOption[List[Person]].getOrElse(Nil)

    // work with your data types as you normally would
    val nice = people.map(person =>
      person.copy(greeting = person.greeting.orElse(Some("Hello good sir!"))))

    // convert back to json, and then to a pretty printed string, alternative
    // ways to print may be nospaces, spaces2, or a custom format

    val result = nice.asJson
    println(result.spaces4)

    assert(result.array.exists(_.length == 3))
  }
}


case class Address(street: String, number: Int, postcode: Int)

object Address {
  // Define codecs easily from case classes
  implicit def AddressCodecJson: CodecJson[Address] =
    casecodec3(Address.apply, Address.unapply)("street", "number", "post_code")
}

case class Person(name: String, age: Int, address: Option[Address], greeting: Option[String])

object Person {
  implicit def PersonCodecJson: CodecJson[Person] =
    casecodec4(Person.apply, Person.unapply)("name", "age", "address", "greeting")
}