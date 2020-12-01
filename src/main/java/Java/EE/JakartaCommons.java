package Java.EE;

import org.apache.commons.lang3.StringUtils;

public class JakartaCommons {

    public static void main(String[] args) {
        /*
        例如有一个带输入框的窗体,用户在输入框中输入许可证秘钥,
        希望允许输入1111-JAVA格式的秘钥,那么我们就要做一些必要的检查:
        具体的检查过程就体现在下面checkLicenseKey()方法中:
         */
        boolean result1 = checkLicenseKey("1111-JAVA");
        boolean result2 = checkLicenseKey("1111-LOVE");
        System.out.println(result1+" "+result2);
    }

    public static boolean checkLicenseKey (String key) {

        if (StringUtils.isBlank(key)) {
            return false;
        }

        key = StringUtils.deleteWhitespace(key);

        String[] keySplit =
                StringUtils.split(key,"-");
        if (keySplit.length != 2
        || keySplit[0].length() != 4
        || keySplit[1].length() != 4) {
            return false;
        }

        if (! StringUtils.isNumeric(keySplit[0])) {
            return false;
        }

        if (! StringUtils.containsOnly(keySplit[1],
                new char[]{'J','A','V','A'})) {
            return false;
        }

        return true;
    }
}
