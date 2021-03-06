   <div id="header">
      <div class="header-row">
          <div class="header-alignleft">
             <div class="header-contact-info">
                <img src="/image/website-banners.png" alt='网站横幅 ' class="img-responsive lazy">
             </div>
          </div>
          <div class="header-alignright">
             <div class="header-secondary-menu">
               <ul id="menu-top-new" class="menu">
                <#if (Session.myHeadTop)??>
                 <li id="menu-item-5" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-5">
                       <span class='pm-img img'></span>
				      <#list Session.myHeadTop?keys as itemKey>
					     <#if itemKey="privateSmsSum">
					      <#if Session.myHeadTop[itemKey]!=0>
					       <span class="zg-noti-number zu-top-nav-pm-count" style="visibility:visible" data-count="${Session.myHeadTop[itemKey]}">
					         ${Session.myHeadTop[itemKey]}
					       </span>
					      </#if>
					     </#if>
			 		   </#list> 
                 </li>
                 <li id="menu-item-4" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-4">
                    <span class='smg-img img'></span>
			        <#list Session.myHeadTop?keys as itemKey>
				     <#if itemKey="newSmsSum">
				       <#if Session.myHeadTop[itemKey]!=0>
				          <span class="zg-noti-number zu-top-nav-smg-count" style="visibility:visible" data-count="${Session.myHeadTop[itemKey]}">
				            ${Session.myHeadTop[itemKey]}
				          </span>
				       </#if>
				      </#if>
		 		    </#list> 
                 </li>
                 </#if>
                 <#if (Session.user)??>
                 <input type="hidden" value="${Session.user.userid}" id="currentUserId">
                 <input type="hidden" value="${Session.user.username}" id="currentUserName">
                 <li id="menu-item-1" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1 pj-top-nav-userinfo">
                    <a href='/myCenter/getMyHeadTop' target='_self'>您好!  ${Session.user.username}</a>
                 </li> 
                 <li style='line-height:44px;font-size: 14px;'>|</li>
                 <li id="menu-item-3" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3">
                     <a href="javascript:void(0);" onclick="toLogout();">退出</a>
                 </li>
                <#else>
                 <li id="menu-item-1" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-1">
                    <a href="javascript:void(0);" onclick="toLogin();">请登录</a>
                 </li>
                 <li style='line-height:44px;font-size: 14px;'>|</li>
                 <li id="menu-item-3" class="menu-item menu-item-type-post_type menu-item-object-page menu-item-3">
                     <a href="/registration.html">注册</a>
                 </li>
                </#if>
               </ul>
             </div>
          </div>
      </div>
    </div>
    <div id="hd">
        <div class="box top-search-box">
          <div class="navbar-header">
            <a class="navbar-brand" href="/index">
               <img src="/image/pluslogo.png" alt='JobPlus' class="img-responsive lazy">
            </a>
          </div>
          <ul class='pj_tnav_wrap'>
            <li>
					<a href="/index" <#if indexPage==0>class='current'</#if> target='_self'>首页</a>
		    </li>
            <li>
					<a href="/knowledgeBaseIndex"  target='_self' <#if indexPage==1>class='current'</#if>>知识库</a>
		    </li>
		    <li>
					<a href="/about/newbie_guide" target='_self' <#if indexPage==2>class='current'</#if>>使用攻略</a>
		    </li>
		    <li>
					<a href="/about/feedback" target='_self' <#if indexPage==3>class='current'</#if>>建议反馈</a>
		    </li>
          </ul>
        </div>
    </div>
    <script src="/scripts/sockjs-0.3.min.js"></script>
	<script type="text/javascript">
	   
		//用户登录成功后需要初始化websocket以便服务器进行消息通知
		var socket;
        if(document.getElementById("currentUserId")){
            var currentUserId=document.getElementById("currentUserId").value;
            if ('WebSocket' in window) {
                socket = new WebSocket("ws://192.168.0.39:7397/ws?userId="+currentUserId);
            } else if ('MozWebSocket' in window) {
                socket = new MozWebSocket("ws://192.168.0.39:7397/ws?userId="+currentUserId);
            } else {
                socket = new SockJS("http://192.168.0.39:7397/ws/sockjs?userId="+currentUserId);
            }

            socket.onopen = function(event) {
                console.log("websocket open");
            };
            socket.onclose = function(event) {
                console.log("websocket close");
            };

            socket.onmessage = function(event) {
                //业务处理逻辑 event.data==1  私信+1，=0 系统通知+1
                if(event.data==1){
                    getSmsCount();
                }else{
                    getpmCount();
                }
            }
        }
    </script>