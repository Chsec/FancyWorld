package com.chw.application.util;

import com.elvishew.xlog.XLog;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileEncryptUtils {

    private static final String LOG_TAG = "FileEncryptUtils";

    /**
     * AES 文件加密方法
     *
     * @param inPath  待加密文件路径
     * @param outPath 加密文件输出路径
     * @param AES_KEY AES加密秘钥
     */
    public static void aesEncrypt(String inPath, String outPath, String AES_KEY) {
        try (FileInputStream fis = new FileInputStream(inPath); FileOutputStream fos = new FileOutputStream(outPath);) {
            //SecretKeySpec此类来根据一个字节数组构造一个 SecretKey
            SecretKeySpec sks = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            //Cipher类为加密和解密提供密码功能,获取实例
            Cipher cipher = Cipher.getInstance("AES");
            //初始化
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            //CipherOutputStream 为加密输出流
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            int b;
            byte[] d = new byte[1024];
            while ((b = fis.read(d)) != -1) {
                cos.write(d, 0, b);
            }
            cos.flush();
        } catch (Exception e) {
            XLog.tag(LOG_TAG).e(e.getMessage());
        }
    }

    /**
     * 移动文件，自带简单混淆功能
     *
     * @param inPath  源文件绝对路径
     * @param outPath 文件目的地绝对路径
     */
    public static void fileMove(String inPath, String outPath) {
        try (FileInputStream fis = new FileInputStream(inPath); FileOutputStream fos = new FileOutputStream(outPath)) {
            int b;
            byte[] cache = new byte[1024];
            while ((b = fis.read(cache)) != -1) {
                byte[] temp = new byte[b];
                // 按索引倒序置换
                for (int i = b - 1; i >= 0; i--) {
                    int a = b - 1 - i;
                    temp[a] = cache[i];
                }
                // 输出文件数据
                fos.write(temp, 0, b);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取文件字节数组，自带简单混淆功能
     *
     * @param inPath 源文件绝对路径
     */
    public static byte[] fileLoad(String inPath) {
        try (FileInputStream fis = new FileInputStream(inPath);) {
            byte[] result = new byte[]{};
            int b;
            byte[] cache = new byte[1024];
            while ((b = fis.read(cache)) != -1) {
                byte[] temp = new byte[b];
                // 按索引倒序置换
                for (int i = b - 1; i >= 0; i--) {
                    int a = b - 1 - i;
                    temp[a] = cache[i];
                }
                // 输出文件数据
                result = combineArrays(result, temp);
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * @param a 待合并字节数组
     * @return 合并后的数组
     */
    private static byte[] combineArrays(byte[]... a) {
        int massLength = 0;
        for (byte[] b : a) {
            massLength += b.length;
        }
        byte[] c = new byte[massLength];
        byte[] d;
        int index = 0;
        for (byte[] anA : a) {
            d = anA;
            System.arraycopy(d, 0, c, 0 + index, d.length);
            index += d.length;
        }
        return c;
    }

}
