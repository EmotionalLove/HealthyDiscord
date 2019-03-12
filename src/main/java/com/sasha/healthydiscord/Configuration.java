package com.sasha.healthydiscord;

import com.sasha.simplesettings.annotation.Setting;

import java.util.ArrayList;

public class Configuration {

    @Setting public String discordToken = "[no default]";
    @Setting public String timezoneCode = "PDT";
    @Setting public int maxMessagesPerDay = 350;
    @Setting public int warningAt = 50;
    @Setting public int maxServerCount = 20;
    @Setting public ArrayList<String> whitelistedUserDmIds = new ArrayList<>();
    @Setting public ArrayList<String> whitelistedServerIds = new ArrayList<>();

    {
        whitelistedUserDmIds.add("509576093540286479");
        whitelistedServerIds.add("550479351372578817");
    }
}
