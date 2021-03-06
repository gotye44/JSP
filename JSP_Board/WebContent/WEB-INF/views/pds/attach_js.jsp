<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page trimDirectiveWhitespaces="true" %>

<script>
window.addEventListener('load', function() {
	$("#addFileBtn").on("click", function(e) {
		if($("input[name='uploadFile']").length >= 5){
			alert("첨부파일은 5개까지만 가능합니다.")
			return
		}
		var input=$("<input>").attr({"type":"file","name":"uploadFile"}).css("display","inline")
		var div = $("<div>").addClass("inputRow")
		div.append(input).append("<button style='border:0;outline:0;' class='badge bg-red' type='button'>X</button>")
		div.appendTo(".fileInput")
	})
	
	$("div.fileInput").on("click", "div.inputRow>button",function(e){
		$(this).parent("div.inputRow").remove()
	})
	
	$("div.fileInput").on("change",'input[type="file"]',function(e){
		if(this.files[0].size>1024*1024*40){
			alert("파일 용량이 40MB를 초과하였습니다.")
			this.value = "";
			$(this).focus()
			return false
		}
	})
})
</script>