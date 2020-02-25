package amcharts

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("@amcharts/amcharts4/plugins/wordCloud", JSImport.Namespace)
@js.native
object am4plugins_wordCloud extends js.Any {

  val WordCloud: am4charts.ChartType = js.native

}

@JSImport("@amcharts/amcharts4/plugins/wordCloud", "WordCloudSeries")
@js.native
class WordCldoudSeries extends Series
