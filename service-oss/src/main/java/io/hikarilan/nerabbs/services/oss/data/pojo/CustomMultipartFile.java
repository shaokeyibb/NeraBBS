package io.hikarilan.nerabbs.services.oss.data.pojo;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

@Value
public class CustomMultipartFile implements MultipartFile {

    @NotNull
    public String name;

    @Nullable
    public String originalFilename;

    @Nullable
    public String contentType;

    public byte[] bytes;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public long getSize() {
        return bytes.length;
    }

    @Override
    public @NotNull InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    @Override
    public void transferTo(@NotNull File dest) {
        throw new UnsupportedOperationException();
    }
}
