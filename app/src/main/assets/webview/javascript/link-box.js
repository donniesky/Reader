/*
 * 控制 link-box 样式
 *
 * 依赖：
 * zepto.min.js
 * prototype.js
 * template.js
 * image.js
 */

var HOST_ZHIHU = 'zhihu.com';
var HOST_ZHIHU_WWW = 'www.zhihu.com';
var HOST_ZHIHU_COLUMN = 'zhuanlan.zhihu.com';
var HOST_ZHIHU_PROMOTION_ARTICLE = 'promotion.zhihu.com';

var LINK_BOX_HOLDER_BG_THUMBNAIL_LIGHT = 'file:///android_asset/webview/resource/link_box_holder_bg_thumbnail_light.png';
var LINK_BOX_HOLDER_BG_THUMBNAIL_DARK = 'file:///android_asset/webview/resource/link_box_holder_bg_thumbnail_dark.png';
var LINK_BOX_HOLDER_BG_CONTENT_LIGHT = 'file:///android_asset/webview/resource/link_box_holder_bg_content_light.png';
var LINK_BOX_HOLDER_BG_CONTENT_DARK = 'file:///android_asset/webview/resource/link_box_holder_bg_content_dark.png';

function autoLayoutLinkBox() {
    $('.link-box .title').each(function() {
        var title = $(this);

        var fontSize = title.css('font-size');
        fontSize = parseFloat(fontSize);

        var height = title.height();
        height = parseFloat(height);

        var count = height / fontSize;
        if (count < 2) {
            title.css('line-height', 'normal');
        } else{
            title.css('line-height', '125%');
        }
    });

    $('.link-box .label').each(function() {
        var label = $(this);
        var labelText = label.text();
        var width = (labelText.width() + 4) + 'px';
        label.css('width', width);

        var url = label.closest('.content').children('.url');
        width = (url.html().width() + 8) + 'px';
        label.css('margin-left', width);

        var urlTopOffset = url.offset().top;
        var labelTopOffset = label.offset().top;
        var labelMarginTop = label.css('margin-top');
        var offset = Math.round(urlTopOffset - labelTopOffset);
        labelMarginTop = parseInt(labelMarginTop) + offset;
        if (labelText === 'Live') {
            labelMarginTop += 1;
        }
        label.css('margin-top', labelMarginTop + 'px');

        if (labelText === 'Live') {
            label.css('height', '12px');
            label.css('line-height', '1.4');
        }
    });
}

function disableLinkBoxClick() {
    $('.link-box').each(function() {
        $(this).removeAttr('href');
    });
}

// http://stackoverflow.com/a/15979390/4696820
function getUrlHost(url) {
    var parser = document.createElement('a');
    parser.href = url;
    return parser.hostname;
}

function isZhihuUrl(url) {
    var hostname = getUrlHost(url);
    return hostname === HOST_ZHIHU || hostname === HOST_ZHIHU_WWW
            || hostname === HOST_ZHIHU_COLUMN || hostname === HOST_ZHIHU_PROMOTION_ARTICLE;
}

function isPromotionUrl(url) {
    var hostname = getUrlHost(url);
    return hostname === HOST_ZHIHU_PROMOTION_ARTICLE;
}

function buildLinkHolderHtml() {
    return '<div class="link-box-holder" style="margin: 16px 20px 20px 20px;">'
            + '<div class="link-box-holder-bg">'
            + '<img class="link-box-holder-bg-thumbnail" src="">'
            + '<img class="link-box-holder-bg-content" src="">'
            + '</div>'
            + '<div class="link-box-holder-loading"></div>'
            + '</div>';
}

function buildLinkBoxHtml(linkUrl, linkImageUrl, linkTitle, linkLabel, margin, loading) {
    var tmpl = '';
    var linkHostUrl = linkUrl;

    if (margin) {
        tmpl = '<a class="link-box" href="{{link_url}}" style="margin: 16px 20px 20px 20px">';
    } else {
        tmpl = '<a class="link-box" href="{{link_url}}">';
    }
    if (linkImageUrl.length > 0) { // 没有就不加
        if (loading) {
            tmpl += '<img class="thumbnail" src="file:///android_asset/webview/resource/image_holder_loading.gif" original-src="{{link_image_url}}" />';
        } else {
            tmpl += '<img class="thumbnail" src="{{link_image_url}}" original-src="{{link_image_url}}" />';
        }
    }
    tmpl += '<span class="content">';
    tmpl += '<span class="title">{{link_title}}</span>';
    if (linkTitle.length > 0) {
        tmpl += '<span class="url">{{link_host_url}}</span>';
        if (isZhihuUrl(linkHostUrl)) {
            if (isPromotionUrl(linkHostUrl)) {
                linkHostUrl = HOST_ZHIHU_PROMOTION_ARTICLE;
            } else {
                // 改为不展示 zhihu.com
                // linkHostUrl = '';
                linkHostUrl = HOST_ZHIHU;
            }
        } else {
            linkHostUrl = getUrlHost(linkHostUrl);
        }
    } else {
        // 没有标题的时候, 链接就是标题
        linkTitle = linkUrl;
    }
    if (linkLabel && linkLabel.length > 0) {
        tmpl += '<span class="label">{{link_label}}</span>';
    }
    tmpl += '</span>';
    tmpl += '</a>';

    var data = '{'
            + '"link_url": "' + linkUrl + '",'
            + '"link_image_url": "' + linkImageUrl + '",'
            + '"link_title": "' + linkTitle + '",'
            + '"link_host_url": "' + linkHostUrl + '",'
            + '"link_label": "' + linkLabel + '"'
            + '}';
    data = JSON.parse(data);
    return template(tmpl, data);
}

function setLinkTheme(isDark) {
    if (isDark) {
        $('.link-box-holder-bg-thumbnail').attr('src', LINK_BOX_HOLDER_BG_THUMBNAIL_DARK);
        $('.link-box-holder-bg-content').attr('src', LINK_BOX_HOLDER_BG_CONTENT_DARK);
    } else {
        $('.link-box-holder-bg-thumbnail').attr('src', LINK_BOX_HOLDER_BG_THUMBNAIL_LIGHT);
        $('.link-box-holder-bg-content').attr('src', LINK_BOX_HOLDER_BG_CONTENT_LIGHT);
    }
}