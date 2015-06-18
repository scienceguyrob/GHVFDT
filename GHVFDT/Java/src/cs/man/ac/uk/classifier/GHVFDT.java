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
 * File name: 	GHVFDT.java
 * Package: cs.man.ac.uk.classifier
 * Created:	September 30th, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.classifier;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import cs.man.ac.uk.data.I_Instance;
import cs.man.ac.uk.observers.AttributeClassObserver;
import cs.man.ac.uk.observers.GHNumericAttributeClassObserver;
import cs.man.ac.uk.split.AttributeSplitSuggestion;
import cs.man.ac.uk.split.GHDSplitCriterion;
import cs.man.ac.uk.split.InstanceConditionalTest;
import cs.man.ac.uk.tree.Tree;
import cs.man.ac.uk.tree.nodes.ActiveLearningNode;
import cs.man.ac.uk.tree.nodes.FoundNode;
import cs.man.ac.uk.tree.nodes.InactiveLearningNode;
import cs.man.ac.uk.tree.nodes.LearningNode;
import cs.man.ac.uk.tree.nodes.Node;
import cs.man.ac.uk.tree.nodes.SplitNode;

/**
 * <p>
 * Gaussian-Hellinger Very Fast Decision Tree (GHVFDT). 
 * 
 * A Hoeffding tree is an incremental, any-time decision tree induction algorithm
 * that is capable of learning from massive data streams, assuming that the
 * distribution generating examples does not change over time. Hoeffding trees
 * exploit the fact that a small sample can often be enough to choose an optimal
 * splitting attribute. This idea is supported mathematically by the Hoeffding
 * bound, which quantifies the number of observations (in our case, examples)
 * needed to estimate some statistics within a prescribed precision (in our
 * case, the goodness of an attribute).</p> 
 * 
 * <p>A theoretically appealing feature of Hoeffding Trees not shared by other
 * incremental decision tree learners is that it has sound guarantees of performance.
 * Using the Hoeffding bound one can show that its output is asymptotically nearly
 * identical to that of a non-incremental learner using infinitely many examples. 
 * 
 * The GHVFDT utilizes a decision tree split criterion designed to improve minority
 * class recall rates on imbalanced data streams, i.e. those streams where the class
 * distribution is worse than 1:100. See the following for details:</p>
 * 
 * <p> This implementation is built upon the Hoeffding Tree provided in MOA, thus
 * a great deal of credit goes to the MOA team for their initial implementation and
 * library. We greatly acknowledge their efforts. Though this version of the algorithm
 * is a complete re-write, and thus not compatible with WEKA or MOA, since it it intended
 * for stand-alone use and study.</p>
 * 
 * For more details of the algorithm see,
 * 
 * <p>R. J. Lyon, J. M. Brooke, J. D. Knowles, B. W. Stappers. Hellinger Distance Trees
 * for Imbalanced Streams In 22nd International Conference on Pattern Recognition,
 * pages 1969-1974, Stockholm, Sweden, 2014. IEEE.</p>
 * 
 * For details of the original VFDT model, see:
 * <p>G. Hulten, L. Spencer, and P. Domingos. Mining time-changing data streams.
 * In KDD’01, pages 97-106, San Francisco, CA, 2001. ACM Press.</p>
 *
 * <p>Parameters:</p> 
 * <ul> 
 * 	<li> The number of instances a leaf should observe between split attempts </li>
 * 	<li> The allowable error in split decision, values closer to 0 will take longer to decide </li>
 * 	<li> Threshold below which a split will be forced to break ties </li>
 * 	<li> Only allow binary splits </li>
 * </ul>
 *
 * 
 * <p>Acknowledgments</p>
 * 
 * <p>This work was supported by grant EP/I028099/1 for the University of Manchester Centre
 * for Doctoral Training in Computer Science, from the UK Engineering and Physical Sciences
 * Research Council (EPSRC).</p>
 * 
 * @author Rob Lyon
 */
public class GHVFDT extends Classifier implements Tree
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The number of instances a leaf should observe between split attempts.
	 */
	public int gracePeriod = 200;

	/**
	 * The allowable error in split decision, values closer to 0 will take longer to decide.
	 */
	public double splitConfidence = 0.0000001;

	/**
	 * Threshold below which a split will be forced to break ties.
	 */
	public double tieThreshold = 0.05;

	/**
	 * Only allow binary tree splits.
	 */
	public boolean binarySplits = true;

	/**
	 * The root node of the decision tree.
	 */
	protected Node treeRoot;

	/**
	 * Count of decision nodes in the tree.
	 */
	protected int decisionNodeCount;

	/**
	 * Count of the active leaf nodes in the tree.
	 */
	protected int activeLeafNodeCount;

	/**
	 * Count of the inactive leaf nodes in the tree.
	 */
	protected int inactiveLeafNodeCount;

	/**
	 * When true, allows the tree to grow new splits.
	 */
	protected boolean growthAllowed;

	//*****************************************
	//*****************************************
	//           Getters & Setters
	//*****************************************
	//*****************************************


	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#setGracePeriod(int)
	 */
	public void setGracePeriod(int gracePeriod) { this.gracePeriod = gracePeriod; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#setSplitConfidence(double)
	 */
	public void setSplitConfidence(double splitConfidence) { this.splitConfidence = splitConfidence; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#setTieThreshold(double)
	 */
	public void setTieThreshold(double tieThreshold) { this.tieThreshold = tieThreshold; }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#setBinarySplits(boolean)
	 */
	public void setBinarySplits(boolean binarySplits) { this.binarySplits = binarySplits; }

	//*****************************************
	//*****************************************
	//          Learning methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.classifier.Classifier#trainOnInstance(cs.man.ac.uk.data.I_Instance)
	 */
	public void trainOnInstance(I_Instance inst) 
	{
		boolean isTraining = (inst.weight() > 0.0);

		if (inst.classIsMissing() == true)
			isTraining = false;

		if (isTraining) 
		{
			// If the tree hasn't been initialized, create the root node.
			if (this.treeRoot == null) 
			{
				this.treeRoot = newLearningNode();
				this.activeLeafNodeCount = 1;
			}

			// Find the leaf that the current training instance reaches.
			FoundNode foundNode = this.treeRoot.filterInstanceToLeaf(inst, null, -1);
			Node leafNode = foundNode.node; // Obtain the leaf node.

			// If the leaf hasn't been initialized, then create it.
			if (leafNode == null)
			{
				// Create the new leaf.
				leafNode = newLearningNode();

				// Give the leaf a pointer to its parent node.
				foundNode.parent.setChild(foundNode.parentBranch, leafNode);
				this.activeLeafNodeCount++;
			}

			// If the leaf node is capable of learning...
			if (leafNode instanceof LearningNode) 
			{
				// Cast the node to a learning node to access it.
				LearningNode learningNode = (LearningNode) leafNode;

				// instruct the leaf to learn from the latest training instance.
				learningNode.learnFromInstance(inst, this);

				// If the tree is permitted to grow (i.e. memory limits haven't been
				// reached, and the leaf is a learning node, check if this node should be split.
				if (this.growthAllowed && (learningNode instanceof ActiveLearningNode)) 
				{
					ActiveLearningNode activeLearningNode = (ActiveLearningNode) learningNode;

					// Get the weights seen at the node, i.e. the distribution of examples
					// reaching the node. The distribution is stored in an array [c_1,c_2,...,c_n]
					// such that c_1 is the count of class zero examples, c_2 the count of class 1 examples,
					// and so on until class n.
					double weightSeen = activeLearningNode.getWeightSeen();


					// If the learning node has seen more examples than the grace period, then
					// try to split. Basically the grace period is used to prevent the tree from
					// attempting to split on each example seen. This is useful for streams as
					// the computational overhead of trying to split on each example can be costly.
					// So if the grace period is 100, then after this node has seen 100 examples,
					// it will then attempt to split.
					if (weightSeen - activeLearningNode.getWeightSeenAtLastSplitEvaluation() >= gracePeriod) 
					{
						// SPlit the node if possible.
						split(activeLearningNode, foundNode.parent,foundNode.parentBranch);

						// Update the weight (total number of examples) seen by this learning node.
						activeLearningNode.setWeightSeenAtLastSplitEvaluation(weightSeen);
					}
				}
			}
		}
	}


	/* (non-Javadoc)
	 * @see cs.man.ac.uk.classifier.Classifier#predict(cs.man.ac.uk.data.I_Instance)
	 */
	@Override
	public double[] predict(I_Instance inst)
	{
		// If the tree has been initialized, then make a prediction...
		if (this.treeRoot != null) 
		{
			// Get the leaf node that the instance reaches.
			FoundNode foundNode = this.treeRoot.filterInstanceToLeaf(inst,null, -1);
			Node leafNode = foundNode.node;

			// If the node is uninitialized... 
			if (leafNode == null)
				leafNode = foundNode.parent; // use the parent of the node found instead.

			// Get the prediction votes.
			return leafNode.getClassVotes(inst, this);
		}

		// Else return empty votes.
		return new double[0];
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.classifier.Classifier#classify(cs.man.ac.uk.data.I_Instance)
	 */
	public int classify(I_Instance inst)
	{
		return this.maxIndex(predict(inst));
	}
		
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.classifier.Classifier#correctlyClassifies(cs.man.ac.uk.data.I_Instance)
	 */
	public boolean correctlyClassifies(I_Instance inst) 
	{
		return this.maxIndex(predict(inst)) == (int) inst.classValue();
	}
	
	/**
	 * Computes the Hoeffding bound, used to statistically select optimal
	 * tree split points.
	 * @param range the range of the data.
	 * @param confidence the user specified confidence value delta.
	 * @param n the number of data instances observed.
	 * @return the error, epsilon.
	 */
	public static double computeHoeffdingBound(double range, double confidence,double n) 
	{
		return Math.sqrt(((range * range) * Math.log(1.0 / confidence)) / (2.0 * n));
	}

	/**
	 * Methods which determines the optimal split point. Uses the Gaussian approximation of the
	 * Hellinger distance to measure the distance between the positive and negative class for
	 * a given split point.
	 * 
	 * @param node the current node to split, if possible.
	 * @param parent the parent of node.
	 * @param parentIndex the index of the node in the parent, corresponding to which branch
	 * of the parent it belongs to.
	 */
	public void split(ActiveLearningNode node, SplitNode parent, int parentIndex) 
	{
		// If a node has witnessed an impure distribution.
		if (!node.observedClassDistributionIsPure()) 
		{
			GHDSplitCriterion splitCriterion = new GHDSplitCriterion();

			// Use the split criterion to create split point suggestions.
			AttributeSplitSuggestion[] bestSplitSuggestions = node.getBestSplitSuggestions(splitCriterion, this);
			Arrays.sort(bestSplitSuggestions);

			boolean shouldSplit = false;

			// Check if there are enough split point suggestions.
			if (bestSplitSuggestions.length < 2) { shouldSplit = bestSplitSuggestions.length > 0; }
			else 
			{
				// Compute Hoeffding bound
				double hoeffdingBound = computeHoeffdingBound(splitCriterion.getRangeOfMerit(node.getObservedClassDistribution()),
						splitConfidence, node.getWeightSeen());

				// Get best, and second best split point suggestions.
				AttributeSplitSuggestion bestSuggestion = bestSplitSuggestions[bestSplitSuggestions.length - 1];
				AttributeSplitSuggestion secondBestSuggestion = bestSplitSuggestions[bestSplitSuggestions.length - 2];


				// If the best split point is statistically better then the second, i.e.
				// there is enough evidence to advocate its use.
				if ((bestSuggestion.merit - secondBestSuggestion.merit > hoeffdingBound) || (hoeffdingBound < tieThreshold)) 
					shouldSplit = true;
			}

			// If the checks above indicated that a split should be made...
			if (shouldSplit) 
			{
				// Get the best split point (last in the array).
				AttributeSplitSuggestion splitDecision = bestSplitSuggestions[bestSplitSuggestions.length - 1];

				if (splitDecision.splitTest == null) 
					deactivateLearningNode(node, parent, parentIndex);
				else 
				{
					// Create the new split node
					SplitNode newSplit = newSplitNode(splitDecision.splitTest,node.getObservedClassDistribution(),splitDecision.numSplits() );

					for (int i = 0; i < splitDecision.numSplits(); i++) 
					{
						// create a new child for each split. For a binary split, there
						// will be only two child nodes created.
						Node newChild = newLearningNode(splitDecision.resultingClassDistributionFromSplit(i));
						newSplit.setChild(i, newChild);
					}

					// Update node counts - here one leaf has been replaced by a decision
					// node, and some number of new child nodes. So if the split made above
					// was binary, then this.activeLeafNodeCount will be incremented by two.
					this.activeLeafNodeCount--;
					this.decisionNodeCount++;
					this.activeLeafNodeCount += splitDecision.numSplits();

					// If the parent node is null, then this must be the root node.
					// Therefore make the root the new split point.
					if (parent == null)
						this.treeRoot = newSplit;
					else
						parent.setChild(parentIndex, newSplit);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#newSplitNode(cs.man.ac.uk.observers.InstanceConditionalTest, double[], int)
	 */
	public SplitNode newSplitNode(InstanceConditionalTest splitTest,double[] classObservations, int size) 
	{
		return new SplitNode(splitTest, classObservations, size);
	}
	
	//*****************************************
	//*****************************************
	//        Tree utility methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#newLearningNode()
	 */
	public LearningNode newLearningNode() { return newLearningNode(new double[0]); }

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#newLearningNode(double[])
	 */
	public LearningNode newLearningNode(double[] initialClassObservations)
	{
		LearningNode ret = new ActiveLearningNode(initialClassObservations);

		return ret;
	}
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#deactivateLearningNode(cs.man.ac.uk.tree.nodes.ActiveLearningNode, cs.man.ac.uk.tree.nodes.SplitNode, int)
	 */
	public void deactivateLearningNode(ActiveLearningNode toDeactivate,SplitNode parent, int parentBranch) 
	{
		Node newLeaf = new InactiveLearningNode(toDeactivate.getObservedClassDistribution());

		if (parent == null)
			this.treeRoot = newLeaf;
		else
			parent.setChild(parentBranch, newLeaf);

		this.activeLeafNodeCount--;
		this.inactiveLeafNodeCount++;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#activateLearningNode(cs.man.ac.uk.tree.nodes.InactiveLearningNode, cs.man.ac.uk.tree.nodes.SplitNode, int)
	 */
	public void activateLearningNode(InactiveLearningNode toActivate,SplitNode parent, int parentBranch) 
	{
		Node newLeaf = newLearningNode(toActivate.getObservedClassDistribution());

		if (parent == null)
			this.treeRoot = newLeaf;
		else
			parent.setChild(parentBranch, newLeaf);

		this.activeLeafNodeCount++;
		this.inactiveLeafNodeCount--;
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#findLearningNodes()
	 */
	public FoundNode[] findLearningNodes() 
	{
		List<FoundNode> foundList = new LinkedList<FoundNode>();
		findLearningNodes(this.treeRoot, null, -1, foundList);
		return foundList.toArray(new FoundNode[foundList.size()]);
	}

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#findLearningNodes(cs.man.ac.uk.tree.nodes.Node, cs.man.ac.uk.tree.nodes.SplitNode, int, java.util.List)
	 */
	public void findLearningNodes(Node node, SplitNode parent, int parentBranch, List<FoundNode> found) 
	{
		if (node != null) 
		{
			if (node instanceof LearningNode)
				found.add(new FoundNode(node, parent, parentBranch));

			if (node instanceof SplitNode) 
			{
				SplitNode splitNode = (SplitNode) node;
				for (int i = 0; i < splitNode.numChildren(); i++)
					findLearningNodes(splitNode.getChild(i), splitNode, i,found);
			}
		}
	}


	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#newNumericClassObserver()
	 */
	public AttributeClassObserver newNumericClassObserver() 
	{
		AttributeClassObserver numericClassObserver = (AttributeClassObserver) new GHNumericAttributeClassObserver();
		return (AttributeClassObserver) numericClassObserver;
	}
	

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.Tree#deactivateAllLeaves()
	 */
	public void deactivateAllLeaves() 
	{
		FoundNode[] learningNodes = findLearningNodes();

		for (int i = 0; i < learningNodes.length; i++) 
		{
			if (learningNodes[i].node instanceof ActiveLearningNode) 
			{
				deactivateLearningNode(
						(ActiveLearningNode) learningNodes[i].node,
						learningNodes[i].parent, learningNodes[i].parentBranch);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see cs.man.ac.uk.classifier.Classifier#resetLearning()
	 */
	@Override
	public void resetLearning()
	{
		this.treeRoot = null;
		this.decisionNodeCount = 0;
		this.activeLeafNodeCount = 0;
		this.inactiveLeafNodeCount = 0;
		this.growthAllowed = true;
	}
}