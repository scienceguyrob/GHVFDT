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
 * File name: 	Classifier.java
 * Package: cs.man.ac.uk.classifier
 * Created:	May 21st, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.classifier;

import cs.man.ac.uk.data.I_Instance;

/**
 * Abstract class describing the methods that a generic classifier should possess.
 * 
 * @author Rob Lyon
 */
public abstract class Classifier
{
	/**
	 * Resets this classifiers internal learning model.
	 */
	public abstract void resetLearning();

	/**
	 * Trains this classifier incrementally using the given instance.
	 *
	 * @param inst the instance to be used for training.
	 */
	public abstract void trainOnInstance(I_Instance inst);

	/**
	 * Predicts the class label for an instance. 
	 *
	 * @param inst the instance to be classified.
	 * @return an array containing the estimated membership
	 * probabilities of the test instance in each class.
	 */
	public abstract double[] predict(I_Instance inst);

	/**
	 * Predicts the class label for an instance. 
	 *
	 * @param inst the instance to be classified.
	 * @return an integer value representing the predicted class label.
	 */
	public abstract int classify(I_Instance inst);
	
	/**
	 * Checks whether an instance is correctly classified.
	 *
	 * @param inst the instance to be classified.
	 * @return true if the instance is correctly classified.
	 */
	public abstract boolean correctlyClassifies(I_Instance inst);

	/**
	 * Returns index of maximum element in a given array of doubles.
	 * First maximum is returned.
	 * 
	 * @param doubles the array of doubles
	 * @return the index of the maximum element
	 */
	public int maxIndex(double[] doubles) 
	{
		double maximum = 0;
		int maxIndex = 0;

		for (int i = 0; i < doubles.length; i++) 
			if ((i == 0) || (doubles[i] > maximum)) 
			{
				maxIndex = i;
				maximum = doubles[i];
			}

		return maxIndex;
	}
}