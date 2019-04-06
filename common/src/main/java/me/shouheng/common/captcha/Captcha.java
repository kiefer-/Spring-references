package me.shouheng.common.captcha;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author shouh, 2019/4/6-12:01
 */
@Data
public class Captcha {

    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    private int width;
    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    private int height;
    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    private int codeCount;
    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    private int lineCount;
    @Setter(value = AccessLevel.PRIVATE)
    @Getter(value = AccessLevel.PRIVATE)
    private char[] codeSequence;

    @Setter(value = AccessLevel.PRIVATE)
    private String code = null;
    @Setter(value = AccessLevel.PRIVATE)
    private BufferedImage buffImg = null;

    private Captcha(int width, int height, int codeCount, int lineCount, char[] codeSequence) {
        this.width = width;
        this.height = height;
        this.codeCount = codeCount;
        this.lineCount = lineCount;
        this.codeSequence = codeSequence;
    }

    private void createCode() {
        int x, fontHeight, codeY;
        int red, green, blue;

        // 每个字符的宽度
        x = width / (codeCount + 2);
        // 字体的高度
        fontHeight = height - 2;
        codeY = height - 4;

        // 图像buffer
        buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = buffImg.createGraphics();
        // 生成随机数
        Random random = new Random();
        // 将图像填充为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        // 创建字体
        CaptchaImgFont captchaImgFont = new CaptchaImgFont();
        Font font = captchaImgFont.getFont(fontHeight);
        g.setFont(font);

        for (int i = 0; i < lineCount; i++) {
            int xs = random.nextInt(width);
            int ys = random.nextInt(height);
            int xe = xs + random.nextInt(width / 8);
            int ye = ys + random.nextInt(height / 8);
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawLine(xs, ys, xe, ye);
        }

        // randomCode 记录随机产生的验证码
        StringBuilder randomCode = new StringBuilder();
        // 随机产生 codeCount 个字符的验证码。
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色值，让输出的每个字符的颜色值都将不同。
            red = random.nextInt(255);
            green = random.nextInt(255);
            blue = random.nextInt(255);
            g.setColor(new Color(red, green, blue));
            g.drawString(strRand, (i + 1) * x, codeY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(strRand);
        }
        // 将四位数字的验证码保存
        code = randomCode.toString();
    }

    public void write(String path) throws IOException {
        OutputStream sos = new FileOutputStream(path);
        this.write(sos);
    }

    public void write(OutputStream sos) throws IOException {
        ImageIO.write(buffImg, "png", sos);
        sos.close();
    }

    public static class Builder {
        /**
         * 图片的宽度
         */
        private int width = 160;

        /**
         * 图片的高度
         */
        private int height = 40;

        /**
         * 验证码字符个数
         */
        private int codeCount = 5;

        /**
         * 验证码干扰线数
         */
        private int lineCount = 3;

        /**
         * 用来生成验证码的序列
         */
        private char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
                'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                '2', '3', '4', '5', '6', '7', '8', '9' };

        public Builder() {
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setCodeCount(int codeCount) {
            this.codeCount = codeCount;
            return this;
        }

        public Builder setLineCount(int lineCount) {
            this.lineCount = lineCount;
            return this;
        }

        public Builder setCodeSequence(char[] codeSequence) {
            this.codeSequence = codeSequence;
            return this;
        }

        public Captcha build() {
            Captcha captcha = new Captcha(width, height, codeCount, lineCount, codeSequence);
            captcha.createCode();
            return captcha;
        }
    }
}
