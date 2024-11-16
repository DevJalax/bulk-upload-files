import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/upload")
    public ResponseEntity<List<DocumentResponse>> uploadDocuments(
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        List<DocumentResponse> responses = documentService.saveDocuments(files);
        return ResponseEntity.ok(responses);
    }
}
