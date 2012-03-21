/**
 * 
 */


function urlencode(str) {
	return escape(str).replace('+', '%2B').replace('%20', '+').replace('*', '%2A').replace('/', '%2F').replace('@', '%40');
	}

function refreshScore()
{
	
	text = document.getElementsByTagName("TEXTAREA")[0].value;
	
	xmlhttp=new XMLHttpRequest();
	xmlhttp.onreadystatechange=function()
	  {
	  if (xmlhttp.readyState==4 && xmlhttp.status==200)
	    {
		  metrics = JSON.parse(xmlhttp.responseText);
	      document.getElementById("ari").innerHTML="<a href=\"/readability/GetReadabilityScores?output=txt&metric=ARI&text="+urlencode(text)+"\">"+metrics.ari+"</a>";
	      document.getElementById("smog").innerHTML="<a href=\"/readability/GetReadabilityScores?output=txt&metric=SMOG&text="+urlencode(text)+"\">"+metrics.smog+"</a>";
	      document.getElementById("smogindex").innerHTML="<a href=\"/readability/GetReadabilityScores?output=txt&metric=SMOGIndex&text="+urlencode(text)+"\">"+metrics.smogindex+"</a>";
	      document.getElementById("fleschkincaid").innerHTML="<a href=\"/readability/GetReadabilityScores?output=txt&metric=FleschKincaid&text="+urlencode(text)+"\">"+metrics.fleschkincaid+"</a>";
	      document.getElementById("colemanliau").innerHTML="<a href=\"/readability/GetReadabilityScores?output=txt&metric=ColemanLiau&text="+urlencode(text)+"\">"+metrics.colemanliau+"</a>";
	      document.getElementById("gunningfog").innerHTML="<a href=\"/readability/GetReadabilityScores?output=txt&metric=GunningFog&text="+urlencode(text)+"\">"+metrics.gunningfog+"</a>";
	      document.getElementById("flesch").innerHTML="<a href=\"/readability/GetReadabilityScores?output=txt&metric=FleschReading&text="+urlencode(text)+"\">"+metrics.fleschreading+"</a>";
	      
	      document.getElementById("words").innerHTML=metrics.words;
	      document.getElementById("characters").innerHTML=metrics.characters;
	      document.getElementById("sentences").innerHTML=metrics.sentences;
	      document.getElementById("syllables").innerHTML=metrics.syllables;
	      document.getElementById("complex").innerHTML=metrics.complexwords;

	    }
	  }
	
	
	
	xmlhttp.open("POST","/readability/GetReadabilityScores",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("text="+urlencode(text)+"&format=json");
	xmlhttp.send();
}
