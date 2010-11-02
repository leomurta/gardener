package br.uff.ic.gardener.comm;

import java.io.InputStream;
import java.util.Map;

import br.uff.ic.gardener.RevisionID;
import br.uff.ic.gardener.TransationException;

/**
 * API Client Interface
 * 
 * @author Vitor Neves
 */
public interface ComClient {
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

}
