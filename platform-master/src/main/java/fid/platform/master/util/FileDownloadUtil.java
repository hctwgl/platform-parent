package fid.platform.master.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * Created by mengtian on 2017/11/14
 */
public class FileDownloadUtil {

    public static void downloadExcel(String excelPath, String showName, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        File downloadFile = new File(excelPath);
        String fileName = downloadFile.getName();
        if (StringUtils.isEmpty(showName)) {
            showName = fileName;
        }
        showName = StringUtils.replace(showName, "+", "%20");//替换空格
        if (UserAgentUtil.isChrome(request) || UserAgentUtil.isFirefox(request)) {
            showName = new String(showName.getBytes("UTF-8"), "ISO-8859-1");
        } else {
            showName = URLEncoder.encode(showName, "UTF-8");
        }
        response.reset();
        response.setContentType("application/oct-stream;charset=UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + showName);
        response.setHeader("Connection", "close");
        //MimeUtility.encodeText(showName, "UTF-8", "B");
        InputStream input = null;
        BufferedOutputStream out = null;
        try {
            input = new FileInputStream(downloadFile);
            out = new BufferedOutputStream(response.getOutputStream());
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int size = 0;
            while ((size = input.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
            out.flush();
        } catch (Exception e) {
            throw new Exception("下载失败");
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(out);
        }
    }

}
