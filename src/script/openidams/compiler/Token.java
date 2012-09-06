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
 * Identifies a lexical unit
 * @author ECITeam
 * @version 0.0.8
 */
public class Token {
    
    /**
     * End of file
     */
    public static final int EOF = 0;

    /**
     * Other token
     */
    public static final int OTHER = 1;

    /**
     * Identifier
     */
    public static final int IDENTIFIER = 2;

    /**
     * Equal
     */
    public static final int EQUAL = 3;

    /**
     * Semicolon
     */
    public static final int SEMICOLON = 4;

    /**
     * Integer constant
     */
    public static final int INTEGER = 5;

    /**
     * Real constant
     */
    public static final int REAL = 6;

    /**
     * String constant
     */
    public static final int STRING = 7;

    /**
     * Left Parenthesis
     */
    public static final int LPAR = 8;

    /**
     * Right Parenthesis
     */
    public static final int RPAR = 9;

    /**
     * Comma
     */
    public static final int COMMA = 10;

    /**
     * end
     */
    public static final int END = 11;

    /**
     * run
     */
    public static final int RUN = 12;

    /**
     * dataset
     */
    public static final int DATASET = 13;

    /**
     * dictionary
     */
    public static final int DICTIONARY = 14;

    /**
     * variable
     */
    public static final int VARIABLE = 15;

    /**
     * filter
     */
    public static final int FILTER = 16;

    /**
     * recode
     */
    public static final int RECODE = 17;

    /**
     * or
     */
    public static final int OR = 18;

    /**
     * and
     */
    public static final int AND = 19;

    /**
     * not
     */
    public static final int NOT = 20;

    /**
     * numeric
     */
    public static final int NUMERIC = 21;

    /**
     * alfanumeric
     */
    public static final int ALFANUMERIC = 22;

    /**
     * Less Than
     */
    public static final int LTHAN = 23;

    /**
     * Less Equal
     */
    public static final int LEQUAL = 24;

    /**
     * Greater Than
     */
    public static final int GTHAN = 25;

    /**
     * Greater Equal
     */
    public static final int GEQUAL = 26;

    /**
     * Not Equal
     */
    public static final int UNEQUAL = 27;

    /**
     * add
     */
    public static final int ADD = 28;

    /**
     * substract
     */
    public static final int SUBTRACT = 29;

    /**
     * times
     */
    public static final int TIMES = 30;

    /**
     * divide
     */
    public static final int DIVIDE = 31;

    /**
     * procedure
     */
    public static final int PROCEDURE = 32;

    /**
     * map
     */
    public static final int MAP = 33;

    /**
     * Token id
     */
    private int id;

    /**
     * Lexer string
     */
    private String lexeme;

    /**
     * Creates a token with the given id an lex string
     * 
     * @param id
     *            Token Id
     * @param lexeme
     *            Lex string
     */
    public Token(int id, String lexeme) {
        this.id = id;

        if (id == STRING) {
            lexeme = lexeme.substring(1, lexeme.length() - 2);
        }
        this.lexeme = lexeme;
    }

    /**
     * Returns the token id
     * @return token id
     */
    public int getId() {
        return id;
    }

    /**
     * Return the lexeme string
     * @return token lexeme
     */
    public String getLexeme() {
        return lexeme;
    }
}
