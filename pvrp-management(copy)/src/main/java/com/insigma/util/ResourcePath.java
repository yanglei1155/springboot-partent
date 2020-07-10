package com.insigma.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class ResourcePath {
	@Autowired
	private ResourceLoader resourceLoader;

	public Resource getResourcePath(String fileName) throws IOException {
		Resource resource = new ClassPathResource("templates/" + fileName);
		// Resource resource =
		// this.resourceLoader.getResource(ResourceUtils.CLASSPATH_URL_PREFIX +
		// "templates/"+fileName);
		return resource;
	}

	public void createSimsun() {
		try {
			ClassPathResource classPathResource = new ClassPathResource("templates/simsun.ttc");
            InputStream stream = classPathResource.getInputStream();
//			InputStream stream = this.getClass().getClassLoader().getResourceAsStream("templates/simsun.ttc");
			File targetFile = new File("templates/tmp/simsun.ttc");
			org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, targetFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
