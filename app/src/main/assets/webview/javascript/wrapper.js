/*
 * 设置 HTML 内容边距;
 * 由于直接给 body/content 设置边距, 会导致光标位置显示异常, 因此统一使用 wrapper 调整边距
 *
 * native 需要实现的接口有：
 * int provideContentPaddingTop();
 * int provideContentPaddingRight();
 * int provideContentPaddingBottom();
 * int provideContentPaddingLeft();
 *
 * native 可以调用的方法有：
 * void setContentPaddingTop(int paddingTop);
 * void setContentPaddingRight(int paddingRight);
 * void setContentPaddingBottom(int paddingBottom);
 * void setContentPaddingLeft(int paddingLeft);
 * void setContentPadding(int paddingTop, int paddingRight, int paddingBottom, int paddingLeft);
 */

function setContentPaddingTop(paddingTop) {
	$('#wrapper').css('padding-top', paddingTop + 'px');
}

function setContentPaddingRight(paddingRight) {
	$('#wrapper').css('padding-right', paddingRight + 'px');
}

function setContentPaddingBottom(paddingBottom) {
	$('#wrapper').css('padding-bottom', paddingBottom + 'px');
}

function setContentPaddingLeft(paddingLeft) {
	$('#wrapper').css('padding-left', paddingLeft + 'px');
}

function setContentPadding(paddingTop, paddingRight, paddingBottom, paddingLeft) {
	$('#wrapper').css('padding-top', paddingTop + 'px');
	$('#wrapper').css('padding-right', paddingRight + 'px');
	$('#wrapper').css('padding-bottom', paddingBottom + 'px');
	$('#wrapper').css('padding-left', paddingLeft + 'px');
}

var paddingTop = WrapperBridge.provideContentPaddingTop();
var paddingRight = WrapperBridge.provideContentPaddingRight();
var paddingBottom = WrapperBridge.provideContentPaddingBottom();
var paddingLeft = WrapperBridge.provideContentPaddingLeft();
setContentPadding(paddingTop, paddingRight, paddingBottom, paddingLeft);