package br.uff.ic.gardener;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.uff.ic.gardener.cli.CLITest;
import br.uff.ic.gardener.workspace.WorkspaceTest;

@RunWith(Suite.class)
@SuiteClasses( { WorkspaceTest.class,
	CLITest.class })
public class AllTests 
{	
}