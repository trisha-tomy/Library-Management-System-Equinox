public class User {
    private String username;

    private String password;

    private String currentBook;
    private int daysLeftToRenew;


    public User(String username, String password, String currentBook, int daysLeftToRenew){
        this.username=username;
        this.password= password;
        this.currentBook= currentBook;
        this.daysLeftToRenew= daysLeftToRenew;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password= password;
    }

    public String getCurrentBook(){
        return currentBook;
    }

    public void setCurrentBook(String currentBook){
        this.currentBook= currentBook;
    }

    public int getDaysLeftToRenew(){
        return daysLeftToRenew;
    }

    public void setDaysLeftToRenew(int daysLeftToRenew){
        this.daysLeftToRenew= daysLeftToRenew;
    }

}