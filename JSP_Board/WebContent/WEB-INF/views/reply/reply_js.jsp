<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page trimDirectiveWhitespaces="true" %>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.7.6/handlebars.min.js"></script>
<script type="text/x-handlebars-template"  id="reply-list-template" >
{{#each .}}
<div class="replyLi" >
        	<img data-target="{{replyer}}" src="{{getPicture replyer}}" class="fas" alt="User Image"  style="width:30px; height:30px">
 	<div class="timeline-item" >
  		<span class="time">
    		<i class="fa fa-clock"></i>{{prettifyDate regdate}}
	 		<a class="btn btn-primary btn-xs" id="modifyReplyBtn" data-rno={{rno}}
				style="display:{{loginUserCheck replyer}}"
	    		data-replyer={{replyer}} data-toggle="modal" data-target="#modifyModal">Modify</a>
  		</span>
	
  		<h3 class="timeline-header"><strong style="display:none;">{{rno}}</strong>{{replyer}}</h3>
  		<div class="timeline-body" id="{{rno}}-replytext">{{replytext}} </div>
	</div>
</div>
{{/each}}	
</script>
<script>
	window.onload =  function() {
		var replyPage = 1;
		getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="+replyPage)
		function getPage(pageInfo) {
			$.getJSON(pageInfo, function(data) {
				printData(data.replyList,$("#repliesDiv"),$("#reply-list-template"))
				printPaging(data.pageMaker,$(".pagination"))
			})
		}
		
		$(".pagination").on("click", "li a", function(event) {
			event.preventDefault()
			replyPage=$(this).attr("href")
			getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="+replyPage)
		})
		
		$("#replyAddBtn").on("click", function(e) {
//			alert("reply regist btn click")
			var replyer=$("#newReplyWriter").val();
			var replytext=$("#newReplyText").val()
			
			if(replytext ==""){
				alert("댓글 내용은 필수입니다.")
				$("#newReplyText").focus().css("border-color", "red")
				.attr("placeholder", "내용은 필수입니다.")
				return
			}
			
			var data={
					"bno":"${board.bno}",
					"replyer":replyer,
					"replytext":replytext
			}
			$.ajax({
				url : "<%=request.getContextPath()%>/reply/add.do",
				type : "post",
				data : JSON.stringify(data),
				success : function(data) {
					alert("댓글이 등록되었습니다.")
					getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="+data)
					$("#newReplyText").val("");
				},
				error: function(e) {
					alert("댓글 등록에 실패했습니다.")
				}
			})
		})
		
		$("div.timeline").on("click", "#modifyReplyBtn", function(e) {
			var rno = $(this).attr("data-rno")
			var replyer = $(this).attr("data-replyer")
			var replytext = $("#"+rno+"-replytext").text();
			
			$("#replytext").val(replytext)
			$(".modal-title").text(rno)
		})
		
		$("#replyModBtn").on("click", function(e) {
			var rno = $(".modal-title").text();
			var replytext = $("#replytext").val()
			
			var sendData={
				rno : rno,
				replytext : replytext
			}
			
			$.ajax({
				url : "<%=request.getContextPath()%>/reply/update.do",
				type : "post",
				data : JSON.stringify(sendData),
				success : function(result) {
					alert("수정되었습니다")
					getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="+replyPage)
				},
				error : function(error) {
					alert("수정 실패")
				},
				complete: function() {
					$("#modifyModal").modal("hide")
				}
			})
		})
		$("#replyDelBtn").on("click", function(e) {
			var rno = $(".modal-title").text()
			
			var sendData={
				bno : "${board.bno}",
				rno : rno,
				page : replyPage
			}
			
			$.ajax({
				url : "<%=request.getContextPath()%>/reply/remove.do",
				type : "post",
				data : JSON.stringify(sendData),
				success : function(page) {
					alert("삭제되었습니다.")
					replyPage = page
					getPage("<%=request.getContextPath()%>/reply/list.do?bno=${board.bno}&page="+page)
				},
				error : function(error) {
					alert("삭제 실패했습니다.")
				},
				complete : function() {
					$("#modifyModal").modal("hide")
				}
			})
			
		})
	}
	
	
	var printData = function (replyArr, target, templateObject) {
		var template=Handlebars.compile(templateObject.html())
		var html = template(replyArr)
		$(".replyLi").remove()
		target.after(html)
	}
	
	Handlebars.registerHelper({
		"prettifyDate":function(timeValue){
			var dateObj = new Date(timeValue)
			var year = dateObj.getFullYear()
			var month = dateObj.getMonth()+1
			var date = dateObj.getDate()
			return year+"/"+month+"/"+date
		},"loginUserCheck":function(replyer){
			var result = "none"
			if("${loginUser.id}" == replyer){
				result = "visible"
			}
			return result
		},
		"getPicture":function(replyer){
			var data={id:replyer};
			var src="<%=request.getContextPath() %>/member/getPicture.do?picture=";
			$.getJSON("<%=request.getContextPath()%>/member/getMemberToJson.do",data,function(result){			
				if(result){
					src+=result.picture;	
				}else{
					src+="noImage.jpg";		
				}
				$('img[data-target="'+replyer+'"]').attr("src",src);
			});
		}
	})
	
	var printPaging=function(pageMaker, target){
		
		var str = "";
		
		if(pageMaker.prev){
			str += "<li class='page-item'><a class='page-link' href='" + (pageMaker.startPage-1)
					+"'> <i class='fas fa-angle-left'/></a></li>";
		}
		for(var i=pageMaker.startPage; i<=pageMaker.endPage; i++){
			var strClass = pageMaker.cri.page == i ? 'active' : '';
			str += "<li class='page-item "+strClass+"'><a class='page-link' href='"+i+"'>"+i+"</a></li>"
		}
		
		if(pageMaker.next){
			str+="<li class='page-item><a class='page-link' href='"+(pageMaker.endPage+1)+"'><i class='fas fa-angle-right'/></a></li>"
		}
		
		target.html(str)
	}
</script>