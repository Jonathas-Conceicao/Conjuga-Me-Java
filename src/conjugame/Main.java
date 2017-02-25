package conjugame;

import java.util.Arrays;

import printer.Linux;
import interpreter.Verbo;
import storage.VerboStorage;
/**
 * Main class to run the program.
 */
public class Main{
  /**
   * Main method to run the program.
   * @param  args(0-(n-1)) list of verbs to be conjugated.
   * @param  args(n) store folder
   */
  public static void main(String[] args) {
    VerboStorage db = new VerboStorage(args[args.length - 1]);
    Verbo verb = null;
    String[] verbs = Arrays.copyOf(args, args.length - 1);
    for (String input: verbs) {
      if (db.exists(input)) {
        verb = db.readVerbo(input);
      }else{
        verb = new Verbo(input);
        db.writeVerbo(verb);
      }
      Linux out = new Linux(verb);
      out.print();
    }
  }
}
