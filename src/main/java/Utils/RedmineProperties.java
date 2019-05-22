package Utils;

import java.util.Properties;

public class RedmineProperties {

    private static Properties instance;

    public static Properties getProperties(){
        return instance;
    }

    public RedmineProperties(String baseURL, String token){
        instance = new Properties();
        instance.setProperty(Constants.BASE_URL, baseURL);
        instance.setProperty(Constants.TOKEN, token);
    }

}
