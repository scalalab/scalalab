package scalalab.utils

object Runner {
  /* Converts any methods to be a runnable
   */
  implicit def fn2Runnable(fn: () => Unit) = new Runnable {
    def run() = fn
  }
}
