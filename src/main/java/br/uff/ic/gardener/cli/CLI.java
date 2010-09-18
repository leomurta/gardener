/* UFF - Universidade Federal Fluminense
 *  IC - Instituto de Computação  
*/ 

package br.uff.ic.gardener.cli;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

import br.uff.ic.gardener.client.APIClient;
import br.uff.ic.gardener.client.ClientFactory;

/**
 * CLI class. Implements CLI interface with a singletons
 * @author Marcos Côrtes
 * @seealso https://args4j.dev.java.net/source/browse/args4j/args4j/examples/SampleMain.java?view=markup
 *
 */
public class CLI {

	
	@Option(name="-ch",aliases="-checkout", metaVar="CHECKOUT", usage="Checkout a Configuration Item in repository")
	private boolean bCheckout = false;
	
	@Option(name="-commit", aliases="-commit", metaVar="COMMIT", usage="Commit a Configuration Item in repository")
	private boolean bCommit = false;
	
	// Other arguments
    @Argument
    private List<String> listArguments = new ArrayList<String>();

    @Option(name="-ws", aliases="-workspace", metaVar="WORKSPACE", usage="Specify the source of ICs (URI or file")
    private URI uriWorkspace = null;
    
   
    /**
     * Define possible operations to CLI
     * @author Marcos
     *
     */
    private enum OPERATION{
    	CHECKOUT,
    	COMMIT,
    	NULL
    }
	
   // private OPERATION operation = OPERATION.NULL;
    
//    private void setOperation(OPERATION op){
//    	operation = op;
//    }
    
    private OPERATION getOperation(){
		
    	if(bCheckout){
    		return OPERATION.CHECKOUT;
    	}
    	else if(bCommit){
    		return OPERATION.COMMIT;
    	}else
    		return OPERATION.NULL;
    	
    	
    }
    
    private static APIClient apiClient = null; 
    public static APIClient getClient()
    {
    	if(apiClient == null)
    		apiClient = ClientFactory.createAPIClient();
    	return apiClient;
    }
    
	
	static private CLI cliSingletons = null;
	
	synchronized static public CLI me()
	{
		if(cliSingletons == null)
			cliSingletons = new CLI();
			
		return cliSingletons;
	}
	
	/** Interpret and execute cli operations in the args array
	* @param args Array of strings containing the parameters
	*/
	static public void doMain(String[] args)
	{
		me().internalDoMain(args);
	}
	
	/**
	 * Return actual path
	 * @return
	 */
	static File getActualPath()
	{
		 String str = System.getProperty("user.dir");
		 File dir = new File(str);
	     return dir;
	}
	//private output(String
			
	/**
	 * Realize main application. It is needed to create the singletons.
	 * @param args Parâmetros da aplicação
	 */
	private void internalDoMain(String[] args){
		CmdLineParser parser = new CmdLineParser(this);
		 
		//default destiny
		 me().uriWorkspace = CLI.getActualPath().toURI();
	    
		//default length of execution line
	    parser.setUsageWidth(80);
	
	    try {
	        // parse the arguments.
	        parser.parseArgument(args);
	        
	        // after parsing arguments, you should check
            // if enough arguments are given.
         //   if( listArguments.isEmpty() )
           //     throw new CmdLineException("No argument is given");
	        
	        
	        switch(getOperation())
	        {
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
	    } catch( CmdLineException e ) {
	        // if there's a problem in the command line,
	        // you'll get this exception. this will report
	        // an error message.
	        System.err.println(e.getMessage());
	       // System.err.println("gardener -[checkout|commit] [-workspace]:%%path%%");
	
	        // print the list of available options
	        parser.printUsage(System.err);
	        System.err.println();
	
	        // print option sample. This is useful some time
	        System.err.println("  Exemplo: gardener"+parser.printExample(ExampleMode.ALL));
	
	        return;
	    }
	}
	    
	    
	/**
	 * Commit event
	 */
    private void onCommit()
    {
    	
    }
    
    /**
     * Checkout event
     */
    private void onCheckout()
    {    	
    }   
}
