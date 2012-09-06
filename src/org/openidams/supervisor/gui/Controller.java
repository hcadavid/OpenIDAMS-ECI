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

import org.openidams.supervisor.gui.output.ProceduresOutputHandler;

/**
 * Supervisor Controller
 * 
 * @author ECITeam
 */
public class Controller {
    /**
     * Id for the new command
     */
    public static final String NEW = "New";

    /**
     * Id for the open command
     */
    public static final String OPEN = "Open";

    /**
     * Id for the save command
     */
    public static final String SAVE = "Save";

    /**
     * Id for the save as command
     */
    public static final String SAVE_AS = "Save As";

    /**
     * Id for the quit command
     */
    public static final String QUIT = "Quit";

    /**
     * Id for the copy command
     */
    public static final String COPY = "Copy";

    /**
     * Id for the cut command
     */
    public static final String CUT = "Cut";

    /**
     * Id for the paste command
     */
    public static final String PASTE = "Paste";

    /**
     * Id for the compile command
     */
    public static final String COMPILE = "Compile";

    /**
     * Id for the run command
     */
    public static final String RUN = "Run";

    /**
     * Id for the about command
     */
    public static final String ABOUT = "About";

    /**
     * Id for the about command
     */
    public static final String VIEWM = "View output window";

    /**
     * Id for the about command
     */
    public static final String VIEWPROCS = "View available procedures";

    
    /**
     * View component
     */
    private View view = null;

    /**
     * Model component
     */
    private Model model = null;

    /**
     * View's event handler
     * 
     * @param arg0
     *            Event name
     */
    public void actionPerformed(String arg0) {
        try {
            if (arg0.equals(Controller.QUIT)) {
                model.saveScript();
                System.exit(0);
            } else if (arg0.equals(Controller.NEW)) {
                model.newScript();                
            } else if (arg0.equals(Controller.OPEN)) {
                model.openScript();
            } else if (arg0.equals(Controller.SAVE)) {
                model.saveScript();
            } else if (arg0.equals(Controller.SAVE_AS)) {
                model.saveScriptAs();
            } else if (arg0.equals(Controller.COMPILE)) {
                model.compile();
            } else if (arg0.equals(Controller.RUN)) {
                model.run();
            } else if (arg0.equals(Controller.ABOUT)) {
            	view.showCredits();
            } else if (arg0.equals(Controller.VIEWM)) {
            	ProceduresOutputHandler.getInstance().showOutput();
            } else if (arg0.equals(Controller.VIEWPROCS)){
            	new ProceduresInfoFrame().setVisible(true);
            }
            
        } catch (Exception e) {
			Throwable rootCause=e;
			while (rootCause.getCause()!=null){
				rootCause=rootCause.getCause();
			}									
            view.showMessage(e.toString()+":"+rootCause);
            e.printStackTrace();
        }
    }

    /**
     * Sets the view for which this controller exists
     * 
     * @param arg0
     *            View to be setted
     */
    public void setView(View arg0) {
        view = arg0;
    }

    /**
     * Sets the model for which this controller exists
     * 
     * @param arg0
     *            Model to be setted
     */
    public void setModel(Model arg0) {
        model = arg0;
    }
}
