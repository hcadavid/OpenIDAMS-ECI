package org.openidams.utilities.winidamscompatibility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;

public class WinIDAMSDataFileReader {

	private LinkedList<WinIDAMSDictionaryEntry> winIdamsDict;
	private File file;
	private String fileLine;
	private RandomAccessFile raf;
	private boolean iteratorNextMoved;
	
	/**
	 * 
	 * @param f
	 * @param wiDict
	 */
	public WinIDAMSDataFileReader(File f,LinkedList<WinIDAMSDictionaryEntry> wiDict) {
		winIdamsDict=wiDict;
		file=f;
		
	}
	
	/**
	 * 
	 * @return 
	 * @throws InvalidWinIDAMSDataFileException
	 */
	public Iterator<LinkedList<String>> getRows() throws InvalidWinIDAMSDataFileException{
		try {
			raf=new RandomAccessFile(file,"r");
			iteratorNextMoved=false;
			
			return new Iterator<LinkedList<String>>(){

				boolean lastHasNextValue=false;
				
				//verify if next() was invoked after hasNext(). If not, lastHasNextValue will be returned for better 
				//performance and interation consistency.
				boolean nextPerformed=true;

				
				
				public boolean hasNext() {
					if (nextPerformed){
						try {
							
							fileLine=raf.readLine();
							nextPerformed=false;
							if (fileLine==null){
								raf.close();
								lastHasNextValue=false;
								return false;
							}
							lastHasNextValue=true;
							return true;
						} catch (IOException e) {
							throw new InvalidWinIDAMSDataFileException("Error reading WinIDAMS file: "+file.getName(),e);
						}
					}
					else{
						return lastHasNextValue;
					}
				}

				public LinkedList<String> next() {
					LinkedList<String> res=new LinkedList<String>();
					for (int i=0;i<winIdamsDict.size();i++){
						WinIDAMSDictionaryEntry entry=winIdamsDict.get(i);
						res.add(fileLine.substring(entry.getPosition()-1,entry.getPosition()+entry.getLenght()-1));						
					}
					nextPerformed=true;
					return res;
				}

				public void remove() {}				
			
			};					
		} catch (FileNotFoundException e) {
			throw new InvalidWinIDAMSDataFileException("WinIDAMS data file not found:"+file.getName(),e);
		}
		
	}

}
