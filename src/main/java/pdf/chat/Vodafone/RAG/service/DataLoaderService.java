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

    @Autowired
    public MongoDBAtlasVectorStore vectorStore;

    @Autowired
    private MongoTemplate mongoTemplate;

    // Load pdf from the pdfResource in DB.
    public void load(String file) {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(file, PdfDocumentReaderConfig.builder().build());
        vectorStore.add(pdfReader.get());
    }

    // Clear all pdfs from the collection.
    public void clear() {
        mongoTemplate.getCollection("vector_store").deleteMany(new Document());
    }
}