/*
*
*
*
*
*
* EXAMPLE ROUTE FOR SHOWING HOW TO USE MONGOOSE MODELS
* AND INTERACT WITH MONGODB / ROUTING.
* See: /models/user.js
*
*
*
*
*
* */

const express = require('express');
let router = express.Router();

// User schema
let User = require('../models/user');

/* GET users:id listing. */
router.get('/:id', function (req,res) {
    User.findOne( { _id: req.params.id}, function (err, user) {
        if (err) {
            return res.send(err);
        }

        res.json(user);
    });
});

/* PUT users:id listing. */
router.put('/:id', function (req, res) {
    User.findOne( { _id: req.params.id }, function (err, user) {
        if (err) {
            return res.send(err);
        }

        for (prop in req.body) {
            user[prop] = req.body[prop];
        }

        user.save(function (err) {
            if (err) {
                return res.send(err);
            }

            res.json({ message: 'User updated' });
        });
    });
});

/* DELETE users:id listing */
router.delete('/:id', function (req, res) {
    User.deleteOne( { _id: req.params.id}, function (err, user) {
        if (err) {
            return res.send(err);
        }

        res.json({ message: 'Successfully deleted' });
    });
});

/* POST to add document to user:id listing */
// In this example, a user has `clientDocuments`
// we update the clientDocuments array with a new instance
// of the docModel `Document` schema using information from the
// HTTP request body.
router.post('/:id', function (req, res) {
    User.findOne( { _id: req.params.id }, function (err, user) {

        /*
        user.clientDocuments.push(new docModel(req.body));
        */

        user.save(function (err) {
            if (err) {
                return res.send(err);
            }

            res.send({ message: 'Document Added' });
        });
    });
});

module.exports = router;