
/* UFF - Universidade Federal Fluminense
 *  IC - Instituto de Computação
 */

package br.uff.ic.gardener.cli;

import br.uff.ic.gardener.diff.DiffException;
import java.io.File;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;

import com.mongodb.io.StreamUtil;

import br.uff.ic.gardener.RevisionCommited;
import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;
import br.uff.ic.gardener.client.APIClient;
import br.uff.ic.gardener.client.APIClientException;
import br.uff.ic.gardener.comm.ComClientException;
import br.uff.ic.gardener.diff.Diff;
import br.uff.ic.gardener.util.ANDFileFilter;
import br.uff.ic.gardener.util.FileHelper;
import br.uff.ic.gardener.util.GlobFilenameFilter;
import br.uff.ic.gardener.util.NotDirectoryFileFilter;
import br.uff.ic.gardener.util.TokenizerWithQuote;
import br.uff.ic.gardener.util.UtilStream;
import br.uff.ic.gardener.workspace.CIWorkspaceStatus;
import br.uff.ic.gardener.workspace.WorkspaceException;
import br.uff.ic.gardener.workspace.WorkspaceOperation.Operation;


/**
 * CLI class. Implements CLI interface with a singletons
 *
 * @author Marcos Côrtes
 * @seealso	{@link https://args4j.dev.java.net/source/browse/args4j/args4j/examples/SampleMain.java?view=markup}
 *
 */
public class CLI {

	@Option(name = "-init", aliases = "--init", metaVar = "INIT", usage = "Init a new project in the serv and create a workspace in the current path")
	private boolean bInit = false;
	
	@Option(name = "-co", aliases = "--checkout", metaVar = "CHECKOUT", usage = "Checkout a Configuration Item from a repository to a new workspace")
	private boolean bCheckout = false;
	
	@Option(name = "-up", aliases = "--update", metaVar = "UPDATE", usage = "Update current version of workspace to the last version")
	private boolean bUpdate = false;

	@Option(name = "-ci", aliases = "--commit", metaVar = "COMMIT", usage = "Commit a Configuration Item in repository")
	private boolean bCommit = false;

	@Option(name = "-add", aliases = "--add", metaVar = "ADD", usage = "Add a Configuration Item (it can use prompt regular expression)")
	private boolean bAdd = false;
	
	@Option(name = "-rem", aliases = "--remove", metaVar = "REMOVE", usage = "Remove a Configuration Item (it can use prompt regular expression)")
	private boolean bRemove = false;
	
	@Option(name = "-rename", aliases = "--rename", metaVar = "RENAME", usage = "Rename a file in the workspace")
	private boolean bRename = false;
	
	@Option(name = "-status", aliases = "--status", metaVar = "STATUS", usage = "Show status of workspace")
	private boolean bStatus = false;
	
	@Option(name = "-log", aliases = "--log", metaVar = "LOG", usage = "Show log of system")
	private boolean bLog = false;
	
	@Option(name = "-m", aliases = "--message", metaVar = "MESSAGE", usage="Message to transations (Checkout, Checkin)")
	private String strMessage = "";
	
	@Option(name = "-user", aliases = "--user", metaVar= "USER", usage="The user of manager the workspace. it is usage to send the user information to the serv in a commit")
	private String strUser = null;
	
	// Other arguments
	@Argument
	private List<String> listArguments = new ArrayList<String>();

	@Option(name = "-w", aliases = "--workspace", metaVar = "WORKSPACE", usage = "Specify the source of ICs (URI or path)")
	private File pathWorkspace = null;

	@Option(name = "-s", aliases = "--serv", metaVar = "SERV", usage = "Specify the serv")
	private URI uriServ = null;

	@SuppressWarnings("unused")
	@Option(name = "-r", aliases = "--revision", metaVar = "REVISION", usage = "Specify the revision in checkout")
	private void setRevision(String strRevision) {
		int pos = -1;
		if((pos = strRevision.indexOf(':')) > 0)
		{
			String temp1 = strRevision.substring(0,pos);
			String temp2 = strRevision.substring(pos+1, strRevision.length());
			revision = RevisionID.fromString(temp1);
			lastRevision = RevisionID.fromString(temp2);
		}else
		{
			revision = RevisionID.fromString(strRevision);
			lastRevision = null;
		}
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
	 
	@Option(name = "-c", aliases="--ContextFormat", metaVar="DIFF_CONTEXT_FORMAT", usage = "Select Context Format")
	private char charDiffContextFormat = ' ';
	
	@Option(name = "-u", aliases="--UnifiedFormat", metaVar="DIFF_UNIFIED_FORMAT", usage = "Use Unified Format")
	private boolean bDiffUnifiedFormat = false;
	
	/**
	 * Revision or first revision
	 */
	private RevisionID revision = RevisionID.LAST_REVISION;
	
	private RevisionID lastRevision = null;

	/**
	 * Define possible operations to CLI
	 *
	 * @author Marcos
	 *
	 */
	private enum OPERATION {
		INIT, CHECKOUT, COMMIT, UPDATE, DIFF, ADD, REMOVE, RENAME, STATUS, LOG, NULL
	}

	// private OPERATION operation = OPERATION.NULL;

	// private void setOperation(OPERATION op){
	// operation = op;
	// }

	private OPERATION getOperation() {
		
		if(bInit){
			return OPERATION.INIT;
		}else if (bCheckout) {
			return OPERATION.CHECKOUT;
		
		} else if (bUpdate) {
			return OPERATION.UPDATE;
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
		}else if(bStatus){
			return OPERATION.STATUS;
		}else if(bLog)
		{
			return OPERATION.LOG;
		}
		else
			return OPERATION.NULL;
	}

	/**
	 * Instance of APIClient
	 */
	private APIClient apiClient = null;
	

	public APIClient getClient() {
		if (apiClient == null) {
			try {
				if(uriServ != null)
					apiClient = new APIClient(pathWorkspace, uriServ);
				else
					apiClient = new APIClient(pathWorkspace);
			} catch (APIClientException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
		return apiClient;
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
		if(args.length == 0)
			return args;
		
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
	 * Do the main and parse the arg parameter with shall be String.format formatation.
	 * @param arg
	 * @param formatArgs
	 */
	static public void doMain(String arg, Object... formatArgs)
	{
		doMain(String.format(arg, formatArgs));
	}
	/**
	 * Interpret and execute cli operations in the args array
	 * @param args
	 * 				String with comands (it will be tokenize)
	 */
	static public void doMain(String args)
	{
		TokenizerWithQuote  tokens = new TokenizerWithQuote(args);
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
		resetCLI();
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

		if(strUser != null)
			getClient().setUser(strUser);
		
		// default destiny
		pathWorkspace = CLI.getActualPath();
				
		// default length of execution line
		parser.setUsageWidth(80);

		try {
			
			args = preParser(args);
			// parse the arguments.
			parser.parseArgument(args);

			if (uriServ != null) {
				// trata URI sem file://
				if (uriServ.getScheme() == null || uriServ.getScheme() == "") {
					uriServ = (new File(uriServ).toURI());
				}
			} else {
				uriServ = pathWorkspace.toURI();
			}
			
			switch (getOperation()) {
			case NULL:
				System.out.println("Specify a command: ");
				for(OPERATION op: OPERATION.values())
				{
					System.out.println(op.toString() +  ", ");
				}
			break;
			case INIT:
				if(listArguments.size() == 0)
				{
					System.err.println("Specify the project name to init.");
					break;
				}
				onInit(listArguments.get(0));
				break;
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
					
					File fileSource = new File(pathWorkspace, listArguments.get(0));
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
			
			case UPDATE:
					
			onUpdate();
			
			break;
			case CHECKOUT:
					onCheckout();
			break;
			case COMMIT:
					onCommit();
			break;
			
			case STATUS:
				onStatus();
			break;
			case LOG:
				onLog();
			break;
			default:
				// this will redirect the output to the specified output
				parser.printUsage(System.out);
				break;
			}
		} catch (CmdLineException e) {
			printError("Invalid command line", e, parser);
		}
		catch (WorkspaceException e) {
			 printError("Workspace error", e, parser);
		}
		catch (TransationException e) {
			 printError("Transation error", e, parser);
		}
		catch(APIClientException e)
		{
			printError("Controller error:", e, parser);
		} catch (ComClientException e) {
			printError("ComClinet error:", e, parser);
		}
	}
	
	private void onStatus() throws WorkspaceException, APIClientException {
		Collection<CIWorkspaceStatus> coll = new LinkedList<CIWorkspaceStatus>();
		getClient().status(coll);
		
		for(CIWorkspaceStatus ci: coll)
		{
			System.out.println(ci.toString());
		}		
	}
	
	private void onLog() throws APIClientException, ComClientException
	{
		LinkedList<RevisionCommited> list = new LinkedList<RevisionCommited>();
		getClient().generateLog(list, revision, lastRevision);
		
		for(RevisionCommited rc: list)
		{
			String s = rc.getId().toString();
			System.out.printf("Revision :%s%s", s, UtilStream.getLineSeperator());
			DateFormat d = new SimpleDateFormat();
			System.out.printf("\tDate   : %s%s", d.format(rc.getDateCommit()), UtilStream.getLineSeperator());
			System.out.printf("\tUser   : %s%s", rc.getUser(), UtilStream.getLineSeperator());
			System.out.printf("\tMessage: %s%s", rc.getMessage(), UtilStream.getLineSeperator());
		}
	}

	/**
	 * Reset CLI Commands
	 */
	private static void resetCLI() 
	{
		cliSingletons.forceCloseSystem();
		cliSingletons = null;
		CLI.me();
	}

	private static void printError(String msg, Throwable error, CmdLineParser parser)
	{
		System.err.println(String.format("%s: %s%s%s%s", msg, error.getMessage(), UtilStream.getLineSeperator()
				,"================================================================" 
				,UtilStream.getLineSeperator()));
		
		System.err.println(String.format("Exception messages:"));
		Throwable t = error;
		while(t != null)
		{
			System.err.println(String.format("\t%s", t.toString()));
			t= t.getCause();
		}
		System.err.println(String.format("%s%s%s", UtilStream.getLineSeperator()
				,UtilStream.getLineSeperator()
				,"================================================================"
				,UtilStream.getLineSeperator()
				));
		System.err.println("Possible Commands:");
		
		
		parser.printUsage(System.err);
		System.err.println(parser.printExample(ExampleMode.ALL));
		
		// if there's a problem in the command line,
		// you'll get this exception. this will report
		// an error message.
		//System.err.println(e.getMessage());
		// System.err.println("gardener -[checkout|commit] [-workspace]:%%path%%");

		// print the list of available options
		//parser.printUsage(System.err);
		//System.err.println();

		// print option sample. This is useful some time
	}

	
	//==============================================================
	//==============================================================
	//events to Commands
	//==============================================================
	
	private void onInit(String string) throws APIClientException, WorkspaceException {
		getClient().init(string);
	}

	private void onUpdate() throws TransationException
	{
		getClient().update();
	}
	
	/**
 	 * Commit event
	 *
	 * @throws WorkspaceException
	 */
	private void onCommit() throws TransationException {
			getClient().commit(this.strMessage);
	}

	/**
	 * Checkout event
	 *
	 * @throws TransationException 
	 */
	private void onCheckout() throws TransationException {
		getClient().checkout(revision);
	}
	
	
	private void onAdd(Collection<String> coll) throws WorkspaceException, APIClientException
	{
		if(coll.size() == 0)
		{
			System.err.printf("Nenhum arquivo encontrado");
			return;
		}
		
		LinkedList<File> listFile = new LinkedList<File>(); 
		for(String strGlob: coll)
		{
			FileHelper.findFiles(
					pathWorkspace, 
					listFile, 
					new ANDFileFilter(getClient().getWorkspaceNotFileConfigFilter(), new GlobFilenameFilter(strGlob),
					new NotDirectoryFileFilter()
					));
		}

		getClient().addFiles(listFile);
		
		URI uriBase = pathWorkspace.toURI();
		
		for(File f: listFile)
		{
			URI uri = FileHelper.getRelative(uriBase,f.toURI());
			System.out.println(String.format("Arquivo %s adicionado", uri.getPath()));			
		}
	}
	
	private void onRemove(Collection<String> coll) throws WorkspaceException, APIClientException
	{
		if(coll.size() == 0)
		{
			System.err.printf("Nenhum arquivo encontrado");
			return;
		}
		
		LinkedList<File> listFile = new LinkedList<File>(); 
		for(String strGlob: coll)
		{
			FileHelper.findFiles(CLI.getActualPath(), listFile, strGlob, false);
		}
		getClient().removeFiles(listFile);
		
		for(File f: listFile)
		{
			System.out.printf("Arquivo %s removido", f.toString());
		}
	}

	private void onRename(File fileSource, String strNewName) throws APIClientException, WorkspaceException
	{
		getClient().renameFile(fileSource, strNewName);
		System.out.printf("Arquivo %s renomeado para %s", fileSource.toString(), strNewName);
	}
	
	/**
	* O diff tem 4 formatos diferentes e umas configurações.
	*
	*	Segue os comandos...
	*
	*	•Ignorar Espaços em branco e Tabs
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
	private void onDiff(List<File> collFiles ) throws TransationException
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
		
                try {
                    Diff diff = new Diff(collFiles.get(0), collFiles.get(1), getContextFormat() );
                    diff.setOutputFormat();
                } catch (DiffException ex) {
                    throw new TransationException("Erro fatal durante execução do diff", ex);
                }
        
		//usem estas variáveis
		bDiffIgnoreWhiteSpaces = false;
			
		bDiffIgnoreWhiteLines = false;
			
		bDiffIgnoreCaseSensitive = false;
			 
		charDiffContextFormat = ' ';
			
		bDiffUnifiedFormat = false;
	}

	
	/**Return the format for diff
	 *   case 'c': //CONTEXT-FORMAT
            return getContextFormatter();
        case 'l': // LESS-CONTEXT-FORMAT
            return getLessContextFormatter();
        case 'f': //FULL-CONTEXT-FORMAT
            return getFullContextFormatter();
        case 'n':  //NORMAL-FORMAT
            return getNormalFormatter();
        case 'u':  //UNIFIED-FORMAT
            return getUnifiedFormatter();
	 */
	private char getContextFormat() {
		if(bDiffUnifiedFormat)
			return 'u';
		else if(charDiffContextFormat != ' ')
		{
			if(charDiffContextFormat == 'l')
				return 'l';
			else if(charDiffContextFormat == 'f')
				return 'f';
			else
				return 'c';
		}
		else
			return 'n';
	}

	private void forceCloseSystem() {
		try {
			this.getClient().forceClose();
		} catch (WorkspaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (APIClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}