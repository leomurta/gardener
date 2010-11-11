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
	public static CIWorkspaceStatus NEVER_EQUAL_STATUS = new CIWorkspaceStatus(null, null, null);
	
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
	
	public CIWorkspaceStatus(CIWorkspaceStatus ciw, Status otherStatus) 
	{
		super(ciw);
		oldURI = ciw.getOldURI();
		status = otherStatus;
	}
	
	public CIWorkspaceStatus(URI _uri, InputStream _stream, Status _status) {
		super(_uri, _stream);
		status = _status;
	}
	
	public CIWorkspaceStatus(URI _uri, InputStream _stream, Status _status, Date date, URI _oldURI) {
		super(_uri, _stream, date);
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
}
