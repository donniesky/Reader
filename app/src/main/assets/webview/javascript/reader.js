/*
 * 用于 Zhihu Android WebView Reader
 *
 * 依赖:
 * zepto.min.js
 * moment.min.js
 * lazyload.js
 * image.js
 * video-box.js
 *
 * native 需要实现的接口有:
 * void onClickBody();
 * void onWindowLoad();
 * boolean isThemeDark();
 * String provideUserId();
 * String provideContent();
 * int provideContentType();
 * int provideBodyFontSize();
 * int provideTitleFontSize();
 *
 * native 可以调用的方法有:
 * void setBodyFontSize(int fontSize);
 * void setTitleFontSize(int fontSize);
 * void setContentOffset(int offset);
 * void setupTheme();
 */

function setBodyFontSize(fontSize) {
	$('body').css('font-size', fontSize + 'px');
}

function setTitleFontSize(fontSize) {
	$('div').each(function() {
		var that = $(this);
		if (that.hasClass('title')) {
			that.css('font-size', fontSize + 'px');
		}
	});
}

// 一定要保证 window 的高度 >= offset 才会生效
function setContentOffset(offset) {
	window.scrollTo(0, offset);
}

function setupTheme() {
	var dark = ReaderBridge.isThemeDark();
	if (dark) {
		$('body').addClass('dark');
	} else {
		$('body').removeClass('dark');
	}
}

// =================================================================================================

function buildContentFromQuestion(json) {
	var question = JSON.parse(json);
	if (question.detail == null) {
		return '';
	}

	return question.detail;
}

function buildContentFromAnswer(json) {
	var answer = JSON.parse(json);
	var content = answer.content != null ? answer.content : '';
	var time = buildTimeTag(answer.created_time * 1000, answer.updated_time * 1000);
	var copyright = buildCopyrightTag(answer.is_copyable);
	var extras = answer.extras != null ? answer.extras : '';
	return content + time + copyright + extras;
}

function buildContentFromAnswerWithSuggestEdit(json) {
	var answer = JSON.parse(json);
	var userId = ReaderBridge.provideUserId();
	var authorId = answer.author.id;
	if (userId == authorId) {
		return buildSuggestEditTag(answer.suggest_edit, authorId) + answer.content;
	} else {
		return buildSuggestEditTag(answer.suggest_edit, authorId);
	}
}

function buildContentFromArticle(json) {
	var article = JSON.parse(json);
	var title = buildTitleTag(article.title);
	var content = article.content != null ? article.content : '';
	var time = buildTimeTag(article.created * 1000, article.updated * 1000);
	return title + content + time;
}

function buildContentFromArticleWithSuggestEdit(json) {
	var article = JSON.parse(json);
	var userId = ReaderBridge.provideUserId();
	var authorId = article.author.id;
	if (userId == authorId) {
		return buildSuggestEditTag(article.suggest_edit, authorId) + article.content;
	} else {
		return buildSuggestEditTag(article.suggest_edit, authorId);
	}
}

function buildCopyrightTag(copyable) {
	return $('<copyright />', {'text': copyable ? '作者保留权利' : '禁止转载'})[0].outerHTML;
}

function buildTitleTag(title) {
	return $('<div />', {'class': 'title', 'text': title})[0].outerHTML;
}

function buildTimeTag(createdTime, updatedTime) {
	if (createdTime <= 0 && updatedTime <= 0) {
		return '';
	}

	if (createdTime == updatedTime) {
		return $('<time />', {'text': '创建于 ' + formatTime(createdTime)})[0].outerHTML;
	}

	createdTime = '创建于 ' + formatTime(createdTime);
	updatedTime = '编辑于 ' + formatTime(updatedTime);
	$(document).on('click', 'time', function() {
		var that = $(this);
		if (that.html() == createdTime) {
			that.html(updatedTime);
		} else {
			that.html(createdTime);
		}
	});
	return $('<time />', {'text': createdTime})[0].outerHTML;
}

function formatTime(time) {
	return moment(time).format('YYYY-MM-DD');
}

function buildSuggestEditTag(suggestEdit, authorId) {
	var reason = '';
	if (suggestEdit.reason != null) {
		reason = $('<div />', {'class': 'suggest-edit-reason', 'text': suggestEdit.reason})[0].outerHTML;
	}

	var title = '';
	if (suggestEdit.title != null) {
		title = $('<a />', {'href': suggestEdit.url, 'text': suggestEdit.title})[0].outerHTML;
	}

	var border = '';
	var userId = ReaderBridge.provideUserId();
	if (userId == authorId) {
		border = $('<p />', {'class': "suggest-edit-border"})[0].outerHTML;
	}

	var tip = '';
	if (suggestEdit.tip != null) {
		tip = $('<div />', {
			'class': 'suggest-edit-tip',
			'html': suggestEdit.tip + '<br>' + title + '<br>' + border
		})[0].outerHTML;
	}

	return reason + tip;
}

// =================================================================================================

var bodyFontSize = ReaderBridge.provideBodyFontSize();
var titleFontSize = ReaderBridge.provideTitleFontSize();
setBodyFontSize(bodyFontSize);
setTitleFontSize(titleFontSize);

var content = ReaderBridge.provideContent();
var contentType = ReaderBridge.provideContentType();
switch (contentType) {
	case 1:
		content = buildContentFromQuestion(content);
		break
	case 2:
		content = buildContentFromAnswer(content);
		break;
	case 3:
		content = buildContentFromAnswerWithSuggestEdit(content);
		break;
	case 4:
		content = buildContentFromArticle(content);
		break;
	case 5:
		content = buildContentFromArticleWithSuggestEdit(content);
		break;
	default:
		break;
}
$('#content').append(content);

setupImage(true);
setupTheme();

$(document).ready(function() {
	$('a').click(function(event) {
		event.stopPropagation();
	});

	$('body').click(function(event) {
		var tag = event.target.tagName.toLowerCase();
		if (tag != 'a' && tag != 'img' && tag != 'time') {
			ReaderBridge.onClickBody();
		}
	});

	window.onload = function() {
		ReaderBridge.onWindowLoad();
	};

	autoLayoutVideoBox();
});