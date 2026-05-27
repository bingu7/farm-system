package com.example.controller;

import com.example.common.Result;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/files")
public class FileController {

    private static final Path FILE_DIRECTORY = Paths.get(System.getProperty("user.dir"), "files").toAbsolutePath().normalize();
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif", "webp", "bmp");
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp",
            "image/bmp",
            "image/x-ms-bmp"
    );

    @Value("${fileBaseUrl}")
    private String fileBaseUrl;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return Result.error("仅支持 JPG、JPEG、PNG、GIF、WEBP、BMP 图片");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            return Result.error("上传文件类型不合法");
        }

        try {
            Files.createDirectories(FILE_DIRECTORY);

            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
            Path targetPath = FILE_DIRECTORY.resolve(fileName).normalize();
            if (!targetPath.startsWith(FILE_DIRECTORY)) {
                return Result.error("文件路径不合法");
            }

            file.transferTo(targetPath);
            String url = fileBaseUrl + "/files/download/" + fileName;
            return Result.success(url);
        } catch (IOException e) {
            return Result.error("文件上传失败");
        }
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        String extension = getExtension(fileName);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path targetPath = FILE_DIRECTORY.resolve(fileName).normalize();
        if (!targetPath.startsWith(FILE_DIRECTORY) || !Files.exists(targetPath) || !Files.isRegularFile(targetPath)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String contentType = Files.probeContentType(targetPath);
        response.setContentType(contentType != null ? contentType : "application/octet-stream");
        response.setHeader("Content-Disposition",
                "inline;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

        byte[] bytes = Files.readAllBytes(targetPath);
        ServletOutputStream os = response.getOutputStream();
        os.write(bytes);
        os.flush();
        os.close();
    }

    private String getExtension(String fileName) {
        if (fileName == null) {
            return "";
        }
        int index = fileName.lastIndexOf('.');
        if (index < 0 || index == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(index + 1).toLowerCase();
    }
}
