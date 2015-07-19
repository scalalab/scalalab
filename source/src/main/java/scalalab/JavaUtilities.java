package scalalab;

import java.io.File;
import java.net.URL;

import scala.tools.nsc.Settings;
import scalaExec.Interpreter.GlobalValues;

public class JavaUtilities {

    public static boolean pathsDetected = false;  // avoid detecting paths multiple times since they do not change for single execution

    static URL jarPathOfClass(String className) {
        try {
            return Class.forName(className).getProtectionDomain().getCodeSource().getLocation();
        } catch (ClassNotFoundException ex) {
            System.out.println("error in jarPathOfClass" + className + ")");
            ex.printStackTrace();
            return null;
        }

    }


    public static void setByUser(Settings set) {
        set.classpath().setByUser_$eq(true);
    }


    public static void useJavaCP(Settings set) {
        set.usejavacp();
    }

    static public void detectPaths() {
        if (pathsDetected == false) {


            boolean hostIsUnix = true;
            if (File.pathSeparatorChar == ';')
                hostIsUnix = false;  // Windows host

            if (hostIsUnix) {
                JavaGlobals.jarFilePath = jarPathOfClass("scalaSci.RichDouble2DArray").toString().replace("file:/", "/");
                JavaGlobals.compFile = jarPathOfClass("scala.tools.nsc.ScriptRunner").toString().replace("file:/", "/");
                JavaGlobals.libFile = jarPathOfClass("scala.io.Source").toString().replace("file:/", "/");
                JavaGlobals.reflectFile = jarPathOfClass("scala.reflect.api.Names").toString().replace("file:/", "/");
                JavaGlobals.scalaActorsFile = jarPathOfClass("scala.actors.Actor").toString().replace("file:/", "/");

                JavaGlobals.akkaActorsFile = jarPathOfClass("akka.actor.dsl.Inbox").toString().replace("file:/", "/");
                JavaGlobals.actorsMigrationFile = jarPathOfClass("scala.actors.migration.ActorDSL").toString().replace("file:/", "/");
                JavaGlobals.parserCombinatorsFile = jarPathOfClass("scala.util.parsing.combinator.RegexParsers").toString().replace("file:/", "/");
                JavaGlobals.xmlScalaFile = jarPathOfClass("scala.xml.factory.XMLLoader").toString().replace("file:/", "/");

                JavaGlobals.swingFile = jarPathOfClass("scala.swing.Swing").toString().replace("file:/", "/");
                JavaGlobals.jfreechartFile = jarPathOfClass("org.jfree.chart.ChartFactory").toString().replace("file:/", "/");
                JavaGlobals.jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "/");

                if (GlobalValues.hostIsLinux64 || GlobalValues.hostIsWin64) {
                    JavaGlobals.javacppFile = jarPathOfClass("org.bytedeco.javacpp.DoublePointer").toString().replace("file:/", "/");
                    JavaGlobals.gslFile = jarPathOfClass("org.bytedeco.javacpp.presets.gsl").toString().replace("file:/", "/");
                } else {
                    JavaGlobals.javacppFile = ".";
                    JavaGlobals.gslFile = ".";

                }

                JavaGlobals.mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "/");
                JavaGlobals.ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "/");
                JavaGlobals.ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "/");
                JavaGlobals.rsyntaxTextAreaFile = jarPathOfClass("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea").toString().replace("file:/", "/");
                JavaGlobals.matlabScilabFile = jarPathOfClass("matlabcontrol.MatlabConnector").toString().replace("file:/", "/");
                JavaGlobals.jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "/");
                JavaGlobals.numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/", "/");
                JavaGlobals.LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/", "/");
                JavaGlobals.ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/", "/");
                JavaGlobals.javacFile = jarPathOfClass("com.sun.tools.javac.Main").toString().replace("file:/", "/");
                JavaGlobals.JASFile = jarPathOfClass("org.matheclipse.core.eval.EvalEngine").toString().replace("file:/", "/");


            } else {
                JavaGlobals.jarFilePath = jarPathOfClass("scalaSci.RichDouble2DArray").toString().replace("file:/", "");
                JavaGlobals.compFile = jarPathOfClass("scala.tools.nsc.ScriptRunner").toString().replace("file:/", "");
                JavaGlobals.libFile = jarPathOfClass("scala.io.Source").toString().replace("file:/", "");
                JavaGlobals.reflectFile = jarPathOfClass("scala.reflect.api.Names").toString().replace("file:/", "");
                JavaGlobals.scalaActorsFile = jarPathOfClass("scala.actors.Actor").toString().replace("file:/", "");
                JavaGlobals.swingFile = jarPathOfClass("scala.swing.Swing").toString().replace("file:/", "");

                JavaGlobals.akkaActorsFile = jarPathOfClass("akka.actor.dsl.Inbox").toString().replace("file:/", "");
                JavaGlobals.actorsMigrationFile = jarPathOfClass("scala.actors.migration.ActorDSL").toString().replace("file:/", "");
                JavaGlobals.parserCombinatorsFile = jarPathOfClass("scala.util.parsing.combinator.RegexParsers").toString().replace("file:/", "");
                JavaGlobals.xmlScalaFile = jarPathOfClass("scala.xml.factory.XMLLoader").toString().replace("file:/", "");

                JavaGlobals.jfreechartFile = jarPathOfClass("org.jfree.chart.ChartFactory").toString().replace("file:/", "");
                JavaGlobals.jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "");
                if (GlobalValues.hostIsLinux64 || GlobalValues.hostIsWin64) {
                    JavaGlobals.javacppFile = jarPathOfClass("org.bytedeco.javacpp.DoublePointer").toString().replace("file:/", "");
                    JavaGlobals.gslFile = jarPathOfClass("org.bytedeco.javacpp.presets.gsl").toString().replace("file:/", "");
                } else {
                    JavaGlobals.javacppFile = ".";
                    JavaGlobals.gslFile = ".";
                }
                JavaGlobals.mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "");
                JavaGlobals.ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "");
                JavaGlobals.ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "");
                JavaGlobals.rsyntaxTextAreaFile = jarPathOfClass("org.fife.ui.rsyntaxtextarea.RSyntaxTextArea").toString().replace("file:/", "");
                JavaGlobals.matlabScilabFile = jarPathOfClass("matlabcontrol.MatlabConnector").toString().replace("file:/", "");
                JavaGlobals.jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "");
                JavaGlobals.numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/", "");
                JavaGlobals.LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/", "");
                JavaGlobals.ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/", "");
                JavaGlobals.javacFile = jarPathOfClass("com.sun.tools.javac.Main").toString().replace("file:/", "");
                JavaGlobals.JASFile = jarPathOfClass("org.matheclipse.core.eval.EvalEngine").toString().replace("file:/", "");
            }

            System.out.println("jarFile =" + JavaGlobals.jarFilePath);
            System.out.println("compFile = " + JavaGlobals.compFile);
            System.out.println("libFile = " + JavaGlobals.libFile);
            System.out.println("reflectFile= " + JavaGlobals.reflectFile);
            System.out.println("swingFile = " + JavaGlobals.swingFile);
            System.out.println("Scala Actors File = " + JavaGlobals.scalaActorsFile);
            System.out.println("Actors migration file = " + JavaGlobals.actorsMigrationFile);
            System.out.println("Scala Parser Combinators File = " + JavaGlobals.parserCombinatorsFile);
            System.out.println("Scala XML file = " + JavaGlobals.xmlScalaFile);


            System.out.println("MTJColtSGTFile = " + JavaGlobals.mtjColtSGTFile);
            System.out.println("JBLAS File = " + JavaGlobals.jblasFile);
            System.out.println("Apache Common Maths File = " + JavaGlobals.ApacheCommonsFile);
            System.out.println("EJMLFile = " + JavaGlobals.ejmlFile);
            System.out.println("RSyntaxTextArea file = " + JavaGlobals.rsyntaxTextAreaFile);
            System.out.println("MATLAB - SciLab connection file = " + JavaGlobals.matlabScilabFile);
            System.out.println("NUMALFile = " + JavaGlobals.numalFile);
            System.out.println("JFreeChartFile = " + JavaGlobals.jfreechartFile);
            System.out.println("Java Algebra System = " + JavaGlobals.JASFile);
            System.out.println("LAPACK library = " + JavaGlobals.LAPACKFile);
            System.out.println("ARPACK library = " + JavaGlobals.ARPACKFile);
            System.out.println("Scala actors library = " + JavaGlobals.scalaActorsFile);
            System.out.println("Java Compiler = " + JavaGlobals.javacFile);

            pathsDetected = true;   // paths are detected. Avoid the redundant job to rediscover them
        }
    }


    static public void detectBasicPaths() {
        detectPaths();   // use the  same paths for now
    }

}
