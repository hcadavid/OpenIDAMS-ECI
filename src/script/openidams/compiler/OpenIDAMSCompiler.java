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

import java.lang.reflect.Array;

import org.openidams.datatable.Catalog;
import org.openidams.procedure.Metadata;
import org.openidams.script.compiler.ICompiler;
import org.openidams.script.interpreter.ExecutionStep;
import org.openidams.script.interpreter.StatementScript;

import script.openidams.interpreter.Argument;

/**
 * OpenIDAMS Language Compiler.
 * 
 * @author ECITeam
 * @version 0.0.8
 */
public class OpenIDAMSCompiler implements ICompiler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openidams.script.compiler.ICompiler#parse(String)
	 */
	public StatementScript parse(String filename) throws Exception {
		Object[] steps;
		Object[] arguments;
		Step step;
		Argument argument;
		int i, j, length, alength;
		Parser parser = new Parser(filename);
		StatementScript script = new StatementScript();
		Metadata metadata = new Metadata();

		steps = parser.parse();
		length = Array.getLength(steps);
		for (i = 0; i < length; i++) {
			step = (Step) steps[i];
			metadata = new Metadata();
			arguments = step.getArguments();
			alength = Array.getLength(arguments);
			for (j = 0; j < alength; j++) {
				argument = (Argument) arguments[j];
				metadata
						.addProperty(argument.getKeyword(), argument.getValue());
			}
			script
					.addStatementStep(new ExecutionStep(step.getName(),
							metadata));
		}

		return script;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openidams.script.compiler.ICompiler#compile(String)
	 */
	public StatementScript compile(String filename) throws Exception {
		StatementScript script = parse(filename);
	
		//refresh datatable catalog
		Catalog.getInstance().init();
				
		return script;
	}
}
