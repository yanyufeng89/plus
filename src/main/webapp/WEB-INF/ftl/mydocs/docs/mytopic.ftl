<!DOCTYPE html>
<html class="expanded">
  <head>
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <title>
          我的话题-个人中心-JobPlus知识库
    </title> 
    <#include "/mydocs/commonTemplate/headstyle/headstyle.ftl"/>
  </head>
    <body>
      <div id="doc" class="page">
      <#include "/mydocs/commonTemplate/topandtail/top.ftl"/>
      <div id="bd">
		<div class="bd-wrap">
		<div class="uc-aside <#if Session.user.usertype==2>com-aside</#if>">
		<div class="uc-user-box">
		<div class="<#if Session.user.usertype==2>img-business-center<#else>img-center</#if>">
		 <a  href="<#if Session.user.usertype==2>/comp/getHomePage/${Session.user.userid}?isReview=0<#else>/myCenter/getMyHeadTop</#if>" target="_self">
		  <#if (Session.user.headicon)?? && Session.user.headicon?length gt 0>
		    <img src="${Session.user.headicon}" width='100' height='100' alt="个人头像" class='lazy'>
		  <#else>
		    <img src="/image/<#if Session.user.usertype==2>cm-defaultIcon100.jpg<#else>myphoto.jpg</#if>" width='100' height='100' alt="个人头像" class='lazy'>
		  </#if>
		 </a>
		</div>
		<p class="user-name">
			<a class="name-link" href="<#if Session.user.usertype==2>/comp/getHomePage/${Session.user.userid}?isReview=0<#else>/myCenter/getMyHeadTop</#if>" target="_self">
	            <#if (Session.user)??>
		           ${Session.user.username}
		        </#if>
		     </a>
		    <#if Session.user.usertype==2>
		    <#else>
             <span class="pj-level"><em>LV.${Session.user.userlevel}</em></span>
		    </#if>
		</p>
	    <p class="user-level">
	      <#if Session.user.usertype==2>
		    <a href="/comp/getHomePage/${Session.user.userid}?isReview=0" target="_blank">&nbsp;进入公司主页</a> 
		  <#else>
		    <a href="/myHome/getHomePage/${Session.user.userid}?isReview=0" target="_blank">&nbsp;进入个人主页</a>
		  </#if>
	    </p>
		<div class="mydoc-list">
		    <ul id="accordion">
		      <li class="open">
		         <div class="my-doc link">
		           <p>
		            <a href='/myCenter/getMyHeadTop'>
			         <span class="icon-ken <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的知识库
			        </a>
			        <b class="iconfont up-arrow doc-arrow-btn"></b>
			        </p>
			     </div>
		         <ul class="submenu" style="display:block">
	                 <li id="mydocument"><a href="/myCenter/getMyDocsUploaded"><span class="icon-doc <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的文档</a></li>
	                 <li id="mytopic"><a href="/myCenter/getMyTopicsUploaded" class='current'><span class="icon-topic <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的话题</a></li>
	                 <li id="mybook"><a href="/myCenter/getSharedBookList"><span class="icon-book <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的书籍</a></li>
	                 <li id="mycourse"><a href="/myCenter/getSharedCourseList"><span class="icon-course <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的课程</a></li>
	                 <li id="myarticle"><a href="/myCenter/getSharedArticleList"><span class="icon-article <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的文章</a></li>
	                 <li id="mysite"><a href="/myCenter/getSharedSiteList"><span class="icon-site <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的站点</a></li>
		         </ul>
		      </li>
		      <li>
		         <div class="my-doc link"><p><span class="icon-account <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的账户<b class="iconfont up-arrow doc-arrow-btn"></b></p></div>
		         <ul class="submenu">
	                 <li><a href="/myCenter/getAllSms?islook=0"><span class="icon-info <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>全部消息</a></li>
	                 <li><a href="/myCenter/getSmsFilterParm"><span class="icon-setinfo <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>消息设置</a></li>
	                 <li><a href="/myCenter/account/security"><span class="icon-accountsafety <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>账户安全</a></li>
		          </ul>
		       </li>
		       <li>
		         <div class="my-order link"><p><span class="icon-order <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的订单<b class="iconfont up-arrow doc-arrow-btn"></b></p></div>
		         <ul class="submenu">
	                 <li><a href="/user/allorder"><span class="icon-allorder <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>全部订单</a></li>
	                 <li><a href="/user/cash"><span class="icon-cash <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>已经支出</a></li>
	                 <li><a href="/user/nocash"><span class="icon-nocash <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>尚未支付</a></li>
		          </ul>
		      </li>
		       <li>
		         <div class="my-order link"><p><span class="icon-wealth <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>我的财富<b class="iconfont up-arrow doc-arrow-btn"></b></p></div>
		         <ul class="submenu">
	                <li><a href="/account/getDetailListByRecord?changetype=1"><span class="icon-incomewealth <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>财富收益</a></li>
	                  <li><a href="/account/getDetailListByRecord?changetype=2"><span class="icon-wealthspending <#if (Session.user.usertype==2)>c-icon<#else>u-icon</#if>"></span>财富支出</a></li>
		          </ul>
		      </li>
	     </ul>
	
		</div>
		</div>
		<div class="side-nav-bar" id="side-nav-bar" style="display: none; left: 1461.5px; position: fixed; top: 650px;">
		<ul>
		<li><a href="#"  target="_self"><span class="s-ic guide"></span>反馈</a></li>
		<li class="gotop"><a href="javascript:void(0);"><span class="s-ic top"></span></a></li>
		</ul>
		</div>
		</div>
		<div class="uc-main">
			<div class="uc-head" style="padding:0;width:950px;">				
				<#include "/mydocs/commonTemplate/confansinfo/confansinfoshare.ftl"/>
			</div>
		
		   <div class='maincontent'>
		    <div class="uc-head-bottom">
				  
				    <span><a class='icon-mytopic'></a></span>
				    <div style="float:left">
					    <p>我的话题</p>
					    <p class="dll">
	                       <a href='/myCenter/getMyTopicsUploaded' <#if (topicsPage)??>class="current"</#if> id='topicshare'>分享&nbsp;<#if (Session.operationSum.topicssharesum)??>${Session.operationSum.topicssharesum}</#if></a>
	                       <a href='/myCenter/getMyTopicsAttention' <#if (attentionPage)??>class="current"</#if> id='topicattention'>关注&nbsp;<#if (Session.operationSum.topicsattesum)??>${Session.operationSum.topicsattesum}</#if></a>
	                       <a href='/myCenter/getMyTopicsComment' <#if (topicsCommentPage)??>class="current"</#if> id='topicreplay'>回复&nbsp;<#if (Session.operationSum.topicscomsum)??>${Session.operationSum.topicscomsum}</#if></a>
					    </p>
				    </div>
				     <a href='javascript:void(0);' onclick="share('topic');" class='head-sharein' style='float:right;margin:23px 28px 0 0'></a>
			</div>
		    <#if (topicsPage)??>
			 <div class="uc-mydocshare">
			<#else>
			 <div class="uc-mydocshare" style='display:none'>
			</#if>
			   <div class="console-box">
					<a class="current">已分享</a>
	           </div>
	           <div id="sharecontent">
		           <div class="successshare">
		             <div class="docs-list-box">
		               <div class='delete-box normal-console-box clearfix'>
		                 <#--<span class="remove" data-type='1'><b class="iconfont"></b>&nbsp;&nbsp;删除</span>-->
		               </div>
		               <div class="status-box">
						  <div class="w332 ib">话题名称</div>
						  <div class="w92 ib">是否匿名</div>
						  <div class="w92 ib">关注</div>
						  <div class="w92 ib">回复</div>
						  <div class="w92 ib">点赞</div>
						  <div class="w92 ib">创建时间</div>
						  <div class="w92 ib">操作</div>
					   </div>
					   <div class="docs-list">
					     <ul>
					       <#if (topicsPage)??>
                              <#list topicsPage.list as list>
                                <#--<#if (list.ispublic==1)>-->
                                  <li>
                                    <div class='w332 fs14 fc3 ib titlehidden'>
                                   	  <#--<div class="checkbox chk" data-topicid="${list.id}" data-name="${list.title}"></div>-->
                                      <a href="/topics/getTopicsDetail/${list.id}" target="_blank" title="${list.title}">${list.title}</a>  
                                    </div>
                                    <div class='w92 ib topic-ispublic'>
                                       <#if list.ispublic==1>
                                         	公开
                                        <#elseif list.ispublic==0>
                                        	匿名
                                        <#else>
                                        	公开
                                       </#if>
                                    </div>
                                    <div class='w92 ib iltopic'>
                                       <#if (list.followssum)??>
                                         ${list.followssum}
                                       </#if>
                                    </div>
                                    <div class='w92 ib iltopic'>
                                      <#if (list.replysum)??>
                                         ${list.replysum}
                                       </#if>
                                    </div>
                                    <div class='w92 ib iltopic'>
                                       <#if (list.likesum)??>
                                         ${list.likesum}
                                       </#if>
                                    </div>
                                    <div class='w92 ib'>
                                        <#if (list.createtime?string("yyyy-MM-dd"))??>
                                         ${list.createtime?string("yyyy-MM-dd")}
                                       </#if>
                                    </div>
                                    <div class='w92 ib operate edittopic'>
                                       <a href="/topics/getTopicsDetail/${list.id}"  class='pr10 modify-doc __HIDE__' target='_self'>
								         <b class="iconfont pr2"></b>修改
								       </a>
								       <#--<span data-id='${list.id}' data-name='${list.title}' data-type='1'>
						                  <b class="iconfont pr2"></b> 删除
								       </span>-->
                                    </div>
                                  </li>  
                                <#--</#if>-->
                              </#list>
                           </#if>
					     </ul>
					   </div>
		             </div>
					  <#if (topicsPage.last)??>
						 <#if (topicsPage.last gt 1)>
							<div class='page-inner'>
							  <div class="ui-pager pager-center">
								<div class='pager'>
								 
								  <div class="pager-inner">
									<div id="sharetopicpaging" class="page"></div>
									
								  </div>
								
								</div>
							  </div>
							</div>
						 </#if>
					  </#if>
		           </div>
	           </div>
	           </div>
	        <#if (attentionPage)??>
	         <div class="uc-mydocfocus">
	        <#else>
	         <div class="uc-mydocfocus" style="display:none">
	        </#if>
			   <div class="console-box">
		          <a class="current">关注</a>
	           </div>
	           <div class="focus">
                 <div class="docs-list-box">
                   <div class='delete-box normal-console-box clearfix'>
		               <span class="remove" data-type='2'><b class="iconfont"></b>&nbsp;&nbsp;删除</span>
		           </div>
	               <div class="status-box">
					  <div class="w340 ib"><div class="checkbox select-all"></div>话题名称</div>
					  <div class="w92 ib">点赞</div>
					  <div class="w92 ib">回复</div>
					  <div class="w92 ib">关注</div>
					  <div class="w140 ib">关注时间</div>
					  <div class="w119 ib">操作</div>
				   </div>
				   <div class="docs-list attention-list">
				     <ul>
				       <#if (attentionPage)??>
				         <#list attentionPage.list as alist>
				           <li>
				             <div class='w340 fs14 fc3 ib titlehidden'>
				                <div class="checkbox chk" data-topicid="${alist.id}" data-name="${alist.topics.title}"></div>
                                <a href="/topics/getTopicsDetail/${alist.objectid}" target="_blank" title="${alist.topics.title}">${alist.topics.title}</a>  
				             </div>
				             <div class='w92 ib iltopic'>
				                <#if (alist.topics.likesum)??>
				                  ${alist.topics.likesum}
				                </#if>
				             </div>
				             <div class='w92 ib iltopic'>
				                <#if (alist.topics.replysum)??>
				                  ${alist.topics.replysum}
				                </#if>
				             </div>
				             <div class='w92 ib iltopic'>
				                <#if (alist.topics.followssum)??>
				                  ${alist.topics.followssum}
				                </#if>
				             </div>
				             <div class='w140 ib'>
				                 <#if (alist.attentiontime?string("yyyy-MM-dd"))??>
				                    ${alist.attentiontime?string("yyyy-MM-dd")}
				                </#if>
				             </div>
				             <div class='w119 ib attention'>
				                    <span data-topicid='${alist.id}' data-id='${alist.objectid}' data-name='${alist.topics.title}' data-actiontype='0' data-titlename='${alist.topics.title}' data-createperson='${alist.userid}'> 
						                <a  class="topic-disblue" href='javascript:void(0)'>取消关注</a>
								    </span>
				             </div>
				           </li>
				         </#list>
				       </#if>
				     </ul>
				   </div>
	             </div>
				 <#if (attentionPage.last)??>
						 <#if (attentionPage.last gt 1)>
							<div class='page-inner'>
							  <div class="ui-pager pager-center">
								<div class='pager'>
								 
								  <div class="pager-inner">
									<div id="attentiontopicpaging" class="page"></div>
									
								  </div>
								
								</div>
							  </div>
							</div>
						 </#if>
				  </#if>
	           </div>
	     	</div>
	     	<#if (topicsCommentPage)??>
	     	   <div class="uc-mydocreply">
	     	<#else>
	     	  <div class="uc-mydocreply" style="display:none">
	     	</#if>
			   <div class="console-box">
					<a class="current">我的回复</a>
	           </div>
	            <div class="focus">
                 <div class="docs-list-box">
                   <div class='delete-box normal-console-box clearfix'>
		              <span class="remove" data-type='0'><b class="iconfont"></b>&nbsp;&nbsp;删除</span>
		           </div>
	               <div class="status-box">
					  <div class="w450 ib"><div class="checkbox select-all"></div>话题名称</div>
					  <div class="w214 ib">回复</div>
					  <div class="w140 ib">回复时间</div>
					  <div class="w92 ib">操作</div>
				   </div>
				   <div class="docs-list">
				     <ul>
				       <#if (topicsCommentPage)??>
				        <#list topicsCommentPage.list as clist>
				          <li>
				            <div class='w430 fs14 fc3 ib titlehidden'>
				              <div class="checkbox chk" data-topicid="${clist.id}" data-name="${clist.topics.title}"></div>
                              <a href="/topics/getTopicsDetail/${clist.topicsid}" target="_blank" title="${clist.topics.title}">${clist.topics.title}</a>  
				            </div>
				            <div class='w214 ib replayhidden' >
				              <#if (clist.commcontent)??>
				               <p title='${clist.commcontent?replace('white-space: normal;', '')}'> 
					                ${clist.commcontent?replace('white-space: normal;', '')}
					           </p>
				              </#if>
				            </div>
				            <div class='w140 ib'>
				                <#if (clist.createtime?string("yyyy-MM-dd"))??>
				                  ${clist.createtime?string("yyyy-MM-dd")}
				                </#if>
				            </div>
				            <div class='w92 ib operate'>
						       <span data-id='${clist.id}' data-name='${clist.topics.title}' data-type='0'> 
				                  <b class="iconfont pr2"></b>
				                    删除
						       </span>
				            </div>
				          </li>
				        </#list>
				       </#if>
				     </ul>
				   </div>
	             </div>
				 <#if (topicsCommentPage.last)??>
					 <#if (topicsCommentPage.last gt 1)>
						<div class='page-inner'>
						  <div class="ui-pager pager-center">
							<div class='pager'>
							 
							  <div class="pager-inner">
								<div id="commenttopicpaging" class="page"></div>
								
							  </div>
							
							</div>
						  </div>
						</div>
					 </#if>
				  </#if>
	           </div>
	     	</div>
	      </div>
      	  </div>
		</div>
		</div>
		</div>
	  </div>
	  <div class='pagetemplate'></div>
      <#include "/mydocs/commonTemplate/topandtail/tail.ftl"/>
      <#include "/mydocs/commonTemplate/bottomjs/bottomjs.ftl"/>
      <script type='text/javascript' src='/scripts/pj_mycentertopic.js'></script>
	  <script type="text/javascript">
		  <#if (topicsPage.list)??>
			$(function(){
				$("#sharetopicpaging").pagination({
					items: ${topicsPage.count},
					itemsOnPage:${topicsPage.pageSize},
					cssStyle: 'light-theme',
					moduleType:'topicshare'
				});
			});
		  </#if>
		  
		  <#if (attentionPage.list)??>
			  $(function(){
					$("#attentiontopicpaging").pagination({
						items: ${attentionPage.count},
						itemsOnPage:${attentionPage.pageSize},
						cssStyle: 'light-theme',
						moduleType:'topicattention'
					});
				});
		  </#if>
		  
		  <#if (topicsCommentPage.list)??>
			  $(function(){
					$("#commenttopicpaging").pagination({
						items: ${topicsCommentPage.count},
						itemsOnPage:${topicsCommentPage.pageSize},
						cssStyle: 'light-theme',
						moduleType:'topiccomment'
					});
				});
		  </#if>
	  </script>
  </body>
</html>