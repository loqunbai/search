var net = require('net'), co = require("co");

var client = new net.Socket();
var events = {};


client.on('data', function(data) {

	var segument = data.toString().split("\u2222");

	segument.map(function(x){
		if(x.indexOf(">") == 0) x = x.slice(1);

		var rst = x.split(">"), name = rst[0].slice(2);
		
		events[name] && events[name](rst[1]);
		// console.log(x)
		delete events[name];
	});

	// console.log(data+"\n\n\n\n\n\n\n\n");
});

client.on('error', function(e) {
	console.log(e);
});



function Search(port, host){


	

	this.port = port;
	this.host = host;

	var me = this;

}


Search.prototype = {

	init: false,


	deleteIndex: function(id, callback){

		var data = {};

		data.time = generate();
		data.type = "delete";
		data._id = id;
		events[data.time] = callback;
		write(data);
	},
	coDeleteIndex: function(id){

		return function(done){
			
			var data = {};

			data.time = generate();
			data.type = "delete";
			data._id = id;
			events[data.time] = function(str){
				done(null, str);
			};
			write(data);
		}
	},

	connect: function(callback){
		
		client.connect(this.port, this.host, function() {
			console.log("connected...");
			callback  && callback();
		});

		var me = this;
		client.on('close', function() {
			console.log("disconnected... 3秒后重试");
			setTimeout(function(){
				client.connect(me.port, me.host, function() {
					console.log("connected...");
				});
			}, 3000);
		});
	},

	coAddIndex: function(data){

		return function(done){
			data.time = generate();
			data.type = "addIndex";
			events[data.time] = function(str){
				done(null, str);
			};
			write(data);
		};
	},

	addMultiIndex: function(data, callback){
		data.time = generate();
		data.type = "addMultiIndex";
		events[data.time] = callback;
		write(data);
	},
	coaddMultiIndex: function(data){
		return function(done){
			data.time = generate();
			data.type = "addMultiIndex";
			events[data.time] = function(str){
				done(null, str);
			};
			write(data);
		}
	},

	coQuery: function(str){

		return function(done){
			var data = {};
			data.time = generate();
			data.type = "search";
			data.queryStr = str;
			events[data.time] = function(str){
				done(null, str);
			};
			write(data);
		};
	},

	addIndex: function(data, callback){

		data.time = generate();
		data.type = "addIndex";
		events[data.time] = callback;
		write(data);
	},

	query: function(str, callback){

		var data = {};
		data.time = generate();
		data.type = "search";
		data.queryStr = str;
		events[data.time] = callback;
		console.log(str);
		write(data);
	},

	multiSearch: function(str, callback){

		var data = {};
		data.time = generate();
		data.type = "multiSearch";
		data.queryStr = str;
		events[data.time] = callback;
		write(data);
	},

	coMultiSearch: function(str){
		
		return function(done){
			var data = {};
			data.time = generate();
			data.type = "multiSearch";
			data.queryStr = str;
			events[data.time] = function(str){
				done(null, str);
			};
			write(data);
		};
	}
}

function write(obj){
	
	client.write(JSON.stringify(obj)+"\n");
}


module.exports = Search;

function generate(){

    var crypto = require('crypto');
    var shasum = crypto.createHash('sha1');

    shasum.update(String(Math.random()+Date.now()));

    var str = shasum.digest('hex');

    return String(str);
}

