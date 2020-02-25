package survey

import scala.collection.immutable.ArraySeq
import Question.{MultipleSelect, SingleSelect, Text}

object Questions {

  val all = ArraySeq(
    SingleSelect("How long have you been using Scala?", List(
      "Less than 1 year",
      "Between 1 year and 3 years",
      "More than 3 years"
    )),
    MultipleSelect("How are you using Scala", List(
      "At my company",
      "For personal projects",
      "As a teaching vehicle",
      "As a library author/maintainer"
    )),
    MultipleSelect("Which Scala versions do you use?", List(
      "2.10",
      "2.11",
      "2.12",
      "2.13",
      "Dotty"
    )),
    MultipleSelect("Which JDKs do you use?", List(
      "OracleJDK",
      "GraalVM",
      "OpenJDK",
      "Azul Zulu"
    )),
    MultipleSelect("Which versions of Java do you use?", List(
      "8",
      "11",
      ">11"
    )),
    MultipleSelect("Which Scala-related command-line tools do you have installed on your development environment?", List(
      "sbt",
      "scalafmt",
      "scalafix",
      "mill",
      "ammonite",
      "bloop",
      "coursier"
    )),
    MultipleSelect("Which execution platforms do you target?", List(
      "JVM",
      "Native",
      "JS"
    )),
    MultipleSelect("Which build tools do you use?", List(
      "sbt",
      "mill",
      "pants",
      "bazel",
      "maven",
      "cbt",
      "seed",
      "fury",
      "gradle"
    )),
    MultipleSelect("Which code editors do you use?", List(
      "IntelliJ",
      "Metals + VS Code",
      "Metals + another text editor",
      "A text editor (vim, emacs, etc.)",
      "Eclipse"
    )),
    MultipleSelect("Which tools do you use to analyze/enforce code quality?", List(
      "-Yfatal-warnings compiler option",
      "WartRemover",
      "scalafix",
      "Codacy",
      "Scapegoat"
    )),
    MultipleSelect("In which application domains do you use Scala?", List(
      "Data science",
      "Other type of backend",
      "Web frontend",
      "Research",
      "Other",
      "Microservices",
      "Streaming data"
    )),
    MultipleSelect("How did you learn Scala?", List(
      "Written material (books, blogs, tutorial, …)",
      "Online courses",
      "On the job",
      "At the university"
    )),
    MultipleSelect("What are the main pain points in your daily workflow?", List(
      "Long compilation times",
      "Handling type errors",
      "Handling missing implicit errors",
      "Editing code (code fixes, assistance, refactorings)",
      "Navigating through code",
      "Debugging complex logic",
      "Other (see below)"
    )),
    Text("What are the *other* pain points in your daily workflow?"),
    MultipleSelect("What are the main pain points to get started (ie setup a development environment) in Scala?", List(
      "JDK setup",
      "Build tool setup",
      "Editor setup",
      "Other (see below)"
    )),
    Text("What are the *other* pain points to get started in Scala?"),
    MultipleSelect("What are the main pain points in using/integrating multiple libraries into a stack?", List(
      "Aligning dependencies to avoid binary incompatibilities",
      "Upgrading to new versions",
      "Finding the “coordinates” of a library",
      "Finding which Scala versions and platforms are supported by a library",
      "Other (see below)"
    )),
    Text("What are the *other* pain points in using/integrating multiple libraries?"),
    MultipleSelect("What are the main pain points related to implicits?", List(
      "Syntax is confusing",
      "Mechanism is unclear",
      "Fixing “implicit not found” errors is hard",
      "Other (see below)",
      "Finding which parameters have been inferred is hard"
    )),
    Text("What are the *other* pain points related to implicits?"),
    MultipleSelect("What are your expectations with regards to Scala 3?", List(
      "Less warts",
      "More powerful language features",
      "More concise syntax",
      "Reduced compilation times",
      "Better error messages",
      "Other (see below)"
    )),
    Text("What are your *other* expectations with regards to Scala 3?"),
    MultipleSelect("What are your fears with regards to Scala 3?", List(
      "Migration cost",
      "New syntax",
      "Tooling support",
      "Other (see below)"
    )),
    Text("What are your *other* fears with regards to Scala 3?")
  )

}
