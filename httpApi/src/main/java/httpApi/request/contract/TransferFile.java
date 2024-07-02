package httpApi.request.contract;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.util.Map;

public interface TransferFile
{

    boolean downloadFileFromUrl(File systemPath, String url);

//    BufferedOutputStream downloadFile(String filePath);

    void uploadFileToUrl(File sourceFile, String url, Map requestData);

//    void uploadFile(String filePath, BufferedInputStream bufferedInputStream);

}
