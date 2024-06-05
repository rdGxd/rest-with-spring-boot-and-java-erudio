package org.example.restwithspringbootandjavaerudio.services;

import lombok.Getter;
import lombok.Setter;
import org.example.restwithspringbootandjavaerudio.config.FileStorageConfig;
import org.example.restwithspringbootandjavaerudio.exceptions.FileStorageException;
import org.example.restwithspringbootandjavaerudio.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
@Getter
@Setter
public class FileStorageService {
    // Quando até a pasta onde os arquivos serão armazenados
    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry filename contains invalid path sequence " + fileName);
            }
            // Para salvar na nuvem ou banco de dados mude essas duas linhas
            Path targetLocation = this.fileStorageLocation.resolve(fileName); // 1
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING); // 2
            return fileName;
        } catch (Exception ex) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename() + "Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) return resource;
            else throw new MyFileNotFoundException("File not found " + filename);
            
        } catch (Exception ex) {
            throw new MyFileNotFoundException("File not found" + filename, ex);
        }
    }
}
