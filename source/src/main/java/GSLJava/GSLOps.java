package GSLJava;
// gcc -lgsl  -lgslcblas -fPIC -fpermissive -I"/home/sterg/jdk1.8.0_20/include" -I"/home/sterg/jdk1.8.0_20/include/linux" -shared -o  libGSLOps.so  GSLOps.cpp 

import java.io.File;

import scalaExec.Interpreter.GlobalValues;

public class GSLOps {
    static {
        if (GlobalValues.hostIsLinux64) {  // these operations have been ported to Linux64 only
            String libName = "libGSLOps.so";

            try {
                System.out.println("Trying to load GSL library [" + libName + "] ...");
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


    public native double gslSfBesselJ0(double x);

    //public native double gslEigSymm(double [] A, int N);

}
// multiplies two column indexed matrixes, a(n1 X n2), b (n2 X n3) returning result in c

 	
	


