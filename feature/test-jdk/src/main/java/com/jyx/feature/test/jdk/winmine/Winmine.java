package com.jyx.feature.test.jdk.winmine;

import com.sun.jna.Memory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author jiangyaxin
 * @since 2023/7/7 11:22
 */
public class Winmine {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        Win32Processor win32Processor = new Win32Processor("扫雷");

        Memory faceFlagMemory = win32Processor.readProcessMemory(0x01005160, 4);
        int faceFlag = faceFlagMemory.getInt(0);

        Memory widthMemory = win32Processor.readProcessMemory(0x01005334, 4);
        int width = widthMemory.getInt(0);

        Memory heightMemory = win32Processor.readProcessMemory(0x01005338, 4);
        int height = heightMemory.getInt(0);

        byte[][] gameArray = new byte[height][width];
        Memory gameArrayMemory = win32Processor.readProcessMemory(0x01005361, height * 32);

        for (int i = 0; i < height; i++) {
            gameArray[i] = gameArrayMemory.getByteArray(i * 32, width);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                executorService.submit(new WinmineThread(i, j) {
                    public void run() {
                        int bomb = 0x8F;
                        int currentBox = gameArray[i][j] & 0xFF;
                        if (currentBox != bomb) {
                            int x = 18 + (int) (j * 20 / win32Processor.getDesktopScaleValue());
                            int y = 62 + (int) (i * 20 / win32Processor.getDesktopScaleValue());
                            win32Processor.clickMouseLeftButton(x, y);
                        }
                    }
                });


            }
        }

        win32Processor.destroy();
    }
}
