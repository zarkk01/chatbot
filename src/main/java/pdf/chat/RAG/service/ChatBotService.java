package pdf.chat.RAG.service;

import java.net.MalformedURLException;
import java.util.List;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.*;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class ChatBotService {
    private final String collection = System.getenv("COLLECTION_NAME");

    @Autowired
    private ChatModel chatClient;

    @Autowired
    private DataRetrievalService dataRetrievalService;

    @Autowired
    private DataLoaderService dataLoaderService;

    @Autowired
    private MongoTemplate mongoTemplate;

    private final String PROMPT_BLUEPRINT = """
        You're assisting with questions about a Company's Confluence / Wiki.
        
        Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
        If unsure, simply state that you don't know.
        
        This is the question you have to answer based only on the information from DOCUMENTS sections:
        {query}
        
        DOCUMENTS:
        {context}
    """;

    public String chat(String query) {
        log.info("Received chat request with query: {}", query);
        return chatClient.call(createPrompt(query, dataRetrievalService.searchData(query)));
    }

    public Flux<String> chatStream(String query) {
        log.info("Received chat stream request with query: {}", query);
        var context = dataRetrievalService.searchData(query);
        String prompt = createPrompt(query, context);
        return chatClient.stream(prompt);
    }

    private String createPrompt(String query, List<Document> context) {
        log.debug("Creating prompt with query: {} and context: {}", query, context);
        PromptTemplate promptTemplate = new PromptTemplate(PROMPT_BLUEPRINT);
        promptTemplate.add("query", query);
        promptTemplate.add("context", context);
        String renderedPrompt = promptTemplate.render();
        log.debug("Rendered prompt: {}", renderedPrompt);
        return renderedPrompt;
    }

//    TODO implement logic to check which file are already inserted into DB and which are not
    public void load() throws MalformedURLException {
        if (mongoTemplate.getCollection(collection).countDocuments() == 0) {
            log.info("Loading documents from env variable set path folder location.");
            dataLoaderService.load();
        } else {
            log.info("There are already files into the Database.");
        }
    }

    public void load(String file) throws MalformedURLException {
        log.info("Loading documents from specified file: {}", file);
        if (file == null || file.isEmpty()) {
            log.info("Loading documents from whatever is in env variable.");
            dataLoaderService.load();
        } else {
            log.info("Loading documents from specified file: {}", file);
            dataLoaderService.load(file);
        }
    }

    public void clear() {
        log.info("Clearing all documents from the collection.");
        dataLoaderService.clear();
    }
}
