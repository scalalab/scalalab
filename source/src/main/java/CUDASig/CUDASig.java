package CUDASig;


import java.io.File;

import scalaExec.Interpreter.GlobalValues;

// Java interface to CUDA kernel operations  for signal processing
public class CUDASig {
    static {

        if (GlobalValues.hostIsWin64 || GlobalValues.hostIsLinux64) {  // Windows and Linux only can have CUDA support

            String libName = "CUDASig.so";
            if (File.pathSeparatorChar == ';') {   // Windows OS
                libName = "CUDASig.dll";

                try {
                    System.out.println("Trying to load CUDASig library [" + libName + "] ...");
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
    }

    public native void cudadwt(float[] data, int size, float[] output);


}

