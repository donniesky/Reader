/* 增加一些 prototype */

// 计算 font 宽度
// http://stackoverflow.com/a/5047712/4696820
String.prototype.width = function(font) {
	var f = font || '12px arial',
		o = $('<div>' + this + '</div>')
			.css({'position': 'absolute', 'float': 'left', 'white-space': 'nowrap', 'visibility': 'hidden', 'font': f})
			.appendTo($('body')),
		w = o.width();
	o.remove();
	return w;
}

// 添加字符串 replaceAll 方法
String.prototype.replaceAll = function(search, replacement) {
	return this.replace(new RegExp(search, 'g'), replacement);
};

// 计算子串的出现次数
String.prototype.count = function(string) {
	return (this.length - this.replace(new RegExp(string, 'g'), '').length) / string.length;
}