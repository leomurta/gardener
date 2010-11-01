/**
 * 
 */
package br.uff.ic.gardener.cli;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import br.uff.ic.gardener.util.UtilStream;

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
		if (CLI.me() == null)
			fail("Cannot get CLI singletons");
	}

	/**
	 * Test method for
	 * {@link br.uff.ic.gardener.cli.CLI#main(java.lang.String[])}.
	 */
	@Test
	public void testDoMain() {
		// TODO Marcos
		// fail("Not yet implemented");
	}

	@Test
	public void testGetActualPath() {
		try {
			File file = CLI.getActualPath();

			if (!file.isDirectory())
				fail("does not generate a valid directory");
		} catch (Exception e) {
			fail("generate exception");
		}

	}

	/**
	 * Temporary directory to use with workspace
	 */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}
	
	@Test
	public void testDoDiff() throws IOException
	{
		File pathWS = folder.newFolder("cliDiff");
		File fileA = new File(pathWS, "a.txt");
		File fileB = new File(pathWS, "b.txt");
		UtilStream.fillFile(fileA, "1", "2", "3");
		UtilStream.fillFile(fileB, "1", "2", "4");
		CLI.doMain(String.format("diff %s %s", fileA.toString(), fileB.toString()));
	}
}
