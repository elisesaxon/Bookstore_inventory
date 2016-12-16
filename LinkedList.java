/*
 * Elise Saxon - CSCI 145 Assignment 3
 * 
 * A Linked List class that holds data of type E (can be defined). Methods that deal with the Linked 
 * List include: size, isEmpty, add (to end or at index), get (data from node at index), and remove 
 * (first element or at index).
 *    
 */

 
public class LinkedList<E> {
	
	public class node {
		E data;
		node next;
		
		// CONSTRUCTORS
		// Creates an empty node
		public node() {
		}
		
		// Creates a node from given data
		public node(E data) {
			this.data = data;
		}
		
		// Creates a node from given data and pointing to a given next node
		public node(E book, node next) {
			this.data = data;
			this.next = next;
		}
	}
	
	
	
	// Initialize global size variable and empty first node of list
	int size = 0;
	private node first;
	
	
	// CONSTRUCTOR: LinkedList
	// Creates an empty LinkedList object from first node (initialized above)
	public LinkedList() {
		first = null;
	}
	
	
	/* METHOD: size
	 * Input: nothing | Output: number of elements in list
	 * 
	 * Returns the number of items in the list
	 * (updated by both add methods and both remove methods)
	 */
	public int size() {
		return size;
	}
	
	
	/* METHOD: isEmpty
	 * Input: nothing | Output: boolean true or false
	 * 
	 * Checks the size of the list and, if the size is zero, isEmpty is true. If the size is
	 * more than zero, there are elements in the list and isEmpty is false.
	 */
	public boolean isEmpty() {
		if (size() == 0) {
			// No elements in list
			return true;
		} else {
			// At least one element in list. Not empty.
			return false;
		}
	}
	
	
	/* METHOD: add
	 * Input:  | Output: nothing (void)
	 * 
	 * Creates a node with the given value and iterates till the end of the list to add it onto 
	 * the end. Adjusts size.
	 */
	public void add(E value) {
		if (size() == 0) {
			// This is the first element in the list
			this.first = new node(value);
		} else {
			// Iterate to the end
			node current = this.first;
			while (current.next != null) {
				current = current.next;
			}
			// Point last element to new node of given value
			current.next = new node(value);
		}
		// Update size
		size += 1;
	}
	
	
	/* METHOD: add
	 * Input:  | Output: nothing (void)
	 * 
	 * Creates a node with the given value and adds it to list at given index by pointing its "next"
	 * field to the node currently at the given index, then pointing the previous node to the 
	 * newly created node. Adjusts the size.
	 */
	public void add(int index, E value) {
		// Create new node from "value" data, create node to point at index of where to add node
		node toAdd = new node(value);
		node addHere = this.first;
		for (int i = 0; i < (index - 1); i++) { 
			addHere = addHere.next;
		}
		
		if (size() == 0) {
			// This is the first element in the list
			this.first = toAdd;
			size += 1;
			
		} else if (index == 0) {
			// Add at beginning
			toAdd.next = this.first;
			this.first = toAdd;
			size += 1;
			
		} else if ((index > 0) && (index < this.size())) {
			// Add at index
			toAdd.next = addHere.next;
			addHere.next = toAdd;
			size += 1;
			
		} else if (index == this.size()) {
			// Add onto end
			add(value);
			
		} else {
			// Index is not valid
			throw new IndexOutOfBoundsException();
		}
	}
	
	
	/* METHOD: get
	 * Input: index of desired element | Output: data from desired element
	 * 
	 * Finds the node at the given index by iterating through the list with a "current" pointer node
	 * until it gets to that node. Retrieves the data and returns it.
	 */
	public E get(int index) {
		node current = first;
		if (index >=0 && index <= this.size()) {
			for (int i = 0; i < index; i++) {
				current = current.next;
			}
		} else {
			throw new IndexOutOfBoundsException();
		}
		
		return current.data;
	}
	
	
	/* METHOD: remove
	 * Input: nothing | Output: data from removed element
	 * 
	 * Removes and returns the data in the first element of the list by re-setting the first element
	 * to the second element and returning the data in the original first element.
	 */
	public E remove() {
		node current = this.first;
		if (size() > 0) {
			this.first = this.first.next;
		} else {
			throw new IndexOutOfBoundsException();
		}
		size -= 1;
		return current.data;
	}
	
	
	/* METHOD: remove
	 * Input: index of element to remove | Output: data in removed element
	 * 
	 * Removes the element at the given index and returns the data from that element.
	 * Uses a current and a previous node to reset where nodes point to in order to remove the
	 * node at the given index. Checks for an illegal index. Adjusts the size.
	 */
	public E remove(int index) {
		// Initiate and set values for the 
		node current = this.first;
		node previous = this.first;
		for (int i = 0; i < (index - 1); i++) {
			previous = previous.next;
		}
		
		if (index == 0) {
			// Remove first element of the list
			this.first = this.first.next;
		} else if ((index > 0) && (index < size())) {
			// Remove the element at the index (point previous node to node after node at index)
			current = previous.next;
			previous.next = current.next;
		} else {
			// Given index is illegal
			throw new IndexOutOfBoundsException();
		}
		
		// Adjust size and return data from removed element
		size -= 1;
		return current.data;
	}
	
}