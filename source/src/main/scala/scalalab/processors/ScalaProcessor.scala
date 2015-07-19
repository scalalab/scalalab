package scalalab.processors

import scala.actors.{InputChannel, OutputChannel}
import scalalab.core.UserMessages._
import scalalab.core.InterpreterMessages._
import javax.swing.JFrame
import scala.actors.Actor
import Actor._
import java.io.{PrintWriter, PipedWriter, PipedReader}
import scala.tools.nsc.Interpreter

import scalaExec.Interpreter._


class ScalaProcessor(private val p: Actor) {

  import ScalaProcessor._

  import scala.tools.nsc.{Interpreter, Settings}


  case class ResultText(res: String)

  case class Restart()

  case class RestartEJML()

  case class RestartMTJ()

  case class RestartApacheCommons()

  private[scalalab] def restart() {
    commandProc ! Restart
  }

  private[scalalab] def restartEJML() {
    commandProc ! RestartEJML
  }

  private[scalalab] def restartMTJ() {
    commandProc ! RestartMTJ
  }

  private[scalalab] def restartApacheCommons() {
    commandProc ! RestartApacheCommons
  }


  private[scalalab] def process(cmd: ProcessCell) {
    commandProc ! cmd
  }


  //This actor processes all commands that come across
  //from the interpreter
  private val commandProc = actor {
    // matrixType controls which library to initialize
    class InterpWrapper(matrixType: Int) {
      private val pipe = new PipedWriter
      private val writer = new PrintWriter(pipe)

      private val IDESettings = new Settings


      // detect the paths of the core ScalaLab jars at the local file system
      _root_.scalalab.JavaUtilities.detectPaths


      // make the classpath settable by user
      scalalab.JavaUtilities.setByUser(IDESettings);
      //scalalab.JavaUtilities.useJavaCP(IDESettings);


      IDESettings.classpath.append(scalalab.JavaGlobals.jarFilePath) // the Scalalab .jar file by itself
      IDESettings.classpath.append(scalalab.JavaGlobals.compFile) // Scala Compiler
      IDESettings.classpath.append(scalalab.JavaGlobals.libFile) // Scala Libraries
      IDESettings.classpath.append(scalalab.JavaGlobals.reflectFile) // scala-reflect file
      IDESettings.classpath.append(scalalab.JavaGlobals.scalaActorsFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.actorsMigrationFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.akkaActorsFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.xmlScalaFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.parserCombinatorsFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.swingFile) // Scala Swing
      IDESettings.classpath.append(scalalab.JavaGlobals.jfreechartFile) // ScalaLab JFreeChart library
      IDESettings.classpath.append(scalalab.JavaGlobals.numalFile) // NUMAL library
      IDESettings.classpath.append(scalalab.JavaGlobals.mtjColtSGTFile) // MTJ, Colt, SGT libraries
      IDESettings.classpath.append(scalalab.JavaGlobals.ApacheCommonsFile) // Apache commons file - the current version
      IDESettings.classpath.append(scalalab.JavaGlobals.jsciFile) // Jsci library
      IDESettings.classpath.append(scalalab.JavaGlobals.javacppFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.gslFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.ejmlFile) // EJML file
      IDESettings.classpath.append(scalalab.JavaGlobals.rsyntaxTextAreaFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.matlabScilabFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.jblasFile) // JBLAS File
      IDESettings.classpath.append(scalalab.JavaGlobals.LAPACKFile) // LAPACK linear algebra library
      IDESettings.classpath.append(scalalab.JavaGlobals.ARPACKFile)
      IDESettings.classpath.append(scalalab.JavaGlobals.JASFile) // Java Algebra System
      IDESettings.classpath.append(scalalab.JavaGlobals.javacFile) // Java compiler

      scalaExec.Interpreter.ControlInterpreter.prepareSettings(IDESettings)

      theCellsInterpreter = new scala.tools.nsc.Interpreter(IDESettings, writer)

      // interpret the proper imports for each library
      matrixType match {

        case _root_.scalaExec.Interpreter.GlobalValues.EJMLMat =>
          println("EJML Interpreter")
          theCellsInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
          if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64)
            theCellsInterpreter.interpret(GlobalValues.warmUpGSLScript);
          writer.flush // drop the interpreter's output from the imports

        case _root_.scalaExec.Interpreter.GlobalValues.EIGENMat =>
          println("EIGEN Interpreter")
          theCellsInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEigenScala)
          if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64)
            theCellsInterpreter.interpret(GlobalValues.warmUpGSLScript);
          writer.flush // drop the interpreter's output from the imports

        case _root_.scalaExec.Interpreter.GlobalValues.CUDAMat =>
          println("CUDA Interpreter")
          theCellsInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsCUDAScala)
          if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64)
            theCellsInterpreter.interpret(GlobalValues.warmUpGSLScript);
          writer.flush // drop the interpreter's output from the imports

        case _root_.scalaExec.Interpreter.GlobalValues.MTJMat =>
          println("MTJ Interpreter")
          theCellsInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsMTJScala)
          if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64)
            theCellsInterpreter.interpret(GlobalValues.warmUpGSLScript);
          writer.flush // drop the interpreter's output from the imports

        case _root_.scalaExec.Interpreter.GlobalValues.ApacheCommonMathsMat =>
          println("Common Maths Interpreter")
          theCellsInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsCommonMathsScala)
          if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64)
            theCellsInterpreter.interpret(GlobalValues.warmUpGSLScript);
          writer.flush // drop the interpreter's output from the imports

        case _root_.scalaExec.Interpreter.GlobalValues.JBLASMat =>
          println("JBLAS  Interpreter")
          theCellsInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsJBLASScala)
          if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64)
            theCellsInterpreter.interpret(GlobalValues.warmUpGSLScript);
          writer.flush // drop the interpreter's output from the imports

        case _ =>
          println("EJML Interpreter")
          theCellsInterpreter.interpret(_root_.scalaExec.Interpreter.GlobalValues.basicImportsEJMLScala)
          if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64)
            theCellsInterpreter.interpret(GlobalValues.warmUpGSLScript);
          writer.flush // drop the interpreter's output from the imports

      }
      // match


      // connect now the reader in order to avoid to take the output from importing the global imports
      private val reader = new PipedReader
      pipe.connect(reader)

      def interpret(command: String) = theCellsInterpreter.interpret(command)

      def closeInterpreter = {
        theCellsInterpreter.close
        reader.close
      }

      def hasResult: Boolean = {
        reader.ready
      }

      def getResult: String = {
        reader.read match {
          case -1 =>
            //We are done with this actor, we can exit it
            println("Reader dead, exiting: 0")
            exit
          case x =>
            val sb = new StringBuilder
            val c = x.asInstanceOf[Char]
            sb.append(c)
            def readMore {
              if (reader.ready) {
                reader.read match {
                  case -1 =>
                    println("Reader dead, exiting: 1")
                  case x =>
                    val c = x.asInstanceOf[Char]
                    sb.append(c)
                }
              }
              if (reader.ready) {
                readMore
              } else {
                //Hack to fix issues with how the interpreter
                //flushes its outputs.
                Thread.sleep(50)
                if (reader.ready) {
                  readMore
                }
              }
            }
            readMore
            sb.toString
        }
      }


    } // InterpWrapper

    // create a ScalaLab interpreter based on the default 0-indexed EJML matrix type
    interp = new InterpWrapper(GlobalValues.EJMLMat)


    def restartEJML {
      interp.asInstanceOf[InterpWrapper].closeInterpreter
      interp = new InterpWrapper(GlobalValues.EJMLMat)
      var toolbarFrame = new JFrame("EJML - Mat ")
      var matToolbar = new scalaExec.gui.MatScalaOperationsToolbar
      toolbarFrame.add(matToolbar)
      toolbarFrame.setSize(400, 200)
      toolbarFrame.setLocation(400, 400)
      toolbarFrame.setVisible(true)
    }

    def restartMTJ {
      interp.asInstanceOf[InterpWrapper].closeInterpreter
      interp = new InterpWrapper(GlobalValues.MTJMat)
      var toolbarFrame = new JFrame("MTJ - Mat ")
      var matToolbar = new scalaExec.gui.MatScalaOperationsToolbar
      toolbarFrame.add(matToolbar)
      toolbarFrame.setSize(400, 200)
      toolbarFrame.setLocation(400, 400)
      toolbarFrame.setVisible(true)
    }


    def restartApacheCommons {
      interp.asInstanceOf[InterpWrapper].closeInterpreter
      interp = new InterpWrapper(GlobalValues.ApacheCommonMathsMat)
      var toolbarFrame = new JFrame("Apache Commons - Mat ")
      var matToolbar = new scalaExec.gui.MatScalaOperationsToolbar
      toolbarFrame.add(matToolbar)
      toolbarFrame.setSize(400, 200)
      toolbarFrame.setLocation(400, 400)
      toolbarFrame.setVisible(true)
    }

    def restart {
      interp.asInstanceOf[InterpWrapper].closeInterpreter
      interp = new InterpWrapper(GlobalValues.JAMAMat)
    }

    loop {
      // the loop of the commandProc actor
      receive {
        case command: ProcessCell =>
          import scala.tools.nsc.interpreter.Results._
          def interpret(x: String) = {
            interp.asInstanceOf[InterpWrapper].interpret(x) match {
              case scala.tools.nsc.interpreter.Results.Success | scala.tools.nsc.interpreter.Results.Error =>
                def getResult: String = {
                  if (interp.asInstanceOf[InterpWrapper].hasResult) {
                    //Here return the result, less the line end
                    interp.asInstanceOf[InterpWrapper].getResult.stripLineEnd
                  } else {
                    "<No Result>"
                  }
                } // getResult
                InterpResult(command, getResult)
              case scala.tools.nsc.interpreter.Results.Incomplete =>
                //Do Nothing!
                //Just let the interpreter keep waiting
                InterpResult(command, "<Incomplete Expression>")
            } // match
          } // interpret

          import scala.util.matching.Regex
          val loadRegex = new Regex( """\s*:load\s+(.*)\s*""", "filename")
          val readRegex = new Regex( """\s*:read\s+(.*)\s*""", "filename")

          def read(filename: String)(fn: (String) => InterpResult): InterpResult = {
            import scala.io.Source
            import java.io.{FileNotFoundException, IOException}

            try {
              val sb = new StringBuilder;
              scala.io.Source.fromFile(new java.io.File(filename)).getLines().foreach(sb.append(_))
              fn(sb.toString)
            } catch {
              case e: FileNotFoundException =>
                InterpResult(command, "<Unable to load file, Not Found:[" + filename + "]>")
              case e: IOException =>
                InterpResult(command, "<Unable to load file, IOException: " + e + ">")
              case e: Throwable =>
                InterpResult(command, "<Unable to load file: " + e + ">")
            }
          }

          command.text match {
            case `loadRegex`(filename) =>
              p ! read(filename) {
                interpret(_)
              }
            case `readRegex`(filename) =>
              p ! read(filename) {
                InterpResult(command, _)
              }
            case x =>
              p ! interpret(x)
          }

        case Restart =>
          restart
        case RestartEJML =>
          restartEJML
        case RestartMTJ =>
          restartMTJ
        case RestartApacheCommons =>
          restartApacheCommons

      }
    } // the loop of the commandProc actor
  }
}

object ScalaProcessor {
  private var interp: AnyRef = _
  var theCellsInterpreter: scala.tools.nsc.Interpreter = _

}