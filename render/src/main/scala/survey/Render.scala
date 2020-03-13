package survey

import amcharts.{CategoryAxis, ColumnSeries, PieSeries, ValueAxis, WordCldoudSeries, am4charts, am4core, am4plugins_wordCloud}
import org.scalajs.dom

import scala.scalajs.js
import scala.util.{Failure, Success}
import scalajs.concurrent.JSExecutionContext.Implicits.queue

object Render {

  def main(args: Array[String]): Unit = {
    Endpoints.formResults(()).onComplete {
      case Failure(exception) =>
        dom.console.error(s"Unable to load the survey summary: $exception")
        dom.window.alert("Unable to load the survey summary")
      case Success(formSummary) =>
        dom.document.querySelector("article") match {
          case null => dom.window.alert("Unable to render the summary")
          case node =>
            node.removeChild(node.firstChild)
            node.appendChild(renderForm(formSummary))
        }
    }
  }

  def renderForm(formSummary: FormSummary): dom.Node = {
    import scalatags.JsDom.short._
    frag(
      p(s"${formSummary.numberOfParticipants} people have filled the 2019 Scala Developer Survey.") +:
      formSummary.values.map(renderQuestion(_, formSummary.numberOfParticipants)): _*
    ).render
  }

  def renderQuestion(questionSummary: QuestionSummary, numberOfParticipants: Int) = {
    import scalatags.JsDom.short._
    val chartNode =
      questionSummary match {
        case QuestionSummary.Text(_, answers) =>
          val node = div(*.style := "height: 25em;").render
          val chart = am4core.create(node, am4plugins_wordCloud.WordCloud)
          val series = chart.series.push(new WordCldoudSeries)
          series.text =
            answers
              .flatMap { case (word, occurrences) => List.fill(occurrences)(word) }
              .mkString(" ")
          node
        case QuestionSummary.SingleSelect(_, answers) =>
          val node = div(*.style := "height: 20em;").render
          val chart = am4core.create(node, am4charts.PieChart)
          chart.data = js.Array(answers.toSeq.map { case (option, occurrences) =>
            new PieChartItem(option, occurrences)
          }: _*)
          val pieSeries = chart.series.push(new PieSeries)
          js.Dictionary
          pieSeries.dataFields.category = "option"
          pieSeries.dataFields.value = "occurrences"
          node
        case QuestionSummary.MultipleSelect(_, answers) =>
          val node = div(*.style := "height: 25em;").render
          val chart = am4core.create(node, am4charts.XYChart)
          chart.data = js.Array(answers.toSeq.map { case (option, occurrences) =>
            new XYChartItem(option, occurrences, occurrences.toDouble * 100 / numberOfParticipants)
          }: _*)
          val categoryAxis = chart.xAxes.push(new CategoryAxis)
          categoryAxis.dataFields.category = "option"
          val untypedCategoryAxis = categoryAxis.asInstanceOf[js.Dynamic]
          untypedCategoryAxis.events.on("sizechanged", (ev: js.Dynamic) => {
            val axis = ev.target
            val cellWidth = axis.pixelWidth / (axis.endIndex - axis.startIndex)
            axis.renderer.labels.template.maxWidth = cellWidth
          })
          val label = untypedCategoryAxis.renderer.labels.template
          label.wrap = true
          label.tooltipText = "{categoryX}"
          untypedCategoryAxis.renderer.grid.template.location = 0
          chart.yAxes.push(new ValueAxis)
          val series = chart.series.push(new ColumnSeries)
          series.dataFields.valueY = "occurrences"
          series.dataFields.categoryX = "option"
          series.columns.template.tooltipText = "{categoryX}: {percentageOfParticipants.formatNumber('###.0')}% ({valueY})"
          node
      }

    val id =
      questionSummary
        .title
        .stripSuffix("?")
        .toLowerCase
        .replaceAllLiterally(" ", "-")

    frag(
      h2(*.id := id, questionSummary.title, " ", a(*.href := s"#$id", "¶")),
      chartNode
    )
  }

}

class PieChartItem(val option: String, val occurrences: Int) extends js.Object

class XYChartItem(val option: String, val occurrences: Int, val percentageOfParticipants: Double) extends js.Object
