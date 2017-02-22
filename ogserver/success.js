var http=require('http');
var express= require("express");
//var PayU = require('payu');

var bodyParser = require('body-parser');
var app = express.createServer();
var port = Number(process.env.PORT || 8005);
app.use(bodyParser.json()); // to support JSON-encoded bodies
app.use(bodyParser.urlencoded({
  extended: true
}));
app.locals.baseurl = 'http://localhost:8005';

// payu auth configuration
//var payu = new PayU("gtKFFx", "eCwWELxi", "https://test.payu.in/_payment");

// Page will display after payment has beed transfered successfully
app.post('/success', function(req, res) {
    console.log('inside success');
    //console.log(res);
    console.log(req);
    res.writeHead(200, {"Content-Type": "application/json"});
    res.write(JSON.stringify({"Payment transfered successfully":req.body}));
    res.end();
});



// Page will display when you canceled the transaction
app.get('/cancel', function(req, res) {
   console.log('inside success');
  res.writeHead(200, {"Content-Type": "application/json"});
  res.write("Payment canceled successfully.");
  res.send(res);
});

app.get('/', function(req, res) {
    console.log('inside success');
    res.writeHead(200, {"Content-Type": "application/json"});
   res.send(req);
});

app.listen(8005);