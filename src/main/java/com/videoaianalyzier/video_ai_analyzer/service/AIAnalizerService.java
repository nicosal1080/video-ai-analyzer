package com.videoaianalyzier.video_ai_analyzer.service;

import com.videoaianalyzier.video_ai_analyzer.model.VideoIdea;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class AIAnalizerService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(TranscriberService.class);


    @Autowired
    public AIAnalizerService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<VideoIdea> generateTitleAndDescription(String transcription, String context) {

        List<VideoIdea> videoIdeas = new ArrayList<>();

        try {
            JSONObject jsonObject = createRequestJsonObject(transcription, context);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(openAiApiKey);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "https://api.openai.com/v1/completions",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() != HttpStatus.OK) {
                logger.error(response.getBody());
                throw new RuntimeException("Error en la comunicación con OpenAI: " + response.getStatusCode());
            }

            JSONObject jsonResponse = new JSONObject(response.getBody());
            processJsonResponse(jsonResponse, videoIdeas);

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error al generar título y descripción: " + e.getMessage());
        }

        return videoIdeas;

    }

    private static JSONObject createRequestJsonObject(String transcription, String context) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("model", "gpt-3.5-turbo-instruct");
        jsonObject.put("prompt", "Genera 3 ideas de títulos atractivos para un video de YouTube y una sola " +
                "breve descripción para el siguiente fragmento: " + transcription +". " +
                "Asegúrate de que los títulos sean concisos (énfasis en esto), reflejen el contenido, sean " +
                "intrigantes, y directos para atraer a la audiencia. Además, ten en cuenta el contexto " +
                "del creador del video: " + context + ". Formato de salida: " +
                "Título 1, Título 2, Título 3, Descripción única (un párrafo únicamente.");

        jsonObject.put("max_tokens", 500);
        return jsonObject;
    }

    private void processJsonResponse(JSONObject jsonResponse, List<VideoIdea> videoIdeas) {
        String generatedText = jsonResponse.getJSONArray("choices").getJSONObject(0).
                getString("text").trim();
        System.out.println(generatedText);

        String[] ideas = generatedText.split("\n");

        for (String idea : ideas) {
            String[] parts = idea.split(" - ");
            if (parts.length == 2) {
                String titulo = parts[0].trim();
                String descripcion = parts[1].trim();
                videoIdeas.add(new VideoIdea(titulo, descripcion));
            }
        }
    }

}
