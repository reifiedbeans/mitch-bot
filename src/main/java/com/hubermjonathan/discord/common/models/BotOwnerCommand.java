package com.hubermjonathan.discord.common.models;

import com.hubermjonathan.discord.common.Constants;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public abstract class BotOwnerCommand extends Command {
    public BotOwnerCommand(String name, CommandData commandData) {
        super(name, commandData);
    }

    @Override
    protected boolean shouldIgnoreEvent() {
        return getEvent().getUser().isBot()
                || !getEvent().getName().equals(getName())
                || !getEvent().getUser().getId().equals(Constants.BOT_OWNER_ID);
    }
}
