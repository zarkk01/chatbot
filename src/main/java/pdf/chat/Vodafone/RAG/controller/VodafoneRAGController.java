package pdf.chat.Vodafone.RAG.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pdf.chat.Vodafone.RAG.service.ChatBotService;

@RestController
public class VodafoneRAGController {
    @Autowired
    private ChatBotService chatBotService;

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(VodafoneRAGController.class);

    //https:localhost:8080/chat?query=what is the github account of ecommerce department
    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query") String query) {
        logger.info("Received chat request with query: {}", query);
        String response = chatBotService.chat(query);
        logger.info("Response sent: {}", response);
        return response;
    }

    //https:localhost:8080/load
    @PostMapping("/load")
    public ResponseEntity<String> load() {
        try {
            chatBotService.load();
            logger.info("Load request processed.");
            return ResponseEntity.ok("Chat loaded successfully");
        } catch (Exception e) {
            logger.error("Failed to load chat data", e);
            return ResponseEntity.status(500).body("Failed to load chat data");
        }
    }

    //https:localhost:8080/clear
    @DeleteMapping("/clear")
    public ResponseEntity<String> clear() {
        try {
            chatBotService.clear();
            logger.info("Clear request processed.");
        } catch (Exception e) {
            logger.error("Failed to clear chat data", e);
            return ResponseEntity.status(500).body("Failed to clear chat data");
        }
    }
}
