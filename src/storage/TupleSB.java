package storage;

import java.io.Serializable;
import java.util.ArrayList;

public class TupleSB implements Serializable{
  private ArrayList<String>  fst = null;
  private ArrayList<Boolean> snd = null;
  private int indexFst;
  private int indexSnd;

  public TupleSB(){
    this.fst = new ArrayList<String>(2);
    this.snd = new ArrayList<Boolean>(2);
    this.indexFst = 0;
    this.indexSnd = 0;
  }

  public void addConju(String s, Boolean b){
    this.fst.add(s);
    this.snd.add(b);
  }

  public String getFirst(){
    return this.fst.get(this.indexFst++);
  }

  public Boolean getSecond(){
    return this.snd.get(this.indexSnd++);
  }

  public String getFirst(int n){
    return this.fst.get(n);
  }

  public Boolean getSecond(int n){
    return this.snd.get(n);
  }

  public int getSize(){
    return this.indexFst;
  }
}
