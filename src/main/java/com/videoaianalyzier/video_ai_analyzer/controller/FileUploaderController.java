package com.videoaianalyzier.video_ai_analyzer.controller;

import com.google.cloud.speech.v1.SpeechClient;
import com.videoaianalyzier.video_ai_analyzer.model.VideoIdea;
import com.videoaianalyzier.video_ai_analyzer.service.AIAnalizerService;
import com.videoaianalyzier.video_ai_analyzer.service.TranscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("")
@Controller
public class FileUploaderController {

    private TranscriberService transcriberService;

    private AIAnalizerService aiAnalizerService;

    @Autowired
    public FileUploaderController(TranscriberService transcriberService, AIAnalizerService aiAnalyzerService) {
        this.transcriberService = transcriberService;
        this.aiAnalizerService = aiAnalyzerService;
    }

    @GetMapping("/home")
    public String viewHome() {
        return "index";
    }

    @PostMapping("/upload")
    public String uploadVideo(@RequestParam("videoFile") MultipartFile file,
                              @RequestParam(value = "context", required = false) String context,
                              Model model) {

        String transcribedAudio = this.transcriberService.transcribeAudioFromVideo(file);

        if (transcribedAudio == null) {
            model.addAttribute("errorMessage",
                    "Error procesando el video. Intenta un archivo de menor tamaño.");
            return "index :: response";
        }

        List<VideoIdea> videoIdeas = this.aiAnalizerService.generateTitleAndDescription(transcribedAudio, context);

        if(videoIdeas == null) {
            model.addAttribute("errorMessage",
                    "Error generando las recomendaciones. Inténtalo de nuevo");
            return "index :: response";
        }

        System.out.println(videoIdeas);

        model.addAttribute("errorMessage", null);
        model.addAttribute("transcript", transcribedAudio);
        model.addAttribute("titles", videoIdeas);
        model.addAttribute("description", videoIdeas.isEmpty() ? "" : videoIdeas.get(0).getDescripcion());

        return "response :: generatedResults";
    }



}
