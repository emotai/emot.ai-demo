/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ai.emot.demo;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.ml4j.imaging.SerializableBufferedImageAdapter;
import org.ml4j.imaging.targets.ImageDisplay;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import ai.emot.api.EmotAI;
import ai.emot.api.impl.EmotAITemplate;
import ai.emot.core.EmotionProfile;
/**
 * 
 * @author Michael Lavelle
 *
 */
public class EmotAIDemo {

	public static void main(String[] args) throws IOException, InterruptedException
	{
		if (args.length != 2)
		{
			printUsage();
	        System.exit(0);
		}
		
		String emotAIAPIBaseUrl = args[0];
		String accessToken = args[1];
		
		PathMatchingResourcePatternResolver fileResolver = new PathMatchingResourcePatternResolver(EmotAIDemo.class.getClassLoader());
		Resource[] resources = fileResolver.getResources("images");
		File dir = resources[0].getFile();
		File imagesDir = new File(dir,"/face/cropped");
		
		EmotAI emotAI = new EmotAITemplate(emotAIAPIBaseUrl,accessToken);
		
		// Create a display for the images
		ImageDisplay<Long> imageDisplay = new ImageDisplay<Long>(250,250);
			
		for (File imageFile : imagesDir.listFiles(new JpegFileFilter()))
		{
			// Read each image
			BufferedImage image = ImageIO.read(imageFile);
			
			// Get the emotion profile for each image
			EmotionProfile emotionProfile = emotAI.emotionOperations().getFaceImageEmotionProfile(image);
			
			// Output emotion, and display image
			System.out.println(imageFile.getName() + " : " + emotionProfile);
			imageDisplay.onFrameUpdate(new SerializableBufferedImageAdapter(image), 1l);
			
			// Sleep for 1 second
			Thread.sleep(1000);
			
		}
	
        System.exit(1);
	}
	
	 private static void printUsage() {

	        StringBuffer use = new StringBuffer();
	        use.append("usage: java EmotAIDemo <emot.ai.api.base.url> <emot.ai.api.access.token>\n\n");
	        System.err.println (use);
	    }
}
