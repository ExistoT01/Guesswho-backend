package com.existot01.guesswho.controller;

import com.existot01.pojo.Instance;
import com.existot01.pojo.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * 文件上传和下载
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${upload-img.path}")
    private String basePath;

    @Value("${mockdata.path}")
    private String mockData;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @CrossOrigin
    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        log.info(file.toString());

        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 创建一个目录对象
        File dir = new File(basePath);

        // 判断当前目录是否存在
        if (!dir.exists()) {
            // 目录不存在，需要创建
            dir.mkdirs();
        }

        // 将临时文件转存
        try {
            file.transferTo(new File(basePath + originalFilename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return originalFilename;
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @CrossOrigin
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {

        try {
            // 输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            response.setContentType("image/jpeg");

            byte[] bytes = new byte[1024];
            int len = 0;

            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            // 关闭资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @CrossOrigin
    @GetMapping("/analyse")
    public Instance analyse (String name) throws IOException {
        log.info(mockData);

        Instance ans = new Instance();

        ObjectMapper mapper = new ObjectMapper();

        Root root = mapper.readValue(new File(mockData), Root.class);

        for (Instance instance : root.getInstance()) {
            if (Objects.equals(instance.getImg_url(), name)) {
                ans = instance;
            }
        }

        return ans;
    }
}
