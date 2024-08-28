package pdf.chat.RAG.service;

//                                                    ,--.
//        ,---.  ,-|  /  .-' ,--.--. ,---.  ,--,--. ,-|  |,---. ,--.--. ,--.--.,--,--. ,---.
//        | .-. |' .-. |  `-, |  .--'| .-. :' ,-.  |' .-. | .-. :|  .--' |  .--' ,-.  || .-. |
//        | '-' '\ `-' |  .-' |  |   \   --.\ '-'  |\ `-' \   --.|  |    |  |  \ '-'  |' '-' '
//        |  |-'  `---'`--'   `--'    `----' `--`--' `---' `----'`--'    `--'   `--`--'.`-  /
//        `--'                                                                         `---'


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
import lombok.extern.slf4j.Slf4j;
import java.io.File;

@Service
@Slf4j
public class DataLoaderService {
    private final String collection = System.getenv("COLLECTION_NAME");

    @Autowired
    public MongoDBAtlasVectorStore vectorStore;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Load PDFs from folder specified in FOLDER_PATH env variable.
    public void load() {
        Resource[] resources = folderLoader();

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
        }
    }

    // Find all PDF files from given folder path.
    private Resource[] folderLoader() {
        log.info("Loading all PDF files from folder: {}", "src/main/resources/docs");

        File folder = new File("src/main/resources/docs");
        File[] pdfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".pdf"));
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
