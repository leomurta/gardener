package br.uff.ic.gardener;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.uff.ic.gardener.cli.CLITest;
import br.uff.ic.gardener.client.ClientFactoryTest;
import br.uff.ic.gardener.client.LocalAPIClientTest;
import br.uff.ic.gardener.util.TokenizerWithQuoteTest;
import br.uff.ic.gardener.workspace.WorkspaceConfigParserTest;
import br.uff.ic.gardener.workspace.WorkspaceTest;

@RunWith(Suite.class)
@SuiteClasses({ WorkspaceTest.class, CLITest.class,  ClientFactoryTest.class, LocalAPIClientTest.class, TokenizerWithQuoteTest.class, WorkspaceTest.class, WorkspaceConfigParserTest.class})
public class AllTests {
}