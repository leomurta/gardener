package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Select files witch the name is not equal to list of names
 * @author Marcos
 *
 */
public class NotFilenameFilter implements FileFilter {

	String[] names;
	
	public NotFilenameFilter(String... _names)
	{
		names = _names;
	}
	
	@Override
	public boolean accept(File pathname) {
		for(String s: names)
		{
			if(s.equals(pathname.getName()))
			return false;
		}
		
		return true;
	}

}
