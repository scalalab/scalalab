package scalalab.gui

import scalalab.utils.BetterSwing._
import javax.swing._
import javax.swing.event._
import javax.swing.text._
import java.awt._
import java.awt.event._
import scala.actors._
import scalalab.utils.BetterSwing._
import scalalab.utils.Props
import scalalab.core._

// the editor for a particular code cell
// the OuterEditor provides the functionality to interpret, save, etc. cells
class CodeCellEditor(private val outEd: OuterEditor, var isOut: Boolean) extends JTextPane {

  val cell = new CodeCell
  val editorPopup = new JPopupMenu // the popup menu of the cells
  editorPopup.setFont(scalaExec.Interpreter.GlobalValues.puifont)

  // implement a setter for the field isOut
  def isOut_(isOut: Boolean) {
    // true if the cell is for output
    this.isOut = isOut
  }


  // a mouse adapter for the cell's JTextPane based editor
  private class MouseAdapterForEditor extends MouseAdapter {

    override def mousePressed(e: MouseEvent) = {
      if (e.isPopupTrigger()) {
        editorPopup.show(e.getSource().asInstanceOf[Component], e.getX(), e.getY())
      }
    }

    override def mouseReleased(e: MouseEvent) = {
      if (e.isPopupTrigger()) {
        editorPopup.show(e.getSource().asInstanceOf[Component], e.getX(), e.getY())
      }

    }
  }

  swingLater {
    //Binds all the actions that we want
    {
      val cutJMenuItem = new JMenuItem(new DefaultEditorKit.CutAction)
      cutJMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont)
      editorPopup.add(cutJMenuItem)
      val copyJMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction)
      copyJMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont)
      editorPopup.add(copyJMenuItem)
      val pasteJMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction)
      pasteJMenuItem.setFont(scalaExec.Interpreter.GlobalValues.puifont)
      editorPopup.add(pasteJMenuItem)
      addMouseListener(new MouseAdapterForEditor())
      add(editorPopup)

      val keymap = JTextComponent.addKeymap("InnerEditorBindings", getKeymap())

      //Util method for binding an action to a key
      def bindAction(key: Int, mask: Int)(act: => Unit) {
        val keystroke = KeyStroke.getKeyStroke(key, mask)
        val action = new AbstractAction {
          override def actionPerformed(e: ActionEvent) {
            act
          }
        }
        keymap.addActionForKeyStroke(keystroke, action)
      } // bindAction

      import KeyEvent._;
      import Event._;

      //Bind the save action
      bindAction(VK_S, CTRL_MASK) {
        outEd.save(false)
      }

      bindAction(VK_R, CTRL_MASK) {
        outEd.restart
      }

      bindAction(VK_F1, 0) {
        outEd.help
      }

      bindAction(VK_N, CTRL_MASK) {
        outEd.mkCodeCell
      }

      bindAction(VK_DELETE, CTRL_MASK) {
        outEd.delCodeCell
      }

      bindAction(VK_UP, CTRL_MASK) {
        outEd.mvFocusUp
      }

      bindAction(VK_DOWN, CTRL_MASK) {
        //Jump down one cell
        outEd.mvFocusDown
      }

      bindAction(VK_UP, ALT_MASK) {
        //Jump up one cell
        outEd.mvCellUp
      }

      bindAction(VK_DOWN, ALT_MASK) {
        //Jump down one cell
        outEd.mvCellDown
      }

      bindAction(VK_ENTER, SHIFT_MASK) {
        outEd.interpret
      }

      bindAction(VK_ENTER, SHIFT_MASK | CTRL_MASK) {
        outEd.interpretAll
      }
      setKeymap(keymap)
      //Bind the other commands
    }

    addKeyListener(new KeyListener {

      import KeyEvent._;
      import Event._;

      def keyTyped(e: KeyEvent) {
      }

      def keyPressed(e: KeyEvent) {
      }

      def keyReleased(e: KeyEvent) {
      }
    })

    //setFont(new Font(Props("InnerEditor.font.name", "Courier New"), 0, Props("InnerEditor.font.size", 12)))
    setFont(new Font(scalaExec.Interpreter.GlobalValues.paneFontName, Font.PLAIN, scalaExec.Interpreter.GlobalValues
      .paneFontSize))
    setBorder(BorderFactory.createMatteBorder(0, 0, 1, 2, Color.BLUE))
    setTabStops(this, 4)
  }
}
