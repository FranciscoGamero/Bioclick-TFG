package com.salesianostriana.bioclick.service.local;

import com.salesianostriana.bioclick.model.AbstractFileMetadata;
import com.salesianostriana.bioclick.model.FileMetadata;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class LocalFileMetadataImpl extends AbstractFileMetadata {

    public static FileMetadata of(String filename) {
        return LocalFileMetadataImpl.builder()
                .id(filename)
                .filename(filename)
                .build();
    }

}
