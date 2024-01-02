package com.spotit.backend.storage.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.dockerjava.api.exception.BadRequestException;
import com.spotit.backend.storage.ErrorDeletingFileException;
import com.spotit.backend.storage.ErrorUploadingFileException;
import com.spotit.backend.storage.StorageServiceImpl;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.BaseFile;
import io.imagekit.sdk.models.results.Result;
import io.imagekit.sdk.models.results.ResultList;

@ExtendWith(MockitoExtension.class)
class StorageServiceTest {

    @Mock
    ImageKit imageKit;

    @InjectMocks
    StorageServiceImpl storageServiceImpl;

    @Test
    void shouldReturnFilenameFromUrl() {
        // given
        var fileUrl = "***REMOVED***/flags/english-flag";

        // when
        var result = StorageServiceImpl.getFilenameFromUrl(fileUrl);

        // then
        assertEquals("english-flag", result);
    }

    @Test
    void shouldReturnUrlOfUploadedFile() throws Exception {
        // given
        var file = new byte[0];
        var directory = "directory";
        var filename = "filename";
        var url = "***REMOVED***/" + directory + "/" + filename;

        var mockedResult = new Result();
        mockedResult.setUrl(url);

        when(imageKit.upload(any())).thenReturn(mockedResult);

        // when
        var result = storageServiceImpl.uploadFile(file, directory, filename);

        // then
        verify(imageKit, times(1)).upload(any());

        assertEquals(url, result);
    }

    @Test
    void shouldReturnErrorWhenErrorUploadingFile() throws Exception {
        // given
        var file = new byte[0];
        var directory = "directory";
        var filename = "filename";

        when(imageKit.upload(any())).thenThrow(BadRequestException.class);

        // when
        var result = assertThrows(
                ErrorUploadingFileException.class,
                () -> storageServiceImpl.uploadFile(file, directory, filename));

        // then
        verify(imageKit, times(1)).upload(any());

        assertEquals("Error when uploading file '" + filename + "'!", result.getMessage());
    }

    @Test
    void shouldDeleteFile() throws Exception {
        // given
        var filename = "filename";
        var mockedFoundFile = new BaseFile();
        mockedFoundFile.setFileId("fileID");

        var mockedResultList = new ResultList();
        mockedResultList.setResults(List.of(mockedFoundFile));

        when(imageKit.getFileList(any())).thenReturn(mockedResultList);

        // when
        storageServiceImpl.deleteFile(filename);

        // then
        verify(imageKit, times(1)).getFileList(any());
        verify(imageKit, times(1)).deleteFile("fileID");
    }

    @Test
    void shouldReturnErrorWhenDeletingFile() throws Exception {
        // given
        var filename = "filename";
        var mockedFoundFile = new BaseFile();
        mockedFoundFile.setFileId("fileID");

        var mockedResultList = new ResultList();
        mockedResultList.setResults(List.of(mockedFoundFile));

        when(imageKit.getFileList(any())).thenReturn(mockedResultList);
        when(imageKit.deleteFile("fileID")).thenThrow(BadRequestException.class);

        // when
        var result = assertThrows(
                ErrorDeletingFileException.class,
                () -> storageServiceImpl.deleteFile(filename));

        // then
        verify(imageKit, times(1)).getFileList(any());
        verify(imageKit, times(1)).deleteFile("fileID");

        assertEquals("Error while deleting file '" + filename + "'!", result.getMessage());
    }

    @Test
    void shouldReturnWhenFileNotFound() throws Exception {
        // given
        var filename = "filename";
        var mockedResultList = new ResultList();
        mockedResultList.setResults(List.of());

        when(imageKit.getFileList(any())).thenReturn(mockedResultList);

        // when
        storageServiceImpl.deleteFile(filename);

        // then
        verify(imageKit, times(1)).getFileList(any());
        verify(imageKit, times(0)).deleteFile("fileID");
    }
}
