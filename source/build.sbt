name := "ScalaLab"

version := ""

organization := ""

scalaVersion := "2.11.4"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

crossScalaVersions := Seq("2.11.4")

description := "A MATLAB-like environment)"

scalacOptions ++= Seq("-optimise")

exportJars := true

val classPath = Seq(
  "lib/ApacheCommonMaths.jar",
  "lib/JASYMCA.jar",
  "lib/JFreeChart.jar",
  "lib/JavaCompiler.jar",
  "lib/LBFGS.jar",
  "lib/MTJColtSGTJCUDA.jar",
  "lib/NumericalRecipesNUMAL.jar",
  "lib/PDFRenderer.jar",
  "lib/RSyntaxTextArea.jar",
  "lib/akka-actor.jar",
  "lib/apidoc.jar",
  "lib/arpack_combo-0.1.jar",
  "lib/config-1.2.1.jar",
  "lib/diffutils.jar",
  "lib/ejml024.jar",
  "lib/f2jutil.jar",
  "lib/fjbg.jar",
  "lib/gsl-linux-x86.jar",
  "lib/gsl-linux-x86_64.jar",
  "lib/gsl-macosx-x86_64.jar",
  "lib/gsl-windows-x86.jar",
  "lib/gsl-windows-x86_64.jar",
  "lib/gsl.jar",
  "lib/itext-2.1.5.jar",
  "lib/jSciJPlasmaJSparseJTransforms.jar",
  "lib/javacpp.jar",
  "lib/jblas-1.2.3.jar",
  "lib/jdk6Help.jar",
  "lib/jhall.jar",
  "lib/jline-2.12.1.jar",
  "lib/jline.jar",
  "lib/jna-4.0.0.jar",
  "lib/jsearch.jar ",
  "lib/jsyntaxpane-0.9.5-b29.jar",
  "lib/jzy3d-api-0.9.1.jar",
  "lib/matlabscilab.jar",
  "lib/netlib-java-0.9.3.jar",
  "lib/optimization.jar",
  "lib/scala-actors-migration.jar",
  "lib/scala-actors.jar",
  "lib/scala-compiler.jar",
  "lib/scala-continuations-library.jar",
  "lib/scala-continuations-plugin.jar",
  "lib/scala-library.jar",
  "lib/scala-parser-combinators.jar",
  "lib/scala-reflect.jar",
  "lib/scala-swing.jar",
  "lib/scala-xml.jar",
  "lib/scalaHelp.jar",
  "lib/scalap.jar",
  "lib/txt2xhtml.jar",
  "lib/xmlgraphics-commons.jar"
)

packageOptions += Package.ManifestAttributes(
  "Class-Path" -> classPath.mkString(" "),
  "Main-Class" -> "scalaExec.scalaLab.scalaLab"
)