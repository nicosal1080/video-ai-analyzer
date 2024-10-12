package com.videoaianalyzier.video_ai_analyzer.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class TranscriberService {

    private static final Logger logger = LoggerFactory.getLogger(TranscriberService.class);

    public String transcribeAudioFromVideo(MultipartFile videoFile) {
        try {
            String audioFilePath = extractAudioFromVideo(videoFile);
            return transcribeAudio(audioFilePath);

        } catch (IOException | IllegalArgumentException | InterruptedException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    private String extractAudioFromVideo(MultipartFile videoFile) throws IOException, InterruptedException {

        File tempVideoFile = File.createTempFile("tempVideo", ".mp4");
        videoFile.transferTo(tempVideoFile);

        File tempAudioFile = new File(tempVideoFile.getAbsolutePath().replace(".mp4", ".wav"));
        double audioFileSizeInMB = getAudioFileSizeInMB(tempVideoFile, tempAudioFile);

        if (audioFileSizeInMB > 10) {
            throw new IllegalArgumentException("El tamaño del audio supera el límite de 10MB");
        }

        System.out.printf("El tamaño del archivo de audio extraído es: %.2f MB%n", audioFileSizeInMB);
        return tempAudioFile.getAbsolutePath();
    }

    private static double getAudioFileSizeInMB(File tempVideoFile, File tempAudioFile)
            throws IOException, InterruptedException {

        ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i",
                tempVideoFile.getAbsolutePath(), "-vn", "-acodec", "pcm_s16le", "-ar", "16000",
                "-ac", "1", tempAudioFile.getAbsolutePath());

        Process process = processBuilder.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            throw new InterruptedException();
        }

        long audioFileSizeInBytes = tempAudioFile.length();
        return audioFileSizeInBytes / (1024.0 * 1024.0);
    }

    private static String transcribeAudio(String audioFilePath) throws IOException {
        try (SpeechClient speechClient = SpeechClient.create()) {

            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("es-ES")
                    .build();

            ByteString audioBytes = ByteString.readFrom(new FileInputStream(audioFilePath));
            RecognitionAudio audio = RecognitionAudio.newBuilder().setContent(audioBytes).build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            StringBuilder transcription = new StringBuilder();

            for (SpeechRecognitionResult result : response.getResultsList()) {
                transcription.append(result.getAlternativesList().get(0).getTranscript());
            }

            return transcription.toString();

        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new IOException("Error transcribiendo el audio");
        }
    }


}
