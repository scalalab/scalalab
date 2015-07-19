package scalalab.utils

import javax.swing._
import text._;

object BetterSwing {
  /** Simple wrapper method that runs a fn on the swing thread
    */
  def swingLater[R](fn: => R) {
    SwingUtilities.invokeLater {
      new Runnable() {
        def run {
          fn
        }
      }
    }
  }

  /** Sets the number of tab stops to use for a JTextPane object when the user presses tab
    */
  def setTabStops(tp: JTextPane, charsPerTab: Int) {
    val tWidth: Int = tp.getFontMetrics(tp.getFont).charWidth('w') * charsPerTab
    val tabSet = new TabSet(Array.tabulate[TabStop](25)(i => new TabStop(tWidth * (i + 1))))
    val attributes = new SimpleAttributeSet
    StyleConstants.setTabSet(attributes, tabSet)
    tp.getStyledDocument.setParagraphAttributes(0, tp.getDocument.getLength, attributes, true)
  }
}
