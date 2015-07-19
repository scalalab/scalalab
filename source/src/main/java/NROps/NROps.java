package NROps;


import java.io.File;

import scalaExec.Interpreter.GlobalValues;

// Java interface to CUDA kernel operations  
public class NROps {
    static {

        if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64) {  // Windows and Linux only can have CUDA support

            String libName = "NROps.so";
            if (File.pathSeparatorChar == ';')   // Windows OS
                libName = "NROps.dll";

            try {
                System.out.println("Trying to load NR library [" + libName + "] ...");
                File path = new File("");
                System.out.println("Current Path = " + path.getAbsolutePath());
                String libPath = path.getAbsolutePath() + File.separator + "lib" + File.separator + libName;

                System.load(libPath);
                System.out.println("Library loaded");
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }
    }

    public native void cnrfft(double[] data, int size, int isign);

    public native void nfourn(final double[] data, final int[] nn, final int isign);

    public native void mul(double[] a, int n, int m, double[] b, int k, double[] c);


}
