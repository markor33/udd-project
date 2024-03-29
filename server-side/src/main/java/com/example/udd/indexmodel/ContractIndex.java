package com.example.udd.indexmodel;

import com.example.udd.infrastructure.model.Location;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "contract")
@Setting(settingPath = "/configuration/serbian-analyzer-config.json")
public class ContractIndex {

    @Id
    private UUID id;

    @Field(type = FieldType.Text, store = true, name = "title", analyzer = "serbian")
    private String title;

    @Field(type = FieldType.Text, store = true, name = "firstName", analyzer = "serbian")
    private String firstName;

    @Field(type = FieldType.Text, store = true, name = "lastName", analyzer = "serbian")
    private String lastName;

    @Field(type = FieldType.Text, store = true, name = "governmentName", analyzer = "serbian")
    private String governmentName;

    @Field(type = FieldType.Text, store = true, name = "levelOfAdministration", analyzer = "serbian")
    private String levelOfAdministration;

    @Field(type = FieldType.Text, store = true, name = "content", analyzer = "serbian")
    private String content;

    @Field(type = FieldType.Text, store = true, name = "serverFilename", index = false)
    private String serverFilename;

    @GeoPointField
    @Field(store = true, name = "location")
    private GeoPoint location;

    public ContractIndex(
            final String title,
            final String firstName,
            final String lastName,
            final String governmentName,
            final String levelOfAdministration,
            final String content,
            final String serverFilename,
            final Location location
    ) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.governmentName = governmentName;
        this.levelOfAdministration = levelOfAdministration;
        this.content = content;
        this.serverFilename = serverFilename;
        this.location = new GeoPoint(location.getLat(), location.getLon());
    }
}
