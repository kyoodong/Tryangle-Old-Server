package com.gomson.tryangle;

import com.gomson.tryangle.api.image.ImageService;
import com.gomson.tryangle.config.WebConfig;
import com.gomson.tryangle.domain.component.ObjectComponent;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebConfig.class)
@SpringBootTest
class ImageServiceTest {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ImageService imageService;

	@Test
	void segmentation() throws IOException, JSONException {
		ClassLoader classLoader = getClass().getClassLoader();
		File imageFile = new File(classLoader.getResource("test.jpg").getFile());
		List<ObjectComponent> componentList =
				imageService.imageSegmentation(FileUtils.readFileToByteArray(imageFile));
		System.out.println("segmentation test complete");
	}

}
