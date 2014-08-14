package Library;

import java.io.*;
import java.text.*;
import java.util.*;

//Sen Li 

public class Library {
	
	private ArrayList<Book> bookList;
	private ArrayList<User> userList;
	final String bookListURL = "c:\\BookList.dat";
	final String userListURL = "c:\\userList.dat";
	static final int LIBRARY_OWNER_ID = 0; // ownerId of the library is 0. i.e. not rented.  
	final long OverdueTimeLimit = 60*1000; // in millisecond
	final long NewbookTimeLimit = 60*1000; // in millisecond
	final int FINE_PER_SECOND = 1;
	
	Library(){ //constructor
		bookList = new ArrayList<Book>();
		userList = new ArrayList<User>();
	}
	
	
	void saveBooks() throws IOException{
		
		FileOutputStream fout = new FileOutputStream(bookListURL);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(bookList);

	}
	
	void saveUsers() throws IOException{
		
		FileOutputStream fout = new FileOutputStream(userListURL);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(userList);
		oos.close();
	}
	
	@SuppressWarnings("unchecked")
	void loadBooks() throws Exception{
		
		FileInputStream fis = new FileInputStream(bookListURL);
	    ObjectInputStream ois = new ObjectInputStream(fis);
	    bookList = (ArrayList<Book>)ois.readObject();
	    ois.close();
	    
	 
	}
	
	@SuppressWarnings("unchecked")
	void loadUsers() throws Exception{
		
		FileInputStream fis = new FileInputStream(userListURL);
	    ObjectInputStream ois = new ObjectInputStream(fis);
	    userList = (ArrayList<User>)ois.readObject();
	    ois.close();
	}
	 
	
	
	boolean loginCheck(String userName, String password){
		Iterator<User> userItr = userList.iterator();
		while(userItr.hasNext()){
			User tempUser = userItr.next();
			if(userName.equals(tempUser.getUserName()) && password.equals(tempUser.getPassword())){
				return true;
			}//end if
		}//end while
		return false;
		
	}//login check  - old version
	
	
	User login(String userName, String password){
		
		User currentUser = null; //initialize
		Iterator<User> userItr = userList.iterator();
		while(userItr.hasNext()){
			User tempUser = userItr.next();
			if(tempUser.getUserName().equals(userName) && tempUser.getPassword().equals(password)){
				currentUser = tempUser;
				break;
			}//if login successful
		}//while
		return currentUser; //return null if login failed.
	}
	
	String getBookImgFileFullName(String isbn){
		
		return isbn + ".jpg"; //Extension .jpg
		
	}
	
	
	void addBook(Book b){
		b.setAddedDate(new Date());
		bookList.add(b);
		
	}//add book
	
	void updateBook(String isbn, Book b){
		Iterator<Book> bookItr = bookList.iterator();
		int index = -1;
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			index++;
			if(tempBook.getIsbn().equals(isbn)){
				bookList.set(index, b);
				break;
			}//end if
		}//end while
	}//update book
	
	void deleteBook(String isbn){
		Iterator<Book> bookItr = bookList.iterator();
		int index = -1;
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			index++;
			if(tempBook.getIsbn().equals(isbn)){
				bookList.remove(index);
				break;
			}//end if
		}//end while
	}//delete book
	
	boolean addUser(User user){
		return userList.add(user);
	}
	
	boolean updateUser(int userId, User user){
		if(user == null){
			return false;
		}
		Iterator<User> userItr = userList.iterator();
		int index = -1;
		while(userItr.hasNext()){
			User tempUser = userItr.next();
			index++;
			if(tempUser.getUserId()==userId){
				userList.set(index, user);
				return true;
			}//end if
		}//end while
		
		return false;
	}//update user
	
	boolean deleteUser(int userId){
		
		Iterator<User> userItr = userList.iterator();
		int index = -1;
		while(userItr.hasNext()){
			User tempUser = userItr.next();
			index++;
			if(tempUser.getUserId()==userId){
				userList.remove(index);
				return true;
			}//end if
		}//end while
		return false;
				
	}//delete user
	

	ArrayList<User> showUserList(){
		
		return userList;
	}//show all user
	
	ArrayList<Book> showBookList_all(){
		
		return bookList;
	}//show all books
	
	ArrayList<Book> showBookList_rented(){
		
		Iterator<Book> bookItr = bookList.iterator();
		ArrayList<Book> tempBookList = new ArrayList<Book>();
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			if(tempBook.isRented() == true){
				tempBookList.add(tempBook);
			}//end if
		}//end while
		
		return tempBookList;
		
	}//
	
	ArrayList<Book> showBookList_remainder(){
		
		Iterator<Book> bookItr = bookList.iterator();
		ArrayList<Book> tempBookList = new ArrayList<Book>();
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			if(tempBook.isRented() != true){
				tempBookList.add(tempBook);
			}//end if
		}//end while
		
		return tempBookList;
	}
	
	ArrayList<Book> showBookList_BorrowedByCustomer(int customerId){
	
		Iterator<Book> bookItr = bookList.iterator();
		ArrayList<Book> tempBookList = new ArrayList<Book>();
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			if(tempBook.getOwnerId()==customerId){
				tempBookList.add(tempBook);
				
			}//end if
		}//end while
		return tempBookList;	
	}
	
	
	ArrayList<Book> showBookList_overdue(){
		
		Iterator<Book> bookItr = bookList.iterator();
		ArrayList<Book> tempBookList = new ArrayList<Book>();
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			if(tempBook.isRented()==true){
				
				if(new Date().getTime() - tempBook.getLastRented().getTime() >=  OverdueTimeLimit)
				tempBookList.add(tempBook);
				
				
			}//end if
		}//end while
		
		return tempBookList;
	}
	
	
	boolean rentBook(int customerId, String isbn){
		
		Iterator<Book> bookItr = bookList.iterator();
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			if(tempBook.getIsbn().equals(isbn) && tempBook.isRented()==false){
				tempBook.setLastRented(new Date());
				tempBook.setRented(true);
				tempBook.setOwnerId(customerId);
				return true;
			}//end if
		}//end while
		return false;
	}
	
	void returnBook(String isbn){
		

		Iterator<Book> bookItr = bookList.iterator();
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			if(tempBook.getIsbn().equals(isbn)){
				tempBook.setRented(false);
				tempBook.setOwnerId(LIBRARY_OWNER_ID);
				
			}//end if
		}//end while

	}
	
	ArrayList<Book> showBookList_preferedCategory(Category[] ctgList){
		
		ArrayList<Book> tempBookList = new ArrayList<Book>();
		Category currentCtg = null;
		for(int i =0;i<ctgList.length;i++){//for each category
			currentCtg = ctgList[i];
			Iterator<Book> bookItr = bookList.iterator();
			while(bookItr.hasNext()){
				Book tempBook = bookItr.next();
				if(tempBook.getCategory()==currentCtg){
					tempBookList.add(tempBook);
					
				}//end if
			}//end while
			
		}//end for
		return tempBookList;
		
	}
	
	ArrayList<Book> showBookList_new(Category[] ctgList){
		
		Date currentDate = new Date(); 
		ArrayList<Book> tempBookList = new ArrayList<Book>();
		Category currentCtg = null;
		for(int i =0;i<ctgList.length;i++){//for each category
			currentCtg = ctgList[i];
			Iterator<Book> bookItr = bookList.iterator();
			while(bookItr.hasNext()){
				Book tempBook = bookItr.next();
				if(tempBook.getCategory()==currentCtg){
					
					if(currentDate.getTime() - tempBook.getAddedDate().getTime() < NewbookTimeLimit)
					tempBookList.add(tempBook);
					
				}//end if
			}//end while
			
		}//end for
		return tempBookList;
		
	}
	
	public double fine(String isbn, Date returnDate){
		
		Iterator<Book> bookItr = bookList.iterator();
		while(bookItr.hasNext()){
			Book tempBook = bookItr.next();
			if(tempBook.getIsbn().equals(isbn)){
				
				double fine_amount = (returnDate.getTime() - tempBook.getLastRented().getTime())/1000*FINE_PER_SECOND;
				return fine_amount;
			}//end if
		}//end while
		
		return 0.0;
	}
	
		
}
