package pdf.chat.RAG.service;

import java.util.List;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.MongoDBAtlasVectorStore;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DataRetrievalService {

    @Autowired
    private MongoDBAtlasVectorStore vectorStore;

    /**
     * Performs a similarity search on the vector database to find the most relevant documents for a given query.
     * This method queries the vector store and retrieves the top 5 documents that are most similar to the query.
     *
     * @param query The search query for which similar documents are to be retrieved.
     * @return A list of the top 5 most relevant {@link Document} objects based on the query.
     */
    public List<Document> searchData(String query) {
        log.info("DataRetrievalService::searchData - Performing similarity search with query: {}", query);
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.defaults().withQuery(query).withTopK(5));
        log.info("DataRetrievalService::searchData - Search returned {}", documents);
        return documents;
    }
}
