package com.izatec.pay.infra.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import org.krysalis.barcode4j.impl.code128.EAN128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GoogleImagem {
    public static byte[] gerarQRCode(String texto) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.MARGIN, 1); // Ajuste da margem

        // Gerar o código QR em formato de matrizes de bits
        com.google.zxing.common.BitMatrix bitMatrix = qrCodeWriter.encode(texto, BarcodeFormat.QR_CODE, 250, 250, hints);

        // Criar a imagem a partir da BitMatrix
        BufferedImage bufferedImage = new BufferedImage(250, 250, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 250; i++) {
            for (int j = 0; j < 250; j++) {
                bufferedImage.setRGB(i, j, bitMatrix.get(i, j) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        // Converter a imagem para um array de bytes (sem salvar no disco)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

//https://www.baeldung.com/java-generating-barcodes-qr-codes
    public static byte[] generateBarcodeImage(String barcodeText) throws Exception {
        EAN128Bean barcodeGenerator = new EAN128Bean();
        BitmapCanvasProvider canvas =
                new BitmapCanvasProvider(300, BufferedImage.TYPE_BYTE_BINARY, false, 0);

        barcodeGenerator.generateBarcode(canvas, barcodeText);
        BufferedImage bufferedImage = canvas.getBufferedImage();


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    public static void escreverQRCode(String texto,String destino) throws WriterException, IOException {
        byte[] qrCodeBytes = gerarQRCode(texto);
        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(qrCodeBytes));
        // Salvar a imagem em um arquivo
        ImageIO.write(bufferedImage, "PNG", new java.io.File(destino));
    }

    public static void main(String[] args) {
        try {
            escreverQRCode("https://izaplay.com.br", "C:\\izatec\\iza-pay\\izatec-pay-api\\src\\main\\resources\\dev\\qrcode.png");
        } catch (WriterException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
