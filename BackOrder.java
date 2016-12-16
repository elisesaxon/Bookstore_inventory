/*
 * Elise Saxon - CS145 Assignment 3
 * 
 * CLASS BackOrder
 * Class to hold BackOrder objects.
 * Holds a customer number and the stock amount of a book 
 * that customer ordered.
 */

public class BackOrder  {
	public int customer;
	public int stock;
	
	public BackOrder (int customerNum, int bookStock) {
		customer = customerNum;
		stock = bookStock;
	}
}