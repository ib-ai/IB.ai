const Discord = require('discord.js');
const fs = require('fs');

//TODO: move class declerations to seperate file?
// Keep maing file for initialisation and startup^?
class Bot {
	constructor() {}

	botLogin(token) {
		client.login(token);
	}

	//TODO: Move find-token to utilities
	findToken(path) {
		// Opens file reader and gets token from token.json file
		// If file does not exist, generates file and stops
		if (fs.existsSync(path)) {
			let tokenJSONObj = JSON.parse(fs.readFileSync('token.json', 'utf8'));
			return tokenJSONObj.token;

		} else {
			fs.writeFileSync('token.json', '{\"token\":"TOKEN_HERE"}');
			throw new Error('Token.json was not found and has been created for you.');
		}
	}
}

const client = new Discord.Client();
let bot = new Bot();

function startup(betaMode, pathToken) {
	//TODO: Print splashscreen
	//TODO: Startup database
	//TODO: Initialise configuration from database
	//TODO: Setup commands and listeners
	//TODO: Setup utilities where needed
	bot.botLogin(bot.findToken(pathToken));
}

//TODO: Startup variables from config
startup(false, 'token.json');

// * * * * * * * * * * * * * * * //
// Listener Declerations
// * * * * * * * * * * * * * * * //

//TODO: Move listeners into seperate files

client.on('ready', () => {
	console.log(`Logged in as ${client.user.tag}!`);
	client.user.setPresence({
		game: {
			name: '3.0 | discord.gg/IBO'
		},
		status: 'dnd'
	});
});


client.on('message', msg => {
	// On message
});