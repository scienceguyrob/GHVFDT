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
 * File name: 	NullAttributeClassObserver.java
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
 * Class for observing the class data distribution for a null attribute.
 * This method is used to disable the observation for an attribute. Based
 * on code in MOA written by Richard Kirkby, modified by Rob Lyon.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class NullAttributeClassObserver implements AttributeClassObserver
{
    /* (non-Javadoc)
     * @see cs.man.ac.uk.observers.AttributeClassObserver#observeAttributeClass(double, int, double)
     */
    @Override
    public void observeAttributeClass(double attVal, int classVal, double weight) {}

    /* (non-Javadoc)
     * @see cs.man.ac.uk.observers.AttributeClassObserver#probabilityOfAttributeValueGivenClass(double, int)
     */
    @Override
    public double probabilityOfAttributeValueGivenClass(double attVal,int classVal) { return 0.0; }

    /* (non-Javadoc)
     * @see cs.man.ac.uk.observers.AttributeClassObserver#getBestEvaluatedSplitSuggestion(cs.man.ac.uk.observers.SplitCriterion, double[], int, boolean)
     */
    @Override
    public AttributeSplitSuggestion getBestEvaluatedSplitSuggestion(
            SplitCriterion criterion, double[] preSplitDist, int attIndex,
            boolean binaryOnly)
    {
        return null;
    }
}
