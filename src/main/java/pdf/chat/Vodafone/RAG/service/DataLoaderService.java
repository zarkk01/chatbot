package pdf.chat.Vodafone.RAG.service;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class DataLoaderService {
    // Here we specify which pdf will be used.
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
    public void load(String file) {
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(file, PdfDocumentReaderConfig.builder().build());
        vectorStore.add(pdfReader.get());
    }
    public void load() {
        Resource[] resources = { thirdLevelResource, newcomersResource, shiftsResource };
        for (Resource resource : resources) {
            var config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(3)
                            .withNumberOfTopPagesToSkipBeforeDelete(1)
                            .build())
                    .withPagesPerDocument(1)
                    .build();

            var pdfReader = new PagePdfDocumentReader(resource, config);
            var textSplitter = new TokenTextSplitter();
            vectorStore.accept(textSplitter.apply(pdfReader.get()));
        }
    }

    // Clear all pdfs from the collection.
    public void clear() {
        mongoTemplate.getCollection("internal").deleteMany(new Document());
    }
}