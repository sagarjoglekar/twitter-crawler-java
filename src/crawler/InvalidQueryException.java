package crawler;

public class InvalidQueryException extends Exception{
	 
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidQueryException(String query) {
      super("Query string '"+query+"' is invalid");
  }
}
