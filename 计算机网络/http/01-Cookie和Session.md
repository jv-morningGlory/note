# Cookie

cookie是服务器分发给浏览器的，浏览器存储在本地。
```
Cookie:key1=value1; key2=value2

```

- name : Cookie的名字，用作唯一标识Cookie
- value ： Cookie所包含的值
- Max-Age或Expire： Cookie的过期实际按。可以设置一个特定的日期和时间（Expire），或者相对于当前时间的秒数（Max-Age）。
- Domain： Cookie 适用的域名。如果未指定，则默认为创建 Cookie 的页面所在的域名。
- Path ： Cookie 适用的路径。如果未指定，则默认为创建 Cookie 的页面所在的路径。
- HttpOnly ： 防止通过 JavaScript 访问 Cookie。这可以提高安全性，因为攻击者无法通过注入恶意脚本来窃取 Cookie。
- Secure ： 只有在使用 HTTPS 安全协议时才发送 Cookie。这同样可以提高安全性，因为它可以防止中间人攻击。
- SameSite : 这个属性是Cookie的一个重要的属性，用于指示浏览器是否应该在跨站点请求中发送Cookie。

        1. Strict: 浏览器仅会在目标网站的域名完全匹配的时候才会发送Cookie。
        2. Lax： 浏览器将在某些情况下（例如从超链接da）