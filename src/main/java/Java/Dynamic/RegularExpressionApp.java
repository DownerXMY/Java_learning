package Java.Dynamic;
/*
正则表达式:
正则表达式的作用就是提升文本处理的表达能力,我们也是列举出它的基本要点:
1.正则表达式是一串字符,它描述了一个文本模式,可以帮助我们方便地处理文本.
2.正则表达式中的字符有两类:
    一种是普通字符,也就是匹配字符本身,
    另外一种是元字符,往往具有特殊含义
3.大部分语言都是支持正则表达式的,只不过语法体系都不大相同
4.正则表达式的不同部分:
    普通单个字符:就是用字符本身表示
    特殊字符:比如"\t","\n",...
    八进制表示的字符:以\0开头,后跟1~3位数字,比如\0141
    十六进制表示的字符:以\x开头,后面跟两位字符,比如\x6A
    斜杠:斜杠\本身是一个元字符,如果想要匹配它本身,那么要用两个斜杠"\\"
    元字符本身:元字符有很多,比较常用的包括: . * ? +
        如果想要匹配元字符本身,需要在前面加上斜杠
5.量词通常是用 ？ + * 三个字符来表示的
    + 表示其前面的字符可能一次出现或者多次出现
    * 表示其前面的字符可能零次出现或者多次出现
    ? 表示其前面的字符可能出现也可能不出现
    贪婪机制:
        <a>.*</a>
        这里就有一个问题: * 的匹配机制究竟是包含了多少内容呢？
        如果不加说明,那么它是贪婪的,
        换句话说 * 的作用范围包含了<a>和</a>之间的所有内容
6.()表示分组,分组可以嵌套,分组默认有一个编号,
    我们已经知道[]表示匹配其中的任意一个元素,
    那么(XXX1|XXX2|XXX3)表示匹配其中的任意一个子表达式
7.当然正则表达式的语法是很多的,
    比如"-"表示范围,"^"表示排除,[]表示匹配其中的任意字符,...
    再比如还有什么特殊边界匹配,环视边界匹配,转义与匹配模式,...
    显然全部列举出来是不现实的,
    所以我们还是从实际应用中去看比较合理
 */

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressionApp {

    public void demo1() {
        String regex = "<(\\w+)>(.*)</\\1>";
        // 正则表达式可以被编译成一个Pattern对象
        // 这里我们要强调一点,
        // 那即是Pattern对象与处理的具体文本对象没有关系,可以安全的被多线程共享
        Pattern pattern = Pattern.compile(regex);
        // 也可以指定匹配模式:单行模式(点号模式),多行模式,大小写无关模式,...
        Pattern pattern1 = Pattern.compile(regex,Pattern.DOTALL);
        Pattern pattern2 = Pattern.compile(regex,Pattern.MULTILINE);
        Pattern pattern3 = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        // 在"LITERAL"模式下,元字符将看做普通字符,从而失去其特殊含义
        Pattern pattern4 = Pattern.compile(regex,Pattern.LITERAL);
        // 当然我们有一种方法.quote()能够达到和"LITERAL"类似的效果
        Pattern pattern5 = Pattern.compile(Pattern.quote(regex));
    }

    public void demo2() {
        // 字符串的分隔也是有大讲究的
        String str1 = "abc def hello.\n world";
        // 注意"/s"变化的是 \t\n\x0B\f\r
        String[] strings1 = str1.split("[\\s.]+");
        System.out.println(Arrays.toString(strings1));
        String str2 = ",abc,,def,,";
        String[] strings2 = str2.split(",");
        System.out.println(Arrays.toString(strings2));
        // 值得了解的是Pattern也有split方法
        // 然后Pattern的split方法接受的参数是CharSequence,
        // 更加的通用,因为我们知道String,StringBuffer,StringBuilder等等都实现了该接口
        String str3 = "abc,def,hello";
        Pattern pattern = Pattern.compile(",");
        String[] strings3 = pattern.split(str3);
        System.out.println(Arrays.toString(strings3));
        // 注意是将"分隔符regex"编译成Pattern,然后split()中传入的是带分隔的对象
    }

    public void demo3() {
        // 验证输入文本是不是完整匹配预定义正则表达式
        // 换句话说就是给出的字符串是不是满足正则表达式提供的模式
        String regex = "\\d{8}";
        String str1 = "12345678";
        System.out.println(str1.matches(regex));
        System.out.println("--------------------");
        // 查找和正则表达式匹配的字符组对应的位置
        String regex2 = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(regex2);
        String str2 = "today is 2020-10-21, yesterday is 2020-10-20";
        Matcher matcher = pattern.matcher(str2);
        while (matcher.find()) {
            System.out.println("find "+matcher.group()+
                    " position: "+matcher.start()+"-"+matcher.end());
        }
    }

    public static void main(String[] args) {
        RegularExpressionApp regularExpressionApp =
                new RegularExpressionApp();
        regularExpressionApp.demo1();
        System.out.println("--------------------");
        regularExpressionApp.demo2();
        System.out.println("--------------------");
        regularExpressionApp.demo3();
        // 当然正则表达式的内容还有很多,等到了真正要用的时候,再去学习比较合适...
    }
}
