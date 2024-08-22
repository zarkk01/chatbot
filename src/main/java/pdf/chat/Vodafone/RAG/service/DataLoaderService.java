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

    // Here we specify which pdf will be used. a
    @Value("classpath:/data/3rdLevel_1.pdf")
    private Resource thirdLevelResource;

    @Value("classpath:/data/Newcomers_1.pdf")
    private Resource newcomersResource;

    @Value("classpath:/data/Shifts_1.pdf")
    private Resource shiftsResource;

    @Value("classpath:/data/combinedAll.pdf")
    private Resource combinedAllResource;

    @Autowired
    public MongoDBAtlasVectorStore vectorStore;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Load pdf from the pdfResource in DB.
    public void load() {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(shiftsResource, PdfDocumentReaderConfig.builder().build());
        vectorStore.add(pdfReader.get());
    }

    // Clear all pdfs from the collection.
    public void clear() {
        mongoTemplate.getCollection("vector_store").deleteMany(new Document());
    }
}