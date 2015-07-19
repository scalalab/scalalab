package scalaSci

import org.bytedeco.javacpp.DoublePointer
import org.bytedeco.javacpp.gsl


object GSLMacro {

  def GSL_REAL(x: org.bytedeco.javacpp.gsl.gsl_complex) = x.dat.get(0)

  def GSL_IMAG(x: org.bytedeco.javacpp.gsl.gsl_complex) = x.dat.get(0)

  def GSL_SET_COMPLEX(xr: Double, yr: Double) = {
    var x = new org.bytedeco.javacpp.gsl.gsl_complex()
    x.put(0, xr)
    x.put(1, yr)
    x
  }

  def GSL_SET_COMPLEX(z: org.bytedeco.javacpp.gsl.gsl_complex, xr: Double, yr: Double) = {
    z.put(0, xr)
    z.put(1, yr)
    z
  }
}
