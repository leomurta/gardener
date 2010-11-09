package br.uff.ic.gardener.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Neg the other FileFilter
 * @author Marcos
 *
 */
public class NotFileFilter implements FileFilter {

	FileFilter other;
	public NotFileFilter(FileFilter _other)
	{
		other = _other;
	}
	@Override
	public boolean accept(File arg0) {
		
		return !other.accept(arg0);
	}
}
