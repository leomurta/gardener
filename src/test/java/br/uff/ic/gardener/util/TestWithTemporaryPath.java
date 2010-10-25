package br.uff.ic.gardener.util;

import java.io.File;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class TestWithTemporaryPath {

	/**
	 * Temporary directory to use
	 */
	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	private File path = null;

	protected File getPath() {
		if (path == null)
			path = folder.newFolder(this.getClass().getName());
		return path;
	}
}
