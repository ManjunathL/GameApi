var env = "dev";
var restBase = "https://uat.mygubbi.com";
//var restBase = "https://localhost";
var fbaseRootUrl = "https://mygubbi-uat.firebaseio.com/";
var applyAnalytics = false;
var appendRobotsNoFollow = true;

// Initialize Firebase
var fbaseconfig = {
   apiKey: "AIzaSyCoKs1sqeE8GmMKysYpkOdO9KkZXcmhDQ0",
   authDomain: "mygubbi-uat.firebaseapp.com",
   databaseURL: "https://mygubbi-uat.firebaseio.com/",
   storageBucket: "mygubbi-uat.appspot.com",
   messagingSenderId: "106892346704"
 };

/* Online Payment Details */
var paybaseUrl="https://test.payu.in/_payment";
var merchantKey = "gtKFFx";
var SALT = "eCwWELxi";
var successbaseUrl = "https://localhost:8787/paysuccess";
var failurebaseUrl = "http://192.168.104.88:8080/TestingApp/failure.jsp";
var cancelbaseUrl = "http://192.168.104.88:8080/TestingApp/failure.jsp";