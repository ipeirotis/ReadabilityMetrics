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
	      document.getElementById("ari").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=ARI&text="+urlencode(text)+"\">"+metrics.results.ARI+"</a>";
	      document.getElementById("smog").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=SMOG&text="+urlencode(text)+"\">"+metrics.results.SMOG+"</a>";
	      document.getElementById("smogindex").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=SMOG_Index&text="+urlencode(text)+"\">"+metrics.results.SMOG_INDEX+"</a>";
	      document.getElementById("fleschkincaid").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=Flesch_Kincaid&text="+urlencode(text)+"\">"+metrics.results.FLESCH_KINCAID+"</a>";
	      document.getElementById("colemanliau").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=Coleman_Liau&text="+urlencode(text)+"\">"+metrics.results.COLEMAN_LIAU+"</a>";
	      document.getElementById("gunningfog").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=Gunning_Fog&text="+urlencode(text)+"\">"+metrics.results.GUNNING_FOG+"</a>";
	      document.getElementById("flesch").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=Flesch_Reading&text="+urlencode(text)+"\">"+metrics.results.FLESCH_READING+"</a>";
	      
	      document.getElementById("words").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=WORDS&text="+urlencode(text)+"\">"+metrics.results.WORDS+"</a>";
	      document.getElementById("characters").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=CHARACTERS&text="+urlencode(text)+"\">"+metrics.results.CHARACTERS+"</a>";
	      document.getElementById("sentences").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=SENTENCES&text="+urlencode(text)+"\">"+metrics.results.SENTENCES+"</a>";
	      document.getElementById("syllables").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=syllables&text="+urlencode(text)+"\">"+metrics.results.SYLLABLES+"</a>";
	      document.getElementById("complex").innerHTML="<a href=\"/readability/GetReadabilityScores?format=txt&metric=COMPLEXWORDS&text="+urlencode(text)+"\">"+metrics.results.COMPLEXWORDS+"</a>";

	    }
	  }
	
	
	
	xmlhttp.open("POST","/readability/GetReadabilityScores",true);
	xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	xmlhttp.send("text="+urlencode(text)+"&format=json");
	xmlhttp.send();
}
