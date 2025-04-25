package com.repsy.main.service;

import com.repsy.main.entity.FileMetadata;
import com.repsy.main.repository.FileMetadataRepository;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class FileMetadataService {

    private final FileMetadataRepository repository;

    @Value("${storage.file.baseDir}")
    private String uploadDir; // Dosyaların yükleneceği yerel dizin

    @Value("${storage.strategy}")
    private String storageStrategy;

    @Value("${minio.endpoint}")
    private String minioEndpoint;

    @Value("${minio.accessKey}")
    private String minioAccessKey;

    @Value("${minio.secretKey}")
    private String minioSecretKey;

    @Value("${minio.bucketName}")
    private String minioBucketName;

    private MinioClient minioClient;

    public FileMetadataService(FileMetadataRepository repository) {
        this.repository = repository;
    }

    @PostConstruct
    public void initMinioClient() {
        this.minioClient = MinioClient.builder()
                .endpoint(minioEndpoint)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
    }

    // Dosya yükleme ve metadata kaydetme işlemi
    public void saveFileMetadata(MultipartFile file) throws IOException, MinioException {
        String fileName = file.getOriginalFilename();
        long fileSize = file.getSize();
        String fileType = file.getContentType();
        String checksum = calculateChecksum(file);

        // Depolama stratejisini kontrol et
        if ("local".equals(storageStrategy)) {
            // Lokal dosya sistemi kullanılıyorsa
            Path path = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), path);
            FileMetadata fileMetadata = new FileMetadata(fileName, path.toString(), fileSize, fileType, checksum);
            repository.save(fileMetadata);
        } else if ("object".equals(storageStrategy)) {
            // MinIO kullanılıyorsa
            // MinIO'ya yükleme yap
            try (var inputStream = file.getInputStream()) {
                // PutObjectArgs ile parametreleri ileterek dosya yükleme işlemi
                PutObjectArgs args = PutObjectArgs.builder()
                        .bucket(minioBucketName)
                        .object(fileName)
                        .stream(inputStream, fileSize, -1)  // -1 son parametre contentType olmadan
                        .contentType(fileType)
                        .build();

                minioClient.putObject(args); // PutObjectArgs parametresi ile dosyayı yükle
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            } catch (InvalidKeyException e) {
                throw new RuntimeException(e);
            }
            String fileUrl = minioEndpoint + "/" + minioBucketName + "/" + fileName;
            FileMetadata fileMetadata = new FileMetadata(fileName, fileUrl, fileSize, fileType, checksum);
            repository.save(fileMetadata);
        }
    }

    // MD5 checksum hesaplama
    public String calculateChecksum(MultipartFile file) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] fileBytes = file.getBytes();
            if (fileBytes.length == 0) {
                System.out.println("Dosya boş, checksum hesaplanamıyor!");
            } else {
                System.out.println("Dosya boyutu: " + fileBytes.length);
            }

            byte[] hashBytes = digest.digest(fileBytes);
            StringBuilder hexString = new StringBuilder();

            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }

            System.out.println("Checksum: " + hexString.toString());
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Error while calculating checksum", e);
        }
    }

    public List<FileMetadata> getAllFileMetadata() {
        return repository.findAll();
    }

    public boolean deleteFileMetadata(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public FileMetadata getFileMetadataById(Long id) {
        return repository.findById(id).orElse(null);
    }

}
