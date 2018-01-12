<html>
<head>
<meta charset = "utf-8">
</head>
<body>
<#assign title = "nowcoder"/>
<#include "header.ftl"/>
${title}
<br/>
hello
<!-- pre用来定义格式化文本，里面文字保留空格和换行符，常用与源代码表示 -->
<pre>
	${value}
<#-- #用来表示标签，list标签遍历 sequence as item-->
<#-- item_index隐藏变量 -->
<#list colors as color>
	${color}，第${color_index+1}个用户
	<#if !color_has_next>
	共有${colors?size}个,最后一个是${color}
	</#if>
</#list>

<#-- map["${key}"] 记得加双引号，表示字符串键 -->
<#--在freemarker中,map的key只能是字符串来作为key-->

<#list map?keys as key>
	key:${key} value:${map["${key}"]} 
</#list>

<#--必须要有getName方法，才能找到该值-->
${User.name}

<#assign hello = "hello"/>
<#--显示源码:在特殊字符串前加r-->
<#assign  helloworld = "${r'${hello}'} world"/>
${hello}
${helloworld}
</pre>
</body>
</html>