package tmall.util;

import org.springframework.web.multipart.MultipartFile;

public class UploadImage {

    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
