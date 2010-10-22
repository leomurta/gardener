
/* UFF - Universidade Federal Fluminense
 *  IC - Instituto de Computa��o
 */

package br.uff.ic.gardener.cli;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.client.APIClient;
import br.uff.ic.gardener.client.ClientFactory;
import br.uff.ic.gardener.client.CreationAPIClientException;
import br.uff.ic.gardener.workspace.Workspace;
import br.uff.ic.gardener.workspace.WorkspaceException;

/**
 * CLI class. Implements CLI interface with a singletons
 *
 * @author Marcos C�rtes
 * @seealso
 *          https://args4j.dev.java.net/source/browse/args4j/args4j/examples/SampleMain
 *          .java?view=markup
 *
 */
public class CLI {

	@Option(name = "-co", aliases = "--checkout", metaVar = "CHECKOUT", usage = "Checkout a Configuration Item in repository")
	private boolean bCheckout = false;

	@Option(name = "-ci", aliases = "--commit", metaVar = "COMMIT", usage = "Commit a Configuration Item in repository")
	private boolean bCommit = false;

	// Other arguments
	@SuppressWarnings("unused")
	@Argument
	private List<String> listArguments = new ArrayList<String>();

	@Option(name = "-w", aliases = "--workspace", metaVar = "WORKSPACE", usage = "Specify the source of ICs (URI or path)")
	private URI uriWorkspace = null;

	@Option(name = "-s", aliases = "--serv", metaVar = "SERV", usage = "Specify the serv")
	private URI uriServ = null;

	@SuppressWarnings("unused")
	@Option(name = "-r", aliases = "--revision", metaVar = "REVISION", usage = "Specify the revision in checkout")
	private void setRevision(String strRevision) {
		revision = RevisionID.fromString(strRevision);
	}

	private RevisionID revision = RevisionID.LAST_REVISION;

	/**
	 * Define possible operations to CLI
	 *
	 * @author Marcos
	 *
	 */
	private enum OPERATION {
		CHECKOUT, COMMIT, NULL
	}

	// private OPERATION operation = OPERATION.NULL;

	// private void setOperation(OPERATION op){
	// operation = op;
	// }

	private OPERATION getOperation() {

		if (bCheckout) {
			return OPERATION.CHECKOUT;
		} else if (bCommit) {
			return OPERATION.COMMIT;
		} else
			return OPERATION.NULL;

	}

	private static APIClient apiClient = null;

	public APIClient getClient() {
		if (apiClient == null) {
			try {
				File file = (new File(uriServ));
				apiClient = ClientFactory.createAPIClient(file.toString());
			} catch (CreationAPIClientException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return apiClient;
	}

	private Workspace workspace = null;

	/**
	 * Cria um workspace
	 *
	 * @param path
	 *            O caminho do workspace
	 * @return o workspace criado
	 */
	private Workspace createWorkspace(File path) {
		if (workspace != null)
			workspace.close();

		workspace = new Workspace(path, getClient());
		return workspace;
	}

	private Workspace getWorkspace() {
		return workspace;
	}

	static private CLI cliSingletons = null;

	private CLI() {
	}

	synchronized static public CLI me() {
		if (cliSingletons == null)
			cliSingletons = new CLI();

		return cliSingletons;
	}

	/**
	 * Interpret and execute cli operations in the args array
	 *
	 * @param args
	 *            Array of strings containing the parameters
	 */
	static public void doMain(String[] args) {
		me().internalDoMain(args);
	}

	/**
	 * Return actual path
	 *
	 * @return
	 */
	static File getActualPath() {
		String str = System.getProperty("user.dir");
		File dir = new File(str);
		return dir;
	}

	// private output(String

	/**
	 * Realize main application. It is needed to create the singletons.
	 *
	 * @param args
	 *            Application params
	 */
	private void internalDoMain(String[] args) {
		CmdLineParser parser = new CmdLineParser(this);

		// default destiny
		me().uriWorkspace = CLI.getActualPath().toURI();
		// me().uriServ = CLI.getActualPath().toURI();

		// default length of execution line
		parser.setUsageWidth(80);

		try {
			// parse the arguments.
			parser.parseArgument(args);

			{
				// trata URI sem file://
				if (uriWorkspace.getHost() == null
						|| uriWorkspace.getHost() == "") {
					uriWorkspace = (new File(uriWorkspace.toString())).toURI();
				}

				if (uriServ != null) {
					// trata URI sem file://
					if (uriServ.getHost() == null || uriServ.getHost() == "") {
						uriServ = (new File(uriServ.toString())).toURI();
					}
				} else {
					uriServ = uriWorkspace;
				}

			}

			createWorkspace(new File(uriWorkspace));

			switch (getOperation()) {
			case CHECKOUT:
				onCheckout();
				break;

			case COMMIT:
				onCommit();
				break;
			default:
				// this will redirect the output to the specified output
				parser.printUsage(System.out);
				break;
			}
		} catch (CmdLineException e) {
			// if there's a problem in the command line,
			// you'll get this exception. this will report
			// an error message.
			System.err.println(e.getMessage());
			// System.err.println("gardener -[checkout|commit] [-workspace]:%%path%%");

			// print the list of available options
			parser.printUsage(System.err);
			System.err.println();

			// print option sample. This is useful some time
			System.err.println("  Exemplo: gardener"
					+ parser.printExample(ExampleMode.ALL));

			return;
		} catch (WorkspaceException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Commit event
	 *
	 * @throws WorkspaceException
	 */
	private void onCommit() throws WorkspaceException {
		getWorkspace().commit();
	}

	/**
	 * Checkout event
	 *
	 * @throws WorkspaceException
	 */
	private void onCheckout() throws WorkspaceException {
		getWorkspace().checkout(revision);
	}
}