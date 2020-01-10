/*
 * 调整 video-box 样式
 *
 * 依赖：
 * zepto.min.js
 */

    var SUCCESS = "success";
    var DATA_LENS_ID = "data-lens-id";
    var DATA_POSTER = "data-poster";
    var DATA_DURATION = "data-duration";
    var DATA_NAME = "data-name";

    var videoCoverArray = [];

function autoLayoutVideoBox() {

    $('.video-box .title').each(function () {
        var title = $(this);

        var fontSize = title.css('font-size');
        fontSize = parseFloat(fontSize);

        var height = title.height();
        height = parseFloat(height);

        var count = height / fontSize;
        if (count < 2) {
            title.css('line-height', 'normal');
        } else {
            title.css('line-height', '125%');
        }
    });

    //如果检测到video-box 中包含 data-lens-id, 则代表这是一个自有视频，将它转变成 own-video class，video-box就不要了
    $('.video-box').each(function () {
        var videoBox = $(this);

        var videoId = videoBox.attr(DATA_LENS_ID);

        if (videoId) {
            //这是自有视频
            videoBox.addClass('own-video');
            videoBox.removeClass('video-box');

            //将无用的元素去掉
            videoBox.empty();

            //获得视频名称
            var poster = videoBox.attr(DATA_POSTER);
            var title = videoBox.attr(DATA_NAME);
            var duration = videoBox.attr(DATA_DURATION);

            videoBox.append('<div class="own-video-wrap" contenteditable="false">' +
                createImgDiv(poster) +
                createDurationDiv(duration) +
                '</div>');

            if (title)
                videoBox.append(createTitleDiv(title));

            videoBox.click(function (event) {
                var videoBox = $(this);

                //唤起本地播放器
                VideoBridge.openVideo(videoId,title,poster,null);
            });

            videoBox.attr('href',"#");

            VideoBridge.getVideoInfo(videoId);
        }
    });

}

    function updateVideoInfo(videoId,videoInfoJsonString) {

//        alert(videoInfoJsonString);

        var videoInfo = JSON.parse(videoInfoJsonString);

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
                    ownVideoWrap.find('.thumbnail').attr('original-src', videoInfo.thumbnail);
                    ImageBridge.loadImage(videoInfo.thumbnail);

                    if (!videoInfo.thumbnail)
                        ownVideoWrap.find('.thumbnail').remove();
                    /////////////////////////////////////////////////////////////////////////////////////////


                    //时长
                    if (videoInfo.duration && !ownVideoWrap.find('.data-duration')) {
                        //之前处于无图状态，需添加上img
                        ownVideoWrap.append(createDurationDiv(videoInfo.duration), 0);
                    }

                    ownVideoWrap.find('.data-duration').text(videoInfo.duration);

                    if (!videoInfo.duration)
                        ownVideoWrap.find('.data-duration').remove();
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

     function createImgDiv(videoUrl) {
            if (videoUrl.startsWith("http")) {
                ImageBridge.loadImage(videoUrl);
            }
            return videoUrl ? '<img class="thumbnail" src="' + videoUrl + '" original-src="'+videoUrl+'">' : '';
        }

        function createTitleDiv(videoTitle) {
            return videoTitle ? '<div class="video-title">' + videoTitle + '</div>' : '';
        }

        function createDurationDiv(videoDuration) {
            return videoDuration ? '<div class="data-duration">' + videoDuration + '</div>' : '';
        }

    function createVideoWrap(videoInfo) {
        if (videoInfo.is_completed) {

            return '<div class="own-video-wrap" contenteditable="false">'
                + createImgDiv(videoInfo.thumbnail)
                + createDurationDiv(videoInfo.duration)
                + '</div>';
        }
    }

        function createFailedCover() {
            return '<div class="own-video-wrap-failed" contenteditable="false">视频上传失败</div>';
        }

