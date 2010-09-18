package br.uff.ic.gardener.client;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import br.uff.ic.gardener.TransationException;

public interface APIClient 
{
	/**
	 * Commit the modifications in repository.
	 * @param eraseSubItems SubItems removed.
	 * @param includeSubItems SubItems Included.
	 * @param modifyItems SubItems modified
	 * @throws TransationException Throw the exception in error in the middle of transation.
	 */
	void commit(Collection<String> eraseSubItems, Map<String, OutputStream> includeSubItems, Map<String, InputStream> modifyItems) throws TransationException;
	
	/**
	 * CheckOut a revision from the serv.
	 * @param eraseSubItems SubItems removed.
	 * @param includeSubItems SubItems Included.
	 * @param modifyItems SubItems modified
	 * @param actualRevision Actual revision.
	 * @param requestRevision the revision requested to checkout.
	 * @throws TransationException
	 */	 
	void checkout(Collection<String> eraseSubItems, Map<String, InputStream> includeSubItems, Map<String, InputStream>  modifySubItems, long actualRevision, long requestRevision) throws TransationException;
	
	/**
	 * CheckOut a revision from the serv.
	 * @param items the items of revision.
	 * @param revision the revision number.
	 */
	void checkout(Map<String, InputStream> items, long revision);
}
