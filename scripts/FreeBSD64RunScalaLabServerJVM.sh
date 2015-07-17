cp ./target/scala-2.11/scalalab_2.11-.jar ScalaLab.jar
java -server -d64   -XX:+UseNUMA -XX:+UseParallelGC -XX:+UseCompressedOops -XX:+AggressiveOpts -Djava.library.path=./libCUDA:.:./lib -Xss5m -Xms4000m -Xmx23500m -jar ScalaLab.jar
