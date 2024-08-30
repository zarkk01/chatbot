package pdf.chat.RAG.service;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import static org.springframework.util.ResourceUtils.getFile;

@Service
@Slf4j
public class DataLoaderService {
    private final String collection = System.getenv("COLLECTION_NAME");

    @Autowired
    public MongoDBAtlasVectorStore vectorStore;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void load() throws MalformedURLException {
        log.info("Loading PDFs from default classpath.");
        load("");
    }

    // Load PDFs either from specified file from http(s) or from the docs folder in the resources.
    public void load(String file) throws MalformedURLException {
        Resource[] resources = file.isEmpty()
                ? folderLoader()
                : new Resource[]{new UrlResource(file)};

        for (Resource resource : resources) {
            log.debug("Processing PDF resource: {}", resource.getFilename());

            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build())
                    .withPagesPerDocument(1)
                    .build();

            var pdfReader = new PagePdfDocumentReader(resource, config);
            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));

            log.info("Successfully processed and stored resource: {}", resource.getFilename());

            if(file.isEmpty()) {
                deleteFile(resource);
            }
        }
    }

    // Helper method to convert files from Resource to File type and then delete them
    private void deleteFile(Resource resource) {
        try {
            File myFile = getFile(resource.getURI());
            myFile.delete();

            log.info("Successfully deleted file: {}", myFile.getCanonicalPath());
        } catch (IOException e) {
            log.error("Can not delete file that does not exist");
        }
    }

    // Find all PDF files from given folder path.
    private Resource[] folderLoader() {
        log.info("Loading all PDF files from folder: {}", "src/main/resources/docs");

        File folder = new File("src/main/resources/docs");
        File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        assert pdfFiles != null;
        Resource[] resources = new Resource[pdfFiles.length];

        for (int i = 0; i < pdfFiles.length; i++) {
            resources[i] = new FileSystemResource(pdfFiles[i]);

            log.debug("Found PDF file: {}", pdfFiles[i].getName());
        }

        return resources;
    }

    // Clear all PDFs from the collection.
    public void clear() {
        log.info("Clearing all documents from collection: {}", collection);

        mongoTemplate.getCollection(collection).deleteMany(new Document());

        log.info("All documents cleared from collection: {}", collection);
    }
}
