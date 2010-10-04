package br.uff.ic.gardener;

/**
 * It Represents a revision I create it because I do not know if the revision
 * going to be a long or a hexa.
 * 
 * @author Marcos
 * 
 */
public class RevisionID implements Comparable<RevisionID> {

	/**
	 * The revision number
	 */
	private long revision;

	public final static RevisionID ZERO_REVISION = new RevisionID(0);

	public static final RevisionID LAST_REVISION = new RevisionID(-1);

	public RevisionID(long rev) {
		revision = rev;
	}

	/**
	 * Return the revision number. I do not implement the setNumber bacause I do
	 * not if it going to be a long or a hexa.
	 * 
	 * @return
	 */
	public long getNumber() {
		return revision;
	}

	/**
	 * Generate a new revision. In this time, I think that a new revision should
	 * be generate by the RevisionID because it is not responsibility of
	 * APIClient nor Workspace. Peharps it should be responsibility of APIServ,
	 * but i do not know. It too need a seed. If the revision going to be
	 * incremental, it shall use the last revision of serv. but it shall use the
	 * hex unique id, it shall used a time seed or similar.
	 * 
	 * @param seed
	 *            The seed used
	 * @return the new RevisionID
	 */
	public static RevisionID generateNewRevision(long seed) {
		return new RevisionID(++seed);
	}

	/**
	 * Convert to the format "Revision X"
	 */
	@Override
	public String toString() {
		return String.format("Revision %d", revision);
	}

	public static RevisionID fromString(String str) {
		if (str.toLowerCase().compareTo("last") == 0) {
			return RevisionID.LAST_REVISION;
		}
		if (str.toLowerCase().compareTo("zero") == 0) {
			return RevisionID.ZERO_REVISION;
		}

		int pos = str.lastIndexOf(' ');

		if (pos < str.length() - 1) {
			try {
				String number = str.substring(pos + 1);
				return new RevisionID(Long.parseLong(number));
			} catch (NumberFormatException e) {

			}
		} else {
			try {
				return new RevisionID(Long.parseLong(str));
			} catch (NumberFormatException e) {
			}
		}

		return RevisionID.ZERO_REVISION;
	}

	@Override
	public int compareTo(RevisionID arg0) {
		return (int) (getNumber() - arg0.getNumber());
	}
}
