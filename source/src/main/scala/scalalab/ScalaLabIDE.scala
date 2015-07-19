package scalalab

import scalaExec.Interpreter._

class scalalab(var args: Array[String]) {

  import scalalab.core.UserMessages._

  //utils.ForkStream.fork(System.out, System.setOut, {x => p ! SysoutMessage(x)})
  //utils.ForkStream.fork(System.err, System.setErr, {x => p ! SyserrMessage(x)})
  println(" Forking Streams")

  import scala.actors.Actor
  import Actor._
  import scalalab.processors.ScalaProcessor
  import scalalab.gui.ScalalabFrame
  import scalalab.utils.Props

  //Load the properties

  case class SetCurrentSaveName(filename: Option[String])

  //Set up the actor for relaying messages back and forth
  lazy val p: Actor = actor {
    try {
      Props.loadProps("scalalab.properties")
    } catch {
      case e: java.io.IOException =>
        System.err.println(e.toString)
    }

    val interp = new ScalaProcessor(p)

    val frame = new ScalalabFrame(p)

    if (args != null)
      if (args.size > 0) {
        p ! LoadFileByName(args(0))
      }

    var currentSaveName: Option[String] = None

    import scalalab.core.InterpreterMessages._
    import javax.swing._
    import java.io.File


    def mkFileChooser(action: (JFileChooser) => Int)(fileHandler: File => Unit) = {
      import javax.swing.filechooser._
      import scalalab.utils.BetterSwing._

      val fc = new JFileChooser
      swingLater {
        fc.addChoosableFileFilter(new FileFilter {
          override def accept(f: File) = {
            f.isDirectory || f.getName.endsWith(".scala") || f.getName.endsWith(".ssci")
          }

          override def getDescription = {
            "ScalaLab  files"
          }
        })
        if (action(fc) == JFileChooser.APPROVE_OPTION) {
          fileHandler(fc.getSelectedFile)
        }
      } // swingLater
      fc
    } // mkFileChooser

    def loadFile(filename: String) {
      {
        println("Loading [" + filename + "]");
        try {
          val data = Some(scala.xml.XML.load(filename))
          p ! SetCurrentSaveName(Some(filename))
          data
        } catch {
          case e =>
            println("Unable to read scalabook file [" + filename + "] " + e.toString)
            None
        }
      }.foreach {
        frame load _
      }
    }

    loop {
      receive {
        case SetCurrentSaveName(filename) =>
          currentSaveName = filename
        case msg: UserMessage =>
          msg match {
            case NewFile() =>
              println(msg)
            case OpenFile() =>
              println(msg)
              actor {
                mkFileChooser(_.showOpenDialog(frame)) {
                  f =>
                    actor {
                      loadFile(f.getAbsolutePath)
                    }
                }
              }
            case LoadFileByName(filename) =>
              actor {
                loadFile(filename)
              }
            case SaveFile() =>
              println(msg)

            case RestartEJML() =>
              interp.restartEJML()

            case RestartMTJ() =>
              interp.restartMTJ()

            case RestartApacheCommons() =>
              interp.restartApacheCommons()

            case Restart() =>
              interp.restart()

            case ShowHelpDialog() =>
              actor {
                new gui.HelpDialog
              }
            case x: SysoutMessage =>
              frame.process(x)
            case x: SyserrMessage =>
              frame.process(x)
            case cmd: ProcessCell =>
              interp.process(cmd)
            case SaveData(data, prompt) =>
              def save(filename: String) {
                try {
                  scala.xml.XML.save(filename, data, "UTF-8", true, null)
                  p ! SetCurrentSaveName(Some(filename))
                } catch {
                  case e =>
                }
              }
              if (currentSaveName.isEmpty || prompt) {
                actor {
                  mkFileChooser(_.showSaveDialog(frame)) {
                    f =>
                      actor {
                        val fn = {
                          val fn = f.getAbsolutePath;
                          if (fn.endsWith(".scala") || fn.endsWith(".ssci")) fn else fn + ".scala"
                        }
                        save(fn)
                      }
                  }
                }
              } else {
                val filename = currentSaveName.get
                actor {
                  save(filename)
                }
              }
          }
        case msg: InterpreterMessage =>
          msg match {
            case res: InterpResult =>
              frame.process(res)
          }
        case
          msg => println("Unhandled Message " + msg)
      }

    }
  }
  //Init the lazy val
  p
}
