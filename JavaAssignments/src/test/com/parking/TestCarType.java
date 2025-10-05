package com.parking;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestCarType {
    @Test
    public void testCarTypeValues() {
        CarType[] types = CarType.values();
        // check for only two car types
        assertEquals(2, types.length);
        assertEquals(CarType.COMPACT, types[0]);
        assertEquals(CarType.SUV, types[1]);
    }
}
