const express = require("express");
const app = express();
const prefix = "!";
const Discord = require("discord.js")
const { Client, GatewayIntentBits } = require('discord.js');
const { EmbedBuilder } = require('discord.js');
const client = new Discord.Client({
  intents: [
    GatewayIntentBits.Guilds,
    GatewayIntentBits.GuildMembers,
    GatewayIntentBits.GuildMessages,
    GatewayIntentBits.MessageContent
  ]
})

client.once('ready', () => {
  console.log("Discord client bot enabled!");
});

var mysql = require('mysql');
var con = mysql.createConnection({
  host: "sql5.freesqldatabase.com",
  user: "sql5527360",
  password: process.env.password,
  database: "sql5527360"
});

con.connect(function(err) {
  if (err) throw err;
  console.log("Connected!");
});

client.on('guildMemberAdd', member => {
  let channel = member.guild.channels.cache.get('1031672423956873296');
  member.roles.add(member.guild.roles.cache.find(role => role.name === "Unverified"));
  channel.send({
    embeds: [new EmbedBuilder()
      .setDescription(`Welcome ${member.user}! Get started by verifing your Minecraft account!`)
      .setColor("#d81e5b")
      .setTitle("Competitive Minecraft")
      .setThumbnail(member.user.displayAvatarURL())
      .setTimestamp()]
  });
});

client.on('messageCreate', message => {
  if (!message.content.startsWith(prefix) || message.author.bot) return;

  const args = message.content.slice(prefix.length).split(/ +/);
  const command = args.shift().toLowerCase();

  if (command === "link") {
    message.delete();
    if (message.channelId !== '1031670511723348011') return;
    if (args.length == 1) {
      con.query("SELECT name, uuid FROM players WHERE code = '" + args[0] + "'", function(err, result) {
        if (err) throw err;
        if (typeof result[0] !== 'undefined') {
          message.channel.send("You have successfully linked your Discord to '" + result[0].name + "'!")
          message.member.roles.remove(message.member.guild.roles.cache.find(role => role.name === "Unverified"));
          message.member.roles.add(message.member.guild.roles.cache.find(role => role.name === "Verified"));
          con.query("INSERT INTO discords (uuid, discord) VALUES ('" + result[0].uuid + "', '" + message.member.id + "')",   function(err, res) {
            if (err) throw err;
          });
        } else {
          message.channel.send("Invalid or inactive code! Please try linking again...");
        }
      });
    } else {
      message.channel.send("You need to provide a verification code!");
    }
  }
});

client.login(process.env.token); 
