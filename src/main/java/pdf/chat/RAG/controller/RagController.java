package pdf.chat.RAG.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pdf.chat.RAG.service.ChatbotService;
import reactor.core.publisher.Flux;
import java.net.MalformedURLException;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class RagController {

    @Autowired
    private ChatbotService chatBotService;

    /**
     * Initializes the controller by loading initial data.
     * This method runs after the bean's properties have been set.
     *
     * @throws MalformedURLException if the data source URL is malformed but during init NO URL will be given,
     * so it's okay.
     */
    @PostConstruct
    public void init() throws MalformedURLException {
        log.info("RAGController::init - Received load request.");
        chatBotService.load();
        log.info("RAGController::init - Load process completed.");
    }

    /**
     * Handles a chat request with a given query.
     * Example GET request with query : http://localhost:8080/chat?query=What is the
     * phone number of PERSON?
     *
     * @param query the query to be processed by our chat service
     * @return the chat response
     */
    @GetMapping("/chat")
    public ResponseEntity<String> chat(@RequestParam(name = "query") String query) {
        log.info("RAGController::chat - Received chat request with query: {}", query);
        String response = chatBotService.chat(query);
        log.info("RAGController::chat - Returning chat response: {}", response);
        return ResponseEntity.ok(response);
    }

    /**
     * Handles a chat stream request with a given query and return the response as a stream.
     * Example GET request with query : http://localhost:8080/chat?query=What is the
     * phone number of PERSON?
     *
     * @param query the query to be processed by our chat service
     * @return a stream of chat responses
     */
    @GetMapping(value = "/chat/stream", produces = "text/event-stream")
    public Flux<String> chatStream(@RequestParam(name = "query") String query) {
        log.info("RAGController::chatStream - Received chat request for streaming response with query: {}", query);
        return chatBotService.chatStream(query);
    }

    /**
     * Loads data from a specified file URL or performs a default load if no file is provided.
     * When you want to load whatever PDF is in docs, you send this POST request : http://localhost:8080/load .
     * When you want to specify which PDF to load from HTTP(s), you send a POST request like this :
     * http://localhost:8080/load?file=https://www.newitalianbooks.it/wp-content/uploads/2020/05/CristianoRonaldo.pdf
     *
     * @param file the optional URL of the file to load
     * @throws MalformedURLException if the provided file URL is malformed
     */
    @PostMapping("/load")
    public ResponseEntity<String> load(@RequestParam(name = "file", required = false) String file) throws MalformedURLException {
        try {
            if (file == null) {
                log.info("RAGController::load - No file specified, performing default load meaning will load whatever is in docs file.");
                chatBotService.load("");
            } else {
                log.info("RAGController::load - Received load request for PDF in HTTP(s): {}", file);
                chatBotService.load(file);
                log.info("RAGController::load - Load process completed for PDF in HTTP(s): {}", file);
            }
            return ResponseEntity.ok("Load process completed successfully.");
        } catch (MalformedURLException e) {
            log.error("RAGController::load - Malformed URL: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid URL provided.");
        }
    }

    /**
     * Clears all data from the database, meaning all PDFs stored there will disappear.
     * Use with caution!
     * Example POST request : http://localhost:8080/clear
     *
     * @return a response entity indicating the result of the clear operation
     */
    @PostMapping("/clear")
    public ResponseEntity<String> clear() {
        log.info("RAGController::clear - Received clear request.");
        chatBotService.clear();
        log.info("RAGController::clear - Clear process completed.");
        return ResponseEntity.ok("Clear process completed successfully.");
    }
}
