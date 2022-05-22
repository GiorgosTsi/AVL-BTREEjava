package mainMemoryStructures.giorgos.tsi;

public class AVLNode {
	
	int data; // holds the key
	
	AVLNode parent; // pointer to the parent
	
	AVLNode left; // pointer to left child
	
	AVLNode right; // pointer to right child
	
	int bf; // balance factor of the node

	public AVLNode(int data) {
		this.data = data;
		this.parent = null;
		this.left = null;
		this.right = null;
		this.bf = 0;
	}
}
