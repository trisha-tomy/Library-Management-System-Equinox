import java.util.ArrayList;
public class BookDatabase_Library {
    private ArrayList<Book> books;

    public BookDatabase_Library(){
        books= new ArrayList<>();
    }

    public void create(Book book){
        books.add(book);
    }

    public Book read(String bookName){
        for (Book book: books){
            if (book.getBookName().equals(bookName)){
                return book;
            }
        }
        return null;
    }

    public boolean update(String bookName, double ISBN, String currentUser, int daysLeftToRenew){
        for ( Book book: books){
            if (book.getBookName().equals(bookName)){
                book.setISBN(ISBN);
                book.setCurrentUser(currentUser);
                book.setDaysLeftToRenew(daysLeftToRenew);
                return true;
            }
        }
        return false;
    }



    public boolean delete(String bookName){
        for (Book book: books){
            if (book.getBookName().equals(bookName)){
                books.remove(book);
                return true;
            }
        }
        return false;
    }
}
