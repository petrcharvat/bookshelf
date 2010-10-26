package com.pch.bm.chm;

import org.apache.commons.lang.StringUtils;

import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@SuppressWarnings({"UseOfSystemOutOrSystemErr"})
public class Main {

    public static void main2(String[] args) {
        for (byte i = -30; i < 30; i++) {
            System.out.println(String.format("%2X", i));
        }

    }

    public static void main(String[] args) throws Exception {

        RandomAccessFile f = new RandomAccessFile("d:/temp/Addison Wesley - 2005 - Java Puzzlers - Traps, Pitfalls and Corner Cases.chm", "r");




        final byte h1 = f.readByte();
        final byte h2 = f.readByte();
        final byte h3 = f.readByte();
        final byte h4 = f.readByte();





        System.out.println(h1);
        System.out.println(h2);
        System.out.println(h3);
        System.out.println(h4);

        final byte[] bytes = {h1, h2, h3, h4};
        final String h = new String(bytes);
        System.out.println(h);


        f.seek(0);
        final byte[] b = new byte[0x38 + 0x10+8+1024];
        f.read(b);

        // pokusny komentar

        final ByteBuffer buffer = ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN);

        System.out.println(position(buffer) + " " + buffer.get());
        System.out.println(position(buffer) + " " + buffer.get());
        System.out.println(position(buffer) + " " + buffer.get());
        System.out.println(position(buffer) + " " + buffer.get());
        System.out.println("------");

        printInt(buffer, "version");
        printInt(buffer, "total header length");
        printInt(buffer, "1. unknown");
        System.out.println("------");
        printByte(buffer, "timestamp 1");
        printByte(buffer, "timestamp 2");
        printByte(buffer, "timestamp 3");
        printByte(buffer, "timestamp 4");
        System.out.println("------");
        printByte(buffer, "language 1");
        printByte(buffer, "language 2");
        printByte(buffer, "language 3");
        printByte(buffer, "language 4");
        System.out.println("------");
        printGuid(buffer);
        printGuid(buffer);
        System.out.println("------");


        printLong(buffer, "offset of section from beginning of file");
        printLong(buffer, "length of section");
        printLong(buffer, "Offset within file of content section 0");

//        buffer.position(120);

        System.out.println("------ Header Section 0 ----------");
        printInt(buffer, "01FE uknown");
        printInt(buffer, "uknown");
        printLong(buffer, "file size");
        printInt(buffer, "uknown");
        printInt(buffer, "uknown");






//        printByte(buffer, "guid 1");
//        printByte(buffer, "guid 2");
//        printByte(buffer, "guid 3");
//        printByte(buffer, "guid 4");
//        printByte(buffer, "guid 5");
//        printByte(buffer, "guid 6");
//        printByte(buffer, "guid 7");
//        printByte(buffer, "guid 8");
//        printByte(buffer, "guid 9");
//        printByte(buffer, "guid 10");


    }

    private static String position(final ByteBuffer buffer) {
        return toHex(buffer.position(), 4) + ": ";
    }

    private static String toHex(final int value) {
        return toHex(4);
    }

    private static String toHex(final int value, final int pad) {
        return StringUtils.leftPad(String.format("%X", value), pad, '0');
    }

    private static String toHex(final byte value, final int pad) {
        return StringUtils.leftPad(String.format("%X", value), pad, '0');
    }

    private static void printInt(final ByteBuffer buffer, final String description) {
        System.out.println(position(buffer) + StringUtils.rightPad(" " + buffer.getInt(), 10) + description);
    }

    private static void printByte(final ByteBuffer buffer, final String description) {
        System.out.println(position(buffer) + StringUtils.rightPad(" " + buffer.get(), 10) + description);
    }

    private static void printLong(final ByteBuffer buffer, final String description) {
        System.out.println(position(buffer) + StringUtils.rightPad(" " + buffer.getLong(), 10) + description);
    }

    private static void printGuid(final ByteBuffer buffer) {
        StringBuilder b = new StringBuilder();
        b.append(position(buffer));
        b.append("{");
        b.append(toHex(buffer.getInt(), 8));
        b.append("-");
        b.append(toHex(buffer.getShort(), 4));
        b.append("-");
        b.append(toHex(buffer.getShort(), 4));
        b.append("-");
        b.append(toHex(buffer.get(), 2));
        b.append(toHex(buffer.get(), 2));
        b.append("-");
        b.append(toHex(buffer.get(), 2));
        b.append(toHex(buffer.get(), 2));
        b.append("-");
        b.append(toHex(buffer.get(), 2));
        b.append(toHex(buffer.get(), 2));
        b.append("-");
        b.append(toHex(buffer.get(), 2));
        b.append(toHex(buffer.get(), 2));
        b.append("}");
        System.out.println(b.toString());
    }


}
