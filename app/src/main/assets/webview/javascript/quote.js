/*
 * 控制 Pin 引用样式
 *
 * 依赖：
 * zepto.min.js
 * template.js
 */

function disableQuoteClick() {
	$('.quote').each(function() {
		$(this).removeAttr('href');
	});
}

function buildQuoteHtml(html, margin) {
	var result = '';
	if (margin) {
		result = '<blockquote class="quote" style="margin: 16px 20px 0 20px">';
	} else {
		result = '<blockquote class="quote">';
	}

	result += html;
	result += '</blockquote>';
	return result;
}