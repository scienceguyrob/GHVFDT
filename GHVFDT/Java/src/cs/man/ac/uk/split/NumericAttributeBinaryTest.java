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
 * File name: 	NumericAttributeBinaryTest.java
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
 * Numeric binary conditional test for instances to use to split nodes in VFDTs.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class NumericAttributeBinaryTest extends InstanceConditionalBinaryTest 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************
	
	/**
	 * The index of the attribute.
	 */
	protected int attIndex;

	/**
	 * The attribute value.
	 */
	protected double attValue;

	protected boolean passesTest;

	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************

	public NumericAttributeBinaryTest(int attIndex, double attValue,boolean equalsPassesTest)
	{
		this.attIndex = attIndex;
		this.attValue = attValue;
		this.passesTest = equalsPassesTest;
	}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.split.InstanceConditionalTest#branchForInstance(cs.man.ac.uk.data.I_Instance)
	 */
	@Override
	public int branchForInstance(I_Instance inst) 
	{
		int instAttIndex = this.attIndex < -1 ? this.attIndex : this.attIndex + 1;
		
		if (inst.isMissing(instAttIndex))
			return -1;

		double v = inst.value(instAttIndex);
		
		if (v == this.attValue) 
			return this.passesTest ? 0 : 1;

		return v < this.attValue ? 0 : 1;
	}
}