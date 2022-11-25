package dev.micah.minecraftcompetitive.database;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;

public class DiscordInfo {
    private String discordId;
    private JDA jda;
    public DiscordInfo(String discordId) {
        this.jda = JDABuilder.createLight(System.getenv("process.discord.token")).build();
        this.discordId = discordId;
    }

    public User getDiscordUser() {
        return jda.retrieveUserById(discordId).complete();
    }

    public String getName() {
        return getDiscordUser().getAsTag();
    }

    public String getDiscordId() {
        return discordId;
    }

}
