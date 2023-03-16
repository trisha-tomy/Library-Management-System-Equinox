public class Book {
    private String bookName;
    private double ISBN;
    private String currentUser;
    private int daysLeftToRenew;

    public Book(String bookName, double ISBN, String currentUser, int daysLeftToRenew){
        this.bookName=bookName;
        this.ISBN= ISBN;
        this.currentUser= currentUser;
        this.daysLeftToRenew= daysLeftToRenew;
    }

    public String getBookName(){
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public double getISBN(){
        return ISBN;
    }

    public void setISBN(double ISBN){
        this.ISBN= ISBN;
    }

    public String getCurrentUser(){
        return currentUser;
    }

    public void setCurrentUser(String currentUser){
        this.currentUser= currentUser;
    }

    public int getDaysLeftToRenew(){
        return daysLeftToRenew;
    }

    public void setDaysLeftToRenew(int daysLeftToRenew){
        this.daysLeftToRenew= daysLeftToRenew;
    }

}