package printer;

public class InvalidDisplayOptionException extends Exception{
  private static final long serialVersionUID = 1842;

  public InvalidDisplayOptionException() {}

  public InvalidDisplayOptionException(String message){
     super(message);
  }
}
