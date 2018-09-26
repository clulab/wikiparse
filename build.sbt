name := "wikiparse"
organization := "org.clulab"
scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
    "ai.lum" %% "common" % "0.0.8",
    "commons-io" % "commons-io" % "2.6",
    "org.apache.commons" % "commons-compress" % "1.18",
    "org.clulab" %% "processors-main" % "7.4.2",
    "org.clulab" %% "processors-corenlp" % "7.4.2",
    "org.clulab" %% "processors-modelsmain" % "7.4.2",
    "org.clulab" %% "processors-modelscorenlp" % "7.4.2"
)
