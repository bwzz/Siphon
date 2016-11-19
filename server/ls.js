var fs = require('fs')

module.exports = function(req, res) {
  var root = __dirname + '/../apps/' + req.path
  console.log("root : " + root)
  res.writeHead(200, {
    'Content-Type': 'application/json'
  })
  fs.readdir(root, function(err, files) {
    var fileJsons = []
    if (files != null) {
      for (var i = 0; i < files.length; ++i) {
        var stat = fs.statSync(root + '/' + files[i])
        fileJsons.push({
          "href": req.path + '/' + files[i],
          "name": files[i],
          "date": new Date(new Date(stat.ctime).getTime() + 8*60*60*1000),
          "size": stat.size,
          "ctime": new Date(stat.ctime).getTime(),
          "isDir": stat.isDirectory()
        })
      }
    }
    fileJsons.sort(function(l, r) {
      if (l.ctime > r.ctime) {
        return -1
      } else if (l.ctime < r.ctime) {
        return 1
      } else {
        return 0
      }
    })
    res.write(JSON.stringify(fileJsons, null, "  "))
    res.end()
  })
}
