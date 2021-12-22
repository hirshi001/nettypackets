package nettypackets.util.defaultpackets.primitivepackets;

import nettypackets.util.BooleanCompression;

/**
 * A class that represents a packet that contains multiple booleans.
 * The booleans are stored in a byte.
 */
public class MultiBooleanPacket extends BytePacket {

    public MultiBooleanPacket(byte data) {
        super(data);
    }

    public MultiBooleanPacket(boolean... data) {
        this.data = BooleanCompression.compressBooleans(data);
    }

    //Create 8 constructors, one for each bit in the byte.
    public MultiBooleanPacket(boolean bit0) {
        this.data = BooleanCompression.compressBooleans(bit0);
    }
    public MultiBooleanPacket(boolean bit0, boolean bit1) {
        this.data = BooleanCompression.compressBooleans(bit0, bit1);
    }
    public MultiBooleanPacket(boolean bit0, boolean bit1, boolean bit2) {
        this.data = BooleanCompression.compressBooleans(bit0, bit1, bit2);
    }
    public MultiBooleanPacket(boolean bit0, boolean bit1, boolean bit2, boolean bit3) {
        this.data = BooleanCompression.compressBooleans(bit0, bit1, bit2, bit3);
    }
    public MultiBooleanPacket(boolean bit0, boolean bit1, boolean bit2, boolean bit3, boolean bit4) {
        this.data = BooleanCompression.compressBooleans(bit0, bit1, bit2, bit3, bit4);
    }
    public MultiBooleanPacket(boolean bit0, boolean bit1, boolean bit2, boolean bit3, boolean bit4, boolean bit5) {
        this.data = BooleanCompression.compressBooleans(bit0, bit1, bit2, bit3, bit4, bit5);
    }
    public MultiBooleanPacket(boolean bit0, boolean bit1, boolean bit2, boolean bit3, boolean bit4, boolean bit5, boolean bit6) {
        this.data = BooleanCompression.compressBooleans(bit0, bit1, bit2, bit3, bit4, bit5, bit6);
    }
    public MultiBooleanPacket(boolean bit0, boolean bit1, boolean bit2, boolean bit3, boolean bit4, boolean bit5, boolean bit6, boolean bit7) {
        this.data = BooleanCompression.compressBooleans(bit0, bit1, bit2, bit3, bit4, bit5, bit6, bit7);
    }

    //getters
    public boolean getBit0() {
        return BooleanCompression.getBoolean(data, 0);
    }
    public boolean getBit1() {
        return BooleanCompression.getBoolean(data, 1);
    }
    public boolean getBit2() {
        return BooleanCompression.getBoolean(data, 2);
    }
    public boolean getBit3() {
        return BooleanCompression.getBoolean(data, 3);
    }
    public boolean getBit4() {
        return BooleanCompression.getBoolean(data, 4);
    }
    public boolean getBit5() {
        return BooleanCompression.getBoolean(data, 5);
    }
    public boolean getBit6() {
        return BooleanCompression.getBoolean(data, 6);
    }
    public boolean getBit7() {
        return BooleanCompression.getBoolean(data, 7);
    }
    public boolean getNthBit(int n) {
        return BooleanCompression.getBoolean(data, n);
    }

    //setters
    public void setBit0(boolean bit0) {
        data = BooleanCompression.setBoolean(data, 0, bit0);
    }
    public void setBit1(boolean bit1) {
        data = BooleanCompression.setBoolean(data, 1, bit1);
    }
    public void setBit2(boolean bit2) {
        data = BooleanCompression.setBoolean(data, 2, bit2);
    }
    public void setBit3(boolean bit3) {
        data = BooleanCompression.setBoolean(data, 3, bit3);
    }
    public void setBit4(boolean bit4) {
        data = BooleanCompression.setBoolean(data, 4, bit4);
    }
    public void setBit5(boolean bit5) {
        data = BooleanCompression.setBoolean(data, 5, bit5);
    }
    public void setBit6(boolean bit6) {
        data = BooleanCompression.setBoolean(data, 6, bit6);
    }
    public void setBit7(boolean bit7) {
        data = BooleanCompression.setBoolean(data, 7, bit7);
    }
    public void setNthBit(int n, boolean bit) {
        data = BooleanCompression.setBoolean(data, n, bit);
    }

}
