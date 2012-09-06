package org.openidams.utilities.winidamscompatibility;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;

public class WinIDAMSDictionaryInterpreter {

	private WinIDAMSDictionaryInterpreter() {}
	
	public static void main(String[] args) {
		try {
			LinkedList<WinIDAMSDictionaryEntry> entries=getDictionaryEntries(new File("C:/Archivos de programa/WinIDAMS11-EN/data/educ.dic"));
			System.out.println(entries);
			WinIDAMSDataFileReader dfr=new WinIDAMSDataFileReader(new File("C:/Archivos de programa/WinIDAMS11-EN/data/educ.dat"),entries);
			
			Iterator<LinkedList<String>> rows=dfr.getRows();
			while (rows.hasNext()){
				System.out.println(rows.next());
			}
		} catch (InvalidWinIDAMSDictionaryException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public static LinkedList<WinIDAMSDictionaryEntry> getDictionaryEntries(File f) throws InvalidWinIDAMSDictionaryException{
		LinkedList<WinIDAMSDictionaryEntry> entries=new LinkedList<WinIDAMSDictionaryEntry>();
		int line=-1;
		//String fname="C:/Archivos de programa/WinIDAMS11-EN/data/educ.dic";
		try {
			RandomAccessFile raf=new RandomAccessFile(f,"r");

			String fileLine;
			
			String varID;
			String name;
			int pos;
			int length;
			boolean hasDec;
			boolean isAlphanum;	
			
			while ((fileLine=raf.readLine())!=null){
				line++;
				if (fileLine.indexOf("T")==0){
					varID=""+Integer.parseInt(fileLine.substring(2,5).trim());
					name=fileLine.substring(6,31).trim();
					pos=Integer.parseInt(fileLine.substring(31,35).trim());
					length=Integer.parseInt(fileLine.substring(35,39).trim());
					hasDec=fileLine.substring(39,40).equals("1")?true:false;
					isAlphanum=fileLine.substring(40,41).equals("1")?true:false;
					entries.add(new WinIDAMSDictionaryEntry(varID,name,pos,length,hasDec,isAlphanum));
				}
				
			}
		} catch (FileNotFoundException e) {
			throw new InvalidWinIDAMSDictionaryException("Dictionary file not found:"+f.getAbsolutePath(),e);
		}
		catch (IOException e){
			throw new InvalidWinIDAMSDictionaryException("Error reading "+f.getName(),e);
		}
		catch (NumberFormatException e){
			throw new InvalidWinIDAMSDictionaryException("Invalid data on the dictionary "+f.getName()+" at line "+line,e);
		}
		catch (ArrayIndexOutOfBoundsException e){
			throw new InvalidWinIDAMSDictionaryException("Invalid data on the dictionary "+f.getName()+" at line "+line,e);
		}

		
		return entries;
	}

}

