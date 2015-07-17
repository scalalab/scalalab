cp ./target/scala-2.11/scalalab_2.11-.jar ScalaLab.jar
java  -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops -XX:+AggressiveOpts -Djava.library.path=./libCUDA:.:./lib -Xss5m -Xms2000m -Xmx23500m -jar ScalaLab.jar
