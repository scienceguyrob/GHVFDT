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
 * File name: 	SplitCriterion.java
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
 * Interface for computing splitting criteria, with respect to
 * distributions of class values.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public interface SplitCriterion
{
    /**
     * Computes the merit of splitting for a given distribution
     * before the split and after it.
     *
     * @param preSplitDist the class distribution before the split
     * @param postSplitDists the class distribution after the split
     * @return value of the merit of splitting
     */
    public double getMeritOfSplit(double[] preSplitDist,double[][] postSplitDists);

    /**
     * Computes the range of splitting merit
     *
     * @param preSplitDist the class distribution before the split
     * @return value of the range of splitting merit
     */
    public double getRangeOfMerit(double[] preSplitDist);
}
