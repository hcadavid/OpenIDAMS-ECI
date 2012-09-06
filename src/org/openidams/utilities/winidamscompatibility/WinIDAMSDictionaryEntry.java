package org.openidams.utilities.winidamscompatibility;

public class WinIDAMSDictionaryEntry {

	private String varId;
	
	private String name;
	
	private int position;
	
	private int lenght;
	
	private boolean decimals;
	
	private boolean alphanumeric;

	/**
	 * @param id
	 * @param position
	 * @param lenght
	 * @param decimals
	 * @param alphanumeric
	 */
	protected WinIDAMSDictionaryEntry(String id, String name,int position, int lenght, boolean decimals, boolean alphanumeric) {
		varId = id;
		this.position = position;
		this.name=name;
		this.lenght = lenght;
		this.decimals = decimals;
		this.alphanumeric = alphanumeric;
	}

	/**
	 * @return Returns the alphanumeric.
	 */
	public boolean isAlphanumeric() {
		return alphanumeric;
	}

	/**
	 * @return Returns the decimals.
	 */
	public boolean hasDecimals() {
		return decimals;
	}

	/**
	 * @return Returns the lenght.
	 */
	public int getLenght() {
		return lenght;
	}

	/**
	 * @return Returns the position.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return Returns the varId.
	 */
	public String getVarId() {
		return varId;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ""+getVarId()+","
				 +getName()+","
				 +getPosition()+","
				 +getLenght()+","
				 +hasDecimals()+","
				 +isAlphanumeric()+"\n";
	}
	
	

}
