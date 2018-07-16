const Discord = require('discord.js');
const fs = require('fs');

const client = new Discord.Client();
let token = '';

// Opens file reader and gets token from token.json file
// If file does not exist, generates file and stops
if (fs.existsSync('token.json')) {
    let tokenJSONObj = JSON.parse(fs.readFileSync('token.json', 'utf8'));
    token = tokenJSONObj.token;

}else {
    fs.writeFileSync('token.json', '{\"token\":"TOKEN_HERE"}');
    throw new Error('Token.json was not found and has been created for you.');
}

// Declaring listeners
// *
// *
// *

client.on('ready', () => {
    console.log(`Logged in as ${client.user.tag}!`);
    client.user.setPresence({ game: { name: '3.0 | discord.gg/IBO' }, status: 'dnd' });
});


client.on('message', msg => {
  // On message
});

// Bot startup
// *
// *
// *

client.login(token);
