# Library Management System for IGDTUW: Project Submission for Equinox 

# The BiblioStead
## Your one stop destination for all your bookish needs— A Library management system for IGDTUW

In this project, I've used Java, Java Swing, and MySQL for creating a CRUD application for managing a library. It can access a database with multiple tables, and be used for issuing, returning, and reserving books, as well as adding, editing and deleting users as well as books.

I did not know how to use Swing or MySQL before this project, but I learnt a lot in the course of this project. I used Java Swing for the graphic user interface as I was able to find preexsting projects using it. 

I faced various challenges in the making of this project, especially because I was very new to the technologies used. I've detailed some of the major ones below:
* I had initially thought to use the reference material provided and completely skip out on using any database, but then I realised I was making things complicated unnecessarily. Since I had no idea how to use databases or MySQL in Java, I referred to preexisting projects on the internet, but soon discovered many things included in those would not work, ex. using the `rs2xml.jar` file: I didn't know what exactly it was and any secure site to download it from so I decided to completely forgo that approach and instead use: `JTable book_list= new JTable(buildTableModel(result)); JScrollPane scrollPane= new JScrollPane(book_list);`

* Another thing I had no idea of was how to populate the 'JTable' using 'resultSet'. That I found on StackOverflow: https://stackoverflow.com/questions/10620448/how-to-populate-jtable-from-resultset/10625471#10625471

* I was unable to verify passwords that were entered at the time of login to those stored in the database. For this, I took guidance from a mentor who suggested: `String myPass=String.valueOf(passwordField.getPassword());` for taking the password as a string rather then a `char[]`. There is however a downside to this approach, as the `getText()` field is deprecated for passwords in Java because a `String` is immutable and thus can pose a security risk. Given more time, I would've liked to have come up with a way to resolve this. https://stackoverflow.com/questions/10443308/why-gettext-in-jpasswordfield-was-deprecated

* I was unable to retrieve a specific value in a cell from the database using java and swing, then after consulting StackOverflow I understood that I should use the `resultSet.getBoolean()` for applying the `==true` condition, rather than the `resultSet.next()`, which is used for something completely different. I found out that when I consulted the oracle docs online at https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html#retrieve_rs

* Since I was on a time crunch due to prior commitments, I was unable to experiment with adding images to the GUI; I had wanted to add the logo on every page, as well as improve the layout.

## Installing and running this project:
* Requires Java
* Add `mysql:mysql-connector-java:8.0.30` library using maven in your IDE (I used IntelliJ IDEA)
* Download the code
* Add a LIBRARY in MySQL using MariaDB (in my case on Linux)
* To this library, add tables by the name of BOOKS, USERS, ISSUED, into these tables add columns as detailed below.
```
# code block
CREATE DATABASE LIBRARY

USE LIBRARY

CREATE TABLE USERS(USERID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERNAME VARCHAR(30), ADMIN BOOLEAN)

INSERT INTO USERS(USERNAME, PASSWORD, ADMIN) VALUES('admin', 'admin', TRUE)

CREATE TABLE BOOKS(BOOKID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, BOOKNAME VARCHAR(50), GENRE VARCHAR(20), PRICE INT, )

CREATE TABLE ISSUED (ISSUEDID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, USERID INT, BOOKID INT, ISSUED_DATE VARCHAR(20), RETURN_DATE VARCHAR(20), PERIOD INT, FINE INT)

INSERT INTO BOOKS(BOOKNAME, GENRE, PRICE) VALUES('War and Peace', 'Mystery', 200), ('The Guest Book', Fiction', 300), ('The Perfect Murder', 'Mystery', 150), ('Accidental Presidents', 'Biography', 250), ('The Wicked King', 'Fiction', 350)`

```
Note: Every line is a separate command
Note: I later added columns `AUTHOR`, `ISSUED`, `RESERVED`, `RESERVED_BY` to `TABLE BOOKS`, with fields being `VARCHAR`, `BOOLEAN`, `BOOLEAN`, `INT` respectively and initialised them all to NULL and 0.
* Now you can start executing

* The default admin username and password are admin and admin respectively. For the default user passwords, I used user1 and user1. A UserID can be created using the admin login.
* All the funcitionalities are demonstrated in the Slide Deck I have linked below.

### Screenshots of this project can be found in the Google Drive given below


# Links:
* [Slide Deck](https://www.canva.com/design/DAFdkoNVn7Y/z_OQwQUmefcsawv4uSbC9w/view?utm_content=DAFdkoNVn7Y&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton)
* [Video Demonstration](https://www.loom.com/share/085e9c2a85eb433a9c42109cdabcc990)
* [Google Drive](https://drive.google.com/drive/folders/1WPIs3opVLeR53DpDK8XRSmIEAOdKOkvD?usp=share_link)
