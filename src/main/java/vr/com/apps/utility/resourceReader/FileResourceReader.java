package vr.com.apps.utility.resourceReader;

import java.io.*;

public class FileResourceReader extends ResourceReader {

    private String fileAddress;

    @Override
    public void makeConnection() throws IOException {

        fileAddress = (String)getRequestProperty("$FileAddress");
        setResourceExists();
        if (!resourceExist){
            throw new IOException("File \"" +fileAddress+ "\" not exist");
        }

        File file = new File(fileAddress);
        setResponseProperty("length", file.length());

    }

    @Override
    public void read() throws IOException {
        if (!resourceExist){
            throw new IOException("File \"" +fileAddress+ "\" not exist");
        }

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (FileInputStream in = new FileInputStream(fileAddress)) {
                byte[] buf = new byte[1024];
                for (int read; (read = in.read(buf)) != -1; ) {
                    bos.write(buf, 0, read);
                }
                rez = bos.toByteArray();
            }
        } catch (IOException e) {
            System.out.println("Error read file " + fileAddress);
            throw new IOException(e);
        }
    }

    private void setResourceExists(){
        File file = new File(fileAddress);
        resourceExist = true;
        if (!file.exists() && !file.canRead() && !file.isFile()){
            resourceExist = false;
        }
    }

}
