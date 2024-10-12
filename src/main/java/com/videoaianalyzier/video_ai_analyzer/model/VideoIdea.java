package com.videoaianalyzier.video_ai_analyzer.model;

public class VideoIdea {
    private String titulo;
    private String descripcion;

    public VideoIdea(String titulo, String descripcion) {
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "Título: " + titulo + "\nDescripción: " + descripcion;
    }
}

