package testing;

import java.io.IOException;

import org.tuc.counter.MultiCounter;

import ds.bplus.bptree.BPlusConfiguration;
import ds.bplus.bptree.BPlusTree;
import ds.bplus.bptree.BPlusTreePerformanceCounter;
import ds.bplus.util.InvalidBTreeStateException;
import mainMemoryStructures.giorgos.tsi.AVLTree;
import mainMemoryStructures.giorgos.tsi.BTree;

/**
 * @author giorgos tsi
 * Class that contains the methods
 * to do the measurements.
 *  */
public class Tester {

	/*Elements to make the tests with: */
	private int[] totalElementsToInsert;      // 10^6 elements to insert.
	
	private int[] elementsToDelete;           // 100 elements to delete
	
	private int[] elementsToSearch;           // 100 elements to search
	
	private int[] oneHundredElementsToInsert; // 100 extra elements to insert 
	
	static int[] NUMBER_OF_ELEMENTS_PER_TEST = { 5000, 10000,50000, 100000, 200000, 300000, 400000, 500000,
			                                   600000, 700000, 800000, 900000, 1000000};
	
	/**Constructor:
	 * Load the elements from the files
	 * and store them in the above arrays.
	 * @throws IOException 
	 *  */
	
	public Tester() throws IOException {
		
		System.out.println("Loading Elements from the files...");
		
		this.totalElementsToInsert = Load.totalElementsToInsert();
		
		this.elementsToDelete = Load.elementsToDelete();
		
		this.elementsToSearch = Load.elementsToSearch();
		
		this.oneHundredElementsToInsert = Load.oneHundredElementsToInsert();
		
		System.out.println("Starting process: ");
	}
	
	
	public void doTest() throws IOException, InvalidBTreeStateException {
		
		/*******************  AVL TESTS ********************/
		System.out.println("*".repeat(40) +"TESTS FOR AVL TREE" + "*".repeat(40));
		this.doAVLTreeTest();
		
		/*******************  BTree TESTS ********************/
		System.out.println("*".repeat(40) +"TESTS FOR B TREE (DEGREE = 4)" + "*".repeat(40));
		this.doBTreeTest(4);// 4 is the degree of the tree.
		
		System.out.println("*".repeat(40) +"TESTS FOR B TREE (DEGREE = 64)" + "*".repeat(40));
		this.doBTreeTest(64);// 64 is the degree of the tree.
		
		/*******************  BPTree TESTS ********************/
		System.out.println("*".repeat(40) +"TESTS FOR BP TREE WITH PAGE SIZE 128" + "*".repeat(40));
		this.doBPlusTreeTest(128);
		
		System.out.println("*".repeat(40) +"TESTS FOR BP TREE WITH PAGE SIZE 256" + "*".repeat(40));
		this.doBPlusTreeTest(256);
	}
	
	/**
	 * private method to do the avl tree measurements.
	 *  */
	private void doAVLTreeTest() {
		
		/* reset the counters: */
		MultiCounter.resetCounter(1);
		MultiCounter.resetCounter(2);
		
		AVLTree avlTree = new AVLTree();
		double totalOperationsDone =0.0;
		for(int numberOfTest=0; numberOfTest < NUMBER_OF_ELEMENTS_PER_TEST.length ; numberOfTest++) {
			
			//System.out.println("root: " + avlTree.getRoot());
			int numberOfElements =NUMBER_OF_ELEMENTS_PER_TEST[numberOfTest];
			System.out.println("\n\t**AVLTree test for " + numberOfElements + " elements in the structure:");
			
			MultiCounter.resetCounter(2);//reset the counter.
			
			/*Insert as many elements as the array NUMBER_OF_ELEMENTS_PER_TEST indicates */
			for(int i=0; i<NUMBER_OF_ELEMENTS_PER_TEST[numberOfTest] ; i++)
				avlTree.insert(this.totalElementsToInsert[i]);
			
			/*** Start tests ***/
			
			/********** 1) Insert 100 extra elements: **********/
			
			//insert 100 extra elements:
			for(int i=0; i < 100 ; i++) {
				
				MultiCounter.resetCounter(2);//reset the counter.
				avlTree.insert(this.oneHundredElementsToInsert[i]);
				totalOperationsDone += MultiCounter.getCount(2);//operations for inserting a key,stored in counter 2.
				
			}
			System.out.println("\t\t Mean operations for inserting 100 elements in avl tree with " + numberOfElements + " elements: " + totalOperationsDone/100);
			
			/********** 2) Delete 100 elements: **********/
			
			totalOperationsDone = 0.0;//reset the variable,so we test the deletions.
			
			//delete 100 elements:
			for(int i=0; i < 100 ; i++) {
				
				MultiCounter.resetCounter(2);//reset the counter.
				avlTree.delete(this.elementsToDelete[i]);
				totalOperationsDone += MultiCounter.getCount(2);//operations for deleting a key,stored in counter 2.
				
			}
			System.out.println("\t\t Mean operations for deleting 100 elements in avl tree with " + numberOfElements + " elements: " + totalOperationsDone/100);
			
			/********** 3) Search 100 elements: **********/
			
			totalOperationsDone = 0.0;//reset the variable,so we test the search method.
			
			//search 100 elements:
			for(int i=0; i < 100 ; i++) {
				
				MultiCounter.resetCounter(1);//reset the counter.
				avlTree.find(this.elementsToSearch[i]);
				totalOperationsDone += MultiCounter.getCount(1);//operations for searching a key,stored in counter 1.
				
			}
			System.out.println("\t\t Mean operations for searching 100 elements in avl tree with " + numberOfElements + " elements: " + totalOperationsDone/100);
			
			//end of this number of elements test,make a new empty structure for the next test:
			avlTree = new AVLTree();
		}
	}
	
	
	/**
	 * Method to do the b tree measurements.
	 * @param the degree of the b tree. 
	 *  */
	private void doBTreeTest(int degree) {
		
		/* reset the counters: */
		MultiCounter.resetCounter(1);
		MultiCounter.resetCounter(2);
		
		BTree bTree = new BTree(degree/2);// constructor takes min degree as parameter.
		double totalOperationsDone =0.0;
		for(int numberOfTest=0; numberOfTest < NUMBER_OF_ELEMENTS_PER_TEST.length ; numberOfTest++) {
			
			int numberOfElements =NUMBER_OF_ELEMENTS_PER_TEST[numberOfTest];
			System.out.println("\n\t**BTree test for " + numberOfElements + " elements in the structure:");
			
			MultiCounter.resetCounter(2);//reset the counter.
			
			/*Insert as many elements as the array NUMBER_OF_ELEMENTS_PER_TEST indicates */
			for(int i=0; i<NUMBER_OF_ELEMENTS_PER_TEST[numberOfTest] ; i++)
				bTree.insert(this.totalElementsToInsert[i]);
			
			/*** Start tests ***/
			
			/********** 1) Insert 100 extra elements: **********/
			
			//insert 100 extra elements:
			for(int i=0; i < 100 ; i++) {
				
				MultiCounter.resetCounter(2);//reset the counter.
				bTree.insert(this.oneHundredElementsToInsert[i]);
				totalOperationsDone += MultiCounter.getCount(2);//operations for inserting a key,stored in counter 2.
				
			}
			System.out.println("\t\t Mean operations for inserting 100 elements in b tree with " + numberOfElements + " elements: " + totalOperationsDone/100);
			
			/********** 2) Delete 100 elements: **********/
			
			totalOperationsDone = 0.0;//reset the variable,so we test the deletions.
			
			//delete 100 elements:
			for(int i=0; i < 100 ; i++) {
			
				MultiCounter.resetCounter(2);//reset the counter.
				bTree.remove(this.elementsToDelete[i]);
				totalOperationsDone += MultiCounter.getCount(2);//operations for deleting a key,stored in counter 2.
				
			}
			System.out.println("\t\t Mean operations for deleting 100 elements in b tree with " + numberOfElements + " elements: " + totalOperationsDone/100);
			
			
			
			/********** 3) Search 100 elements: **********/
			
			totalOperationsDone = 0.0;//reset the variable,so we test the search method.
			
			//search 100 elements:
			for(int i=0; i < 100 ; i++) {
				
				MultiCounter.resetCounter(1);//reset the counter.
				bTree.search(this.elementsToSearch[i]);
				totalOperationsDone += MultiCounter.getCount(1);//operations for searching a key,stored in counter 1.
				
			}
			System.out.println("\t\t Mean operations for searching 100 elements in b tree with " + numberOfElements + " elements: " + totalOperationsDone/100);
			
			//end of this number of elements test,make a new empty structure for the next test:
			bTree = new BTree(degree/2);
		}
	}
	
	private void doBPlusTreeTest(int dataPageSize) throws IOException, InvalidBTreeStateException {
		
		BPlusConfiguration conf = new BPlusConfiguration(dataPageSize, 8, 4);// key=8 bytes, info = 4 bytes.
		BPlusTreePerformanceCounter counter = new BPlusTreePerformanceCounter(true);
		BPlusTree bp = new BPlusTree(conf, counter) ;
		
		double totalDiskAccessesDone =0.0;
		for(int numberOfTest=0; numberOfTest < NUMBER_OF_ELEMENTS_PER_TEST.length  ; numberOfTest++) {
			
			int numberOfElements =NUMBER_OF_ELEMENTS_PER_TEST[numberOfTest];
			System.out.println("\n\t**BPTree test for " + numberOfElements + " elements in the structure:");
			
			/*Insert as many elements as the array NUMBER_OF_ELEMENTS_PER_TEST indicates */
			for(int i=0; i<NUMBER_OF_ELEMENTS_PER_TEST[numberOfTest] ; i++)
				bp.insertKey(this.totalElementsToInsert[i],"",true);
			
			/*** Start tests ***/
			
			/********** 1) Insert 100 extra elements: **********/
			
			//insert 100 extra elements:
			for(int i=0; i < 100 ; i++) {
				
				counter.resetAllMetrics();
				//MultiCounter.resetCounter(2);//reset the counter.
				bp.insertKey(this.oneHundredElementsToInsert[i],"",true);
				totalDiskAccessesDone += counter.getPageReads() + counter.getPageWrites();
				
			}
			System.out.println("\t\t Mean number of disk accesses for inserting 100 elements in b plus tree with " + numberOfElements + " elements: " + totalDiskAccessesDone/100);
			
			/********** 2) Delete 100 elements: **********/
			
			totalDiskAccessesDone = 0.0;//reset the variable,so we test the deletions.
			
			//delete 100 elements:
			for(int i=0; i < 100 ; i++) {
			
				counter.resetAllMetrics();
				bp.deleteKey(this.elementsToDelete[i],true);
				totalDiskAccessesDone += counter.getPageReads() + counter.getPageWrites();
				
			}
			System.out.println("\t\t Mean number of disk accesses for deleting 100 elements in b plus tree with " + numberOfElements + " elements: " + totalDiskAccessesDone/100);
			
			
			
			/********** 3) Search 100 elements: **********/
			
			totalDiskAccessesDone = 0.0;//reset the variable,so we test the search method.
			
			//search 100 elements:
			for(int i=0; i < 100 ; i++) {
				
				counter.resetAllMetrics();
				bp.searchKey(this.elementsToSearch[i],true);
				totalDiskAccessesDone += counter.getPageReads() + counter.getPageWrites();
				
			}
			System.out.println("\t\t Mean number of disk accesses for searching 100 elements in b plus tree with " + numberOfElements + " elements: " + totalDiskAccessesDone/100);
			
			//end of this number of elements test,make a new empty structure for the next test:
			bp = new BPlusTree(conf, counter); 
		}
		
	}
	
}
