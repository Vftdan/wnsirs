all: target/wnsirs-1.0-SNAPSHOT-jar-with-dependencies.jar

target/wnsirs-1.0-SNAPSHOT-jar-with-dependencies.jar: pom.xml $(shell find src/main/java/ -name '*.java')
	mvn assembly:assembly
