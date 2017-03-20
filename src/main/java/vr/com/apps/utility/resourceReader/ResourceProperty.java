package vr.com.apps.utility.resourceReader;

import java.util.HashMap;

public class ResourceProperty extends HashMap<String, Object>{

    private ResourceType resourceType;

    public ResourceProperty(){

    }

    public ResourceProperty(ResourceType resourceType){

        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public void setRequestProperty(String key, String value) throws NullPointerException{
        if (key == null)
            throw new NullPointerException ("key is null");
        put(key, value);
    }

    public Object getRequestProperty(String key) throws NullPointerException{
        if (key == null)
            throw new NullPointerException ("key is null");
        if (!containsKey(key))
            throw new NullPointerException("no such key \"" +key+ "\"");
        return get(key);
    }
}
