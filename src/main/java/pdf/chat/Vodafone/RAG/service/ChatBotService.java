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
        You're assisting with questions about Vodafone's Confluence / Wiki.
        \s
        Use the information from the DOCUMENTS section to provide accurate answers but act as if you knew this information innately.
        If unsure, simply state that you don't know.
        \s
        This is the question you have to answer based only on the information from DOCUMENTS sections:
        {query}
        \s
        DOCUMENTS:
        {context}
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
    public void load(String file) {
        dataLoaderService.loadWithFile(file);
    }

    public void clear() {
        dataLoaderService.clear();
    }
}
