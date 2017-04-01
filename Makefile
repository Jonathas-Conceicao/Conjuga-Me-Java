SHELL = /bin/bash

P_PRINTER = printer/Linux \
						printer/InvalidDisplayOptionException

P_INTERPRETER = interpreter/InvalidVerbException \
								interpreter/Verbo

P_STORAGE = storage/Conjugation \
						storage/VerboStorage

P_CONJUGAME = conjugame/Main

JAVAC = javac
JC_FLAG = -Xlint:all
CLASSPATH =

JD = javadoc
JD_FLAG = -Xdoclint:all

JR = jar
JR_FLAG = cmvf
JR_MANIFEST = META-INF/MANIFEST.MF
TARGET = conjugame

MAN_FILE = conjugame.1
COMPLETE_FILE = complete_conjugame

SRC = src
BIN = bin
DOC = docs

RM = rm
RM_FLAG = -rf

BIN_LOCATION ?= .
JAR_LOCATION ?= .
MAN_LOCATION ?= .
AUTO_COMPLETE_LOCATION ?= .
MV = mv
MV_FLAG = -u
CP = cp
CP_FLAG = -u

all:
	$(JAVAC) $(JC_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE) $(P_CONJUGAME))) -d $(BIN)/

clean:
	$(RM) $(RM_FLAG) $(BIN)/*/*.class

cleanTarget:
	$(RM) $(RM_FLAG) $(TARGET)*

doc: $(addprefix $(BIN)/, $(addsuffix .class, $(P_CONJUGAME) ) ) $(addprefix $(BIN)/, $(addsuffix .class, $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE)) )
	$(JD) $(JD_FLAG) -d $(DOC)/

cleanDoc:
	$(RM) $(RM_FLAG) $(DOC)/*

jar:
	cd $(BIN); \
	$(JR) $(JR_FLAG) $(JR_MANIFEST) $(TARGET).jar $(addsuffix .class, $(P_CONJUGAME) $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE) ); \
	$(MV) $(TARGET).jar ..

install:
	$(MV) $(MV_FLAG) $(TARGET) $(BIN_LOCATION)/$(TARGET) || true
	$(MV) $(MV_FLAG) $(TARGET).jar $(JAR_LOCATION)/.Jar/$(TARGET).jar || true
	$(CP) $(CP_FLAG) $(MAN_FILE) $(MAN_LOCATION)/$(TARGET).1 || true
	$(MV) $(MV_FLAG) $(COMPLETE_FILE) $(AUTO_COMPLETE_LOCATION)/$(TARGET)
