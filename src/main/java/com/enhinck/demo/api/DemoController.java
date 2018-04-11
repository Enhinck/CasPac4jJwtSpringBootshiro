package com.enhinck.demo.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.enhinck.demo.entity.App;
import com.enhinck.demo.entity.Demo;
import com.enhinck.demo.repository.AppRepository;
import com.enhinck.demo.repository.DemoRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;


@Controller
@RequestMapping("view")
@Slf4j
@Api(value = "用户接口", description = "")
public class DemoController extends BaseAPI {

	@Autowired
	DemoRepository demoRepository;

	@GetMapping("hello")
	@ApiOperation(value="测试",notes="测试")
	public String getFarm(Model model) {
		model.addAttribute("user", "");
		
		App app = new App();
		app.setApiKey("111");
		//app.setId(new ObjectId("0x11"));
		appRepository.insert(app);
		
		App temp = appRepository.findOneByApiKey("111");
		
		log.info(temp.toString());
		
		return "hello";
	}
	
	@Autowired
	AppRepository appRepository;


	@GetMapping("demo/{demoId}")
	public String getArea(Model model, @PathVariable("demoId") Integer demoId) {
		
		
		
		if (demoId <= 0) {
			return "404";
		}

		Demo demo = demoRepository.findOne(demoId);
		if (demo == null) {
			return "404";
		}

		model.addAttribute("demo", demo);
		return "demo";
	}
	

	

}
