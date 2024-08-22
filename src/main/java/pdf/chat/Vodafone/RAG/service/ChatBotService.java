package pdf.chat.Vodafone.RAG.service;

import java.util.List;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatBotService {
    @Autowired
    private ChatModel chatClient;

    @Autowired
    private DataRetrievalService dataRetrievalService;

    @Autowired
    private DataLoaderService dataLoaderService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(ChatBotService.class);

    private final String PROMPT_BLUEPRINT = """
        This is the provided context :
        {context}
        
        Answer me this query based only on
        {query}
    """;

    public String chat(String query) {
        logger.info("Received chat query: {}", query);
        List<Document> context = dataRetrievalService.searchData(query);
        String prompt = createPrompt(query, context);
        logger.debug("Generated prompt: {}", prompt);
        String response = chatClient.call(prompt);
        logger.info("Chat response: {}", response);
        return response;
    }

    private String createPrompt(String query, List<Document> context) {
        logger.debug("Creating prompt with query: {} and context: {}", query, context);
        PromptTemplate promptTemplate = new PromptTemplate(PROMPT_BLUEPRINT);
        promptTemplate.add("query", query);
        promptTemplate.add("context", context);
        return promptTemplate.render();
    }

    public void load() {
        logger.info("Starting to load data.");
        dataLoaderService.load();
        logger.info("Data loading completed.");
    }

    public void clear() {
        logger.info("Starting to clear data.");
        dataLoaderService.clear();
        logger.info("Data clearing completed.");
    }
}
