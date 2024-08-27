package pdf.chat.Vodafone.RAG.service;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataLoaderService {
    // Here we specify which pdf will be used.
    @Value("${pdf.ThirdLevel.path}")
    private Resource thirdLevelResource;

    @Value("${pdf.Newcomers.path}")
    private Resource newcomersResource;

    @Value("${pdf.Shifts.path}")
    private Resource shiftsResource;

    @Value("${pdf.CombinedAll.path}")
    private Resource combinedAllResource;

    private final String collection = System.getenv("COLLECTION_NAME");

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
        Resource[] resources = file.isEmpty()
                ? new Resource[]{thirdLevelResource, newcomersResource , shiftsResource}
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

    // Clear all PDFs from the collection.
    public void clear() {
        mongoTemplate.getCollection(collection).deleteMany(new Document());
    }
}