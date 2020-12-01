package Java.DesignPattern;

import java.util.Iterator;

public class AbstractFactoryApp2 {

    public static void main(String[] args) {
        System.out.println("This is an instance...");
    }
}

class ListFactory extends AbstractFactory {

    @Override
    public Link createLink(String caption, String url) {
        return new ListLink(caption,url);
    }

    @Override
    public Tray createTray(String caption) {
        return new ListTray(caption);
    }

    @Override
    public Page createPage(String title, String author) {
        return new ListPage(title,author);
    }
}

class ListLink extends Link {

    public ListLink(String caption,String url) {
        super(caption,url);
    }

    @Override
    public String makeHTML() {
        return "<li><a href=\""+url+"\">"+caption+"</a></li>\n";
    }
}

class ListTray extends Tray {

    public ListTray(String caption) {
        super(caption);
    }

    @Override
    public String makeHTML() {
        StringBuffer stringBuffer =
                new StringBuffer();
        stringBuffer.append("<li>\n");
        stringBuffer.append(caption+"\n");
        stringBuffer.append("<ul>\n");
        Iterator iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            stringBuffer.append(item.makeHTML());
        }
        stringBuffer.append("</ul>\n");
        stringBuffer.append("</li>\n");
        return stringBuffer.toString();
    }
}

class ListPage extends Page {

    public ListPage(String title,String author) {
        super(title,author);
    }

    @Override
    public String makeHTML() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("<html><head><title>"+title+"</title></head>\n");
        stringBuffer.append("<body>\n");
        stringBuffer.append("<h1>"+title+"</h1>\n");
        stringBuffer.append("<u1>\n");
        Iterator iterator = content.iterator();
        while (iterator.hasNext()) {
            Item item = (Item) iterator.next();
            stringBuffer.append(item.makeHTML());
        }
        stringBuffer.append("</u1>");
        stringBuffer.append("<hr><address>"+author+"</author>");
        stringBuffer.append("</body></html>\n");
        return stringBuffer.toString();
    }
}