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
 * File name: 	AttributeClassObserver.java
 * Package: cs.man.ac.uk.observers
 * Created:	September 30th, 2013
 * Author:	Richard Kirkby (rkirkby@cs.waikato.ac.nz), Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */

package cs.man.ac.uk.observers;

import cs.man.ac.uk.split.AttributeSplitSuggestion;
import cs.man.ac.uk.split.SplitCriterion;


/**
 * This observer monitors the class distribution of a given attribute. Based on
 * code in MOA written by Richard Kirkby, modified by Rob Lyon.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public interface AttributeClassObserver 
{
    /**
     * Updates statistics of this observer given an attribute value, a class
     * and the weight of the instance observed
     *
     * @param attVal the value of the attribute
     * @param classVal the class
     * @param weight the weight of the instance
     */
    public void observeAttributeClass(double attVal, int classVal, double weight);

    /**
     * Gets the probability (relative likelihood) of an attribute value for a given class.
     *
     * @param attVal the attribute value
     * @param classVal the class
     * @return probability for an attribute value given a class
     */
    public double probabilityOfAttributeValueGivenClass(double attVal,int classVal);

    /**
     * Gets the best split suggestion given a criterion and a class distribution
     *
     * @param criterion the split criterion to use
     * @param preSplitDist the class distribution before the split
     * @param attIndex the attribute index
     * @param binaryOnly true to use binary splits
     * @return suggestion of best attribute split
     */
    public AttributeSplitSuggestion getBestEvaluatedSplitSuggestion(
            SplitCriterion criterion, double[] preSplitDist, int attIndex,
            boolean binaryOnly);
}