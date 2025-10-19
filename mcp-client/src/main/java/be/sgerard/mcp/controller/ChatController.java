package be.sgerard.mcp.controller;

import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {

    private final AnthropicChatModel openAiChatModel;
    private final SyncMcpToolCallbackProvider toolCallbackProvider;

    public ChatController(AnthropicChatModel openAiChatModel, SyncMcpToolCallbackProvider toolCallbackProvider) {
        this.openAiChatModel = openAiChatModel;
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @GetMapping("/do-ask")
    public String ask(@RequestParam String question) {
        return ChatClient.create(openAiChatModel)
                .prompt(question)
                .toolCallbacks(toolCallbackProvider)
                .call()
                .content();
    }
}