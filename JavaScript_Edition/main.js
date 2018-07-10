const Discord = require('discord.js');
//const Listeners = require('./listeners/listeners.js');
const fs = require('fs');

const client = new Discord.Client();
var token = '';

// Opens file reader and gets token from token.json file
// If file does not exist, generates file and stops
if (fs.existsSync('token.json')) {
    var tokenJSONObj = JSON.parse(fs.readFileSync('token.json', 'utf8'));
    token = tokenJSONObj.token;

}else {
    fs.writeFileSync('token.json', '{\"token\":"TOKEN_HERE"}');
    throw new Error('Token.json was not found and has been created for you.');
}

// Declaring listeners

// Bot 'ready' event
client.on('ready', () => {
  console.log(`Logged in as ${client.user.tag}!`);
});

// Bot 'message' event
// Hears DMs, Group DMs and Text-ChannelsJ
client.on('message', msg => {

    // Filter out bot messages
    if (msg.author.bot) {
        return;
    }

    if (msg.channel.type === 'dm') {
        console.log('DM from ' + msg.author.username + '#' + msg.author.discriminator + ': \"' + msg.content + '\"');
    }
});

client.login(token);
