package connection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfig {

    private  static Properties dbConfig;

    static {
         try {
            dbConfig = new Properties();
             InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("dbconfig.properties");
//            InputStream inputStream = new FileInputStream("src/dbconfig.properties");
            dbConfig.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getConfig(String key){
        return dbConfig.getProperty(key,"");
    }
    public static String getConfig(String key,String defaultValue){
        return dbConfig.getProperty(key,defaultValue);
    }
    public static Integer getIntegerValue(String key){
        return Integer.parseInt(dbConfig.getProperty(key,"2"));
    }
    public static Integer getIntegerValue(String key,String defaultValue){
        return Integer.parseInt(dbConfig.getProperty(key,defaultValue));
    }
}
