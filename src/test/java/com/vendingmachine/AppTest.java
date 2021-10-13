package com.vendingmachine;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }
    public static void main(String[] args) throws IOException, ParseException{
        VendingMachine vm = new VendingMachine();
        
    }
}
