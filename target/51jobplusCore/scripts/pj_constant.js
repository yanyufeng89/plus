var projectName = '/';
var url_rules = {
    "tbl_topics": "topics/getTopicsDetail/",
    "TOPICS": "topics/getTopicsDetail/",
    "tbl_books": "books/getBookDetail/",
    "BOOK": "books/getBookDetail/",
    "6": "books/getBookDetail/",
    "tbl_courses": "courses/getCourseDetail/",
    "COURSES": "courses/getCourseDetail/",
    "tbl_sites": "sites/getSiteDetail/",
    "SITES": "sites/getSiteDetail/",
    "tbl_article": "article/getArticleDetail/",
    "ARTICLE": "article/getArticleDetail/",
    "tbl_docs": "docs/getDocsDetail/",
    "DOC": "docs/getDocsDetail/"
};


var name_rules = {
    "tbl_topics": "话题",
    "TOPICS": "话题",
    "tbl_books": "书籍",
    "BOOK": "书籍",
    "tbl_courses": "课程",
    "COURSES": "课程",
    "tbl_sites": "站点",
    "SITES": "站点",
    "tbl_article": "文章",
    "ARTICLE": "文章",
    "tbl_docs": "文档",
    "DOC": "文档"
};

/**
 * 组装超链接url
 * @param type
 * @param id
 * @returns {string}
 */
function getLinkUrl(type, id) {
    return projectName + url_rules[type] + id;
}

/**
 * 获取类型名称
 * @param type
 * @param id
 * @returns {string}
 */
function getTypeName(type) {
    return name_rules[type];
}

/**
 * 打开新窗口跳转
 * @param type
 * @param id
 */
function toHref(type, id) {
    window.open(getLinkUrl(type, id));
}

/**
 * 跳转
 * @param type
 * @param id
 * @param isSelf ture:当前页面跳转 false:打开新窗口
 */
function toHref(type, id, isSelf) {
    if (isSelf)
        window.location.href = getLinkUrl(type, id);
    else
        window.open(getLinkUrl(type, id));
}

/**
 * 配置全局ajax配置
 * @returns {boolean}
 */
$.ajaxSetup({
    complete: function (XMLHttpRequest, textStatus) {
        if (textStatus == "parsererror") {
            toLogin();
        } else if (textStatus == "error") {
            //alert("服务器忙，请稍后再试！");
        }
    }
});

String.prototype.startWith = function (str) {
    if (str == null || str == "" || this.length == 0 || str.length > this.length)
        return false;
    if (this.substr(0, str.length) == str)
        return true;
    else
        return false;
    return true;
}

function getBackUrl() {
    var currentUrl = window.location.href.replace("http://", "");
    var backurl = encodeURI(currentUrl.substring(currentUrl.indexOf(projectName) + projectName.length, currentUrl.length));
    if (backurl.startWith("?"))
        backurl = "";
    else if (backurl.indexOf("?message=") > -1) {
        backurl = backurl.substring(0, backurl.indexOf("?message="));
    }
    return backurl;
}

function getLoginUrl() {
    return projectName + "login?backurl=" + getBackUrl();
}

function toLogin() {
    window.location.href = projectName + "login?backurl=" + getBackUrl();
}

function toLogout() {
    window.location.href = projectName + "logout?backurl=" + getBackUrl();
}

var addEvent = (function () {
    if (document.addEventListener) {
        return function (el, type, fn) {
            if (el.length) {
                for (var i = 0; i < el.length; i++) {
                    addEvent(el[i], type, fn);
                }
            } else {
                el.addEventListener(type, fn, false);
            }
        };
    } else {
        return function (el, type, fn) {
            if (el.length) {
                for (var i = 0; i < el.length; i++) {
                    addEvent(el[i], type, fn);
                }
            } else {
                el.attachEvent('on' + type,
                    function () {
                        return fn.call(el, window.event);
                    });
            }
        };
    }
})();

addEvent(window, 'storage', function (event) {
    if (event.key == 'pmCount') {
        // getpmCount();
        $('#menu-item-4 .zg-noti-number').html(localStorage.pmCount);
    }
    if (event.key == 'smsCount') {
        // getSmsCount();
        $('#menu-item-5 .zg-noti-number').html(localStorage.smsCount);
    }
});

function setCurrentLocationForBackUrlCookie() {
    var currentUrl = window.location.href.replace("http://", "");
    if (currentUrl.indexOf("registration") == -1 && currentUrl.indexOf("login") == -1) {
        var backurl = encodeURI(currentUrl.substring(currentUrl.indexOf(projectName) + projectName.length, currentUrl.length));
        if (backurl.startWith("?"))
            backurl = "";
        else if (backurl.indexOf("?message=") > -1) {
            backurl = backurl.substring(0, backurl.indexOf("?message="));
        }
        setBackUrlCookie(backurl);
    }
}

function setBackUrlCookie() {
    var paramName = "backurl=";
    var currentUrl = window.location.href.replace("http://", "");
    var backurl = "";
    if (currentUrl.indexOf(paramName) > -1)
        backurl = encodeURI(currentUrl.substring(currentUrl.indexOf(paramName) + paramName.length, currentUrl.length));
    setBackUrlCookie(backurl);
}

function setBackUrlCookie(backurl) {
    $.cookie("backurl", backurl);
}

function deleteBackUrlCookie() {
    $.cookie('backurl', '', {expires: -1});
}

function getBackUrlCookie() {
    return $.cookie("backurl");
}

function isEmail(str) {
    if (/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(str))
        return true;
    return false;
}

function isTel(str) {
    if (/^1[34578]\d{9}$/.test(str))
        return true;
    return false;
}