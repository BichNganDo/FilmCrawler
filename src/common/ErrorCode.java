/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

/**
 *
 * @author Ngan Do
 */
public enum ErrorCode {
    SUCCESS(0), FAIL(-1), CONNECTION_FAIL(-2), NOT_EXIST(-3);

    private int value;

    private ErrorCode(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return this.value;
    }
}
