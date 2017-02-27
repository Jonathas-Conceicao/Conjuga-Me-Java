package storage;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  Stores all acceptable verbs conjugations for the same person in the verbal tense.
 */
public class Conjugation implements Serializable{
  private ArrayList<String>  fst = null;
  private ArrayList<Boolean> snd = null;
  private static final long serialVersionUID = 1842;

  /**
   * Constructor method.
   */
  public Conjugation(){
    this.fst = new ArrayList<String>(2);
    this.snd = new ArrayList<Boolean>(2);
  }

  /**
   * Add a conjugations and informs if it is a regular or irregular form of conjugation.
   * @param s verb conjugated.
   * @param b true for inregular, false for regular.
   */
  public void addConju(String s, Boolean b){
    this.fst.add(s);
    this.snd.add(b);
    return;
  }

  /**
   * Get the number of conjugations forms that the verb had for that person and time tense.
   * @return int with the size.
   */
  public int getSize(){
    return this.fst.size();
  }

  /**
   * Get verb's conjugation for a given indice.
   * @param n             Select one of the forms of the conjugations use {@link #getSize()}.
   * @return  String       containing the conjugation.
   */
  public String getFirst(int n){
    return this.fst.get(n);
  }

  /**
   * Get verb's flag indicating if it is a regular or inregular conjugation
   * @param n             Select one of the forms of the conjugations use {@link #getSize()}.
   * @return           true if conjugation is inregular or false if regular.
   */
  public Boolean getSecond(int n){
    return this.snd.get(n);
  }
}
