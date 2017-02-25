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

SRC = src
BIN = bin
DOC = docs

RM = rm
RM_FLAG = -rf

all:
	$(JAVAC) $(JC_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE) $(P_CONJUGAME))) -d $(BIN)/

clean:
	$(RM) $(RM_FLAG) $(BIN)/*
	$(RM) $(RM_FLAG) $(DOC)/*

doc:
	$(JD) $(JD_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_PRINTER) $(P_INTERPRETER) $(P_STORAGE) $(P_CONJUGAME))) -d $(DOC)/
	# $(JD) $(JD_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_PRINTER) ) ) -d $(DOC)
	# $(JD) $(JD_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_STORAGE) ) ) -d $(DOC)
	# $(JD) $(JD_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_CONJUGAME) ) ) -d $(DOC)
	# $(JD) $(JD_FLAG) $(addprefix $(SRC)/, $(addsuffix .java, $(P_INTERPRETER) ) ) -d $(DOC)
