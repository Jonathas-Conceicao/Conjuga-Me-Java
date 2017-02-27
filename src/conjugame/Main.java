package conjugame;

import java.util.Arrays;
import java.util.ArrayList;

import printer.Linux;
import interpreter.Verbo;
import storage.VerboStorage;
import printer.InvalidDisplayOptionException;
/**
 * Main class to run the program.
 */
public class Main{
  /*Input commands*/

  //Number of colums to be displayed
  private static final String SINGLECOLUM = "--Colum=Single";
  private static final String DOUBLECOLUM = "--Colum=Double";
  private static final String TRIPLECOLUM = "--Colum=Triple";

  private static final String INDICATIVO = "-Indicativo";
  private static final String CONJUNTIVO = "-Conjuntivo";
  private static final String IMPERATIVO = "-Imperativo";

  /**
   * Main method to run the program.
   * @param  args(0-(n-1)) list of verbs to be conjugated.
   * @param  args(n) store folder
   */
  public static void main(String[] args) {
    String[] arguments = Arrays.copyOf(args, args.length - 1);
    int divisor = Main.interpretEntry(arguments);
    String[] verbs = Arrays.copyOfRange(arguments, divisor, arguments.length);
    String[] commands = Arrays.copyOf(arguments, divisor);

    VerboStorage db = new VerboStorage(args[args.length - 1]);
    ArrayList<Integer> options = null;
    Verbo verb = null;

    int colums = Main.interpretMode(commands);

    for (String input: verbs) {
      try{
        options = Main.interpretTime(commands);
        if (db.exists(input)) {
          verb = db.readVerbo(input);
        }else{
          verb = new Verbo(input);
          db.writeVerbo(verb);
        }
        Linux out = new Linux(verb);
        out.print(colums, options);
      }catch(InvalidDisplayOptionException e){
        System.err.println("Invalid option entred\nSee 'man conjugame' for help");
      }
    }
    return;
  }

  private static int interpretMode(String[] commands){
    if (commands.length > 0){
      if (commands[0].equals(Main.SINGLECOLUM))
        return 1;
      else if(commands[0].equals(Main.DOUBLECOLUM))
        return 2;
    }
    return 3;
  }

  private static ArrayList<Integer> interpretTime(String[] commands) throws InvalidDisplayOptionException{
    int indice = 0;
    ArrayList<Integer> actions = new ArrayList<Integer>(3);
    for(String command : commands){
      if (command.contains(Main.SINGLECOLUM) || command.contains(Main.DOUBLECOLUM) || command.contains(Main.TRIPLECOLUM))
        ;
      else if(command.contains(Main.INDICATIVO))
        actions.add(1);
      else if(command.contains(Main.CONJUNTIVO))
        actions.add(2);
      else if(command.contains(Main.IMPERATIVO))
        actions.add(3);
      else
        throw new InvalidDisplayOptionException();
    }
    if (actions.isEmpty()) {
      actions.add(1);
      actions.add(2);
      actions.add(3);
    }
    return actions;
  }

  private static int interpretEntry(String[] entry){
    int i = 0;
    for(String s : entry)
      if (s.contains("-"))
        i++;
      else
        break;
    return i;
  }
}
