module.exports = {
    Route: 'ping',
    Handle: (req, res) => {
        res.status(200).json({
            received: new Date().getTime()
        })
    }
}