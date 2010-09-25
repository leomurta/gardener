/**
 * 
 */
package br.uff.ic.gardener.workspace;

import static org.junit.Assert.fail;

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
 * @author Marcos Côrtes
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

	/**
	 * Test method for
	 * {@link br.uff.ic.gardener.workspace.Workspace#Workspace(java.io.File, br.uff.ic.gardener.client.APIClient)}
	 * .
	 */
	@Test
	public final void testWorkspace() {
		// folder
		workspace = new Workspace(pathWS, new StubAPIClient());
	}

	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#commit()}.
	 */
	@Test
	public final void testCommit() {
		//
		try {
			workspace.commit();
		} catch (WorkspaceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
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
	 */
	@Test
	public final void testCheckout() {
		try {
			workspace.checkout(RevisionID.LAST_REVISION);
		} catch (WorkspaceException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	/**
	 * Test method for {@link br.uff.ic.gardener.workspace.Workspace#close()}.
	 */
	@Test
	public final void testClose() {
		workspace.close();
	}
}
