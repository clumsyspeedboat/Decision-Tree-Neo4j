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
	@UserFunction
    @Description("pass directory")
	public String directory(@Name("path") String path)
	{
		if(path == null)
		{
			return null;
		}
		else
		{
			return "path to csv file: " + path;
		}
	}
}
