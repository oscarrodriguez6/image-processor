package com.count.dto;

import java.util.List;
import java.util.Map;

public class ResultadoDuplicadosDTO {
    private int imagenes;
    private int videos;
    private int otros;
    private Map<String, List<String>> duplicados;

    // Getters y setters
    public int getImagenes() { return imagenes; }
    public void setImagenes(int imagenes) { this.imagenes = imagenes; }

    public int getVideos() { return videos; }
    public void setVideos(int videos) { this.videos = videos; }

    public int getOtros() { return otros; }
    public void setOtros(int otros) { this.otros = otros; }

    public Map<String, List<String>> getDuplicados() { return duplicados; }
    public void setDuplicados(Map<String, List<String>> duplicados) { this.duplicados = duplicados; }
}
