package com.hubermjonathan.discordbot.buttons;

import com.hubermjonathan.discordbot.Constants;
import com.hubermjonathan.discordbot.models.Button;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;

import java.util.EnumSet;

public class Lock extends Button {
    public Lock(String name) {
        super(name);
    }

    @Override
    public void execute() throws Exception {
        Category category = getEvent().getChannel().getParent();

        category.getVoiceChannels().get(0).getManager()
                .clearOverridesAdded()
                .putPermissionOverride(getEvent().getGuild().getPublicRole(), null, EnumSet.of(Permission.VOICE_CONNECT))
                .queue();
    }
}
