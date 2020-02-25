package amcharts

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@amcharts/amcharts4/charts", JSImport.Namespace)
@js.native
object am4charts extends js.Any {

  type ChartType

  val PieChart: ChartType = js.native
  val XYChart: ChartType = js.native

}

@js.native
trait Chart extends js.Any {
  var data: js.Array[js.Object] = js.native
  val series: Am4List[Series] = js.native
  val xAxes: Am4List[Axis] = js.native
  val yAxes: Am4List[Axis] = js.native
}

@js.native
trait Series extends js.Any {
  val dataFields: DataFields = js.native
  var columns: Columns = js.native
  var calculatePercent: Boolean = js.native
  var text: String = js.native
}

@JSImport("@amcharts/amcharts4/charts", "PieSeries")
@js.native
class PieSeries extends Series

@JSImport("@amcharts/amcharts4/charts", "ColumnSeries")
@js.native
class ColumnSeries extends Series

@js.native
trait DataFields extends js.Object {
  var value: String = js.native
  var category: String = js.native
  var valueY: String = js.native
  var categoryX: String = js.native
}

@js.native
trait Axis extends js.Any {
  var dataFields: DataFields = js.native
}

@JSImport("@amcharts/amcharts4/charts", "CategoryAxis")
@js.native
class CategoryAxis extends Axis

@JSImport("@amcharts/amcharts4/charts", "ValueAxis")
@js.native
class ValueAxis extends Axis

@js.native
trait Columns extends js.Any {
  val template: Template = js.native
}

@js.native
trait Template extends js.Any {
  var tooltipText: String = js.native
}
