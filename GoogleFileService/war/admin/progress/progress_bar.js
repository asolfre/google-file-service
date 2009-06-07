function show_progress_bar (percent, bytesRead, contentLength) {
	if (percent > 0) {
		document.getElementById("progressContainer").style.display = 'block';
		document.getElementById("progressBar").style.width = (percent*4) + "px";
		document.getElementById("percentText").innerHTML= ' ' + percent;
		document.getElementById("bytesRead").innerHTML= ' ' + (bytesRead/1000);
		document.getElementById("contentLength").innerHTML= ' ' + (contentLength/1000) + ' ';
	}
}