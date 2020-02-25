package survey

import scala.collection.compat.immutable.ArraySeq
import scala.collection.immutable.SeqMap

case class FormSummary(
  numberOfParticipants: Int,
  values: ArraySeq[QuestionSummary]
)

sealed trait QuestionSummary {
  def title: String
}

object QuestionSummary {

  case class Text(title: String, answers: Map[String, Int]) extends QuestionSummary
  case class SingleSelect(title: String, answers: SeqMap[String, Int]) extends QuestionSummary
  case class MultipleSelect(title: String, answers: SeqMap[String, Int]) extends QuestionSummary

}

trait FormSummarySchema extends endpoints.generic.JsonSchemas {

  implicit val formSummarySchema: JsonSchema[FormSummary] = {
    implicit def seqMapSchema[K : JsonSchema, V : JsonSchema]: JsonSchema[SeqMap[K, V]] =
      arrayJsonSchema[Seq, (K, V)].xmap(_.to(SeqMap))(_.to(Seq))
    implicit val questionSummarySchema: JsonSchema[QuestionSummary] = genericJsonSchema
    genericJsonSchema
  }

}

object FormSummary extends FormSummarySchema with endpoints.ujson.JsonSchemas
