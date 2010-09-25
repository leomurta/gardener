package br.uff.ic.gardener.client;

import java.io.InputStream;
import java.util.Map;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;

/**
 * API Client Interface
 * 
 * @author Marcos Côrtes
 */
public interface APIClient {
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
	void checkout(Map<String, InputStream> items, RevisionID revision)
			throws TransationException;

	/**
	 * Commit a new revision to the serv. it is the simple way.
	 * 
	 * @param items
	 * @return the new revision generate
	 * @throws TransationException
	 */
	RevisionID commit(Map<String, InputStream> items)
			throws TransationException;

	/**
	 * Return the last revision in the repository
	 * 
	 * @return the last revision in the repository
	 */
	RevisionID getLastRevision();

}
