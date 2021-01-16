package com.nodenetwork;

import com.nodenetwork.controllers.AjiraNetProcessController;
import com.nodenetwork.controllers.CustomRequestMapping;
import com.nodenetwork.controllers.handlers.CreateConnectionsRequestHandler;
import com.nodenetwork.controllers.handlers.CreateDevicesCustomRequestHandler;
import com.nodenetwork.controllers.handlers.FetchDevicesCustomRequestHandler;
import com.nodenetwork.controllers.handlers.FetchRoutesCustomRequestHandler;
import com.nodenetwork.controllers.handlers.ModifyDevicesRequestHandler;
import com.nodenetwork.controllers.handlers.SendMessageRequestHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BasicApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext=  SpringApplication.run(BasicApplication.class, args);
        registerControllers(applicationContext);
    }
    
    private static void registerControllers(ApplicationContext applicationContext) {
        AjiraNetProcessController baseController = applicationContext.getBean(AjiraNetProcessController.class);
        baseController.addMapping(new CustomRequestMapping("/connections","CREATE"), applicationContext.getBean(CreateConnectionsRequestHandler.class));
        baseController.addMapping(new CustomRequestMapping("/devices","CREATE"), applicationContext.getBean(CreateDevicesCustomRequestHandler.class));
        baseController.addMapping(new CustomRequestMapping("/devices","FETCH"), applicationContext.getBean(FetchDevicesCustomRequestHandler.class));
        baseController.addMapping(new CustomRequestMapping("/info-routes","FETCH"), applicationContext.getBean(FetchRoutesCustomRequestHandler.class));
        baseController.addMapping(new CustomRequestMapping("/devices","MODIFY"), applicationContext.getBean(ModifyDevicesRequestHandler.class));
        baseController.addMapping(new CustomRequestMapping("/devices","SEND"), applicationContext.getBean(SendMessageRequestHandler.class));       
    }
}
