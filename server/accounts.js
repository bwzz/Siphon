var fs = require('fs');

module.exports = function(req, res) {
  var accountsFile = __dirname + '/../apps/accounts.json'
  console.log("get accounts from : " + accountsFile)
  fs.readFile(accountsFile, function(error, content) {
    res.writeHead(200, {
      'Content-Type': 'application/json'
    })
    if (content == undefined) {
      res.write('[]')
    } else {
      res.write(content)
    }
    res.end()
  })
}
