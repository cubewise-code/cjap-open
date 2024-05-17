package OpenCJAPAuth.utils;

import com.ibm.db.util.PropertiesLoader;

public class FileProperties {
    private static FileProperties fp = new FileProperties();
    private PropertiesLoader bootProps = null;

    private FileProperties(){
        try {
            bootProps = PropertiesLoader.getLoader("conf.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static FileProperties getInstance(){
        return fp;
    }

    public String getValue(String key){
        return bootProps.getProperty(key);
    }
}
