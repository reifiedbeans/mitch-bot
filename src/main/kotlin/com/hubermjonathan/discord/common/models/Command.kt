package com.hubermjonathan.discord.common.models

import com.hubermjonathan.discord.mitch.MitchConfig
import com.hubermjonathan.discord.mitch.botTestingChannel
import com.hubermjonathan.discord.mitch.purdudesGuild
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.lang.IllegalArgumentException

abstract class Command(private val name: String, val commandData: CommandData, protected val context: Context) : ListenerAdapter(), KoinComponent {
    protected open val allowedChannels: List<TextChannel>? = null
    protected val jda: JDA by inject()
    private val logger = context.logger

    protected open fun shouldIgnoreEvent(event: SlashCommandInteractionEvent): Boolean {
        val userIsBot = event.user.isBot
        val commandNameIsWrong = event.name != this.name
        val channelIsNotTextChannel = event.channel !is TextChannel

        return userIsBot ||
            commandNameIsWrong ||
            channelIsNotTextChannel
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (shouldIgnoreEvent(event)) return

        try {
            val isRunningInDevMode = MitchConfig.DEV_MODE
            val channelIsNotAllowed = allowedChannels?.contains(event.channel.asTextChannel()) == false
            val channelIsNotTestingChannel = event.channel != jda.purdudesGuild.botTestingChannel

            if (!isRunningInDevMode && channelIsNotAllowed) {
                throw IllegalArgumentException("command cannot be run in this channel")
            }

            if (isRunningInDevMode && channelIsNotTestingChannel) {
                throw IllegalArgumentException("command cannot be run outside of bot testing while in dev mode")
            }

            val result = execute(event)

            event
                .reply("\uD83E\uDD18 $result")
                .setEphemeral(true)
                .queue()
        } catch (e: Exception) {
            logger.error(e.localizedMessage, e)
            event
                .reply(e.localizedMessage)
                .setEphemeral(true)
                .queue()
        }
    }

    abstract fun execute(event: SlashCommandInteractionEvent): String
}
