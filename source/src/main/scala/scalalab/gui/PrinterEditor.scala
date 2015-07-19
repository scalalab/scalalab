package scalalab.gui

import javax.swing._
import scalalab.core.UserMessages._
import scalalab.utils.BetterSwing._
import java.io._

class PrinterEditor extends JTextPane {
  def display(s: String) {
    swingLater {
      setCaretPosition(getDocument.getLength)
      getEditorKit.read(new StringReader(s), getDocument(), getDocument().getLength())
    }
  }

  def process(msg: SysoutMessage) {
    display(msg.msg)
  }

  def process(msg: SyserrMessage) {
    display(msg.msg)
  }

  def clear {
    swingLater {
      setText("")
    }
  }
}
