package interpreter;

public class InvalidVerbException extends Exception{

  public InvalidVerbException() {}

  public InvalidVerbException(String message){
     super(message);
  }
}
