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

import java.io.*;

/**
 * Buffered read from a file on a char basis
 * 
 * @author ECITeam
 * @version 0.0.6
 */
public class Scanner {

    /**
     * End of file character
     */
    public static final char EOI = '\0';

    /**
     * Scanner block length
     */
    private final int BLOCK_LEN = 512;

    /**
     * File input stream, file source
     */
    private FileInputStream fis;

    /**
     * Buffer
     */
    private byte[] buffer = new byte[(2 * BLOCK_LEN) + 2];

    /**
     * Scanner index
     */
    private int index;

    /**
     * Save index
     */
    private int save;

    /**
     * last half index
     */
    private int lastHalf;

    /**
     * Filename to be scanned
     */
    private String name;

    /**
     * Creates a scanner for a file given its name
     * 
     * @param filename
     *            Text file name
     */
    public Scanner(String filename) throws Exception {
        name = filename;
        index = 0;
        save = 0;
        buffer[BLOCK_LEN] = EOI;
        buffer[(2 * BLOCK_LEN) + 1] = EOI;
        fis = new FileInputStream(name);
        lastHalf = 0;
        readHalf(1);
    }

    /**
     * Finalizes the scanner use of the stream
     */
    public void finalize() {
        try {
            fis.close();
        } catch (Exception e) {
        }
    }

    /**
     * Init requested Buffer Half
     * 
     * @param half
     *            requested buffer half
     */
    private void initHalf(int half) {
        switch (half) {
        // Half 1
        case 1:

            for (int i = 0; i < BLOCK_LEN; i++) {
                buffer[i] = EOI;
            }

            break;

        // Half 2
        case 2:

            for (int i = BLOCK_LEN + 1; i < ((2 * BLOCK_LEN) + 1); i++) {
                buffer[i] = EOI;
            }

            break;
        } // switch
    }

    /**
     * Read requested buffer half
     * 
     * @param half
     *            requested buffer half
     */
    private void readHalf(int half) throws Exception {
        if (half != lastHalf) {
            if (half == 1) {
                initHalf(1);
                fis.read(buffer, 0, BLOCK_LEN);
                lastHalf = 1;
            } else if (half == 2) {
                initHalf(2);
                fis.read(buffer, BLOCK_LEN + 1, BLOCK_LEN);
                lastHalf = 2;
            }
        }
    }

    /**
     * Returns the current char in buffer
     * @return current char in buffer (buffer[index])
     */
    public char getChar() throws Exception {
        byte c;

        c = buffer[index];

        if (c == EOI) {
            if (index == BLOCK_LEN) {
                readHalf(2);
                index++;
                c = buffer[index];
            } else if (index == ((2 * BLOCK_LEN) + 1)) {
                readHalf(1);
                index = 0;
                c = buffer[0];
            }
        }

        index++;

//        Character character = new Character((char) c);

        return Character.toLowerCase((char) c);
    }

    /**
     * Step back (circularly) one buffer position
     */
    public void retract() {
        index--;

        if (index == -1) {
            index = (2 * BLOCK_LEN) - 1;
        } else if (index == BLOCK_LEN) {
            index--;
        }
    }

    /**
     * Copy current index to save
     */
    public void savePtr() {
        save = index;
    }

    /**
     * Current lexeme in buffer
     * 
     * @return current lexeme in buffer
     */
    public String getLexeme() {
        int i;
        String lexeme;

        lexeme = "";

        if (save < index) {
            for (i = save; i < index; i++) {
                if (i != BLOCK_LEN) {
                    lexeme += ((char) buffer[i]);
                }
            }
        } else if (save > index) {
            for (i = save; i <= (2 * BLOCK_LEN); i++) {
                lexeme += ((char) buffer[i]);
            }

            for (i = 0; i < index; i++) {
                lexeme += ((char) buffer[i]);
            }
        }

        return lexeme;
    }
}
