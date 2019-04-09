name := "wikiparse"
organization := "org.clulab"
scalaVersion := "2.12.6"

libraryDependencies ++= {
	val procVersion = "7.5.1"
	Seq(
    	"ai.lum" %% "common" % "0.0.8",
    	"commons-io" % "commons-io" % "2.6",
    	"org.apache.commons" % "commons-compress" % "1.18",
    	"org.clulab" %% "processors-main" % procVersion,
    	"org.clulab" %% "processors-corenlp" % procVersion,
    	"org.clulab" %% "processors-modelsmain" % procVersion,
    	"org.clulab" %% "processors-modelscorenlp" % procVersion
	)
}
