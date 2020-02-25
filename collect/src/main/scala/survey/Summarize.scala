package survey

import scala.collection.immutable.SeqMap

object Summarize {

  def apply(formResults: FormResults): FormSummary = {

    def occurrences(options: List[String], questionIndex: Int): SeqMap[String, Int] =
      options.map { option =>
        val occurrences =
          formResults.participants.map { answers =>
            answers.values(questionIndex).count(_ == option)
          }.sum
        option -> occurrences
      }.to(SeqMap)

    val questionSummaries = formResults.questions.zipWithIndex.map {
      case (Question.MultipleSelect(title, options), i) =>
        QuestionSummary.MultipleSelect(title, occurrences(options, i))
      case (Question.SingleSelect(title, options), i) =>
        QuestionSummary.SingleSelect(title, occurrences(options, i))
      case (Question.Text(title), i) =>
        val answers =
          formResults
            .participants
            .flatMap { answers =>
              answers.values(i)
                .view
                .flatMap(_.split("\\s|\\p{Punct}"))
                .map(_.trim.toLowerCase)
                .filter(_.length > 2)
                .filterNot(excludedWords)
                // ad-hoc normalization
                .map {
                  case "dependencies" => "dependency"
                  case "errors"       => "error"
                  case "implicits"    => "implicit"
                  case "imports"      => "import"
                  case "indentations" => "indentation"
                  case "libraries"    => "library"
                  case "macros"       => "macro"
                  case "styles"       => "style"
                  case "types"        => "type"
                  case other => other
                }
                // Special case...
                .filter { word =>
                  if (title == "What are the *other* pain points related to implicits?")
                    word != "implicit"
                  else if (title == "What are the *other* pain points in using/integrating multiple libraries?")
                    word != "library"
                  else
                    true
                }
            }
            .groupMapReduce(identity)(_ => 1)(_ + _)
            .filter(_._2 > 5 /* occurrences */)
        QuestionSummary.Text(title, answers)
    }
    FormSummary(formResults.participants.size, questionSummaries)
  }

  val excludedWords = Set(
    "the", "with", "let", "are", "for", "and", "not", "but", "many", "that", "you",
    "all", "don", "can", "they", "also", "have", "from", "need", "better", "between",
    "very", "about", "this", "lots", "lot", "just", "would", "feels", "like", "has",
    "when", "lack", "ways", "different", "using", "best", "across", "other", "too",
    "used", "things", "due", "thing", "idea", "more", "hard", "use", "sometimes",
    "writing", "multi", "etc", "most", "non", "out", "bad", "much", "should", "lack",
    "right", "make"
  )

}
