/*
*
*
*
*
*
* EXAMPLE ROUTE FOR SHOWING HOW TO USE MONGOOSE MODELS
* AND INTERACT WITH MONGODB / ROUTING.
* See: /routes/users.js
*
*
*
*
*
* */

const mongoose = require('mongoose');
let Schema = mongoose.Schema;

var userSchema = new Schema ({
    userId: String,
    username: String
});

module.exports = mongoose.model('User', userSchema);