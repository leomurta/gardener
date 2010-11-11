package br.uff.ic.gardener.workspace;

/**
 * Status do CI no Workspace
 * @author Marcos
 *
 */
enum Status
{
	UNVER("Unversioned", "UNVER"),
	ADD("Added", "ADD"),
	ADD_MISSED("Add Missed", "ADD_MISSED"),
	VER("Versioned", "VER"),
	VER_MISSED("Versioned Missed", "VER_MISSED"),
	MOD("Modified", "MOD"),
	RENAME("Renamed", "RENAME"),
	RENAME_MISSED("Renamed Missed", "RENAME_MISSED"),
	REM("Removed", "REM");
	
	private final String name;
	
	private final String label;
	
	Status(String _name, String _label)
	{
		name = _name;
		label = _label;
	}
	
	public final String getName()
	{
		return name;
	}
	
	public final String getLabel()
	{
		return label;
	}
	
	public String toString()
	{
		return getName();
	}
	
	public boolean isLabel(String o)
	{
		return this.getLabel().equals(o);
			
	}
	
	public static Status MissedStatus(Status s)
	{
		switch(s)
		{
			case ADD:
				return ADD_MISSED;
			case RENAME:
				return RENAME_MISSED;
			case VER:
			case MOD:
				return VER_MISSED;
			default:
				throw new IllegalArgumentException(String.format("Status %s does not have a missed status", s.getName()));
		}
	}

	public Status getMissed() {
		return MissedStatus(this);
	}
	
	
}
