function replaceInElement(element, find, replace) {
				// iterate over child nodes in reverse, as replacement may increase
				// length of child node list.
				for (var i= element.childNodes.length; i-->0;) {
					var child= element.childNodes[i];
					if (child.nodeType==1) { // ELEMENT_NODE
						var tag= child.nodeName.toLowerCase();
						if (tag!='style' && tag!='script') // special case, don't touch CDATA elements
							replaceInElement(child, find, replace);
					} else if (child.nodeType==3) { // TEXT_NODE
						replaceInText(child, find, replace);
					}
				}
			}

			function replaceInText(text, find, replace) {
				var match;
				var matches= [];
				while (match= find.exec(text.data))
					matches.push(match);
				for (var i= matches.length; i-->0;) {
					match= matches[i];
					text.splitText(match.index);
					text.nextSibling.splitText(match[0].length);
					text.parentNode.replaceChild(replace(match), text.nextSibling);
				}
			}

			function addRuby(word, tran) {
				var find = new RegExp("\\b"+word+"\\b", 'gi');
				replaceInElement($("#readability-page-1").get(0), find, function(match) {
					var ruby = document.createElement('ruby');
					var rt = document.createElement('rt')
					ruby.appendChild(document.createTextNode(match[0]));
					rt.appendChild(document.createTextNode(tran));
					ruby.appendChild(rt)
					return ruby;
				});
			}

			function text2span() {
				var find = new RegExp("\\b\\w+\\b", 'gi');
				replaceInElement($("#readability-page-1").get(0), find, function(match) {
					var span = document.createElement('span');
					$(span).addClass("ladder_word");
					span.appendChild(document.createTextNode(match[0]));
					return span;
				});
			}

			function addAllRuby(rubyObject,blacklist) {
				for (var ro in rubyObject) {
					if(!blacklist.includes(ro))
						addRuby(ro,rubyObject[ro]);
				}
			}

			$(function() {

				$('#readability-page-1 a').replaceWith(function() {
					return this.childNodes;
				});

				$(".ladder_word").on( "click", function() {
					var word
					if ($(this).find("ruby").length>0) {
						word = $(this).find("ruby")[0].childNodes[0].nodeValue
					} else {
						word = this.childNodes[0].nodeValue
					}
					$(location).attr('href', 'dict://'+word);
					$(this).css('background-color', 'green');
					var span = $(this)
					setTimeout(function () {
						span.css("background-color", "transparent");
					}, 800);

					//$(this).delay(800).animate({backgroundColor:'inherit'});
				});
			});