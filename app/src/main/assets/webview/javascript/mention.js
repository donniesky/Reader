/*
 * 提供 @ 人的操作
 *
 * 依赖：
 * caret.js
 *
 * native 需要实现的接口有：
 * void onMentionKeyUp();
 *
 * native 可以调用的接口有：
 * void appendMention(long id, String name);
 */

var mentionBeforeLength = -1;
var mentionAfterLength = -1;

function appendMention(id, name) {
	if (mentionAfterLength > -1) {
		var content = $('#content')[0];
		setCaretPosition(content, mentionAfterLength, mentionAfterLength);
		mentionAfterLength = -1;

		if (isAt()) {
			document.execCommand('Delete', 'false', null);
		}
	}

	var mentionHtml = '&nbsp;<a data-hash="' + id + '" href="https://www.zhihu.com/people/' + id + '" class="member_mention">@' + name + '</a>&nbsp;'
	document.execCommand('insertHTML', false, mentionHtml);

	// 简单 hack
	try {
		renderHashTag();
	} catch (e) {};
}

function isAt() {
	var selText = window.getSelection().anchorNode.textContent.substring(0, window.getSelection().anchorOffset);
	if (selText.length > 0) {
		var lastChar = selText.substr(selText.length - 1);
		return lastChar == '@';
	}

	return false;
}

$(document).ready(function() {
	var content = $('#content');

	content.keydown(function() {
		mentionBeforeLength = getCaretOffset(content[0]);
	});

	content.keyup(function() {
		mentionAfterLength = getCaretOffset(content[0]);
		if (mentionAfterLength > mentionBeforeLength && isAt()) {
			MentionBridge.onMentionKeyUp();
		}
	});
});
