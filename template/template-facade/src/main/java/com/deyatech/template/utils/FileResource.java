package com.deyatech.template.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @Description: 获取本地指定路径下资源文件夹及文件, 包含文件内容读取
 * @Author: zhaichen
 * @Date: 2018-07-16 13:56
 * @Version: 1.0
 * @Created in idea by autoCode
 */
public class FileResource {

    private static Logger logger = LoggerFactory.getLogger(FileResource.class);

    /**
     * 获取当前路径下的资源
     *
     * @param rootDir 定义的根目录
     * @param path    当前路径
     * @return
     */
    public static String getFiles(String rootDir, String path) {

        JSONArray files = new JSONArray();
        File file = new File(path);
        File[] tempList = file.listFiles();
        path = path.replaceAll("\\\\", "/");
        if (path.lastIndexOf('/') != path.length() - 1) {
            path = path + '/';
        }
        rootDir = rootDir.replaceAll("\\\\", "/");
        if (rootDir.lastIndexOf('/') != rootDir.length() - 1) {
            rootDir = rootDir + '/';
        }

        if (!path.equals(rootDir)) {
            JSONObject parent = new JSONObject();
            parent.put("fileType", "folder");
            parent.put("fileName", "...");
            parent.put("filePath", file.getParent() + "/");
            files.add(parent);
        }
        if(ObjectUtil.isNotNull(tempList)){
            tempList = sort(tempList);
            for (int i = 0; i < tempList.length; i++) {
                JSONObject object = new JSONObject();
                if(tempList[i].getName().indexOf(".") != 0){
                    if (tempList[i].isFile() && !"README.md".equals(tempList[i].getName())) {
                        object.put("fileType", "file");
                        object.put("fileName", tempList[i].getName());
                        object.put("filePath", tempList[i].toString());
                        object.put("lastModified", timestampToDate(tempList[i].lastModified(), "yyyy-MM-dd HH:mm"));
                        files.add(object);
                    }
                    if (tempList[i].isDirectory()) {
                        object.put("fileType", "folder");
                        object.put("fileName", tempList[i].getName());
                        object.put("filePath", tempList[i].toString());
                        object.put("lastModified", timestampToDate(tempList[i].lastModified(), "yyyy-MM-dd HH:mm"));
                        files.add(object);
                    }
                }
            }
        }
        logger.info("获取的资源数据为:{}", files.toString());
        return files.toString();
    }

    /*
     *   获取当前文件的内容
     */
    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return stringToHTMLString(new String(filecontent, encoding));
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * long类型时间转string类型
     *
     * @param var0
     * @param var2
     * @return
     */
    public static String timestampToDate(long var0, String var2) {
        Timestamp var3 = new Timestamp(var0);
        return (new SimpleDateFormat(var2)).format(var3);
    }

    public static String stringToHTMLString(String string) {
        StringBuffer sb = new StringBuffer(string.length());
        // true if last char was blank
        boolean lastWasBlankChar = false;
        int len = string.length();
        char c;


        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ') {
                // blank gets extra work,
                // this solves the problem you get if you replace all
                // blanks with &nbsp;, if you do that you loss
                // word breaking
                if (lastWasBlankChar) {
                    lastWasBlankChar = false;
                    sb.append("&nbsp;");
                } else {
                    lastWasBlankChar = true;
                    sb.append(' ');
                }
            } else {
                lastWasBlankChar = false;
                //
                // HTML Special Chars
                if (c == '"') {
                    sb.append("&quot;");
                } else if (c == '&') {
                    sb.append("&amp;");
                } else if (c == '<') {
                    sb.append("&lt;");
                } else if (c == '>') {
                    sb.append("&gt;");
                } else if (c == '\n')
                // Handle Newline
                {
                    sb.append("<br/>");
                } else {
                    int ci = 0xffff & c;
                    if (ci < 160)
                    // nothing special only 7 Bit
                    {
                        sb.append(c);
                    } else {
                        // Not 7 Bit use the unicode system
                        sb.append("&#");
                        sb.append(new Integer(ci).toString());
                        sb.append(';');
                    }
                }
            }
        }
        return sb.toString();
    }

    /*
     *   获取当前文件的内容
     */
    public static String readToString2(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 递归获取当前路径下的全部资源
     *
     * @param path 当前路径
     * @return
     */
    public static String getAllFiles(String path,String type) {
        JSONArray files = new JSONArray();
        path = path.replaceAll("\\\\", "/");
        String dir = path.substring(path.lastIndexOf("/")+1);
        if(dir.equals("images") || dir.equals("js") || dir.equals("styles")){
            return null;
        }
        if(StrUtil.isNotBlank(type) && !type.equals(dir) && !dir.equals("template")){
            return null;
        }
        File file = new File(path);
        File[] tempList = file.listFiles();
        if(ObjectUtil.isNotNull(tempList)) {
            tempList = sort(tempList);
            for (int i = 0; i < tempList.length; i++) {
                JSONObject object = new JSONObject();
                if(tempList[i].getName().indexOf(".") != 0){
                    if (tempList[i].isFile() && !"README.md".equals(tempList[i].getName())) {
                        object.put("fileType", "file");
                        object.put("fileName", tempList[i].getName());
                        object.put("filePath", tempList[i].toString());
                        object.put("lastModified", timestampToDate(tempList[i].lastModified(), "yyyy-MM-dd HH:mm"));
                        files.add(object);
                    }
                    if (tempList[i].isDirectory()) {
                        object.put("fileType", "folder");
                        object.put("fileName", tempList[i].getName());
                        object.put("filePath", tempList[i].toString());
                        object.put("lastModified", timestampToDate(tempList[i].lastModified(), "yyyy-MM-dd HH:mm"));

                        String children = getAllFiles(tempList[i].toString(),type);
                        if(StrUtil.isNotBlank(children)){
                            object.put("children", children);
                            files.add(object);
                        }
                    }

                }
            }
        }
        logger.info("获取的资源数据为:{}", files.toString());
        return files.toString();
    }


    /**
     * 将文件数组排序，目录放在上面，文件在下面
     * @param file
     * @return
     */
    public static File[] sort(File[] file){
        ArrayList<File> list = new ArrayList<File>();
        for(File f:file){
            if(f.isDirectory()){
                list.add(f);
            }
        }
        for(File f:file){
            if(f.isFile()){
                list.add(f);
            }
        }
        return list.toArray(new File[file.length]);
    }
    public static void main(String[] args) {
//        getFiles("d:\\temp");
        String files = getAllFiles("D:/temp/","");
        System.out.println(files);
    }

}
