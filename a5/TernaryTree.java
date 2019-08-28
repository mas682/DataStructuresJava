package cs445.a5;

import java.util.Iterator;
import java.util.NoSuchElementException;

import cs445.StackAndQueuePackage.*;

public class TernaryTree<T> implements TernaryTreeInterface<T>  {
	private TernaryNode<T> root;
	
	public TernaryTree() {
		root = null;
	}
	
	public TernaryTree(T rootData) {
		root = new TernaryNode<>(rootData);
	}
	
	public TernaryTree(T rootData, TernaryTree<T> leftTree,
						TernaryTree<T> middleTree, TernaryTree<T> rightTree) {
		privateSetTree(rootData, leftTree, middleTree, rightTree);
	}
	
	/** Sets the ternary tree to a new one-node ternary tree with the given data
     *  @param rootData  The data for the new tree's root node
     */
	public void setTree(T rootData) {
		root = new TernaryNode<>(rootData);
	}
	 
	 /** Sets this ternary tree to a new ternary tree
     *  @param rootData  The data for the new tree's root node
     *  @param leftTree  The left subtree of the new tree
     *  @param middleTree  The middle subtree of the new tree
     *  @param rightTree  The right subtree of the new tree
     */
	public void setTree(T rootData, TernaryTreeInterface<T> leftTree,
						TernaryTreeInterface<T> middleTree,
						TernaryTreeInterface<T> rightTree)  {
		privateSetTree(rootData, (TernaryTree<T>) leftTree, (TernaryTree<T>) middleTree,
						(TernaryTree<T>) rightTree);
	}
	
	private void privateSetTree(T rootData, TernaryTree<T> leftTree,
											TernaryTree<T> middleTree,
											TernaryTree<T> rightTree) {
		root = new TernaryNode<>(rootData);
		
		if((leftTree != null) && !leftTree.isEmpty()) {
			root.setLeftChild(leftTree.root);
		}
		if((middleTree != null) && !middleTree.isEmpty()) {
			if(middleTree != leftTree && middleTree != rightTree) {
				root.setMiddleChild(middleTree.root);
			} else {
				root.setMiddleChild(middleTree.root.copy());
			}
		}
		
		if((rightTree != null) && !rightTree.isEmpty()) {
			if(rightTree != leftTree && rightTree != middleTree){
				root.setRightChild(rightTree.root);
			} else{
				root.setRightChild(rightTree.root.copy());
			}
		}
		
		if((leftTree != null) && (leftTree != this)) {
			leftTree.clear();
		}
		
		if((middleTree != null) && (middleTree != this)) {
			middleTree.clear();
		}
		
		if((rightTree != null) && (rightTree != this)) {
			rightTree.clear();
		}
	}
	
	 /** Gets the data in the root node
     *  @return  the data from the root node
     *  @throws EmptyTreeException  if the tree is empty */
	public T getRootData() {
		if(isEmpty()) {
			throw new EmptyTreeException();
		} else {
			return root.getData();
		}
	}
	
	 /** Determines whether the tree is empty (i.e., has no nodes)
     *  @return  true if the tree is empty, false otherwise */
	public boolean isEmpty() {
		return root == null;
	}
	
	/** Removes all data and nodes from the tree */
	public void clear() {
		root = null;
	}
	
	protected void setRootData(T rootData) {
		root.setData(rootData);
	}
	
	protected void setRootNode(TernaryNode<T> rootNode) {
		root = rootNode;
	}
	
	protected TernaryNode<T> getRootNode() {
		return root;
	}
	
	 /** Gets the height of the tree (i.e., the maximum number of nodes passed
     *  through from root to leaf, inclusive)
     *  @return  the height of the tree */
	public int getHeight() {
		int height = 0;
		if(!isEmpty()) {
			height = root.getHeight();
		}
		return height;
	}
	
	 /** Counts the total number of nodes in the tree
     *  @return  the number of nodes in the tree */
	public int getNumberOfNodes() {
		int numberOfNodes = 0;
		if(!isEmpty()) {
			numberOfNodes = root.getNumberOfNodes();
		}
		return numberOfNodes;
	}
	
	public Iterator<T> getPreorderIterator() {
		return new PreorderIterator();
	}
	
	/**
	* We do not implement this method as it depends how the client would
	* want implement it.  Given you could visit the middle node before or after 
	* visiting the parent node, we leave it up to the client to decide.
	*/
	public Iterator<T> getInorderIterator() {
		throw new UnsupportedOperationException();
	}
	
	public Iterator<T> getPostorderIterator() {
		return new PostorderIterator();
	}
	
	public Iterator<T> getLevelOrderIterator() {
		return new LevelOrderIterator();
	}
	
	private class PreorderIterator implements Iterator<T> {
		private StackInterface<TernaryNode<T>> nodeStack;
		
		public PreorderIterator() {
			nodeStack = new LinkedStack<>();
			if(root != null){
				nodeStack.push(root);
			}
		}
		
		public boolean hasNext() {
			return !nodeStack.isEmpty();
		}
		
		public T next() {
			TernaryNode<T> nextNode;
			
			if(hasNext()) {
				nextNode = nodeStack.pop();
				TernaryNode<T> leftChild = nextNode.getLeftChild();
				TernaryNode<T> middleChild = nextNode.getMiddleChild();
				TernaryNode<T> rightChild = nextNode.getRightChild();
				
				if(rightChild != null) {
					nodeStack.push(rightChild);
				}
				
				if(middleChild != null) {
					nodeStack.push(middleChild);
				}
				
				if(leftChild != null) {
					nodeStack.push(leftChild);
				}
				
			} else{
				throw new NoSuchElementException();
			}
			
			return nextNode.getData();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
	private class PostorderIterator implements Iterator<T> {
		private StackInterface <TernaryNode<T>> nodeStack;
		private TernaryNode<T> currentNode;
		
		public PostorderIterator() {
			nodeStack = new LinkedStack<>();
			currentNode = root;
		}
		
		public boolean hasNext() {
			return !nodeStack.isEmpty() || (currentNode != null);
		}
		
		public T next() {
			boolean foundNext = false;
			TernaryNode<T> leftChild, middleChild, rightChild, nextNode = null;
			
			while(currentNode != null) {
				nodeStack.push(currentNode);
				//System.out.println("Pushing: " + currentNode.getData());
				leftChild = currentNode.getLeftChild();
				middleChild = currentNode.getMiddleChild();
				rightChild = currentNode.getRightChild();
				if(leftChild == null){
					if(middleChild == null)
					{
							currentNode = currentNode.getRightChild();
					}
					else
					currentNode = currentNode.getMiddleChild();
				}
				else{
					currentNode = currentNode.getLeftChild();
				}
		}
		
		if(!nodeStack.isEmpty()) {
			nextNode = nodeStack.pop();
			
			TernaryNode<T> parent = null;
			if( !nodeStack.isEmpty()) {
				parent = nodeStack.peek();
				if (nextNode == parent.getLeftChild()) {
					if(parent.getMiddleChild() == null)
					{
						currentNode = parent.getRightChild();
					}
					else
						currentNode = parent.getMiddleChild();
				}
				else if (nextNode == parent.getMiddleChild()) {
					currentNode = parent.getRightChild();
				} else {
					currentNode = null;
				}
			} else {
				currentNode = null;
			}
		} else {
			throw new NoSuchElementException();
		}
		
		return nextNode.getData();
	}
	
	public void remove() {
		throw new UnsupportedOperationException();
	}

	}
	
	private class LevelOrderIterator implements Iterator<T> {
		private QueueInterface<TernaryNode<T>> nodeQueue;
		
		public LevelOrderIterator() {
			nodeQueue = new LinkedQueue<>();
			if(root != null) {
				nodeQueue.enqueue(root);
			}
		}
		
		public boolean hasNext() {
			return !nodeQueue.isEmpty();
		}
		
		public T next() {
			TernaryNode<T> nextNode;
			
			if(hasNext()) {
				nextNode = nodeQueue.dequeue();
				TernaryNode<T> leftChild = nextNode.getLeftChild();
				TernaryNode<T> middleChild = nextNode.getMiddleChild();
				TernaryNode<T> rightChild = nextNode.getRightChild();
				
				if(leftChild != null) {
					nodeQueue.enqueue(leftChild);
				}
				
				if(middleChild != null) {
					nodeQueue.enqueue(middleChild);
				}
				
				if(rightChild != null) {
					nodeQueue.enqueue(rightChild);
				}
			} else {
				throw new NoSuchElementException();
			}
			return nextNode.getData();
		}
		
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
	
				
	
	
		



}