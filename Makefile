P_PRINTER = printer/Linux

P_INTERPRETER = interpreter/InvalidVerbException \
								interpreter/Verbo

P_STORAGE = storage/Conjugation \
						storage/VerboStorage

P_CONJUGAME = conjugame/Main

JAVAC = javac
JC_FLAG =

JD = javadoc
JD_FLAG = -Xdoclint:all

JR = jar
JR_FLAG = cmvf
JR_MANIFEST = META-INF/MANIFEST.MF
TARGET = conjugame

MAN_FILE = conjugame.1

SRC = src
BIN = bin
DOC = docs

RM = rm
RM_FLAG = -rf

BIN_LOCATION ?= .
JAR_LOCATION ?= .
MAN_LOCATION ?= /usr/local/share/man/man1
MV = mv

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
	$(MV) $(TARGET) $(BIN_LOCATION)/$(TARGET)
	$(MV) $(TARGET).jar $(JAR_LOCATION)/Jar/$(TARGET).jar
	$(MV) $(MAN_FILE) $(MAN_LOCATION)/$(MAN_FILE)
