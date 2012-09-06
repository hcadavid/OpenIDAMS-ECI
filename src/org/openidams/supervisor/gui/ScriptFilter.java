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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Supervisor's view helper
 * @author ECITeam
 * @version 0.0.6
 */
public class ScriptFilter extends FileFilter {

    /**
     * Last extension used in the view
     */
    private static String lastExtension = null;

    /**
     * Compiler used by the view
     */
    private String compiler;

    /**
     * Extension used by the view
     */
    private String extension;

    /**
     * Creates an ScriptFilter with the given compiler name and extension
     * @param compiler Compiler name
     * @param extension Extension name
     */
    public ScriptFilter(String compiler, String extension) {
        this.compiler = compiler;
        this.extension = extension;
    }

    /**
     * Get the extension of a file
     * @param f File
     * @return Extension of f
     */
    private String getExtension(File f) {
        String ext;
        String s;
        int i;

        ext = "";
        s = f.getName();
        i = s.lastIndexOf('.');

        if ((i > 0) && (i < (s.length() - 1))) {
            ext = s.substring(i).toLowerCase();
        }

        return ext;
    }

    /**
     * Accepts the file and modifies the lastExtension attribute in
     * the affirmative case
     * @param f File to be accepted
     * @return The file is accepted or not
     */
    public boolean accept(File f) {
        boolean ok;

        ok = (getExtension(f).toLowerCase().compareTo(extension) == 0) ||
            f.isDirectory();

        if (ok) {
            lastExtension = extension;
        }

        return ok;
    }

    /**
     * Returns the description of the filter
     * @return Description of the filter
     */
    public String getDescription() {
        return compiler + " Script (*" + extension + ")";
    }

    /**
     * Returns the last accepted extension of the view
     * @return Last accepted extension
     */
    public static String getLastExtension() {
        return lastExtension;
    }
}
