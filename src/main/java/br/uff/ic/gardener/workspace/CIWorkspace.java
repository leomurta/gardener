package br.uff.ic.gardener.workspace;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;

/**
 * The class represent a CI in the workspace context
 * @author Marcos
 *
 */
public class CIWorkspace implements Comparable<CIWorkspace>
{
	
	public static CIWorkspace NEVER_EQUAL = new CIWorkspace(null, null);
	
	/**
	 * uri of CI
	 */
	private URI uri = null;
	
	/**
	 * Date modification of CI 
	 */
	Date dateModified = null;
	
	/**
	 * InputStream source of CI
	 * 	 */
	private InputStream inputStream;
	
	
	public CIWorkspace(URI _uri)
	{
		uri = _uri;
		inputStream = null;
	}
	
	public CIWorkspace(CIWorkspace other)
	{
		uri = other.getURI();
		inputStream = other.getInputStream();
		dateModified = other.getDateModified();
	}
	public CIWorkspace(URI _uri, InputStream _stream)
	{
		uri = _uri;
		inputStream = _stream;
	}
	
	public CIWorkspace(URI _uri, InputStream _stream, Date date)
	{
		uri = _uri;
		inputStream = _stream;
		dateModified = date;
	}
	
	/**
	 * URI of CI
	 * @return
	 */
	public URI getURI()
	{
		return uri;
	}
	
	public String getStringID()
	{
		return uri.getPath();
	}
	
	public InputStream getInputStream()
	{
		return inputStream;
	}
	@Override
	public int compareTo(CIWorkspace arg0) {
		return this.getURI().compareTo(arg0.getURI());
	}
	
	public Date getDateModified()
	{
		return dateModified;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CIWorkspace)
		{
			CIWorkspace ciw = (CIWorkspace)obj;
			if(ciw.getURI() == null && ciw.getInputStream()==null)
				return false;//neverEqual
			else
				return getURI().equals(ciw.getURI());
		}else
		{
			return false;
		}
	}
}
