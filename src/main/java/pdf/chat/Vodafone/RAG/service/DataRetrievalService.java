package pdf.chat.Vodafone.RAG.service;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataRetrievalService {
    @Autowired
    private MongoDBAtlasVectorStore vectorStore;

    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataRetrievalService.class);

    public List<Document> searchData(String query) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.defaults().withQuery(query).withTopK(2).withSimilarityThreshold(0.7));
        logger.info(documents.toString());
        return documents;
    }
}


