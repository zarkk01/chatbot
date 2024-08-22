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

    private final String PROMPT_BLUEPRINT = """
        This is the provided context :
        {context}
        
        Answer me this query based only on the provided context : 
        {query}
        
        If you don't have an answer based on the provided context, just answer "I don't have an answer based on this context." and stop there.
    """;

    public String chat(String query) {
        return chatClient.call(createPrompt(query, dataRetrievalService.searchData(query)));
    }

    private String createPrompt(String query, List<Document> context) {
        PromptTemplate promptTemplate = new PromptTemplate(PROMPT_BLUEPRINT);
        promptTemplate.add("query", query);
        promptTemplate.add("context", context);
        return promptTemplate.render();
    }

    public void load() {
        dataLoaderService.load();
    }

    public void clear() {
        dataLoaderService.clear();
    }
}
