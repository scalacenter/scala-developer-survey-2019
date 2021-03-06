package survey

import endpoints.xhr

object Endpoints extends xhr.future.Endpoints with xhr.JsonEntitiesFromSchemas with FormSummarySchema {

  val formResults = endpoint(
    get(path / "scala-developer-survey-2019" / "results.json"),
    ok(jsonResponse[FormSummary])
  )

}
