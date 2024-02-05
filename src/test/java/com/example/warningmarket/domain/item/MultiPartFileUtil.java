package com.example.warningmarket.domain.item;

import org.springframework.mock.web.MockMultipartFile;
import java.io.FileInputStream;
import java.io.IOException;

public class MultiPartFileUtil {

    public static MockMultipartFile getMultiPartFile() throws IOException {
        MockMultipartFile file;
        try(FileInputStream fileInputStream = new FileInputStream("src/test/resources/testdata/test.jpeg")) {
            file = new MockMultipartFile("item_images", "test.jpeg", "image/jpeg", fileInputStream);
        }
        return file;
    }
}
