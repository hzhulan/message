<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Pudding's Pic</title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="./bootstrap/css/bootstrap.min.css" crossorigin="anonymous">
</head>
<body>
<style type="text/css">

    #content {
        width: 1865px;
        margin: 0px auto;
        /*padding: 20px auto 0px;*/
        border: 1px solid beige;
        resize: both;
        overflow: hidden;
        /*background: #80808038;*/
    }

    #albumTitle {
        line-height: 80px;
        text-align: center;
        font-size: 20px;
        height: 80px;
        box-sizing: border-box;
        border-bottom: 1px solid #dddd;
        background: linear-gradient(#f3c51e, #ffffff);
    }

    #imgContent {
        display: flex;
        flex-direction: row;
        flex-wrap: wrap;
        width: 100%;
        height: 100%;
        align-content: flex-start;
    }

    #pageContent {
        padding-right: 150px;
        float: right;
    }

    .photo {
        flex: 0 0 100px;
        border: 1px solid darkgrey;
        box-sizing: border-box;
        width: 300px;
        height: 400px;
    }
    .imgName {
        width: 300px;
    }

</style>

<div id="content">
    <div id="albumTitle">相册</div>
    <div id="imgContent">
    </div>

</div>
<div id="pageContent">
    <nav aria-label="Page navigation">
        <ul class="pagination">
            <li>
                <a href="#" aria-label="Previous">
                    <span id="lastPage" aria-hidden="true">上一页</span>
                </a>
            </li>
            <li>
                <a href="#" aria-label="Next">
                    <span id="nextPage" aria-hidden="true">下一页</span>
                </a>
            </li>
        </ul>
    </nav>
</div>

</body>
<script type="text/javascript" src="${ctx}/js/common/jquery-3.3.1.js"></script>
<script type="text/javascript" src="${ctx}/bootstrap/js/bootstrap.min.js"></script>

<script type="text/javascript">
    var ctx = '${ctx}';
    var albumId = '${albumId}';
    var pageNo = 1;
    var pageSize = 12;
    var maxPage = 1;

    function loadImg(pageNo, pageSize) {
        $.ajax({
            url: ctx + '/fileSys/list',
            type: 'post',
            data: {albumId: albumId, pageNo: pageNo, pageSize: pageSize},
            success: function (data) {
                $("#imgContent").empty();
                if (data) {
                    var totalSize = data.page.totalSize;
                    maxPage = parseInt(totalSize / pageSize) + 1;
                    $.each(data.data, function (index, fileObj) {
                        var imgHtml = '<div style="margin: 0 5px;">' +
                            '<div><img class="photo" src="' + fileObj.url + '"></div>' +
                            '<div class="imgName" align="center">'+ fileObj.fileName +'</div>' +
                            '</div>';
                        $("#imgContent").append(imgHtml);
                    });


                    $("#pageContent").find("li").removeClass("disabled");
                    if (pageNo == 1) {
                        $("#lastPage").parent().parent("li").addClass("disabled");
                    }
                    if (maxPage == pageNo) {
                        $("#nextPage").parent().parent("li").addClass("disabled");
                    }
                }
            }
        })
    }

    loadImg(pageNo, pageSize);

    $("#imgContent").on('click','.photo', function () {
        var url = $(this).attr("src");
        if (url) {
            window.open(url);
        } else {
            console.log("找不到有效链接")
        }
    });

    // 上一页
    $("#lastPage").click(function () {
        if ($(this).parent().parent("li").hasClass("disabled")) {
            return;
        }
        pageNo = pageNo - 1  <= 1 ? 1 : pageNo - 1;
        loadImg(pageNo, pageSize)
    });
    $("#nextPage").click(function () {
        if ($(this).parent().parent("li").hasClass("disabled")) {
            return;
        }
        pageNo = pageNo + 1;
        loadImg(pageNo, pageSize)
    });
</script>

</html>