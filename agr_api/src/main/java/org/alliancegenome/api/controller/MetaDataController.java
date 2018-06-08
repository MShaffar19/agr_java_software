package org.alliancegenome.api.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.alliancegenome.api.rest.interfaces.MetaDataRESTInterface;
import org.alliancegenome.api.service.MetaDataService;
import org.alliancegenome.core.exceptions.GenericException;
import org.alliancegenome.es.index.data.APIResponce;
import org.alliancegenome.es.index.data.GetReleasesResponce;
import org.alliancegenome.es.index.data.SnapShotResponce;
import org.alliancegenome.es.index.data.SubmissionResponce;
import org.alliancegenome.es.index.data.doclet.SnapShotDoclet;
import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

@RequestScoped
public class MetaDataController extends BaseController implements MetaDataRESTInterface {

    @Inject
    private MetaDataService metaDataService;

    private Logger log = Logger.getLogger(getClass());

    @Override
    public SubmissionResponce submitData(String api_access_token, MultipartFormDataInput input) {
        SubmissionResponce res = new SubmissionResponce();

        Map<String, List<InputPart>> form = input.getFormDataMap();
        boolean success = true;

        for(String key: form.keySet()) {
            if(authenticate(api_access_token)) {
                InputPart inputPart = form.get(key).get(0);
                Date d = new Date();
                String outFileName = "tmp.data_" + d.getTime();
                File outfile = new File(outFileName);
                try {
                    InputStream is = inputPart.getBody(InputStream.class, null);
                    
                    log.info("Saving file to local filesystem: " + outfile.getAbsolutePath());
                    FileUtils.copyInputStreamToFile(is, outfile);
                    log.info("Save file to local filesystem complete");
                    
                    metaDataService.submitData(key, outfile);
                    res.getFileStatus().put(key, "success");
                } catch (GenericException | IOException e) {
                    log.error(e.getMessage());
                    outfile.delete();
                    res.getFileStatus().put(key, e.getMessage());
                    //e.printStackTrace();
                    success = false;
                }
            } else {
                res.getFileStatus().put(key, "Authentication Failure: Please check your api_access_token");
                success = false;
            }
        }
        if(success) {
            res.setStatus("success");
        } else {
            res.setStatus("failed");
        }
        return res;

    }

    @Override
    public APIResponce validateData(MultipartFormDataInput input) {
        SubmissionResponce res = new SubmissionResponce();

        Map<String, List<InputPart>> form = input.getFormDataMap();
        boolean success = true;
        
        for(String key: form.keySet()) {
            InputPart inputPart = form.get(key).get(0);
            
            Date d = new Date();
            String outFileName = "tmp.data_" + d.getTime();
            File outfile = new File(outFileName);
        
            try {
                InputStream is = inputPart.getBody(InputStream.class, null);
                
                log.info("Saving file to local filesystem: " + outfile.getAbsolutePath());
                FileUtils.copyInputStreamToFile(is, outfile);
                log.info("Save file to local filesystem complete");
                
                boolean passed = metaDataService.validateData(key, outfile);
                if(passed) {
                    res.getFileStatus().put(key, "success");
                } else {
                    res.getFileStatus().put(key, "failed");
                    success = false;
                }
            } catch (GenericException | IOException e) {
                log.error(e.getMessage());
                outfile.delete();
                res.getFileStatus().put(key, e.getMessage());
                //e.printStackTrace();
                success = false;
            }

        }
        if(success) {
            res.setStatus("success");
        } else {
            res.setStatus("failed");
        }
        return res;
    }

    @Override
    public APIResponce takeSnapShot(String api_access_token, String system, String releaseVersion) {
        SnapShotResponce res = new SnapShotResponce();
        res.setStatus("success");
        if(authenticate(api_access_token)) {
            SnapShotDoclet ssd = metaDataService.takeSnapShot(system, releaseVersion);
            res.setSnapShot(ssd);
        } else {
            res.setStatus("failed");
            res.setMessage("Authentication Failure: Please check your api_access_token");
        }
        
        return res;
    }

    @Override
    public APIResponce getSnapShot(String system, String releaseVersion) {
        SnapShotResponce res = new SnapShotResponce();
        res.setStatus("success");
        SnapShotDoclet ssd = metaDataService.getShapShot(system, releaseVersion);
        res.setSnapShot(ssd);
        return res;
    }

    @Override
    public APIResponce getReleases(String system) {
        GetReleasesResponce res = new GetReleasesResponce();
        res.setStatus("success");
        res.setReleases(metaDataService.getReleases(system));
        return res;
    }
    
}