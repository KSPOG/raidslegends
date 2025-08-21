#include <windows.h>

extern "C" {

__declspec(dllexport) BOOL clickRaid(int x, int y) {
    HWND hwnd = FindWindowA(nullptr, "Raid: Shadow Legends");
    if (!hwnd) return FALSE;
    LPARAM lParam = (y << 16) | (x & 0xFFFF);
    PostMessage(hwnd, WM_LBUTTONDOWN, MK_LBUTTON, lParam);
    PostMessage(hwnd, WM_LBUTTONUP, 0, lParam);
    return TRUE;
}

__declspec(dllexport) BOOL captureRaid(const char* path) {
    HWND hwnd = FindWindowA(nullptr, "Raid: Shadow Legends");
    if (!hwnd) return FALSE;
    RECT rc;
    if (!GetClientRect(hwnd, &rc)) return FALSE;
    HDC hdcWindow = GetDC(hwnd);
    HDC hdcMemDC = CreateCompatibleDC(hdcWindow);
    int width = rc.right - rc.left;
    int height = rc.bottom - rc.top;
    HBITMAP hbmp = CreateCompatibleBitmap(hdcWindow, width, height);
    if (!hbmp) {
        DeleteDC(hdcMemDC);
        ReleaseDC(hwnd, hdcWindow);
        return FALSE;
    }
    SelectObject(hdcMemDC, hbmp);
    BitBlt(hdcMemDC, 0, 0, width, height, hdcWindow, 0, 0, SRCCOPY);

    BITMAPFILEHEADER bmfHeader{};
    BITMAPINFOHEADER bi{};
    bi.biSize = sizeof(BITMAPINFOHEADER);
    bi.biWidth = width;
    bi.biHeight = height;
    bi.biPlanes = 1;
    bi.biBitCount = 32;
    bi.biCompression = BI_RGB;
    DWORD dwBmpSize = width * height * 4;

    HANDLE hDIB = GlobalAlloc(GHND, dwBmpSize);
    char* lpbitmap = (char*)GlobalLock(hDIB);
    GetDIBits(hdcWindow, hbmp, 0, height, lpbitmap, (BITMAPINFO*)&bi, DIB_RGB_COLORS);

    bmfHeader.bfOffBits = sizeof(BITMAPFILEHEADER) + sizeof(BITMAPINFOHEADER);
    bmfHeader.bfSize = dwBmpSize + bmfHeader.bfOffBits;
    bmfHeader.bfType = 0x4D42; // BM

    HANDLE hFile = CreateFileA(path, GENERIC_WRITE, 0, nullptr, CREATE_ALWAYS, FILE_ATTRIBUTE_NORMAL, nullptr);
    if (hFile == INVALID_HANDLE_VALUE) {
        GlobalUnlock(hDIB);
        GlobalFree(hDIB);
        DeleteObject(hbmp);
        DeleteDC(hdcMemDC);
        ReleaseDC(hwnd, hdcWindow);
        return FALSE;
    }

    DWORD dwBytesWritten = 0;
    WriteFile(hFile, &bmfHeader, sizeof(BITMAPFILEHEADER), &dwBytesWritten, nullptr);
    WriteFile(hFile, &bi, sizeof(BITMAPINFOHEADER), &dwBytesWritten, nullptr);
    WriteFile(hFile, lpbitmap, dwBmpSize, &dwBytesWritten, nullptr);
    CloseHandle(hFile);

    GlobalUnlock(hDIB);
    GlobalFree(hDIB);
    DeleteObject(hbmp);
    DeleteDC(hdcMemDC);
    ReleaseDC(hwnd, hdcWindow);
    return TRUE;
}

}
