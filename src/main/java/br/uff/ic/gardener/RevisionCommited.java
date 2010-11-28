package br.uff.ic.gardener;

import java.util.Date;

/**
 * Represent a revision commited to System
 * @author Marcos
 *
 */
public class RevisionCommited {

	private String user;
	
	private Date dateCommit;
	
	private String message;
	
	private RevisionID id;

	public RevisionCommited(RevisionID _id, String _user, String _msg, Date _date)
	{
		user = _user;
		id = _id;
		dateCommit = _date;
		message = _msg;
	}
	public final String getUser() {
		return user;
	}

	public final Date getDateCommit() {
		return dateCommit;
	}

	public final String getMessage() {
		return message;
	}

	public final RevisionID getId() {
		return id;
	}
}
