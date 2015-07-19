package scalalab.gui

import javax.swing._

import scalalab.utils.BetterSwing._

class InfoDialog(val title2: String, val text: String) extends JDialog {
  val body = new JTextArea()
  swingLater {
    setSize(400, 400)
    body.setEditable(false)
    val font = new java.awt.Font("Arial", java.awt.Font.BOLD, 18)
    body.setFont(font)
    val textAll = text + " Scala Code Cells allows to execute independently small chunks of Scala Code \n" +
      " each on its own cell\n\n" +
      " Scala Code cells make use of a  Scala interpreter, that is inititialized with the ScalaLab's libraries\n" +
      "You can execute the current code of each cell with SHIFT+Enter \n" +
      "Press CTRL+UP/DOWN to move between cells \n\n\n" +
      "This interface of ScalaLab is adapted from  scalaide of  Benjamin Jackman, http://code.google.com/p/scalide/ \n"


    body.setText(textAll)
    add(body)
    setTitle(title2)
    setModal(false)
    pack
    setVisible(true)
  }
}
