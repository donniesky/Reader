/*
 * 设置图片的默认加载行为
 *
 * native 需要实现的接口有:
 * String readImageCache(String url);
 * boolean isAutoLoadImage();
 * void loadImage(String url);
 * void openImage(String urls, int index);
 *
 * native 可以调用的方法有:
 * void onImageLoadFailed(String url);
 * void onImageLoadSuccess(String url, String localUrl);
 */

var IMAGE_HOLDER_CLICK_TO_LOAD_URL = 'file:///android_asset/webview/resource/image_holder_click_to_load.png';
var IMAGE_HOLDER_LOAD_FAILED_URL = 'file:///android_asset/webview/resource/image_holder_load_failed.png';
var IMAGE_HOLDER_LOADING_URL = 'file:///android_asset/webview/resource/image_holder_loading.gif';

function findImageByUrl(url) {

    //先查看静态图片url是否存在于某个dom的data-thumbnail中，有的话代表这是一个gif，不同于普通图片存在于original-src
    var gifDom = $('img[data-thumbnail="' + url + '"]');
    if (gifDom && gifDom.length > 0) {
        return gifDom;
    }

	return $('img[original-src="' + url + '"]');
}

function onImageLoadFailed(url) {
	var image = findImageByUrl(url);
	if (image) {
		image.attr('src', IMAGE_HOLDER_LOAD_FAILED_URL);
	}
}

function onImageLoadSuccess(url, localUrl) {
	var image = findImageByUrl(url);
	if (image) {
		image.removeClass('image-holder');
		image.attr('src', localUrl);

		var dataThumbnail = image.attr('data-thumbnail');

        if(dataThumbnail){
        	if(!image.parent().hasClass('gif_wrap')){//避免重复添加gif_wrap
        	    image.wrap('<div class="gif_wrap" contenteditable="false"></div>');
        	    image.parent().wrap('<div contenteditable="false"></div>');
        	}
        }
	}
}

function isVideoImage(image) {
	return image.parent().prop('tagName').toLowerCase() == 'a';
}

function isEquationImage(image) {
	return image[0].hasAttribute('eeimg');
}

function isLinkBoxImage(image) {
	return image.parent().hasClass('link-box');
}

function isLinkBoxHolderHolderImage(image) {
	return image.hasClass('link-box-holder-bg-thumbnail') || image.hasClass('link-box-holder-bg-content');
}

function isPinTypeUpdateImage(image) {
	return image.parent().hasClass('update');
}

function loadImage(url) {
	ImageBridge.loadImage(url);
}

/**
如果image存在data-thumbnail代表这是一个gif image，data-thumbnail是他的静态图
*/
function setupImage(callOpenImage) {
	var urls = [];
	var index = 0;

	$('img').each(function() {
		var image = $(this);

		var originalUrl = image.attr('src');
		image.attr('original-src', originalUrl);

        var dataThumbnail = image.attr('data-thumbnail');

		if (!isVideoImage(image) && !isEquationImage(image)
				&& !isLinkBoxImage(image) && !isLinkBoxHolderHolderImage(image)
				&& !isPinTypeUpdateImage(image)) {
			urls.push(originalUrl);
			image.attr('index', index ++);
		}

		// 不走图片加载库
		if (isEquationImage(image) || isLinkBoxHolderHolderImage(image) || isPinTypeUpdateImage(image)) {
			return;
		}

		var cacheUrl = ImageBridge.readImageCache(originalUrl);
		var isAutoLoadImage = ImageBridge.isAutoLoadImage();

		if (!dataThumbnail && cacheUrl) {
			image.attr('src', cacheUrl);
		} else if (isAutoLoadImage) {
			image.addClass('image-holder');
			image.attr('src', IMAGE_HOLDER_LOADING_URL);
			image.unveil(200, function() {

			    if(dataThumbnail){
			        ImageBridge.loadImage(image.attr('data-thumbnail'));
			    }else{
				    ImageBridge.loadImage(originalUrl);
			    }
			});
		} else {
			image.addClass('image-holder');
			image.attr('src', IMAGE_HOLDER_CLICK_TO_LOAD_URL)
		}
	});

	$('img').click(function(event) {
		var image = $(this);

		var url = image.attr('src');
		var originalUrl = image.attr('original-src');
		if (url == IMAGE_HOLDER_CLICK_TO_LOAD_URL || url == IMAGE_HOLDER_LOAD_FAILED_URL) {
			image.attr('src', IMAGE_HOLDER_LOADING_URL);

			 var dataThumbnail = image.attr('data-thumbnail');

             if(dataThumbnail){
             	ImageBridge.loadImage(dataThumbnail);
             }else{
             	ImageBridge.loadImage(originalUrl);
             }
			event.preventDefault();
			event.stopPropagation();
			return;
		}

		if (!isVideoImage(image) && !isEquationImage(image) &&
				!isLinkBoxImage(image) && !isLinkBoxHolderHolderImage(image)
				&& !isPinTypeUpdateImage(image) && callOpenImage) {
			var index = 0;
			$('img').each(function() {
				if (!isVideoImage(image) && !isEquationImage(image)
						&& !isLinkBoxImage(image) && !isLinkBoxHolderHolderImage(image)
						&& !isPinTypeUpdateImage(image)) {
					if (event.target === this) {
						index = parseInt($(this).attr('index'));
					}
				}
			});

			ImageBridge.openImage(JSON.stringify(urls), index);
			event.preventDefault();
			event.stopPropagation();
		}
	});
}