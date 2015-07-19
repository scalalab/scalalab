package scalalab.core


object InterpreterMessages {

  import UserMessages._

  sealed abstract class InterpreterMessage()

  case class InterpResult(cmd: ProcessCell, text: String) extends InterpreterMessage

}
