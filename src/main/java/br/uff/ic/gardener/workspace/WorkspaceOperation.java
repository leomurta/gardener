package br.uff.ic.gardener.workspace;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa transações a serem realizadas em um workspace que devem ser commitadas
 * @author Marcos
 *
 */
public class WorkspaceOperation {
	public enum Operation
	{
		NONE(0, "000"),
		ADD_FILE(1, "+++"),
		REMOVE_FILE(2, "---"),
		RENAME_FILE(3, "+-RRR");
		
		private int id = 0;
		private String label = "";
		
		Operation(int _id, String _label){
			id = _id;
			label = _label;
		}
		
		public String getLabel()
		{
			return label;
		}
		
		public int getId()
		{
			return id;
		}
	}
	
	private Operation transation = Operation.NONE;
	
	private static ArrayList<String> DEFAULT_NULL_ARRAYLIST = new ArrayList<String>();
	
	private List<String> listParam = DEFAULT_NULL_ARRAYLIST; 
	
	
	WorkspaceOperation(Operation trans, String... parameters)
	{
		transation = trans;
		listParam = new ArrayList<String>(parameters.length);
		for(String s: parameters)
			listParam.add(s);
	}
	
	public String getParamAt(int pos)
	{
		return listParam.get(pos);
	}
	
	public int getParamQtd()
	{
		return listParam.size();
	}
	
	public Operation getOperation()
	{
		return transation;
	}
	
	@Override
	public String toString()
	{
		if(listParam.size() == 1)
		{
			return String.format("%s \"%s\"", getOperation().getLabel(), listParam.get(0));
		}else if(listParam.size() == 2)
		{
			return String.format("%s \"%s\" \"%s\"", getOperation().getLabel(), listParam.get(0), listParam.get(1));
		}else
		{
			StringBuilder sb = new StringBuilder();
			sb.append(getOperation().getLabel());
			sb.append(" ");
			for(String s: listParam)
			{
				sb.append("\"");
				sb.append(s);
				sb.append("\"");
				sb.append(" ");
			}
			return sb.toString();
		}
	}
	
	public boolean equals(Object o)
	{
		WorkspaceOperation op = (WorkspaceOperation)o;
		if(!op.getOperation().equals(this.getOperation()))
			return false;
		
		if(op.getParamQtd() != this.getParamQtd())
			return false;
		for(int i = 0; i < op.getParamQtd(); i++)
		{
			if(!op.getParamAt(i).equals(this.getParamAt(i)))
				return false;
		}
		return true;
		
		
	}
}
