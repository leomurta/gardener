package br.uff.ic.gardener.merge;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;


public interface IMerge {
	public Boolean merge(File file1, File file2, File destiny) throws MergeException;
	public Boolean merge(File file1, File file2, File baseFile, File destiny) throws MergeException;

	public OutputStream merge(InputStream file1, InputStream file2) throws MergeException;
	public OutputStream merge(InputStream file1, InputStream file2, InputStream baseFile) throws MergeException;
}
