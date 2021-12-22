package nettypackets.util;

/*
A static helper class for putting multiple booleans into a byte and retrieving the booleans from a byte
 */
public class BooleanCompression {

    //method which takes in an array of booleans and returns a byte
    public static byte compressBooleans(boolean[] booleans) {
        byte b = 0;
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                b |= (1 << i);
            }
        }
        return b;
    }

    public static byte[] compressBooleanArray(boolean[] booleans) {
        byte[] compressed = new byte[(booleans.length + 7) / 8];
        for (int i = 0; i < booleans.length; i++) {
            if (booleans[i]) {
                compressed[i / 8] |= 1 << (i % 8);
            }
        }
        return compressed;
    }

    //a method which takes in a byte[] and the number of booleans. Returns an array of booleans
    public static boolean[] decompressBooleans(byte[] compressed, int numBooleans) {
        boolean[] booleans = new boolean[numBooleans];
        for (int i = 0; i < numBooleans; i++) {
            booleans[i] = (compressed[i / 8] & (1 << (i % 8))) != 0;
        }
        return booleans;
    }



    /*
        8 methods which each take 1-8 booleans as arguments, puts them into a byte, and returns the byte
        The first method is for 1 boolean, the second for 2, the third for 3, and so on.
        The methods should not use any if statements and should all be 1 line long
     */
    public static byte compressBooleans(boolean a) {
        return (byte) (a ? 1 : 0);
    }

    public static byte compressBooleans(boolean a, boolean b) {
        return (byte) ((a ? 1 : 0) | (b ? 1 : 0) << 1);
    }

    public static byte compressBooleans(boolean a, boolean b, boolean c) {
        return (byte) ((a ? 1 : 0) | (b ? 1 : 0) << 1 | (c ? 1 : 0) << 2);
    }

    public static byte compressBooleans(boolean a, boolean b, boolean c, boolean d) {
        return (byte) ((a ? 1 : 0) | (b ? 1 : 0) << 1 | (c ? 1 : 0) << 2 | (d ? 1 : 0) << 3);
    }

    public static byte compressBooleans(boolean a, boolean b, boolean c, boolean d, boolean e) {
        return (byte) ((a ? 1 : 0) | (b ? 1 : 0) << 1 | (c ? 1 : 0) << 2 | (d ? 1 : 0) << 3 | (e ? 1 : 0) << 4);
    }

    public static byte compressBooleans(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f) {
        return (byte) ((a ? 1 : 0) | (b ? 1 : 0) << 1 | (c ? 1 : 0) << 2 | (d ? 1 : 0) << 3 | (e ? 1 : 0) << 4 | (f ? 1 : 0) << 5);
    }

    public static byte compressBooleans(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g) {
        return (byte) ((a ? 1 : 0) | (b ? 1 : 0) << 1 | (c ? 1 : 0) << 2 | (d ? 1 : 0) << 3 | (e ? 1 : 0) << 4 | (f ? 1 : 0) << 5 | (g ? 1 : 0) << 6);
    }

    public static byte compressBooleans(boolean a, boolean b, boolean c, boolean d, boolean e, boolean f, boolean g, boolean h) {
        return (byte) ((a ? 1 : 0) | (b ? 1 : 0) << 1 | (c ? 1 : 0) << 2 | (d ? 1 : 0) << 3 | (e ? 1 : 0) << 4 | (f ? 1 : 0) << 5 | (g ? 1 : 0) << 6 | (h ? 1 : 0) << 7);
    }

    /*
        a method which takes 1 byte and 1 index and returns the boolean at the index of the byte
     */
    public static boolean getBoolean(byte b, int index) {
        return (b & (1 << index)) != 0;
    }

    //a method which takes 1 byte, 1 index, and a boolean to set the boolean at the index of the byte
    public static byte setBoolean(byte b, int index, boolean value) {
        if (value) {
            b |= (1 << index);
        } else {
            b &= ~(1 << index);
        }
        return b;
    }






}
