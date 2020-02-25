import org.scalajs.sbtplugin.Stage
import sbtcrossproject.CrossPlugin.autoImport.{CrossType, crossProject}

inThisBuild(Seq(
  scalaVersion := "2.13.1"
))

// Data types shared by both the `collect` and `render` projects
val shared =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .in(file("shared"))
    .settings(
      libraryDependencies ++= Seq(
        "org.julienrf" %%% "endpoints-openapi" % "0.14.0"
      )
    )

// Program collecting survey results and turning them into data that can be rendered
// on a web page
val collect =
  project.in(file("collect"))
    .settings(
      libraryDependencies ++= Seq(
        "com.lihaoyi" %% "requests" % "0.4.7",
        "com.lihaoyi" %% "upickle" % "0.9.5",
        "com.lihaoyi" %% "os-lib" % "0.6.2",
        "com.typesafe" % "config" % "1.4.0"
      ),
    ).dependsOn(shared.jvm)

// Program rendering the data produced by the `collect` program
val render =
  project.in(file("render"))
    .enablePlugins(ScalaJSBundlerPlugin)
    .settings(
      libraryDependencies ++= Seq(
        "org.julienrf" %%% "endpoints-xhr-client" % "0.14.0",
        "com.lihaoyi" %%% "scalatags" % "0.8.2"
      ),
      scalaJSUseMainModuleInitializer := true,
      Compile / npmDependencies += "@amcharts/amcharts4" -> "4.9.0",
      Compile / npmDevDependencies += "uglifyjs-webpack-plugin" -> "2.2.0",
      Compile / scalacOptions += "-P:scalajs:sjsDefinedByDefault",
      useYarn := true,
      fullOptJS / webpackConfigFile := Some(baseDirectory.value / "prod.webpack.config.js"),
      Compile / fullOptJS / scalaJSLinkerConfig ~= { _.withSourceMap(false) }
    ).dependsOn(shared.js)

val `developer-survey-2019` =
  project.in(file("."))
    .enablePlugins(GhpagesPlugin)
    .settings(
      git.remoteRepo := "git@github.com:scalacenter/developer-survey-2019",
      mappings in makeSite ++= Def.taskDyn {
        val resultsFileName = "results.json"
        val resultsPath = (collect / target).value / resultsFileName
        val renderAssets = (render / Compile / fullOptJS / webpack).value
        val renderBundle =
          renderAssets
            .find(_.metadata(BundlerFileTypeAttr) == BundlerFileType.ApplicationBundle)
            .getOrElse(throw new MessageOnlyException("Unable to find the application bundle"))
        val indexFile = target.value / "index.html"
        IO.write(
          indexFile,
          s"""<!DOCTYPE html>
             |<html>
             |  <head>
             |    <meta charset="utf-8">
             |    <meta name=viewport content="width=device-width, initial-scale=1">
             |    <title>2019 Scala Developer Survey</title>
             |    <script src="app.js" defer></script>
             |    <style>
             |      h1, h2 { color: #1473A1; }
             |      html { background-color: #E5F6FD; font-family: sans }
             |      body { margin-left: 1em; margin-right: 1em; }
             |    </style>
             |  </head>
             |  <body>
             |    <h1>2019 Scala Developer Survey</h1>
             |    <article>Loading…</article>
             |  </body>
             |</html>""".stripMargin
        )
        Def.task {
          val _ = (collect / Compile / run).toTask(" " + resultsPath.absolutePath).value
          Seq(
            resultsPath -> resultsFileName,
            renderBundle.data -> "app.js",
            indexFile -> "index.html"
          )
        }
      }.value
    )

Global / onChangedBuildSource := ReloadOnSourceChanges
