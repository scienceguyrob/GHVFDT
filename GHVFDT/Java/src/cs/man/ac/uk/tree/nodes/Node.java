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
 * File name: 	Node.java
 * Package: cs.man.ac.uk.tree.nodes
 * Created:	September 30th, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.tree.nodes;

import cs.man.ac.uk.classifier.GHVFDT;
import cs.man.ac.uk.data.I_Instance;
import cs.man.ac.uk.utils.DoubleVector;

/**
 * The basic tree node object.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class Node
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The class distribution observed at this node. Each entry
	 * in the vector contains a count of examples observed of
	 * each class.
	 */
	protected DoubleVector observedClassDistribution;

	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param classObservations the  distribution observed at the node.
	 */
	public Node(double[] classObservations) 
	{
		this.observedClassDistribution = new DoubleVector(classObservations);
	}

	//*****************************************
	//*****************************************
	//            Getters / Setters
	//*****************************************
	//*****************************************

	/**
	 * @return the observed class distribution.
	 */
	public double[] getObservedClassDistribution() 
	{
		return this.observedClassDistribution.getArrayCopy();
	}

	/**
	 * Gets the predictive votes for each class at this node, for
	 * the supplied instance. The votes are actually the counts of
	 * observed examples of each class. For example, [ 10, 50, 32 ] 
	 * would correspond to 10 votes for class 0, 50 for class 1, and
	 * 32 for class 2.
	 * 
	 * @param inst the data instance to obtain the predictive votes for.
	 * @param ht the GHVFDT the node belongs to.
	 * @return the votes for each class as a double array.
	 */
	public double[] getClassVotes(I_Instance inst, GHVFDT ht) 
	{
		return this.observedClassDistribution.getArrayCopy();
	}

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/**
	 * @return true if this is a leaf node, and not a learning node.
	 */
	public boolean isLeaf() { return true; }

	/**
	 * Passes the instance to a leaf node for classification. If this node is not 
	 * a leaf node, it is passed down recursively.
	 * @param inst the instance to filter down.
	 * @param parent the parent split node.
	 * @param parentBranch the index of the node being deactivated, in the parent,
	 * corresponding to which branch of the parent it belongs to.
	 * @return the leaf node found.
	 */
	public FoundNode filterInstanceToLeaf(I_Instance inst, SplitNode parent, int parentBranch) 
	{
		return new FoundNode(this, parent, parentBranch);
	}

	/**
	 * @return true if all examples observed by this node belong
	 * to a single class.
	 */
	public boolean observedClassDistributionIsPure() 
	{
		return this.observedClassDistribution.numNonZeroEntries() < 2;
	}
}