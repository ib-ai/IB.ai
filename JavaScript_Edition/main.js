const Discord = require('discord.js');
const fs = require('fs');

const client = new Discord.Client();
let token = '';
let webHook = null;

// Opens file reader and gets token from token.json file
// If file does not exist, generates file and stops
if (fs.existsSync('token.json')) {
    let tokenJSONObj = JSON.parse(fs.readFileSync('token.json', 'utf8'));
    token = tokenJSONObj.token;
    webHook = new Discord.WebhookClient(tokenJSONObj.webHookID, tokenJSONObj.webHookToken);

    if(webHook == null){
        return;
    }

}else {
    fs.writeFileSync('token.json', '{\"token\":"TOKEN_HERE",\"webHookToken\":\"WEBHOOK_TOKEN_HERE\",\"webHookID\":\"WEBHOOK_ID_HERE\"}');
    throw new Error('Token.json was not found and has been created for you.');
}

// Declaring listeners

// Bot 'ready' event
client.on('ready', () => {
    console.log(`Logged in as ${client.user.tag}!`);
    client.user.setPresence({ game: { name: 'dm to message mods <3' }, status: 'online' });
});

// Bot 'message' event
// Hears DMs, Group DMs and Text-ChannelsJ
client.on('message', msg => {

    // Filter out bot messages
    if (msg.author.bot) {
        return;
    }

    if (msg.channel.type === 'dm') {
        if(msg.content.length === 0){
            return;
        }

        console.log('DM from ' + msg.author.username + '#' + msg.author.discriminator + ': \"' + msg.content + '\"');
        webHook.send('**User ' + msg.author.username + '#' + msg.author.discriminator + ' says:**' +
            '\n' + msg.content);
    }
});

client.login(token);
