package Java.DesignPattern;

import java.util.Arrays;
import java.util.Iterator;
// 学习"设计模式"最重要的还是掌握思想,当然实现能力也是很重要的

public class DP_Iterator {

    public static void main(String[] args) {

        for (int i = 1; i <= 7; i++) {
            System.out.print("Hello ");
        }
        System.out.println();
        /*
        我们先来写一个简单的小程序,实际上就是一个循环:
        我们说,这里的循环变量i的作用抽象化,通用化
        这样形成的模式就称为"Iterator模式"...
        当然我们要说的事情绝对不会是这么简答的,
        虽然Iterator模式是DP中最简单的一种...
         */
        System.out.println("---------------------");
        /*
        我们先来做一件小事情,我们假设这么一个应用场景:
        把书放到书架上,并按照书的名字显示出来...
        为此我们要定义一些类和接口
         */
        BookShelf bookShelf = new BookShelf(4);
        bookShelf.appendBooks(new Book("海底两万里"));
        bookShelf.appendBooks(new Book("罪与罚"));
        bookShelf.appendBooks(new Book("双城记"));
        bookShelf.appendBooks(new Book("泰坦尼克号"));
        // 这个时候我们知道了bookShelf.books实际上就是一个Book数组
        MyIterator myIterator = bookShelf.myiterator();
        // 这个API实际上就是获得了一个MyIterator的实例对象
        // 当然实际上我们也可以用Java提供给我们的Iterator接口...
        // 比如我们重新写一个DP_Iterator2.java尝试一下
        // 只不过我们知道Iterator接口中最重要的方法其实就是hasNext和next
        // 所以这里我们自定义了一个MyIterator的接口其实也是没有关系的...
        while(myIterator.hasNext()) {
            Book book = (Book)myIterator.next();
            System.out.print(book.getName()+" ");
        }
        System.out.println();
        System.out.println("---------------------");
        /*
        当然这只是一个很简答的小程序,但是我们如果能深刻理解,
        就能对设计模式有一些入门的认识,并对Java接口的实现有一些自己的理解
        ------------------
        不过说到底这里我们想要实现遍历打印这件事情,完全可以不用Iterator
        我们有很多直接遍历额方式,比如可以用forEach,
        那么这就是真正值得解答的问题:
            因为Iterator可以将遍历与实现分离开来!
            具体一点,我们看main方法中的while句块,
            其中完全没有用到BookShelf实例,
            一切遍历都是基于MyIterator实现的...
            while循环并不依赖于BookShelf的实现
        那么这一点究竟有什么好处呢?
        如果我们不想用数组,想用List,想用Vector,等等
        只要能够返回Iterator都可以实现遍历,
        当然在这里我们用了MyIterator,在DP_Iterator2.java中,
        我们将getIterator()的实现改成:
        return Arrays.stream(books1).iterator();
        结果是一样的,那么你可以想象,如果下一次是List,是不是也有类似:
        return List.stream(books1).iterator();
        这才是我们说的Iterator设计模式的魅力...
        -------------------
        总结起来说,Iterator设计模式的一大特色就是:
            将遍历功能置于Aggregate角色之外!
        */
        /*
        当然后面我们会对Iterator模式做增强,比如:
        我们想对一个个取出来的元素做操作,
        使其具有递归结构
        与Factory Method向结合
        ...
         */
    }
}

interface Aggregate {
    MyIterator myiterator();
}

interface MyIterator {
    // 接口中的对象或者方法默认public,final,abstract
    boolean hasNext();
    Object next();
}

class Book {

    private String name;

    public Book(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class BookShelf implements Aggregate {
    // 注意我们要理解的最重要的事情其实是为什么这里要实现Aggregate?
    // 或者换句话说,这个myiterator方法究竟有什么用处?

    private Book[] books;
    private int last;

    public BookShelf(int maxsize) {
        this.books = new Book[maxsize];
    }

    public Book getBook(int index) {
        return books[index];
    }

    public void appendBooks(Book book) {
        this.books[last] = book;
        last++;
    }

    public int getLength() {
        return last;
    }

    @Override
    public MyIterator myiterator() {
        return new BookShelfIterator(this);
    }
}

class BookShelfIterator implements MyIterator {

    private BookShelf bookShelf;
    private int index;

    public BookShelfIterator(BookShelf bookShelf) {
        this.bookShelf = bookShelf;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        if (index < bookShelf.getLength()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object next() {
        Book book = bookShelf.getBook(index);
        index++;
        return book;
    }
}