package br.uff.ic.gardener.util;

import java.io.File;

/**
 * return false to files that couple with a regular expression.
 * 
 * @author Marcos C�rtes
 * 
 */
public class NotRegExpNameFileFilter extends RegExpNameFileFilter {


	public NotRegExpNameFileFilter(String _regExp) {
		super(_regExp);
	}
	
	public NotRegExpNameFileFilter(String _regExp, boolean allowPath) 
	{
		super(_regExp, allowPath);
	}
	@Override
	public boolean accept(File file)
	{
		return !super.accept(file);
		
	}

}
