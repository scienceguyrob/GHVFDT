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
 * File name: 	GHVFDTTester.java
 * Package: cs.man.ac.uk.test
 * Created:	March 25th, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.test;

import cs.man.ac.uk.classifiers.stream.StreamAlgorithmTester;
import cs.man.ac.uk.common.Common;
import cs.man.ac.uk.sample.Sampler;
import cs.man.ac.uk.stats.ClassifierStatistics;
import moa.classifiers.trees.GHVFDT;
import moa.classifiers.trees.HDVFDT;
import moa.classifiers.trees.HoeffdingTree;

/**
 * Class used to test the GHVFDT algorithm.
 * 
 * @author Rob Lyon
 */
public class GHVFDTTester 
{

	//*****************************************
	//*****************************************
	//              Variables
	//*****************************************
	//*****************************************

	/**
	 * Path to the training data to use.
	 */
	@SuppressWarnings("unused")
	private static String trainData = "";

	/**
	 * Path to the test data to use.
	 */
	@SuppressWarnings("unused")
	private static String testData  = "";

	/**
	 * The root data file directory.
	 */
	private static String root = "/home/rob/git/scienceguyrob/GHVFDT/GHVFDT/MOA/data/";
	
	//*****************************************
	//*****************************************
	//              Main Method
	//*****************************************
	//*****************************************

	public static void main(String[] args) 
	{
		// Perform some tests with STUFFED.
	    
		String data       = root + "SPINN.arff"; // Only positives labelled.
		String trainData  = root + "temp/train.arff";
		String testData   = root + "temp/test.arff";
		String log        = root + "log.txt";

		// Check files accessible:
		if(!Common.fileExist(data))
		{
			System.out.println("Could not find input data file: "+ data);
			System.out.println("Exiting...");
			System.exit(0);
		}

		// Clean up before testing.
		Common.fileDelete(log);
		Common.fileDelete(trainData);
		Common.fileDelete(testData);

		Sampler s = new Sampler(log, false);
		s.load(data, -1);

		// 2OO + training samples
		// 200 - training samples
		// training set balance 1:1
		// test set balance 1:100
		// test set labelling ratio 50 %.
		Object[] output = s.sampleToARFF(trainData, testData, 200, 200, 1.0, 0.0001, 1.0);

		// Process outcome of sampling operation.
		boolean result = (Boolean) output[0];
		String outcome = (String) output[1];

		// If sampling fails...
		if(!result)
		{
			System.out.println("Error in sampling:");
			System.out.println(outcome);
			System.exit(0);
		}

		// Test one
		StreamAlgorithmTester sat = new StreamAlgorithmTester(log,"GHVFDT",true,new GHVFDT());

		sat.train(trainData);

		int[][] confusionMatrix = sat.testStream(testData, log,s.getPathToPositiveMetaData(),s.getPathToPositiveMetaData());

		// Collect stats
		ClassifierStatistics stats_1 = new ClassifierStatistics();
		stats_1.setConfusionMatrix(confusionMatrix);
		stats_1.calculate();

		System.out.println(stats_1.toString());

		// Test Two
		sat = new StreamAlgorithmTester(log,"HDVFDT",true,new HDVFDT());

		sat.train(trainData);

		confusionMatrix = sat.testStream(testData, log,s.getPathToPositiveMetaData(),s.getPathToPositiveMetaData());

		// Collect stats
		ClassifierStatistics stats_2 = new ClassifierStatistics();
		stats_2.setConfusionMatrix(confusionMatrix);
		stats_2.calculate();

		System.out.println(stats_2.toString());

		// Test Three
		sat = new StreamAlgorithmTester(log,"Hoeffding Tree",true,new HoeffdingTree());

		sat.train(trainData);

		confusionMatrix = sat.testStream(testData, log,s.getPathToPositiveMetaData(),s.getPathToPositiveMetaData());

		// Collect stats
		ClassifierStatistics stats_3 = new ClassifierStatistics();
		stats_3.setConfusionMatrix(confusionMatrix);
		stats_3.calculate();

		System.out.println(stats_3.toString());
	}											
}
