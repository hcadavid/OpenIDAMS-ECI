/**
 * Copyright (C) 2006  OpenIDAMS ECITeam 
 * ECITeam: Gerardo Ospina, HŽctor Cadavid, Camilo Rocha
 * <gospina,hcadavid,hrocha@escuelaing.edu.co>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package script.openidams.compiler;

/**
 * This is the class that defines a procedure
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class ProcedureProperties implements Cloneable{

    /**
     * Procedure name
     */
    private String name;

    /**
     * Is a two level procedure?
     */
    private boolean twoLevel;

    /**
     * String indicating the end tag
     */
    private String end;

    /**
     * Parameters of the procedure
     */
    private Parameter[] parameters;

    /**
     * Code name of the procedure
     */
    private String codeName;

    /**
     * Creates a procedure
     */
    public ProcedureProperties() {
        name = null;
        twoLevel = false;
        parameters = null;
        end = null;
        codeName = null;
    }

    /**
     * Returns the name of the procedure
     * 
     * @return Name of the procedure
     */
    public String getProcedureName() {
        return name;
    }

    /**
     * Returns the end keyword of the procedure
     * 
     * @return End keyword of the procedure
     */
    public String getEndKeyword() {
        return end;
    }

    /**
     * Returns if it is a two level procedure
     * 
     * @return Is a two level procedure?
     */
    public boolean getTwoLevel() {
        return twoLevel;
    }

    /**
     * Returns the code name of the procedure
     * 
     * @return Code name of the procedure
     */
    public String getCodeName() {
        return codeName;
    }

    /**
     * Returns the parameters of the procedure
     * 
     * @return Parameters of the procedure
     */
    public Parameter[] getParameters() {
        return parameters;
    }

    /**
     * Modifies the name of the procedure
     * 
     * @param name
     *            Name of the procedure
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Modifies the two levels attribute of the procedure
     * 
     * @param twoLevel
     *            two levels attribute value of the procedure
     */
    public void setTwoLevel(boolean twoLevel) {
        this.twoLevel = twoLevel;
    }

    /**
     * Modifies the end tak of the procedure
     * 
     * @param end
     *            End tag
     */
    public void setEnd(String end) {
        this.end = end;
    }

    /**
     * Modifies the parameters of the procedure
     * @param parameters
     *            Parameters of the procedure
     */
    public void setParameters(Parameter[] parameters) {
        this.parameters = parameters;
    }
    
    public String toString(){
    	String str= ""+this.getCodeName()+","
    			+this.getEndKeyword()+","
    			+this.getProcedureName()+","
    			+this.getTwoLevel()+". Params:";
    	for (int i=0;i<parameters.length;i++){
    		str+=parameters[i];
    	}
    	return str;
    }
    
    

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProcedureProperties) {
			ProcedureProperties pp=(ProcedureProperties)obj;
			boolean bval=pp.getTwoLevel()==this.getTwoLevel();
			bval=bval && (pp.getCodeName()==null && this.getCodeName()==null)?true:pp.getCodeName().equals(this.getCodeName());
			bval=bval && (pp.getEndKeyword()==null && this.getEndKeyword()==null)?true:pp.getEndKeyword().equals(this.getEndKeyword());
			bval=bval && (pp.getProcedureName()==null && this.getProcedureName()==null)?true:pp.getProcedureName().equals(this.getProcedureName());
			
			Parameter[] par1=pp.getParameters();
			Parameter[] par2=this.getParameters();
			
			if (par1.length!=par2.length) return false;
			
			for (int i=0;i<par1.length;i++){
				bval=bval && par1[i].equals(par1[i]);
			}
			
			return bval;
		}
		else{
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
