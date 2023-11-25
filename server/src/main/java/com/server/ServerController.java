package com.server;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ServerController {
    @ResponseBody
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public List<ServerEntity.Bundle> home() {
        List<ServerEntity.Bundle> response = new ArrayList<>();

        ServerEntity.Bundle bundleA = new ServerEntity.Bundle();
        bundleA.setName("AModel");
        bundleA.setDesc("第一个模块");
        response.add(bundleA);

        ServerEntity.Bundle bundleB = new ServerEntity.Bundle();
        bundleB.setName("BModel");
        bundleB.setDesc("第二个模块");
        response.add(bundleB);

        return response;
    }

    @ResponseBody
    @RequestMapping(value = "/bundle/{name}", method = RequestMethod.GET)
    public ServerEntity.Bundle getBundle(@PathVariable String name) {
        ServerEntity.Bundle response = new ServerEntity.Bundle();

        if (name.equals("AModel")) {
            response.setName("AModel");
            response.setDesc("第一个模块");
        } else {
            response.setName("BModel");
            response.setDesc("第二个模块");
        }
        response.setLogo("");

        return response;
    }

    @RequestMapping(value = "/download/bundle/{name}", method = RequestMethod.GET, produces = "application/zip")
    public ResponseEntity<Resource> download(@PathVariable String name) throws Exception {
        File file;

        if (name.equals("AModel")) {
            file = new File("E:\\Workspace\\rnWorkspace\\react-native-bundle\\AModel\\AModel.zip");
        } else {
            file = new File("E:\\Workspace\\rnWorkspace\\react-native-bundle\\BModel\\BModel.zip");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + name + ".zip");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
    }
}