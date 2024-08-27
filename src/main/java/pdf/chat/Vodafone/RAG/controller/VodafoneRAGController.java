package pdf.chat.Vodafone.RAG.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pdf.chat.Vodafone.RAG.service.ChatBotService;

@RestController
public class VodafoneRAGController {

    private static final Logger logger = LoggerFactory.getLogger(VodafoneRAGController.class);

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

    // http://localhost:8080/load
    @PostMapping("/load")
    public void load(@RequestParam(name = "file", required = false) String file) {
        if (file == null || file.isEmpty()) {
            logger.info("Received load request with no file. Loading default.");
            chatBotService.load();
        } else {
            logger.info("Received load request with file: {}", file);
            chatBotService.load(file);
        }
        logger.info("Load process completed.");
    }

    // http://localhost:8080/clear
    @DeleteMapping("/clear")
    public void clear() {
        logger.info("Received clear request.");
        chatBotService.clear();
        logger.info("Clear process completed.");
    }
}
