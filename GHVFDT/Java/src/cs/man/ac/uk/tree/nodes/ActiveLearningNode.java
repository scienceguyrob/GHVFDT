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
 * File name: 	ActiveLearningNode.java
 * Package: cs.man.ac.uk.tree.nodes
 * Created:	September 30th, 2013
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.tree.nodes;

import java.util.LinkedList;
import java.util.List;

import cs.man.ac.uk.classifier.GHVFDT;
import cs.man.ac.uk.data.I_Instance;
import cs.man.ac.uk.observers.AttributeClassObserver;
import cs.man.ac.uk.split.AttributeSplitSuggestion;
import cs.man.ac.uk.split.SplitCriterion;
import cs.man.ac.uk.utils.AutoExpandVector;

/**
 * The main learning node object within the GHVFDT.
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public class ActiveLearningNode extends LearningNode 
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * The weight observed since a split at this node was last considered.
	 */
	protected double weightSeenAtLastSplitEvaluation;

	/**
	 * Used to monitor the data distributions for each feature at this node.
	 */
	protected AutoExpandVector<AttributeClassObserver> attributeObservers = new AutoExpandVector<AttributeClassObserver>();

	/**
	 * Flag that indicates whether or not this node is activated.
	 */
	protected boolean isInitialized;

	//*****************************************
	//*****************************************
	//              Constructor
	//*****************************************
	//*****************************************

	/**
	 * Default constructor.
	 * @param initialClassObservations the class observations used to initialize this node.
	 */
	public ActiveLearningNode(double[] initialClassObservations)
	{
		super(initialClassObservations);
		this.weightSeenAtLastSplitEvaluation = getWeightSeen();
		this.isInitialized = false;		
	}

	//*****************************************
	//*****************************************
	//           Getters / Setters
	//*****************************************
	//*****************************************

	/**
	 * @return the weight of examples seen at this node (weight is 1.0 per example).
	 */
	public double getWeightSeen() { return this.observedClassDistribution.sumOfValues();}
	
	/**
	 * @return the weight observed since a split at this node was last considered, if this is greater
	 * than the grace period then a split will be attempted.
	 */
	public double getWeightSeenAtLastSplitEvaluation() { return this.weightSeenAtLastSplitEvaluation; }
	
	/**
	 * @param weight sets the weight seen since a split at this node was last considered.
	 */
	public void setWeightSeenAtLastSplitEvaluation(double weight) { this.weightSeenAtLastSplitEvaluation = weight; }

	//*****************************************
	//*****************************************
	//              Methods
	//*****************************************
	//*****************************************

	/* (non-Javadoc)
	 * @see cs.man.ac.uk.tree.nodes.LearningNode#learnFromInstance(cs.man.ac.uk.data.I_Instance, cs.man.ac.uk.classifier.GHVFDT)
	 */
	@Override
	public void learnFromInstance(I_Instance inst, GHVFDT ht)
	{
		if (this.isInitialized == false) 
		{
			this.attributeObservers = new AutoExpandVector<AttributeClassObserver>(inst.numAttributes());
			this.isInitialized = true;
		}

		this.observedClassDistribution.addToValue((int) inst.classValue(),inst.weight());

		for (int i = 0; i < inst.numAttributes() - 1; i++)
		{
			AttributeClassObserver obs = this.attributeObservers.get(i);

			if (obs == null)
			{
				obs =  ht.newNumericClassObserver();
				this.attributeObservers.set(i, obs);
			}

			obs.observeAttributeClass(inst.value(i), (int) inst.classValue(), inst.weight());
		}
	}

	/**
	 * Gets the best split point suggestions at this node.
	 * @param criterion the split criterion used ( Hellinger distance).
	 * @param ht the GHVFDT this node belongs to.
	 * @return an array of split point suggestions, if there are any.
	 */
	public AttributeSplitSuggestion[] getBestSplitSuggestions(SplitCriterion criterion, GHVFDT ht)
	{
		List<AttributeSplitSuggestion> bestSuggestions = new LinkedList<AttributeSplitSuggestion>();
		double[] preSplitDist = this.observedClassDistribution.getArrayCopy();

		for (int i = 0; i < this.attributeObservers.size(); i++)
		{
			AttributeClassObserver obs = this.attributeObservers.get(i);
			if (obs != null)
			{
				AttributeSplitSuggestion bestSuggestion = obs.getBestEvaluatedSplitSuggestion(criterion,
						preSplitDist, i, ht.binarySplits);

				if (bestSuggestion != null)
					bestSuggestions.add(bestSuggestion);
			}
		}

		return bestSuggestions.toArray(new AttributeSplitSuggestion[bestSuggestions.size()]);
	}
}