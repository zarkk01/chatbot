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

    // http://localhost:8080/chat?query=What is the github organization of ecom?
    @GetMapping("/chat")
    public String chat(@RequestParam(name = "query") String query) {
        return chatBotService.chat(query);
    }

    // http://localhost:8080/load
    @PostMapping("/load")
    public void load(@RequestParam(name = "file", required = false) String file) {
        if (file == null || file.isEmpty()) {
            chatBotService.load();
        } else {
            chatBotService.load(file);
        }
    }
    // http://localhost:8080/clear
    @PostMapping("/clear")
    public void clear() {
        chatBotService.clear();
    }
}