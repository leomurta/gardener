
/* UFF - Universidade Federal Fluminense
 *  IC - Instituto de Computa��o
 */

package br.uff.ic.gardener.cli;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;
import br.uff.ic.gardener.client.APIClient;
import br.uff.ic.gardener.client.ClientFactory;
import br.uff.ic.gardener.client.CreationAPIClientException;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.Workspace;
import br.uff.ic.gardener.workspace.WorkspaceException;

/**
 * CLI class. Implements CLI interface with a singletons
 *
 * @author Marcos Côrtes
 * @seealso	{@link https://args4j.dev.java.net/source/browse/args4j/args4j/examples/SampleMain.java?view=markup}
 *
 */
public class CLI {

	@Option(name = "-co", aliases = "--checkout", metaVar = "CHECKOUT", usage = "Checkout a Configuration Item in repository")
	private boolean bCheckout = false;

	@Option(name = "-ci", aliases = "--commit", metaVar = "COMMIT", usage = "Commit a Configuration Item in repository")
	private boolean bCommit = false;

	@Option(name = "-add", aliases = "--add", metaVar = "ADD", usage = "Add a Configuration Item (it can use prompt regular expression)")
	private boolean bAdd = false;
	
	@Option(name = "-rem", aliases = "--remove", metaVar = "REMOVE", usage = "Remove a Configuration Item (it can use prompt regular expression)")
	private boolean bRemove = false;
	
	@Option(name = "-rename", aliases = "--remove", metaVar = "RENAME", usage = "Rename a file in the workspace")
	private boolean bRename = false;
	
	// Other arguments
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
	
	/**
	 * Indica que a operação é de diff
	 */
	@Option(name = "-diff", aliases = "--difference", metaVar = "DIFF", usage = "Diff two files at the form: diff [-E|-B|-i|-c [l|n]|-u ] fileA fileB")
	private boolean bDiff = false;

	/*
	*	
	*	•Formato Unificado
	*	diff -u arquivo_1 arquivo_2
	 */
	
	@SuppressWarnings("unused")
	@Option(name = "-E", aliases="--IgnoreWhiteSpaces", metaVar="DIFF_IGNORE_WHITE_SPACES", usage = "Ignore White Spaces and tabs on Diff operation")
	private boolean bDiffIgnoreWhiteSpaces = false;
	
	@SuppressWarnings("unused")
	@Option(name = "-B", aliases="--IgnoreWhiteLines", metaVar="DIFF_IGNORE_WHITE_LINES", usage = "Ignore White Lines on Diff operation")
	private boolean bDiffIgnoreWhiteLines = false;
	
	@SuppressWarnings("unused")
	@Option(name = "-i", aliases="--IgnoreCaseSensitive", metaVar="DIFF_IGNORE_CASE_SENSITIVE", usage = "Ignore Case Sensitive")
	private boolean bDiffIgnoreCaseSensitive = false;
	 
	@SuppressWarnings("unused")
	@Option(name = "-c", aliases="--ContextFormat", metaVar="DIFF_CONTEXT_FORMAT", usage = "Select Context Format")
	private char charDiffContextFormat = ' ';
	
	@SuppressWarnings("unused")
	@Option(name = "-u", aliases="--UnifiedFormat", metaVar="DIFF_UNIFIED_FORMAT", usage = "Use Unified Format")
	private boolean bDiffUnifiedFormat = false;
	
	private RevisionID revision = RevisionID.LAST_REVISION;

	/**
	 * Define possible operations to CLI
	 *
	 * @author Marcos
	 *
	 */
	private enum OPERATION {
		CHECKOUT, COMMIT, DIFF, ADD, REMOVE, RENAME, NULL
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
		} else if(bDiff){
			return OPERATION.DIFF;
		}else if(bAdd){
			return OPERATION.ADD;
		}else if(bRemove){
			return OPERATION.REMOVE;
		}else if(bRename){
			return OPERATION.RENAME;
		}
		else
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
	 * @throws WorkspaceException 
	 */
	private Workspace createWorkspace(File path) throws WorkspaceException {
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
	 * Realiza um per-parsing para tratar dos casos fora do padrão javac.
	 * É uma gambiarra para eu não ter de mexer no args4j ou então trocá-lo por outra biblioteca.
	 * @param args os argumentos da aplicação
	 */
	static private String[] preParser(String[] args)
	{
		if(! args[0].startsWith("-"))
		{
			args[0] = "-" + args[0]; 			
		}
		
		LinkedList<String> list = new LinkedList<String>();
		for(int i = 0; i < args.length; i++)
		{
			String s = args[i];
			list.add(s);
			//trata do caso do -c com parâmetro default
			if(s.equals("-c"))
			{
				if(i+1 < args.length)
				{
					String nextS = args[i+1];
					if(!nextS.equals("l"))
						list.add("n");
				}else
				{
					list.add("n");
				}
			}
		}
		
		return list.toArray(new String[list.size()]);
	}

	/**
	 * Interpret and execute cli operations in the args array
	 * @param args
	 * 				String with comands (it will be tokenize)
	 */
	static public void doMain(String args)
	{
		StringTokenizer  tokens = new StringTokenizer(args);
		LinkedList<String> list = new LinkedList<String>();
		while(tokens.hasMoreTokens())
		{
			list.add(tokens.nextToken());
		}
		doMain(list.toArray(new String[list.size()]));
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
			
			args = preParser(args);
			// parse the arguments.
			parser.parseArgument(args);
			
			switch (getOperation()) {
			
			case DIFF:
				LinkedList<File> list = new LinkedList<File>();
				for(String s: this.listArguments)
					list.add(new File(s));
				onDiff(list);
			break;
			case ADD:
				if(listArguments.size() == 0)
				{
					System.err.println("É preciso especificar o nome do(s) arquivo(s) ou uma expressão regular");
				}else
				{
					
					onAdd(listArguments);
				}
				break;
			case REMOVE:
				if(listArguments.size() == 0)
				{
					System.err.println("É preciso especificar o nome do(s) arquivo(s) ou uma expressão regular");
				}else
				{
					
					onRemove(listArguments);
				}
				break;
				
			case RENAME:
				if(listArguments.size() != 2)
				{
					System.err.println("É preciso especificar o nome do arquivo de origem e o arquivo destino.");
					System.err.println("gardener rename nome_origem nome_destino");
				}else
				{
					
					File fileSource = new File(workspace.getPath(), listArguments.get(0));
					String strNewName = listArguments.get(1);
					if(!fileSource.exists())
					{
						System.err.printf("Arquivo %s não encontrado.\n", listArguments.get(0));
					}else
					{
						onRename(fileSource, strNewName);
					}
				}
				break;
				
			case CHECKOUT:
			case COMMIT:
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

				createWorkspace(new File(uriWorkspace));
				if(getOperation() == OPERATION.CHECKOUT)
					onCheckout();
				else
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
		 catch (TransationException e) {
				e.printStackTrace();
				return;
		}
	}

	
	private void onAdd(Collection<String> coll) throws WorkspaceException
	{
		if(coll.size() == 0)
		{
			System.err.printf("Nenhum arquivo encontrado");
			return;
		}
		
		LinkedList<File> listFile = new LinkedList<File>(); 
		for(String strGlob: coll)
		{
			UtilStream.findFiles(getWorkspace().getPath(), listFile, strGlob, false);
		}
		workspace.addFiles(listFile);
		
		for(File f: listFile)
		{
			System.out.printf("Arquivo %s adicionado", f.toString());			
		}
	}
	
	private void onRemove(Collection<String> coll) throws WorkspaceException
	{
		if(coll.size() == 0)
		{
			System.err.printf("Nenhum arquivo encontrado");
			return;
		}
		
		LinkedList<File> listFile = new LinkedList<File>(); 
		for(String strGlob: coll)
		{
			UtilStream.findFiles(getWorkspace().getPath(), listFile, strGlob, false);
		}
		workspace.removeFiles(listFile);
		
		for(File f: listFile)
		{
			System.out.printf("Arquivo %s removido", f.toString());
		}
	}

	private void onRename(File fileSource, String strNewName) throws WorkspaceException
	{
		workspace.renameFile(fileSource, strNewName);
		System.out.printf("Arquivo %s renomeado para %s", fileSource.toString(), strNewName);
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
	
	
	/**
	* O diff tem 4 formatos diferentes e umas configurações.
	*
	*	Segue os comandos...
	*
	*	•Ignorar Espaços em branco e “Tabs”
	* 	diff -E arquivo_1 arquivo_2
	*	
	*	•Ignorando Linhas em branco
	*	diff -B arquivo_1 arquivo_2
	*	
	*	•Ignorando diferenças letras maiúsculas e minúsculas (Case Sensitive)
	*	diff -i arquivo_1 arquivo_2
	*	
	*	•Formato Contexto
	*	diff -c arquivo_1 arquivo_2
	*	
	*	•Formato Menor Contexto
	*	diff -c l arquivo_1 arquivo_2
	*	
	*	•Formato Unificado
	*	diff -u arquivo_1 arquivo_2
	*	
	*	•Formato Normal
	*	diff arquivo_1 arquivo_2
	*/
	private void onDiff(Collection<File> collFiles ) throws TransationException
	{
		if(collFiles.size() != 2)
		{
			StringBuilder str = new StringBuilder();
			str.append("Não é possível comparar menos ou mais de dois arquivos: Foram especificados ");
			str.append(Integer.toString(collFiles.size()));
			str.append(" com os nomes:\n");
			for(File f: collFiles)
			{
				str.append(f.toString());
				str.append("\n");
			}
			
			throw new TransationException(str.toString());
		}
		
		//usem estas variáveis
		bDiffIgnoreWhiteSpaces = false;
			
		bDiffIgnoreWhiteLines = false;
			
		bDiffIgnoreCaseSensitive = false;
			 
		charDiffContextFormat = ' ';
			
		bDiffUnifiedFormat = false;
	}
}