var __hasProp = Object.prototype.hasOwnProperty, __extends = function(child,
		parent) {
	for ( var key in parent) {
		if (__hasProp.call(parent, key))
			child[key] = parent[key];
	}
	function ctor() {
		this.constructor = child;
	}
	ctor.prototype = parent.prototype;
	child.prototype = new ctor;
	child.__super__ = parent.prototype;
	return child;
};

ReadabilityMetrics = (function() {
	function ReadabilityMetrics() {
		$.ajaxSetup({
			beforeSend: function(xhrObj) {
				xhrObj.setRequestHeader("x-mashape-user-publickey", "mjc4c5cjumzfi2jspakkw4zbymljqe");
			}
		});
	}
	
	ReadabilityMetrics.prototype.submit_text = function(body, callback) {
		return $.post("/readability/api/v1/text", body, callback, 'json');
	};
	
	ReadabilityMetrics.prototype.fetch_metrics = function(id, callback) {
		return $.get("/readability/api/v1/text/" + id + "/metrics", {}, callback, 'json');
	};
	
	return ReadabilityMetrics;
})();
	
$("#btnSubmit").click(function(e) {
	var body = $("#txtBody").attr('value');
	
	$("td span").fadeOut();
	
	readability_metrics = new ReadabilityMetrics();

	readability_metrics.submit_text(body, function(data) {
		var id = data['id'];

		readability_metrics.fetch_metrics(id, function(result) {
			//console.log(metrics);
			
			for (var e in result.metrics) {
				var k = e;
				var v = result.metrics[e];
				
				$("#spn" + k).text(result.metrics[e]);
			};
			
			$("td span").fadeIn();
		});
		
	});
	
});