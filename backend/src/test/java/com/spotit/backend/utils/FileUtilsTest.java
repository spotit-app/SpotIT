package com.spotit.backend.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class FileUtilsTest {

    @Mock
    MockMultipartFile file;

    @Test
    void testGetBytesFromFileWithoutError() throws IOException {
        // given
        var bytes = "content".getBytes();
        when(file.getBytes()).thenReturn(bytes);

        // when
        var result = FileUtils.getBytesFromFile(file);

        // then
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(bytes[i], result[i]);
        }
    }

    @Test
    void testGetBytesFromFileWithError() throws IOException {
        // given
        when(file.getBytes()).thenThrow(IOException.class);

        // when
        var result = FileUtils.getBytesFromFile(file);

        // then
        assertNull(result);
    }
}
