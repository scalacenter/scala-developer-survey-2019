package survey

import com.typesafe.config.ConfigFactory
import survey.Question.{MultipleSelect, SingleSelect, Text}

import scala.collection.immutable.ArraySeq

object Fetch {

  val airtableApiKey    = ConfigFactory.load().getString("airtable.secret")
  val airtableRateLimit = 5 // requests per second
  val airtableDelay     = 1000 / (airtableRateLimit / 2) // milliseconds

  sealed trait State
  case object Done extends State
  case class  Fetching(maybeOffset: Option[String]) extends State

  def fetch(): FormResults = {
    val answers =
      ArraySeq.unfold[Iterable[FilledForm], State](Fetching(None)) {
        case Done => None
        case Fetching(maybeOffset) =>
          val params = maybeOffset match {
            case Some(offset) => Map("offset" -> offset)
            case None => Map.empty
          }
          val response =
            requests.get(
              "https://api.airtable.com/v0/appvYFBtZDFFbKYhp/Survey",
              params = params,
              headers = Map("Authorization" -> s"Bearer $airtableApiKey")
            )
          val json = ujson.read(response.text())
          val maybeNextOffset = json.obj.get("offset")

          val nextState =
            maybeNextOffset match {
              case Some(offset) =>
                Thread.sleep(airtableDelay)
                Fetching(Some(offset.str))
              case None => Done
            }

          val pageRecords = json("records").arr.map(parseJson)
          Some((pageRecords, nextState))
      }.flatten
    FormResults(Questions.all, answers)
  }

  def parseJson(json: ujson.Value): FilledForm =
    FilledForm(
      Questions.all.map { question =>
        val maybeAnswer = json("fields").obj.get(question.title)
        question match {
          case _: Text | _: SingleSelect => maybeAnswer.map(_.str).toList
          case _: MultipleSelect         => maybeAnswer.map(_.arr.map(_.str)).toList.flatten
        }
      }
    )

}
