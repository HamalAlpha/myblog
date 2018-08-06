package top.arieslee.myblog.utils;

import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @ClassName Tools
 * @Description 工具类
 * @Author Aries
 * @Date 2018/7/16 10:56
 * @Version 1.0
 **/
public class Tools {

    //markdown解析对象
    private static Parser parser = Parser.builder().build();

    /**
     * 替换HTML脚本，防止XSS注入
     *
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "^-^");
        return value;
    }

    /**
     * @param markdown markdown源文档
     * @return java.lang.String
     * @Description 将markdown文档转化为html文档
     **/
    public static String mdToHtml(String markdown) {
        if (StringUtils.isBlank(markdown)) {
            return "";
        }
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String content = renderer.render(document);
        content = Commons.emoji(content);

        // TODO 支持网易云音乐输出
//        if (TaleConst.BCONF.getBoolean("app.support_163_music", true) && content.contains("[mp3:")) {
//            content = content.replaceAll("\\[mp3:(\\d+)\\]", "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=350 height=106 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=0&height=88\"></iframe>");
//        }
        // 支持gist代码输出
//        if (TaleConst.BCONF.getBoolean("app.support_gist", true) && content.contains("https://gist.github.com/")) {
//            content = content.replaceAll("&lt;script src=\"https://gist.github.com/(\\w+)/(\\w+)\\.js\">&lt;/script>", "<script src=\"https://gist.github.com/$1/$2\\.js\"></script>");
//        }
        return content;
    }

    /**
     * @Description md5加密（转16进制）
     **/
    public static String MD5encode(String source) {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ignored) {
        }
        //调用jdk自带的MD5工具
        byte[] encode = messageDigest.digest(source.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte anEncode : encode) {
            String hex = Integer.toHexString(0xff & anEncode);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * @param cleartext      待加密明文
     * @param keyLength      密钥长度：128、192、256（密钥长度与安全性成正比，与性能成反比）
     * @param workingPattern 工作模式
     * @param fillStyle      填充模式
     * @return java.lang.String 返回加密后的字符串
     * @Description AES加密
     **/
    public static String enAES(String cleartext, String key, int keyLength, String workingPattern, String fillStyle) throws Exception {
        //自定义加密规则
        String cipherInitInfo = "AES/" + workingPattern + "/" + fillStyle;
        //构造加密器
        Cipher cipher = Cipher.getInstance(cipherInitInfo);
        //获取明文字节数组
        byte[] byteContent = cleartext.getBytes("utf-8");
        //使用加密模式，传入密钥
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key, keyLength));
        //加密操作
        byte[] result = cipher.doFinal(byteContent);
        //加密后使用BASE64编码转换
        return new BASE64Encoder().encode(result);
    }

    /**
     * @Description 默认接口，使用AES加密默认值
     **/
    public static String enAES(String cleartext, String key) throws Exception {
        return enAES(cleartext, key, 128, "ECB", "ISO10126Padding");
    }

    /**
     * @param ciphertext     待解密密文
     * @param key
     * @param keyLength
     * @param workingPattern
     * @param fillStyle
     * @return java.lang.String 返回解密后字符串
     * @Description AES解密
     **/
    public static String deAES(String ciphertext, String key, int keyLength, String workingPattern, String fillStyle) throws Exception {
        String cipherInitInfo = "AES/" + workingPattern + "/" + fillStyle;
        Cipher cipher = Cipher.getInstance(cipherInitInfo);
        //用BASE64解码
        byte[] byteContent = new BASE64Decoder().decodeBuffer(ciphertext);
        //使用解密模式，传入密钥
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key, keyLength));
        return new String(cipher.doFinal(byteContent));
    }

    public static String deAES(String ciphertext, String key) throws Exception {
        return deAES(ciphertext, key, 128, "ECB", "ISO10126Padding");
    }

    /**
     * @param key       自定义密钥种子
     * @param keyLength 密钥长度
     * @return javax.crypto.spec.SecretKeySpec
     * @Description 获取密钥
     **/
    private static SecretKeySpec getSecretKey(String key, int keyLength) throws NoSuchAlgorithmException {
        //密钥生产者
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //初始化密钥生产信息。SecureRandom这里指定种子key.getBytes()，意味着每次所获取的随机数一样
        kgen.init(keyLength, new SecureRandom(key.getBytes()));
        //获取密钥
        SecretKey secretKey = kgen.generateKey();
        //返回AES专用密钥
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }

    /**
     * @param max 最大值（包括）
     * @param min 最小值（包括）
     * @return int
     * @Description 
     **/
    public static int getRandom(int max, int min) {
        Random r = new Random();
        return r.nextInt(max - min + 1) + min;
    }
}
