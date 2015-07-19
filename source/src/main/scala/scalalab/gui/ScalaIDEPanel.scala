package scalalab.gui


import javax.swing._
import java.awt.{Color, Font, Dimension, BorderLayout}
import scala.actors._
import Actor._
import scalalab.utils._
import scalalab.core.InterpreterMessages._
import scalalab.core.UserMessages._

class ScalaidePanel(private val p: Actor) extends JPanel {

  import BetterSwing._

  val editor = new OuterEditor(p)
  val printer = new PrinterEditor()

  private[scalalab] val proc = actor {

    loop {
      receive {
        // receive a GUITask and process it
        case GUITask(fn) =>
          SwingUtilities.invokeLater(
            new Runnable() {
              def run {
                fn()
              }
            }
          )
        case res: InterpResult =>
          println("processing result = " + res.toString)
          editor process res
        case msg: SysoutMessage =>
          printer.process(msg)
        case msg: SyserrMessage =>
          printer.process(msg)
      }
    }
  }

  def process(res: InterpResult) {
    proc ! res
  }

  def process(msg: SysoutMessage) {
    proc ! msg
  }

  def process(msg: SyserrMessage) {
    proc ! msg
  }

  def load(data: scala.xml.Elem) {
    editor.load(data)
  }

  private def guiTask(task: => Unit) {
    proc ! GUITask(() => task)
  }

  //On startup add the gui task into the processor
  guiTask {

    add(new JScrollPane(editor))
    editor.start
  }

  private case class GUITask(fn: () => Unit)

}
