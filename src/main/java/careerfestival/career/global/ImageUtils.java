package careerfestival.career.global;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {
    public static File multipartFileToFile(MultipartFile multipartFile) throws IOException {
        // 임시 파일 생성
        File tempFile = File.createTempFile("temp", getFileExtension(multipartFile.getOriginalFilename()));
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

    public static BufferedImage resizeImage(MultipartFile image, int maxWidth, int maxHeight) throws Exception {
        File inputFile = ImageUtils.multipartFileToFile(image);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // 원본 크기
        int originalWidth = inputImage.getWidth();
        int originalHeight = inputImage.getHeight();

        // 비율 계산
        double aspectRatio = (double) originalHeight / originalWidth;
        int targetWidth = maxWidth;
        int targetHeight = maxHeight;

        if (originalWidth > maxWidth || originalHeight > maxHeight) {
            if (originalWidth > originalHeight) {
                // 가로가 더 긴 경우
                targetHeight = (int) (maxWidth * aspectRatio);
            } else {
                // 세로가 더 긴 경우
                targetWidth = (int) (maxHeight / aspectRatio);
            }
        } else {
            return inputImage;
        }

        // 크기 조정
        Image resultingImage = inputImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, inputImage.getType());
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);

        // 임시 파일 삭제
        inputFile.delete();

        return outputImage;
    }

    // 파일 확장자 가져오기
    private static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }
}
