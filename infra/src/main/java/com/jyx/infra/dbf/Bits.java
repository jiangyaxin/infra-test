package com.jyx.infra.dbf;

public class Bits {
	public static int makeInt(byte b1, byte b2) {
		return ((b1) & 0x000000FF) +
			   ((b2 <<  8) & 0x0000FF00);
	}
	
	public static int makeInt(byte b1, byte b2, byte b3, byte b4) {
		return ((b1) & 0x000000FF) +
		       ((b2 <<  8) & 0x0000FF00) + 
		       ((b3 << 16) & 0x00FF0000) + 
		       ((b4 << 24) & 0xFF000000);
	}
	
	public static byte[] makeByte4(int i) {
		byte[] b = {
			(byte)(i & 0x000000FF),
			(byte)((i >> 8)& 0x000000FF),
			(byte)((i >> 16)& 0x000000FF),
			(byte)((i >> 24)& 0x000000FF)
		};
		return b;
	}
	
	public static byte[] makeByte2(int i) {
		byte[] b = {
			(byte)(i & 0x000000FF),
			(byte)((i >> 8)& 0x000000FF)
		};
		return b;
	}

	public static int littleEndian(final int value) {

		int num1 = value;
		int mask = 0xFF;
		int num2 = 0x00;

		num2 |= (num1 & mask);

		for (int i = 1; i < 4; ++i) {
			num2 <<= 8;
			mask <<= 8;
			num2 |= (num1 & mask) >> 8 * i;
		}

		return num2;
	}
}
