package scalaExec.Interpreter;

import CCOps.CCOps;
import NROps.NROps;

// keeps the objects implemented with native libraries
// during startup ScalaLab initializes JNI in order the Scala Interpreter to be able to
// use the corresponding native libraries properly
public class NativeLibsObj {

    static public NROps nrObj = new NROps();   // Numerical recipes based routines
    static public CCOps ccObj = new CCOps();    // CCMath based routines

    static public GSLJava.GSLOps gslOps = new GSLJava.GSLOps();  // GSL based operations

    static public CUDAOps.KernelOps cudaObj = null;   // NVIDIA CUDA's routines, require installation of CUDA
    static public CUDASig.CUDASig cudaSigObj = null;  // NVIDIA CUDA's routines for signal processing, require installation of CUDA

}