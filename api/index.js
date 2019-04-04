const express = require('express');
const fs = require('fs');
const app = express();

fs.readdir('./endpoints', (err, files) => {
    if(err != null) {
        console.error(err);
        return;
    }
    files.forEach(it => {
        if(!it.endsWith('.js')) {
            return;
        }
        let endpoint = require(`./endpoints/${it}`);
        app.all(`/${endpoint.Route}`, endpoint.Handle);
        console.log(`Registered endpoint "${endpoint.Route}".`);
    });
    app.listen(4441, () => {
        console.log('Listening...');
    });
});