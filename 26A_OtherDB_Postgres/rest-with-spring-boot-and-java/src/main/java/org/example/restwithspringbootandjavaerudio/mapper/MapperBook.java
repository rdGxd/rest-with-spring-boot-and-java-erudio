package org.example.restwithspringbootandjavaerudio.mapper;

import org.example.restwithspringbootandjavaerudio.data.vo.v1.BookVO;
import org.example.restwithspringbootandjavaerudio.model.Book;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class MapperBook {

    private static final ModelMapper mapper = new ModelMapper();

    static {
        mapper.createTypeMap(Book.class, BookVO.class).addMapping(Book::getId, BookVO::setKey);
        mapper.createTypeMap(BookVO.class, Book.class).addMapping(BookVO::getKey, Book::setId);
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<>();
        for (O o : origin) {
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }

}
