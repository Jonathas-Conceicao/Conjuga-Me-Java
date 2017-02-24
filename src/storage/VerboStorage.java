package storage;

import java.io.*;
import storage.TupleSB;
import interpreter.Verbo;

public class VerboStorage{

  private Verbo verbo = null;
  private String path = "";

  public VerboStorage(){

  }
  public VerboStorage(String path){
    this.path = path;
  }

  public void setPath(String s){
    this.path = s;
  }

  public void writeVerbo(Verbo v){
    FileOutputStream fileOut = null;
    ObjectOutputStream out = null;
    try{
      fileOut = new FileOutputStream(this.path + v.getVerbo().getFirst(0) + ".ser");
      // fileOut = new FileOutputStream(this.path + v.getVerbo().getFirst() + ".ser");
      out = new ObjectOutputStream(fileOut);
      out.writeObject(v);
      out.close();
      fileOut.close();
    }catch(IOException e) {

      //TODO:Handle Erros

    }finally{
      fileOut = null;
      out = null;
    }
  }

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

      //TODO:Handle Erros

    }catch(ClassNotFoundException e){

      //TODO:Handle Erros

    }finally{
      fileIn = null;
      in = null;
    }
    return verbo;
  }

  public Boolean exists(String v){
    File f = new File(this.path + v + ".ser");
    return f.exists() && !f.isDirectory();
  }
}
