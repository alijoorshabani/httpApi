package httpApi.request.imp;

import httpApi.retrofitConnection.ServiceGenerator;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FileTransferTest
{
    public void testFileTransfer()
    {
        String serverUrl = "http://localhost:8081/mrm.txt";

        ServiceGenerator serviceGenerator = new ServiceGenerator();
        TransferFileImpl transferFile = new TransferFileImpl(serviceGenerator);

        File fileToUpload = new File("C:\\Users\\maryam\\Desktop\\mrm.txt");
        File downloadDir = new File("C:\\Users\\maryam\\Desktop\\download");

//        if (!fileToUpload.exists())
//        {
//            throw new RuntimeException("File to upload does not exist: " + fileToUpload.getAbsolutePath());
//        }


        Map<String ,String > requestData  = new HashMap<>();
        requestData.put("mediaType", "text/plain");


        try {
            // Test file upload
            transferFile.uploadFileToUrl(fileToUpload, "http://localhost:8081", new HashMap<>());
            // Download the file
            // transferFile.downloadFileFromUrl(downloadDir, serverUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void main(String[] args)
    {

        FileTransferTest fileTransferTest = new FileTransferTest();
        fileTransferTest.testFileTransfer();

    }
}
