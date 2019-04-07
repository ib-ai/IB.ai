const express = require('express');
const router = express.Router();

router.get('/', function (req, res) {
    res.status(200).send(toString(new Date().getTime()));
});

module.exports = router;