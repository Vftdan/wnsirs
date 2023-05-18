TARGET=target/wnsirs-1.0-SNAPSHOT-jar-with-dependencies.jar
all: ${TARGET}

${TARGET}: pom.xml $(shell find src/main/java/ -name '*.java')
	mvn assembly:assembly

run: ${TARGET}
	java -jar $<
