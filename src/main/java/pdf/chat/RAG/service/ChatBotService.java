package pdf.chat.RAG.service;

//                                                    ,--.
//        ,---.  ,-|  /  .-' ,--.--. ,---.  ,--,--. ,-|  |,---. ,--.--. ,--.--.,--,--. ,---.
//        | .-. |' .-. |  `-, |  .--'| .-. :' ,-.  |' .-. | .-. :|  .--' |  .--' ,-.  || .-. |
//        | '-' '\ `-' |  .-' |  |   \   --.\ '-'  |\ `-' \   --.|  |    |  |  \ '-'  |' '-' '
//        |  |-'  `---'`--'   `--'    `----' `--`--' `---' `----'`--'    `--'   `--`--'.`-  /
//        `--'                                                                         `---'


import java.util.List;

import com.mongodb.client.MongoClient;
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
    @Autowired
    private MongoClient mongo;

    public String chat(String query) {
        log.info("Received chat request with query: {}", query);
        String response = chatClient.call(createPrompt(query, dataRetrievalService.searchData(query)));
        log.info("Returning chat response: {}", response);
        return response;
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
    public void load() {
        if(mongoTemplate.getCollection("vector_store").countDocuments()==0) {
            log.info("Loading documents from env variable set path folder location.");
            dataLoaderService.load();
        }else {
            log.info("There are Already files into the Database");
        }
    }

    public void clear() {
        log.info("Clearing all documents from the collection.");
        dataLoaderService.clear();
    }
}
