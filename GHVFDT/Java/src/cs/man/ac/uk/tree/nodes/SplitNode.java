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
 * File name: 	SplitNode.java
 * Package: cs.man.ac.uk.tree.nodes
 * Created:	September 30th, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.tree.nodes;


import cs.man.ac.uk.data.I_Instance;
import cs.man.ac.uk.split.InstanceConditionalTest;
import cs.man.ac.uk.utils.AutoExpandVector;

/**
 * Represents a split node node, used to partition the data space.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class SplitNode extends Node 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The split test used at this node.
	 */
	protected InstanceConditionalTest splitTest;
	
	/**
	 * The children nodes below this split point.
	 */
	protected AutoExpandVector<Node> children;

	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************

	/**
	 * Creates a new split point.
	 * @param splitTest the actual split test.
	 * @param classObservations the class distribution used to initialize this node.
	 * @param size the number of split points used, i.e. for binary splits size = 2.
	 */
	public SplitNode(InstanceConditionalTest splitTest,double[] classObservations, int size) 
	{
		super(classObservations);
		this.splitTest = splitTest;
		this.children = new AutoExpandVector<Node>(size);
	}

	/**
	 * Creates a new split point.
	 * @param splitTest the actual split test.
	 * @param classObservations the class distribution used to initialize this node.
	 */
	public SplitNode(InstanceConditionalTest splitTest,double[] classObservations) 
	{
		super(classObservations);
		this.splitTest = splitTest;
		this.children = new AutoExpandVector<Node>();
	}

	//*****************************************
	//*****************************************
	//            Getters / Setters
	//*****************************************
	//*****************************************
	
	/**
	 * @return the number of child nodes below this split point in the tree.
	 */
	public int numChildren() { return this.children.size(); }

	public void setChild(int index, Node child) 
	{
		if ((this.splitTest.maxBranches() >= 0) && (index >= this.splitTest.maxBranches())) 
			throw new IndexOutOfBoundsException();

		this.children.set(index, child);
	}

	public Node getChild(int index) { return this.children.get(index); }

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	public int instanceChildIndex(I_Instance inst) { return this.splitTest.branchForInstance(inst); }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.nodes.Node#isLeaf()
	 */
	@Override
	public boolean isLeaf() { return false; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.nodes.Node#filterInstanceToLeaf(cs.man.ac.uk.data.I_Instance, cs.man.ac.uk.tree.nodes.SplitNode, int)
	 */
	@Override
	public FoundNode filterInstanceToLeaf(I_Instance inst, SplitNode parent,int parentBranch) 
	{
		int childIndex = instanceChildIndex(inst);

		if (childIndex >= 0) 
		{
			Node child = getChild(childIndex);

			if (child != null) 
				return child.filterInstanceToLeaf(inst, this, childIndex);

			return new FoundNode(null, this, childIndex);
		}
		return new FoundNode(this, parent, parentBranch);
	}
}