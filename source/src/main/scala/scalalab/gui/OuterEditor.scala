package scalalab.gui

import javax.swing._
import scalalab.utils.BetterSwing._
import java.awt.{Color, Dimension, Font, BorderLayout}
import java.awt.event.{KeyListener, KeyEvent, FocusListener, FocusEvent}
import scala.actors._
import Actor._
import scalalab.core.InterpreterMessages._
import scalalab.core.UserMessages._
import java.io.{StringReader}
import scalalab.utils.{Props, scalalabCollections}
import scalalab.utils.scalalabCollections._

import scalaExec.Interpreter.GlobalValues

class OuterEditor(listener: Actor) extends JTextPane {

  val version = GlobalValues.scalalabBuildVersion

  //An editor group consists of an editor and the code for it
  class EditorGroup(private val out: Boolean, val text: Option[String], private var commandNum: Option[Int]) {
    def this(isOut: Boolean) = this(isOut, None, None)

    // out cells do not have associatd text and command number
    private val panel = new JPanel
    private val editor = new CodeCellEditor(OuterEditor.this, out)
    private val label = new JLabel()

    def isOut = editor.isOut // an input cell or an output?

    def getText = editor.getText // get the text of the editor

    def getCell = editor.cell

    private def updateLabel() {
      label.setText((if (editor.isOut) "out" else "in") + commandNum.foldRight("")("[" + _ + _ + "]") + "=")
    }

    def setText(text: String, commandNum: Option[Int]) {
      swingLater {
        this.commandNum = commandNum
        editor.setText(text)
        updateLabel()
      }
    }

    def setAsIn(commandNum: Int) {
      this.commandNum = Some(commandNum)
      editor.isOut = false
      updateLabel()
    }

    def grabFocus = this.editor.grabFocus

    def getPanel = this.panel

    swingLater {
      editor.setText(text getOrElse "")
      updateLabel()
      label.setForeground(Color.BLUE)
      label.setFont(new Font(Props("InnerEditor.font.name", "Courier New"), 0, 10))
      label.setOpaque(false)
      panel.setLayout(new BorderLayout)
      panel.add(editor, BorderLayout.CENTER)
      panel.add(label, BorderLayout.WEST)
      editor.addFocusListener {
        new FocusListener {
          def focusLost(e: FocusEvent) {}

          def focusGained(e: FocusEvent) {
            proc ! ChangeFocus(Some(EditorGroup.this))
          }
        }
      }
    }
  }

  // class EditorGroup

  class CodeCellAction()

  case class MoveFocusUp() extends CodeCellAction

  case class MoveFocusDown() extends CodeCellAction

  case class MoveCellUp() extends CodeCellAction

  case class MoveCellDown() extends CodeCellAction

  case class ChangeFocus(ed: Option[EditorGroup])

  def process(res: InterpResult) {
    proc ! res
  }

  def process(cmd: ProcessCell) {
    listener ! cmd
  }

  def start {
    proc ! Refresh()
  }

  def save(promptForFile: Boolean) {
    proc ! Save(promptForFile)
  }

  def help {
    listener ! ShowHelpDialog()
  }

  def restart {
    listener ! Restart()
  }

  def restartEJML {
    listener ! RestartEJML()
  }

  def restartMTJ {
    listener ! RestartMTJ()
  }

  def mkCodeCell {
    proc ! MakeCodeCell()
  }

  def delCodeCell {
    proc ! DeleteCodeCell()
  }

  def mvFocusUp {
    proc ! MoveFocusUp()
  }

  def mvFocusDown {
    proc ! MoveFocusDown()
  }

  def mvCellUp {
    proc ! MoveCellUp()
  }

  def mvCellDown {
    proc ! MoveCellDown()
  }

  def load(xml: scala.xml.Elem) {
    proc ! Load(xml)
  }

  def interpret {
    proc ! InterpretCurrent()
  }

  def interpretAll {
    proc ! InterpretAll()
  }

  case class InterpretCurrent()

  case class InterpretAll()

  case class MakeCodeCell()

  case class DeleteCodeCell()

  case class Refresh()

  case class Save(promptForFile: Boolean)

  case class Load(xml: scala.xml.Elem)


  // the processing actor
  val proc: Actor = actor {
    var editors = List[EditorGroup](new EditorGroup(false))
    var focused: Option[EditorGroup] = Some(editors.head)
    //counter used to mark commands with a number
    var i = 0

    loop {
      def generateSaveXML = {
        (<ScalaLab version={version.toString}>
          {for (ed <- editors) yield {
            (<CodeCell isOut={ed.isOut.toString} isFocus={(focused match {
              case Some(x) => x == ed
              case None => false
            }).toString}>
              {ed.getText}
            </CodeCell>)
          }}
        </ScalaLab>)
      } // generateSaveXML


      def interpret(ed: EditorGroup) = {
        i += 1
        val commandNum = i
        swingLater {
          import scalalab.core.UserMessages._
          val text = ed.getText
          ed.setAsIn(commandNum)
          val command = ProcessCell(ed.getCell, commandNum, text)
          process(command)
        }
        i
      } // interpret

      receive {
        case InterpretCurrent() =>
          focused foreach { edg =>
            edg.setAsIn(interpret(edg))
          }
        case InterpretAll() =>
          editors filter (!_.isOut) foreach {
            interpret _
          }
        case ChangeFocus(ed) =>
          focused = ed
        case Save(prompt) =>
          val s = generateSaveXML
          listener ! SaveData(s, prompt)
        case Load(xml) =>
          val newEds = xml \ "CodeCell" map {
            cell =>
              //determine if this is an out cell, default to false when we have no elements
              val isOut: Boolean = (cell \ "@isOut").foldRight(false) {
                (x, y) =>
                  try {
                    x.text.toBoolean
                  } catch {
                    case e => false
                  }
              }
              val isFocus: Boolean = (cell \ "@isFocus").foldRight(false) {
                (x, y) =>
                  try {
                    x.text.toBoolean
                  } catch {
                    case e => false
                  }
              }
              val ed = new EditorGroup(isOut, Some(cell.text), None)
              if (isFocus) {
                focused = Some(ed)
                swingLater {
                  ed.grabFocus
                }
              }
              ed
          } toList match {
            case Nil =>
            //Do nothing when we parse an empty list
            case xs =>
              editors = xs
          }
          proc ! Refresh()
        case MakeCodeCell() =>
          val newEd = new EditorGroup(false);
          editors = focused match {
            case Some(fed) =>
              editors flatMap {
                _ match {
                  case `fed` =>
                    fed :: newEd :: Nil
                  case ed =>
                    ed :: Nil
                }
              }
            case None =>
              editors ::: newEd :: Nil
          }
          focused = Some(newEd)
          swingLater {
            newEd.grabFocus
          }
          proc ! Refresh()
        case MoveCellUp() =>
        //Not implemented
        case MoveCellDown() =>
        //Not implemented
        case MoveFocusUp() =>
          if (focused.isDefined) {
            justBefore(editors.iterator, focused.get ==).foreach {
              ed =>
                focused = Some(ed)
                swingLater {
                  ed.grabFocus
                }
            }
          }
        case MoveFocusDown() =>
          if (focused.isDefined) {
            justAfter(editors.iterator, focused.get ==).foreach {
              ed =>
                focused = Some(ed)
                swingLater {
                  ed.grabFocus
                }
            }
          }
        case DeleteCodeCell() =>
          editors = editors.span(ed => !focused.exists(fed => ed == fed)) match {
            case (Nil, _ :: Nil) | (_, Nil) =>
              //This would leave us with too few editors,
              //or we do not have a focus that matches
              editors
            case (Nil, y0 :: y1 :: ys) =>
              //Erasing the first cell
              focused = Some(y1)
              y1 :: ys
            case (xs, y :: ys) =>
              //Erasing any cell after the first
              focused = Some(xs.last)
              xs ::: ys
          }
          proc ! Refresh()
        case res: InterpResult =>
          var foundIt = false;
          var insertedIt = false;
          def mkNew = new EditorGroup(true, Some(res.text), Some(res.cmd.requestId))
          editors = editors.flatMap {
            ed =>
              if (insertedIt) {
                //Return just the editor after we have already inserted
                ed :: Nil
              } else if (foundIt) {
                //Now we are going to insert the editor since
                //we have just found the old one
                insertedIt = true
                if (ed.isOut) {
                  swingLater {
                    ed.setText(res.text, Some(res.cmd.requestId))
                  }
                  ed :: Nil
                } else {
                  mkNew :: ed :: Nil
                }
              } else {
                if (ed.getCell == res.cmd.cell) {
                  foundIt = true
                }
                ed :: Nil
              }
          }
          if (!insertedIt) {
            val newEd = new EditorGroup(false)
            editors = editors ::: mkNew :: newEd :: Nil
            focused = Some(newEd)
          }
          //Be sure to refresh the view after we change the list
          proc ! Refresh()
        case Refresh() =>
          val eds = editors
          swingLater {
            //Clear this window
            setText("")
            //Set the caret to the end of the document
            setCaretPosition(getDocument.getLength)
            eds.foreach {
              ed =>
                insertComponent(ed.getPanel)
                getEditorKit.read(new StringReader("\n"), getDocument(), getDocument().getLength())
            }
            //Focus on the active editor
            focused.foreach(_.grabFocus)
          }
      } // receive
    } // loop
  } // proc actor

  swingLater {
    setEditable(false)
    setForeground(Color.BLUE)
    setFont(new Font("Consolas", 0, 12))
    /*
    addKeyListener(new KeyListener {
      def keyTyped(e : KeyEvent) {
        e.consume
      }
      def keyPressed(e : KeyEvent) {
        e.consume
      }
      def keyReleased(e : KeyEvent) {
        e.consume
      }
    })
     */
    proc ! Refresh()
  }
}

