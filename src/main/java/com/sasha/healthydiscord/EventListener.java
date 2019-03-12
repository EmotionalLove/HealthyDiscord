package com.sasha.healthydiscord;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.PermissionOverride;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.SubscribeEvent;

import java.awt.*;

import static com.sasha.healthydiscord.Main.CFG;

public class EventListener {

    @SubscribeEvent
    public void onServerJoin(final GuildJoinEvent e) {
        if (e.getJDA().getGuilds().size() > CFG.maxServerCount) {
            e.getGuild().leave().submit();
        }
    }

    @SubscribeEvent
    public void onMessageRxServer(final GuildMessageReceivedEvent e) {
        if (e.getAuthor().getIdLong() != Main.discord.getSelfUser().getIdLong()) return; // we only want msgs we send
        if (CFG.whitelistedServerIds.contains(e.getGuild().getId())) return; // we dont care about whitelisted servers
        if (Main.dailyMessages >= CFG.maxMessagesPerDay) {
            e.getMessage().editMessage("**Message Content Redacted**").embed(noMessagesAlert()).queue(s -> {}, f -> {
                e.getMessage().editMessage(Main.discord.getSelfUser().getName() + ", you have reached your daily message limit of " + CFG.maxMessagesPerDay + " messages.").submit();
            });
            return;
        }
        if (Main.dailyMessages == CFG.maxMessagesPerDay - CFG.warningAt) {
            e.getMessage().editMessage(e.getMessage().getContentRaw()).embed(warnMessagesAlert()).queue(s -> {}, f -> {
                e.getMessage().editMessage(Main.discord.getSelfUser().getName() + ", you have almost reached your daily message limit of " + CFG.maxMessagesPerDay + " messages.").submit();
            });
        }
        Main.dailyMessages++;
    }

    @SubscribeEvent
    public void onMessageRxPrivate(final PrivateMessageReceivedEvent e) {
        if (e.getAuthor().getIdLong() != Main.discord.getSelfUser().getIdLong()) return; // we only want msgs we send
        if (CFG.whitelistedUserDmIds.contains(e.getChannel().getId())) return; // we dont care about whitelisted servers
        if (Main.dailyMessages >= CFG.maxMessagesPerDay) {
            e.getMessage().editMessage("**Message Content Redacted**").embed(noMessagesAlert()).queue(s -> {}, f -> {
                e.getMessage().editMessage(Main.discord.getSelfUser().getName() + ", you have reached your daily message limit of " + CFG.maxMessagesPerDay + " messages.").submit();
            });
            return;
        }
        if (Main.dailyMessages == CFG.maxMessagesPerDay - CFG.warningAt) {
            e.getMessage().editMessage(e.getMessage().getContentRaw()).embed(warnMessagesAlert()).queue(s -> {}, f -> {
                e.getMessage().editMessage(Main.discord.getSelfUser().getName() + ", you have almost reached your daily message limit of " + CFG.maxMessagesPerDay + " messages.").submit();
            });
        }
        Main.dailyMessages++;
    }

    public static MessageEmbed noMessagesAlert() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Daily message limit reached!");
        builder.setDescription(Main.discord.getSelfUser().getName() + ", you have reached your daily message limit of " + CFG.maxMessagesPerDay + " messages.");
        builder.setColor(Color.RED);
        return builder.build();
    }
    public static MessageEmbed warnMessagesAlert() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Daily message limit almost reached!");
        builder.setDescription(Main.discord.getSelfUser().getName() + ", you have almost reached your daily message limit of " + CFG.maxMessagesPerDay + " messages.");
        builder.setColor(Color.YELLOW);
        return builder.build();
    }

}
