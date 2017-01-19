package test;

import interpreter.Verbo;
import storage.VerboStorage;
import printer.*;

public class Main{
  public static void main(String[] args) {
    VerboStorage db = new VerboStorage();
    Verbo test = null;
    if (db.exists(args[0])) {
      test = db.readVerbo(args[0]);
    }else{
      test = new Verbo(args[0]);
      db.writeVerbo(test);
    }
    Linux out = new Linux(test);
    out.print();
  }
}
