package br.uff.ic.gardener.merge;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;


public interface IMerge {
	public File merge(File base, File file1, File file2);
	public OutputStream merge(InputStream base, InputStream file1, InputStream file2);
}
