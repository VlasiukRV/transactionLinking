package vr.com.apps.utility.resourceReader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceReader {

    protected ResourceProperty requestProperty = new ResourceProperty();
    protected Map<Object, Object> responseProperty = new HashMap<>();

    protected boolean resourceExist = false;
    protected byte[] rez = new byte[0];

    public abstract void makeConnection() throws IOException;
    public abstract void read()throws IOException;

    public boolean exists(){
        return resourceExist;
    }

    public void resetRequestProperty() {
        requestProperty.clear();
    }

    public void setRequestProperty(ResourceProperty requestProperty){
        this.requestProperty = requestProperty;
    }

    public void setRequestProperty(String key, String value) {
        if (key == null)
            throw new NullPointerException ("key is null");
        requestProperty.put(key, value);
    }

    public Object getRequestProperty(String key) {
        if (key == null)
            throw new NullPointerException ("key is null");
        if (!requestProperty.containsKey(key))
            throw new NullPointerException("no such key \"" +key+ "\"");
        return requestProperty.get(key);
    }

    public void resetResponseProperty() {
        responseProperty.clear();
    }

    public Object getResponseProperty(String key) {
        if (key == null)
            throw new NullPointerException ("key is null");
        if (!requestProperty.containsKey(key))
            throw new NullPointerException("no such key \"" +key+ "\"");
        return requestProperty.get(key);
    }

    public byte getByteArray()[]{
        return Arrays.copyOf(rez, rez.length);
    }

    public String getString(){
        return getString("UTF-8");
    }

    public String getString(String charsetName) {
        try {
            return new String(rez, 0, rez.length, charsetName);
        }catch (UnsupportedEncodingException e){
            System.out.println("UnsupportedEncodingException " +charsetName);
            return "";
        }
    }

    protected void setResponseProperty(Object key, Object value){
        if(key == null)
            throw new NullPointerException("key is null");
        responseProperty.put(key, value);
    }

}
