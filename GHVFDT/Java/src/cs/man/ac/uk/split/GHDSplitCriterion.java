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
 * File name: 	GHDSplitCriterion.java
 * Package: cs.man.ac.uk.split
 * Created:	September 30th, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.split;

/**
 * Implementation of the Hellinger Distance as a decision tree split criterion as described in
 * "Learning Decision Trees for Unbalanced Data", Cieslak and Chawla, 2008 and "Hellinger distance
 * decision trees are robust and skew-insensitive", Cieslak et al., 2012. This split criterion is to
 * be used on imbalanced data sets.
 *
 * @author Rob Lyon
 */
public class GHDSplitCriterion implements SplitCriterion 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/** 
	 * The natural logarithm of 2.
	 */
	public static double log2 = Math.log(2);

	/**
	 * The minimum proportion of examples that should be present in
	 * a class distribution resulting from a split.
	 */
	double minBranchFrac = 0.01;

	//*****************************************
	//*****************************************
	//               Methods
	//*****************************************
	//*****************************************

	/**
	 * Computes the merit of splitting for a given distribution before the split and after it. For
	 * instance, given a two class input distribution this function will determine the merit of
	 * the pre-split distribution, minus the merit of splitting this distribution according to some 
	 * attribute.
	 * 
	 * Example:
	 * 
	 * preSplitDist = {100,200} where 100 is the number of instances belonging to class zero and 200
	 *                          is the number of instances belonging to class one.
	 * 
	 * postSplitDists = { {25,75} , {150,50} }
	 * 			    		|           |
	 *                      v           v
	 *                First branch  Second branch
	 *                
	 * To be clear, postSplitDists[0] contains the distribution for the first branch and postSplitDists[1]
	 * contains the distribution for the second branch. Then postSplitDists[0][0] contains the number of class
	 * zero instances at the first branch and postSplitDists[0][1] is the number of class one instances at 
	 * the first branch (defined similarly for the other branch).
	 * 
	 * @param preSplitDist the distribution before the split. For two class case preSplitDist
	 * 		  will contain two elements where preSplitDist[0] contains the number of class zero
	 *        instances and preSplitDist[1] contains the number of class one instances.
	 * @param postSplitDists the distribution after the split.
	 */
	@Override
	public double getMeritOfSplit(double[] preSplitDist,double[][] postSplitDists) 
	{
		if (numSubsetsGreaterThanFrac(postSplitDists, this.minBranchFrac) < 2) 

			return Double.NEGATIVE_INFINITY;

		return computeHellinger(postSplitDists);
	}

	/**
	 * Hellinger distance as used by Cieslak and Chawla.
	 * @param dist the distribution of instances.
	 * @return the hellinger distance between them.
	 */
	public static double computeHellinger(double[][] dist) 
	{
		if(dist.length==1)
			return 0; // Can't compute distance here.

		double leftBranchNegatives = 0;
		double leftBranchPositives = 0;

		double rightBranchNegatives = 0;
		double rightBranchPositives = 0;

		try{ leftBranchNegatives = dist[0][0]; }catch(Exception e){return 0;}// Can't compute distance here.
		try{ leftBranchPositives = dist[0][1]; }catch(Exception e){return 0;}// Can't compute distance here.

		try{ rightBranchNegatives = dist[1][0]; }catch(Exception e){return 0;}// Can't compute distance here.
		try{ rightBranchPositives = dist[1][1]; }catch(Exception e){return 0;}// Can't compute distance here.

		double totalNegatives = leftBranchNegatives+rightBranchNegatives;
		double totalPositives = leftBranchPositives+rightBranchPositives;


		double hellinger = 0.0;

		hellinger= Math.pow(Math.sqrt(leftBranchNegatives/totalNegatives)-Math.sqrt(leftBranchPositives/totalPositives),2) + 
				Math.pow(Math.sqrt(rightBranchNegatives/totalNegatives)-Math.sqrt(rightBranchPositives/totalPositives),2);

		return Math.sqrt(hellinger);
	}

	/**
	 * Computes the Hellinger distance between two normal distributions P and Q.
	 * 
	 * The function used is:
	 * 
	 * H(P,Q)= 1 - SQRT( 2 * standard deviation of P * standard deviation of Q / variance of P + variance of Q) * e^{ -1/4 * ( (mean of P - mean of Q)^{2} / variance of P + variance of Q)}
	 * @param P_mean the mean of the positive distribution.
	 * @param P_variance the variance of the positive distribution.
	 * @param Q_mean the mean of the negative distribution.
	 * @param Q_variance the variance of the negative distribution.
	 * @return the Hellinger distance for the given class distribution.
	 */
	public static double computeHellinger(double P_mean, double P_variance,double Q_mean,double Q_variance) 
	{
		double hellinger = 0.0;

		double P_stdev = Math.sqrt(P_variance);
		double Q_stdev = Math.sqrt(Q_variance);

		hellinger = (double)1 - Math.sqrt(((double)2 * P_stdev * Q_stdev)/(P_variance+Q_variance)) * Math.exp((-(double)1/(double)4)*(Math.pow(P_mean-Q_mean, 2) / (P_variance+Q_variance)));
		return Math.sqrt(hellinger);
	}

	/**
	 * Gets the value of the range of splitting merit.
	 */
	@Override
	public double getRangeOfMerit(double[] preSplitDist) 
	{
		int numClasses = preSplitDist.length > 2 ? preSplitDist.length : 2;
		return log2(numClasses);
	}

	/**
	 * Returns the logarithm of a for base 2.
	 * 
	 * @param a a double
	 * @return the logarithm for base 2
	 */
	public static double log2(double a) { return Math.log(a) / log2; }

	/**
	 * Computes the number of subsets resulting from the split,
	 * possessing more examples than the minimum fraction of examples.
	 * 
	 * For instance lets say the minimum fraction = 0.01 = 1%. now consider
	 * the following binary split, 
	 * 
	 *   					0     1   class
	 *   					|     |
	 *   					v     v
	 * 					  [ 20 , 80 ] distribution  
	 * 						   |
	 * 			------------------------------
	 * 			|							 |
	 *          v							 v
	 *     [ 6 , 30 ]                   [ 14 , 50 ] distributions post split.
	 *       ^    ^                        ^    ^
	 *       |    |                        |    |
	 *       0    1                        0    1   class
	 *          
	 * 	   splitIndex = 0              splitIndex = 1
	 * 
	 * There are 100 examples in total. For the left split, we have
	 * 
	 * 6  / 100 = 0.06 = 6%
	 * 30 / 100 = 0.30 = 30%
	 * 
	 * and for the right hand split we have,
	 * 
	 * 14  / 100 = 0.14 = 14%
	 * 50  / 100 = 0.50 = 50%
	 * 
	 * So here each subset resulting from the split has a number of examples greater
	 * than the minimum fraction. Thus in this case, 4 would be returned.
	 * 
	 * @param distributions the input distributions to check.
	 * @param minFrac the minimum fraction of examples to check for.
	 * @return the number of subsets greater than minFrac
	 */
	public static int numSubsetsGreaterThanFrac(double[][] distributions,double minFrac) 
	{
		double totalWeight = 0.0;
		double[] distSums = new double[distributions.length];

		for (int i = 0; i < distSums.length; i++) 
		{
			for (int j = 0; j < distributions[i].length; j++) 
				distSums[i] += distributions[i][j];

			totalWeight += distSums[i];
		}

		int numGreater = 0;

		for (double d : distSums) 
		{
			double frac = d / totalWeight;

			if (frac > minFrac) 
				numGreater++;
		}
		
		return numGreater;
	}
}