package net.ddns.yline.withAPI.domain.file;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UploadedFile {

    private String originalName;
    private String uniqueName;
    private String path;
    private String type;
    private Long size;
    private FileStatus fileStatus;
}
