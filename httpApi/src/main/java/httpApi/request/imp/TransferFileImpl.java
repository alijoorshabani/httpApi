package httpApi.request.imp;

import httpApi.request.contract.TransferFile;
import httpApi.request.retrofitCalls.FileApiService;
import httpApi.retrofitConnection.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.net.URL;
import java.util.Map;

@Service
public class TransferFileImpl implements TransferFile
{
    @Autowired
    private final ServiceGenerator serviceGenerator;


    @Autowired
    public TransferFileImpl(ServiceGenerator serviceGenerator)
    {
        this.serviceGenerator = serviceGenerator;
    }


    @Override
    public boolean downloadFileFromUrl(File systemPath, String url)
    {
        try
        {
            URL urlObj = new URL(url);
            String baseUrl = urlObj.getProtocol() + "://" + urlObj.getHost();

            if (urlObj.getPort() != -1)
            {
                baseUrl += ":" + urlObj.getPort();
            }

            String path = urlObj.getFile().substring(1); // Removing leading slash
            FileApiService fileApiService = serviceGenerator.createService(baseUrl, FileApiService.class);
            Response<ResponseBody> response = fileApiService.downloadFile(path).execute();

            if (response.isSuccessful() && response.body() != null)
            {
                if (!systemPath.exists())
                {
                    systemPath.mkdir();
                }
                // Save the file to disk
                File outputFile = new File(systemPath, path.substring(path.lastIndexOf('/') + 1));
                try (FileOutputStream fos = new FileOutputStream(outputFile))
                {
                    fos.write(response.body().bytes());
                    System.out.println("The file was downloaded successfully");
                }
                return true;
            }
            else
            {
                System.err.println("Failed to download file. Response code: " + response.code());
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException("Error downloading file", e);
        }
        return false;
    }


//    // Method to download a file from a local directory
//    @Override
//    public boolean downloadFile(File sourceFile, File destinationDirectory)
//    {
//        if (!sourceFile.exists() || !sourceFile.isFile())
//        {
//            System.err.println("resource file does not exist or not a file:  " + sourceFile.getAbsolutePath());
//            return false;
//        }
//
//        if (!destinationDirectory.exists())
//        {
//            destinationDirectory.mkdirs();
//        }
//
//        File destinationFile = new File(destinationDirectory, sourceFile.getName());
//
//        try (InputStream in = new FileInputStream(sourceFile); OutputStream out = new FileOutputStream(destinationFile))
//        {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            while ((bytesRead = in.read(buffer)) != -1)
//            {
//                out.write(buffer, 0, bytesRead);
//            }
//
//            System.out.println("File downloaded successfully to: " + destinationFile.getAbsolutePath());
//            return true;
//        }
//        catch (IOException e)
//        {
//            return false;
//        }
//    }


    @Override
    public void uploadFileToUrl(File sourceFile, String url, Map requestData)
    {
        FileApiService fileApiService = serviceGenerator.createService(url, FileApiService.class);

        /**
         * Prepares the file to be uploaded as a part of the multipart request.
         *
         * @param requestData A map containing request-specific data. This includes:
         *                    <ul>
         *                      <li>"mediaType": The media type of the file to be uploaded, e.g., "text/plain" or "image/jpeg".
         *                      If not provided, defaults to "multipart/form-data".</li>
         *                    </ul>
         * @param sourceFile The file to be uploaded.
         *
         * @return A {@link MultipartBody.Part} instance that contains the file data, which can be used in a multipart request.
         */
        MultipartBody.Part body = MultipartBody.Part.createFormData("file",
                sourceFile.getName(),
                RequestBody.create(MediaType.parse(String.valueOf(requestData.getOrDefault("mediaType", "multipart/form-data"))), sourceFile));

        Call<ResponseBody> call = fileApiService.uploadFile(requestData, body);

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
            {
                if (response.isSuccessful())
                {
                    System.out.println("File uploaded successfully.");
                }
                else
                {
                    System.out.println("Failed to upload file. Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable)
            {
                System.err.println("Failed to execute request: " + throwable.getMessage());
            }
        });
    }

//    @Override
//    public void uploadFile(File sourceFile, File destinationDirectory)
//    {
//        if (!sourceFile.exists() || !sourceFile.isFile())
//        {
//            System.err.println("Source file does not exist or is not a file: " + sourceFile.getAbsolutePath());
//            return;
//        }
//
//        if (!destinationDirectory.exists())
//        {
//            destinationDirectory.mkdir();
//        }
//
//        File destinationFile = new File(destinationDirectory, sourceFile.getName());
//
//        try (InputStream in = new FileInputStream(sourceFile);
//             OutputStream out = new FileOutputStream(destinationFile))
//        {
//            byte[] buffer = new byte[1024];
//            int bytesRead;
//
//            while ((bytesRead = in.read(buffer)) != -1)
//            {
//                out.write(buffer, 0, bytesRead);
//            }
//            System.out.println("File uploaded successfully to: " + destinationFile.getAbsolutePath());
//        }
//        catch (IOException e)
//        {
//            throw new RuntimeException(e);
//        }
//    }


}
