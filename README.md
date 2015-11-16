# node-lucene
Nodejs + Lucene RPC Server


## Java ENV

[How to install OpenJDK 8 on 14.04 LTS?](http://askubuntu.com/questions/464755/how-to-install-openjdk-8-on-14-04-lts)
	
## Lucene 

	
	nohup java -jar qunbai-search/qunbai-search.jar -Xmx256M --port 8888 &

or

	screen -R lucene
	java -jar qunbai-search/qunbai-search.jar -Xmx256M --port 8888

## Synonyms

edit index/synonyms file:

	ap,爸爸

## Node module
	
```javascript

	var Search = require("./index");

	var client = new Search(8888, "127.0.0.1");

	client.connect(query);

	function query(){
		
		client.addMultiIndex({
			_id: "hi",
			fieldType: "lolitawiki",
			field: "color:黑色,type:衣装,year:2015",
			date: Date.now()
		}, function(str){


			client.addMultiIndex({
				_id: "hi2",
				fieldType: "lolitawiki",
				field: "color:白色\u2222type:衣装\u2222year:2015",
				date: Date.now()
			}, function(str){
				client.multiSearch("color:白色 AND year:2015", function(ids){
					console.log(ids)
				});
			});
		})
	}

	// lolitawiki:hi2,
```
