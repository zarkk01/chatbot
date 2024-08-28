package pdf.chat.RAG.service;

//                                                    ,--.
//        ,---.  ,-|  /  .-' ,--.--. ,---.  ,--,--. ,-|  |,---. ,--.--. ,--.--.,--,--. ,---.
//        | .-. |' .-. |  `-, |  .--'| .-. :' ,-.  |' .-. | .-. :|  .--' |  .--' ,-.  || .-. |
//        | '-' '\ `-' |  .-' |  |   \   --.\ '-'  |\ `-' \   --.|  |    |  |  \ '-'  |' '-' '
//        |  |-'  `---'`--'   `--'    `----' `--`--' `---' `----'`--'    `--'   `--`--'.`-  /
//        `--'                                                                         `---'


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

    // Perform a similarity search on our vector database to return the 5 most relevant Documents.
    // These Documents will later be used to perform the prompt to GPT.
    public List<Document> searchData(String query) {
        log.info("Performing similarity search with query: {}", query);
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.defaults().withQuery(query).withTopK(2));
        log.info("Search returned {} documents", documents.size());
        log.info("Search returned {}", documents);
        return documents;
    }
}
