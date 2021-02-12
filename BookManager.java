package sait.bms.manager;

import java.io.*;
import java.util.*;

//import sait.bms.application.AppDriver;
import sait.bms.problemDomain.*;

/**
 * Class description: CPRG 251_D
 *
 * @author BNolan (561596)
 *
 */
public class BookManager {

	static char ending;
	private static ArrayList<Book> books;
	private Scanner keyboard;

	/**
	 * Initializes the newly created BookManager
	 * 
	 * @throws IOException Allows loadFile method to function
	 */
	 
	public BookManager() throws IOException {
		books = new ArrayList<>();
		loadFile();
		menu();
	}

	/**
	 * This method prints the main menu
	 * 
	 * @throws IOException Allows saveBookFile method to function
	 */
	private void menu() throws IOException {
		int select;
		do {
			System.out.println("Welcome to ABC Book Company: How may we assist you?");
			System.out.println("1      Checkout Book");
			System.out.println("2      Find Books by Title");
			System.out.println("3      Display Books by Type");
			System.out.println("4      Produce Random Book List");
			System.out.println("5      Save & Exit");
			System.out.print("Enter option:");
			keyboard = new Scanner(System.in);
			select = keyboard.nextInt();
			keyboard.nextLine();

			switch (select) {
			case 1:
				checkoutBook();
				break;
			case 2:
				findBookByTitle();
				break;
			case 3:
				displayBooksByCategory();
				break;
			case 4:
				produceRandom();
				break;
			case 5:
				saveBookFile();
				break;
				default:
					System.out.println("Wrong menu item!");
			}

		} while (select != 5);
	}

	/**
	 * This method loads the books.txt file and scans the file to retrieve all of
	 * the book objects
	 * 
	 * @throws FileNotFoundException Throws fileNotFound error
	 */
	private void loadFile() throws FileNotFoundException {
		File file = new File("res/books.txt");
		
		//InputStream input = BookManager.class.getClassLoader().getResourceAsStream("res/books.txt");
		//this.getClass().getResourceAsStream("res/books.txt");
		Scanner in = new Scanner(file);

		getValues(in);
	}

	/**
	 * This method finds the book by its title
	 */
	private void findBookByTitle() {
		System.out.print("Enter a title to search for: ");
		String titleSearch = keyboard.nextLine();
		System.out.println();

		for (int i = 0; i < books.size(); i++) {
			Book b = books.get(i);

				if (b.getBookTitle().contains(titleSearch)) {
					b.printBook();
				}
			}
		}

	/**
	 * This method displays books belonging to a particular category
	 */
	private void displayBooksByCategory() {
		System.out.println("#     Type");
		System.out.println("1     Children's Books");
		System.out.println("2     CookBooks");
		System.out.println("3     Paperbacks");
		System.out.println("4     Periodicals");
		System.out.println();
		System.out.print("Enter type of book: ");
		int category = keyboard.nextInt();

		printBooksByType(category);
	}

	/**
	 * This method is used to check out a book by the title
	 * 
	 */
	private void checkoutBook() {
		System.out.println("What book would you like to check out?");
		String selection = keyboard.nextLine();

		for (int i = 0; i < books.size(); i++) {

			Book b = books.get(i);

			if (((Book) b).getBookTitle().equals(selection)) {

				int availNumber = ((Book) b).getAvailabiltyNumber();

				System.out.println("Currently Available: " + availNumber);

				availNumber -= 1;

				((Book) b).setAvailabiltyNumber(availNumber);

				System.out.println("Book has been checked out and the new Available Number is: " + availNumber);
				System.out.println();
			}
		}
	}

	/**
	 * This method produces a number of random books depeneding on user input
	 */
	private void produceRandom() {
		System.out.print("Enter number of books:");
		int numberOfBooks = keyboard.nextInt();
		Random random = new Random();

		System.out.println("Random Books:");
		for (int i = 0; i < numberOfBooks; i++) {
			int x = random.nextInt(books.size());
			Book b = books.get(x);

				b.printBook();
			}
		}
	

	/**
	 * This method saves and updates the books.txt file
	 * 
	 * @throws IOException Throws error of IOException
	 */
	private void saveBookFile() throws IOException {
		System.out.println("Thank you for using ABC Book Company, Goodbye");

		PrintWriter out = new PrintWriter(new FileWriter("res//books.txt"));
		for (Book b : books) {
			if (b.isActive()) {
				out.println(b);
			}
		}
		out.close();
	}

	/**
	 * @param in Scans the text file
	 *            
	 * @author BNolan
	 */
	private void getValues(Scanner in) {
		while (in.hasNext()) {

			String line = in.nextLine();
			String[] fields = line.split(";");

			String StringISBN = fields[0]; //I start the ISBN as a string to get the last digit for the if statement
			long ISBN = Long.parseLong(fields[0]);

			ending = StringISBN.charAt(StringISBN.length() - 1);

			if (ending == '2' || ending == '3') {

				String callNumber = fields[1];
				int availNumber = Integer.parseInt(fields[2]);
				int totalNumber = Integer.parseInt(fields[3]);
				String bookTitle = fields[4];
				String publisher = fields[5];
				String diet = fields[6];

				books.add(new CookBook(ISBN, callNumber, availNumber, totalNumber, bookTitle, publisher, diet));

			} else if (ending == '0' || ending == '1') {
				String callNumber = fields[1];
				int availNumber = Integer.parseInt(fields[2]);
				int totalNumber = Integer.parseInt(fields[3]);
				String bookTitle = fields[4];
				String author = fields[5];
				String format = fields[6];

				books.add(new ChildrensBook(ISBN, callNumber, availNumber, totalNumber, bookTitle, author, format));

			} else if (ending == '4' || ending == '7') {
				String callNumber = fields[1];
				int availNumber = Integer.parseInt(fields[2]);
				int totalNumber = Integer.parseInt(fields[3]);
				String bookTitle = fields[4];
				String author = fields[5];
				long year = Long.parseLong(fields[6]);
				String genre = fields[7];

				books.add(new PaperBack(ISBN, callNumber, availNumber, totalNumber, bookTitle, author, year, genre));
			} else if (ending == '8' || ending == '9') {
				String callNumber = fields[1];
				int availNumber = Integer.parseInt(fields[2]);
				int totalNumber = Integer.parseInt(fields[3]);
				String bookTitle = fields[4];
				String frequency = fields[5];

				books.add(new Periodical(ISBN, callNumber, availNumber, totalNumber, bookTitle, frequency));
			}
		}
		in.close();
	}

	private void printBooksByType(int category) {

		if (category == 1) {
			System.out.println("Enter a Format (E for Educational, C for Comic, P for Picture): ");
			String format = keyboard.next();

			for (int i = 0; i < books.size(); i++) {
				Book b = books.get(i);

				if (books.get(i) instanceof ChildrensBook) {
					if (((ChildrensBook) b).getFormat().equals(format)) {
						b.printBook();
					}
				}
			}
		}
		if (category == 2) {
			System.out.println(	"Enter a Diet (G for Gluten-Free, I for Italian, N for No-Carbs, V for Vegan, D for Diabetic)");
			String diet = keyboard.next();

			for (int i = 0; i < books.size(); i++) {
				Book b = books.get(i);

				if (books.get(i) instanceof CookBook) {
					if (((CookBook) b).getDiet().equals(diet)) {
						b.printBook();
					}
				}
			}
		} else if (category == 3) {
			System.out.println("Enter a Genre (F for Fantasy, C for Classic, E for Educational, D for Drama, S for Suspense, A for Action): ");
			String genre = keyboard.next();

			for (int i = 0; i < books.size(); i++) {
				Book b = books.get(i);
				if (books.get(i) instanceof PaperBack) {
					if (((PaperBack) b).getGenre().equals(genre)) {
						b.printBook();
					}
				}
			}
		} else if (category == 4) {
			System.out.println("Enter a Frequency (D for Daily, W for Weekly, M for Monthly, B for Biweekly, or Q for Quarterly): ");
			String frequency = keyboard.next();

			for (int i = 0; i < books.size(); i++) {
				Book b = books.get(i);
				if (b instanceof Periodical) {
					if (((Periodical) b).getFrequency().equals(frequency)) {
						b.printBook();
					}
				}
			}
		}
	}
}
