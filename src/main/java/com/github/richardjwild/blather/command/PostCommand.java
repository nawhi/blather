package com.github.richardjwild.blather.command;

import com.github.richardjwild.blather.datatransfer.Message;
import com.github.richardjwild.blather.datatransfer.MessageRepository;
import com.github.richardjwild.blather.datatransfer.User;
import com.github.richardjwild.blather.datatransfer.UserRepository;
import com.github.richardjwild.blather.time.Clock;

import java.time.Instant;
import java.util.Optional;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class PostCommand implements Command {

    private final String recipientUserName;
    private final String messageText;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    public PostCommand(
            String recipientUserName,
            String messageText,
            MessageRepository messageRepository,
            UserRepository userRepository,
            Clock clock)
    {
        this.recipientUserName = recipientUserName;
        this.messageText = messageText;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.clock = clock;
    }

    @Override
    public void execute() {
        Message message = buildMessage();
        messageRepository.postMessage(message);
    }

    private Message buildMessage() {
        User recipient = findOrCreateRecipient();
        Instant timestamp = clock.now();
        return new Message(recipient, messageText, timestamp);
    }

    private User findOrCreateRecipient() {
        return findRecipient().orElseGet(this::createRecipient);
    }

    private Optional<User> findRecipient() {
        return userRepository.find(recipientUserName);
    }

    private User createRecipient() {
        User recipient = new User(recipientUserName);
        userRepository.save(recipient);
        return recipient;
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }
}
