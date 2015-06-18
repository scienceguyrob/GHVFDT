/**
 *
 * This file is part of GHVFDT.
 *
 * GHVFDT is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GHVFDT is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GHVFDT.  If not, see <http://www.gnu.org/licenses/>.
 *
 * File name: 	DataFile.java
 * Package: cs.man.ac.uk.data
 * Created:	May 21st, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs.man.ac.uk.obj.BaseObject;

/**
 * Simple file object, base of file object hierarchy for this project.
 * 
 * @author Rob Lyon
 */
public abstract class DataFile extends BaseObject
{
	/**
	 *	Variables 
	 */
	
	/**
	 * Path to the data file (absolute path).
	 */
	protected String path = "";
	
	/**
	 * Number of examples in the data file.
	 */
	protected int examples = 0;
	
	/**
	 * Number of features (data attributes) in the file.
	 */
	protected int features = 0;
	
	/**
	 * The class distribution of the data in the file.
	 */
	protected int[] classDist;
	
	/**
	 * Flag that indicates the validity of the file, invalid 
	 * files cannot be processed.
	 */
	protected boolean isValid = false;
	
	/**
	 * The symbol used to signify a comment in the data file.
	 */
	protected String commentSymbol = "%";
	
	/**
	 * The symbol used to separate data items in the file.
	 */
	protected String separator = ",";
	
	/**
	 * Flag that indicates if the file should be read incrementally or not.
	 * If not read incrementally, then the file must be read in to memory 
	 * in a single pass.
	 */
	protected boolean readIncrementally = false;
	
	/**
	 * Used to read in the file.
	 */
	protected BufferedReader br = null;
    
    /**
	 *	Constructor 
	 */
    
    /**
     * Default constructor.
     * @param pth the full path to the file on disk.
     * @param inc a boolean flag that indicates whether or not to read the
     * @param v the verbose logging flag
     * file incrementally. True means read incrementally.
     */
	public DataFile(String pth,boolean inc,boolean v)
	{
		super(v);
		this.path = pth;
		this.readIncrementally = inc;
		
		
		try { br = new BufferedReader( new FileReader(this.path)); } 
		catch (FileNotFoundException e){ e.printStackTrace(); }
	}
	
	/**
	 * Processes the data file, and extracts all possible data instances.
	 * @return a list of data instances, else null if there are any errors.
	 */
	public List<I_Instance> read()
	{
        try
        {
        	if(!this.isValid)
        		return null;
        	
        	// First make sure any previously opened file is closed.
        	closeFile();
        	
        	// The set of instances found.
        	List<I_Instance> instances = new ArrayList<I_Instance>();
        	
        	// Re-initialize the buffered reader.
            br = new BufferedReader( new FileReader(this.path));
            
            // Stores the next line.
        	String line = "";
        	
            while ((line = br.readLine()) != null)
            {
            	double[] data = doesLineContainValidData(line);
            	if(data != null)
            		instances.add(createInstance(data));
            }
            
            return instances;
        }
        catch (IOException e) { this.processError(e,"IOException in public List<I_Instance> read()"); return null; }
        finally { closeFile(); prepareFile(); }
	}
	
	/**
	 * Reads the next individual instance from the data file.
	 * @return the next I_Instance example from the file, else null
	 * if there is an error.
	 */
	public I_Instance readNext()
	{
        try
        {
        	if(!this.isValid)
        		return null;
        	
        	// If the reader hasn't been initialized...
        	if(br == null)
        		br = new BufferedReader( new FileReader(this.path)); 
        	
            String line = br.readLine();
            
            // This while loop added to accommodate files with headers.
            // In other words if reading incrementally, keep reading until
            // the next valid data instance is found.
            while( line!= null && doesLineContainValidData(line) == null)
            	line = br.readLine();
            
            if(line == null)
            {
            	if (br != null)
                    br.close();// must be a problem with the file.
            	
            	return null;
            }
            else
            {
            	double[] data = doesLineContainValidData(line);
            	
            	if(data != null)
            		return createInstance(data);
            	else
            		return null;
            }
            
        }
        catch (IOException e) { this.processError(e,"IOException in public I_Instance readNext()"); return null; }
	}
	
	/**
	 * Explicitly closes the data file, if it is being
	 * read incrementally.
	 */
	public void closeFile()
	{
		if(this.readIncrementally)
			if (br != null)
				try { br.close(); } 
				catch (IOException e) { e.printStackTrace();}
	}
	
	/**
	 * @return the symbol which signifies the start of a comment in the data file.
	 */
	public String getCommentSymbol(){ return this.commentSymbol; }
	
	/**
	 * Sets the comment symbols used by this data file.
	 * @param sym the symbol which signifies the start of a comment in the data file.
	 */
	public void setCommentSymbol(String sym) { this.commentSymbol = sym; }
	
	/**
	 * @return the symbol used to separate data items in the file.
	 */
	public String getSeparator(){ return this.separator; }
	
	/**
	 * Sets the string used to separate data items in the file.
	 * @param sep the symbol used to separate data items in the file.
	 */
	public void setSeparator(String sep) { this.separator = sep; }
	
	/**
	 * Creates a new data instance.
	 * @param line a string describing a line from a data file.
	 * @return a new instance object created from the line, esle null if there
	 * are any errors.
	 */
	public abstract I_Instance createInstance(String line);
	
	/**
	 * Creates a new data instance.
	 * @param data the numerical data.
	 * @return a new instance object created from the line, else null if there
	 * are any errors.
	 */
	public abstract I_Instance createInstance(double[] data);
	
	/**
	 * Checks the validity of the file.
	 * @return true if valid, else false.
	 */
	public abstract boolean checkValidity();
	
	/**
	 * Checks if a line of data from the file represented by this class,
	 * is valid or not.
	 * @param line the line of data from the data file.
	 * @return true if a data file line contains valid data,
	 * i.e. numerical information, else false.
	 */
	//public abstract boolean doesLineContainValidData(String line);
	
	/**
	 * Checks if a line of data from the file represented by this class,
	 * is valid or not.
	 * @param line the line of data from the data file.
	 * @return the numerical data if valid (including the class label), else null.
	 */
	public abstract double[] doesLineContainValidData(String line);
	
	/**
	 * Removes comments appended to the end of a line of data, where a comment
	 * starts at the supplied comment symbol.
	 * @param symbol that signals the start of a comment.
	 * @param line the text to remove the comment from.
	 * @return the processed string, or null if the string is empty.
	 */
	public abstract String removeComment(String symbol,String line);
	
	/**
	 * Prepares the data file if needed to be read incrementally.
	 */
	public abstract void prepareFile();
	
	/**
	 * Over-ridden toString method.
	 * @return a string describing this class.
	 */
	public String toString()
	{
		String message = "\nFile:\t" + this.path + "\nExamples:\t" + this.examples +
				 "\nFeatures:\t" + this.features + "\nClass dist:\n\t";
		
		if(this.classDist != null)
			for(int i=0;i<this.classDist.length;i++)
				message += "Class "+i+ ":\t" + this.classDist[i] + "\n\t";
		else
			message += " empty\n";
		
		message += "\n";
		
		return message;
	}
}
