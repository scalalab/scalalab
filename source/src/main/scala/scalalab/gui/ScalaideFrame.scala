package scalalab.gui


import javax.swing._
import java.awt.{Color, Font, Dimension, BorderLayout}
import scala.actors._
import Actor._
import scalalab.utils._
import scalalab.core.InterpreterMessages._
import scalalab.core.UserMessages._

import scalaExec.Interpreter._


// p is the processing actor
class ScalalabFrame(private val p: Actor) extends JFrame {

  import BetterSwing._

  val editor = new OuterEditor(p)
  val printer = new PrinterEditor()


  private[scalalab] val proc = actor {

    loop {
      // actor loop
      receive {
        // receive a GUITask and process it
        case GUITask(fn) =>
          SwingUtilities.invokeLater(
            new Runnable() {
              def run {
                fn()
              }
            } // runnable
          )
        case res: InterpResult => // editor processes Interpreter results
          editor process res
        case msg: SysoutMessage => // printer processes SysoutMessage and SyserrMessage
          printer.process(msg)
        case msg: SyserrMessage =>
          printer.process(msg)
      } // receive
    } // actor loop
  } // actor

  // processing is performed by delegating to the proc actor
  def process(res: InterpResult) {
    proc ! res
  }

  def process(msg: SysoutMessage) {
    proc ! msg
  }

  def process(msg: SyserrMessage) {
    proc ! msg
  }

  private def guiTask(task: => Unit) {
    proc ! GUITask(() => task)
  }


  def load(data: scala.xml.Elem) {
    editor.load(data)
  }

  //On startup add the gui task into the processor
  guiTask {
    def mkMenuBar = {
      import scalalab.core.UserMessages._
      val mb = new MenuBar {
        new Menu("State") {
          "Print Cells Interpreter State" does {

          }
        }


        new Menu("File") {
          "New" does {
            p ! NewFile()
          }
          "Open File..." does {
            p ! OpenFile()
          }
          ---
          "Save File                Ctrl+S" does {
            editor.save(false)
          }
          "Save As ..." does {
            editor.save(true)
          }
        }
        new Menu("Interpreter") {
          "Interpret               Shift+Enter" does {
            editor.interpret
          }
          "Interpret All      Ctrl+Shift+Enter" does {
            editor.interpretAll
          }
          "Restart with default libraries                     Ctrl+R" does {
            p ! Restart()
          }
          "Restart with EJML  based libraries " does {
            p ! RestartEJML()
          }
          "Restart with MTJ  based libraries " does {
            p ! RestartMTJ()
          }
          "Restart with Apache Common Maths  based libraries " does {
            p ! RestartApacheCommons()
          }
        }
        new Menu("CodeCell") {
          "New                          Ctrl+N" does {
            editor.mkCodeCell
          }
          "Delete                     Ctrl+Del" does {
            editor.delCodeCell
          }
        }
        new Menu("Help") {
          "Contents                         F1" does {
            p ! ShowHelpDialog()
          }
        }
      }
      mb.setFont(new Font(scalalab.utils.Props("InnerEditor.font.name", "Courier New"), 0, 10))
      mb
    }

    //Set up the frame
    setJMenuBar(mkMenuBar)
    setTitle("ScalaLab Code Cells  Build:  " + scalaExec.Interpreter.GlobalValues.scalalabBuildVersion)
    //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    //val splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT)

    //splitPane.setTopComponent(new JScrollPane(editor))
    //splitPane.setBottomComponent(new JScrollPane(printer))
    setContentPane(new JScrollPane(editor))


    setLocation(GlobalValues.locX, GlobalValues.locY)
    setSize((0.5 * GlobalValues.sizeX).asInstanceOf[Int], GlobalValues.sizeY)

    //pack
    setVisible(true)
    //Have to set size after making the frame visible
    // splitPane.setDividerLocation(900)
    editor.start // start the editor in order to be able to edit cells
  }

  // guiTask

  private case class GUITask(fn: () => Unit)

}
