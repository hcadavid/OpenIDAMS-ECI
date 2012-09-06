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
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.Vector;

import org.openidams.procedure.Catalog;
import org.openidams.procedure.manager.Descriptor;

import script.openidams.interpreter.Argument;
import com.thoughtworks.xstream.XStream;

/**
 * OpenIDAMS script language recursive descendent parser
 * 
 * @author ECITeam
 * @version 0.0.8
 */
public class Parser {
    /**
     * Xstream object
     */
    private XStream xstream;

    /**
     * Lexer
     */
    private Lexer lexer;

    /**
     * Token
     */
    private Token token;

    /**
     * List of steps
     */
    private Vector <Step>steps;

    /**
     * Procedure
     */
    private ProcedureProperties procedure;

    /**
     * Parameters
     */
    private Parameter[] parameters;

    /**
     * Creates a parser for the file given its name
     * 
     * @param filename
     *            File name
     * @throws Exception
     *             Problems creating the parser
     */
    @SuppressWarnings("unchecked")
    public Parser(String filename) throws Exception {
        steps = new Vector();
        lexer = new Lexer(filename);
        xstream = new XStream();
        xstream.alias("procedure", ProcedureProperties.class);
        xstream.alias("parameter", Parameter.class);
        xstream.alias("procedureDescriptor", Descriptor.class);
    }

    /**
     * Loads the procedure definition given the file name
     * 
     * @param name
     *            Name of the procedure
     * @throws Exception
     *             Problems loading the procedure definition
     */
    private void loadProcedureDefinition(String name) throws Exception {
    	//procedure=Catalog.getInstance().getProcedureProperties(name);
        //String filename = lexer.getProcedureDir() + name + ".xml";
        
    	String filename =Catalog.getInstance().getProcedurePath(name)+"/descriptor.xml";
        
        try {
            procedure = ((Descriptor) xstream.fromXML(new FileReader(new File(
                    filename)))).getProcedureProperties();
        } catch (Exception e) {
            throw new InvalidProcedureException(name + e.toString());
        }
    }

    /**
     * Return the argument for a given type
     * 
     * @param type
     *            Type of the argument
     * @return Argument for the given type
     * @throws Exception
     *             Problems formating the argument
     */
    private Argument getArgument(String name, int type) throws Exception {
        Argument argument = null;
        String keyword;
        String value = null;
        
        switch (type) {
        case 1:
            keyword = token.getLexeme();
            token = lexer.getToken();
            if (token.getId() == Token.EQUAL) {
                token = lexer.getToken();
            }
            if (token.getId() == Token.IDENTIFIER ||
            		token.getId() == Token.STRING ||
            		token.getId() == Token.INTEGER ||
            		token.getId() == Token.REAL) {
            	argument = new Argument(keyword, token.getLexeme());
            } else {
            	throw new InvalidParameterException(name + " Variable Name, String, Integer or Real Expected: " + token.getLexeme());
            }
        	break;
        case 2:
            if (token.getId() == Token.IDENTIFIER) {
            	argument = new Argument(token.getLexeme());
            } else {
            	throw new InvalidParameterException(name + " Keyword Expected: " + token.getLexeme());
            }
        	break;
        case 3:
            keyword = token.getLexeme();
            token = lexer.getToken();
            if (token.getId() == Token.EQUAL) {
                token = lexer.getToken();
            }
            if (token.getId() == Token.IDENTIFIER) {
            	argument = new Argument(keyword, token.getLexeme());
            } else {
            	throw new InvalidParameterException(name + " Variable Name Expected: " + token.getLexeme());
            }
        	break;
        case 4:
            keyword = token.getLexeme();
            token = lexer.getToken();
            if (token.getId() == Token.EQUAL) {
                token = lexer.getToken();   
            }
            if (token.getId() == Token.IDENTIFIER ||
            		token.getId() == Token.INTEGER) {
            	argument = new Argument(keyword, token.getLexeme());
            } else if (token.getId() == Token.LPAR) {
                token = lexer.getToken();
                value = "";
                while (token.getId() == Token.IDENTIFIER ||
                		token.getId() == Token.INTEGER) {
                	value = value + token.getLexeme() + " ";
                    token = lexer.getToken();
                }
                if (token.getId() == Token.RPAR) {
                	argument = new Argument(keyword, value);
                } else {
                	throw new InvalidParameterException(name + " Right Parenthesis Expected: " + token.getLexeme());
                }
            } else {
            	throw new InvalidParameterException(name + " Variable Name or Integer Expected: " + token.getLexeme());
            }
        	break;
        case 5:
            keyword = token.getLexeme();
            token = lexer.getToken();
            if (token.getId() == Token.EQUAL) {
                token = lexer.getToken();   
            }
            if (token.getId() == Token.IDENTIFIER ||
            		token.getId() == Token.REAL) {
            	argument = new Argument(keyword, token.getLexeme());
            } else if (token.getId() == Token.LPAR) {
                token = lexer.getToken();
                value = "";
                while (token.getId() == Token.IDENTIFIER ||
                		token.getId() == Token.REAL) {
                	value = value + token.getLexeme() + " ";
                    token = lexer.getToken();
                }
                if (token.getId() == Token.RPAR) {
                	argument = new Argument(keyword, value);
                } else {
                	throw new InvalidParameterException(name + " Right Parenthesis Expected: " + token.getLexeme());
                }
            } else {
            	throw new InvalidParameterException(name + " Variable Name or Real Expected: " + token.getLexeme());
            }
        	break;
        case 6:
            keyword = token.getLexeme();
            token = lexer.getToken();
            if (token.getId() == Token.EQUAL) {
                token = lexer.getToken();   
            }
            if (token.getId() == Token.IDENTIFIER) {
            	argument = new Argument(keyword, token.getLexeme());
            } else if (token.getId() == Token.LPAR) {
                token = lexer.getToken();
                value = "";
                while (token.getId() == Token.IDENTIFIER) {
                	value = value + token.getLexeme() + " ";
                    token = lexer.getToken();
                }
                if (token.getId() == Token.RPAR) {
                	argument = new Argument(keyword, value);
                } else {
                	throw new InvalidParameterException(name + " Right Parenthesis Expected: " + token.getLexeme());
                }
            } else {
            	throw new InvalidParameterException(name + " Variable NameExpected: " + token.getLexeme());
            }
            break;
        default:
        	throw new InvalidParameterException(name + " Invalid Parameter Type: " + type);
        }
        token = lexer.getToken();
        if (token.getId() == Token.SEMICOLON) {
            token = lexer.getToken();
        }
        return argument;
    }

    /**
     * Is a parameter found?
     * 
     * @param parameter
     *            parameter list
     * @return Is a parameter found?
     */
    private boolean parameterMatch(Parameter parameter)
            throws Exception {
        boolean rc;

        if (parameter.getCount() > 0
        		|| !parameter.match(token.getLexeme())) {
            rc = false;
        } else {
            parameter.incCount();
            rc = true;
        }
        return rc;
    }

    /**
     * Get Parameter Id?
     * 
     * @return parameter id
     */
    private int getParameterId() throws Exception {
        int length;
        int id;

        id = token.getId();
        if (id != Token.IDENTIFIER) {
            id = -1;
        } else {
            length = Array.getLength(parameters);
            id = 0;
            while (id < length && !parameterMatch(parameters[id])) {
                id++;
            }
            if (id == length) {
                id = -1;
            }
        }
        return id;
    }

    /**
     * argumentList
     * 
     * @return Return the parameter list for a procedure
     */
    @SuppressWarnings("unchecked")
    private Vector <Argument>argumentList() throws Exception {
        int id;
        Vector <Argument>arguments = new Vector();

        id = getParameterId();
        while (id != -1) {
            arguments.add(getArgument(parameters[id].getName(),parameters[id].getType()));
            id = getParameterId();
        }
        return arguments;
    }

    /**
     * Are parameters ok?
     * 
     * @return if parameters and arguments coincide
     */
    private void checkParameters() throws Exception{
        boolean ok;
        int i, length;

        length = Array.getLength(parameters);
        i = 0;
        ok = true;
        while (ok && i < length) {
            ok = !parameters[i].getMandatory()
                    || (parameters[i].getCount() > 0);
            if (ok) {
                i++;
            } else {
                throw new InvalidParameterException(procedure.getProcedureName() + ": Parameter " + parameters[i].getName() + " is mandatory");
            }
        }
    }

    /**
     * Parses a script step
     */
    @SuppressWarnings("unchecked")
    private Vector <Argument>step() throws Exception {
        Vector <Argument>arguments;

        loadProcedureDefinition(token.getLexeme());
        parameters = procedure.getParameters();
        token = lexer.getToken();
        if (token.getId() == Token.SEMICOLON) {
            token = lexer.getToken();
        }
        arguments = argumentList();
        checkParameters();
        return arguments;
    }

    private String factor() throws Exception {
    	String formula;
    	
    	boolean negate = false;
    	while (token.getId() == Token.SUBTRACT) {
    		token = lexer.getToken();
    		negate = !negate;
    	}
    	if (token.getId() == Token.IDENTIFIER ||
    			token.getId() == Token.INTEGER ||
    			token.getId() == Token.REAL ||
    			token.getId() == Token.STRING) {
    		formula = token.getLexeme();
    		token = lexer.getToken();
    	} else if (token.getId() == Token.LPAR) {
    		token = lexer.getToken();
    		formula = logicalExpression();
    		if (token.getId() == Token.RPAR) {
        		token = lexer.getToken();
    		} else {
    			throw new RightParenthesisExpectedException();
    		}
    	} else {
    		throw new InvalidFactorException(token.getLexeme());
    	}
    	if (negate) {
    		formula = formula + " !";
    	}
    	return formula;
    }
    
    private boolean isMultiplicativeOperator() {
    	return token.getId() == Token.TIMES ||
    		token.getId() == Token.DIVIDE;
    }

    private String term() throws Exception {
    	String formula;
    	int operator;
    	
    	formula = factor();
    	while (isMultiplicativeOperator()){
    		operator = token.getId();
    		token = lexer.getToken();
    		formula = formula + " " + factor();
    		if (operator == Token.TIMES) {
    			formula = formula + " *";
    		} else if (operator == Token.DIVIDE) {
    			formula = formula + " /";
    		}
    	}
    	return formula;
    }
    
    private boolean isAdditiveOperator() {
    	return token.getId() == Token.ADD ||
    		token.getId() == Token.SUBTRACT;
    }

    private String arithmeticExpression() throws Exception {
    	String formula;
    	int operator;
    	
    	formula = term();
    	while (isAdditiveOperator()){
    		operator = token.getId();
    		token = lexer.getToken();
    		formula = formula + " " + term();
    		if (operator == Token.ADD) {
    			formula = formula + " +";
    		} else if (operator == Token.SUBTRACT) {
    			formula = formula + " -";
    		}
    	}
    	return formula;
    }
    
    private boolean isRelationalOperator()
    {
      return token.getId() == Token.LTHAN ||
      		token.getId() == Token.LEQUAL ||
      		token.getId() == Token.GTHAN ||
      		token.getId() == Token.GEQUAL ||
      		token.getId() == Token.EQUAL ||
      		token.getId() == Token.UNEQUAL;
    }
    
    private String relationalExpression() throws Exception {
    	String formula;
    	int operator;
    	
    	formula = arithmeticExpression();
    	if (isRelationalOperator()) {
    		operator = token.getId();
    		token = lexer.getToken();
    		formula = formula + " " + arithmeticExpression();
    		switch (operator) {
    		case Token.LTHAN:
    			formula = formula + " <";
    			break;
    		case Token.LEQUAL:
    			formula = formula + " #";
    			break;
    		case Token.GTHAN:
    			formula = formula + " >";
    			break;
    		case Token.GEQUAL:
    			formula = formula + " $";
    			break;
    		case Token.EQUAL:
    			formula = formula + " =";
    			break;
    		case Token.UNEQUAL:
    			formula = formula + " @";
    			break;
    		}
    	}
    	return formula;
    }
    
    private String notExpression() throws Exception {
    	String formula;
    	boolean not = false;
    	
    	while (token.getId() == Token.NOT) {
    		token = lexer.getToken();
    		not = !not;
    	}
    	formula = relationalExpression();
    	if (not) {
    		formula = formula + " ~";
    	}
    	return formula;
    }
    
    private String andExpression() throws Exception {
    	String formula;
    	
    	formula = notExpression();
    	if (token.getId() == Token.AND){
    		token = lexer.getToken();
    		formula = formula + " " + notExpression() + " &";
    	}
    	return formula;
    }
    
    private String logicalExpression() throws Exception {
    	String formula;
    	
    	formula = andExpression();
    	if (token.getId() == Token.OR){
    		token = lexer.getToken();
    		formula = formula + " " + andExpression() + " |";
    	}
    	return formula;
    }
    
    private String getMaps(String procedure) throws Exception {
        String maps;
        int count;
        
    	token = lexer.getToken();
    	maps = ""; 
    	count = 0;
    	while (token.getId() == Token.IDENTIFIER){
    		if (count > 0) {
    		  maps = maps + ";";
    		}
    		maps = maps + token.getLexeme();
    		token = lexer.getToken();
    		if (token.getId() == Token.EQUAL){
        		token = lexer.getToken();
    		}
        	if (token.getId() != Token.IDENTIFIER) {
        		throw new InvalidMapException(procedure + " " + token.getLexeme());
        	} else {
        		maps = maps + "," + token.getLexeme();
        		token = lexer.getToken();
        		count++;
        	}
    	}
    	return maps;
    }
    
    /**
     * Parses a script dataset step
     */
    private void datasetStep() throws Exception {
        Vector <Argument>arguments;
        String name, maps;

	    token = lexer.getToken();
	    if (token.getId() != Token.PROCEDURE) {
            throw new InvalidProcedureException(token.getLexeme());
        }
	    else {
	    	name = token.getLexeme();
	        arguments = step();
	        if (token.getId() == Token.MAP) {
	        	maps = getMaps(name);
	        	arguments.add(new Argument(Configuration.map, maps));
	        }
	        steps.add(new Step(procedure.getProcedureName(), arguments));
	    }
    }

    /**
     * Parses a script run step
     */
    private void runStep() throws Exception {
        Vector <Argument>arguments;

	    token = lexer.getToken();
	    if (token.getId() != Token.PROCEDURE) {
            throw new InvalidProcedureException(token.getLexeme());
        } else {
	        arguments = step();
	        steps.add(new Step(procedure.getProcedureName(), arguments));
	    }
    }

    private String getVariables() throws Exception {
    	String variables;
    	int count;
    	
    	variables = "";
    	count = 0;
    	while (token.getId() == Token.IDENTIFIER){
    		if (count > 0) {
    			variables = variables + ";";
    		}
    		variables = variables + token.getLexeme();
    		token = lexer.getToken();
    		if (token.getId() != Token.NUMERIC &&
    				token.getId() != Token.ALFANUMERIC){
    			throw new TypeExpectedException(token.getLexeme());
    		} else {
    			variables = variables + "," + token.getLexeme().substring(0,1);
        		token = lexer.getToken();
    		}
    		if (token.getId() == Token.STRING) {
    			variables = variables + "," + token.getLexeme();
        		token = lexer.getToken();
    		}
    		count++;
    	}
    	return variables;
    }
    private String getRecodes() throws Exception {
    	String recodes;
    	int count;
    	
    	recodes = "";
    	count = 0;
    	while (token.getId() == Token.IDENTIFIER){
    		if (count > 0) {
    			recodes = recodes + ";";
    		}
    		recodes = recodes + token.getLexeme();
    		token = lexer.getToken();
    		if (token.getId() == Token.EQUAL){
        		token = lexer.getToken();
    		}
        	recodes = recodes + "," + arithmeticExpression();
        	count++;
    	}
    	return recodes;
    }
    
    /**
     * Parses a script dictionary step
     */
    private void dictionaryStep() throws Exception {
        Vector <Argument>arguments;
        String name, variables, filter, recodes;

        
        name = token.getLexeme();
        
	    //token = lexer.getToken();
        arguments = step();
        if (token.getId() == Token.VARIABLE) {
    	    token = lexer.getToken();
        	variables = getVariables();
        	arguments.add(new Argument(Configuration.variable, variables));
        }
        if (token.getId() == Token.FILTER) {
    	    token = lexer.getToken();
        	filter = logicalExpression();
        	arguments.add(new Argument(Configuration.filter, filter));
        }
        if (token.getId() == Token.RECODE) {
    	    token = lexer.getToken();
    	    recodes = getRecodes();
        	arguments.add(new Argument(Configuration.recode, recodes));
        }
        
        steps.add(new Step(name, arguments));
    }

    /**
     * Indicates if the current string is a meta procedure definition
     * 
     * @return If the current string is a meta procedure definition
     */
    private boolean isMetaProcedure() {
        return token.getId() == Token.RUN ||
        	token.getId() == Token.DATASET ||
        	token.getId() == Token.DICTIONARY;
    }

    /**
     * Parses a script step list
     */
    private void stepList() throws Exception {
        while (isMetaProcedure()) {
        	if (token.getId() == Token.RUN) {
        		runStep();
        	} else if (token.getId() == Token.DATASET) {
        		datasetStep();
        	} else if (token.getId() == Token.DICTIONARY) {
        		dictionaryStep();
        	}
	        if (token.getId() == Token.END) {
	            token = lexer.getToken();
		        if (token.getId() == Token.SEMICOLON) {
		            token = lexer.getToken();
		        }
	        }
        }
    }

    /**
     * Parses a script
     */
    public Object[] parse() throws Exception {
        steps.clear();

        token = lexer.getToken();
        stepList();
        if (token.getId() != Token.EOF) {
            throw new InvalidProcedureException(token.getLexeme());
        }

        return steps.toArray();
    }

    /**
     * Returns the empty message
     * @return Emtpy message
     */
    public String message() {
        return "";
    }
}