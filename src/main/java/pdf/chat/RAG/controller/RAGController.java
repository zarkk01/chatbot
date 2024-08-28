package pdf.chat.RAG.controller;

//                                                    ,--.
//        ,---.  ,-|  /  .-' ,--.--. ,---.  ,--,--. ,-|  |,---. ,--.--. ,--.--.,--,--. ,---.
//        | .-. |' .-. |  `-, |  .--'| .-. :' ,-.  |' .-. | .-. :|  .--' |  .--' ,-.  || .-. |
//        | '-' '\ `-' |  .-' |  |   \   --.\ '-'  |\ `-' \   --.|  |    |  |  \ '-'  |' '-' '
//        |  |-'  `---'`--'   `--'    `----' `--`--' `---' `----'`--'    `--'   `--`--'.`-  /
//        `--'                                                                         `---'

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pdf.chat.RAG.service.ChatBotService;
import reactor.core.publisher.Flux;

@RestController
public class RAGController {

    private static final Logger logger = LoggerFactory.getLogger(RAGController.class);

    @Autowired
    private ChatBotService chatBotService;

    // http://localhost:8080/chat?query=What is the github organization of ecom?
    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query") String query) {
        logger.info("Received chat request with query: {}", query);
        String response = chatBotService.chat(query);
        logger.info("Returning chat response: {}", response);
        return response;
    }
    @GetMapping(value = "/chat/stream", produces = "text/event-stream")
    public Flux<String> chatStream(@RequestParam(name = "query") String query) {
        return chatBotService.chatStream(query);
    }
    // http://localhost:8080/load
    //check if db is empty before loading data  done
    //TODO: POST request should be implemented with http client
    //TODO: the endpoint "load Document" will use a POST request with http and post a pdf to get tokenized kai loaded to db
    //replace @PostMapping with @PostConstruct  done
    @PostConstruct()
    public void load() {
        logger.info("Received load request.");
        chatBotService.load();
        logger.info("Load process completed.");
    }

    // http://localhost:8080/clear
    @PostMapping("/clear")
    public void clear() {
        logger.info("Received clear request.");
        chatBotService.clear();
        logger.info("Clear process completed.");
    }

//    TODO: delete pdf files after being inserted into DB
//    TODO: use in memory(explore)
//    TODO: implement function calling
//    TODO: fix Streaming in response

}
