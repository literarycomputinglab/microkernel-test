/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.microkernel.test;

import it.cnr.ilc.lc.library.test.Lib;



/**
 *
 * @author angelo
 */
public class KernelTest {
    public static void main(String[] args) {
        String test = "una stringa di prova";
        String res = Lib.cut(1,3, test);
        System.err.println(res);
    }
}
