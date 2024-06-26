package com.spotit.backend.storage;

import org.springframework.stereotype.Service;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.GetFileListRequest;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.models.results.ResultList;

@Service
public class StorageServiceImpl implements StorageService {

    private final ImageKit imageKit;

    public StorageServiceImpl(ImageKit imageKit) {
        this.imageKit = imageKit;
    }

    public static String getFilenameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    @Override
    public String uploadFile(byte[] file, String directory, String filename) {
        try {
            FileCreateRequest fileCreateRequest = new FileCreateRequest(file, filename);
            fileCreateRequest.setFolder(directory);
            fileCreateRequest.setUseUniqueFileName(true);

            Result result = imageKit.upload(fileCreateRequest);

            return result.getUrl();
        } catch (Exception e) {
            e.printStackTrace();

            throw new ErrorUploadingFileException(filename);
        }
    }

    @Override
    public void deleteFile(String filename) {
        try {
            GetFileListRequest getFileListRequest = new GetFileListRequest();
            getFileListRequest.setSearchQuery("name = " + filename);
            ResultList result = imageKit.getFileList(getFileListRequest);

            if (result.getResults().isEmpty()) {
                return;
            }

            String fileID = result.getResults().get(0).getFileId();

            imageKit.deleteFile(fileID);
        } catch (Exception e) {
            e.printStackTrace();

            throw new ErrorDeletingFileException(filename);
        }
    }
}
