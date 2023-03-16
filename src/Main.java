public class Main{
    public static void main(String[] args) {
        BookDatabase_Library db= new BookDatabase_Library();

        Book c1= new Book("Orion", 73765, "maria", 15);
        db.create(c1);

        Book c2= db.read("Orion");
        System.out.println("Book name: "+ c2.getBookName());
        System.out.println("ISBN: "+ c2.getISBN());
        System.out.println("Username: "+ c2.getCurrentUser());
        System.out.println("Days left to renew: "+ c2.getDaysLeftToRenew());

        db.update("Orion", 8237569, "jason", 14);

        System.out.println("Book name: "+ c2.getBookName());
        System.out.println("ISBN: "+ c2.getISBN());
        System.out.println("Username: "+ c2.getCurrentUser());
        System.out.println("Days left to renew: "+ c2.getDaysLeftToRenew());

        db.delete("Orion");

        System.out.println("Book name: "+ c2.getBookName());
        System.out.println("ISBN: "+ c2.getISBN());
        System.out.println("Username: "+ c2.getCurrentUser());
        System.out.println("Days left to renew: "+ c2.getDaysLeftToRenew());

    }
}