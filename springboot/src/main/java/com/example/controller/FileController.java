package com.example.controller;

import com.example.common.Result;
import com.example.exception.CustomException;
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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.max-upload-size}")
    private long maxUploadSize;

    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CustomException("请选择要上传的文件");
        }
        if (file.getSize() > maxUploadSize) {
            throw new CustomException("上传文件不能超过5MB");
        }

        String extension = getExtension(file.getOriginalFilename());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new CustomException("仅支持 JPG、JPEG、PNG、GIF、WEBP、BMP 图片");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new CustomException("上传文件类型不合法");
        }

        try {
            Path fileDirectory = getFileDirectory();
            Files.createDirectories(fileDirectory);

            String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
            Path targetPath = fileDirectory.resolve(fileName).normalize();
            if (!targetPath.startsWith(fileDirectory)) {
                throw new CustomException("文件路径不合法");
            }

            file.transferTo(targetPath);
            return Result.success(fileBaseUrl + "/files/download/" + fileName);
        } catch (IOException e) {
            throw new CustomException("文件上传失败");
        }
    }

    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        String extension = getExtension(fileName);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Path targetPath = resolveDownloadPath(fileName);
        if (targetPath == null) {
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

    private Path getFileDirectory() {
        return Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    private Path resolveDownloadPath(String fileName) {
        Path[] directories = new Path[]{
                getFileDirectory(),
                Paths.get(System.getProperty("user.dir"), "files").toAbsolutePath().normalize(),
                Paths.get(System.getProperty("user.dir"), "..", "files").toAbsolutePath().normalize()
        };

        for (Path directory : directories) {
            Path targetPath = directory.resolve(fileName).normalize();
            if (targetPath.startsWith(directory) && Files.exists(targetPath) && Files.isRegularFile(targetPath)) {
                return targetPath;
            }
        }

        return null;
    }
}
