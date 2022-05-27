package testing;

import java.io.IOException;

import org.tuc.counter.MultiCounter;

import mainMemoryStructures.giorgos.tsi.AVLTree;

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
	
	
	public void doTest() {
		
		/*******************  AVL TESTS ********************/
		System.out.println("*".repeat(40) +"TESTS FOR AVL TREE" + "*".repeat(40));
		this.doAVLTreeTest();
		
		
	}
	
	/**
	 * private method to do the avl tree measurements.
	 *  */
	private void doAVLTreeTest() {
		
		AVLTree avlTree = new AVLTree();
		double totalOperationsDone =0.0;
		for(int numberOfTest=0; numberOfTest < NUMBER_OF_ELEMENTS_PER_TEST.length ; numberOfTest++) {
			
			//System.out.println("root: " + avlTree.getRoot());
			int numberOfElements =NUMBER_OF_ELEMENTS_PER_TEST[numberOfTest];
			System.out.println("\t**AVLTree test for " + numberOfElements + " elements in the structure:\n");
			
			MultiCounter.resetCounter(2);//reset the counter.
			
			/*Insert as many elements as the array of number of elements indicates */
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
}
