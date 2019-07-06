<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门小DEMO </title>
</head>
<body>

<#list goodsList as goods>
${goods_index+1}--${goods.name}---${goods.price}<br>
</#list>

</body>
</html>