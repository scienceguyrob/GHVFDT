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
 * File name: 	FoundNode.java
 * Package: cs.man.ac.uk.tree.nodes
 * Created:	September 30th, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.tree.nodes;

/**
 * Represents a node found when searching for a node in a decision tree,
 * or when trying to find which node a data instance ends up at.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class FoundNode 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The node found.
	 */
	public Node node;
	
	/**
	 * The parent node that the found node belongs to.
	 */
	public SplitNode parent;
	
	/**
	 * The index of the node in the parent, corresponding to which branch
	 * of the parent it belongs to.
	 */
	public int parentBranch;


	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************
	
	/**
	 * Default constructor.
	 * @param node the node found.
	 * @param parent the parent of the node found.
	 * @param parentBranch the index of the node in the parent, corresponding to which branch
	 * of the parent it belongs to.
	 */
	public FoundNode(Node node, SplitNode parent, int parentBranch) 
	{
		this.node = node;
		this.parent = parent;
		this.parentBranch = parentBranch;
	}
}