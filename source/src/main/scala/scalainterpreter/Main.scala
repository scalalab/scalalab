package scalainterpreter

import java.awt.{EventQueue, GraphicsEnvironment}
import javax.swing.{JFrame, JSplitPane, SwingConstants, WindowConstants}

import scalaExec.Interpreter.GlobalValues

object Main extends Runnable {
  def main(args: Array[String]) {
    EventQueue.invokeLater(this)
  }

  def run {
    val ip = new ScalaInterpreterPane
    GlobalValues.editorPane.setText("") // reset the text of the Scala Interpreter Pane
    // update the variable that keeps the Scala Interpreter Pane interpreter
    GlobalValues.globalInterpreterPane.resetInterpreter(scalaExec.Interpreter.GlobalValues.globalInterpreter)

    val lp = new LogPane
    lp.init
    ip.out = Some(lp.writer)
    Console.setOut(lp.outputStream)
    Console.setErr(lp.outputStream)
    ip.init

    var titleStr = if (GlobalValues.scalaInterpreterTypeProp == GlobalValues.EJMLMat)
      "Scala Interpreter using EJML Library"
    else
      "Scala Interpreter using  JAMAMat library"
    val frame = new JFrame(titleStr)
    val sp = new JSplitPane(SwingConstants.HORIZONTAL)
    sp.setTopComponent(ip)
    sp.setBottomComponent(lp)
    val cp = frame.getContentPane
    cp.add(sp)
    val b = GraphicsEnvironment.getLocalGraphicsEnvironment.getMaximumWindowBounds
    frame.setSize(b.width / 2, b.height * 5 / 6)
    sp.setDividerLocation(b.height * 2 / 3)
    frame.setLocationRelativeTo(null)
    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE)
    frame.setVisible(true)
  }
}