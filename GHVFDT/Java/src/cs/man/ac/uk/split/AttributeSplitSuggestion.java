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
 * File name: 	AttributeSplitSuggestion.java
 * Package: cs.man.ac.uk.split
 * Created:	September 30th, 2013
 * Author:	Richard Kirkby (rkirkby@cs.waikato.ac.nz), Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */

package cs.man.ac.uk.split;


/**
 * Class for computing attribute split suggestions given a split test.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class AttributeSplitSuggestion implements Comparable<AttributeSplitSuggestion> 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************
	
	/**
	 * The split test.
	 */
	public InstanceConditionalTest splitTest;

	/**
	 * The class distributions resulting from the split.
	 */
	public double[][] resultingClassDistributions;

	/**
	 * The merit of the split.
	 */
	public double merit;

	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param splitTest the new split test.
	 * @param resultingClassDistributions the class distributions that result from using the split.
	 * @param merit the merit of the split.
	 */
	public AttributeSplitSuggestion(InstanceConditionalTest splitTest,double[][] resultingClassDistributions, double merit) 
	{
		this.splitTest = splitTest;
		this.resultingClassDistributions = resultingClassDistributions.clone();
		this.merit = merit;
	}

	//*****************************************
	//*****************************************
	//                 Methods
	//*****************************************
	//*****************************************

	/**
	 * @return the number of split points entailed by the split.
	 */
	public int numSplits() 
	{
		return this.resultingClassDistributions.length;
	}

	/**
	 * Gets the class distribution resulting from the split, at specific
	 * split index. For example consider the following binary split, given 
	 * the starting distribution of:
	 *
	 *   0     1   class
	 *   |     |
	 *   v     v
	 * [ 20 , 80 ] distribution 
	 * 
	 * In other words there are 20 class 0, and 80 class 1 examples. Now suppose
	 * a binary split is created. It splits this distribution in two. Here is one
	 * possible outcome,
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
	 * This method gets the split distribution at the specified split index as shown
	 * above, i.e. here calling resultingClassDistributionFromSplit(0) would return
	 * the array [ 6 , 30 ].
	 * 
	 * @param splitIndex the index of the branch of the split to obtain the class distribution for.
	 * @return the class distribution as an array.
	 */
	public double[] resultingClassDistributionFromSplit(int splitIndex) 
	{
		return this.resultingClassDistributions[splitIndex].clone();
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AttributeSplitSuggestion comp) 
	{
		return Double.compare(this.merit, comp.merit);
	}
}