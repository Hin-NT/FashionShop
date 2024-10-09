package com.example.FashionShop.controller;

import com.example.FashionShop.dto.ResponseDTO;
import com.example.FashionShop.dto.SizeDTO;
import com.example.FashionShop.model.Size;
import com.example.FashionShop.service.interfaces.ISize;
import com.example.FashionShop.utils.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sizes")
public class SizeController {

    private final ISize sizeService;

    @GetMapping("")
    public ResponseEntity<ResponseDTO<List<SizeDTO>>> getAllSizes() {
        return sizeService.getAll();
    }

    @GetMapping("/{sizeId}")
    public ResponseEntity<ResponseDTO<SizeDTO>> getSizeById(@PathVariable String sizeId) {
        Size size = new Size();
        size.setSizeId(sizeId);

        return sizeService.findById(size);
    }

    @PostMapping("")
    public ResponseEntity<ResponseDTO<Size>> createSize(@RequestBody Size size) {
        Util.trimFields(size);
        return sizeService.create(size);
    }

    @PutMapping("")
    public ResponseEntity<ResponseDTO<Size>> updateSize(@RequestBody Size size) {
        Util.trimFields(size);
        return sizeService.update(size);
    }

    @DeleteMapping("/{sizeId}")
    public ResponseEntity<ResponseDTO<String>> deleteSize(@PathVariable String sizeId) {
        Size size = new Size();
        size.setSizeId(sizeId);
        return sizeService.delete(size);
    }
}
