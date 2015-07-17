cp .\target\scala-2.11\scalalab_2.11-.jar ScalaLab.jar
java -server  -d64  -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops -XX:+AggressiveOpts -Djava.library.path=./lib;.;./libCUDA -Xss5m -Xms3000m -Xmx23000m -jar ScalaLab.jar
