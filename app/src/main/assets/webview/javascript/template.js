/*
 * 简单模版匹配, 用于生成 link-box 结构
 *
 * Alex Young
 * https://gist.github.com/alexyoung/1301000
 *
 * Render templates.
 *
 * @param {String} The template to use `<p>Hello {{name}}</p>`
 * @param {String} The data `{ name: 'Alex' }`
 * @return {String} The rendered template
 **/
function template(t, d) {
	return t.replace(/{{([^}]*)}}/g, function(m, f, p, a) {
		return d[f] || '';
	});
};