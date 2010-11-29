package br.uff.ic.gardener.workspace;

import java.net.URI;
import java.util.Date;

/**
 * The class represent a CI in the workspace context
 * @author Marcos
 *
 */
public class CIWorkspace implements Comparable<CIWorkspace>
{
	
//	public static CIWorkspace NEVER_EQUAL = new CIWorkspace(null, null);
	
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
	//private InputStream inputStream;
	
	
	public CIWorkspace(URI _uri)
	{
		uri = _uri;
	//	inputStream = null;
	}
	
	public CIWorkspace(CIWorkspace other)
	{
		uri = other.getURI();
	//	inputStream = other.getInputStream();
		dateModified = other.getDateModified();
	}
	
	
	public CIWorkspace(URI _uri, Date date)
	{
		uri = _uri;
	//	inputStream = _stream;
		dateModified = date;
	}
	
	public CIWorkspace(CIWorkspace current, URI newURI) {
		this(current);
		uri = newURI;
		
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
	
/*	public InputStream getInputStream()
	{
		return inputStream;
	}
	*/
	@Override
	public int compareTo(CIWorkspace arg0)
	{
		if(this == null)
		{
			if(arg0 == null)
				return 0;
			else
				return -1;
		}else
		{
			if(arg0 == null)
				return 1;
		}
		
		if(getURI() != null && arg0.getURI() != null)
			return this.getURI().getPath().compareTo(arg0.getURI().getPath());
		else
		{
			if(getURI() != null)
				return 1;
			else if(arg0.getURI() != null)
				return -1;
			else
				return 0;
		}
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s)", uri.getPath());
	}
	
	public Date getDateModified()
	{
		return dateModified;
	}
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CIWorkspace)
			return compareTo((CIWorkspace)obj) == 0;
		else
			return false;
	}
}
