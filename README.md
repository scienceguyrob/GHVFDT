******************************************************************************************

# GHVFDT
An imbalanced data stream classifier, which uses the Hoeffding bound and Hellinger
distances, to improve minority class recall.

Gaussian Hellinger Very Fast Decision Tree (GH-VFDT)

Author: Rob Lyon, School of Computer Science & Jodrell Bank Centre for Astrophysics,
		University of Manchester, Kilburn Building, Oxford Road, Manchester M13 9PL.

Contact:	rob@scienceguyrob.com or robert.lyon@postgrad.manchester.ac.uk
Web:		http://www.scienceguyrob.com or http://www.cs.manchester.ac.uk
			or alternatively http://www.jb.man.ac.uk
******************************************************************************************

1. Overview 

	The GH-VFDT utilises a decision tree split criterion, designed to improve minority
	class recall rates on imbalanced data streams, i.e. those streams where the class
	distribution is worse than 1:100. This implementation is built upon the Hoeffding
	Tree provided in MOA, thus a great deal of credit goes to the MOA team for their
	initial implementation and library. We greatly acknowledge their efforts.
	
	For more details of the algorithm (bibtex reference below) see,
	
	R. J. Lyon, J. M. Brooke, J. D. Knowles, B. W. Stappers. Hellinger Distance Trees
	for Imbalanced Streams In 22nd International Conference on Pattern Recognition,
	pages 1969-1974, Stockholm, Sweden, 2014. IEEE.

2. Algorithms

	There are two version of the algorithm provided. The first version is designed to work with the
	MOA stream testing framework. It can be found in the MOA sub-directory of the distribution. The 
	second version is a complete Java version that requires no external libraries, designed to operate
	outside of MOA. Use whichever version suits your needs.
	
3. Use
	
	- MOA VERSION - 
	The algorithm is designed to work directly with the MOA stream test framework. To use it
	with MOA, simply download the MOA files, and navigate to the MOA download directory. Make
	sure this directory contains the following files:
	
	i)   moa.jar
	ii)  sizeofag.jar
	iii) weka.jar - optional, include if you intend to use WEKA classifiers.
	
	Copy the GHVFDT.jar file to the MOA download directory, then execute the following command
	at the terminal/command line:
	
	java -cp GHVFDT.jar:moa.jar -javaagent:sizeofag.jar moa.gui.GUI
	
	or if also using WEKA classifiers,
	
	java -cp GHVFDT.jar:moa.jar:weka.jar -javaagent:sizeofag.jar moa.gui.GUI
	
	When MOA starts, the GH-VFDT will be included in the list of classifiers available for testing.
	
	Alternatively this algorithm can be used outside of MOA. Please see our Stuffed framework for a simple
	example of how this is possible. Stuffed is a wrapper for WEKA and MOA classifiers that enables evaluation
	on large, unlabelled data sets. Please see:
	
	https://github.com/scienceguyrob/Stuffed
	
	- Java VERSION - 
	
	Find the ClassifierTest.java file in the Java directory. This is a complete example showing how to run and
	test the algorithm.
	
4. Citing our work

	Please use the following citation if you make use of this algorithm:
	
	@inproceedings{Lyon:2014:jk,
	author    = {{Lyon}, R.~J. and {Knowles}, J.~D. and {Brooke}, J.~M. and {Stappers}, B.~W.},
	title     = {{Hellinger Distance Trees for Imbalanced Streams}},
	booktitle = {22nd IEEE International Conference on Pattern Recognition}, 
	series    = {ICPR '14},
	year      = {2014},
	month     = {August},
	pages     = {1969-1974},
	location  = {Stockholm, Sweden},
	publisher = {IEEE}
	}
	
5. Acknowledgements

	This work was supported by grant EP/I028099/1 for the University of Manchester Centre for
	Doctoral Training in Computer Science, from the UK Engineering and Physical Sciences Research
	Council (EPSRC).
