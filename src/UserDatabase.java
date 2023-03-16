import java.util.ArrayList;
public class UserDatabase {
    private ArrayList<User> users;

    public UserDatabase(){
        users= new ArrayList<>();
    }

    public void create(User user){
        users.add(user);
    }

    public User read(String username){
        for (User user: users){
            if (user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }

    public boolean update(String username, String password, String currentBook, int daysLeftToRenew){
        for ( User user: users){
            if (user.getUsername().equals(username)){
                user.setPassword(password);
                user.setCurrentBook(currentBook);
                user.setDaysLeftToRenew(daysLeftToRenew);
                return true;
            }
        }
        return false;
    }



    public boolean delete(String username){
        for (User user: users){
            if (user.getUsername().equals(username)){
                users.remove(user);
                return true;
            }
        }
        return false;
    }
}
