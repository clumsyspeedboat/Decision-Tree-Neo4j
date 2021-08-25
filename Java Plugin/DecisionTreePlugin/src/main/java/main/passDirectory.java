package main;

import java.util.List;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

/**
 *  This aim at passing csv directory into java plugin
 * @author minhd
 *
 */
public class passDirectory{
	static String globalString = "";
	
	@UserFunction
    @Description("pass directory")
	public String CreateTreeOnData(@Name("path") String path)
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			return "Create Tree on Data: " + globalString;
		}
	}
	
	@UserFunction
    @Description("pass directory")
	public String QueryData(@Name("path") String path)
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			globalString = path;
			return "Query Data: " + globalString;
		}
	}
}
