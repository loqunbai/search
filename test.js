


var Search = require("./index");


var client = new Search(12345, "test.loqunbai.com");

client.connect(query);

function query(){
	
	// client.addMultiIndex({
	// 	_id: "hi111",
	// 	fieldType: "test",
	// 	field: "color:黑色\u2222type:衣装\u2222year:2011",
	// 	date: Date.now()
	// }, function(str){

	// 	client.addMultiIndex({
	// 		_id: "hi2",
	// 		fieldType: "test",
	// 		field: "color:白色\u2222type:衣装\u2222year:2011",
	// 		date: Date.now()
	// 	}, function(str){
			client.multiSearch("dress:a*", function(ids){
				console.log(ids)
			});
		// });
	// })
}

