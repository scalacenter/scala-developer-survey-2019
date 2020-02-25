package survey

import scala.collection.immutable.ArraySeq

sealed trait Question {
  def title: String
}

object Question {

  case class Text(title: String) extends Question
  case class SingleSelect(title: String, options: List[String]) extends Question
  case class MultipleSelect(title: String, options: List[String]) extends Question

}

// The answers of one participant. One answer per question, each answer
// is a `List[String]`
case class FilledForm(values: ArraySeq[List[String]])

// The form and all the answers (from all the participants)
case class FormResults(questions: ArraySeq[Question], participants: ArraySeq[FilledForm]) {
  assert(participants.forall(_.values.sizeIs == questions.size))
}
