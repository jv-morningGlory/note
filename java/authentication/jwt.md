# 定义

jwt(json web token), 是一种开发标准。

# 来源

## 一般的认证流程如下

1. 用户输入认证信息
2. 服务器存储用户信息，用session_id(token)标识
3. 浏览器存储session_id(token)
4. 浏览器每次请求携带session_id(token)
5. 服务器通过获取session_id(token)来认证用户信息

上面的流程有一个特点，浏览器只会存储一个session_id,并不会存用户的详细信息。
而且对于多个服务来说，是需要一个统一认证的服务。这个统一认证的服务压力会很大。

## jwt思想

现在有一个想法，如果在把用户信息写入到session_id,以后每个不同的服务想要知道是
什么用户来请求服务器，可以直接从session_id,里面读取。当然session_id是通过加密的。
如果服务器想要解密这个session_id,可以通过像认证服务获取```密钥```即可。

# 组成

一个jwt有三个部分组成

### 1. 头部（header）


    {
    "alg": "HS256",
    "typ": "JWT"
    }

    typ: 声明类型
    alg: 签名算法

### 2. 载荷 （payload）


    {
    "sub": "1234567890",
    "name": "John Doe",
    "role": "user",
    "exp": 1516239022
    }

### 3. 签名（signature）
JWT的第三部分是签名，它由头部、载荷以及一个密钥（或者公钥，具体取决于签名算法）生成。签名的作用是验证消息在传递过程中没有被篡改，并确保消息的发送者是有权生成JWT的。

总结
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwicm9sZSI6InVzZXIiLCJleHAiOjE1MTYyMzkwMjJ9
SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

```
JWT由头部、载荷和签名三部分组成，它们通过.分隔并编码为Base64 URL安全格式的字符串。


```java


package com.example.demo;

import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestJwt {

    public static final String SECRET_KEY = "your_secret_key";


    public static void main(String[] args) throws Exception {


        Header header = new Header();
        Payload payload = new Payload();
        payload.setName("万芳");


        TestJwt testJwt = new TestJwt();
        String jwt = testJwt.generate(header, payload);
        System.out.println(jwt);

        boolean verify = testJwt.verify(jwt);
        System.out.println(verify);


    }


    public String generate(Header header, Payload payload) throws Exception {


        String jsonHead = JSONUtil.toJsonStr(header);
        String base64UrlHeader = base64UrlEncode(jsonHead);

        String payloadJson = JSONUtil.toJsonStr(payload);
        String base64UrlPayload = base64UrlEncode(payloadJson);



        String signature = createSignature(
                base64UrlHeader + "." + base64UrlPayload,
                header.getAlgorithm()
        );
        return base64UrlHeader + "." + base64UrlPayload + "." + signature;

    }

    public boolean verify(String jwt) throws Exception {
        // Split JWT into parts
        String[] parts = jwt.split("\\.");
        if (parts.length != 3) {
            return false; // Invalid JWT format
        }

        String base64UrlHeader = parts[0];
        String base64UrlPayload = parts[1];
        String signature = parts[2];

        // Decode Header and Payload
        String headerJson = new String(Base64.getUrlDecoder().decode(base64UrlHeader), StandardCharsets.UTF_8);
        String payloadJson = new String(Base64.getUrlDecoder().decode(base64UrlPayload), StandardCharsets.UTF_8);

        // Parse Header to get algorithm
        Header header = JSONUtil.toBean(headerJson, Header.class);

        // Reconstruct the signature
        String reconstructedSignature = createSignature(base64UrlHeader + "." + base64UrlPayload, header.getAlgorithm());

        // Compare signatures
        return signature.equals(reconstructedSignature);
    }







    private String createSignature(String baseString, String algorithm) throws Exception {

        Mac sha256Hmac = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8),
                algorithm
        );
        sha256Hmac.init(secretKey);
        byte[] hashBytes = sha256Hmac.doFinal(baseString.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
    }


    private String base64UrlEncode(String input) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(input.getBytes(StandardCharsets.UTF_8));
    }

    @Getter
    @Setter
    public static class Payload {

        private String name;
    }


    @Getter
    @Setter
    @Data
    public static class Header {

        private  String algorithm = "HmacSHA256";

        private  String type = "jwt";


    }


}


```



