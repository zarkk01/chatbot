package pdf.chat.RAG.service;

//                                                    ,--.
//        ,---.  ,-|  /  .-' ,--.--. ,---.  ,--,--. ,-|  |,---. ,--.--. ,--.--.,--,--. ,---.
//        | .-. |' .-. |  `-, |  .--'| .-. :' ,-.  |' .-. | .-. :|  .--' |  .--' ,-.  || .-. |
//        | '-' '\ `-' |  .-' |  |   \   --.\ '-'  |\ `-' \   --.|  |    |  |  \ '-'  |' '-' '
//        |  |-'  `---'`--'   `--'    `----' `--`--' `---' `----'`--'    `--'   `--`--'.`-  /
//        `--'                                                                         `---'


import java.util.List;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import pdf.chat.RAG.model.QueryRequest;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
@Slf4j
public class ChatBotService {
    @Autowired
    private DataRetrievalService dataRetrievalService;

    @Autowired
    private DataLoaderService dataLoaderService;

    private final ChatClient chatClient;
    private String lastConversationId;

    public ChatBotService(ChatClient.Builder builder) {
        InMemoryChatMemory memory = new InMemoryChatMemory();
        this.chatClient = builder.
                defaultAdvisors(
                        new PromptChatMemoryAdvisor(memory),
                        new MessageChatMemoryAdvisor(memory)
                )
                .build();
    }

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
        QueryRequest queryRequest = new QueryRequest();
        queryRequest.setQuery(query);
        if (lastConversationId == null) {
            queryRequest.setConversationId(UUID.randomUUID().toString());
            lastConversationId = queryRequest.getConversationId();
        } else {
            queryRequest.setConversationId(lastConversationId);
        }

        String prompt = createPrompt(query, dataRetrievalService.searchData(query));
        String response = this.chatClient.prompt().user(prompt)
                .advisors(a -> a
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, queryRequest.getConversationId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call().content();

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