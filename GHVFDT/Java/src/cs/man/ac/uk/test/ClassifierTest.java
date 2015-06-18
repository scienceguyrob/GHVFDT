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
package cs.man.ac.uk.test;

import cs.man.ac.uk.classifier.GHVFDT;
import cs.man.ac.uk.data.ARFFFile;
import cs.man.ac.uk.data.I_Instance;

/**
 * Shows how a GHVFDT can be trained and tested.
 * 
 * @author Rob Lyon
 */
public class ClassifierTest
{
	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	// Test variables, change as appropriate.

	/**
	 * The path to the directory containing test files.
	 */
	private static String root = "/home/rob/git/scienceguyrob/GHVFDT/GHVFDT/data/test/";

	/**
	 * Incremental data file read flag.
	 */
	private static boolean inc = true;

	/**
	 * Verbose logging flag.
	 */
	private static boolean verbose= true;

	/**
	 * FIle extension for test files.
	 */
	private static String ext = ".arff";

	//*****************************************
	//*****************************************
	//    Main method that runs the tests
	//*****************************************
	//*****************************************

	/**
	 * Shows how to use the classifier in practice to classifier data.
	 * @param args unused arguments.
	 */
	public static void main(String[] args) 
	{
		// Create classifier.
		GHVFDT tree = new GHVFDT();

		// Sub-directory containing test files.
		String directory = "Test_0/";

		// Create the training set file object.
		ARFFFile trainingSet =new ARFFFile(root + directory + "Train" + ext ,inc,verbose);

		// Print details of the training set.
		print(trainingSet.toString());

		// Create the test set file object.
		ARFFFile testSet =new ARFFFile(root + directory + "Test" + ext ,inc,verbose);

		// Print details of the test set.
		print(testSet.toString());

		/*
		 * Now train and test the tree.
		 */

		print("Training the tree");

		// Declare some variables used to monitor tree statistics.
		double accuracy = 0;
		double numberSamples = 0;
		double numberSamplesCorrect = 0;

		// Begin timing the training phase.
		long startTime = System.nanoTime();

		I_Instance inst;

		// For each instance in the training file.
		while((inst = trainingSet.getNext()) != null)
		{
			// Check if the tree correctly classifies it.
			if(tree.correctlyClassifies(inst))
				numberSamplesCorrect++;

			// Train on the instance.
			tree.trainOnInstance(inst);

			// Increment count of examples trained on.
			numberSamples++;
		}

		// Stop timing training phase.
		long endTime = System.nanoTime();
		long nanoseconds = endTime - startTime;
		double seconds = (double) nanoseconds / 1000000000.0;

		// Calculate training accuracy.
		accuracy = 100.0 * (double) numberSamplesCorrect / (double) numberSamples; 

		/*
		 * Print out some training details...
		 */
		print("Training data instances: " + numberSamples);
		print("Tree trained on " + numberSamples + " instances & has " + accuracy + "% accuracy.");
		print("Training tree completed in "+nanoseconds+" (ns) or "+seconds+" (s)");

		/*
		 * Now test the classifier...
		 */

		print("Testing the tree");

		// Start timing the classification phase.
		startTime = System.nanoTime();

		// Reset variables.
		inst = null;
		numberSamplesCorrect = 0;
		numberSamples = 0;

		// For each test example.
		while((inst = testSet.getNext()) != null)
		{
			// Check if the tree correctly classifies it.
			if(tree.correctlyClassifies(inst))
				numberSamplesCorrect++;

			// Train on the instance, which will only happen if
			// the label is available.
			tree.trainOnInstance(inst);

			// Increment count of examples tested on.
			numberSamples++;
		}

		// Stop timing training phase.
		endTime = System.nanoTime();
		nanoseconds = endTime - startTime;
		seconds = (double) nanoseconds / 1000000000.0;

		// Calculate training accuracy.
		accuracy = 100.0 * (double) numberSamplesCorrect / (double) numberSamples; 

		/*
		 * Print out some training details...
		 */

		print("Test data instances: " + numberSamples);
		print("Tree tested on " + numberSamples + " instances & has " + accuracy + "% accuracy.");
		print("Testing tree completed in "+nanoseconds+" (ns) or "+seconds+" (s)");

		// Accuracy of the tree when finished, useful for you to compare
		// against if you make changes.
		print("Note original training accuracy  = 98.01271860095389 %");
		print("Note original testing  accuracy  = 98.69243888573052 %");
	}

	//*****************************************
	//*****************************************
	//            Utility Methods
	//*****************************************
	//*****************************************

	/**
	 * Simple wrapper for standard out.
	 * @param msg the string message to print out.
	 */
	private static void print(String msg) { System.out.println(msg); }
}
