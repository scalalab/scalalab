package scalalab.utils

//This class helps fork sysout and
//syserr onto a listener as well as 
//back to the original

import java.io._

object ForkStream {

  /** Redirects the standard output/error.
    * outStream: the stream that is going to be redirected.
    * setFn: the function that replaces the old outStream with the new forked one
    * listener: a listener who will be called when output is sent to outStream
    */
  def fork(outStream: PrintStream, setFn: (PrintStream) => Unit, listener: (String) => Unit) {
    import actors._
    import Actor._

    //Create the pipes we need
    val iOut = new PipedInputStream
    val pOut = new PipedOutputStream(iOut)

    //Redirect to the new standard out
    setFn(new PrintStream(pOut, true))

    //Now use and actor loop to read the data
    actor {
      loop {
        iOut.read match {
          case -1 =>
            exit
          case b =>
            val sb = new StringBuilder
            outStream.write(b)
            sb.append(b.asInstanceOf[Char])
            while (iOut.available > 0) {
              iOut.read match {
                case -1 =>
                  exit
                case b =>
                  outStream.write(b)
                  sb.append(b.asInstanceOf[Char])
              }
            }
            outStream.flush
            listener(sb.toString)
        }
      }
    }
  }
}
