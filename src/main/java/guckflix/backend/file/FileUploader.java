package guckflix.backend.file;

import guckflix.backend.exception.RuntimeIOException;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 파일 업로드 클래스
 */
public class FileUploader {

    public String upload(MultipartFile file, String directory, String extention) {

        String uuid = UUID.randomUUID().toString() + extention;
        /** Try-with-resources */
        try (FileOutputStream fos = new FileOutputStream(FileConst.IMAGE_DIRECTORY_ROOT + "/" + directory + "/" + uuid)){
            fos.write(file.getBytes());
        } catch (IOException e){
             throw new RuntimeIOException("upload error");
        }
        return uuid;
    }

    public void delete(String directory, String deleteFileName){
        Path filePath = Paths.get(FileConst.IMAGE_DIRECTORY_ROOT + "/" + directory + "/" + deleteFileName);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
