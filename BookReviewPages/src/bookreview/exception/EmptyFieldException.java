package bookreview.exception;

@SuppressWarnings("serial")
public class EmptyFieldException extends Exception 
{
	//attribute for printing messahe
	String message;
	
	//zero parameter constructor
	public EmptyFieldException() 
	{
	
	}
	
	//parameterized constructor
	public EmptyFieldException(String message) 
	{
		this.message = message;
	}
	
	
	//Accessor
	@Override
	public String getMessage() 
	{
		return message;
	}
	
	//Overridden form of toString Method
	@Override
	public String toString() {
	
		return "Each Field is Mandatory";
	}
}
