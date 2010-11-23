package br.uff.ic.gardener.comm;

import java.io.Closeable;
import java.net.URI;
import java.util.Collection;

import br.uff.ic.gardener.ConfigurationItem;
import br.uff.ic.gardener.RevisionID;

/**
 * API Client Interface
 * 
 * @author Vitor Neves
 */
public interface ComClient extends Closeable {
	/**
	 * Commit a new revision to the serv. It is the simple way.
	 * @param strProject The name of project in the serv
	 * @param strMessage The message usage in the revision commited
	 * @param items the collection of ConfigurationItems
	 * @return the new revision generate
	 * @throws ComClientException
	 */
	RevisionID commit(String strProject, String strMessage, Collection<ConfigurationItem> items)
			throws ComClientException;
	
	/**
	 * Checkout a revision from repository
	 * @param strProject The name of project in the serv
	 * @param strMessage The message usage in the revision commited
	 * @param items the collection of ConfigurationItems that receive data
	 * @return the real revision checkouted
	 * @throws ComClientException
	 */
	RevisionID checkout(String strProject, RevisionID revision, Collection<ConfigurationItem> items)
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
