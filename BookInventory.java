 /* 
  * Elise Saxon
  * CSCI 145 - David Palzer
  * 
  * Assignment 3 - Book Inventory
  * June 1, 2015
  * 
  * Uses programs: Book.java | LinkedList.java | Queue.java | BackOrder.java
  * 
  * Keeps track of an inventory of books as transactions occur. This program initiates
  * a collection, makes stock changes, keeps track of back orders, and fills orders and
  * back orders.
  * 
  */


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class BookInventory {
	
	// global variable to keep track of dollar amount sold
	public static double totalSold;
	
	/* MAIN
	 * Intakes file names off the command line, creates new file and Linked List (to hold 
	 * inventory) objects, and calls functions to deal with transactions and change the inventory.
	 */
	public static void main (String [] args) {
		totalSold = 0;
		
		// variables to hold user's file names off command line 
		String books = "";
		String transactions = "";
		
		// fewer than two arguments provided on command line
		if (args.length != 2) {
			System.out.println("Error: Must enter two file names on command line");
			System.out.println("Usage: java BookInventory bookfile.txt transactionfile.txt");
			System.exit(0);
		} else {
			// sets given file names to variables for later use
			books = args[0];
			transactions = args[1];
		}
		
		// create new files for names entered on command line
		File transactionFile = new File(transactions);
		File bookFile = new File(books);
		
		// create Book type Linked List object to hold book inventory
		LinkedList<Book> bookList = new LinkedList<Book>();
		
		// call function to check if the files can be read (which calls function to fill Linked List inventory)
		check (bookFile, transactionFile, bookList);
		
		// call function to perform transactions from file (calls functions to perform order or stock transactions)
		performTransactions(bookList, transactionFile);
		
		// print final dollar amount of orders filled
		System.out.println("\nTotal value of orders filled is $" +totalSold);
	}
	
	
	/* FUNCTION: check
	 * Input: files of books and transactions, empty list to fill | Output: nothing
	 * 
	 * Verifies if the two files the user entered can be read. If both can, calls a function to 
	 * fill the empty inventory list with the information in the books file.
	 */
	public static void check (File bookFile, File transactionFile, LinkedList<Book> bookList) {
		// check if files can be opened and read
		if (!transactionFile.canRead()) {
			System.out.println("Error: File " + transactionFile + " not found. Exiting program.");
			System.out.println("Usage: java BookInventory bookfile.txt transactionfile.txt");
			System.exit(0);
		} else if (!bookFile.canRead()) {
			System.out.println("Error: File " + bookFile + " not found. Exiting program.");
			System.out.println("Usage: java BookInventory bookfile.txt transactionfile.txt");
			System.exit(0);
		} else {
			// calls function to fill Linked List ("bookList") of inventory from bookFile
			fillBookList(bookFile, bookList);
		}
	}
	
	
	/* FUNCTION: fillBookList
	 * Input: file of book inventory info, empty list to fill | Output: nothing
	 * 
	 * Sets up scanners to read line-by-line and word-by-word through book file to get information
	 * out of it. Separates each line's information into variables, creates a Book object from 
	 * them, adds the book to the inventory Linked List
	 */
	public static void fillBookList (File bookFile, LinkedList<Book> bookList) {
		Scanner input = null;
		try {
			// sets up a new scanner to go through lines in text file
			input = new Scanner(bookFile);
		} catch (FileNotFoundException ex) {
			System.out.println("Error: File " + bookFile + " not found. Exiting program.");
			System.exit(1);
		}
		// scanner reads through file line-by-line
		while (input.hasNextLine()) {
			String line = input.nextLine();
			// sets up a second scanner for data in each line
			Scanner lineData = new Scanner(line);
			
			// assigns each piece of info a variable name
			String isbn = lineData.next();
			double price = lineData.nextDouble();
			int stock = lineData.nextInt();
			
			// creates a Book object from the information
			Book book = new Book(isbn, price, stock);
			
			// adds book to Linked List ("bookList") of inventory
			bookList.add(book);
		}
	}
	
	
	/* FUNCTION: performTransactions
	 * Input: full inventory list, file with transaction information | Output: nothing
	 * 
	 * Sets up scanners to read line-by-line and word-by-word through transaction file to get directions
	 * for transactions. The function separates each line's data into variables and, depending on the 
	 * first direction given on the line, calls a function to perform either an order or a stock
	 */
	public static void performTransactions(LinkedList<Book> bookList, File transactionFile) {
		Scanner input = null;
		try {
			// sets up a new scanner to go through lines in text file
			input = new Scanner(transactionFile);
		} catch (FileNotFoundException ex) {
			System.out.println("Error: File " + transactionFile + " not found. Exiting program.");
			System.exit(1);
		}
		// reads through file line-by-line
		while (input.hasNextLine()) {
			String line = input.nextLine();
			// sets up a second scanner for data in each line
			Scanner lineData = new Scanner(line);
			while (lineData.hasNext()) {
				if (lineData.next().equals("ORDER")) {
					// separate data from line into variables
					String isbn = lineData.next();
					int toOrder = lineData.nextInt();
					int customer = lineData.nextInt();
					
					// call function to complete order transaction
					order(bookList, isbn, toOrder, customer);
				
				// if not "ORDER," it's "STOCK"
				} else {
					// separate data from line into variables
					String isbn = lineData.next();
					int toStock = lineData.nextInt();
					
					// call function to complete stock transaction
					stock(bookList, isbn, toStock);
				}
			}
		}
	}
	
	
	/* FUNCTION: order
	 * Input: inventory list; book's ISBN, ordered amount, customer number | Output: nothing (print output, no return)
	 * 
	 * Finds the book in the inventory list from its ISBN number, then places orders and back orders for each
	 * book, depending on how many of that book are on stock. If the order is greater than the stock, the 
	 * order filled as much as possible and the rest is added to the book's back order queue and the stock is 
	 * decreased. If there is enough stock on hand to fill the order, the order is filled and stock decreased.
	 * A line of output is printed for each transaction performed. For each transaction, the total value of filled orders
	 * is updated.
	 */
	public static void order (LinkedList<Book> bookList, String isbn, int toOrder, int customer) {
		int i = 0;
		// find index of bookList node holding this isbn
		while (!((bookList.get(i)).getIsbn().equals(isbn))) {
			i++;
		}
		// variable to hold book in this transaction
		Book book = bookList.get(i);
		
		// ordering more than is on stock
		if (toOrder >= book.getStock()) {
			// variable to hold amount of books to put into back order 
			int BOStock = 0;
			
			// order can be partly filled with stock on hand
			if (book.getStock() != 0) {
				int stockBefore = book.getStock();
				
				// get amount to add to back order queue
				BOStock = toOrder - book.getStock();
				
				// change book's stock to 0 (exhausted pre-existing stock)
				book.changeStock(toOrder);
				
				// print transaction
				if (stockBefore != 1) {
					 System.out.println("Order filled for customer " +customer+ " for " +stockBefore+ " copies of book " +isbn);
				} else {
					 System.out.println("Order filled for customer " +customer+ " for " +stockBefore+ " copy of book " +isbn);
				}
				
				// update monetary value of filled orders
				totalSold += (stockBefore * book.getPrice());
				
			// stock is already empty -- put all of order on back order
			} else {
				BOStock = toOrder;
			}
			
			// create new backOrder node for back order Queue
			BackOrder backOrd = new BackOrder (customer, BOStock);
			
			// add object to book's queue
			book.getBackOrders().add(backOrd);
			
			// print transaction
			if (BOStock != 1) {
				 System.out.println("Back order for customer " +customer+ " for " +BOStock+ " copies of book " +isbn);
			} else {
				 System.out.println("Back order for customer " +customer+ " for " +BOStock+ " copy of book " +isbn);
			}
			
		// enough books on stock to fill order
		} else {
			// decrease stock by amount of order
			book.changeStock(toOrder);
			
			// print transaction
			if (toOrder != 1) {
				System.out.println("Order filled for customer "+ customer +" for "+ toOrder+ " copies of book " +isbn);
			} else {
				System.out.println("Order filled for customer "+ customer +" for "+ toOrder+ " copy of book " +isbn);
			}
			
			// update monetary value of filled orders
			totalSold += (toOrder * book.getPrice());
		}
	}
	
	
	/* FUNCTION: stock
	 * Input: book inventory, book's ISBN, amount to stock | Output: nothing (print output, no return)
	 * 
	 * Finds the given book in the inventory list based on its ISBN number and updates its stock on hand with the given amount.
	 * Goes through back order queue, if there are any back orders, and fills as many orders as possible with the stock on hand.
	 * If a back order can only be partly filled, it is filled as much as possible and the book stays at the front of the queue 
	 * with the order amount that could not be filled. A line of output is printed for each transaction. For each transaction the
	 * total monetary value of filled orders is updated.
	 */
	public static void stock (LinkedList<Book> bookList, String isbn, int toStock) {
		int i = 0;
		// find index of bookList node holding this isbn
		while (!((bookList.get(i)).getIsbn().equals(isbn))) {
			i++;
		}
		// variable to hold book for this transaction
		Book book = bookList.get(i); 
		
		// change amount of stock on hand
		System.out.println("Stock for book " +isbn+ " increased from " +book.getStock()+ " to " +(book.getStock() + toStock));
		
		// "changeStock" method subtracts stock so to add, must input a negative number
		book.changeStock(0-toStock);
		
		// go through back orders for the book (won't enter loop if there aren't any back orders)
		while (!(book.getBackOrders().isEmpty())) {
			// first back order on queue can be fully filled. Fill and remove from queue
			if (book.getBackOrders().peek().stock <= book.getStock() ) {
				// decrease stock by entire amount of back order
				book.changeStock(book.getBackOrders().peek().stock);
				
				// print transaction
				if (book.getBackOrders().peek().stock != 1) {
					System.out.println("Back order filled for customer " +book.getBackOrders().peek().customer+ " for " +book.getBackOrders().peek().stock+ " copies of book " +isbn);
				} else {
					System.out.println("Back order filled for customer " +book.getBackOrders().peek().customer+ " for " +book.getBackOrders().peek().stock+ " copy of book " +isbn);
				}
				
				// update monetary value of filled orders
				totalSold += (book.getBackOrders().peek().stock * book.getPrice());
				
				// remove order from back order queue
				book.getBackOrders().remove();
				
			// first order on back order queue can be partially filled. Fill partly and keep on queue.
			} else {
				int stockBefore = book.getStock();
				
				// stock now empty
				book.changeStock(book.getBackOrders().peek().stock);
				
				// change amount on back order
				book.getBackOrders().peek().stock -= stockBefore;
				
				// print transaction
				if (stockBefore != 1) {
					System.out.println("Back order filled for customer " +book.getBackOrders().peek().customer+ " for " +stockBefore+ " copies of book " +isbn); 
				} else {
					System.out.println("Back order filled for customer " +book.getBackOrders().peek().customer+ " for " +stockBefore+ " copy of book " +isbn); 
				}
				
				// update monetary value of filled orders
				totalSold += (stockBefore * book.getPrice());
				
				// break out of this "else" loop
				break;
			}
		}
	}
}