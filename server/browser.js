var express = require('express');
var app = express();
var ls = require('./ls')
var accounts = require('./accounts')

app.get(/^\/(android)|(iphone)|(windows)\/?.*$/, ls)
app.get('/accounts', accounts)

app.listen(3000)
