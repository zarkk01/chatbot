package pdf.chat.Vodafone.RAG.service;

import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataLoaderService {

    @Value("classpath:/data/3rdLevel_1.pdf")
    private Resource thirdLevelResource;

    @Value("classpath:/data/Newcomers_1.pdf")
    private Resource newcomersResource;

    @Value("classpath:/data/Shifts_1.pdf")
    private Resource shiftsResource;

    @Value("classpath:/data/combinedAll.pdf")
    private Resource combinedAllResource;

    @Autowired
    private MongoDBAtlasVectorStore vectorStore;

    @Autowired
    private MongoTemplate mongoTemplate;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataLoaderService.class);

    public void load() {
        logger.info("Loading PDF data from resource: {}", newcomersResource);
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(newcomersResource, PdfDocumentReaderConfig.builder().build());
        vectorStore.add(pdfReader.get());
        logger.info("PDF data loaded successfully.");
    }

    public void clear() {
        logger.info("Clearing all documents from the collection.");
        mongoTemplate.getCollection("internal").deleteMany(new Document());
        logger.info("Collection cleared.");
    }
}
