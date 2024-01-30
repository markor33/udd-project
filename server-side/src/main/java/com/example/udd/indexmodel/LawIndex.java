package com.example.udd.indexmodel;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "law")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class LawIndex {

    @Id
    private UUID id;

    @Field(type = FieldType.Text, store = true, name = "title", analyzer = "serbian")
    private String title;

    @Field(type = FieldType.Text, store = true, name = "content", analyzer = "serbian")
    private String content;

    @Field(type = FieldType.Text, store = true, name = "serverFilename", index = false)
    private String serverFilename;

    public LawIndex(final String title, final String content, final String serverFilename) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.serverFilename = serverFilename;
    }
}
