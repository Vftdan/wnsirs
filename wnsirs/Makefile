TARGET=target/wnsirs-1.0-SNAPSHOT-jar-with-dependencies.jar
SOURCES=pom.xml $(shell find src/main/java/ -name '*.java')

ifndef RELEASE
all: compile
run: run-classes
else
all: assemble
run: run-jar
endif

assemble: ${TARGET}

${TARGET}: ${SOURCES}
	mvn assembly:assembly

compile: ${SOURCES}
	mvn compile

run-jar: ${TARGET}
	java -jar $<

run-classes:
	mvn javafx:run

.PHONY: all run assemble compile run-jar run-classes
