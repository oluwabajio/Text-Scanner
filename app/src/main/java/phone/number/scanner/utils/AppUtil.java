package phone.number.scanner.utils;

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
}
