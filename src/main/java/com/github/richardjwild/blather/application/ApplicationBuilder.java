package com.github.richardjwild.blather.application;

import com.github.richardjwild.blather.command.factory.*;
import com.github.richardjwild.blather.io.ConsoleInput;
import com.github.richardjwild.blather.io.ConsoleOutput;
import com.github.richardjwild.blather.io.Input;
import com.github.richardjwild.blather.io.Output;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.parsing.CommandReader;
import com.github.richardjwild.blather.parsing.InputParser;
import com.github.richardjwild.blather.persistence.InMemoryMessageRepository;
import com.github.richardjwild.blather.persistence.InMemoryUserRepository;
import com.github.richardjwild.blather.time.Clock;
import com.github.richardjwild.blather.time.SystemClock;
import com.github.richardjwild.blather.time.TimestampFormatter;
import com.github.richardjwild.blather.user.UserRepository;

public class ApplicationBuilder {

    private Output output = new ConsoleOutput();
    private Input input = new ConsoleInput();
    private UserRepository userRepository = new InMemoryUserRepository();
    private MessageRepository messageRepository = new InMemoryMessageRepository();
    private Clock clock = new SystemClock();

    public static ApplicationBuilder anApplication() {
        return new ApplicationBuilder();
    }

    public ApplicationBuilder withInput(Input input) {
        this.input = input;
        return this;
    }

    public ApplicationBuilder withOutput(Output output) {
        this.output = output;
        return this;
    }

    public ApplicationBuilder withClock(Clock clock) {
        this.clock = clock;
        return this;
    }

    public ApplicationBuilder withMessageRepository(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        return this;
    }

    public ApplicationBuilder withUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }

    public Application build() {
        InputParser inputParser = new InputParser();
        Controller controller = new Controller();

        TimestampFormatter timestampFormatter = new TimestampFormatter(clock);

        CommandFactories commandFactories = new CommandFactories(
                new FollowCommandFactory(userRepository),
                new PostCommandFactory(messageRepository, userRepository, clock),
                new QuitCommandFactory(controller),
                new ReadCommandFactory(
                        messageRepository,
                        userRepository,
                        timestampFormatter,
                        output),
                new WallCommandFactory(
                        userRepository,
                        messageRepository,
                        timestampFormatter,
                        output));

        CommandReader commandReader = new CommandReader(
                input,
                inputParser,
                commandFactories);

        EventLoop eventLoop = new EventLoop(commandReader, controller);
        return new Application(eventLoop, output);
    }
}
