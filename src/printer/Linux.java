package printer;

import interpreter.Verbo;
import storage.TupleSB;

public class Linux{

  private String defaultColor = null;
  private String regularColor = null;
  private String inregularColor = null;
  private Verbo v = null;
  private final int limite = 42;

  public Linux(Verbo in){
    this(in,(char) 27 + "[0m", (char) 27 + "[1;36m", (char) 27 + "[91m");
    // this.v = in;
    // this.defaultColor = (char) 27 + "[0m";
    // this.regularColor = (char) 27 + "[1;36m";
    // this.inregularColor = (char) 27 + "[91m";
  }
  public Linux(Verbo in, String def, String reg, String inreg){
    this.v = in;
    this.defaultColor = def;
    this.regularColor = reg;
    this.inregularColor = inreg;
  }

  public void print(){

    if(this.v.isValid()){
      System.out.println("Conjuga-Me: Conjugação do verbo " +
      this.regularColor + this.v.getVerbo().getFirst() + this.defaultColor);
      this.printGerundio();
      System.out.print("\t");
      this.printParticipioPassado();
      System.out.print("\n");

      this.printIndicativo();
      this.printConjuntivo();
      this.printImperativo();
    }else{
      System.out.println("Verbo " +
                         this.inregularColor + this.v.getVerbo().getFirst() + this.defaultColor +
                         " não foi encontrado.");
    }

  }

  private void printTab(int escrito){
    System.out.printf("%" + (this.limite - escrito) + "c",' ');
  }

  private int length(String s){
    int n;
    String l = new String(s);
    n = l.length();
    l = null;
    return n;
  }

  private int printWithLength(String s){
    System.out.print(s);
    return length(s);
  }

  private String getPerson(int n){
    switch (n){
      case 0:
        return "eu ";
      case 1:
        return "tu ";
      case 2:
        return "ele/ela ";
      case 3:
        return "nós ";
      case 4:
        return "vós ";
      default:
        return "eles/elas ";

    }
  }

  private String getConjuncaoConjuntivo(int m){
    switch(m){
      case 0:
        return "que ";
      case 1:
        return "se ";
      default:
        return "quando ";
    }
  }

  private void printGerundio(){
    if(this.v.getGerundio().getSecond())
      System.out.print("Gerúndio:" + this.inregularColor + this.v.getGerundio().getFirst() + this.defaultColor);
    else
      System.out.print("Gerúndio:" + this.regularColor + this.v.getGerundio().getFirst() + this.defaultColor);
  }

  private void printParticipioPassado(){
    if(this.v.getParticipioPassado().getSecond())
      System.out.print("Particípio Passado:" + this.inregularColor + this.v.getParticipioPassado().getFirst() + this.defaultColor);
    else
      System.out.print("Particípio Passado:" + this.regularColor + this.v.getParticipioPassado().getFirst() + this.defaultColor);
  }

  private void printIndicativo(){
    int offset = 0;
    int i, j, k;

    this.printTab(offset);
    System.out.println("INDICATIVO");
    offset = this.printWithLength("Presente");
    this.printTab(offset);
    offset = this.printWithLength("Pretérito perfeito");
    this.printTab(offset);
    offset = this.printWithLength("Pretérito imperfeito");
    System.out.print("\n");

    for (i = 0; i < 6; i++) {
      for (j = 0; j < 3 ;j++) {
        offset = 0;
        offset += this.printWithLength(this.getPerson(i));
        if(this.v.getIndicativo()[j][i].getSecond()){
          System.out.print(this.inregularColor);
          offset += this.printWithLength(this.v.getIndicativo()[j][i].getFirst());
          System.out.print(this.defaultColor);
        }else{
          System.out.print(this.regularColor);
          offset += this.printWithLength(this.v.getIndicativo()[j][i].getFirst());
          System.out.print(this.defaultColor);
        }
        // offset += this.length(this.v.getIndicativo()[j][i].getFirst());
        printTab(offset);
      }
      System.out.print("\n");
    }

    offset = this.printWithLength("Pretérito mais-que-perfeito");
    this.printTab(offset);
    offset = this.printWithLength("Futuro do presente");
    this.printTab(offset);
    offset = this.printWithLength("Futuro do pretérito");
    System.out.print("\n");

    for (i = 0; i < 6; i++) {
      for (j = 3; j < 6 ;j++) {
        offset = 0;
        offset += this.printWithLength(this.getPerson(i));
        for (k = 0; k < this.v.getIndicativo()[j][i].getSize(); k++) {
          if(this.v.getIndicativo()[j][i].getSecond(k)){
            System.out.print(this.inregularColor);
            offset += this.printWithLength(this.v.getIndicativo()[j][i].getFirst(k));
            System.out.print(this.defaultColor);
          }else{
            System.out.print(this.regularColor);
            offset += this.printWithLength(this.v.getIndicativo()[j][i].getFirst(k));
            System.out.print(this.defaultColor);
          }
          // offset += this.length(this.v.getIndicativo()[j][i].getFirst());
          printTab(offset);
        }
      }
      System.out.print("\n");
    }

  }

  private void printConjuntivo(){
    int offset = 0;
    int i, j, k;

    this.printTab(offset);
    System.out.println("CONJUNTIVO");
    offset = this.printWithLength("Presente");
    this.printTab(offset);
    offset = this.printWithLength("Pretérito imperfeito");
    this.printTab(offset);
    offset = this.printWithLength("Futuro");
    System.out.print("\n");

    for (i = 0; i < 6; i++) {
      for (j = 0; j < 3 ;j++) {
        offset = 0;
        offset += this.printWithLength(this.getConjuncaoConjuntivo(j));
        offset += this.printWithLength(this.getPerson(i));
        for (k = 0; k < this.v.getConjuntivo()[j][i].getSize(); k++) {
          if(this.v.getConjuntivo()[j][i].getSecond(k)){
            System.out.print(this.inregularColor);
            offset += printWithLength(this.v.getConjuntivo()[j][i].getFirst(k));
            System.out.print(this.defaultColor);
          }else{
            System.out.print(this.regularColor);
            offset += printWithLength(this.v.getConjuntivo()[j][i].getFirst(k));
            System.out.print(this.defaultColor);
          }
        }
        printTab(offset);
      }
      System.out.print("\n");
    }
  }

  private void printImperativo(){
    int offset = 0;
    int i, j, k;

    this.printTab(offset);
    System.out.println("IMPERATIVO");
    offset = this.printWithLength("Afirmativo");
    this.printTab(offset);
    offset = this.printWithLength("Negativo");
    this.printTab(offset);
    offset = this.printWithLength("Infinitivo pessoal");
    System.out.print("\n");

    offset = this.printWithLength("--");
    printTab(offset);
    offset = this.printWithLength("--");
    printTab(offset);
    System.out.print("para ");
    for (k = 0; k < this.v.getImperativo()[2][0].getSize(); k++) {
      if(this.v.getImperativo()[2][0].getSecond(k)){
        System.out.print(this.inregularColor +
                         this.v.getImperativo()[2][0].getFirst(k) +
                         this.defaultColor);
      }else{
        System.out.print(this.regularColor +
                         this.v.getImperativo()[2][0].getFirst(k) +
                         this.defaultColor);
      }
    }
    System.out.print("\n");
    for (i = 1; i < 6; i++) {
      for (j = 0; j < 3 ;j++) {
        offset = 0;
        if (j > 0) {
          if(j > 1){
            offset += this.printWithLength("para ");
          }else{
            offset += this.printWithLength("não ");
          }
        }
        for (k = 0; k < this.v.getImperativo()[j][i].getSize(); k++) {
          if(this.v.getImperativo()[j][i].getSecond(k)){
            System.out.print(this.inregularColor);
            offset += printWithLength(this.v.getImperativo()[j][i].getFirst(k));
            System.out.print(this.defaultColor);
          }else{
            System.out.print(this.regularColor);
            offset += printWithLength(this.v.getImperativo()[j][i].getFirst(k));
            System.out.print(this.defaultColor);
          }
        }
        offset += this.printWithLength(" ");
        offset += this.printWithLength(this.getPerson(i));
        printTab(offset);
      }
      System.out.print("\n");
    }
  }
}
