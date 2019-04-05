package me.shouheng.common.jasypt;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * 加密和解密工具，这里只是两个示例，在 jasypt 库中还有更多的加密工具可以使用
 *
 * @author shouh
 */
public class PasswordJasypter {

	/**
	 * 使用 {@link BasicTextEncryptor} 和指定的密码对字符串进行加密
	 *
	 * @param original 原始字符串
	 * @param password 密码
	 * @return 加密结果
	 */
	public static String encryption(String original, String password){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(password);
		return textEncryptor.encrypt(original);
	}

	/**
	 * 使用 {@link BasicTextEncryptor} 和密码对加密的结果进行解密
	 *
	 * @param encryption 加密后的字符串
	 * @param password 密码
	 * @return 解密结果
	 */
	public static String decryption(String encryption, String password){
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(password);
		return textEncryptor.decrypt(encryption);
	}

	public static void main(String...args) {
		String rightPassword = "123456";
		String wrongPassword = "456789";
		String encryption = encryption("WngShhng", rightPassword);
		System.out.println(encryption);
		System.out.println(decryption(encryption, rightPassword));
		// 使用错误的密码解密，将会抛出错误异常
		System.out.println(decryption(encryption, wrongPassword));
	}

}
