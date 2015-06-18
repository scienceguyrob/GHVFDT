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
 * File name: 	I_Instance.java
 * Package: cs.man.ac.uk.data
 * Created:	May 21st, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.data;

import java.util.Vector;

/**
 * The interface defining the methods for a standard data instance.
 * 
 * @author Rob Lyon
 */
public interface I_Instance
{
	/**
	 * @return the class label of the instance, -2 is returned if 
	 * the true class label is unknown.
	 */
	public int getClassLabel();

	/**
	 * @param f the index of the feature
	 * @return the feature at the specified index, otherwise NaN is returned
	 * if no such feature exists.
	 */
	public double getFeature(int f);

	/**
	 * @return all features that describe this instance (does not include class label),
	 * else an empty double [] is returned.
	 */
	public Vector<Double> getFeatures();

	/**
	 * Sets the class label.
	 * @param i the new class label.
	 */
	public void setClasslabel(int i);

	/**
	 * Sets the value of the feature at the specified index.
	 * @param f the feature index.
	 * @param value the value for the feature.
	 * @return true if the feature is set, else false.
	 */
	public boolean setFeature(int f,double value);

	/**
	 * Sets the values of all features.
	 * @param values the values for the features.
	 * @return true if the feature is set, else false.
	 */
	public boolean setFeatures(double[] values);

	/**
	 * Adds a new feature to this instance.
	 * @param f the feature value.
	 */
	public void addFeature(double f);

	/**
	 * Removes the feature at the specified index.
	 * @param f the feature index.
	 */
	public void removeFeature(int f);

	/**
	 * @return a count of the features possessed by the instance.
	 */
	public int getFeatureCount();

	/**
	 * @return the instance as a string.
	 */
	public String toString();


	/*
	 *	MOA METHODS COMPATIBILITY.
	 */

	/**
	 * @return the training weight of this instance.
	 */
	public double weight();

	/**
	 * @return if the class label for this instance is missing or unknown.
	 */
	public boolean classIsMissing();

	/**
	 * Tests if a specific value attribute value is missing.
	 *
	 * @param index the attribute index to check
	 * @return true if the attribute is missing else false.
	 */
	public boolean isMissing(int index); 
	
	/**
	 * @return a count of the number of attributes possessed by this instance.
	 */
	public int numAttributes();
	
	/**
	 * @return the value of the class label of this instance, as a double value.
	 */
	public double classValue();
	
	/**
	 * Gets the value of the attribute at the specified index.
	 * @param index the attribute to return the value for.
	 * @return the attribute value as a double.
	 */
	public double value(int index);
}
