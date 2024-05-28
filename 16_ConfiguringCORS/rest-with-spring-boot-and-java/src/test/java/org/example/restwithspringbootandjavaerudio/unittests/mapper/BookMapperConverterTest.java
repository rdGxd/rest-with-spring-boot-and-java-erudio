package org.example.restwithspringbootandjavaerudio.unittests.mapper;

import org.example.restwithspringbootandjavaerudio.data.vo.v1.BookVO;
import org.example.restwithspringbootandjavaerudio.mapper.MapperBook;
import org.example.restwithspringbootandjavaerudio.model.Book;
import org.example.restwithspringbootandjavaerudio.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BookMapperConverterTest {

    MockBook inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockBook();
    }

    @Test
    public void parseEntityToVOTest() {
        BookVO output = MapperBook.parseObject(inputObject.mockEntity(), BookVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Rodrigo", output.getAuthor());
        assertEquals("Title Test0", output.getTitle());
        assertEquals(0D, output.getPrice());
        assertNotNull(output.getLaunchDate());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<BookVO> outputList = MapperBook.parseListObjects(inputObject.mockEntityList(), BookVO.class);
        BookVO outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getKey());
        assertEquals("Rodrigo", outputZero.getAuthor());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals(0D, outputZero.getPrice());
        assertNotNull(outputZero.getLaunchDate());

        BookVO outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getKey());
        assertEquals("Rafaela", outputSeven.getAuthor());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals(7D, outputSeven.getPrice());
        assertNotNull(outputSeven.getLaunchDate());


        BookVO outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getKey());
        assertEquals("Rodrigo", outputTwelve.getAuthor());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals(12D, outputTwelve.getPrice());
        assertNotNull(outputTwelve.getLaunchDate());
    }

    @Test
    public void parseVOToEntityTest() {
        Book output = MapperBook.parseObject(inputObject.mockVO(), Book.class);
        assertEquals(Long.valueOf(0L), output.getId());
        assertEquals("Rodrigo", output.getAuthor());
        assertEquals("Title Test0", output.getTitle());
        assertEquals(0D, output.getPrice());
        assertNotNull(output.getLaunchDate());
    }

    @Test
    public void parserVOListToEntityListTest() {
        List<Book> outputList = MapperBook.parseListObjects(inputObject.mockVOList(), Book.class);
        Book outputZero = outputList.getFirst();

        assertEquals(Long.valueOf(0L), outputZero.getId());
        assertEquals("Rodrigo", outputZero.getAuthor());
        assertEquals("Title Test0", outputZero.getTitle());
        assertEquals(0D, outputZero.getPrice());
        assertNotNull(outputZero.getLaunchDate());

        Book outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getId());
        assertEquals("Rafaela", outputSeven.getAuthor());
        assertEquals("Title Test7", outputSeven.getTitle());
        assertEquals(7D, outputSeven.getPrice());
        assertNotNull(outputSeven.getLaunchDate());

        Book outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getId());
        assertEquals("Rodrigo", outputTwelve.getAuthor());
        assertEquals("Title Test12", outputTwelve.getTitle());
        assertEquals(12D, outputTwelve.getPrice());
        assertNotNull(outputTwelve.getLaunchDate());
    }
}
