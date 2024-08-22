package pdf.chat.Vodafone.RAG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pdf.chat.Vodafone.RAG.service.ChatBotService;

@RestController
public class VodafoneRAGController {
    @Autowired
    private ChatBotService chatBotService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VodafoneRAGController.class);

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query") String query) {
        logger.info("Received chat request with query: {}", query);
        String response = chatBotService.chat(query);
        logger.info("Response sent: {}", response);
        return response;
    }

    @PostMapping("/load")
    public void load() {
        logger.info("Received load request.");
        chatBotService.load();
        logger.info("Load request processed.");
    }

    @PostMapping("/clear")
    public void clear() {
        logger.info("Received clear request.");
        chatBotService.clear();
        logger.info("Clear request processed.");
    }
}
