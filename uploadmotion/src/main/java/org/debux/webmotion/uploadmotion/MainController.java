package org.debux.webmotion.uploadmotion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import javax.activation.MimetypesFileTypeMap;
import org.apache.commons.io.IOUtils;
import org.debux.webmotion.server.WebMotionController;
import org.debux.webmotion.server.mapping.Properties;
import org.debux.webmotion.server.render.Render;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainController extends WebMotionController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    
    public Render upload(Properties conf, File file) throws IOException {
        String folderName = conf.getString("folder");
        
        String token = UUID.randomUUID().toString();
        String fileName = folderName + File.separator + token;
        
        FileInputStream inputStream = new FileInputStream(file);
        FileOutputStream outputStream = new FileOutputStream(fileName);
        
        IOUtils.copy(inputStream, outputStream);
        
        inputStream.close();
        outputStream.close();
        
        return renderJSON("token", token);
    }
    
    public Render download(Properties conf, String token) throws IOException {
        String folderName = conf.getString("folder");
        String fileName = folderName + File.separator + token;
        FileInputStream inputStream = new FileInputStream(fileName);
         
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        String mimeType = mimeTypesMap.getContentType(fileName);
        return renderStream(inputStream, mimeType);
    }
}
