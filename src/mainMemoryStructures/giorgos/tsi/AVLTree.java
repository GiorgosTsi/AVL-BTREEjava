package mainMemoryStructures.giorgos.tsi;

import org.tuc.counter.MultiCounter;

/**
 * Implementation made by eugenp
 * https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java?fbclid=IwAR2yD4DWlPeEOspfkGEyw-sggUIreK8wIzYAGP-iG0C1eLob5zGhhAG_Yc4
 * A print method was implemented by me to test the functionality.
 *  */
public class AVLTree {

    public class Node {
        int key;
        int height;
        Node left;
        Node right;

        Node(int key) {
            this.key = key;
        }
    }

    private Node root;

    
    public Node find(int key) {
    	//total number of assignments,comparisons made for finding a node,stored in MultiCounter[0]!
    	
        Node current = root;
        MultiCounter.increaseCounter(1);//one assignment has been made.
        
        while (MultiCounter.increaseCounter(1) && current != null) { // one comparison made!
            if (MultiCounter.increaseCounter(1) && current.key == key) // one comparison made!
               break;
            
            current = current.key < key ? current.right : current.left;
            MultiCounter.increaseCounter(1, 2);//two operations:one comparison , one assignment
        }
        return current;
    }
    
    public void printTree() {
    	this.printHelp(root, 0);
    }
    /**
     * Method made by me , used to print the bst on 'inorder' traversal.
     * Uses recursion.
     * @param root of the tree/subtree and the number of spaces
     *             to print on the line in which the element will
     *             be printed.
     * */
    private void printHelp(Node root,int spaces){

        if(root == null)
            return;

        /*Print Right subtree */
        printHelp(root.right,spaces + 10);

        /*print the root node */
        System.out.print("\n");
        for(int i=0; i<spaces; i++)
            System.out.print(" ");

        System.out.print(root.key);
        System.out.print("\n");

        /*Print Left subtree */
        printHelp(root.left,spaces + 10);

    }

    public void insert(int key) {
    	//total operations(comparisons + assignments) made for inserting a key,stored in MultiCounter[1]
    	
        root = insert(root, key);
        MultiCounter.increaseCounter(2);//one assignment made.
    }

    public void delete(int key) {
    	//total operations for deletion,stored in MultiCounter[1].
        root = delete(root, key);
    }

    public Node getRoot() {
        return root;
    }

    public int height() {
        return root == null ? -1 : root.height;
    }

    private Node insert(Node node, int key) {
    	//total number of operations,stored in MultiCounter 2!
    	
        if (MultiCounter.increaseCounter(2) && node == null) {
            return new Node(key);
        } 
        else if (MultiCounter.increaseCounter(2) && node.key > key) {
            node.left = insert(node.left, key);
            MultiCounter.increaseCounter(2);//one assignment made.
        }
        else if (MultiCounter.increaseCounter(2) && node.key < key) {
            node.right = insert(node.right, key);
            MultiCounter.increaseCounter(2);//one assignment made.
        }
        else {
        	throw new RuntimeException("Duplicate keys are not allowed!");
        }
        return rebalance(node);
    }

    private Node delete(Node node, int key) {
    	//total operations for deletion,stored in MultiCounter[1].
    	
        if (MultiCounter.increaseCounter(2) && node == null) {
            return node;
        }
        else if (MultiCounter.increaseCounter(2) && node.key > key) {
            node.left = delete(node.left, key);
            MultiCounter.increaseCounter(2);//one assignment made. 
        }
        else if (MultiCounter.increaseCounter(2) && node.key < key) {
            node.right = delete(node.right, key);
            MultiCounter.increaseCounter(2);//one assignment made.
        }
        else {
            if ( (MultiCounter.increaseCounter(2) && node.left == null ) ||
            		(MultiCounter.increaseCounter(2) && node.right == null )) {
            	
                node = (node.left == null) ? node.right : node.left;
                MultiCounter.increaseCounter(2,2);//2 operations made.One comparison,One assignment.
                
            }
            else {
                Node mostLeftChild = mostLeftChild(node.right);//operations to find leftmost child also stored in counter 2.
                node.key = mostLeftChild.key;
                node.right = delete(node.right, node.key);
                MultiCounter.increaseCounter(2,3);//three assignments made.
            }
        }
        if (MultiCounter.increaseCounter(2) && node != null) {
            node = rebalance(node);
            MultiCounter.increaseCounter(2);//one assignment made.
        }
        return node;
    }

    private Node mostLeftChild(Node node) {
        Node current = node;
        MultiCounter.increaseCounter(2);//one assignment made. 
        /* loop down to find the leftmost leaf */
        while (MultiCounter.increaseCounter(2) && current.left != null) {
            current = current.left;
            MultiCounter.increaseCounter(2);//one assignment made.
        }
        return current;
    }

    private Node rebalance(Node z) {
        updateHeight(z);
        int balance = getBalance(z);
        
        if (MultiCounter.increaseCounter(2) && balance > 1) {
            if (MultiCounter.increaseCounter(2) && height(z.right.right) > height(z.right.left)) {
                z = rotateLeft(z);
                MultiCounter.increaseCounter(2);//one assignment made.
            } else {
                z.right = rotateRight(z.right);
                z = rotateLeft(z);
                MultiCounter.increaseCounter(2,2);//2 assignments made!
            }
        } else if (MultiCounter.increaseCounter(2) && balance < -1) {
            if (MultiCounter.increaseCounter(2) && height(z.left.left) > height(z.left.right)) {
                z = rotateRight(z);
                MultiCounter.increaseCounter(2);//one assignment made.
            } else {
                z.left = rotateLeft(z.left);
                z = rotateRight(z);
                MultiCounter.increaseCounter(2,2);//operations for updateHeight are counted inside of methods.
            }
        }
        return z;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        MultiCounter.increaseCounter(2,4);//4 assignments made.
        updateHeight(y);
        updateHeight(x);
        //operations for updateHeight are counted inside of methods.
        return x;
    }

    private Node rotateLeft(Node y) {
        Node x = y.right;
        Node z = x.left;
        x.left = y;
        y.right = z;
        MultiCounter.increaseCounter(2,4);//4 assignments made.
        updateHeight(y);
        updateHeight(x);
        //operations for updateHeight are counted inside of methods.
        return x;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
        MultiCounter.increaseCounter(2);//one assignment made.
    }

    private int height(Node n) {
    	MultiCounter.increaseCounter(2,2);//2 operations will be done.One comparison,one assignment!
        return n == null ? -1 : n.height;
    }

    public int getBalance(Node n) {
    	MultiCounter.increaseCounter(2);//one comparison made!
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }
}
