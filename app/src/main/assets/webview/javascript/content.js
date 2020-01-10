/*
 * 用于操纵 content 的行为和属性
 *
 * native 需要实现的接口有：
 * String providePlaceholder();
 * int provideContentMinHeight();
 * int onDocumentReady();
 * void onTextChanged(int length);
 *
 * native 可以调用的接口有：
 * void requestContentFocus();
 * void setContentMinHeight(int minHeight);
 * void setContentVisible(boolean visible);
 * void setPlaceholder(String placeholder);
 */

function requestContentFocus() {
	$('#content').focus();
}

function setContentMinHeight(minHeight) {
	$('#content').css('min-height', minHeight + 'px');
}

function setContentVisible(visible) {
	if (visible) {
		$('#content').css('visibility', 'visible');
	} else {
		$('#content').css('visibility', 'hidden');
	}
}

function setPlaceholder(placeholder) {
	$('#content').attr('placeholder', placeholder);
}

// =================================================================================================

function setupWhenContentEditable() {
	var content = $('#content');
	if (!content[0].hasAttribute('contenteditable')) {
		return;
	}

    //回答拦截用,当有图片默认文字数超过60,拦截消失
    if (content[0].getElementsByTagName("img").length > 0) {
      ContentBridge.onContentLength(70);
    }
	ContentBridge.onContentLength(content.text().length);

	content.bind('input', function() {
		var node = document.getSelection().anchorNode;
		if (node.nodeType == 3) {
			var selector = $(document.getSelection().anchorNode.parentNode);
			if (selector.hasClass('member_mention')) {
				selector.remove();
			}else if($(node.parentNode).is("span")
			        && document.getSelection().getRangeAt(0).startOffset === 0){

			    var ps= node.parentNode.previousSibling;
			    if($(ps).is("img")
			        && ps === content[0].getElementsByTagName("img").item(0)){
                    window.getSelection().removeAllRanges();

                    var range = document.createRange();
                    range.selectNode(ps);
                    window.getSelection().addRange(range);
                }
            }

		}
		var num = content.text().length;
		var numImg = content[0].getElementsByTagName("img").length;
		if(num == 0){
			//一张图算一个字
			num = content[0].getElementsByTagName("img").length
		}

    ContentBridge.onImgChanged(numImg);

		ContentBridge.onTextChanged(num);

		ContentBridge.onVideosChanged(getOwnVideoLinks());
	});

	// paste 回调只会获取粘贴之前的光标位置，需要自己手动加上粘贴文本的长度，并保证粘贴的是纯文本
	content.on('paste', function(e) {
		e.preventDefault();
		var text = (e.originalEvent || e).clipboardData.getData('text/plain');
		text = text.replace(/\n/g, '<br>');
		document.execCommand("insertHTML", false, text);
	});

	requestContentFocus();
}

$(document).ready(function() {
	var placeholder = ContentBridge.providePlaceholder();
	var contentMinHeight = ContentBridge.provideContentMinHeight();
	setPlaceholder(placeholder);
	setContentMinHeight(contentMinHeight);
	setupWhenContentEditable();
	setContentVisible(true);
	ContentBridge.onDocumentReady();
	ContentBridge.onVideosChanged(getOwnVideoLinks());
});