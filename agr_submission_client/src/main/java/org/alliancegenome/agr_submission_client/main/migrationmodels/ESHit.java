package org.alliancegenome.agr_submission_client.main.migrationmodels;

import java.util.List;

import org.alliancegenome.agr_submission.entities.DataSubType;
import org.alliancegenome.agr_submission.entities.DataType;
import org.alliancegenome.agr_submission.entities.ReleaseVersion;
import org.alliancegenome.agr_submission.entities.SchemaFile;
import org.alliancegenome.agr_submission.entities.SchemaVersion;
import org.alliancegenome.agr_submission.forms.CreateSchemaFileForm;
import org.alliancegenome.agr_submission_client.DataSubTypeControllerClientAPI;
import org.alliancegenome.agr_submission_client.DataTypeControllerClientAPI;
import org.alliancegenome.agr_submission_client.ReleaseVersionControllerClientAPI;
import org.alliancegenome.agr_submission_client.SchemaVersionControllerClientAPI;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

@Getter @Setter @ToString
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = As.PROPERTY, property = "_type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ESDataFile.class, name = "data_file"),
    @JsonSubTypes.Type(value = ESSnapshot.class, name = "data_snapshot"),
    @JsonSubTypes.Type(value = ESMetaData.class, name = "meta_data"),
})
@Log4j2
public abstract class ESHit {
    private String _id;
    private String _index;
    
    private static SchemaVersionControllerClientAPI schemaApi = new SchemaVersionControllerClientAPI("http://localhost:8080/api");
    private static ReleaseVersionControllerClientAPI releaseApi = new ReleaseVersionControllerClientAPI("http://localhost:8080/api");
    private static DataTypeControllerClientAPI dataTypeApi = new DataTypeControllerClientAPI("http://localhost:8080/api");
    private static DataSubTypeControllerClientAPI dataSubTypeApi = new DataSubTypeControllerClientAPI("http://localhost:8080/api");
    
    
    public abstract void generateAPICalls();
    
    public SchemaVersion getSchemaVersion(String schemaVersion) {
        List<SchemaVersion> list = schemaApi.getSchemaVersions();
        SchemaVersion selected = null;
        for(SchemaVersion o: list) {
            if(schemaVersion.equals(o.getSchema())) {
                selected = o;
                break;
            }
        }
        return selected;
    }
    
    
    public ReleaseVersion getReleaseVersion(String release) {
        List<ReleaseVersion> list = releaseApi.getReleaseVersions();
        ReleaseVersion selected = null;
        for(ReleaseVersion o: list) {
            if(release.equals(o.getReleaseVersion())) {
                selected = o;
                break;
            }
        }
        return selected;
    }
    
    public DataType getDataType(String dataType) {
        List<DataType> list = dataTypeApi.getDataTypes();
        DataType selected = null;
        for(DataType o: list) {
            if(dataType.equals(o.getName())) {
                selected = o;
                break;
            }
        }
        return selected;
    }
    
    public DataSubType getDataSubType(String dataSubType) {
        List<DataSubType> list = dataSubTypeApi.getDataSubTypes();
        DataSubType selected = null;
        for(DataSubType o: list) {
            if(dataSubType.equals(o.getName())) {
                selected = o;
                break;
            }
        }
        return selected;
    }
    
    
    public ReleaseVersion addReleaseSchema(String releaseVersion, String schema) {
        return releaseApi.addSchema(releaseVersion, schema);
    }

    public ReleaseVersion createReleaseVersion(ReleaseVersion entity) {
        return releaseApi.create(entity);
    }

    public SchemaVersion createSchemaVersion(SchemaVersion entity) {
        return schemaApi.create(entity);
    }
    
    public DataType createDataType(DataType dataType) {
        return dataTypeApi.create(dataType);
    }
    
    public DataType addSchemaFile(String dataType, CreateSchemaFileForm form) {
        DataType type = getDataType(dataType);
        if(type != null) {
            boolean exists = false;
            for(SchemaFile sf: type.getSchemaFiles()) {
                if(sf.getSchemaVersion().getSchema().equals(form.getSchema())) {
                    exists = true;
                    break;
                }
            }
            if(!exists) {
                return dataTypeApi.addSchemaFile(dataType, form);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    
}