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
package org.openidams.supervisor.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.openidams.procedure.Catalog;
import org.openidams.procedure.manager.PlugInException;
import org.openidams.script.interpreter.InterpreterRuntimeException;
import org.openidams.script.interpreter.StatementScript;

/**
 * Supervisor Model
 * @author ECITeam
 * @version 0.0.6
 */
public class Model {
    /**
     * Model View
     */
    private View view = null;

    /**
     * Model Controller
     */
    private Controller controller = null;

    /**
     * File name of the script file shown in the View
     */
    private String filename = null;

    /**
     * Original content of the script file
     */
    private String backup = "";

    /**
     * Instantiates a model
     */
    public Model() {
    }

    /**
     * Sets the view for which this model exists
     * @param pView View to be setted
     */
    public void setView(View pView) {
        view = pView;
    }

    /**
     * Sets the controller for which this model exists
     * @param pController Controller to be setted
     */
    public void setController(Controller pController) {
        controller = pController;
    }
    
    /**
     * Returns the models controller
     * @return Model's controller
     */
    public Controller getController(){
        return controller;
    }

    /**
     * Clears the script file
     */
    public void newScript() throws IOException {
        if (view.getText().compareTo(backup) != 0) {
            saveScript();
        }

        if (backup.compareTo("") != 0) {
            backup = "";
            view.setText("");
        }

        filename = null;
        view.newFile();
    }

    /**
     * Loads a script
     */
    public void openScript() throws FileNotFoundException, IOException {
        if (view.getText().compareTo(backup) != 0) {
            saveScript();
        }

        filename = view.openFile();

        if (filename != null) {
            loadScript();
        }
    }

    /**
     * Saves the current script under a new name
     */
    public void saveScriptAs() throws IOException {
        filename = view.saveFile();

        if (filename != null) {
            storeScript();
        }
    }

    /**
     * Saves the current script, if it is new prompts for a new name
     */
    public void saveScript() throws IOException {
        if (filename == null) {
            saveScriptAs();
        } else {
            storeScript();
        }
    }
        

    /**
     * Compiles the current script
     */
    public void compile() throws Exception {
		if (backup.compareTo("") != 0){
			if (view.getText().compareTo(backup) != 0) {
            	saveScript();
        	}
        	if (view.getText().compareTo(backup) == 0) {
	        	org.openidams.script.compiler.Factory.getInstance().getCompiler(filename)
                                             					   .parse(filename);
                view.showMessage(filename + " compilation OK");
            }
  		}
    }

    /**
     * Executes the current script
     */
    public void run() throws Exception, PlugInException, InterpreterRuntimeException {
		if (backup.compareTo("") != 0){
			if (view.getText().compareTo(backup) != 0) {
            	saveScript();
        	}
        	if (view.getText().compareTo(backup) == 0) {
            	StatementScript script = org.openidams.script.compiler.Factory.getInstance()
                                                                          .getCompiler(filename)
                                                                          .compile(filename);
            	Catalog.getInstance().init();

            	org.openidams.script.interpreter.Factory.getInstance()
                                                    .getInterpreter(filename)
                                                    .executeScript(script);
            }
        }
    }

    /**
     * Loads a script from file
     */
    private void loadScript() throws FileNotFoundException, IOException {
        String line;
        BufferedReader infile;

        backup = "";
        line = "";
        infile = new BufferedReader(new FileReader(filename));

        while ((line = infile.readLine()) != null) {
            backup = backup + line + "\n";
        }

        infile.close();
        view.setText(backup);
    }

    /**
     * Stores the current script in a file
     */
    private void storeScript() throws IOException {
        PrintWriter outfile;
        int i;
        String[] text;

        backup = view.getText();
        outfile = new PrintWriter(new FileWriter(new File(filename)));
        text = backup.split("\n");

        for (i = 0; i < text.length; i++) {
            outfile.println(text[i]);
        }

        outfile.close();
    }
    
}
