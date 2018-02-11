package com.github.richardjwild.blather.command;

import com.github.richardjwild.blather.io.Output;
import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.time.TimestampFormatter;
import com.github.richardjwild.blather.user.User;
import com.github.richardjwild.blather.user.UserRepository;

import java.util.Optional;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class WallCommand implements Command {

    private final String followerUserName;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final TimestampFormatter timestampFormatter;
    private final Output output;

    public WallCommand(
            String followerUserName,
            UserRepository userRepository,
            MessageRepository messageRepository,
            TimestampFormatter timestampFormatter,
            Output output)
    {
        this.followerUserName = followerUserName;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.timestampFormatter = timestampFormatter;
        this.output = output;
    }

    @Override
    public void execute() {
        findFollower().ifPresent(this::printWallMessages);
    }

    private Optional<User> findFollower() {
        return userRepository.find(followerUserName);
    }

    private void printWallMessages(User follower) {
        follower.wallUsers()
                .flatMap(messageRepository::allMessagesPostedTo)
                .sorted()
                .map(this::format)
                .forEach(output::writeLine);
    }

    private String format(Message message) {
        return message.formatWall(timestampFormatter);
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
