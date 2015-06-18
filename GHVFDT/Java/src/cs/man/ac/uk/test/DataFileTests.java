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
 * File name: 	DataFileTests.java
 * Package: cs.man.ac.uk.test
 * Created:	May 21st, 2015
 * Author:	Rob Lyon
 * 
 * Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
 * Web:		<http://www.scienceguyrob.com> or <http://www.cs.manchester.ac.uk> 
 *          or <http://www.jb.man.ac.uk>
 */
package cs.man.ac.uk.test;

import cs.man.ac.uk.data.ARFFFile;
import cs.man.ac.uk.data.CSVFile;

/**
 * Runs basic tests on the file reading capabilities of this code base.
 * 
 * @author Rob Lyon
 */
public class DataFileTests 
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

	//*****************************************
	//*****************************************
	//    Main method that runs the tests
	//*****************************************
	//*****************************************

	/**
	 * Tests the data reading capabilities of the code.
	 * @param args unused arguments.
	 */
	public static void main(String[] args)
	{
		/*
		 *  CSV File tests
		 *  
		 *  1. Test when file is missing an attribute
		 *  2. Test when attribute is non-numerical
		 *  3. Test when file contains comments.
		 *  4. Test when file contains no class attribute.
		 *  5. Test when class label is invalid (string or negative).
		 *  
		 *  Outcome of all tests should be useful information fo user, so that
		 *  the problem can be corrected.
		 */

		print("\n\n\t\tCSV TESTS\n\n");
		runCSVTests();

		print("\n\n\t\tARFF TESTS\n\n");
		runARFFTests();
	}

	//*****************************************
	//*****************************************
	//    			   TESTS
	//*****************************************
	//*****************************************

	/**
	 * Runs the test associated with CSV files.
	 */
	private static void runCSVTests()
	{
		/*
		 *  CSV File tests
		 *  
		 *  1. Test when file is missing an attribute
		 *  2. Test when attribute is non-numerical
		 *  3. Test when file contains comments.
		 *  4. Test when file contains no class attribute.
		 *  5. Test when class label is invalid (string or negative).
		 *  
		 *  Outcome of all tests should be useful information for the user, so that
		 *  the problem can be corrected.
		 */

		String ext = ".csv";

		// Test 1a missing attribute at start of file.
		print("\nTest 1a missing attribute at start of file.\n");
		print("\nExpected outcome: Error\n");
		String directory = "Test_1/";
		String file = "TestFile_1a";

		CSVFile f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());


		// Test 1b missing attribute in middle of file.
		print("\nTest 1b missing attribute in middle of file.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_1/";
		file = "TestFile_1b";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 1c missing attribute at end of file.
		print("\nTest 1c missing attribute at end of file.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_1/";
		file = "TestFile_1c";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 2 non-numerical attribute
		print("\nTest 2 non-numerical attribute.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_2/";
		file = "TestFile_2";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 3a comment in inappropriate place in file.
		print("\nTest 3a comment in inappropriate place in file.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_3/";
		file = "TestFile_3a";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 3b comment on end of first line of file.
		print("\nTest 3b comment on end of first line of file.\n");
		print("\nExpected outcome: Correctly loaded instances\n");
		directory = "Test_3/";
		file = "TestFile_3b";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 3c comment on end of last line of file.
		print("\nTest 3c comment on end of last line of file.\n");
		print("\nExpected outcome: Correctly loaded instances\n");
		directory = "Test_3/";
		file = "TestFile_3c";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 4a no class attribute first instance
		print("\nTest 4a no class attribute first instance.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_4/";
		file = "TestFile_4a";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 4b no class attribute second instance
		print("\nTest 4b no class attribute second instance.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_4/";
		file = "TestFile_4b";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 4c no class attribute last instance
		print("\nTest 4c no class attribute last instance.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_4/";
		file = "TestFile_4c";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 5 invalid class attribute
		print("\nTest Test 5 invalid class attribute.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_5/";
		file = "TestFile_5";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Finally
		// Test 6 valid file...
		print("\nTest Test 6 VALID file.\n");
		print("\nExpected outcome: Correctly loaded instances\n");
		directory = "Test_6/";
		file = "TestFile_6";

		f =new CSVFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());
	}

	/**
	 * Runs the test associated with ARFF files.
	 */
	private static void runARFFTests()
	{
		/*
		 *  ARFF File tests
		 *  
		 *  1. Test when file is missing an attribute
		 *  2. Test when attribute is non-numerical
		 *  3. Test when file contains comments.
		 *  4. Test when file contains no class attribute.
		 *  5. Test when class label is invalid (string or negative).
		 *  
		 *  Outcome of all tests should be useful information for the user, so that
		 *  the problem can be corrected.
		 */

		String ext = ".arff";

		// Test 1a missing attribute at start of file.
		print("\nTest 1a missing attribute at start of file.\n");
		print("\nExpected outcome: Error\n");
		String directory = "Test_1/";
		String file = "TestFile_1a";

		ARFFFile f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());


		// Test 1b missing attribute in middle of file.
		print("\nTest 1b missing attribute in middle of file.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_1/";
		file = "TestFile_1b";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 1c missing attribute at end of file.
		print("\nTest 1c missing attribute at end of file.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_1/";
		file = "TestFile_1c";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 2 non-numerical attribute
		print("\nTest 2 non-numerical attribute.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_2/";
		file = "TestFile_2";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 3a comment in inappropriate place in file.
		print("\nTest 3a comment in inappropriate place in file.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_3/";
		file = "TestFile_3a";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 3b comment on end of first line of file.
		print("\nTest 3b comment on end of first line of file.\n");
		print("\nExpected outcome: Correctly loaded instances\n");
		directory = "Test_3/";
		file = "TestFile_3b";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 3c comment on end of last line of file.
		print("\nTest 3c comment on end of last line of file.\n");
		print("\nExpected outcome: Correctly loaded instances\n");
		directory = "Test_3/";
		file = "TestFile_3c";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 4a no class attribute first instance
		print("\nTest 4a no class attribute first instance.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_4/";
		file = "TestFile_4a";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 4b no class attribute second instance
		print("\nTest 4b no class attribute second instance.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_4/";
		file = "TestFile_4b";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 4c no class attribute last instance
		print("\nTest 4c no class attribute last instance.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_4/";
		file = "TestFile_4c";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Test 5 invalid class attribute
		print("\nTest 5 invalid class attribute.\n");
		print("\nExpected outcome: Error\n");
		directory = "Test_5/";
		file = "TestFile_5";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());

		// Finally
		// Test 6 valid file...
		print("\nTest 6 VALID file.\n");
		print("\nExpected outcome: Correctly loaded instances\n");
		directory = "Test_6/";
		file = "TestFile_6";

		f =new ARFFFile(root + directory + file + ext ,inc,verbose);
		print(f.toString());
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
