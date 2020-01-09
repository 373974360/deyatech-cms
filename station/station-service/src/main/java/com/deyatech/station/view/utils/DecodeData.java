package com.deyatech.station.view.utils;


import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;

/**
 * 描述：数据解密类
 *
 * @Author: MaChaoWei
 * @Date: 2020/1/8 10:34
 */
public class DecodeData {


    public static BASE64Decoder base64Decoder= new BASE64Decoder();

    /**
     * 解密方法
     * @param str 待解密字符串
     * @return 解密后字符串
     */
    public static String decrypt(String str) {
        String secretKey = "11azxsw23ealfgjs";	// 解密密钥
        try {
            return decodeData(str, secretKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密客户端提交的数据
     * @param data
     *        待解密的数据<br/>
     *        数据组成： 摘要信息和加密后的整串组成。格式如下： data = digest&encryptedInfo
     * @param secretKey
     * 		       密钥
     * @return
     * 		解密后的请求数据。<br/>
     * @throws Exception
     */
    public static String decodeData(String data,String secretKey) throws Exception{
        //拆分摘要和结果信息
        String[] digestAndResult = data.split( "&");
        String digestOfServer = digestAndResult[0];
        String result = digestAndResult[1];
        //解密响应结果
        String afterDESResult = decrypt(result, secretKey);
        String afterBase64Decode =new String(base64Decoder.decodeBuffer(afterDESResult),"UTF-8");
        MessageDigest sd = MessageDigest.getInstance("MD5");
        sd.update(afterBase64Decode.getBytes("UTF-8"));
        String digestOfAgent = byteArr2HexStr(sd.digest());
        // 比较生成的摘要与响应结果中的摘要是否一致
        if(!digestOfServer.equals(digestOfAgent)){
            return null;
        }
        return afterBase64Decode;
    }

    /**
     * 解密字符串
     *
     * @param strIn  需解密的字符串
     * @return       解密后的字符串
     * @throws Exception
     */
    public static String decrypt(String strIn,String secretkey) throws Exception {
        Key key = getKey(secretkey.getBytes());
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] param = cipher.doFinal(hexStr2ByteArr(strIn));
        return new String(param);
    }


    /**
     * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
     *
     * @param arrBTmp  构成该字符串的字节数组
     * @return         生成的密钥
     * @throws java.lang.Exception
     */
    private static Key getKey(byte[] arrBTmp) throws Exception {
        // 创建一个空的8位字节数组（默认值为0）
        byte[] arrB = new byte[8];
        // 将原始字节数组转换为8位
        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }
        // 生成密钥
        Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
        return key;
    }


    /**
     * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
     * 互为可逆的转换过程
     *
     * @param strIn       需要转换的字符串
     * @return            转换后的byte数组
     * @throws Exception  本方法不处理任何异常，所有异常全部抛出
     */
    public static byte[] hexStr2ByteArr(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        // 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }


    /**
     * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
     * hexStr2ByteArr(String strIn) 互为可逆的转换过程
     *
     * @param arrB       需要转换的byte数组
     * @return           转换后的字符串
     * @throws Exception 本方法不处理任何异常，所有异常全部抛出
     */
    public static String byteArr2HexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        // 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
        StringBuffer sb = new StringBuffer(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            // 把负数转换为正数
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            // 小于0F的数需要在前面补0
            if (intTmp < 16) {
                sb.append("0");
            }
            sb.append(Integer.toString(intTmp, 16));
        }
        return sb.toString();
    }
}
