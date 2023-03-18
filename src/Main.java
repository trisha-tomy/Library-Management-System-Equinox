import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Main{
    public static class ex{
        public static int days=0;
    }
    public static void main(String[] args) {
        login();
    }

    public static void login(){
        JFrame f= new JFrame("Login");
        JLabel l1,l2;

        l1= new JLabel("Username");
        l1.setBounds(30,15,100,30);

        l2= new JLabel("Password");
        l2.setBounds(30,50,100,30);

        JTextField F_username= new JTextField();
        F_username.setBounds(110,15,200,30);

        JPasswordField F_password= new JPasswordField();
        F_password.setBounds(110,50,200,30);

        JButton login_button= new JButton("Login");
        login_button.setBounds(130,90,80,25);
        login_button.addActionListener(e -> {
            String username= F_username.getText();
            String password=String.valueOf(F_password.getPassword());

            System.out.println(password);

            if (username.equals("")){
                JOptionPane.showMessageDialog(null,"Please enter some username.");
            }
            else if (password.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter some password.");
            }
            else{
                Connection connection= connect();
                try{
                    Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    statement.executeUpdate("USE LIBRARY;");
                    String sttmnt= ("SELECT * FROM USERS WHERE USERNAME= '"+username+"' AND PASSWORD= '"+password+"';"); //
                    ResultSet result= statement.executeQuery(sttmnt);
                    if (result.next()==false){
                        System.out.print("No such user");
                        JOptionPane.showMessageDialog(null,"Incorrectly entered Username or Password.");
                    }
                    else{
                        f.dispose();
                        result.beforeFirst();
                        while (result.next()){
                            String admin= result.getString("ADMIN");
                            String UserID= result.getString("UserID");
                            if(admin.equals("1")){
                                admin_menu();
                            }
                            else {
                                user_menu(UserID);
                            }
                        }
                    }
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }

            }
        });

        f.add(F_password);
        f.add(login_button);
        f.add(F_username);
        f.add(l1);
        f.add(l2);

        f.setSize(400,180);
        f.setLayout(null);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    public static Connection connect(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost/LIBRARY?"+"user=db_user&password=123456");
            return con;
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public static DefaultTableModel buildTableModel(ResultSet resultSet){
        Vector<Vector<Object>> data= new Vector<Vector<Object>>();
        Vector<String> columnNames= new Vector<String>();
        try{
            ResultSetMetaData metaData= resultSet.getMetaData();


            int columnCount= metaData.getColumnCount();
            for (int column = 1; column <=columnCount ; column++) {
                columnNames.add(metaData.getColumnName(column));

            }


            while (resultSet.next()){
                Vector<Object> vector= new Vector<Object>();
                for (int columnIndex = 1; columnIndex <=columnCount ; columnIndex++) {
                    vector.add(resultSet.getObject(columnIndex));

                }
                data.add(vector);
            }

        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return new DefaultTableModel(data,columnNames);
    }


    public static void user_menu(String UserID){
        JFrame f = new JFrame("User Functions");


        JButton view_button= new JButton("View Books");
        view_button.setBounds(20,20,180,25);
        view_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Books Available");

                Connection connection= connect();
                String sql="select * from BOOKS";
                try{
                    Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    statement.executeUpdate("USE LIBRARY");
                    ResultSet result= statement.executeQuery(sql);
                    JTable book_list= new JTable(buildTableModel(result));

                    JScrollPane scrollPane= new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(800,400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                }
                catch (SQLException e1){
                    JOptionPane.showMessageDialog(null,e1);
                }
            }
        });


        JButton my_book= new JButton("My Books");
        my_book.setBounds(220,20,180,25);
        my_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f=new JFrame("My Books");
                int UserID_int= Integer.parseInt(UserID);

                Connection connection = connect();

                String sql= "select distinct ISSUED.*,BOOKS.BOOKNAME,BOOKS.GENRE,BOOKS.PRICE from ISSUED,BOOKS" + " where((ISSUED.USERID="+ UserID_int+ ") and BOOKS.BOOKID in (select BOOKID from ISSUED where ISSUED.USERID="+ UserID_int+")) group by ISSUEDID";
                String sql1= "select BOOKID from ISSUED where USERID="+ UserID_int;

                try{
                    Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    statement.executeUpdate("USE LIBRARY");
                    ArrayList books_list= new ArrayList();

                    ResultSet result= statement.executeQuery(sql);
                    JTable book_list= new JTable(buildTableModel(result));

                    JScrollPane scrollPane= new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(800,400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                }
                catch (SQLException e1){
                    JOptionPane.showMessageDialog(null,e1);
                }
            }
        });


        JButton history= new JButton("Borrowing History");
        history.setBounds(420,20,180,25);
        history.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f=new JFrame("My Books");
                int UserID_int= Integer.parseInt(UserID);

                Connection connection = connect();

                String sql= "select * from ISSUED where USERID= "+ UserID_int+";";
                String sql1= "select BOOKID from ISSUED where USERID="+ UserID_int;

                try{
                    Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    statement.executeUpdate("USE LIBRARY");
                    ArrayList books_list= new ArrayList();

                    ResultSet result= statement.executeQuery(sql);
                    JTable book_list= new JTable(buildTableModel(result));

                    JScrollPane scrollPane= new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(800,400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                }
                catch (SQLException e1){
                    JOptionPane.showMessageDialog(null,e1);
                }
            }
        });


        JButton reserve_book= new JButton(("Reserve Book"));
        reserve_book.setBounds(20,100,180,25);
        reserve_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter Details");
                JLabel l1,l2,l3,l4;
                l1= new JLabel("Book ID (BOOKID)");
                l1.setBounds(30,15,150,30);

                JTextField F_bookid= new JTextField();
                F_bookid.setBounds(180,15,200,30);

                JButton create_button= new JButton(("Submit"));
                create_button.setBounds(130,170,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int UserID_int= Integer.parseInt(UserID);
                        String bookid= F_bookid.getText();

                        Connection connection= connect();

                        try {
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            statement.executeUpdate("UPDATE BOOKS SET RESERVED=1, RESERVED_BY= "+UserID_int+" WHERE BOOKID= "+bookid+";");
                            JOptionPane.showMessageDialog(null,"Book Reserved!");
                            f.dispose();
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }
                    }
                });

                f.add(create_button);
                f.add(l1);
                f.add(F_bookid);
                f.setSize(400,250);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton cancel_reservation= new JButton(("Cancel Reservations"));
        cancel_reservation.setBounds(220,100,180,25);
        cancel_reservation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter Details");
                JLabel l1,l2,l3,l4;
                l1= new JLabel("Book ID (BOOKID)");
                l1.setBounds(30,15,150,30);

                JTextField F_bookid= new JTextField();
                F_bookid.setBounds(180,15,200,30);

                JButton create_button= new JButton(("Submit"));
                create_button.setBounds(130,170,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int UserID_int= Integer.parseInt(UserID);
                        String bookid= F_bookid.getText();

                        Connection connection= connect();

                        try {
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            statement.executeUpdate("UPDATE BOOKS SET RESERVED=0, RESERVED_BY= 0 WHERE BOOKID= "+bookid+";");
                            JOptionPane.showMessageDialog(null,"Reservation cancelled");
                            f.dispose();
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }
                    }
                });

                f.add(create_button);
                f.add(l1);
                f.add(F_bookid);
                f.setSize(400,250);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton create_button= new JButton("Log out");
        create_button.setBounds(420,100,180,25);
        create_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
                JOptionPane.showMessageDialog(null, "Logged out! Please login again to continue");
                f.dispose();

            }
        });


        JButton browse_books= new JButton("Browse Books");
        browse_books.setBounds(20,60,180,25);
        browse_books.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Browse by Author or Genre");

                JRadioButton a1= new JRadioButton("Author");
                a1.setBounds(55,80,200,30);

                JRadioButton a2= new JRadioButton("Genre");
                a2.setBounds(130,80,200,30);

                ButtonGroup bg = new ButtonGroup();
                bg.add(a1);
                bg.add(a2);

                JLabel l1;
                l1= new JLabel("Enter search term");
                l1.setBounds(30,15, 150,30);

                JTextField F_searchBy= new JTextField();
                F_searchBy.setBounds(180,15,200,30);


                JButton browse_button= new JButton("Browse");
                browse_button.setBounds(130,130,100,25);

                browse_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame f= new JFrame("Books Available");

                        Connection connection= connect();
                        String sql=null;
                        String searchBy= F_searchBy.getText();
                        if (a1.isSelected()){
                            sql="select * from BOOKS where AUTHOR='"+searchBy+"'";
                        }
                        else if (a2.isSelected()) {
                            sql="select * from BOOKS where GENRE='"+searchBy+"';";
                        }

                        try{
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            ResultSet result= statement.executeQuery(sql);
                            JTable book_list= new JTable(buildTableModel(result));

                            JScrollPane scrollPane= new JScrollPane(book_list);

                            f.add(scrollPane);
                            f.setSize(800,400);
                            f.setVisible(true);
                            f.setLocationRelativeTo(null);
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }

                    }
                });
                f.add(browse_button);
                f.add(a2);
                f.add(a1);
                f.add(l1);
                f.add(F_searchBy);
                f.setSize(400,200);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton issue_book= new JButton(("Issue Book"));
        issue_book.setBounds(220,60,180,25);
        issue_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter Details");
                JLabel l1,l2,l3,l4;
                l1= new JLabel("Book ID (BOOKID)");
                l1.setBounds(30,15,150,30);

                l3=new JLabel("Period(days)");
                l3.setBounds(30,90,150,30);

                l4= new JLabel("Issued Date(DD-MM-YYY)");
                l4.setBounds(30,127,180,30);

                JTextField F_bookid= new JTextField();
                F_bookid.setBounds(200,15,200,30);

                JTextField F_period= new JTextField();
                F_period.setBounds(200,90,200,30);

                JTextField F_issue= new JTextField();
                F_issue.setBounds(270,130,130,30);

                JButton create_button= new JButton(("Submit"));
                create_button.setBounds(130,170,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int UserID_int= Integer.parseInt(UserID);
                        String bookid= F_bookid.getText();
                        String period= F_period.getText();
                        String issued_date= F_issue.getText();

                        int period_int= Integer.parseInt(period);
                        Connection connection= connect();

                        try {
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            ResultSet resultSet= statement.executeQuery("select ISSUED from BOOKS where BOOKID="+bookid+";");
                            resultSet.next();
                            if(resultSet.getBoolean("ISSUED")==true){
                                JOptionPane.showMessageDialog(null,"Book already issued, kindly place a hold.");
                                f.dispose();
                            }
                            else if (resultSet.getBoolean("ISSUED")==false) {
                                statement.executeUpdate("INSERT INTO ISSUED(USERID, BOOKID, ISSUED_DATE, RETURN_DATE, PERIOD) VALUES('"+UserID_int+"','"+bookid+"','"+issued_date+"', '00-00-0000',"+period_int+")");
                                statement.executeUpdate("UPDATE BOOKS SET ISSUED=1 WHERE BOOKID= "+bookid+";");
                                JOptionPane.showMessageDialog(null,"Book Issued!");
                                f.dispose();
                            }
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }
                    }
                });

                f.add(l3);
                f.add(l4);
                f.add(create_button);
                f.add(l1);
                f.add(F_bookid);
                f.add(F_period);
                f.add(F_issue);
                f.setSize(450,250);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton return_book= new JButton("Return Book");
        return_book.setBounds(420,60,180,25);
        return_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter Details");

                JLabel l1,l2,l3,l4;
                l1= new JLabel("Issue ID (ISSUEID)");
                l1.setBounds(30,15,120,30);

                l2= new JLabel("Book ID (BOOKID)");
                l2.setBounds(30,85,120,30);

                l4= new JLabel("Return Date(DD-MM-YYYY)");
                l4.setBounds(30,50,150,30);

                JTextField F_issueid= new JTextField();
                F_issueid.setBounds(150,15,200,30);

                JTextField F_bookid= new JTextField();
                F_bookid.setBounds(150,85,200,30);

                JTextField F_return= new JTextField();
                F_return.setBounds(220,50,130,30);

                JButton create_button= new JButton("Return");
                create_button.setBounds(130,170,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String issueid= F_issueid.getText();
                        String return_date= F_return.getText();
                        String bookid= F_bookid.getText();

                        Connection connection= connect();

                        try{
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY;");
                            String date1= null;
                            String date2= return_date;

                            ResultSet result= statement.executeQuery("SELECT ISSUED_DATE FROM ISSUED WHERE ISSUEDID= "+issueid+";");
                            while(result.next()){
                                date1= result.getString(1);

                            }
                            try{
                                Date date_1= new SimpleDateFormat("dd-MM-yyyy").parse(date1);
                                Date date_2= new SimpleDateFormat("dd-MM-yyyy").parse(date2);


                                long difference= date_2.getTime()- date_1.getTime();

                                ex.days=(int)(TimeUnit.DAYS.convert(difference,TimeUnit.MILLISECONDS));

                            }
                            catch (ParseException e1){
                                e1.printStackTrace();
                            }

                            statement.executeUpdate("UPDATE ISSUED SET RETURN_DATE='"+return_date+"' WHERE ISSUEDID= "+issueid+";");
                            statement.executeUpdate("UPDATE BOOKS SET ISSUED=0 WHERE BOOKID= "+ bookid +";");
                            f.dispose();

                            Connection connection1= connect();
                            Statement statement1= connection1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement1.executeUpdate("USE LIBRARY");
                            ResultSet result1= statement1.executeQuery("SELECT PERIOD FROM ISSUED WHERE ISSUEDID="+issueid);
                            String difference= null;
                            while (result1.next()){
                                difference= result1.getString(1);
                            }
                            int difference_int= Integer.parseInt(difference);
                            if(ex.days> difference_int){
                                int fine= (ex.days- difference_int)*10;
                                statement1.executeUpdate("UPDATE ISSUED SET FINE="+fine+" WHERE ISSUEDID="+issueid);
                                String fine_str= ("Fine: Rs. "+ fine);
                                JOptionPane.showMessageDialog(null,fine_str);

                            }
                            else if (ex.days<difference_int) {
                                statement1.executeUpdate("UPDATE ISSUED SET FINE= 0 WHERE ISSUEDID= "+ issueid);

                            }

                            JOptionPane.showMessageDialog(null,"Book Returned!");
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }

                    }
                });

                f.add(l4);
                f.add(create_button);
                f.add(l1);
                f.add(l2);
                f.add(F_issueid);
                f.add(F_return);
                f.add(F_bookid);
                f.setSize(350,250);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        f.add(create_button);
        f.add(my_book);
        f.add(view_button);
        f.add(history);
        f.add(reserve_book);
        f.add(cancel_reservation);
        f.add(browse_books);
        f.add(issue_book);
        f.add(return_book);
        f.setSize(620,200);
        f.setLayout(null);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }



    public static void admin_menu(){
        JFrame f= new JFrame("Admin Functions");


        JButton view_button= new JButton("View Books");
        view_button.setBounds(20,20,180,25);
        view_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Books Available");

                Connection connection= connect();
                String sql= "select * from BOOKS";

                try {
                    Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    statement.executeUpdate("USE LIBRARY");
                    statement=connection.createStatement();
                    ResultSet result= statement.executeQuery(sql);
                    MessageFormat header = new MessageFormat("Page {0,number,integer}");

                    JTable book_list= new JTable(buildTableModel(result));

                    JScrollPane scrollPane= new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(1000,400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                }
                catch (SQLException e1){
                    JOptionPane.showMessageDialog(null,e1);
                }
            }
        });


        JButton users_button= new JButton(("View Users"));
        users_button.setBounds(210,20,180,25);
        users_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Users List");

                Connection connection= connect();
                String sql="select* from USERS";

                try {
                    Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    statement.executeUpdate("USE LIBRARY");
                    ResultSet result= statement.executeQuery(sql);
                    JTable book_list= new JTable(buildTableModel(result));

                    JScrollPane scrollPane= new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(1000,400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                }
                catch (SQLException e1){
                    JOptionPane.showMessageDialog(null,e1);
                }
            }
        });


        JButton issue_book= new JButton(("Issue Book"));
        issue_book.setBounds(400,20,180,25);
        issue_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter Details");
                JLabel l1,l2,l3,l4;
                l1= new JLabel("Book ID (BOOKID)");
                l1.setBounds(30,15,150,30);

                l2=new JLabel("User ID (USERID");
                l2.setBounds(30,53,150,30);

                l3=new JLabel("Period(days)");
                l3.setBounds(30,90,150,30);

                l4= new JLabel("Issued Date(DD-MM-YYY)");
                l4.setBounds(30,127,180,30);

                JTextField F_bookid= new JTextField();
                F_bookid.setBounds(200,15,200,30);

                JTextField F_userid= new JTextField();
                F_userid.setBounds(200,53,200,30);

                JTextField F_period= new JTextField();
                F_period.setBounds(200,90,200,30);

                JTextField F_issue= new JTextField();
                F_issue.setBounds(270,130,130,30);

                JButton create_button= new JButton("Submit");
                create_button.setBounds(130,170,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String userid= F_userid.getText();
                        String bookid= F_bookid.getText();
                        String period= F_period.getText();
                        String issued_date= F_issue.getText();

                        int period_int= Integer.parseInt(period);
                        Connection connection= connect();

                        try {
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            ResultSet resultSet= statement.executeQuery("select ISSUED from BOOKS where BOOKID="+bookid+";");
                            resultSet.next();
                            if(resultSet.getBoolean("ISSUED")==false){
                                statement.executeUpdate("INSERT INTO ISSUED(USERID, BOOKID, ISSUED_DATE, RETURN_DATE, PERIOD) VALUES('"+userid+"','"+bookid+"','"+issued_date+"', '00-00-0000',"+period_int+")");
                                statement.executeUpdate("UPDATE BOOKS SET ISSUED=1 WHERE BOOKID= "+bookid+";");
                                JOptionPane.showMessageDialog(null,"Book Issued!");
                                f.dispose();
                            }
                            else if(resultSet.getBoolean("ISSUED")==true){
                                JOptionPane.showMessageDialog(null,"Book already issued, kindly place a hold.");
                                f.dispose();
                            }
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }
                    }
                });

                f.add(l3);
                f.add(l4);
                f.add(create_button);
                f.add(l1);
                f.add(l2);
                f.add(F_userid);
                f.add(F_bookid);
                f.add(F_period);
                f.add(F_issue);
                f.setSize(450,250);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton issued_button= new JButton("View Issued Books");
        issued_button.setBounds(590,20,180,25);
        issued_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Users List");

                Connection connection= connect();
                String sql= "select * from ISSUED where RETURN_DATE= '00-00-0000';";

                try{
                    Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                            ResultSet.CONCUR_READ_ONLY);
                    statement.executeUpdate("USE LIBRARY");
                    ResultSet result= statement.executeQuery(sql);
                    JTable book_list= new JTable(buildTableModel(result));

                    JScrollPane scrollPane= new JScrollPane(book_list);

                    f.add(scrollPane);
                    f.setSize(1000,400);
                    f.setVisible(true);
                    f.setLocationRelativeTo(null);
                }
                catch (SQLException e1){
                    JOptionPane.showMessageDialog(null,e1);
                }
            }
        });


        JButton return_book= new JButton("Return Book");
        return_book.setBounds(20,60,180,25);
        return_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter Details");

                JLabel l1,l2,l3,l4;
                l1= new JLabel("Issue ID (ISSUEID)");
                l1.setBounds(30,15,120,30);

                l2= new JLabel("Book ID (BOOKID)");
                l2.setBounds(30,85,120,30);

                l4= new JLabel("Return Date(DD-MM-YYYY)");
                l4.setBounds(30,50,150,30);

                JTextField F_issueid= new JTextField();
                F_issueid.setBounds(150,15,200,30);

                JTextField F_bookid= new JTextField();
                F_bookid.setBounds(150,85,200,30);

                JTextField F_return= new JTextField();
                F_return.setBounds(220,50,130,30);

                JButton create_button= new JButton("Return");
                create_button.setBounds(130,170,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String issueid= F_issueid.getText();
                        String return_date= F_return.getText();
                        String bookid= F_bookid.getText();

                        Connection connection= connect();

                        try{
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY;");
                            String date1= null;
                            String date2= return_date;

                            ResultSet result= statement.executeQuery("SELECT ISSUED_DATE FROM ISSUED WHERE ISSUEDID= "+issueid+";");
                            while(result.next()){
                                date1= result.getString(1);

                            }
                            try{
                                Date date_1= new SimpleDateFormat("dd-MM-yyyy").parse(date1);
                                Date date_2= new SimpleDateFormat("dd-MM-yyyy").parse(date2);


                                long difference= date_2.getTime()- date_1.getTime();

                                ex.days=(int)(TimeUnit.DAYS.convert(difference,TimeUnit.MILLISECONDS));

                            }
                            catch (ParseException e1){
                                e1.printStackTrace();
                            }

                            statement.executeUpdate("UPDATE ISSUED SET RETURN_DATE='"+return_date+"' WHERE ISSUEDID= "+issueid+";");
                            statement.executeUpdate("UPDATE BOOKS SET ISSUED=0 WHERE BOOKID= "+ bookid +";");
                            f.dispose();

                            Connection connection1= connect();
                            Statement statement1= connection1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement1.executeUpdate("USE LIBRARY");
                            ResultSet result1= statement1.executeQuery("SELECT PERIOD FROM ISSUED WHERE ISSUEDID="+issueid);
                            String difference= null;
                            while (result1.next()){
                                difference= result1.getString(1);
                            }
                            int difference_int= Integer.parseInt(difference);
                            if(ex.days> difference_int){
                                int fine= (ex.days- difference_int)*10;
                                statement1.executeUpdate("UPDATE ISSUED SET FINE="+fine+" WHERE ISSUEDID="+issueid);
                                String fine_str= ("Fine: Rs. "+ fine);
                                JOptionPane.showMessageDialog(null,fine_str);

                            }
                            else if (ex.days<difference_int) {
                                statement1.executeUpdate("UPDATE ISSUED SET FINE= 0 WHERE ISSUEDID= "+ issueid);

                            }

                            JOptionPane.showMessageDialog(null,"Book Returned!");
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }

                    }
                });

                f.add(l4);
                f.add(create_button);
                f.add(l1);
                f.add(l2);
                f.add(F_issueid);
                f.add(F_return);
                f.add(F_bookid);
                f.setSize(350,250);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton browse_books= new JButton("Browse Books");
        browse_books.setBounds(210,60,180,25);
        browse_books.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Browse by Author or Genre");

                JRadioButton a1= new JRadioButton("Author");
                a1.setBounds(55,80,200,30);

                JRadioButton a2= new JRadioButton("Genre");
                a2.setBounds(130,80,200,30);

                ButtonGroup bg = new ButtonGroup();
                bg.add(a1);
                bg.add(a2);

                JLabel l1;
                l1= new JLabel("Enter search term");
                l1.setBounds(30,15, 150,30);

                JTextField F_searchBy= new JTextField();
                F_searchBy.setBounds(180,15,200,30);


                JButton browse_button= new JButton("Browse");
                browse_button.setBounds(130,130,100,25);

                browse_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame f= new JFrame("Books Available");

                        Connection connection= connect();
                        String sql=null;
                        String searchBy= F_searchBy.getText();
                        if (a1.isSelected()){
                            sql="select * from BOOKS where AUTHOR='"+searchBy+"'";
                        }
                        else if (a2.isSelected()) {
                            sql="select * from BOOKS where GENRE='"+searchBy+"';";
                        }

                        try{
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            ResultSet result= statement.executeQuery(sql);
                            JTable book_list= new JTable(buildTableModel(result));

                            JScrollPane scrollPane= new JScrollPane(book_list);

                            f.add(scrollPane);
                            f.setSize(800,400);
                            f.setVisible(true);
                            f.setLocationRelativeTo(null);
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }

                    }
                });
                f.add(browse_button);
                f.add(a2);
                f.add(a1);
                f.add(l1);
                f.add(F_searchBy);
                f.setSize(400,200);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton add_book= new JButton("Add Book");
        add_book.setBounds(400,60,180,25);
        add_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter Book Details");
                JLabel l1,l2,l3,l4;
                l1= new JLabel("Book Name");
                l1.setBounds(30,15,100,30);

                l2=new JLabel("Genre");
                l2.setBounds(30,53,100,30);

                l3= new JLabel("Price");
                l3.setBounds(30,90,100,30);

                l4= new JLabel("Author");
                l4.setBounds(30,130,100,30);

                JTextField F_bookname= new JTextField();
                F_bookname.setBounds(115,15,200,30);

                JTextField F_genre= new JTextField();
                F_genre.setBounds(115,53,200,30);

                JTextField F_price= new JTextField();
                F_price.setBounds(115,90,200,30);

                JTextField F_author= new JTextField();
                F_author.setBounds(115,130,200,30);

                JButton create_button= new JButton("Submit");
                create_button.setBounds(130,170,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String bookname= F_bookname.getText();
                        String genre= F_genre.getText();
                        String price= F_price.getText();
                        String author= F_author.getText();

                        int price_int= Integer.parseInt(price);
                        Connection connection= connect();
                        try{
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            statement.executeUpdate("INSERT INTO BOOKS(BOOKNAME, GENRE, PRICE, AUTHOR, ISSUED, RESERVED, RESERVED_BY) VALUES('"+bookname+"','"+genre+"',"+price_int+",'"+author+"', 0, 0, 0);");
                            JOptionPane.showMessageDialog(null,"Book Added!");
                            f.dispose();
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }
                    }
                });
                f.add(l3);
                f.add(l4);
                f.add(create_button);
                f.add(l1);
                f.add(l2);
                f.add(F_bookname);
                f.add(F_genre);
                f.add(F_price);
                f.add(F_author);
                f.setSize(350,270);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton add_user= new JButton("Add User");
        add_user.setBounds(590,60,180,25);
        add_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Enter User Details");
                JLabel l1,l2;
                l1= new JLabel("Username");
                l1.setBounds(30,15, 100,30);

                l2= new JLabel("Password");
                l2.setBounds(30,50,100,30);

                JTextField F_username= new JTextField();
                F_username.setBounds(110,15,200,30);

                JPasswordField F_password= new JPasswordField();
                F_password.setBounds(110,50,200,30);

                JRadioButton a1= new JRadioButton("Admin");
                a1.setBounds(55,80,200,30);

                JRadioButton a2= new JRadioButton("User");
                a2.setBounds(130,80,200,30);

                ButtonGroup bg = new ButtonGroup();
                bg.add(a1);
                bg.add(a2);

                JButton create_button= new JButton("Create");
                create_button.setBounds(130,130,100,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username= F_username.getText();
                        String password=String.valueOf(F_password.getPassword());
                        Boolean admin= false;

                        if (a1.isSelected()){
                            admin=true;
                        }
                        Connection connection= connect();

                        try {
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY;");
                            statement.executeUpdate("INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) VALUES('"+username+"','"+password+"',"+admin.toString().toUpperCase()+");");
                            JOptionPane.showMessageDialog(null,"User added!");
                            f.dispose();
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }

                    }
                });
                f.add(create_button);
                f.add(a2);
                f.add(a1);
                f.add(l1);
                f.add(l2);
                f.add(F_username);
                f.add(F_password);
                f.setSize(350,200);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton edit_user= new JButton("Edit User Details");
        edit_user.setBounds(20,100,180,25);
        edit_user.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Edit User Details");
                JLabel l1,l2,l3;
                l1= new JLabel("Username");
                l1.setBounds(30,15, 100,30);

                l2= new JLabel("Password");
                l2.setBounds(30,50,100,30);

                l3= new JLabel("UserID");
                l3.setBounds(30,85,100,30);

                JTextField F_username= new JTextField();
                F_username.setBounds(110,15,200,30);

                JPasswordField F_password= new JPasswordField();
                F_password.setBounds(110,50,200,30);

                JTextField F_userid = new JTextField();
                F_userid.setBounds(110, 85, 200,30);

                JButton edit_button= new JButton("Edit");
                edit_button.setBounds(130,130,100,25);
                edit_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String username= F_username.getText();
                        String password=String.valueOf(F_password.getPassword());
                        String userid= F_userid.getText();

                        Connection connection= connect();

                        try {
                            int UserID_int= Integer.parseInt(userid);

                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY;");
                            statement.executeUpdate("UPDATE USERS SET USERNAME= '"+username+"' , PASSWORD= '"+password+"' WHERE USERID= "+UserID_int+";");
                            JOptionPane.showMessageDialog(null,"User details updated!");
                            f.dispose();
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }

                    }
                });
                f.add(edit_button);
                f.add(l1);
                f.add(l2);
                f.add(l3);
                f.add(F_username);
                f.add(F_password);
                f.add(F_userid);
                //f.add(create_button);
                f.setSize(350,200);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton edit_book= new JButton("Edit Book Details");
        edit_book.setBounds(210,100,180,25);
        edit_book.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f= new JFrame("Edit Book Details");
                JLabel l1,l2,l3,l4,l5;
                l1= new JLabel("Book Name");
                l1.setBounds(30,15,100,30);

                l2=new JLabel("Genre");
                l2.setBounds(30,53,100,30);

                l3= new JLabel("Price");
                l3.setBounds(30,90,100,30);

                l4= new JLabel("Author");
                l4.setBounds(30,130,100,30);

                l5= new JLabel("BookID");
                l5.setBounds(30,170,100,30);

                JTextField F_bookname= new JTextField();
                F_bookname.setBounds(115,15,200,30);

                JTextField F_genre= new JTextField();
                F_genre.setBounds(115,53,200,30);

                JTextField F_price= new JTextField();
                F_price.setBounds(115,90,200,30);

                JTextField F_bookid= new JTextField();
                F_bookid.setBounds(115,170,200,30);

                JTextField F_author= new JTextField();
                F_author.setBounds(115,130,200,30);

                JButton create_button= new JButton("Edit");
                create_button.setBounds(130,210,90,25);
                create_button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String bookname= F_bookname.getText();
                        String genre= F_genre.getText();
                        String price= F_price.getText();
                        String bookid= F_bookid.getText();
                        String author= F_author.getText();

                        int price_int= Integer.parseInt(price);
                        Connection connection= connect();
                        try{
                            Statement statement= connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                                    ResultSet.CONCUR_READ_ONLY);
                            statement.executeUpdate("USE LIBRARY");
                            statement.executeUpdate("UPDATE BOOKS SET BOOKNAME= '"+bookname+"', GENRE= '"+genre+"', PRICE="+price_int+", AUTHOR= '"+author+"' WHERE BOOKID="+bookid+";");
                            JOptionPane.showMessageDialog(null,"Book Edited!");
                            f.dispose();
                        }
                        catch (SQLException e1){
                            JOptionPane.showMessageDialog(null,e1);
                        }
                    }
                });
                f.add(l3);
                f.add(l1);
                f.add(l2);
                f.add(l4);
                f.add(l5);
                f.add(create_button);
                f.add(F_bookname);
                f.add(F_genre);
                f.add(F_price);
                f.add(F_bookid);
                f.add(F_author);
                f.setSize(340,300);
                f.setLayout(null);
                f.setVisible(true);
                f.setLocationRelativeTo(null);
            }
        });


        JButton create_button= new JButton("Log out");
        create_button.setBounds(400,100,180,25);
        create_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
                JOptionPane.showMessageDialog(null, "Logged out! Please login again to continue");
                f.dispose();

            }
        });






        f.add(create_button);
        f.add(return_book);
        f.add(issue_book);
        f.add(add_book);
        f.add(add_user);
        f.add(edit_user);
        f.add(edit_book);
        f.add(issued_button);
        f.add(users_button);
        f.add(view_button);
        f.add(browse_books);
        f.setSize(790,250);
        f.setLayout(null);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }
}