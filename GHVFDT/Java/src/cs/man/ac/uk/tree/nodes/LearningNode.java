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
 * File name: 	LearningNode.java
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

/**
 * The basic learning node object within the GHVFDT
 *
 * @author Richard Kirkby (rkirkby@cs.waikato.ac.nz)
 * @author Rob Lyon
 */
public abstract class LearningNode extends Node
{
	/**
	 * Creates a new instance of a learning node.
	 * @param initialClassObservations the class observations to initialize this node with.
	 */
	public LearningNode(double[] initialClassObservations) { super(initialClassObservations); }

	/**
	 * Attempts to learn from the supplied instance.
	 * @param inst the instance to learn from.
	 * @param ht the tree the node belongs to.
	 */
	public abstract void learnFromInstance(I_Instance inst,GHVFDT ht);
}
