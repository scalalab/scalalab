package scalalab.utils

object Props extends PropertiesParser {
  var props: Map[String, String] = Map()

  /* Throws FileNotFoundException if the file was not found
   * Throws IOException on other errors
   */
  def loadProps(file: String) {
    import scala.io.Source
    import scala.util.matching.Regex
    val setting = new Regex( """\s*(.*)=\s*(.*)\s*""", "key", "value")
    def parse(line: String): Option[Pair[String, String]] = {
      line match {
        case `setting`(key, value) =>
          Some((key, value))
        case _ =>
          None
      }
    }

    //try {
    props = Map("InnerEditor.font.name" -> "Courier New", "InnerEditor.font.size" -> "12")
    /*for (val line <- Source.fromFile(new java.io.File(file)).getLines()) {
      parse(line) match {
      case Some(entry) =>
        props = props + entry
      case None =>
      }
    }*/


    //}
  }
}
