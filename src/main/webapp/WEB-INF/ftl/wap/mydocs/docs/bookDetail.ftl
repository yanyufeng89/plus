<!DOCTYPE html>
<html class="expanded">
  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width,height=device-height, user-scalable=no,initial-scale=1, minimum-scale=1, maximum-scale=1">
    <title>
       ${record.bookname}-JobPlus
    </title> 
	<link rel='stylesheet' type='text/css' href='/css/pj_wapdetail.css'>
    <link rel='stylesheet' type='text/css' href='/css/pj_wapbase.css'>
  </head>
  <body>
    <div class="wrap bc">
      <#include "/mydocs/commonTemplate/topandtail/waptop.ftl"/>
        <div class="main bookMain">
           <input type='hidden' name='bookid' value='${record.id}'>
           <input type='hidden' name='bookcontent' value='${record.intro?trim}'>
           <div class="plus-main-content-inner book-content">
				  <#if (record)??>
						<div class='bookmain-left'>
	                           <#if (record.bookimg)??>
	                             <img src="${record.bookimg}" class="book-logo lazy" alt="书籍图片" height='220px' width='158px'>
	                            <#else>
	                              <img src="/image/thief.jpg" class="book-logo lazy" alt="书籍图片" height='220px' width='158px'>
	                           </#if>
	                     </div>
	                     
	                     <div class='bookmain-right'>
                             <div class='headline'>${record.bookname}</div>
                             <div class='author'>
                                <span class='tip'>作者:</span>
                                <span class='noborder'>${record.author}</span>
                             </div>
                             <div class='press'>
                                <span class="tip">出版社:</span>
                                <span class='noborder'>${record.press}</span>
                             </div>
                             <a href='${record.supplierUrl}' target='_blank' class='purchaseurl zg-btn-white' rel='nofollow'>购买本书</a>
	                     </div>
				  </#if>
           </div>
           <div class='book-introduction'>
             <div class='headline'>内容简介:</div>
             <div class='description tool-height'>
                ${record.intro?trim}
                <span class="slidedown slide" style="display: none;"><label>...&nbsp;&nbsp;&nbsp;</label>展示全部</span>
                <div class="slideup slide" style="display: none;"><span>收起</span></div>
             </div>
           </div>
		</div>
		<div class='relatedcontent'>
		<div class='userrecommend'> 用户推荐${record.recommendsum}</div>
		<div class='detail'>
		  <div class='detail-list'>
		  <#if record.commentList.list?size lte 0>
		     <p class='uncomment'>暂无推荐，你也可以发布推荐哦:) </p>
		  </#if>
		  <#if (record.commentList)??>
		     <#list  record.commentList.list as booklist>
		       <div class='item'>
		         <div class='media-left'>
		           <a class='uhead' href='javascript:void(0)' target='_blank' data-userid='${booklist.userid}'>
		             <#if (booklist.userHeadIcon)??>
		               <img class='uname lazy' src="${booklist.userHeadIcon}" alt="个人头像"  data-userid='${booklist.userid}' data-moduletype='1'>
		             <#else>
		               <img class='uname lazy' src='/image/1b48b5a75c71597_100x100.jpg' alt="个人头像" data-userid='${booklist.userid}' data-moduletype='1'>
		             </#if>
		             <#if (booklist.commentbyName)??>
			                <a href='javascript:void(0)' class="uname" data-userid='${booklist.userid}' target='_blank'>${booklist.userName}</a>
			                  <span class="desc">&nbsp;回复&nbsp;</span>
			                <span class="uname"  data-userid='${booklist.commentby}' data-moduletype='1'>
			                <a href='javascript:void(0)' target='_blank'>${booklist.commentbyName}</a>
			                </span>
		             <#else>
			                <a href='javascript:void(0)' class='uname' data-userid='${booklist.userid}' target='_blank'>${booklist.userName}</a>
		             </#if>
		           </a>
		         </div>
		         <div class='media-right'>
		           <div class="reason">${booklist.recommend?html}</div>
		           <div class="operate">
		             <a itemprop="url" class="answer-date-link meta-item" target="_self" href="javascript:;">
		               <#if (booklist.createtime?string("yyyy-MM-dd"))??>
		                  ${booklist.createtime?string("yyyy-MM-dd")}
		               </#if>
					 </a>
		           </div>
		         </div>
		       </div>
		     </#list>
		  </#if>
		 </div>
		  
		</div>
		<#if (record.commentList.last gt 1)>
			   <a data-pageno='1' data-sumpage='${record.commentList.last}' class="zg-btn-white zu-button-more loadmore">更多</a>
	    </#if>
	    </div>
      
	</div>
	 <div class='pagetemplate'></div>
	 <script type="text/javascript" src="/scripts/jquery-1.8.0.min.js"></script>
	 <script type="text/javascript" src="/scripts/jquery-jtemplates.js"></script>
     <script type="text/javascript" src="/scripts/pj_wap.js"></script>
	 <script type="text/javascript">
	         var phoneWidth =  parseInt(window.screen.width);
	         var phoneScale = phoneWidth/640;
	         var ua = navigator.userAgent;
	         if (/Android (\d+\.\d+)/.test(ua)){
	                   var version = parseFloat(RegExp.$1);
	                   if(version>2.3){
	                            document.write('<meta name="viewport" content="width=640, height=device-height minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
	                   }else{
	                            document.write('<meta name="viewport" content="width=640, height=device-height target-densitydpi=device-dpi">');
	                   }
	         } else {
	                   document.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
	         }
	 </script>
  </body>

</html>
