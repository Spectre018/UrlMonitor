package com.monitor.url.UrlMonitor.Controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.monitor.url.UrlMonitor.Service.UrlService;
import com.monitor.url.UrlMonitor.helper.UrlCount;


class UrlControllerTest {
	
	@Autowired
	UrlCount uelCount;
	
//	@Autowired
	 UrlService urlService = new UrlService();
	 Map<String, Integer> longToCountMap ;
	 Map<String, String> longToShortMap;
	 List<UrlCount> list = Collections.synchronizedList(new ArrayList<>());
 
	void startUp() {
		longToShortMap = new ConcurrentHashMap<>();
		longToCountMap = new ConcurrentHashMap<>();
		String[] urls = {"google.com","google.com-a", "google.com-b", "google.com-c", "google.com-d","google.com-e","google.com-f", "google.com-g", "google.com-h", "google.com-i"};
		for(int i=0;i<10;i++) {
			longToShortMap.put(urls[i], urlService.storeUrl(urls[i]));
			longToCountMap.put(urls[i],0);
		}
		
	}
	

//	@Test
//	void test() {
//		fail("Not yet implemented");
//	}
//	

	@Test
	public void storeUrlTest() {
		startUp();
		String url = "google.com";
		String expected = "a";
		longToShortMap.put(url, urlService.storeUrl(url));
		assertEquals(expected, longToShortMap.get(url));
	
	}
	

	
	@Test
    public void getLongToShortUrl() {
		startUp();
		String url = "google.com-a";
		String result = longToShortMap.get(url);
		String expected = "ba";
		assertEquals(expected, result);
		
    }
	
	@Test
	public void countTest() {
		startUp();
		String url = "google.com-a";
		for(int i=0;i<3;i++) {
			urlService.getLongToShortUrl(url);
		}
		int expected = 3;
		int result = urlService.count(url);
		assertEquals(expected, result);
	}
	
	@Test
	public void getListOfUrlsTest() {
		startUp();
		for(int i=0;i<3;i++) {
			urlService.getLongToShortUrl("google.com");
		}
		int page= 1, size =3;
		Map<String, Integer> resultMap = urlService.getListOfUrls(page, size);
		int resultSize = resultMap.size();
		int expectedSize = 3;
		assertEquals(expectedSize, resultSize);
	}

}
