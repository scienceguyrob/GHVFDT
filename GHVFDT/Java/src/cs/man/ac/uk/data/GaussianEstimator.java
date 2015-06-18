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
 * File name: 	GaussianEstimator.java
 * Package: cs.man.ac.uk.utils.Statistics
 * Created:	September 30th, 2013
 * Author:	Richard Kirkby (rkirkby@cs.waikato.ac.nz), modified by Rob Lyon.
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.data;

import cs.man.ac.uk.utils.StatsUtils;

/**
 * Gaussian incremental estimator that uses incremental method that is more resistant
 * to floating point imprecision. For more info see Donald Knuth's "The Art of Computer
 * Programming, Volume 2: Seminumerical Algorithms", section 4.2.2.
 * 
 * This is basically used to model feature distributions within our algorithm.
 * 
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class GaussianEstimator
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * Weighted sum of observed examples.
	 */
	protected double weightSum;

	/**
	 * The mean of the values observed.
	 */
	protected double mean;

	/**
	 * Summed variance of the examples observed.
	 */
	protected double varianceSum;

	public static final double NORMAL_CONSTANT = Math.sqrt(2 * Math.PI);

	//*****************************************
	//*****************************************
	//           Getters / Setters
	//*****************************************
	//*****************************************

	/**
	 * @return the total weight of examples observed.
	 */
	public double getTotalWeightObserved() { return this.weightSum; }

	/**
	 * @return the mean of the values observed.
	 */
	public double getMean() { return this.mean; }

	/**
	 * @return the standard deviation of the examples observed.
	 */
	public double getStdDev() { return Math.sqrt(getVariance()); }

	/**
	 * @return the variance of the examples observed.
	 */
	public double getVariance() 
	{
		return this.weightSum > 1.0 ? this.varianceSum / (this.weightSum - 1.0): 0.0;
	}
	
	//*****************************************
	//*****************************************
	//                Methods
	//*****************************************
	//*****************************************

	/**
	 * Updates the Gaussian model using an observed example.
	 * @param value the double value of the example.
	 * @param weight the weight associated with the example, will
	 * always be 1.0 unless some sort of cost sensitive learning approach
	 * is used.
	 */
	public void addObservation(double value, double weight) 
	{
		// If it has no value, just return.
		if (Double.isInfinite(value) || Double.isNaN(value)) 
			return;

		// If the weight of the example observed is greater than zero,
		// update the statistics. Note that examples will only have a
		// weight less than zero, if you implement some sort of cost
		// sensitive method, whereby positive examples are weighted more
		// than negative examples. See Cost Sensitive Learning for more
		// details.
		if (this.weightSum > 0.0) 
		{
			// Update model...
			this.weightSum += weight;
			double lastMean = this.mean;
			this.mean += weight * (value - lastMean) / this.weightSum; 
			this.varianceSum += weight * (value - lastMean) * (value - this.mean);
		} 
		else 
		{
			this.mean = value;
			this.weightSum = weight;
		}
	}

	/**
	 * Returns the probability density for a value, given the data observed thus far.
	 * @param value to estimate the probability density for.
	 * @return the density in the range [0,1].
	 */
	public double probabilityDensity(double value) 
	{
		if (this.weightSum > 0.0) 
		{
			double stdDev = getStdDev();

			if (stdDev > 0.0) 
			{
				double diff = value - getMean();
				return (1.0 / (NORMAL_CONSTANT * stdDev)) * Math.exp(-(diff * diff / (2.0 * stdDev * stdDev)));
			}

			return value == getMean() ? 1.0 : 0.0;
		}

		return 0.0;
	}

	/**
	 * Returns an estimate of the weight of examples equal to a specific value,
	 * less than a specific value, and greater than a specific value. For example,
	 * imagine that 10% of examples have the value 5, and that there have been 500
	 * examples observed. Assuming a weight of 1.0 per example, then the weight of
	 * examples with the value of 5 = 10% of (1.0 * 500) = 50. If 80% of values
	 * are below 5, then the weight less than 5 = 80% of(1.0 * 500) = 400, and thus 
	 * the weight of examples greater than 5 must be 50. This information is useful
	 * for computing split points in the tree.
	 * @param value the value to estimate the weights for.
	 * @return an array of doubles containing the lessThanWeight, equalToWeight, and
	 * greaterThanWeight in that order.
	 */
	public double[] estimatedWeight_LessThan_EqualTo_GreaterThan_Value(double value) 
	{
		double equalToWeight = probabilityDensity(value) * this.weightSum;
		double stdDev = getStdDev();

		double lessThanWeight = stdDev > 0.0 ? StatsUtils.normalProbability((value - getMean()) / stdDev)
				* this.weightSum - equalToWeight
				: (value < getMean() ? this.weightSum - equalToWeight : 0.0);

		double greaterThanWeight = this.weightSum - equalToWeight - lessThanWeight;

		if (greaterThanWeight < 0.0)
			greaterThanWeight = 0.0;

		return new double[]{lessThanWeight, equalToWeight, greaterThanWeight};
	}
}