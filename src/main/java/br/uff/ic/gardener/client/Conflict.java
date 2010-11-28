package br.uff.ic.gardener.client;

import java.net.URI;

/**
 * conflict between two files
 * @author Marcos
 *
 */
public class Conflict {
	
	private URI pathA;
	private URI pathB;
	
	public final URI getPathA() {
		return pathA;
	}

	public final URI getPathB() {
		return pathB;
	}
	
	public Conflict(URI A, URI B)
	{
		pathA = A;
		pathB = B;
	}
	
	
}
