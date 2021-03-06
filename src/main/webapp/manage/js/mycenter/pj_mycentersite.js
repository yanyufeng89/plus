$(function(){
	//头像信息
	intoUserInfo();
	//单个删除 课程已分享
    $('.operate .cancelshare').live('click',function(){
	    	var siteid=$(this).data('courseid');
	    	var name=$(this).data('name');
	    	$this=$(this);
			$.confirm({
				'title'		: '确认删除已分享的站点',
				'message'	: "确认要删除已分享的站点\""+name+"\"吗?",
				'buttons'	: {
					'确认'	: {
						'class'	: 'blue',
						'action': function(){
							delSharedSite(siteid,$this);				
						}
					},
					'取消'	: {
						'class'	: 'gray',
						'action': function(){}
					}
				}
			});
	});
    //单个删除  收藏
	$('.operate span').live('click',function(){
	     	var siteid=$(this).data('courseid');
	    	var name=$(this).data('name');
	    	$this=$(this);
			$.confirm({
				'title'		: '确认删除收藏的站点',
				'message'	: "确认要删除收藏的站点\""+name+"\"吗?",
				'buttons'	: {
					'确认'	: {
						'class'	: 'blue',
						'action': function(){
							deleteMyCollects(siteid,$this);				
						}
					},
					'取消'	: {
						'class'	: 'gray',
						'action': function(){}
					}
				}
			});
	  });
    //我的站点批量删除(已分享)   批量删除 站点已收藏
    $('.remove').live('click',function(){
    	//0代表已分享  1代表收藏 
    	var $this=$(this);
    	var type=$this.attr('data-type');
    	if($this.hasClass('enabled')){
    		var conditions="";
    		$this.parent().nextAll('.docs-list').find('.select-icon').each(function(){
    			//从界面上移除当前
    			conditions+=$(this).data('courseid')+',';
    			
    		});
    		conditions=conditions.substring(0,conditions.length-1);
			$.confirm({
				'title': '确认删除',
				'message': "确认要删除这些站点吗?",
				'buttons': {
					'确认': {
						'class': 'blue',
						'action': function () {
							if(type==0)
								delSharedSite(conditions,$this);
							else
								deleteMyCollects(conditions,$this);
						}
					},
					'取消': {
						'class': 'gray',
						'action': function () {
						}
					}
				}
			});
    	}
    	$(this).removeClass('enabled');
    });
    //站点详情 点击评论
	$('._CommentForm_actions_ooEq [name=answeraddnew]').live('click',function(){
		insertComment($(this),'0');
	});
	//回到顶部
    $("#sitebacktop").mousemove(function(){
    	$("#sitebacktop").css("background-position-x", "-28px");
    }).mouseleave(function(){
    	$("#sitebacktop").css("background-position-x", "0");
    })
    /*当界面下拉到一定位置出现向上的箭头 start*/
    $(window).scroll(function(){  
        if ($(window).scrollTop()>200){  
            $("#sitebacktop").fadeIn("fast");  
        }else{  
            $("#sitebacktop").fadeOut("fast");  
        }  
    });
    /*当界面下拉到一定位置出现向上的箭头 end*/
    
   //站点详情  删除推荐语
	$('.js-cancel').live('click',function(){
		cancelCommtent($(this));
	});
	//关注人
    $('.zm-rich-follow-btn').live('click',function(){
    	topicFollow($(this),0)
    })
    //发布
	$('.submit-button').live('click',function(){
		insertComment($(this),'1');
	});
	 //站点详情 举报
	$('.js-report').live('click',function(){
		topicReport($(this));
	});
	
	//站点详情 点赞
    $('#sitelike').live('click',function(){
    	siteLike($(this));
    })
    //收藏站点
    $('#sitefollowanswer').live('click',function(){
    	collectSite($(this));
    });
    //站点详情页加载更多
    $('.loadmore').live('click',function(){
    	$(this).addClass('loading').empty().append("<span class='capture-loading'></span>加载中");
    	siteLoadMore($(this))
    });
    $('#siteurl').live('click',function(){
    	var url=$(this).attr('data-url');
    	url=url.substr(0,7).toLowerCase()=="http://"?url:"http://"+url;
    	window.location.href=url;
    });
})
//站点加载更多
function siteLoadMore(obj){
    var pageNo=obj.attr('data-pageno');
    var sumpage=obj.data('sumpage');
    var siteid=$('input[name=siteid]').val();
    $.ajax({
    	type:"POST",
      	url:"/sites/loadComments",
      	data:{pageNo:Number(pageNo)+1,siteid:siteid},
    	dataType:"json",
    	success:function(data){
    		if(data.returnStatus=='000'){//返回成功
    			var datamodel={
    			   courseslist:data.obj.list,
    			}
    		   $('.headiconintotem').setTemplateURL(projectName+'coursesLoadMoreTemplate.html');
          	   $('.headiconintotem').processTemplate(datamodel);
          	   $('.loadmore').before($('.headiconintotem').html());
          	   $(".headiconintotem").empty();
          	   $('.loadmore').attr('data-pageno',Number(pageNo)+1);
               obj.removeClass('loading').empty().append('更多');
          	   if(Number(sumpage)==Number(pageNo)+1)
          		 $('.loadmore').hide();
          	   intoUserInfo();
    		}else{
    			
    		}
    	}
    })
};
//收藏站点
function collectSite(obj){
	var actiontype=obj.attr('data-actiontype');
	var siteid=$('input[name=siteid]').val();
	var $this=obj;
	$.ajax({
		type:"POST",
      	url:"/sites/collectSites",
      	data:{actionType:actiontype,objectid:siteid,judgeTodo:actiontype},
      	dataType:"json",
      	success:function(data){
      		if(data.returnStatus=='000'){//返回成功
      			 if(actiontype==0){//取消关注
      				$this.removeClass('zg-btn-green').addClass('zg-btn-white');
      				$this.empty().html('取消收藏');
      				$this.attr('data-actiontype','1');
      				$this.next().find('strong').html(Number($this.next().find('strong').html())+1);
         		}
         		else{
         			$this.removeClass('zg-btn-white').addClass('zg-btn-green');
         			$this.attr('data-actiontype','0');
         			$this.empty().html('收藏站点');
         			$this.next().find('strong').html(Number($this.next().find('strong').html())-1);
         		} 
      		}
      		else{
      			
      		}
      	}
	})
}
//删除推荐语
function cancelCommtent(obj){
	var recommend=obj.attr('data-recommend');
	var siteid=$('input[name=siteid]').val();
	var tableName=obj.attr('data-tableName');
	$.ajax({
	    type:"POST",
	    url:"/m/backstage/delComment",//url:"/sites/delComment",
     	data:{id:recommend,tableName:tableName},//data:{id:recommend,siteid:siteid},
     	dataType:"json",
     	success:function(data){
     		if(data.returnStatus=='000'){//返回成功
     			//同时从界面上移除一条推荐语
     			obj.parents('.item').remove();
     		}
     		else{
     			
     		}
     	}
	})
}
//删除分享的站点
function delSharedSite(conditions,obj){
	   $.ajax({
	    	type:"POST",
	    	url:"/sites/delSharedSites",
	    	data:{condition:conditions},
	    	dataType:"json",
	    	success:function(data){
	    		if(data.returnStatus=='000'){//返回成功
		    	    $('.remove').removeClass('enabled');
		      		if(conditions.toString().indexOf(',')!=-1){
		     			obj.parent().nextAll('.docs-list').find('.select-icon').each(function(){
		         			 //从界面上移除当前
		         			 $(this).parents('li').remove();
		            		});
		       		}else{
		       		     //判断是否是批量删除 但只删除了一条
         				if(obj.hasClass('remove')){
         					obj.parent().nextAll('.docs-list').find('.select-icon').parents('li').remove()
         				}else{
         					obj.parents('li').remove();
         				}
		            }
		        	$('#siteshare').html("分享("+data.operationSum.sitessharesum+")");
		   		}else{//返回失败
		   		}

	   	}
	});
}
//站点点赞
function siteLike(obj){
	var islike=obj.attr('data-islike');
	var likecount=obj.attr('data-likecount');
	var siteid=$('input[name=siteid]').val();
	var siteCreatePerson=$('input[name=siteCreatePerson]').val();
	var sitesname=$('input[name=sitesname]').val();
	$.ajax({
		type:"POST",
      	url:"/sites/clickLikeOnSite",
      	data:{likeOperate:islike,id:siteid,objCreatepersonPg:siteCreatePerson,relationidPg:siteid,objectNamePg:sitesname},
      	dataType:"json",
      	success:function(data){
      		if(data.returnStatus=='000'){//返回成功
      			var $children=obj.children();
        		if(islike==0){
        			obj.attr('data-islike','1');
        			obj.attr('data-likecount',Number(likecount)+1);
        			obj.empty().append($children).append('取消赞('+obj.attr('data-likecount')+")");
        			
        		}
        		else{
        			obj.attr('data-islike','0');
        			obj.attr('data-likecount',Number(likecount)-1);
        			obj.empty().append($children).append(obj.attr('data-likecount')+'人赞');
        		}
      		}else{
      			
      		}
      	}
	})
}
//删除已收藏课程
function deleteMyCollects(conditions,obj){
   $.ajax({
   	type:"POST",
   	url:"/myCenter/deleteMyCollects",
   	data:{condition:conditions,collecttype:"tbl_sites"},
   	dataType:"json",
   	success:function(data){
   		if(data.returnStatus=='000'){//返回成功
      		console.log(data);
      		$('.remove ').removeClass('enabled');
    		if(conditions.toString().indexOf(',')!=-1){
    			obj.parent().nextAll('.docs-list').find('.select-icon').each(function(){
        			//从界面上移除当前
        			$(this).parents('li').remove();
        		});
    		}else{
    			//判断是否是批量删除 但只删除了一条
 				if(obj.hasClass('remove')){
 					obj.parent().nextAll('.docs-list').find('.select-icon').parents('li').remove()
 				}else{
 					obj.parents('li').remove();
 				}
    		}
    		$('#sitecollect').html("收藏("+data.operationSum.sitescollsum+")");

  		}else{//返回失败
  		}

  	}
  });
}
//加载用户信息
function intoUserInfo(){
	$('.uname,.zm-item-link-avatar,.zm-list-avatar,.author-link').pinwheel();
}
//新增推荐语
function insertComment(obj,type){
	 //type  0 推荐语的评论    1是新发布的评论	  
	 var commendcontent='';
	 var objCreatepersonPg='';
	 var relationid='';
	 var siteid=$('input[name=siteid]').val();
	 //被评论人
	 var commentby=obj.attr('data-recommend');
	 //被推荐人的名字
	 var recommendname=obj.attr('data-recommendname');
	 var objectName=$('input[name=sitesname]').val();
	 //评论内容
	 if(type==0){
		   commendcontent=obj.parent().prev().val();
		   objCreatepersonPg=commentby;
		   relationid=obj.attr('data-relationid');
	 }
	 else{
	      commendcontent=$('.commentcontent').val();
	      objCreatepersonPg=$('input[name=siteCreatePerson]').val();
	      relationid=siteid;
	 }
	 var len=commendcontent.length+(commendcontent.match(/[^\x00-\xff]/g) ||"").length;
	 if(len>1000){
	 		if(obj.parent().find('span').length==0)
	 			obj.before('<span class="errortip">请控制在 1000 字以下</span>&nbsp;&nbsp;');
	     		return false;
	 }
	 if(commendcontent.length==0)return false;
	 var $this=obj;
	 $.ajax({
		type:"POST",
       	url:"/sites/addComment",
       	data:{siteid:siteid,recommend:commendcontent,commentby:commentby,objCreatepersonPg:objCreatepersonPg,relationidPg:relationid,objectNamePg:objectName},
       	dataType:"json",
       	success:function(data){
       		if(data.returnStatus=='000'){//返回成功
       			answerreplyCancel($this);
       			//界面上同时追加一条推荐语
       			var datamodel={
       				user:userInfo,	
       			    id:data.obj.id,
       			    userShareTime:data.obj.userCommentTime,
       			    recommendname:recommendname,
       			    commentby:commentby,
       			    commendcontent:commendcontent
       			}
       		   $('.headiconintotem').setTemplateURL(projectName+'bookAppendTemplate.html');
           	   $('.headiconintotem').processTemplate(datamodel);
               if($('.loadmore').length>0)
            	   $('.loadmore').before($('.headiconintotem').html());
               else
            	   $('.detail-list').append($('.headiconintotem').html());
           	   $(".headiconintotem").empty();
           	   $this.parent().find('.errortip').remove();
           	   intoUserInfo();
           	   $('.commentcontent').val('');
       		}  
       		else{
       			
       		}
       	}
	 })
}