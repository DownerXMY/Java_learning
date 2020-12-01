package Java.DesignPattern;

import java.util.Arrays;
import java.util.Iterator;

public class DP_Iterator2 {

    public static void main(String[] args) {
        BookShelf1 bookShelf1 = new BookShelf1(3);
        bookShelf1.appendBooks(new Book1("设计模式"));
        bookShelf1.appendBooks(new Book1("深入理解JVM"));
        bookShelf1.appendBooks(new Book1("Java并发"));
        Iterator iterator = bookShelf1.getIterator();
        while (iterator.hasNext()) {
            Book1 book1 = (Book1) iterator.next();
            System.out.print(book1.getName()+" ");
        }
        System.out.println();
    }
}

interface Aggregate1 {
    Iterator getIterator();
}

class Book1 {

    private String name;

    public Book1(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class BookShelf1 implements Aggregate1 {

    private Book1[] book1s;
    private int last;

    public BookShelf1(int maxLength) {
        this.book1s = new Book1[maxLength];
    }

    public Book1 getBook(int index) {
        return book1s[index];
    }

    public void appendBooks(Book1 book1) {
        this.book1s[last] = book1;
        last++;
    }

    public int getLength() {
        return last;
    }

    @Override
    public Iterator getIterator() {
        return new BookShelf1Iterator(this);
    }
}

class BookShelf1Iterator implements Iterator {

    private BookShelf1 bookShelf1;
    private int index;

    public BookShelf1Iterator(BookShelf1 bookShelf1) {
        this.bookShelf1 = bookShelf1;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        if (index < bookShelf1.getLength()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object next() {
        Book1 book1 = bookShelf1.getBook(index);
        index++;
        return book1;
    }
}