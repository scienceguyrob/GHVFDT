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
 * File name: 	ARFFFile.java
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
import java.util.List;
import java.util.TreeMap;

/**
 * Represents a data file in WEKA ARFF format.
 * 
 * @author Rob Lyon
 */
public class ARFFFile extends DataFile implements I_File
{
	/**
	 *	Constructor
	 */

	/**
	 * Creates a new ARFF file instance.
	 * @param pth the full path to the file.
	 * @param inc incremental read flag.
	 * @param v the verbose logging flag.
	 */
	public ARFFFile(String pth, boolean inc, boolean v) 
	{
		super(pth, inc, v);
		this.checkValidity();
	}

	/**
	 *	Methods
	 */

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_File#isValid()
	 */
	@Override
	public boolean isValid() { return this.isValid;}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_File#countExamples()
	 */
	@Override
	public int countExamples() { return this.examples; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_File#getAll()
	 */
	@Override
	public List<I_Instance> getAll(){ return this.read(); }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_File#getNext()
	 */
	@Override
	public I_Instance getNext() { return this.readNext(); }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_File#getClassDistribution()
	 */
	@Override
	public int[] getClassDistribution() { return this.classDist; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.I_File#getFeatureCount()
	 */
	@Override
	public int getFeatureCount() { return this.features; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.DataFile#createInstance(java.lang.String)
	 */
	@Override
	public I_Instance createInstance(String line)
	{
		if(!this.isValid)
		{
			String msg = "Data file not valid, cannot createInstance().";
			this.processError(msg);
			return null;
		}

		if(line==null)
			return null;

		I_Instance inst = new Instance();

		String[] components = this.removeComment(this.commentSymbol, line).split(this.separator);

		// if the line string contains no comma separated information...
		if(components != null)
		{
			if(components.length > 1)
				for (int i=0; i< components.length-1;i++)
					try{ inst.addFeature(Double.parseDouble(components[i])); }
			catch(NumberFormatException nfe)
			{ 
				this.processError(nfe,"NumberFormatException in public I_Instance createInstance(String line).");
				return null; // If we can't read data attributes, return null.
			}

			// Class label is last item.
			try{ inst.setClasslabel(Integer.parseInt(components[components.length-1])); }
			catch(NumberFormatException nfe)
			{ 
				this.processError(nfe,"NumberFormatException in public I_Instance createInstance(String line).");
				inst.setClasslabel(-2);
			}

			return inst;
		}
		else
			return null;
	}

	@Override
	public I_Instance createInstance(double[] data) 
	{
		if(!this.isValid)
		{
			String msg = "Data file not valid, cannot createInstance().";
			this.processError(msg);
			return null;
		}


		I_Instance inst = new Instance();

		// if the line string contains no comma separated information...
		if(data != null)
		{
			if(data.length > 1)
				for (int i=0; i< data.length-1;i++)
					inst.addFeature(data[i]); 

			// Class label is last item.
			inst.setClasslabel((int)data[data.length-1]);

			return inst;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.DataFile#checkValidity()
	 */
	@Override
	public boolean checkValidity()
	{
		try
		{
			// If the reader hasn't been initialized...
			if(br==null)
				br = new BufferedReader( new FileReader(this.path)); 

			String line;

			int ftCount = 0;
			TreeMap<Integer,Integer> cDist = new TreeMap<Integer,Integer>();
			int ex = 0;

			int lineNumber = 0;
			while ((line = br.readLine()) != null)
			{
				lineNumber++;
				
				// This if else ignores comments.
				if(line.startsWith("@"))
					continue;
				else if(line.isEmpty())
					continue;
				else if(line.startsWith("\n"))
					continue;
				else if(line.startsWith("\r"))
					continue;
				else if(line.startsWith("%"))
					continue;	
				
				double[] data = doesLineContainValidData(line);

				if(data != null)
				{
					ex++;
					int tempFeatureCount = data.length-1;
					int clazz = (int)data[data.length-1];

					int currentClassCount = 0;

					// Get the class count
					if(cDist.get(clazz) != null) // No examples seen so far.
						currentClassCount = cDist.get(clazz);

					currentClassCount++;
					cDist.put(clazz,currentClassCount);

					if(ftCount == 0)
						ftCount=tempFeatureCount;
					else if(ftCount != tempFeatureCount)
					{
						// Discrepancy between feature lengths, report error.
						String msg = "Error reading data on line " + lineNumber + " discrepency between number of features on each line.";
						this.processError(msg);
						return false;
					}

				}
				else
				{
					// Construct informative error message.
					String msg = "Error reading data on line " + lineNumber + " data could not be read as double[].";
					this.processError(msg);
					return false;
				}
			}

			this.features = ftCount;
			this.examples = ex;
			this.classDist = this.toPrimitiveInt(cDist);
			this.isValid = true;

			return this.isValid;

		}
		catch (IOException e) { this.processError(e,"IOException in public boolean checkValidity()."); return false; }
		finally { closeFile(); prepareFile();  }
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.DataFile#doesLineContainValidData(java.lang.String)
	 */
	@Override
	public double[] doesLineContainValidData(String line)
	{
		if(line == null)
			return null;
		
		// Get string components with comments removed (if there are any).
		String[] components = this.removeComment(this.commentSymbol, line).split(this.separator);

		// if the line string contains no comma separated information...
		if(components != null)
		{
			double[] data = new double[components.length];

			if(components.length > 0)
			{
				for (int i=0; i< components.length;i++)
				{
					try{ data[i] = Double.parseDouble(components[i]); }
					catch(NumberFormatException nfe)
					{ 
						return null; // If we can't read data attributes, return null.
					}
				}

				return data;
			}
			else
				return null;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.DataFile#removeComment(java.lang.String,java.lang.String)
	 */
	@Override
	public String removeComment(String symbol, String line)
	{
		if(line != null)
		{
			// Pre-process Line
			if(line.indexOf(symbol)!= -1)
				return line.substring(0, line.indexOf("%"));
			else
				return line;
		}
		else
			return null;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.data.DataFile#prepareFile()
	 */
	@Override
	public void prepareFile()
	{
		if(this.readIncrementally)
		{	
			try { br = new BufferedReader( new FileReader(this.path)); }
			catch (FileNotFoundException fnf)
			{
				this.processError(fnf,"FileNotFoundException in public void prepareFile().");
			}
		}
	}
}
