package com.example.udd.indexmodel;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

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

    @Field(type = FieldType.Text, store = true, name = "firstName")
    private String firstName;

    @Field(type = FieldType.Text, store = true, name = "lastName")
    private String lastName;

    @Field(type = FieldType.Text, store = true, name = "governmentName")
    private String governmentName;

    @Field(type = FieldType.Text, store = true, name = "levelOfAdministration")
    private String levelOfAdministration;

    @Field(type = FieldType.Text, store = true, name = "content")
    private String content;

    @Field(type = FieldType.Text, store = true, name = "serverFilename", index = false)
    private String serverFilename;

    @GeoPointField
    @Field(store = true, name = "location")
    private GeoPoint location;
}
