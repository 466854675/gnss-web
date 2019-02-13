var swfObj;
mediaIp = "127.0.0.1";
mediaPort = 7777;
$(document).ready(function () {
    loadSwfObj();

    //改变窗口个数
    $("#winNum").change(function () {
        swfObj.setWindowNum(this.value);
    });

    $("#video").click(function(){
        if (swfObj != null && swfObj["startVideo"]) {
            var simcard = $("#simcard").val();
            var streamType = $("#streamType").val();
            swfObj.startVideo(1, "", simcard, 0, streamType, true, "127.0.0.1", 7777);
        } else {
            alert("swf未加载完成");
        }
    });
});

//加载swf
function loadSwfObj() {
    setTimeout(function () {
        swfObj = document.getElementsByName("flashPlayer")[0];
        if (swfObj != null && swfObj["setWindowNum"]) {
            swfObj.setWindowNum(4);
        } else {
            loadSwfObj();
        }
    }, 1000);
}

