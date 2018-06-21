package com.test.springBootApp.Service;

import com.test.springBootApp.Entity.Product;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

class ServiceFile {

    static String safeImage(MultipartFile productImage, String path)  {
        if (ServiceFile.validFile(productImage)) {
            File uploadDir = new File(path);
            String resultFileName;
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            if (!productImage.getOriginalFilename().equals("") && !productImage.getOriginalFilename().isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                resultFileName = uuidFile + "." + productImage.getOriginalFilename();
                try {
                    productImage.transferTo( new File(uploadDir.getAbsolutePath() + File.separator + resultFileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return resultFileName;
            }
        }
        return null;
    }

    static boolean deleteImage(Product product, String path) {
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

    private static boolean validFile(MultipartFile productImage){
        String fileName = productImage.getOriginalFilename();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".gif");
    }
}
