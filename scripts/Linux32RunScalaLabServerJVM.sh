cp ./target/scala-2.11/scalalab_2.11-.jar ScalaLab.jar
java  -XX:+UseNUMA -XX:+UseParallelGC  -Djava.library.path=./libCUDA:.:./lib -Xss5m -Xms1000m -Xmx1500m -jar ScalaLab.jar
