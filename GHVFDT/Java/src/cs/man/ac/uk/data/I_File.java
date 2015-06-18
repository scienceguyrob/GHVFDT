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
 * File name: 	IFIle.java
 * Package: cs.man.ac.uk.data
 * Created:	May 21st, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.data;

import java.util.List;

/**
 * A file interface defining the methods used to read in data
 * usable by the GHVFDT.
 * 
 * @author Rob Lyon
 *
 */
public interface I_File 
{

	/**
	 * @return true if this file is valid, else false.
	 */
	public boolean isValid();
	
	/**
	 * @return a count of the number of data instances in the file. Zero is returned
	 * if no examples are present.
	 */
	public int countExamples();
	
	/**
	 * @return all instances in this file as a single batch, else returns an empty list
	 * if no instance examples are available.
	 */
	public List<I_Instance> getAll();
	
	/**
	 * @return the next instance from this file, if it is read incrementally, else
	 * null if there are no more instances remaining in the file.
	 */
	public I_Instance getNext();
	
	/**
	 * Counts the number of occurrences of each class in the file. Returns an integer
	 * array <i>dists</i> of these counts, such that <i>dists[0]</i> counts the number
	 * of occurrences of class zero examples, <i>dists[1]</i> the number of occurrences
	 * of class one examples and so on.
	 * @return the class distribution, i.e. counts of each class in the the file. Else
	 * an empty array if no distributional data is available.
	 */
	public int[] getClassDistribution();
	
	/**
	 * @return a count of the number of features in the file. Zero is returned if no
	 * features are found.
	 */
	public int getFeatureCount();
}
