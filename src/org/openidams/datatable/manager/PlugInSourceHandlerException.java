/**
 * Copyright (C) 2006  OpenIDAMS ECITeam 
 * ECITeam: Gerardo Ospina, H�ctor Cadavid, Camilo Rocha
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
package org.openidams.datatable.manager;

/**
 * Exception associated to the creation and use of plugins
 * @author ECITeam
 * @version 0.0.6
 */
@SuppressWarnings("serial")
public class PlugInSourceHandlerException extends Exception {

    /**
	 * @param message
	 */
	protected PlugInSourceHandlerException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
     * Creates an exception with the given message and trace
     * @param message Message of the exception
     * @param cause Trace of the exception
     */
    public PlugInSourceHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
