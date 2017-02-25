package storage;

import java.io.*;
import interpreter.Verbo;
/**
 *  Class to serialize, store and recover verbs.
 */
public class VerboStorage{

  private Verbo verbo = null;
  private String path = "";

  /**
   * Constructor method to write where prrogram was called.
   * @deprecated use {@link #VerboStorage(String)} insted
   */
  @Deprecated
  public VerboStorage(){
  }

  /**
   * Constructor method store verb.
   * @param path  path for storing the verbs.
   */
  public VerboStorage(String path){
    this.path = path;
  }

  /**
   * Set new path to store verbs.
   * @param s path to save the verb.
   */
  public void setPath(String s){
    this.path = s;
  }

  /**
   * Store verb passed in class's {@link #setPath(String) path}.
   * @param v Verb to be stored.
   */
  public void writeVerbo(Verbo v){
    FileOutputStream fileOut = null;
    ObjectOutputStream out = null;
    try{
      fileOut = new FileOutputStream(this.path + v.getVerbo().getFirst(0) + ".ser");
      out = new ObjectOutputStream(fileOut);
      out.writeObject(v);
      out.close();
      fileOut.close();
    }catch(IOException e) {
      e.printStackTrace();
    }finally{
      fileOut = null;
      out = null;
    }
  }

  /**
   * Loads a verb from disk.
   * @param v Verb to searck for in disk.
   * @return Verb loaded.
   */
  public Verbo readVerbo(String v){
    FileInputStream fileIn = null;
    ObjectInputStream in = null;
    try{
      fileIn = new FileInputStream(this.path + v + ".ser");
      in = new ObjectInputStream(fileIn);
      verbo = (Verbo) in.readObject();

      in.close();
      fileIn.close();
    }catch(IOException e){
      e.printStackTrace();
    }catch(ClassNotFoundException e){
      e.printStackTrace();
      verbo = null;
    }finally{
      fileIn = null;
      in = null;
    }
    return verbo;
  }

  /**
   * Tests if verb already exists in {@link #setPath(String) path}.
   * @param v Verb to be checked
   * @return true if file exists
   */
  public Boolean exists(String v){
    File f = new File(this.path + v + ".ser");
    return f.exists() && !f.isDirectory();
  }
}
