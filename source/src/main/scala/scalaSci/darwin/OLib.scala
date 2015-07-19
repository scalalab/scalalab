/*
 * Octave-like utility methods
 * 
 * @author Darwin Airola
 * @written 2012-04-27
 */

package scalaSci.darwin


////////////////////////////////////////////////////////////////////////////////
//
//  package imports
//

import org.apache.commons.math3.complex.Complex

//  Apache Complex class

import scalaSci.Vec

//  ScalaSci Vector class

import scalaSci.StaticMaths._

////////////////////////////////////////////////////////////////////////////////


class Util {

}

//  class Util


//  Util's companion object
object Util {


  //////////////////////////////////////////////////////////////////////////////
  //
  //  object imports
  //

  import scala.collection.mutable.MutableList

  //  MutableList class

  //////////////////////////////////////////////////////////////////////////////


  /** **************************************************************************/
  /**
   * abs
   *
   * computes the absolute value on each member of the input array
   *
   * @param in - complex valued array upon which the absolute value is to be
   *           computed
   *
   * @returns - a vector containing the absolute values of the input array
   */
  def abs(in: Array[Complex]): Vec = {

    val out: Vec = new Vec(in.length)

    for (i <- 0 until in.length) {
      //println( i + ": " + in(i) + "; abs = " + in(i).abs )
      out(i) = in(i).abs()
    }

    out //  return value

  } //  abs(Array[Complex])
  /** **************************************************************************/


  /** **************************************************************************/
  /**
   * add
   *
   * defines how to add two arrays of Complex values
   */
  def add(lh: Array[Complex], rh: Array[Complex]): Array[Complex] = {
    val out: Array[Complex] = new Array[Complex](lh.length)

    if (lh.length != rh.length) {
      print("ERROR: the two array lengths must be the same ")
      println("(lh.length = " + lh.length + "; rh.length = " + rh.length + ")")
      out
    }
    else {

      for (i <- lh.indices) {
        out(i) = lh(i).add(rh(i))
      }

      out //  return value
    }
  } //  add( Array[Complex], Array[Complex] )
  /** **************************************************************************/


  /** **************************************************************************/
  /**
   * div
   *
   * defines how to divide two arrays of Complex values
   */
  def div(lh: Array[Complex], rh: Array[Complex]): Array[Complex] = {

    val out: Array[Complex] = new Array[Complex](lh.length)

    if (lh.length != rh.length) {
      println("ERROR: the two array lengths must be the same.")
      out
    }
    else {

      for (i <- lh.indices) {
        out(i) = lh(i).divide(rh(i))
      }

      out //  return value
    }
  } //  div( Array[Complex], Array[Complex] )
  /** **************************************************************************/


  /** **************************************************************************/
  /**
   * like the MATLAB and Octave find(<vector> >= <comp. val.>) method
   *
   * @param v      - the vector whose elements are to be evaluated
   * @param thresh - the comparison value
   *
   * @return - the indices of the input vector and the associated values that
   *         satisfy the comparison
   */
  def findIndicesGE(v: Vec, thresh: Double): (MutableList[Int], Vec) = {

    var indices = new MutableList[Int]() //  matching indices
    var values = new Vec(0) //  values of each of corresponding indices

    for (i <- 0 until v.length) {
      if (v(i) >= thresh) {
        indices += i //  append the index to the end of the list
        values = values ::< v(i) //  append the value to the end of the Vec
      }
    }

    (indices, values) //  return value

  } //  findIndices()
  /** **************************************************************************/


  /** **************************************************************************/
  /**
   * freqz -- like the Octave/MATLAB freqz() function
   *
   * evaluates the polynomial (p) at the specified values (x)
   *
   * @param b  - numerator coefficients
   * @param a  - denominator coefficients
   * @param n  - length
   * @param fS - sample frequency
   */
  def freqz(b: Vec, a: Vec, n: Int, fS: Double): (Array[Complex], Vec) = {

    import scalaSci.FFT.ApacheFFT
    import scalaSci.math.plot.plot._
    //  DELETE
    import scalaSci.math.plot.plotTypes._
    //  DELETE

    //  polyval(fliplr(P),exp(jw)) is O(p n) and fft(x) is O(n log(n)),
    //  where p is the order of the polynomial P.  For small p it
    //  would be faster to use polyval but in practice the overhead for
    //  polyval is much higher and the little bit of time saved is not
    //  worth the extra code.

    var nI = n
    val k = max(b.length, a.length)
    println("k = " + k)
    if (k > (nI / 2)) {
      //  ensure a causal phase response
      nI = nI * pow(2, ceil(log2(2 * k / nI))).asInstanceOf[Int]
    }

    val N = 2 * nI //  half region (whole region --> N = nI)

    println("N = " + N)

    val f = fS * inc(0, 1, nI - 1) / N

    println("ceil( k / N ) = " + ceil(k / N))
    println("ceil( k / N ).asInstanceOf[Int] = " + ceil(k / N).asInstanceOf[Int])
    var padSize = N * max(1.0, ceil(k / N)).asInstanceOf[Int]
    println("padSize = " + padSize)
    val bI = postpad(b, padSize, 0.0)
    println("bI.length = " + bI.length)
    val aI = postpad(a, padSize, 0.0)
    println("a .length = " + a.length)
    println("aI.length = " + aI.length)
    //figure( 1 ) ; subplot( 2, 1, 2 ) ; plot( aI - a )

    //var hb = vzeros( nI )
    //var ha = vzeros( nI )
    var hb = new Array[Complex](nI)
    var ha = new Array[Complex](nI)
    for (i <- 0 until nI) {
      hb(i) = Complex.ZERO
      ha(i) = Complex.ZERO
    }

    for (i <- 0 to padSize - 1 by N) {
      println("i = " + i + ", i+N-1 = " + (i + N - 1))
      //hb = hb+JSci.maths.wavelet.Signal.fft(postpad(b(i,1,i+N),N).getv)(1,1,n)
      //ha = ha+JSci.maths.wavelet.Signal.fft(postpad(a(i,1,i+N),N).getv)(1,1,n)
      //hb = hb + scalaSci.FFT.ApacheFFT.fft( postpad(bI(i,1,i+N),N) )(1,1,nI)
      //ha = ha + scalaSci.FFT.ApacheFFT.fft( postpad(aI(i,1,i+N),N) )(1,1,nI)
      val fftOfBI = scalaSci.FFT.ApacheFFT.fft(postpad(bI(i, 1, i + N - 1), N, 0.0))
      val fftOfAI = scalaSci.FFT.ApacheFFT.fft(postpad(aI(i, 1, i + N - 1), N, 0.0))
      val fBI = new Array[Complex](nI)
      val fAI = new Array[Complex](nI)
      println("fftOfBI.length = " + fftOfBI.length)
      println("fBI.length = " + fBI.length)
      for (i2 <- 0 until nI) {
        fBI(i2) = fftOfBI(i2)
        fAI(i2) = fftOfAI(i2)
      }
      hb = add(hb, fBI)
      ha = add(ha, fAI)
    }

    val h = div(hb, ha)

    (h, f)

  } //  freqz()
  /** **************************************************************************/


  /** **************************************************************************/
  /**
   * postpad -- like the MATLAB/Octave routine
   *
   * @param x - vector to be padded
   * @param l - the length of the output (padded) vector
   *
   * @return - x padded with the scalar c to the length of l
   */
  def postpad(x: Vec, l: Int, c: Double): Vec = {

    var out: Vec = new Vec(l)

    if (x.length >= l) {
      out = x(0, 1, l - 1)
    }
    else {
      out = x ::: c * vones(l - x.length)
    }

    out //  return value

  } //  postpad()
  /** **************************************************************************/


  /** **************************************************************************/
  /**
   * polyVal -- like the Octave/MATLAB polyval() function
   *
   * evaluates the polynomial (p) at the specified values (x)
   *
   * @param p - polynomial
   * @param x - vector of input values at which p is to be evaluated
   */
  /* e.g.
  val p = V("4.5 3.4")
  val x =  inc(0, 0.01, 40)
  val y = polyVal(p, x)
*/
  def polyVal(p: Vec, x: Vec): Vec = {

    val n = p.length - 1
    val y = p(0) * vones(x.length)

    for (i <- 1 to n) {
      for (i2 <- 0 until y.length) {
        y(i2) = y(i2) * x(i2) + p(i)
      }
    }

    y //  return value

  } //  polyVal()
  /** **************************************************************************/


}

//  object Util