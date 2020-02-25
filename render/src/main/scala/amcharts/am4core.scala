package amcharts

import amcharts.am4charts.ChartType
import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@amcharts/amcharts4/core", JSImport.Namespace)
@js.native
object am4core extends js.Any {

  def create(node: dom.Node, chart: ChartType): Chart = js.native

}

@js.native
trait Am4List[A] extends js.Any {
  def push(value: A): A
}

