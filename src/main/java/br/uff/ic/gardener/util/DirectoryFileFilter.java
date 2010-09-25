package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;

/**
 * This Class check if a File is a directory
 * @author Marcos
 *
 */
public class DirectoryFileFilter implements FileFilter {

	@Override
	public boolean accept(File pathname) 
	{
		return pathname.isDirectory();
	}

}
