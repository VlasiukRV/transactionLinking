package vr.com.apps.transactionLinking.service;

import vr.com.apps.utility.resourceReader.ResourceProperty;

import java.io.IOException;
import java.util.ArrayList;

public abstract class ResourceList<T> extends ArrayList<T> {
    protected ResourceProperty resourceProperty;

    public ResourceList(){
        super();
    }

    public ResourceList(ResourceProperty resourceProperty){
        super();
        setResourceProperty(resourceProperty);
    }

    public void setResourceProperty(ResourceProperty resourceProperty) {
        this.resourceProperty = resourceProperty;
    }

    public void setRequestProperty(String key, String value) throws NullPointerException{
        resourceProperty.setRequestProperty(key, value);
    }

    public void downloadResource() throws IOException{
        clear();
    }
}
