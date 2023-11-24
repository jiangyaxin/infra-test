package com.jyx.feature.test.jdk.winmine;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.*;
import com.sun.jna.ptr.IntByReference;

/**
 * @author jiangyaxin
 * @since 2023/7/7 11:19
 */
public class Win32Processor {

    private static User32 user32 = User32.INSTANCE;
    private static Kernel32 kernel32 = Kernel32.INSTANCE;
    private static GDI32 gdi32 = GDI32.INSTANCE;

    private String windowName;
    private WinDef.HWND windowHandle;
    private int processId;
    private WinNT.HANDLE openProcessHandle;

    public Win32Processor(String windowName) {
        this.windowName = windowName;
        initUtil();
    }

    public void initUtil() {
        //获取窗口句柄   param1:类名 param2:窗口名字  return: 窗口句柄
        WinDef.HWND windowHandle = user32.FindWindow(null, windowName);
        if (windowHandle == null) {
            throw new NullPointerException(windowName + "窗口未启动");
        }

        //获取进程id param1:想要获取的窗口句柄 param2:返回值  return:The return value is the identifier of the thread that created the window.
        IntByReference processIdPointer = new IntByReference();
        user32.GetWindowThreadProcessId(windowHandle, processIdPointer);
        int processId = processIdPointer.getValue();

        //打开进程
        // param1:权限类型
        // param2:If this value is TRUE, processes created by this process will inherit the handle. Otherwise, the processes do not inherit this handle.
        // param3:进程Id
        // return: 打开线程的句柄
        WinNT.HANDLE openProcessHandle = kernel32.OpenProcess(Kernel32.PROCESS_ALL_ACCESS, false, processId);

        this.windowHandle = windowHandle;
        this.processId = processId;
        this.openProcessHandle = openProcessHandle;
    }

    public void updateUtil() {
        initUtil();
    }

    public void destroy() {
        kernel32.CloseHandle(windowHandle);
        kernel32.CloseHandle(openProcessHandle);
    }

    /**
     * boolean ReadProcessMemory(WinNT.HANDLE hProcess,
     * Pointer lpBaseAddress,
     * Pointer lpBuffer,
     * int nSize,
     * IntByReference lpNumberOfBytesRead)
     * Parameters:
     * hProcess - A handle to the process with memory that is being read. The handle must have PROCESS_VM_READ access to the process.
     * lpBaseAddress - A pointer to the base address in the specified process from which to read.
     * Before any data transfer occurs, the system verifies that all data in the base address and memory of the specified size is accessible for read access, and if it is not accessible the function fails.
     * lpBuffer - A pointer to a buffer that receives the contents from the address space of the specified process.
     * nSize - The number of bytes to be read from the specified process.
     * lpNumberOfBytesRead - A pointer to a variable that receives the number of bytes transferred into the specified buffer. If lpNumberOfBytesRead is NULL, the parameter is ignored.
     * Returns:
     * If the function succeeds, the return value is nonzero.
     * If the function fails, the return value is 0 (zero). To get extended error information, call GetLastError.
     * The function fails if the requested read operation crosses into an area of the process that is inaccessible.
     */
    public Memory readProcessMemory(long baseAddress, int size) {
        Pointer lpBaseAddress = new Pointer(baseAddress);
        Memory lpBuffer = new Memory(size);
        boolean readSuccess = kernel32.ReadProcessMemory(openProcessHandle, lpBaseAddress, lpBuffer, size, null);
        if (!readSuccess) {
            updateUtil();
            readSuccess = kernel32.ReadProcessMemory(openProcessHandle, lpBaseAddress, lpBuffer, size, null);
        }
        if (!readSuccess) {
            throw new NullPointerException("【" + windowName + "】窗口读取从" + baseAddress + "开始的" + size + "个字节失败");
        }
        return lpBuffer;
    }

    public WinDef.RECT readWindowPosition() {
        WinDef.RECT windowRect = new WinDef.RECT();
        boolean readSuccess = user32.GetWindowRect(windowHandle, windowRect);
        if (!readSuccess) {
            updateUtil();
            readSuccess = user32.GetWindowRect(windowHandle, windowRect);
        }
        if (!readSuccess) {
            throw new NullPointerException("【" + windowName + "】窗口读取位置信息Rect失败");
        }
        return windowRect;
    }

    //按下鼠标左键
    public static final int WM_MOUSEMOVE = 0x0200;
    //按下鼠标左键
    public static final int WM_LBUTTONDOWN = 0x0201;
    //释放鼠标左键
    public static final int WM_LBUTTONUP = 0x0202;
    public static Object lock = new Object();

    public void clickMouseLeftButton(int x, int y) {
        double scaleValue = getDesktopScaleValue();
        long pointX = (long) (x * scaleValue);
        long pointY = (long) (y * scaleValue);

        WinDef.LPARAM lParam = new WinDef.LPARAM(pointY << 16 | pointX);
        synchronized (lock) {
            user32.SendMessage(windowHandle, WM_LBUTTONDOWN, new WinDef.WPARAM(1), lParam);
            user32.SendMessage(windowHandle, WM_LBUTTONUP, new WinDef.WPARAM(0), lParam);
        }
    }

    //沿屏幕宽度每逻辑英寸的像素数，在多显示器系统中，该值对所显示器相同；
    public static final int LOGPIXELSX = 88;
    //沿屏幕高度每逻辑英寸的像素数，在多显示器系统中，该值对所显示器相同；
    public static final int LOGPIXELSY = 90;

    /**
     * dpi   scale
     * 96    100
     * 120   125
     * 144   150
     * 192   200
     *
     * @return
     */
    public double getDesktopScaleValue() {
        // Get desktop dc
        WinDef.HDC desktopHdc = user32.GetDC(null);
        // Get native resolution
        int horizontalDPI = gdi32.GetDeviceCaps(desktopHdc, LOGPIXELSX);
        //int verticalDPI=gdi32.GetDeviceCaps(desktopHdc,LOGPIXELSY);
        switch (horizontalDPI) {
            case 92:
                return 1.0;
            case 120:
                return 1.25;
            case 144:
                return 1.5;
            case 192:
                return 2.0;
            default:
                return 1;
        }
    }

}
