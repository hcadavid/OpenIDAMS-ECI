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
package org.openidams.utilities;

import java.io.*;

/**
 * @author ECITeam
 */
import java.lang.reflect.*;
import java.net.*;

/**
 * Dinamically adds files to the classpath in runtime
 * @author ECITeam
 * @version 0.0.6
 */
public class ClassPathUpdater {

    /**
     * Updater parameters
     */
    private static final Class[] parameters = new Class[] { URL.class };

    /**
     * Adds a file with the given name to the classpaht
     * @param s Name of the file
     * @throws IOException Problems loading the file
     */
    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }

    /**
     * Adds a given file to the classpaht
     * @param f File to be added
     * @throws IOException Problems loading the file
     */
    public static void addFile(File f) throws IOException {
        addURL(f.toURL());
    }

    /**
     * Adds an item in url form to the classpath
     * @param u Url to be added
     * @throws IOException Problems locating the url
     */
    public static void addURL(URL u) throws IOException {        
    	URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;

        try {
            Method method = sysclass.getDeclaredMethod("addURL", parameters);
            method.setAccessible(true);
            method.invoke(sysloader, new Object[] { u });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException(
                "Error, could not add URL to system classloader");
        }
    }
}
