package com.ysc.ai.openai.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.image.ImageOptions;
import org.springframework.ai.image.ImageOptionsBuilder;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc: ai接口
 * @Author: yinqf【yinqf7437@gmail.com】
 * @Date: 2024-12-24 21:36
 */
@RestController
@RequestMapping("/ai")
public class AiController {
    private static final Logger logger = LoggerFactory.getLogger(AiController.class);

    private final OpenAiChatModel openAiChatModel;

    private final OpenAiImageModel openAiImageModel;

    //自定义了超时时间
    private final OpenAiImageModel openAiImageModelCustom;

    @Autowired
    public AiController(OpenAiChatModel openAiChatModel,OpenAiImageModel openAiImageModel,OpenAiImageModel openAiImageModelCustom) {
        this.openAiChatModel = openAiChatModel;
        this.openAiImageModel = openAiImageModel;
        this.openAiImageModelCustom = openAiImageModelCustom;
    }

    @GetMapping("/chat")
    public String generation(@RequestParam(value = "msg",defaultValue = "你好") String msg) {
        logger.info("msg:{}", msg);
        try {
            return openAiChatModel.call(msg);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return "Error occurred: " + e.getMessage();
        }
    }

    //正常画图
    @GetMapping("/img")
    public String imageGen(@RequestParam(value = "msg", defaultValue = "画一只小猫") String msg,
                           @RequestParam(value = "useCustomModel", required = false, defaultValue = "false") boolean useCustomModel){
        logger.info("msg:{}", msg);

        //useCustomModel 为true时，使用自定义配置
        OpenAiImageModel imageModel = useCustomModel ? openAiImageModelCustom : openAiImageModel;

        ImageOptions options = ImageOptionsBuilder.builder().model("dall-e-3").height(1024).width(1024).build();
        ImagePrompt imagePrompt = new ImagePrompt(msg, options);
        ImageResponse response = imageModel.call(imagePrompt);
        String imageUrl = response.getResult().getOutput().getUrl();
        logger.info("imageUrl:{}",imageUrl);
        return "<html><body><img src=\"" + imageUrl + "\" alt=\"Generated Image\" /></body></html>";
    }
}
