package pdf.chat.Vodafone.RAG.service;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataRetrievalService {
    org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DataRetrievalService.class);

    @Autowired
    private MongoDBAtlasVectorStore vectorStore;

    // Perform a simility search on our vector database so to return the 5 most relevant Documents. On these Documents
    // we will later perform the prompt to GPT.
    public List<Document> searchData(String query) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.defaults().withQuery(query).withTopK(5));
        logger.info(documents.toString());
        return documents;
    }
}
