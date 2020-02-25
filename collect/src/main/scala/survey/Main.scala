package survey

object Main {

  /**
   * @param args(0) Path to write the results to
   */
  def main(args: Array[String]): Unit = {
    val targetPath = os.Path(args(0))
    val formResults = Fetch.fetch()
    println(s"Fetched ${formResults.participants.size} answers")
    val jsonContent =
      FormSummary.formSummarySchema.stringCodec.encode(
        Summarize(formResults)
      )
    os.write.over(targetPath, jsonContent, createFolders = true)
  }

}
