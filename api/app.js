/**
 * Module dependencies
 */
const express = require('express'); // Web server
const logger = require('morgan'); // Logging
const mongoose = require('mongoose'); //MongoDB interface
const bodyParser = require('body-parser');

let app = express();
console.log(genMongoConnectionString());
mongoose.connect(genMongoConnectionString(), { useNewUrlParser: true });

/**
 * Middleware, configuring body-parser and logging.
 */
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:true}));

/**
 * Router objects
 */
let indexRoute = require('./routes/index');
app.use('/', indexRoute);

let pingRouter = require('./routes/ping');
app.use('/ping', pingRouter);

// EXAMPLE ROUTE
let usersRouter = require('./routes/users');
app.use('/api/users', usersRouter);

//TODO: Abstract function into /lib/
function genMongoConnectionString () {
    const mongoDockerContainerName = process.env.MONGO_CONTAINER_NAME;
    const mongoPort = process.env.MONGO_PORT;
    const mongoDBName = process.env.MONGO_DB;
    const mongoUsername = process.env.MONGO_USERNAME;
    const mongoPassword = process.env.MONGO_PASSWORD;

    return 'mongodb://' + mongoUsername + ':' + mongoPassword + '@' + mongoDockerContainerName + ':' + mongoPort + '/' + mongoDBName;
}

module.exports = app;