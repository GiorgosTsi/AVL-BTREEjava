package mainMemoryStructures.giorgos.tsi;

public class AVLTest {

	public static void main(String[] args) {
		AVLTree tree = new AVLTree();
		
		//test simple rotations:
		System.out.println("TEST SIMPLE ROTATIONS:\n");
		/*****test simple left rotation: ********/
		System.out.println("Insert 1,2,3");
		tree.insert(1);
		tree.insert(2);
		tree.insert(3);//must rebalance itself!
		tree.printTree();
		System.out.println("Insert 4,5");
		tree.insert(4);
		tree.insert(5);//must rebalance itself!
		System.out.println();
		tree.printTree();
		System.out.println();
		System.out.println("Insert 6");
		tree.insert(6);
		tree.printTree();
		
		//test simple right rotation:
		System.out.println("New tree:\n");
		tree = new AVLTree();
		System.out.println("Insert 13,12,11");
		tree.insert(13);
		tree.insert(12);
		tree.insert(11);//must rebalance itself!
		tree.printTree();
		System.out.println();
		System.out.println("Insert 10,9");
		tree.insert(10);
		tree.insert(9);
		tree.printTree();
		
		//test double rotations:
		tree = new AVLTree();
		System.out.println("New tree:");
		//test left right rotation:
		System.out.println("Insert 13,12,10,8,7");
		tree.insert(13);
		tree.insert(12);
		tree.insert(10);
		tree.insert(8);
		tree.insert(7);
		tree.printTree();
		System.out.println("Insert 11! => Left Right rotation!");
		tree.insert(11);//must rebalance itself
		tree.printTree();
		
		//test right left rotation:
		tree = new AVLTree();
		System.out.println("New tree:");
		System.out.println("Insert 8,6,12,15,11");
		tree.insert(8);
		tree.insert(6);
		tree.insert(12);
		tree.insert(15);
		tree.insert(11);
		tree.printTree();
		System.out.println("insert 9! => Right Left rotation!");
		tree.insert(9);//must rebalance itself!
		tree.printTree();
		
		/////Test DELETIONS////////
		System.out.println("Test Deletions:");
		System.out.println("Delete 6,8");
		tree.delete(6);
		tree.delete(8);
		tree.printTree();
		tree.delete(9);//should be rebalanced!
		System.out.println("Delete 9:");
		tree.printTree();
		
		//rotations for insertion are the same for deletions.They work properly for insertion
		//so they'll be working as well in deletions.No need to check all the cases.
	}

}
