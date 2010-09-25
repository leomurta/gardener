package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;

/**
 * return true to files that couple with a regular expression.
 * 
 * @author Marcos
 * 
 */
public class RegExpNameFileFilter implements FileFilter {

	String strRegExp = "";

	public RegExpNameFileFilter(String _regExp) {
		strRegExp = _regExp;
	}

	@Override
	public boolean accept(File pathname) {
		return strRegExp.matches(strRegExp);
	}

}
