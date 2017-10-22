//Para limpar os campos do formulário
$(document).ready(function () {
    $(".limpa").each(function (e) {
        var value = "";
        	$(this).find(".limpaCampo").val(value);
            //$(this).find(".limpaCampo").html(value);
        	$(this).find(".limpaCkBox").val(false);
        
    });
});


$(document).ready(function(){
	$("#bts").click(function(event){
	  event.preventDefault();
	  $("#msg").hide("slow");
	});
	
});