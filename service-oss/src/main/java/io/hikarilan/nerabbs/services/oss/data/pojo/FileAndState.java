package io.hikarilan.nerabbs.services.oss.data.pojo;

import io.minio.GetObjectResponse;
import io.minio.StatObjectResponse;

public record FileAndState(GetObjectResponse file, StatObjectResponse fileStat) {
}
