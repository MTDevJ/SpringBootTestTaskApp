var size = 150;

var elements = document.querySelectorAll('.descriptionContent');
Array.from(elements).forEach(function(element){
	var temp = element.textContent;
    if (temp.length > size) {
    	element.textContent=temp.slice(0,150)+ '...';
  	}
});


            