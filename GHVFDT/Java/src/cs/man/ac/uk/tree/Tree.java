/**
 *
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
 * File name: 	Tree.java
 * Package: cs.man.ac.uk.tree
 * Created:	May 21st, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.tree;

import java.util.List;

import cs.man.ac.uk.observers.AttributeClassObserver;
import cs.man.ac.uk.split.InstanceConditionalTest;
import cs.man.ac.uk.tree.nodes.ActiveLearningNode;
import cs.man.ac.uk.tree.nodes.FoundNode;
import cs.man.ac.uk.tree.nodes.InactiveLearningNode;
import cs.man.ac.uk.tree.nodes.LearningNode;
import cs.man.ac.uk.tree.nodes.Node;
import cs.man.ac.uk.tree.nodes.SplitNode;

/**
 * Basic interface for a decision tree.
 * 
 * @author Rob Lyon
 */
public interface Tree
{
	/*
	 *	Variable Getters / Setters
	 */
	
	/**
	 * Sets the number of instances a leaf should observe between
	 * split attempts.
	 * 
	 * @param gracePeriod the number of instances a leaf should observe between split attempts.
	 */
	public void setGracePeriod(int gracePeriod);

	/**
	 * Sets the allowable error in split decision, values closer to 0 will take longer to decide.
	 * @param splitConfidence the allowable error in the split decision.
	 */
	public void setSplitConfidence(double splitConfidence);

	/**
	 * Sets the threshold below which a split will be forced to break ties.
	 * This is used to break ties when two features are found to be equally
	 * good split points.
	 * 
	 * @param tieThreshold the tie threshold
	 */
	public void setTieThreshold(double tieThreshold);

	/**
	 * Sets a flag allowing only binary tree splits. If set to false,
	 * than no binary splits can be used.
	 * @param binarySplits the new value for the flag.
	 */
	public void setBinarySplits(boolean binarySplits);

	/*
	 *	Tree learning methods
	 */
	
	/**
	 * Methods which determines the optimal split point.
	 * 
	 * @param node the current node to split, if possible.
	 * @param parent the parent of node.
	 * @param parentIndex the index of the node in the parent,
	 * corresponding to which branch of the parent it belongs to.
	 */
	public void split(ActiveLearningNode node, SplitNode parent, int parentIndex);

	/**
	 * Creates a new tree split node.
	 * @param splitTest the split point test.
	 * @param classObservations the class distributions used to initialize the split point.
	 * @param size the number of split point in the split, i.e. binary = 2, or non-binary.
	 * @return the new split point.
	 */
	public SplitNode newSplitNode(InstanceConditionalTest splitTest,double[] classObservations, int size);

	
	/*
	 *	Tree utility methods
	 */
	
	/**
	 * @return a new learning node capable of making predictions.
	 */
	public LearningNode newLearningNode();

	/**
	 * @param initialClassObservations the class distributions used to initialize the node,
	 * and used for making predictions. The class distribution is stored in a double array,
	 * such that each element in the array contains the total count of examples for a specific
	 * class. For example initialClassObservations = [ 10 , 45 ] using zero indexing indicates 
	 * there are 10 members of class zero, and 45 of class one.
	 * @return a new learning node capable of making predictions.
	 */
	public LearningNode newLearningNode(double[] initialClassObservations);

	/**
	 * Deactivates a learning node, useful when memory is limited.
	 * @param toDeactivate the node to deactivate.
	 * @param parent the parent node of the node being deactivated.
	 * @param parentBranch  the index of the node being deactivated, in the parent,
	 * corresponding to which branch of the parent it belongs to.
	 */
	public void deactivateLearningNode(ActiveLearningNode toDeactivate,SplitNode parent, int parentBranch);

	/**
	 * Activates a learning node, perhaps previous deactivated due to limited memory.
	 * @param toActivate the node to activate.
	 * @param parent the parent node of the node being activated.
	 * @param parentBranch the index of the node being activated, in the parent,
	 * corresponding to which branch of the parent it belongs to.
	 */
	public void activateLearningNode(InactiveLearningNode toActivate,SplitNode parent, int parentBranch);
	
	/**
	 * @return all learning nodes found in the tree.
	 */
	public FoundNode[] findLearningNodes();
	
	/**
	 * Finds the learning nodes recursively, starting at the specified node.
	 * Found nodes are not returned directly, rather they are appended to the List
	 * object passed in to this method.
	 * @param node the node to search from.
	 * @param parent of the node to search from, a split point node.
	 * @param parentBranch the index of the node being activated, in the parent,
	 * corresponding to which branch of the parent it belongs to.
	 * @param found the list to add found nodes to.  
	 */
	public void findLearningNodes(Node node, SplitNode parent,int parentBranch, List<FoundNode> found);
	
	/**
	 * @return a new observer object used to monitor the data distributions for an
	 * individual feature.
	 */
	public AttributeClassObserver newNumericClassObserver();
	
	/**
	 * Deactivates all leaves in the tree.
	 */
	public void deactivateAllLeaves();	
}