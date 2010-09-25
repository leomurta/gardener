/**
 * 
 */
package br.uff.ic.gardener.cli;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;

/**
 * @author Marcos
 *
 */
public class CLITest {

	/**
	 * Test method for {@link br.uff.ic.gardener.cli.CLI#me()}.
	 */
	@Test
	public void testMe() {
		if(CLI.me() == null) 
			fail("Cannot get CLI singletons");
	}

	/**
	 * Test method for {@link br.uff.ic.gardener.cli.CLI#main(java.lang.String[])}.
	 */
	@Test
	public void testDoMain() {
		//TODO Marcos
		//fail("Not yet implemented");
	}
	@Test
	public void testGetActualPath()
	{
		try
		{
			File file = CLI.getActualPath();
			
			if(!file.isDirectory())
				fail("does not generate a valid directory");
		}catch(Exception e)
		{
			fail("generate exception");
		}
	
	}
}
