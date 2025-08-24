package com.ComNCheck.ComNCheck.domain.global.config.gcp;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ImageManager {

    private final String bucketName;
    private final Storage storage;

    // ★ 2. @RequiredArgsConstructor 대신 생성자를 직접 만들어 생성자 주입을 사용합니다.
    public ImageManager(@Value("${spring.cloud.gcp.storage.bucket}") String bucketName, Storage storage) {
        this.bucketName = bucketName;
        this.storage = storage;
    }

    public List<String> uploadImagesToGcs(List<MultipartFile> images) {
        if (images == null || images.isEmpty()) {
            return Collections.emptyList();
        }

        // ★ 3. for문을 stream API로 변경하여 가독성과 간결성을 높입니다.
        return images.stream()
                .map(this::uploadImage) // 각 파일을 개별 업로드 메소드에 매핑
                .collect(Collectors.toList());
    }

    // 단일 파일을 업로드하는 private 메소드로 로직을 분리하여 재사용성을 높입니다.
    private String uploadImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            // ★ 1. [핵심] UUID와 원본 파일명을 조합하여 고유한 파일 이름 생성 (확장자 유지)
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, fileName)
                            .setContentType(file.getContentType())
                            .build(),
                    file.getInputStream()
            );

            log.info("이미지 업로드 성공: {}", fileName);

            // 생성된 전체 파일 이름으로 URL을 만듭니다.
            return "https://storage.googleapis.com/" + bucketName + "/" + fileName;

        } catch (IOException e) {
            log.error("GCS 이미지 업로드 실패: {}", file.getOriginalFilename(), e);
            throw new RuntimeException("이미지 업로드 실패", e);
        }
    }

    // --- 삭제 로직은 이미 훌륭하게 작성되어 있으므로 그대로 유지합니다. ---
    public void deleteImagesFromGcs(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }
        imageUrls.forEach(this::deleteImage);
    }

    private void deleteImage(String imageUrl) {
        try {
            String blobName = extractBlobNameFromUrl(imageUrl);
            BlobId blobId = BlobId.of(bucketName, blobName);
            boolean deleted = storage.delete(blobId);

            if (deleted) {
                log.info("GCS 파일 삭제 성공: {}", blobName);
            } else {
                log.warn("GCS 파일 삭제 실패 (파일이 존재하지 않음): {}", blobName);
            }
        } catch (Exception e) {
            log.error("GCS 파일 삭제 중 오류 발생: {}", imageUrl, e);
        }
    }

    private String extractBlobNameFromUrl(String imageUrl) {
        String prefix = "https://storage.googleapis.com/" + bucketName + "/";
        if (!imageUrl.startsWith(prefix)) {
            throw new IllegalArgumentException("올바른 GCS URL 형식이 아닙니다: " + imageUrl);
        }
        return imageUrl.substring(prefix.length());
    }
}