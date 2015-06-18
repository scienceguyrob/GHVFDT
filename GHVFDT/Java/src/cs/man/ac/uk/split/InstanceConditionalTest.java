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
 * File name: 	InstanceConditionalTest.java
 * Package: cs.man.ac.uk.split
 * Created:	September 30th, 2013
 * Author:	Richard Kirkby (rkirkby@cs.waikato.ac.nz), Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.split;

import cs.man.ac.uk.data.I_Instance;

/**
 * Abstract conditional test for instances to use to split nodes in VFDTs.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public abstract class InstanceConditionalTest
{
    /**
     *  Returns the number of the branch for an instance, -1 if unknown.
     *
     * @param inst the data instance to be used
     * @return the number of the branch for an instance, -1 if unknown.
     */
    public abstract int branchForInstance(I_Instance inst);

    /**
     * Gets whether the number of the branch for an instance is known.
     *
     * @param inst the data instance.
     * @return true if the number of the branch for an instance is known
     */
    public boolean resultKnownForInstance(I_Instance inst) 
    {
        return branchForInstance(inst) >= 0;
    }

    /**
     * Gets the number of maximum branches, -1 if unknown.
     *
     * @return the number of maximum branches, -1 if unknown..
     */
    public abstract int maxBranches();
}