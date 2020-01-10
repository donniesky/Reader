/*
 * 快速分享功能
 */

// http://stackoverflow.com/a/6668159/4696820
function getSelectionHtml() {
	var html = '';

	if (typeof window.getSelection != 'undefined') {
		var sel = window.getSelection();
		if (sel.rangeCount) {
			var container = document.createElement('div');
			for (var i = 0, len = sel.rangeCount; i < len; ++i) {
				container.appendChild(sel.getRangeAt(i).cloneContents());
			}

			html = container.innerHTML;
		}
	} else if (typeof document.selection != 'undefined') {
		if (document.selection.type == 'Text') {
			html = document.selection.createRange().htmlText;
		}
	}

	return html;
}

// http://stackoverflow.com/a/5379408/4696820
function getSelectionText() {
	var text = "";

	if (window.getSelection) {
		text = window.getSelection().toString();
	} else if (document.selection && document.selection.type != "Control") {
		text = document.selection.createRange().text;
	}

	return text;
}