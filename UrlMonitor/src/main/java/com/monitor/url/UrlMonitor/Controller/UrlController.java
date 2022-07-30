package com.monitor.url.UrlMonitor.Controller;


import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.monitor.url.UrlMonitor.Service.UrlService;

@RestController
public class UrlController {
	
	@Autowired
	private UrlService urlService;
	
	@GetMapping("/")
	public String welcome() {
		return "Welcome to url encoder!";
	}
	
	@GetMapping("storeurl")
	public ResponseEntity<Object> storeUrl(@RequestBody @RequestParam("url") String url) {
		urlService.encode(url);
		return new ResponseEntity<Object>(Collections.singletonMap("url", url), HttpStatus.OK);
	}
	
	@GetMapping("/geturl")
	public ResponseEntity<Object> getUrl(@RequestBody @RequestParam("url") String url) {
		String res=urlService.getLongToShortUrl(url);
		if(res!=null) {
			return new ResponseEntity<Object>(Collections.singletonMap("shorturl ", res), HttpStatus.OK);
		}
			
		return new ResponseEntity<Object>(Collections.singletonMap("url not found", url), HttpStatus.BAD_REQUEST);

	}
	
	@GetMapping("/visitcount")
	public ResponseEntity<Object> visitCount(@RequestParam("url") String url) {
		int count = urlService.count(url);;
		if(count==0) {
			return new ResponseEntity<Object>(Collections.singletonMap("url not found", url), HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<Object>(Collections.singletonMap("visit count", count), HttpStatus.OK);
	}
	
	@GetMapping("/getallurl")
	public ResponseEntity<Map<String, Integer>> getAllUrl(){
		Map<String , Integer> all = UrlService.longAndCount;
		for(Map.Entry<String, Integer> list : all.entrySet()) {
			System.out.println("key = "+list.getKey()+"  value = "+list.getValue());
		}
		return new ResponseEntity<>(all,HttpStatus.OK);
	}
}
