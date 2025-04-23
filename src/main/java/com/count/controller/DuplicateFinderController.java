package com.count.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.count.dto.ResultadoDuplicadosDTO;
import com.count.service.DuplicateFinderService;


@RestController
@RequestMapping("/api")
public class DuplicateFinderController {

    @Autowired
    private DuplicateFinderService duplicateFinderService;

    @GetMapping("/duplicados")
    public ResultadoDuplicadosDTO buscarDuplicados(@RequestParam String ruta, @RequestParam String subcarpetaExcluida) {
        return duplicateFinderService.buscarDuplicados(ruta, subcarpetaExcluida);
    }
    @GetMapping("/contador")
    public ResultadoDuplicadosDTO contarArchivos(@RequestParam String ruta, @RequestParam String subcarpetaExcluida) {
        return duplicateFinderService.contarArchivos(ruta, subcarpetaExcluida);
    }
}
