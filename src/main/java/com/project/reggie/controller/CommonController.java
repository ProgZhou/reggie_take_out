package com.project.reggie.controller;

import com.project.reggie.common.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/** 处理文件的上传和下载
 * @author ProgZhou
 * @createTime 2022/06/06
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    /*
    * 文件上传处理
    * */
    @PostMapping("/upload")
    public CommonResult<String> upload(MultipartFile file) {
        log.info("文件上传处理: {}", file.toString());

        //使用UUID生成在服务器中的文件名
        String filename = UUID.randomUUID().toString();
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取文件后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //组合成新的存储在服务器中的文件名
        String newFilename = filename + suffix;
        File dir = new File(basePath);
        if(!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        log.info("新文件名: {}", newFilename);
        try {
            file.transferTo(new File(basePath + File.separator + newFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CommonResult.success(newFilename);
    }

    /*
    * 文件下载处理
    * */
    @GetMapping("/download")
    public void download(@RequestParam("name") String name, HttpServletResponse response) {
        FileInputStream inputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //通过输入流读取文件内容
            inputStream = new FileInputStream(basePath + File.separator + name);
            //通过输出流将文件内容写回浏览器
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            //通过byte数组存储
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
