/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hh.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author Javier
 */
public class QRGenerator {

    public static byte[] generateQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodewriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodewriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngoutput = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngoutput);
        return pngoutput.toByteArray();

    }

}
