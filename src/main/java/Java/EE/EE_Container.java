package Java.EE;
/*
什么叫做Java EE容器?
Java EE中容器的概念很不好理解,它是指组件和底层服务之间的接口,
容器提供了在编译或者部署的时候选择程序行为的能力,
这种能力是通过部署描述文件实现的,用定义好的XML文件来描述组件的行为
Java EE标准定义了四种容器:
    EJB容器:管理其中运行的EJB
    Web容器:管理其中运行的JSP或者Servlet组件
    应用客户端容器:管理其中运行的Client组件
    Applet容器:管理Applet的运行
当然厂商一般会将Web容器和EJB容器集成在一起提供自己的Java EE服务器,
市面上比较常用的服务器包括:WebLogic,Application,Server,IBM,
Apache Tomcat等等,我们下面来介绍着这最后一种:
*/

public class EE_Container {
    public static void main(String[] args) {
        System.out.println("Hello world...");
    }
}
