package com.sasha.healthydiscord;

import com.sasha.simplesettings.SettingHandler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import javax.security.auth.login.LoginException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    //limits
    public static int dailyMessages = 0;
    public static int prevDay = -1;
    //

    public static JDA discord;
    public static final Configuration CFG = new Configuration();
    private static SettingHandler handler = new SettingHandler("config");

    public static void main(String[] args) throws LoginException, InterruptedException {
        System.out.println("Starting up...");
        handler.read(CFG);
        JDABuilder builder = new JDABuilder(AccountType.CLIENT).setToken(CFG.discordToken);
        discord = builder.buildBlocking();
        discord.setEventManager(new AnnotatedEventManager());
        discord.addEventListener(new EventListener());
        discord.getPresence().setStatus(OnlineStatus.INVISIBLE);
        ScheduledExecutorService sex = Executors.newScheduledThreadPool(4);
        sex.scheduleAtFixedRate(() -> {
            if (getDay() != prevDay) {
                prevDay = getDay();
                dailyMessages = 0;
            }
        }, 5L, 5L, TimeUnit.SECONDS);
    }

    public static int getDay() {
        ZonedDateTime zone = ZonedDateTime.now(ZoneId.of(CFG.timezoneCode));
        return zone.getDayOfYear();
    }
}
