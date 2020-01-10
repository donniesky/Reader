/*
 <a own-video>
 <div video-wrap>
 <img thumbnail>
 <duration>
 </div>
 <div>duration</div>
 </a>
 */
var DATA_LENS_ID = "data-lens-id";
var VIDEO_UPLOAD_FAIL_STRING = "[视频上传失败]";
var VIDEO_UPLOAD_FAIL_TAG = "<a class=\"unprocessable_video uploading_fail\" href=\"javascript:;\" data-lens-status=\"uploading_fail\" data-lens-id=\"\" data-description=\"[视频上传失败]\" contenteditable=\"false\"></a>";

function autoLayoutVideoLink() {

    autoLayoutVideoLink(true);
}

function autoLayoutVideoLink(needInsertBr) {

    $(".video-box").addClass("video-link").removeClass("video-box");
    //  删掉，多余的标签, 主要用于 video-box 替换成 video-link 时 移除下方多余tag
    $(".video-link span").remove();

    var POLLING_INTERVAL = 5000;

    //通过 dataLensId - interval 的方式记录所有轮询
    var intervalHolder = {};
    var videoCoverArray = [];

    var firstVideoLink = true;

    $('.video-link[' + DATA_LENS_ID + ']').each(function () {

        var videoLink = $(this);

        //查看是不是有自有视频独有的 data-lens-id 是的话就加入 own-video class
        var videoLensId = videoLink.attr('data-lens-id');
        videoLink.addClass('own-video');
        videoLink.attr('contenteditable', 'false');
        var videoTitle = videoLink.text();
        videoLink.text("");
        var poster = videoLink.attr('data-poster');

        videoLink.click(function (event) {
            event.preventDefault();
        })

        var thumbnail = createImgDiv(poster);

        videoLink.append('<div class="own-video-wrap" contenteditable="false">' + thumbnail + '<img id="video-delete-button" class="video-delete"/></div>');

        //点击删除按钮的时候将视频卡片删除
        videoLink.find("#video-delete-button")
                 .click(function (event) {
                     videoLink.remove();
                     ContentBridge.onVideosChanged(getOwnVideoLinks());
                 })

        //由于会出现第一个出现的 videolink 在编辑器最前面导致光标丢失，第一个 videolink 前统一加上一个 <br>
        if(firstVideoLink){
            videoLink.before('<br>');
            firstVideoLink = false;
        }
        // 前后加上 div 和 br 才能正常删除这个 video-link a 标签
        videoLink.wrap('<div>');
        videoLink.after('<br>');
        if(needInsertBr){
            videoLink.parent().before('<br>');
            videoLink.parent().after('<br>');
        }

        if (videoTitle)
            videoLink.append(createTitleDiv(videoTitle));
    });


    function updateVideoInfo(videoInfo) {

        console.log("更新视频信息");

        //将 videoId 符合的 own-video 更新状态
        var selector = '.own-video[' + DATA_LENS_ID + '="' + videoInfo.id + '"]';
        $(selector).each(function () {

            //更新标题、时长、截图、状态

            //标题
            var ownVideo = $(this);
            if (!videoInfo.title) {
                ownVideo.find('.video-title').remove();
            } else {
                if (!ownVideo.find('.video-title')) {
                    ownVideo.append(createTitleDiv(videoInfo.title));
                }
                ownVideo.find('.video-title').text(videoInfo.title);
            }
            /////////////////////////////////////////////////////////////////////////////////////////


            var ownVideoWrap = ownVideo.find('.own-video-wrap').first();
            var ownVideoUploadingWrap = ownVideo.find('.own-video-wrap-uploading').first();

            if (videoInfo.is_completed) {
                if (ownVideoWrap) {
                    //更新 card 信息
                    //如果原来就有 card ，则更新信息

                    //封面
                    if (videoInfo.thumbnail && !ownVideoWrap.find('.thumbnail')) {
                        //之前处于无图状态，需添加上img
                        ownVideoWrap.append(createImgDiv(videoInfo.thumbnail), 0);
                    }

                    ownVideoWrap.find('.thumbnail').attr('src', videoInfo.thumbnail);

                    if (!videoInfo.thumbnail)
                        ownVideoWrap.find('.thumbnail').remove();
                    /////////////////////////////////////////////////////////////////////////////////////////


                    //时长
                    if (videoInfo.duration && !ownVideoWrap.find('.video-duration')) {
                        //之前处于无图状态，需添加上img
                        ownVideoWrap.append(createDurationDiv(videoInfo.duration), 0);
                    }

                    ownVideoWrap.find('.video-duration').text(videoInfo.duration);

                    if (!videoInfo.duration)
                        ownVideoWrap.find('.video-duration').remove();
                    /////////////////////////////////////////////////////////////////////////////////////////

                }

                if (ownVideoUploadingWrap) {
                    //如果原来是别的状态，则用成功的 card 替换掉
                    ownVideoUploadingWrap.replaceWith(createVideoWrap(videoInfo));
                }

            } else {
                //失败的情况
                var failedCover = createFailedCover();

                if (ownVideoWrap) {
                    ownVideoWrap.replaceWith(failedCover);
                }

                if (ownVideoUploadingWrap) {
                    ownVideoUploadingWrap.replaceWith(failedCover);
                }
            }

        });
    }

}

function createFailedCover() {
    return '<div class="own-video-wrap-failed" contenteditable="false">视频上传失败</div>';
}

function createImgDiv(videoUrl) {
    // bugfix : android4.4.4 can't find startWith
    if (!String.prototype.startsWith) {
       String.prototype.startsWith = function(searchString, position){
            return this.substr(position || 0, searchString.length) === searchString;
       };
    }
    //如果编辑器插入视频卡片的图片是本地的，不需要经过 fresco
    if (videoUrl.startsWith("http")) {
        ImageBridge.loadImage(videoUrl);
    }
    return videoUrl ? '<img class="thumbnail" src="' + videoUrl + '" original-src="' + videoUrl + '">' : ''; //通过 videoId 作为图片的元素 id，在上传视频封面之后更新封面元素的 original-src
}

function createTitleDiv(videoTitle) {
    return '<div class="video-title"><span>' + videoTitle + '</span></div>';
}

function createDurationDiv(videoDuration) {
    return videoDuration ? '<div class="video-duration">' + videoDuration + '</div>' : '';
}

function createVideoWrap(videoInfo) {
    if (videoInfo.is_completed) {

        return '<div class="own-video-wrap" contenteditable="false">'
            + createImgDiv(videoInfo.thumbnail)
            + createDurationDiv(videoInfo.duration)
            + '</div>';
    }
}

function createVideoLink(videoId, videoPoster, videoTitle, videoHref) {
    return '<a class="video-link" data-src="" href="' + videoHref + '" data-videoid="" data-lens-id="' + videoId + '" data-poster="' + videoPoster + '" data-name="' + videoTitle + '">'
}


function insertOwnVideoCard(videoId, videoPoster, videoTitle, videoHref) {
    //插入 video-link 然后
    var videoLink = createVideoLink(videoId, videoPoster, videoTitle, videoHref);
    if (document.queryCommandSupported("insertHTML")) {
        document.execCommand("insertHTML", false, videoLink);
    }
    autoLayoutVideoLink();
}

function filterInvalidVideos(){
 var selector = '.unprocessable_video';
    $(selector).each(function (index, obj) {
                            $(obj).html("");
                        });
}

function updateVideoPoster(videoId, updatedVideoPoster) {

    //将 videoId 符合的 own-video 更新状态
    var selector = '.video-link[' + DATA_LENS_ID + '="' + videoId + '"]';
    $(selector).each(function () {

        var videoLink = $(this);
        videoLink.attr("data-poster", updatedVideoPoster);
    });
}

function videoUploadFailed(videoId){
      var selector = '.video-link[' + DATA_LENS_ID + '="' + videoId + '"]';
        $(selector).each(function () {

            var videoLink = $(this);

            var videoUploadFailedMsgSpan = document.createElement("span");
            videoUploadFailedMsgSpan.innerHTML = VIDEO_UPLOAD_FAIL_TAG;
            videoLink.after(videoUploadFailedMsgSpan);
            videoLink.remove();

        });
}

function removeVideo(videoId){
      var selector = '.video-link[' + DATA_LENS_ID + '="' + videoId + '"]';
      $(selector).each(function () {
          var videoLink = $(this);
          videoLink.remove();
          ContentBridge.onVideosChanged(getOwnVideoLinks());
      });
}

function getOwnVideoLinks(){
      var selector = '.video-link[data-lens-id]';
      var videoArray = [];
      $(selector).each(function () {

          var videoLink = $(this);
          videoArray.push(videoLink.attr(DATA_LENS_ID));
      });

      return videoArray;
}

function videoUploadSuccess(videoId) {
    var selector = '.video-link[' + DATA_LENS_ID + '="' + videoId + '"]';
    $(selector).each(function () {
        var videoLink = $(this);
        videoLink.attr("href","https://www.zhihu.com/video/"+videoId);
    });
}

function changeUnprocessableVideoStyle(){
	$(".unprocessable_video").each(function(index, element) {
        var className = $(element).attr('data-lens-status');
        var tag = $(element);
        tag.addClass(className);
        tag.attr('contenteditable', 'false');

        tag.wrap('<div>');
        tag.after('<br>');

        if (!tag.parent().before().is('br')) {
            $('<br>').insertBefore($(element))
        }
        if (!tag.parent().next().is('br')) {
            $('<br>').insertAfter($(element))
        }
    });
}