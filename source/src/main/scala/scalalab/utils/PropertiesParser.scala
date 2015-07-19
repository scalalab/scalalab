package scalalab.utils

trait PropertiesParser {
  def props: Map[String, String]

  def apply(tag: String, defaultValue: => String): String = {
    props.getOrElse(tag, defaultValue)
  }

  def apply(tag: String, defaultValue: => Int): Int = {
    parseNumber(tag, defaultValue)(_.toInt);
  }

  def apply(tag: String, defaultValue: => Double): Double = {
    parseNumber(tag, defaultValue)(_.toDouble);
  }

  def apply(tag: String, defaultValue: => Long): Long = {
    parseNumber(tag, defaultValue)(_.toLong);
  }

  def apply(tag: String, defaultValue: => Boolean): Boolean = {
    parseNumber(tag, defaultValue)(_.toBoolean);
  }

  private def parse[A](tag: String, defaultValue: A)(parseFn: String => A): A = {
    props.get(tag) match {
      case Some(s) =>
        parseFn(s)
      case None =>
        defaultValue
    }
  }

  private def parseNumber[A](tag: String, defaultValue: A)(parseFn: String => A): A = {
    parse(tag, defaultValue) { s =>
      try {
        parseFn(s)
      } catch {
        case e: NumberFormatException =>
          defaultValue
      }
    }
  }

}
