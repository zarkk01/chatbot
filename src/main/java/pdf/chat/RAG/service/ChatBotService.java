package pdf.chat.RAG.service;

import java.util.List;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChatBotService {

    @Autowired
    private ChatModel chatClient;

    @Autowired
    private DataRetrievalService dataRetrievalService;

    @Autowired
    private DataLoaderService dataLoaderService;

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
        String response = chatClient.call(createPrompt(query, dataRetrievalService.searchData(query)));
        log.info("Returning chat response: {}", response);
        return response;
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

    public void load() {
        log.info("Loading documents from env variable set path folder location.");
        dataLoaderService.load();
    }

    public void clear() {
        log.info("Clearing all documents from the collection.");
        dataLoaderService.clear();
    }
}
