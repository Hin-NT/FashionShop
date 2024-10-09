package com.example.FashionShop.service.interfaces;


import com.example.FashionShop.dto.ResponseDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IService<T, A> {

    ResponseEntity<ResponseDTO<List<A>>> getAll();

    ResponseEntity<ResponseDTO<T>> create(T t);

    ResponseEntity<ResponseDTO<A>> findById(T t);

    ResponseEntity<ResponseDTO<T>> update(T t);

    ResponseEntity<ResponseDTO<String>> delete(T t);

}
