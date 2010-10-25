/**
 * 
 */
package br.uff.ic.gardener.workspace;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.client.StubAPIClient;

/**
 * @author Marcos C�rtes
 * 
 */
public class WorkspaceTest {

	/**
	 * Temporary directory to use with workspace
	 */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	File pathWS = null;

	Workspace workspace = null;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		pathWS = folder.newFolder("workspace");
		File fileA = new File(pathWS, "a.txt");
		File fileB = new File(pathWS, "b.txt");
		File fileC = new File(pathWS, "c.txt");
		fillFile(fileA, "aaaaa", "aaaaaa", "aaaaaaa");
		fillFile(fileB, "bbbbb", "bbbbbb", "bbbbbbb");
		fillFile(fileC, "ccccc", "cccccc", "ccccccc");
	}

	public final void createWorkspace() {
		// folder
		workspace = new Workspace(pathWS, new StubAPIClient());
	}

	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#commit()}.
	 * @throws WorkspaceException 
	 */
	@Test
	public final void testCommit() throws WorkspaceException {
		createWorkspace();
		workspace.commit();
		workspace.close();
	}

	private static void fillFile(File file, String... strVec) {
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			PrintWriter pw = new PrintWriter(fw);

			for (String str : strVec) {
				pw.append(str + "\n");
			}
			pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#checkout()}
	 * .
	 * @throws WorkspaceException 
	 */
	@Test
	public final void testCheckout() throws WorkspaceException {
		createWorkspace();
		workspace.checkout(RevisionID.LAST_REVISION);
		workspace.close();
	}
}
