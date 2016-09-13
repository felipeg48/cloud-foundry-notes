function getData(myFunction){
	 $.ajax({
		    url: "/polls/data",
		    success:function(results){
		    		myFunction(jQuery.parseJSON(results));
		    }
	 });
}
function init() {
	getData(function(data) {
	    	var x = d3.scale.linear()
	        .domain([0, d3.max(data,function(d){return d.total})])
	        .range([0, 420]);
	    	
	    	d3.select(".chart")
	    	  .selectAll("div")
	    	    .data(data)
	    	  .enter().append("div")
	    	    .style("width", function(d) { return x(d.total) + "px"; })
	    	    .text(function(d) { 
	    	    		return d.candidate + " : " + d.total
	    	    	});
	    	
	});
}

function refresh() {
	getData(function(data){
		var x = d3.scale.linear()
        .domain([0, d3.max(data,function(d){return d.total})])
        .range([0, 420]);
		
		d3.select(".chart")
  	  		.selectAll("div")
  	  		.data(data)
  	  	.transition()
  	  		.duration(1000)
  	  		.style("width", function(d) { return x(d.total) + "px"; })
  	  		.text(function(d) { 
    				return d.candidate + " : " + d.total
  	  		});
  	  	
	});
}

init();
setInterval(refresh, 3000 );
