package net.ddns.yline.withAPI.domain.file;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {

    private String originalName;
    private String uniqueName;
    private String path;
    private String type;
    private Long size;
    private FileStatus fileStatus;
}
