<html>
<head>
    <meta charset="utf-8">
    <title>Freemarker入门小DEMO </title>
</head>
<body>

<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<#assign data=text?eval />
${data.bank}
${data.account}

${date?date}
${date?datetime}
${date?time}
${date?string("yyyy年MM月")}


${point+111111111111?c}

<#if 1 lt 1>
存在
<#else>
不存在
</#if>

你是大${aaa!}




</body>
</html>