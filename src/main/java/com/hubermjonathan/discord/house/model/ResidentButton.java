package com.hubermjonathan.discord.house.model;

import com.hubermjonathan.discord.house.Constants;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.command.UserContextInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

public abstract class ResidentButton extends ListenerAdapter {
    private final String name;
    private CommandData commandData;
    private UserContextInteractionEvent event;

    public ResidentButton(String name, CommandData commandData) {
        this.name = name;
        this.commandData = commandData;
    }

    public CommandData getCommandData() {
        return commandData;
    }

    public UserContextInteractionEvent getEvent() {
        return event;
    }

    @Override
    public void onUserContextInteraction(@NotNull UserContextInteractionEvent event) {
        if (event.getUser().isBot()
                || !event.getName().equals(name)
                || event.getMember().equals(event.getTargetMember())
                || !event.getTargetMember().getRoles().get(0).getName().equals(Constants.RESIDENT_ROLE_NAME)) {
            event.reply(Emoji.fromUnicode(Constants.DENY).getName()).setEphemeral(true).queue();
            return;
        }

        this.event = event;

        try {
            execute();
            getEvent().reply(Emoji.fromUnicode(Constants.POKE).getName()).setEphemeral(true).queue();
        } catch (Exception e) {
            e.printStackTrace();
            getEvent().reply(Emoji.fromUnicode(Constants.DENY).getName()).setEphemeral(true).queue();
        }
    }

    public abstract void execute() throws Exception;
}
