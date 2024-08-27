package pdf.chat.Vodafone.RAG.service;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class DataLoaderService {
    private final String collection = System.getenv("COLLECTION_NAME");
    private final String folder_path = System.getenv("FOLDER_PATH");
    
    @Autowired
    public MongoDBAtlasVectorStore vectorStore;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Load PDFs from our hard coded classpath.
    public void load() {
        load("");
    }

    // Load PDFs, flexible, from either hard coded classpath or user specified local path.
    public void load(String file) {
        // If an absolute path to a PDF has been given, load this. If not, load every PDF in selected folder.
        Resource[] resources = file.isEmpty()
                ? folderLoader()
                : new Resource[]{new FileSystemResource(file)};

        for (Resource resource : resources) {
            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().build())
                    .withPagesPerDocument(1)
                    .build();

            var pdfReader = new PagePdfDocumentReader(resource, config);
            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));
        }
    }

    // Find all PDF files from given folder path.
    private Resource[] folderLoader() {
        File folder = new File(folder_path);
        File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
        Resource[] resources = new Resource[pdfFiles.length];
        for (int i = 0; i < pdfFiles.length; i++) {
            resources[i] = new FileSystemResource(pdfFiles[i]);
        }
        return resources;
    }

    // Clear all PDFs from the collection.
    public void clear() {
        mongoTemplate.getCollection(collection).deleteMany(new Document());
    }
}