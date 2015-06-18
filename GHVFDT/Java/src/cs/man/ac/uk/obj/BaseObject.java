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
 * File name: 	BaseObject.java
 * Package: cs.man.ac.uk.obj
 * Created:	May 21st, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.obj;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Basic object with debugging methods, used by classes which read in data.
 * 
 * @author Rob Lyon
 */
public class BaseObject 
{
	/**
	 *	Variables 
	 */

	/**
	 * Verbose logging flag.
	 */
	protected boolean verbose = false;

	/**
	 *	Constructor 
	 */

	/**
	 * Default constructor.
	 * @param v the verbose flag.
	 */
	protected BaseObject(boolean v) { setVerbose(v); }

	/**
	 *	Methods
	 */

	/**
	 * Setter method for the verbose flag.
	 * @param v the new verbose flag.
	 */
	public void setVerbose(boolean v) { this.verbose = v; }

	/**
	 * Used to process errors.
	 * @param e the exception raised.
	 * @param msg the message that describes how/why the error occurred.
	 */
	protected void processError(Exception e,String msg)
	{
		String report = "Class:\t" + this.getClass().getSimpleName() + "\tError: " + msg;

		System.out.println(report);

		if(verbose)
			e.printStackTrace();
	}

	/**
	 * Used to process errors.
	 * @param msg the message that describes how/why the error occurred.
	 */
	protected void processError(String msg)
	{
		String report = "Class:\t" + this.getClass().getSimpleName() + "\tError: " + msg;
		System.out.println(report);
	}

	/**
	 * Converts a Double array to a primitive double[].
	 * @param array the input list.
	 * @return a primitive double array
	 */
	protected double[] toPrimitiveDouble(ArrayList<Double> array) 
	{
		if (array == null) 
			return  new double[]{};
		else if (array.size() == 0)
			return new double[]{};

		double[] result = new double[array.size()];

		for (int i = 0; i < array.size(); i++)
			result[i] = array.get(i).doubleValue();

		return result;
	}

	/**
	 * Converts a Integer array to a primitive int[].
	 * @param array the input list.
	 * @return a primitive int array
	 */
	protected int[] toPrimitiveInt(ArrayList<Integer> array) 
	{
		if (array == null) 
			return new int[]{};
		else if (array.size() == 0)
			return new int[]{};

		int[] result = new int[array.size()];

		for (int i = 0; i < array.size(); i++)
			result[i] = array.get(i).intValue();

		return result;
	}

	/**
	 * Converts a tree map to a primitive int[].
	 * @param map the input map.
	 * @return a primitive int array
	 */
	protected int[] toPrimitiveInt(TreeMap<Integer,Integer> map) 
	{
		if (map == null) 
			return new int[]{};
		else if (map.size() == 0)
			return new int[]{};

		int[] result = new int[map.size()];

		int i = 0;
		for(Map.Entry<Integer,Integer> entry : map.entrySet()) 
		{
			Integer value = entry.getValue();
			result[i] = value.intValue();
			i++;
		}

		return result;
	}
}