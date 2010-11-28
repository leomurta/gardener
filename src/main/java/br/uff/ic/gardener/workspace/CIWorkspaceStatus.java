package br.uff.ic.gardener.workspace;

import java.io.InputStream;
import java.net.URI;
import java.util.Date;

/**
 * Classe que representa o status de um CI ("arquivo") no workspace.
 * Não quis herdar do ConfigurationItem pq ele ainda está em desenvolvimento e não sei o que esta classe e aquela terão em comum.
 * @author Marcos
 *
 */
public class CIWorkspaceStatus extends CIWorkspace
{		
	
	/**
	 * Status of CI
	 */	
	private Status status = Status.UNVER;
	
	public static final String NEVER_EQUAL = null;

	
	private URI oldURI = null;
	

	public CIWorkspaceStatus(CIWorkspaceStatus ciw) 
	{
		super(ciw);
		oldURI = ciw.getOldURI();
		status = ciw.getStatus();
	}
	

	public CIWorkspaceStatus(CIWorkspace ciw, Status s) 
	{
		super(ciw);
		oldURI = null;
		status = s;
	}
	
	public CIWorkspaceStatus(CIWorkspaceStatus ciw, Status otherStatus) 
	{
		super(ciw);
		oldURI = ciw.getOldURI();
		status = otherStatus;
	}
	
	public CIWorkspaceStatus(URI _uri, Status _status) {
		super(_uri);
		status = _status;
	}
	
	public CIWorkspaceStatus(URI _uri, Status _status, Date date, URI _oldURI) {
		super(_uri,  date);
		status = _status;
		oldURI =  _oldURI;
		
	}

	public Status getStatus()
	{
		return status;
	}
	
	public URI getOldURI()
	{
		return oldURI;
	}
	
	@Override
	public String toString()
	{
		if(this.getURI() == null /*&& this.getInputStream() == null*/)
			return "NeverEqual";
		
		return String.format("%s \"%s\" \"%s\"", 
					getStatus().getLabel()!=null?getStatus().getLabel():"null", 
					getURI()!=null?getURI():"null",
					this.getOldURI()!=null?getOldURI():"null");
	}
}
