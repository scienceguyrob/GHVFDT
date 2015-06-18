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
 * File name: 	GHNumericAttributeClassObserver.java
 * Package: cs.man.ac.uk.observers
 * Created:	September 30th, 2013
 * Author:	Richard Kirkby (rkirkby@cs.waikato.ac.nz), Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.observers;

import java.util.Set;
import java.util.TreeSet;

import cs.man.ac.uk.data.GaussianEstimator;
import cs.man.ac.uk.split.AttributeSplitSuggestion;
import cs.man.ac.uk.split.GHDSplitCriterion;
import cs.man.ac.uk.split.NumericAttributeBinaryTest;
import cs.man.ac.uk.split.SplitCriterion;
import cs.man.ac.uk.utils.AutoExpandVector;
import cs.man.ac.uk.utils.DoubleVector;

/**
 * Used to observe the class data distribution for a numeric data attribute
 * using a GaussianEstimator. Based on code in MOA written by Richard Kirkby,
 * modified by Rob Lyon.
 * 
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class GHNumericAttributeClassObserver implements AttributeClassObserver 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * Contains details of the minimum values attained per class for an attribute. For example in the two class case
	 * (negative class zero, positive class one) then this will have two elements referenced by their index.
	 * That is,
	 * 
	 * {  { 1.1 } , { 5.5 } }
	 *       ^         ^
	 *       |         |
	 *    Class 0    Class 1
	 *    
	 * In other words the minimum value observed for class zero for this attribute is 1.1, and the minimum value
	 * attained for class one is 5.5.
	 */
	protected DoubleVector minValueObservedPerClass = new DoubleVector();

	/**
	 * Contains details of the maximum values attained per class for an attribute. For example in the two class case
	 * (negative class zero, positive class one) then this will have two elements referenced by their index.
	 * That is,
	 * 
	 * {  { 100.1 } , { 500.5 } }
	 *       ^         ^
	 *       |         |
	 *    Class 0    Class 1
	 *    
	 * In other words the maximum value observed for class zero for this attribute is 100.1, and the maximum value
	 * attained for class one is 500.5.
	 */
	protected DoubleVector maxValueObservedPerClass = new DoubleVector();

	/**
	 * One Gaussian Estimator per class.
	 */
	protected AutoExpandVector<GaussianEstimator> attValDistPerClass = new AutoExpandVector<GaussianEstimator>();

	/**
	 * Number of bins to use for discretization.
	 */
	int numBins = 10;

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.observers.AttributeClassObserver#observeAttributeClass(double, int, double)
	 */
	@Override
	public void observeAttributeClass(double attVal, int classVal, double weight) 
	{	
		if (Double.isNaN(attVal)) 
			return;
		else 
		{
			// Create object to observe distribution of attribute values.
			GaussianEstimator valDist = this.attValDistPerClass.get(classVal);

			if (valDist == null) 
			{
				// If attribute not previously observed, initialize the observer.
				valDist = new GaussianEstimator();

				// Add the observer to a vector storing all attribute observers.
				this.attValDistPerClass.set(classVal, valDist);

				// Sets the minimum and maximum values observed for each class.
				this.minValueObservedPerClass.setValue(classVal, attVal);
				this.maxValueObservedPerClass.setValue(classVal, attVal);
			} 
			else 
			{
				// Update minimum and max values observed.
				if (attVal < this.minValueObservedPerClass.getValue(classVal)) 
					this.minValueObservedPerClass.setValue(classVal, attVal);
				if (attVal > this.maxValueObservedPerClass.getValue(classVal)) 
					this.maxValueObservedPerClass.setValue(classVal, attVal);
			}

			// Pass the observation to the attribute observer, so that it can update
			// its internal statistics, i.e. mean, variance etc.
			valDist.addObservation(attVal, weight);
		}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.observers.AttributeClassObserver#probabilityOfAttributeValueGivenClass(double, int)
	 */
	@Override
	public double probabilityOfAttributeValueGivenClass(double attVal,int classVal) 
	{
		GaussianEstimator obs = this.attValDistPerClass.get(classVal);
		// If the observation is null return 0, else return the probability density.
		// The function called below will execute:
		//
		// f(x) = (1.0 / (NORMAL_CONSTANT * stdDev)) * Math.exp(-(x-mean)^{2} / (2.0 * variance)));
		//
		// where NORMAL_CONSTANT = Math.sqrt(2 * Math.PI).
		//
		// Returns the relative likelihood for this random variable to take on a given value.
		return obs != null ? obs.probabilityDensity(attVal) : 0.0; 
	}

	/**
	 * Gets the best split suggestion given a criterion and a class distribution.
	 *
	 * @param criterion the split criterion to use
	 * @param preSplitDist the class distribution before the split
	 * @param attIndex the attribute index
	 * @param binaryOnly if true use only binary splits
	 * @return suggestion of best attribute split
	 */
	@Override
	public AttributeSplitSuggestion getBestEvaluatedSplitSuggestion(SplitCriterion criterion, double[] preSplitDist, int attIndex,boolean binaryOnly) 
	{
		// The current best split suggestion. 
		AttributeSplitSuggestion bestSuggestion = null;

		double[] suggestedSplitValues = getSplitPointSuggestions();

		for (double splitValue : suggestedSplitValues) 
		{
			// Find what the distribution of the data would be like if split
			// at the supplied split point.
			double[][] postSplitDists = getClassDistsResultingFromBinarySplit(splitValue);

			/*******************************************/
			// Calculate Gini, Entropy or whatever here.
			double merit = Double.NEGATIVE_INFINITY;
			//double infoGain = criterion.getMeritOfSplit(preSplitDist,postSplitDists);
			double infoGain = 0;

			if(attValDistPerClass.get(0) != null & attValDistPerClass.get(1) != null)
			{
				double P_mean, P_variance, N_mean, N_variance;
				N_mean = attValDistPerClass.get(0).getMean();
				N_variance = attValDistPerClass.get(0).getVariance();
				P_mean = attValDistPerClass.get(1).getMean();
				P_variance = attValDistPerClass.get(1).getVariance();
				merit = GHDSplitCriterion.computeHellinger(P_mean, P_variance, N_mean, N_variance);
			}

			/*******************************************/

			if ((bestSuggestion == null) || (merit+infoGain > bestSuggestion.merit)) 
			{
				bestSuggestion = new AttributeSplitSuggestion(new NumericAttributeBinaryTest(attIndex, splitValue,true), postSplitDists, merit+infoGain);
			}
		}

		return bestSuggestion;
	}

	/**
	 * @return the Hellinger distance between the positive and negative class, for this feature.
	 */
	public double getFeatureHellingerDistance()
	{
		try
		{
			double P_mean, P_variance, N_mean, N_variance;
			N_mean = attValDistPerClass.get(0).getMean();
			N_variance = attValDistPerClass.get(0).getVariance();
			P_mean = attValDistPerClass.get(1).getMean();
			P_variance = attValDistPerClass.get(1).getVariance();
			return GHDSplitCriterion.computeHellinger(P_mean, P_variance, N_mean, N_variance);
		}
		catch(Exception e){ return 1;}
	}

	/**
	 * @param clas the class label.
	 * @return the mean of the feature, for the specified class.
	 */
	public double getFeatureMean(int clas)
	{
		return attValDistPerClass.get(clas).getMean();
	}

	/**
	 * @param clas the class label.
	 * @return the variance of the feature, for the specified class.
	 */
	public double getFeatureVariance(int clas)
	{
		return attValDistPerClass.get(clas).getVariance();
	}

	/**
	 * @return split points for the observed attribute according to how many bins
	 *         are being used, and the range of the variable.
	 */
	public double[] getSplitPointSuggestions() 
	{
		Set<Double> suggestedSplitValues = new TreeSet<Double>();

		// Store the minimum and maximum values observed for an attribute.
		double minValue = Double.POSITIVE_INFINITY;
		double maxValue = Double.NEGATIVE_INFINITY;

		// For each Gaussian Estimator (one per class).
		for (int i = 0; i < this.attValDistPerClass.size(); i++)
		{
			// Get the estimator.
			GaussianEstimator estimator = this.attValDistPerClass.get(i);

			// If the estimator is initialised get the minimum and maximum values for
			// the attribute, irrespective of class.
			if (estimator != null) 
			{
				if (this.minValueObservedPerClass.getValue(i) < minValue) 
					minValue = this.minValueObservedPerClass.getValue(i);

				if (this.maxValueObservedPerClass.getValue(i) > maxValue)
					maxValue = this.maxValueObservedPerClass.getValue(i);
			}
		}

		if (minValue < Double.POSITIVE_INFINITY) 
		{
			// Get the range for the attribute.
			double range = maxValue - minValue;
			// For each bin
			for (int i = 0; i < this.numBins; i++) 
			{
				double splitValue = range / (this.numBins + 1.0) * (i + 1) + minValue;

				if ((splitValue > minValue) && (splitValue < maxValue)) 
					suggestedSplitValues.add(splitValue);
			}
		}

		double[] suggestions = new double[suggestedSplitValues.size()];
		int i = 0;

		for (double suggestion : suggestedSplitValues) 
			suggestions[i++] = suggestion;

		return suggestions;
	}
	
	/**
	 * Calculates the class distributions that would result from a binary
	 * split using the plitValue threshold. Assume all values equal to splitValue
	 * go to the left hand side of a split point.
	 * @param splitValue the threshold split.
	 * @return the distributions for each class post the binary split.
	 */
	public double[][] getClassDistsResultingFromBinarySplit(double splitValue) 
	{
		DoubleVector lhsDist = new DoubleVector();
		DoubleVector rhsDist = new DoubleVector();

		// For the number of classes.
		for (int i = 0; i < this.attValDistPerClass.size(); i++) 
		{
			GaussianEstimator estimator = this.attValDistPerClass.get(i);

			if (estimator != null) 
			{
				// If the suggested split value is less than the minimum 
				// attribute value observed for class i - Add the full weight of the
				// instances seen to the right hand side of the split.
				if (splitValue < this.minValueObservedPerClass.getValue(i)) 
					rhsDist.addToValue(i, estimator.getTotalWeightObserved());

				// If the suggested split value is greater than or equal to the maximum 
				// attribute value observed for class i - Add the full weight of the
				// instances seen to the left hand side of the split.
				else if (splitValue >= this.maxValueObservedPerClass.getValue(i)) 
					lhsDist.addToValue(i, estimator.getTotalWeightObserved());
				else 
				{
					// Find the correct split weighting.
					double[] weightDist = estimator.estimatedWeight_LessThan_EqualTo_GreaterThan_Value(splitValue);
					lhsDist.addToValue(i, weightDist[0] + weightDist[1]);// less than and equal to.
					rhsDist.addToValue(i, weightDist[2]); // greater than
				}
			}
		}
		return new double[][]{ lhsDist.getArrayRef(), rhsDist.getArrayRef() };
	}
}