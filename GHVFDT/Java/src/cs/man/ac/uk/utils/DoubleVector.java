/**
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
 * File name: 	DoubleVector.java
 * Package: cs.man.ac.uk.utils
 * Created:	September 30th, 2013
 * Author:	Richard Kirkby (rkirkby@cs.waikato.ac.nz), Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.utils;

/**
 * Double vector, basically a wrapper for a double array.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class DoubleVector
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * Holds the vector values.
	 */
	protected double[] array;

	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************

	/**
	 * Creates a new double vector.
	 */
	public DoubleVector() { this.array = new double[0]; }

	/**
	 * Creates a new double vector using the data values provided.
	 * @param toCopy the array to copy.
	 */
	public DoubleVector(double[] toCopy) 
	{
		this.array = new double[toCopy.length];
		System.arraycopy(toCopy, 0, this.array, 0, toCopy.length);
	}

	/**
	 * Creates a new Double vector by copying an existing vector.
	 * @param toCopy the array to copy.
	 */
	public DoubleVector(DoubleVector toCopy) 
	{
		this(toCopy.getArrayRef());
	}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/**
	 * @return the number of values in the vector.
	 */
	public int numValues() { return this.array.length; }

	/**
	 * Sets a value in the vector, at the specified index.
	 * @param i the index .
	 * @param v the value to set.
	 */
	public void setValue(int i, double v) 
	{
		if (i >= this.array.length)
			setArrayLength(i + 1);

		this.array[i] = v;
	}

	/**
	 * Adds the supplied value, to the value in the double
	 * vector at the supplied index.
	 * @param i the index.
	 * @param v the value to add.
	 */
	public void addToValue(int i, double v) 
	{
		if (i >= this.array.length) 
			setArrayLength(i + 1);

		this.array[i] += v;
	}

	/**
	 * Get the value of the double vector at the specified index.
	 * @param i the index.
	 * @return the value in the vector at the specified index.
	 */
	public double getValue(int i) 
	{
		return ((i >= 0) && (i < this.array.length)) ? this.array[i] : 0.0;
	}

	/**
	 * @return the sum of all values in the vector.
	 */
	public double sumOfValues() 
	{
		double sum = 0.0;
		for (double element : this.array)
			sum += element;

		return sum;
	}

	/**
	 * @return the count of non-zero entries in the vector.
	 */
	public int numNonZeroEntries() 
	{
		int count = 0;

		for (double element : this.array) 
			if (element != 0.0) 
				count++;

		return count;
	}

	/**
	 * @return a copy of the underlying array that this class wraps.
	 */
	public double[] getArrayCopy() 
	{
		double[] aCopy = new double[this.array.length];
		System.arraycopy(this.array, 0, aCopy, 0, this.array.length);
		return aCopy;
	}

	/**
	 * @return the array as a double.
	 */
	public double[] getArrayRef() { return this.array; }

	/**
	 * Sets the length of the double vector.
	 * @param l the length of the vector.
	 */
	protected void setArrayLength(int l) 
	{
		double[] newArray = new double[l];
		int numToCopy = this.array.length;

		if (numToCopy > l) 
			numToCopy = l;

		System.arraycopy(this.array, 0, newArray, 0, numToCopy);
		this.array = newArray;
	}
}