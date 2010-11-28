package br.uff.ic.gardener.workspace;

/**
 * Uma excecao do workspace
 *
 * @author Marcos
 *
 */
public class WorkspaceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2251516287838447597L;
	
	/**
	 * the CI envolved in the exception
	 */
	CIWorkspaceStatus ciCurrent = null;

	/**
	 * COnstrutor
	 * 
	 * @param file
	 *            Determined file for workspace
	 * @param msg
	 *            The message error
	 * @param parent
	 *            the throwable partent
	 */
	public WorkspaceException(CIWorkspaceStatus ci, String msg, Throwable parent) {
		this(msg, parent);
		ciCurrent = ci;
	}
	
	public WorkspaceException(String msg, Throwable parent)
	{
		super(msg, parent);
		ciCurrent = null;
	}
	
	public String toString()
	{
		return String.format("%s: CI=>%s; parent=>%s", ciCurrent.toString(), this.getCause()!=null?getCause():"null");
	}
}


//~ Formatted by Jindent --- http://www.jindent.com
