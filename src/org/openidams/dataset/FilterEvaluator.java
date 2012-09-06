package org.openidams.dataset;

import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.StringTokenizer;

import org.openidams.dataset.exceptions.InvalidFilterException;




public class FilterEvaluator {

	private FilterEvaluator() {}
	
	

	public static boolean evaluate(Hashtable<String,String> varToValMappings,String exp) throws InvalidFilterException{
		Stack<Double> values=new Stack<Double>();
		StringTokenizer st=new StringTokenizer(exp,"+-*/<>=~&|NGL ",true);

		//N ! (mult -1)
		//L # (<=)
		//G $ (>=)
		//DIF @ (<>)
		boolean lv1,lv2;
		try{
			
			while (st.hasMoreTokens()){
				String t=st.nextToken();
				if (!t.equals(" ")){
					String val=varToValMappings.get(t.trim());
					//if the token correspond to a variable
					if (val!=null){						
						values.push(toNumber(val));
					}	
					else if (isNumber(t)){
						values.push(toNumber(t));
					}
					else if(isOperator(t.trim().charAt(0))){
						double nv=0;
						switch (t.trim().charAt(0)){
							case '+':
								nv=values.pop()+values.pop();
								break;
							case '-':
								nv=values.pop()-values.pop();
								break;
							case '*':
								nv=values.pop()*values.pop();
								break;
							case '/':
								nv=values.pop()/values.pop();
								break;
							case '>':
								nv=values.pop()<values.pop()?1:0;
								break;
							case '$':
								nv=values.pop()<=values.pop()?1:0;
								break;
							case '<':
								nv=values.pop()>values.pop()?1:0;
								break;
							case '#':
								nv=values.pop()>=values.pop()?1:0;
								break;
							case '=':
								nv=values.pop().equals(values.pop())?1:0;
								break;
							case '&':
								lv1=getLogicalValue("and",values.pop());
								lv2=getLogicalValue("and",values.pop());
								nv=(lv1 && lv2)?1:0;								
								break;
							case '|':
								lv1=getLogicalValue("or",values.pop());
								lv2=getLogicalValue("or",values.pop());
								nv=(lv1||lv2)?1:0;								
								break;
							case '@':
								nv=values.pop().equals(values.pop())==true?0:1;								
								break;
							case '~':
								if (values.peek()!=1 && values.peek()!=0){
									throw new InvalidFilterException("~ operator only applyable to boolean values.");
								}
								nv=values.pop()==1?0:1;
								break;	
							case '!':								
								nv=values.pop()*-1;
								break;	
								
								

						}
						values.push(nv);					
					}
					else{
						throw new InvalidFilterException("Invalid expression (invalid element:"+t+"): "+exp);		
					}
				}				
			}
			if (values.size() > 1){
				throw new InvalidFilterException("Invalid expression (missing operators): "+exp);
			}
			else{
				double res=values.peek();
				if (res!=0 && res!=1) throw new InvalidFilterException("Invalid filter expression (expression must be equivalent to a logical value - it requires comparison operators): "+exp); 
				else{
					return res==1?true:false;	
				}
				
			}
			
		}catch (EmptyStackException e){
			throw new InvalidFilterException("Invalid expression (too many operators): "+exp,e);
		}
	}

	private static boolean getLogicalValue(String operator,double v) throws InvalidFilterException{
		if (v!=1 && v!=0){
			throw new InvalidFilterException(operator+" operator only applyable to boolean values.");									
		}
		return v==1?true:false;
		
	}

	private static boolean isNumber(String n){
		try{
			Double.parseDouble(n);
			return true;
		}
		catch (NumberFormatException e){
			return false;
		}
	}


	private static double toNumber(String n){
			return Double.parseDouble(n);
	}

	private static boolean isOperator(char o){

		//N ! (mult -1)
		//L # (<=)
		//G $ (>=)
		//DIF @ (<>)
		String operators="+-*/<>=~!#$@&|";
		return (
			operators.indexOf(o)!=-1 
		);
	}

	
	
}




