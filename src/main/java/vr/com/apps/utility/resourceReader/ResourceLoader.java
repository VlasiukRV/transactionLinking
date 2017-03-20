package vr.com.apps.utility.resourceReader;

import java.io.IOException;

public class ResourceLoader {

    public static ResourceReader getResource(ResourceProperty resourceProperty) throws IOException{
        ResourceReader resourceReader = null;
        if (resourceProperty.getResourceType() == ResourceType.FILE) {
            resourceReader = new FileResourceReader();
        }

        if (resourceReader != null) {
            resourceReader.setRequestProperty(resourceProperty);
            try {
                resourceReader.makeConnection();
                resourceReader.read();
            }catch (IOException e){
                throw new IOException(e);
            }

        }

        return resourceReader;
    }
}
