package br.uff.ic.gardener.util;

import java.io.File;

/**
 * return false to files that couple with a regular expression.
 * 
 * @author Marcos Côrtes
 * 
 */
public class NotRegExpNameFileFilter extends RegExpNameFileFilter {

	String strRegExp = "";

	public NotRegExpNameFileFilter(String _regExp) {
		super(_regExp);
	}

	@Override
	public boolean accept(File pathname) {
		return !super.accept(pathname);
	}

}
