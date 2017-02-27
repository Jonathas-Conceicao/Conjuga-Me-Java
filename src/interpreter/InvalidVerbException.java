package interpreter;

public class InvalidVerbException extends Exception{
  private static final long serialVersionUID = 1842;

  public InvalidVerbException() {}

  public InvalidVerbException(String message){
     super(message);
  }
}
