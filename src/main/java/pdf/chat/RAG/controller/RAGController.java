package pdf.chat.RAG.controller;

//                                                    ,--.
//        ,---.  ,-|  /  .-' ,--.--. ,---.  ,--,--. ,-|  |,---. ,--.--. ,--.--.,--,--. ,---.
//        | .-. |' .-. |  `-, |  .--'| .-. :' ,-.  |' .-. | .-. :|  .--' |  .--' ,-.  || .-. |
//        | '-' '\ `-' |  .-' |  |   \   --.\ '-'  |\ `-' \   --.|  |    |  |  \ '-'  |' '-' '
//        |  |-'  `---'`--'   `--'    `----' `--`--' `---' `----'`--'    `--'   `--`--'.`-  /
//        `--'                                                                         `---'

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pdf.chat.RAG.service.ChatBotService;

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

    // http://localhost:8080/load
//    TODO otan sikwnetai i efarmogi tsekarw an i basi einai adeia an nai load oti briskei sto directory an oxi den kanei tipota
//    .. endpoint  "load DOcument" :POST me http tha dinoume ena pdf to opoio tha ginetai tokenize kai load stin vasi
//    @PostConstruct
    @PostMapping("/load")
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

//    TODO
//    delete to file afou apothikeutei stin db
//    in memory(explore)
//    function calling
//    !! Streaming

}
