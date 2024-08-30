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
import java.net.MalformedURLException;

@RestController
public class RAGController {
    private static final Logger logger = LoggerFactory.getLogger(RAGController.class);

    @Autowired
    private ChatBotService chatBotService;

    // http://localhost:8080/chat?query=What is the phone number of Nikolas Kiamilis?
    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query") String query) {
        logger.info("Received chat request with query: {}", query);
        String response = chatBotService.chat(query);
        logger.info("Returning chat response: {}", response);
        return response;
    }

    // http://localhost:8080/chat/stream?query=What is the phone number of Nikolas Kiamilis?
    @GetMapping(value = "/chat/stream", produces = "text/event-stream")
    public Flux<String> chatStream(@RequestParam(name = "query") String query) {
        return chatBotService.chatStream(query);
    }

    // http://localhost:8080/load
    @PostConstruct()
    public void load() throws MalformedURLException {
        logger.info("Received load request.");
        chatBotService.load();
        logger.info("Load process completed.");
    }

    // http://localhost:8080/loadHttp?file=https://www.newitalianbooks.it/wp-content/uploads/2020/05/CristianoRonaldo.pdf
    @PostMapping("/loadHttp")
    public void loadHttp(@RequestParam(name = "file") String file) throws MalformedURLException {
        logger.info("Received load request for file.");
        chatBotService.load(file);
        logger.info("Load process completed for file.");
    }

    // http://localhost:8080/clear
    @PostMapping("/clear")
    public void clear() {
        logger.info("Received clear request.");
        chatBotService.clear();
        logger.info("Clear process completed.");
    }
}
