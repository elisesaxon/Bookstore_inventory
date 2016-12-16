/*
 * Elise Saxon - CS145 Assignment 3
 *
 * A basic class for a Book object.
 * Holds book's ISBN number, price, amount on stock,
 * and its back order queue.
 */

public class Book{

   public class InsufficientStock extends IllegalArgumentException {
      public InsufficientStock(String msg) {
         super(msg);
      }
   }
	
	// Fields for the Book class
	private String isbn; 
	private double price; 
	private int stock; 
	private Queue<BackOrder> backOrders;
	
	/*
	* Construct a new book.
	* 
	* Construct a Book object with the given data. The stock for the book
	* is set to zero.
	*/
	Book(String isbn, double price, int stock){
		
		this.isbn = isbn; 
		this.price = price; 
		this.stock = stock; 
		this.backOrders = new Queue<BackOrder>();
	}
	
	/*
	* Construct a new Book object by copying the data from an existing
	* Book object.
	*/
	public Book(Book other) {
		this.isbn = other.isbn; 
		this.price = other.price; 
		this.stock = other.stock;  
		this.backOrders = other.backOrders;
	}
	
	/* Return International Standard Book Number (ISBN) */
	public String getIsbn(){
		return isbn; 
	}
	
	/* Return book price */
	public double getPrice(){
		return price; 
	}
	
	/* Return book stock on hand */
	public int getStock(){
		return stock; 
	}
	
	/* Return back order queue */
	public Queue<BackOrder> getBackOrders() {
		return backOrders;
	}
	
	/* Change the stock on hand */
	public void changeStock(int change) {
		
		//change can be positive or negative. change accordingly
		if (stock-change >= 0) {
			stock -= change;
		//if change makes stock negative, stock should only go to 0.
		//leftovers go to back order queue
		} else {
			stock = 0;
		}
		
	}
}