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

SRC = src
BIN = bin
DOC = docs

RM = rm
RM_FLAG = -rf

all:
	$(JAVAC) $(JC_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE) $(P_CONJUGAME))) -d $(BIN)/

clean:
	$(RM) $(RM_FLAG) $(BIN)/*/*.class
	$(RM) $(RM_FLAG) $(BIN)/*.ser
	$(RM) $(RM_FLAG) $(DOC)/*
	$(RM) $(TARGET).jar

doc: $(addprefix $(BIN)/, $(addsuffix .class, $(P_CONJUGAME) ) ) $(addprefix $(BIN)/, $(addsuffix .class, $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE)) )
	$(JD) $(JD_FLAG) -d $(DOC)/

jar:
	cd $(BIN);$(JR) $(JR_FLAG) $(JR_MANIFEST) $(TARGET).jar $(addsuffix .class, $(P_CONJUGAME) $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE) );mv $(TARGET).jar ..
