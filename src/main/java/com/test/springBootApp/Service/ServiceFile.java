package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Product;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

class ServiceFile {

    private static String safeImage(MultipartFile productImage, String path)  {
        File uploadDir = new File(path);
        String resultFileName;
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String uuidFile = UUID.randomUUID().toString();
        resultFileName = uuidFile + "." + productImage.getOriginalFilename();
        try {
            productImage.transferTo( new File(uploadDir.getAbsolutePath() + File.separator + resultFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultFileName;
    }

    static Boolean deleteImage(Product product, String path) {
        if (product.getProductImageName() != null && !product.getProductImageName().equals("")) {
                try{
                    String imageName = product.getProductImageName();
                    File file = new File(path + File.separator + imageName);
                    if (file.exists())
                       return file.delete();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
        return true;
    }

    public static String validFile(MultipartFile productImage, String path){
        if (productImage.getOriginalFilename().equals("") && productImage.getOriginalFilename().isEmpty()) {
            return "3";
        }
        String fileName = productImage.getOriginalFilename();
        boolean isValid = fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif");
        return isValid ? safeImage(productImage,path) : "3";
    }
}
