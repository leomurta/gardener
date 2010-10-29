package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;



public class GlobFilenameFilter implements FileFilter{

	AntPathMatcher matcher = new AntPathMatcher();
	String strPattern = "";
	boolean allowPath = false;
	public GlobFilenameFilter(String strPattern)
	{
		this(strPattern, false);
	}
	
	public GlobFilenameFilter(String _strPattern, boolean _allowPath)
	{
		strPattern = _strPattern;
		allowPath = _allowPath;
	}

	@Override
	public boolean accept(File file)
	{
		if(file.isDirectory() && allowPath)
			return true;
		else
		{
			String name = file.getName();
			return matcher.match(strPattern, name);
		}
	}
}
