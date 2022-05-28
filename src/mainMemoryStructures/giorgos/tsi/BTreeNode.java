package mainMemoryStructures.giorgos.tsi;

import org.tuc.counter.MultiCounter;

/**
 * BTree Node implementation 
 * made by https://programmer.group/b-tree-java-implementation.html?fbclid=IwAR2qT5gYVtYSe8xrsJf0wi3Jvz0-yjprAnNSJgPwMWAxdi9TYsjFM3ZL_ak 
 * Some comments have been added by me, and also fixed some bugs on search methods!
 *  */
public class BTreeNode {

	int[] keys; // keys of nodes
    int MinDeg; // Minimum degree of B-tree node.Degree of the b tree is 2*MinDeg!
    BTreeNode[] children; // Children pointers.
    int num; // #keys stored in the node
    boolean isLeaf; // True when leaf node

    // Constructor
    public BTreeNode(int deg,boolean isLeaf){

        this.MinDeg = deg;
        this.isLeaf = isLeaf;
        this.keys = new int[2*this.MinDeg-1]; // Node has 2*MinDeg-1 keys at most
        this.children = new BTreeNode[2*this.MinDeg];//node has 2*MinDeg children pointers.
        this.num = 0;
    }

    /** Find the first location index equal to or greater than key.
     * This method is used in remove or insert btree node methods.
     * We want to know where is the next greater key so we can traverse the tree
     * if the key we search does not exist in the current node.
     * @param key to search
     * @return the index of the key(or the 1st greater key) or num if the key does not exist.
     */
    public int findKey(int key){

        int idx = 0;
        MultiCounter.increaseCounter(2); // one assignment made.
        // The conditions for exiting the loop are: 1.idx == num, i.e. scan all of them once
        // 2. IDX < num, i.e. key found or greater than key
        while (MultiCounter.increaseCounter(2) && idx < num && 
        		MultiCounter.increaseCounter(2) && keys[idx] < key) {
            ++idx;
            MultiCounter.increaseCounter(2);// 1 assignment for ++idx; 
        }
        return idx;
    }


    public void remove(int key){

        int idx = findKey(key);//find the index of the key or the 1st greater key. 
        if (MultiCounter.increaseCounter(2) && idx < num && 
        		MultiCounter.increaseCounter(2) && keys[idx] == key){ // key found on the current node.
            if (MultiCounter.increaseCounter(2) && isLeaf) // key in leaf node!
                removeFromLeaf(idx);
            else // key is not in the leaf node
                removeFromNonLeaf(idx);
        }
        else{//key does not exist in the current node.Either exists in the idx's child or does not exist.
            if (MultiCounter.increaseCounter(2) && isLeaf){ // If the node is a leaf node, then the key does not exist in the B tree
                //System.out.printf("The key %d is does not exist in the tree\n",key);
                return;
            }

            // Otherwise, the key to be deleted exists in the subtree with the node as the root!

            // This flag indicates whether the key exists in the subtree whose root is the last child of the node
            // When idx is equal to num, the whole node is compared, and flag is true
            boolean flag = idx == num; 
            MultiCounter.increaseCounter(2); // one assignment on the flag.
            
            if (MultiCounter.increaseCounter(2) && children[idx].num < MinDeg) // When the child node of the node is not full, fill it first
                fill(idx);


            //If the last child node has been merged, it must have been merged with the previous child node, so we recurse on the (idx-1) child node.
            // Otherwise, we recurse to the (idx) child node, which now has at least the keys of the minimum degree
            if (MultiCounter.increaseCounter(2) && flag && idx > num)
                children[idx-1].remove(key);
            else
                children[idx].remove(key);
        }
    }

    public void removeFromLeaf(int idx){

        // Shift from idx
        for (int i = idx +1;MultiCounter.increaseCounter(2) && i < num;++i) {
            keys[i-1] = keys[i];
            MultiCounter.increaseCounter(2,2); // 1 assignment for i++, and one more for the other.
        }
        num --;
        MultiCounter.increaseCounter(2);
    }

    public void removeFromNonLeaf(int idx){

        int key = keys[idx];
        MultiCounter.increaseCounter(2);
        // If the subtree before key (children[idx]) has at least t keys
        // Then find the precursor 'pred' of key in the subtree with children[idx] as the root
        // Replace key with 'pred', recursively delete pred in children[idx]
        if (MultiCounter.increaseCounter(2) && children[idx].num >= MinDeg){
            int pred = getPred(idx);
            keys[idx] = pred;
            children[idx].remove(pred);
            MultiCounter.increaseCounter(2,2);//2 assignments.
        }
        // If children[idx] has fewer keys than MinDeg, check children[idx+1]
        // If children[idx+1] has at least MinDeg keys, in the subtree whose root is children[idx+1]
        // Find the key's successor 'succ' and recursively delete succ in children[idx+1]
        else if (MultiCounter.increaseCounter(2) && children[idx+1].num >= MinDeg){
            int succ = getSucc(idx);
            keys[idx] = succ;
            children[idx+1].remove(succ);
            MultiCounter.increaseCounter(2,2); // 2 assignments.
        }
        else{
            // If the number of keys of children[idx] and children[idx+1] is less than MinDeg
            // Then key and children[idx+1] are combined into children[idx]
            // Now children[idx] contains the 2t-1 key
            // Release children[idx+1], recursively delete the key in children[idx]
            merge(idx);
            children[idx].remove(key);
        }
    }

    public int getPred(int idx){ // The predecessor node is the node that always finds the rightmost node from the left subtree

        // Move to the rightmost node until you reach the leaf node
        BTreeNode cur = children[idx];
        MultiCounter.increaseCounter(2);
        
        while ( MultiCounter.increaseCounter(2) && !cur.isLeaf) {
            cur = cur.children[cur.num];
            MultiCounter.increaseCounter(2);
        }
        return cur.keys[cur.num-1];
    }

    public int getSucc(int idx){ // Subsequent nodes are found from the right subtree all the way to the left

        // Continue to move the leftmost node from children[idx+1] until it reaches the leaf node
        BTreeNode cur = children[idx+1];
        MultiCounter.increaseCounter(2);
        
        while ( MultiCounter.increaseCounter(2) &&!cur.isLeaf) {
            cur = cur.children[0];
            MultiCounter.increaseCounter(2);
        }
        return cur.keys[0];
    }

    // Fill children[idx] with less than MinDeg keys
    public void fill(int idx){

        // If the previous child node has multiple MinDeg-1 keys, borrow from them
        if (MultiCounter.increaseCounter(2) && idx != 0 && 
        		MultiCounter.increaseCounter(2) && children[idx-1].num >= MinDeg)
            borrowFromPrev(idx);
        // The latter sub node has multiple MinDeg-1 keys, from which to borrow
        else if (MultiCounter.increaseCounter(2) && idx != num && 
        		MultiCounter.increaseCounter(2) && children[idx+1].num >= MinDeg)
            borrowFromNext(idx);
        else{
            // Merge children[idx] and its brothers
            // If children[idx] is the last child node
            // Then merge it with the previous child node or merge it with its next sibling
            if (MultiCounter.increaseCounter(2) && idx != num)
                merge(idx);
            else
                merge(idx-1);
        }
    }

    // Borrow a key from children[idx-1] and insert it into children[idx]
    public void borrowFromPrev(int idx){

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx-1];

        MultiCounter.increaseCounter(2,2); // 2 assignments made.
        // The last key from children[idx-1] overflows to the parent node
        // The key[idx-1] underflow from the parent node is inserted as the first key in children[idx]
        // Therefore, sibling decreases by one and children increases by one
        for (int i = child.num-1; MultiCounter.increaseCounter(2) &&  i >= 0; --i) {// children[idx] move forward
            child.keys[i+1] = child.keys[i];
            MultiCounter.increaseCounter(2,2); // 2 assignments made.1 for --i and one for the other.
        }

        if ( MultiCounter.increaseCounter(2) && !child.isLeaf){ // Move children[idx] forward when they are not leaf nodes
            for (int i = child.num; MultiCounter.increaseCounter(2) && i >= 0; --i) {
                child.children[i+1] = child.children[i];
                MultiCounter.increaseCounter(2,2); // 2 assignments made.1 for --i and one for the other.
            }
        }

        // Set the first key of the child node to the keys of the current node [idx-1]
        child.keys[0] = keys[idx-1];
        MultiCounter.increaseCounter(2); // 1 assignment.
        if (MultiCounter.increaseCounter(2) && !child.isLeaf) {// Take the last child of sibling as the first child of children[idx]
            child.children[0] = sibling.children[sibling.num];
            MultiCounter.increaseCounter(2);
        }

        // Move the last key of sibling up to the last key of the current node
        keys[idx-1] = sibling.keys[sibling.num-1];
        child.num += 1;
        sibling.num -= 1;
        MultiCounter.increaseCounter(2,3); // 3 assignments made. 
    }

    // Symmetric with borowfromprev
    public void borrowFromNext(int idx){

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx+1];

        child.keys[child.num] = keys[idx];

        MultiCounter.increaseCounter(2,3); // 3 assignments made.
        if ( MultiCounter.increaseCounter(2) &&  !child.isLeaf) {
            child.children[child.num+1] = sibling.children[0];
            MultiCounter.increaseCounter(2);
        }

        keys[idx] = sibling.keys[0];
        MultiCounter.increaseCounter(2);
        
        for (int i = 1;  MultiCounter.increaseCounter(2) &&  i < sibling.num; ++i) {
            sibling.keys[i-1] = sibling.keys[i];
            MultiCounter.increaseCounter(2,2);// 2 assignments made.1 for ++i and one for the other.
        }

        if ( MultiCounter.increaseCounter(2) && !sibling.isLeaf){
            for (int i= 1; MultiCounter.increaseCounter(2) &&  i <= sibling.num;++i) {
                sibling.children[i-1] = sibling.children[i];
                MultiCounter.increaseCounter(2,2);// 2 assignments made.1 for ++i and one for the other.
            }
        }
        child.num += 1;
        sibling.num -= 1;
        MultiCounter.increaseCounter(2,2);// 2 assignments made.
    }

    // Merge childre[idx+1] into childre[idx]
    public void merge(int idx){

        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx+1]; 
        // Insert the last key of the current node into the MinDeg-1 position of the child node
        child.keys[MinDeg-1] = keys[idx];

        MultiCounter.increaseCounter(2,3); // 3 assignments made.
        // keys: children[idx+1] copy to children[idx]
        for (int i =0 ; MultiCounter.increaseCounter(2) && i< sibling.num; ++i) {
            child.keys[i+MinDeg] = sibling.keys[i];
            MultiCounter.increaseCounter(2,2); // 2 assignments made.1 for ++i and one for the other.
        }

        // children: children[idx+1] copy to children[idx]
        if (MultiCounter.increaseCounter(2) && !child.isLeaf){
            for (int i = 0;MultiCounter.increaseCounter(2) && i <= sibling.num; ++i) {
                child.children[i+MinDeg] = sibling.children[i];
                MultiCounter.increaseCounter(2,2); // 2 assignments made.1 for ++i and one for the other.
            }
        }

        // Move keys forward, not gap caused by moving keys[idx] to children[idx]
        for (int i = idx+1; MultiCounter.increaseCounter(2) && i<num; ++i) {
            keys[i-1] = keys[i];
            MultiCounter.increaseCounter(2,2); // 2 assignments made.1 for ++i and one for the other.
        }
        // Move the corresponding child node forward
        for (int i = idx+2; MultiCounter.increaseCounter(2) && i<=num;++i) {
            children[i-1] = children[i];
            MultiCounter.increaseCounter(2,2); // 2 assignments made.1 for ++i and one for the other.
        }

        child.num += sibling.num + 1;
        num--;
        MultiCounter.increaseCounter(2,2); // 2 assignments made.
    }


    public void insertNotFull(int key){

        int i = num -1; // Initialize i as the rightmost index
        MultiCounter.increaseCounter(2);// 1 assignment made.
        
        if (MultiCounter.increaseCounter(2) && isLeaf){ // When it is a leaf node
            // Find the location where the new key should be inserted
            while ( (MultiCounter.increaseCounter(2) && i >= 0 ) && 
            		(MultiCounter.increaseCounter(2) && keys[i] > key )){
                keys[i+1] = keys[i]; // keys backward shift
                i--;
                MultiCounter.increaseCounter(2,2);// 2 assignments made.
            }
            keys[i+1] = key;
            num = num +1;
            MultiCounter.increaseCounter(2,2);// 2 assignments made.
        }
        else{
            // Find the child node location that should be inserted
            while ( (MultiCounter.increaseCounter(2) && i >= 0 ) && 
            		(MultiCounter.increaseCounter(2) && keys[i] > key ) ) {
                i--;
                MultiCounter.increaseCounter(2);// 1 assignment made
            }
            if (MultiCounter.increaseCounter(2) && children[i+1].num == 2*MinDeg - 1){ // When the child node is full
                splitChild(i+1,children[i+1]);
                // After splitting, the key in the middle of the child node moves up, and the child node splits into two
                if (MultiCounter.increaseCounter(2) && keys[i+1] < key) {
                    i++;
                    MultiCounter.increaseCounter(2);// 1 assignment made
                }
            }
            children[i+1].insertNotFull(key);
        }
    }


    public void splitChild(int i ,BTreeNode y){

        // First, create a node to hold the keys of MinDeg-1 of y
        BTreeNode z = new BTreeNode(y.MinDeg,y.isLeaf);
        z.num = MinDeg - 1;
        
        MultiCounter.increaseCounter(2,2); // 2 assignments made.
        // Pass the properties of y to z
        MultiCounter.increaseCounter(2); // 1 assignment for int j = 0
        for (int j = 0; MultiCounter.increaseCounter(2) && j < MinDeg-1; j++) {
            z.keys[j] = y.keys[j+MinDeg];
            MultiCounter.increaseCounter(2,2); // 1 assignment for j++ and 1 for the other.
        }
        if (MultiCounter.increaseCounter(2) && !y.isLeaf){
            for (int j = 0; MultiCounter.increaseCounter(2) &&  j < MinDeg; j++) {
                z.children[j] = y.children[j+MinDeg];
                MultiCounter.increaseCounter(2,2); // 1 assignment for j++ and 1 for the other.
            }
        }
        y.num = MinDeg-1;
        MultiCounter.increaseCounter(2);// 1 assignment.
        // Insert a new child into the child
        for (int j = num; MultiCounter.increaseCounter(2) && j >= i+1; j--) {
            children[j+1] = children[j];
            MultiCounter.increaseCounter(2,2); // 1 assignment for j++ and 1 for the other.
        }
        children[i+1] = z;
        MultiCounter.increaseCounter(2); // 1 assignment.
        
        // Move a key in y to this node
        for (int j = num-1; MultiCounter.increaseCounter(2) && j >= i;j--) {
            keys[j+1] = keys[j];
            MultiCounter.increaseCounter(2,2); // 1 assignment for j++ and 1 for the other.
        }
        keys[i] = y.keys[MinDeg-1];

        num = num + 1;
        MultiCounter.increaseCounter(2,2); // 2 assignments.
    }


    public void traverse(){
        int i;
        for (i = 0; i< num; i++){
            if (!isLeaf)
                children[i].traverse();
            System.out.printf(" %d",keys[i]);
        }

        if (!isLeaf){
            children[i].traverse();
        }
    }


    public BTreeNode search(int key){
        int i = 0;
        MultiCounter.increaseCounter(1); // one assignment made.
        while ( (MultiCounter.increaseCounter(1) && i < num ) && 
        		(MultiCounter.increaseCounter(1) && key > keys[i] ) ) {
            i++;
            MultiCounter.increaseCounter(1);//one assignment made: i = i + 1
        }
        
        // index i now contains the index of key we search,or the index of the 1st greater key.
        // So if keys[i] contains the key,we found it,else we traverse the subtree from i's left child.
        if(MultiCounter.increaseCounter(1) && i < num && 
        		MultiCounter.increaseCounter(1) && keys[i] == key) // key found.We need the i<num check,so we have no index out of bounds exception!
        	return this;
        // key does not exist in the current node.Search i's left subtree.
        else if( MultiCounter.increaseCounter(1) && !isLeaf) // if i == num and this is not a leaf node search the rightmost child of this node.
        	return children[i].search(key);
        //to be here node to search is a leaf && ( i=num || keys[i] != key ), so the key does not exist.
        else 
        	return null;
    }
}

