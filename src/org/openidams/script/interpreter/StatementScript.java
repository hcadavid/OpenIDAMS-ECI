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
package org.openidams.script.interpreter;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Statement script
 * Collection in a specific order of statement steps
 * @author ECITeam
 * @version 0.0.6
 */
public class StatementScript {
	/**
	 * Collection of statements
	 */
	private Collection<StatementStep> statements;
	
	/**
	 * StatementScript constructor
	 */
	public StatementScript(){
		statements = new LinkedList<StatementStep>();
	}
	
	/**
	 * Adds an statement step as the last statement in the script while preserving the
	 * existing order
	 * @param pStatement Statement to be added
	 */
	public void addStatementStep( StatementStep pStatement ){
		statements.add( pStatement );
	}
	
	/**
	 * Returns an iterator of the script statements
	 * @return An iterator of the script statements
	 */
	public Iterator statementsIterator(){
		return statements.iterator();
	}
}
