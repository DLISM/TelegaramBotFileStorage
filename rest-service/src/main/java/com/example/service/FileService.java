package com.example.service;

import com.example.entity.AppDocument;
import com.example.entity.AppPhoto;
import com.example.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
}
