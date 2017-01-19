package storage;

import java.io.Serializable;

public class TupleSB implements Serializable{
  private String  fst;
  private Boolean snd;

  public TupleSB(String s, Boolean b){
    this.fst = s;
    this.snd = b;
  }

  public String getFirst(){
    return this.fst;
  }

  public Boolean getSecond(){
    return this.snd;
  }

  public String asString(){
    return this.getFirst()+";"+this.getSecond().toString();
  }
}
