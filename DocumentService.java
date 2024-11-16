import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    @Value("${file.storage.location}")
    private String storageLocation;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public List<DocumentResponse> saveDocuments(List<MultipartFile> files) throws IOException {
        List<DocumentResponse> responses = new ArrayList<>();

        for (MultipartFile file : files) {
            // Save file to storage
            Path storagePath = Paths.get(storageLocation);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }

            String fileName = file.getOriginalFilename();
            Path filePath = storagePath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Save metadata in database
            Document document = new Document();
            document.setFileName(fileName);
            document.setFileUrl(filePath.toString());
            document.setFileType(file.getContentType());
            documentRepository.save(document);

            // Add to response
            responses.add(new DocumentResponse(fileName, filePath.toString(), file.getContentType()));
        }

        return responses;
    }
}
