package br.uff.ic.gardener;

import br.uff.ic.gardener.cli.CLI;

/**
 * Hello world!
 *
 */
public class App 
{
	/**
	 * call the CLI interface
	 * @param args the parameters of aplication
	 */
    public static void main( String[] args )
    {
    	CLI.doMain(args);
        //System.out.println( "Hello World!" );
    }
}
