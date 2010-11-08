package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Filter files with same name in _strName
 * @author Marcos
 *
 */
public class NameFileFilter implements FileFilter {

	String strName = "";

	public NameFileFilter(String _strName) {
		strName = _strName;
	}

	@Override
	public boolean accept(File pathname) {
		return pathname.getName().equals(strName);
	}

}
