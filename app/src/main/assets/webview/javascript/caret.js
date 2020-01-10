// 获取光标相对于第一个字符的偏移量
// http://stackoverflow.com/a/4812022
function getCaretOffset(target) {
	var caretOffset = 0;

	if (typeof window.getSelection != 'undefined' && window.getSelection().rangeCount > 0) {
		var range = window.getSelection().getRangeAt(0);
		var preCaretRange = range.cloneRange();
		preCaretRange.selectNodeContents(target);
		preCaretRange.setEnd(range.endContainer, range.endOffset);
		caretOffset = preCaretRange.toString().length;
	} else if (typeof document.selection != 'undefined' && document.selection.type != 'Control') {
		var textRange = document.selection.createRange();
		var preCaretTextRange = document.body.createTextRange();
		preCaretTextRange.moveToElementText(target);
		preCaretTextRange.setEndPoint('EndToEnd', textRange);
		caretOffset = preCaretTextRange.text.length;
	}

	return caretOffset;
}

// 获取光标的选择范围；
// 如果没有选择，则 start == end
// http://stackoverflow.com/a/4812022
function getCaretStartAndEnd(target) {
	var start = 0;
	var end = 0;
	var doc = target.ownerDocument || target.document;
	var win = doc.defaultView || doc.parentWindow;
	var sel;

	if (typeof win.getSelection != "undefined") {
		sel = win.getSelection();
		if (sel.rangeCount > 0) {
			var range = win.getSelection().getRangeAt(0);
			var preCaretRange = range.cloneRange();
			preCaretRange.selectNodeContents(target);
			preCaretRange.setEnd(range.startContainer, range.startOffset);
			start = preCaretRange.toString().length;
			preCaretRange.setEnd(range.endContainer, range.endOffset);
			end = preCaretRange.toString().length;
		}
	} else if ( (sel = doc.selection) && sel.type != "Control") {
		var textRange = sel.createRange();
		var preCaretTextRange = doc.body.createTextRange();
		preCaretTextRange.moveToElementText(target);
		preCaretTextRange.setEndPoint("EndToStart", textRange);
		start = preCaretTextRange.text.length;
		preCaretTextRange.setEndPoint("EndToEnd", textRange);
		end = preCaretTextRange.text.length;
	}

	return {start: start, end: end};
}

// https://stackoverflow.com/a/26495188
function getSelectionCoords() {
    var sel = document.selection, range, rect;
    var x = 0, y = 0;
    if (sel) {
        if (sel.type != "Control") {
            range = sel.createRange();
            range.collapse(true);
            x = range.boundingLeft;
            y = range.boundingTop;
        }
    } else if (window.getSelection) {
        sel = window.getSelection();
        if (sel.rangeCount) {
            range = sel.getRangeAt(0).cloneRange();
            if (range.getClientRects) {
                range.collapse(true);
                if (range.getClientRects().length>0){
                    rect = range.getClientRects()[0];
                    x = rect.left;
                    y = rect.top;
                }
            }
            // Fall back to inserting a temporary element
            if (x == 0 && y == 0) {
                var span = document.createElement("span");
                if (span.getClientRects) {
                    // Ensure span has dimensions and position by
                    // adding a zero-width space character
                    span.appendChild( document.createTextNode("\u200b") );
                    range.insertNode(span);
                    rect = span.getClientRects()[0];
                    x = rect.left;
                    y = rect.top;
                    var spanParent = span.parentNode;
                    spanParent.removeChild(span);

                    // Glue any broken text nodes back together
                    spanParent.normalize();
                }
            }
        }
    }
    return { x: x, y: y };
}

// 设置光标位置
// http://stackoverflow.com/a/6242538/4696820
function getTextNodesIn(node) {
	var textNodes = [];
	if (node.nodeType == 3) {
		textNodes.push(node);
	} else {
		var children = node.childNodes;
		for (var i = 0, len = children.length; i < len; ++i) {
			textNodes.push.apply(textNodes, getTextNodesIn(children[i]));
		}
	}
	return textNodes;
}
function setCaretPosition(el, start, end) {
	if (document.createRange && window.getSelection) {
		var range = document.createRange();
		range.selectNodeContents(el);
		var textNodes = getTextNodesIn(el);
		var foundStart = false;
		var charCount = 0, endCharCount;

		for (var i = 0, textNode; textNode = textNodes[i++]; ) {
			endCharCount = charCount + textNode.length;
			if (!foundStart && start >= charCount
					&& (start < endCharCount ||
					(start == endCharCount && i <= textNodes.length))) {
				range.setStart(textNode, start - charCount);
				foundStart = true;
			}
			if (foundStart && end <= endCharCount) {
				range.setEnd(textNode, end - charCount);
				break;
			}
			charCount = endCharCount;
		}

		var sel = window.getSelection();
		sel.removeAllRanges();
		sel.addRange(range);
	} else if (document.selection && document.body.createTextRange) {
		var textRange = document.body.createTextRange();
		textRange.moveToElementText(el);
		textRange.collapse(true);
		textRange.moveEnd("character", end);
		textRange.moveStart("character", start);
		textRange.select();
	}
}

// 在光标位置插入 HTML
// http://stackoverflow.com/a/6691294/4696820
function insertHtmlAtCursor(html) {
	var sel, range;
	if (window.getSelection) {
		// IE9 and non-IE
		sel = window.getSelection();
		if (sel.getRangeAt && sel.rangeCount) {
			range = sel.getRangeAt(0);
			range.deleteContents();

			// Range.createContextualFragment() would be useful here but is
			// only relatively recently standardized and is not supported in
			// some browsers (IE9, for one)
			var el = document.createElement("div");
			el.innerHTML = html;
			var frag = document.createDocumentFragment(), node, lastNode;
			while ( (node = el.firstChild) ) {
				lastNode = frag.appendChild(node);
			}
			range.insertNode(frag);

			// Preserve the selection
			if (lastNode) {
				range = range.cloneRange();
				range.setStartAfter(lastNode);
				range.collapse(true);
				sel.removeAllRanges();
				sel.addRange(range);
			}
		}
	} else if (document.selection && document.selection.type != "Control") {
		// IE < 9
		document.selection.createRange().pasteHTML(html);
	}
}