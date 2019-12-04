package org.horn.mommons.dao;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: hornlive
 * @Date: 2019/8/18 15:34
 */
public class DownUrlZipUtils {
    public static void main(String[] args) throws Exception {
//        String url = "http://ir.dcs.gla.ac.uk/resources/linguistic_utils/stop_words";   //文件URL地址
//        String url2 = "file:///D:/WORK_FILES/WORK_DATA/a.txt";

        /**test**/
        String url = "http://192.168.2.224:8000/data.zip";
        String downDir = "data/url/down";
        String unZipDir = downDir + "/" + "unzip";
        String zipFileName = "test.zip";
        String zipFilePath = downDir + "/" + zipFileName;

        FileUtils.deleteDirectory(new File(downDir));
        downloadFromURL(url, downDir, zipFileName);
//        downloadFromURL(url2, filePath, fileName, "GET");
//        unZip(zipFilePath, unZipDir);
//        ScalaFileUtils.readTxt(unZipDir);
//        ScalaFileUtils.readZip(zipFilePath);
    }


    /**
     * 从URL下载到文件<commons.io.FileUtils>
     *
     * @param url
     * @param dir
     * @param name
     * @return
     */
    public static String downloadFromURL(String url, String dir, String name) {
        try {
            URL httpurl = new URL(url);
            File f = new File(dir + "/" + name);
            FileUtils.copyURLToFile(httpurl, f);
        } catch (Exception e) {
            e.printStackTrace();
            return "Fault!";
        }
        return "Successful!";
    }

    /**
     * 从URL下载数据
     *
     * @param url
     * @param filePath
     * @param filename
     * @param method
     * @return
     */
    public static File downloadFromURL(String url, String filePath, String filename, String method) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath + filename);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }
        return file;
    }


    /**
     * 解压zip格式压缩包  <对应的是ant.jar>
     * 自建目录
     *
     * @param sourceZip
     * @param destDir
     * @throws Exception
     */
    public static void unZip(String sourceZip, String destDir) {
        try {
            Project p = new Project();
            Expand e = new Expand();
            e.setProject(p);
            e.setSrc(new File(sourceZip));
            e.setOverwrite(false);
            e.setDest(new File(destDir));
           /*
           ant下的zip工具默认压缩编码为UTF-8编码，
           而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
           所以解压缩时要制定编码格式
           */
//            e.setEncoding("gbk");
            e.execute();
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
    }
}
