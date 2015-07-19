package scalalab.core

object UserMessages {

  sealed abstract class UserMessage()

  case class NewFile() extends UserMessage

  case class OpenFile() extends UserMessage

  case class LoadFileByName(filename: String) extends UserMessage

  case class SaveFile() extends UserMessage

  case class Restart() extends UserMessage

  case class RestartMTJ() extends UserMessage

  case class RestartEJML() extends UserMessage

  case class RestartApacheCommons() extends UserMessage

  case class ShowHelpDialog() extends UserMessage

  case class ProcessCell(cell: CodeCell, requestId: Int, text: String) extends UserMessage

  //The data that we use to actually save
  case class SaveData(xml: scala.xml.Elem, promptForName: Boolean) extends UserMessage

  case class SysoutMessage(msg: String) extends UserMessage

  case class SyserrMessage(msg: String) extends UserMessage

}