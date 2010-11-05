package br.uff.ic.gardener.comm;

import java.io.Closeable;
import java.io.InputStream;
import java.net.URI;
import java.util.Map;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;

/**
 * API Client Interface
 * 
 * @author Vitor Neves
 */
public interface ComClient extends Closeable {
	/**
	 * CheckOut a revision from the serv. It is the simple way.
	 * 
	 * @param items
	 *            the items of revision.
	 * @param revision
	 *            the revision number.
	 * @throw TransationException it throws when the system cannot checkout
	 *        data. It will have a message of the exception
	 */
	void checkout(RevisionID revision, Map<String, InputStream> items)
			throws ComClientException;

	/**
	 * Commit a new revision to the serv. it is the simple way.
	 * @param strProject TODO
	 * @param strMessage TODO
	 * @param items
	 * @return the new revision generate
	 * @throws TransationException
	 */
	RevisionID commit(String strProject, String strMessage, Map<String, InputStream> items)
			throws ComClientException;

	/**
	 * Return the URI serv
	 * @return
	 */
	URI getURIServ();
	
	/**
	 * Close the connection
	 */
	public void close();
	
	
	/**
	 * Init a new versioned project in the serv 
	 * @param strProject
	 */
	public void init(String strProject);

	/**
	 * Return the last revision of a project at serv
	 * @return
	 */
	RevisionID getLastRevision(String strProject);
	

}
