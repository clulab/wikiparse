package org.clulab.wikiparse

import scala.util.Random
import java.io._
import java.nio.charset.StandardCharsets.UTF_8
import com.typesafe.scalalogging.LazyLogging
import org.apache.commons.io.IOUtils
import org.apache.commons.compress.compressors.CompressorStreamFactory
import ai.lum.common.FileUtils._
import ai.lum.common.RandomUtils._
import org.clulab.processors.clu.CluProcessor
import org.clulab.serialization.json._

object ParseDocuments extends LazyLogging {

  case class WikiDoc(id: String, url: String, title: String, content: String)

  val wikiDocPattern = """(?s)<doc id="(?<id>[^"]+)" url="(?<url>[^"]+)" title="(?<title>[^"]+)">(?<content>.*?)</doc>""".r
  
  val rand = new Random(42)

  def main(args: Array[String]): Unit = {

    if (args.size != 3) {
      sys.error("wrong command line arguments")
    }

    // first command line arg is the input directory
    // second command line arg is the output directory
    val inputDir = new File(args(0)).getCanonicalFile()
    val outputDir = new File(args(1)).getCanonicalFile()

    logger.info("starting processor ...")
    val proc = new CluProcessor
    proc.annotate("this")

    logger.info("parsing ...")


    val sampleSize = args.last.toInt

    val files = inputDir.listFilesByWildcard("*.bz2", recursive = true).toArray

    val subset = if (sampleSize == -1) files else rand.sample[File](files, k = sampleSize, withReplacement=false)

    for {
      file <- subset.par
      wikidoc <- readWikiDocs(file)
    } {
      // annotate doc and serialize to json string
      val doc = proc.annotate(wikidoc.content, keepText = true)
      doc.id = Some(wikidoc.id)
      val json = doc.json()
      // generte output filename
      val outDirStructure = file.getCanonicalPath().replace(inputDir.getCanonicalPath() + "/", "")
      val outputFileDir = new File(outputDir, outDirStructure)
      outputFileDir.mkdirs()
      val outputFile = new File(outputFileDir, s"${wikidoc.id}.json")
      // write output
      logger.info(s"saving ${outputFile} ...")
      outputFile.writeString(json)
      //writeCompressed(outputFile, json)
    }

  }

  def writeCompressed(file: File, content: String): Unit = {
    var os: OutputStream = null
    try {
      os = new CompressorStreamFactory().createCompressorOutputStream(CompressorStreamFactory.BZIP2, file.outputStream)
      os.write(content.getBytes(UTF_8))
    } finally {
      os.close()
    }
  }

  def uncompress(file: File): String = {
    var is: InputStream = null
    try {
      is = new CompressorStreamFactory().createCompressorInputStream(file.inputStream)
      IOUtils.toString(is, UTF_8)
    } finally {
      is.close()
    }
  }

  def prepareContent(title: String, content: String): String = {
    var text = content.trim()
    if (text.startsWith(title)) {
      // remove title from content
      text = text.substring(title.length).trim()
    }
    text
  }

  def readWikiDocs(file: File): Iterator[WikiDoc] = {
    val data = uncompress(file)
    for {
      m <- wikiDocPattern.findAllMatchIn(data)
      id = m.group("id")
      url = m.group("url")
      title = m.group("title")
      content = prepareContent(title, m.group("content"))
      if content.nonEmpty
    } yield WikiDoc(id, url, title, content)
  }

}
