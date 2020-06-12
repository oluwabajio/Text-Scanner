package phone.number.scanner.utils;

import java.util.regex.Pattern;

public class AppUtil {

    static SharedPrefManager sharedPrefManager;

   private AppUtil(){

   }

    public static SharedPrefManager getSharedPrefInstance() {

        if (sharedPrefManager == null) {
            sharedPrefManager = new SharedPrefManager();
        }
        return sharedPrefManager;
    }

    public static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
}
