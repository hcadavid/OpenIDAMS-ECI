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

import java.io.File;

import org.openidams.procedure.Catalog;
import org.openidams.procedure.manager.PlugInException;
import org.openidams.utilities.Properties;

/**
 * Lexical analyzer
 * 
 * @author ECITeam
 * @version 0.0.8
 */
public class Lexer {

    /**
     * Lexer scanner
     */
    private Scanner scanner;

    /**
     * Parser properties
     */
    private Properties properties;

    /**
     * Procedures directory
     */
    private String procedureDir;

    /**
     * Creates a lexer of a given file
     * 
     * @param filename
     *            Text file name
     */
    public Lexer(String filename) throws Exception {
        scanner = new Scanner(filename);
        properties = Properties.getInstance();
        properties.load("supervisor.properties");
        procedureDir = properties.getProperty(Properties.PROCEDUREPLUGINSROOT) + "/";
    }

    /**
     * Is the character a space
     * 
     * @param c
     *            Given character
     * @return c is a space character?
     */
    private boolean isSpace(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    /**
     * Ignore space characters in the scanner
     */
    private void skipSpaces() throws Exception {
        char c;

        c = scanner.getChar();

        if (isSpace(c)) {
            c = scanner.getChar();

            while (isSpace(c)) {
                c = scanner.getChar();
            }
        }

        scanner.retract();
    }

    /**
     * Is the character a letter
     * 
     * @param c
     *            Given character
     * @return c is a letter character?
     */
    private boolean isLetter(char c) {
        return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'))
                || (c == '_');
    }

    /**
     * Is the character a digit
     * 
     * @param c
     *            Given character
     * @return c is a digit character?
     */
    private boolean isDigit(char c) {
        return (c >= '0') && (c <= '9');
    }

    /**
     * Indicates if the current string is a procedure definition
     * 
     * @param name
     *            Definition name
     * @return If the current string is a procedure definition
     * @throws PlugInException 
     */
    private boolean isProcedure(String name) throws PlugInException {
    	return Catalog.getInstance().existsProcedure(name);
    }

    /**
     * Returns a token identifier
     * @return Token identifier
     * @throws Exception Problems obtaining the next token identifier from the scanner
     */
    private Token getIdentifier() throws Exception {
        char c;
        String lexeme;
        int t;

        c = scanner.getChar();

        while (isLetter(c) || isDigit(c)) {
            c = scanner.getChar();
        }

        scanner.retract();

        lexeme = scanner.getLexeme().toLowerCase();
        if (Configuration.end.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.END;
        else if (Configuration.run.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.RUN;
        else if (Configuration.dataset.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.DATASET;
        else if (Configuration.dictionary.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.DICTIONARY;
        else if (Configuration.variable.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.VARIABLE;
        else if (Configuration.map.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.MAP;
        else if (Configuration.filter.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.FILTER;
        else if (Configuration.recode.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.RECODE;
        else if (Configuration.or.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.OR;
        else if (Configuration.and.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.AND;
        else if (Configuration.not.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.NOT;
        else if (Configuration.numeric.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.NUMERIC;
        else if (Configuration.alfanumeric.toLowerCase().compareTo(lexeme) == 0)
        	t = Token.ALFANUMERIC;
        else if (isProcedure(lexeme))
        	t = Token.PROCEDURE;
        else
        	t = Token.IDENTIFIER;
        return new Token(t, scanner.getLexeme());
    }

    /**
     * Returns a token number
     * @return Token number
     * @throws Exception Problems obtaining the next token number from the scanner
     */
    private Token getNumber() throws Exception {
        char c;
        Token token;

        c = scanner.getChar();
        while (isDigit(c)) {
            c = scanner.getChar();
        }
        if (c != '.') {
            scanner.retract();
            token = new Token(Token.INTEGER, scanner.getLexeme());
        } else {
            c = scanner.getChar();
            while (isDigit(c)) {
                c = scanner.getChar();
            }
            scanner.retract();
            token = new Token(Token.REAL, scanner.getLexeme());
        }
        return token;
    }

    /**
     * Returns a token string
     * @return Token string
     * @throws Exception Problems obtaining the next token string from the scanner
     */
    private Token getString(char delimiter) throws Exception {
        char c;

        c = scanner.getChar();

        while (c != Scanner.EOI && c != '\r' && c != '\n' && c != delimiter) {
            c = scanner.getChar();
        }

        if (c == delimiter) {
            c = scanner.getChar();
        }

        return new Token(Token.STRING, scanner.getLexeme());
    }

    /**
     * Skip a comment in the scanner
     */
    private void skipComment() throws Exception {
        char c;

        do {
            c = scanner.getChar();

            while ((c != '*') && (c != Scanner.EOI)) {
                c = scanner.getChar();
            }

            if (c == Scanner.EOI) {
                throw new InvalidCommentException();
            } else if (c == '*') {
                c = scanner.getChar();
            }
        } while (c != '/');
    }

    /**
     * Returns a token 
     * @return Token 
     * @throws Exception Problems obtaining the next token from the scanner
     */
    public Token getToken() throws Exception {
        char c;
        Token token = null;

        while (token == null) {
            skipSpaces();
            scanner.savePtr();
            c = scanner.getChar();

            if (isLetter(c)) {
                token = getIdentifier();
            } else if (isDigit(c)) {
                token = getNumber();
            } else if (c == '\'' || c == '"') {
                token = getString(c);
            } else if (c == '(') {
                token = new Token(Token.LPAR, scanner.getLexeme());
            } else if (c == ')') {
                token = new Token(Token.RPAR, scanner.getLexeme());
            } else if (c == '=') {
                token = new Token(Token.EQUAL, scanner.getLexeme());
            } else if (c == ';') {
                token = new Token(Token.SEMICOLON, scanner.getLexeme());
            } else if (c == ',') {
                token = new Token(Token.COMMA, scanner.getLexeme());
            } else if (c == '+') {
                token = new Token(Token.ADD, scanner.getLexeme());
            } else if (c == '-') {
                token = new Token(Token.SUBTRACT, scanner.getLexeme());
            } else if (c == '*') {
                token = new Token(Token.TIMES, scanner.getLexeme());
            } else if (c == '<') {
                c = scanner.getChar();

                if (c == '=') {
                    token = new Token(Token.LEQUAL, scanner.getLexeme());
                } else if (c == '>') {
                        token = new Token(Token.UNEQUAL, scanner.getLexeme());
                } else {
                    scanner.retract();
                    token = new Token(Token.LTHAN, scanner.getLexeme());
                }
            } else if (c == '>') {
                c = scanner.getChar();

                if (c == '=') {
                    token = new Token(Token.GEQUAL, scanner.getLexeme());
                } else {
                    scanner.retract();
                    token = new Token(Token.GTHAN, scanner.getLexeme());
                }
            } else if (c == '!') {
                c = scanner.getChar();

                if (c == '=') {
                    token = new Token(Token.UNEQUAL, scanner.getLexeme());
                } else {
                    scanner.retract();
                    token = new Token(Token.OTHER, scanner.getLexeme());
                }
            } else if (c == Scanner.EOI) {
                token = new Token(Token.EOF, scanner.getLexeme());
            } else if (c != '/') {
                token = new Token(Token.OTHER, scanner.getLexeme());
            } else if (c == '/') {
                c = scanner.getChar();

                if (c == '*') {
                    skipComment();
                } else {
                    scanner.retract();
                    token = new Token(Token.DIVIDE, scanner.getLexeme());
                }
            }
        }

        return token;
    }

    public String getProcedureDir(){
    	return this.procedureDir;
    }
}
