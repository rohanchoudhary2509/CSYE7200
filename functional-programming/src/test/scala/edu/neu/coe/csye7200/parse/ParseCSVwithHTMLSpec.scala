package edu.neu.coe.csye7200.parse

import java.io.FileNotFoundException

import edu.neu.coe.csye7200.Tag
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Source
import scala.util._

class ParseCSVwithHTMLSpec extends FlatSpec with Matchers {

  behavior of "it"
  it should "work" in {
    val parser = ParseCSVwithHTML(CsvParser())
    val resource = "report.csv"
    val title = "Report"
    val wy: Try[Tag] = parseResource(parser, resource, title)
    wy should matchPattern { case Success(_) => }
    wy.get.toString shouldBe
      s"""
         |<html>
         |<head>
         |<title>Report</title></head>
         |<body>
         |<table border="1">
         |<tr>
         |<th>Name</th>
         |<th>Notes</th></tr>
         |<tr>
         |<td>Robin</td>
         |<td><p>This is Robin</p></td></tr>
         |<tr>
         |<td>Nik</td>
         |<td><p><dir>This is Nik</dir></p></td></tr>
         |<tr>
         |<td>Dino</td>
         |<td><table><tr><th>day</th><th>food</th><tr><td>Sunday</td><td>Mousakka</td></table></td></tr>
         |<tr>
         |<td>Kal</td>
         |<td><ol><li>INFO</li><li>CSYE</li></ol></td></tr>
         |<tr></tr></table></body></html>""".stripMargin
  }

  private def parseResource(parser: ParseCSVwithHTML, resource: String, title: String) = Option(getClass.getResource(resource)) match {
    case Some(u) =>
      val source = Source.fromFile(u.toURI)
      val result = parser.parseStreamIntoHTMLTable(source.getLines.toStream, title)
      source.close()
      result
    case None => Failure(new FileNotFoundException(s"cannot get resource $resource"))
  }
}
