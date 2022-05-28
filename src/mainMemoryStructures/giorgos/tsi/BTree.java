package mainMemoryStructures.giorgos.tsi;

import org.tuc.counter.MultiCounter;

/**
 * BTree implementation 
 * made by https://programmer.group/b-tree-java-implementation.html?fbclid=IwAR2qT5gYVtYSe8xrsJf0wi3Jvz0-yjprAnNSJgPwMWAxdi9TYsjFM3ZL_ak 
 * Some comments have been added by me.
 *  */

public class BTree {

	BTreeNode root;
    int MinDeg;

    // Constructor
    public BTree(int MinDeg){
        this.root = null;
        this.MinDeg = MinDeg; // min degree of the tree. Degree of tree is 2* MinDeg!
    }

    public void traverse(){
        if (root != null){
            root.traverse();
        }
    }

    /**
     * Method to find a key in the b tree.
     * If the key to find exists on the tree,we return
     * the node where it exists,else null.
     * Total operations(comparisons + assignments) made while searching stored in MultiCounter[0].
     * @param key to search
     * @return node that contains the key.
     *  */
    public BTreeNode search(int key){
    	MultiCounter.increaseCounter(1,2);// one comparison,one assignment made.
        return root == null ? null : root.search(key);//start searching from root until leaf.
    }

    public void insert(int key){

        if (root == null){

            root = new BTreeNode(MinDeg,true);
            root.keys[0] = key;
            root.num = 1;
        }
        else {
            // When the root node is full, the tree will grow high
            if (root.num == 2*MinDeg-1){
                BTreeNode s = new BTreeNode(MinDeg,false);
                // The old root node becomes a child of the new root node
                s.children[0] = root;
                // Separate the old root node and give a key to the new node
                s.splitChild(0,root);
                // The new root node has 2 child nodes. Move the old one over there
                int i = 0;
                if (s.keys[0]< key)
                    i++;
                s.children[i].insertNotFull(key);

                root = s;
            }
            else
                root.insertNotFull(key);
        }
    }

    public void remove(int key){
        if (root == null){
            System.out.println("The tree is empty");
            return;
        }

        root.remove(key);

        if (root.num == 0){ // If the root node has 0 keys
            // If it has a child, its first child is taken as the new root,
            // Otherwise, set the root node to null
            if (root.isLeaf)
                root = null;
            else
                root = root.children[0];
        }
    }
}
