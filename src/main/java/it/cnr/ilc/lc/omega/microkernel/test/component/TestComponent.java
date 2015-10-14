/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.microkernel.test.component;

import sirius.kernel.di.std.ConfigValue;
import sirius.kernel.di.std.Register;

/**
 *
 * @author angelo
 */
@Register(classes = TestComponent.class)
public class TestComponent {
    
    @ConfigValue("test.baseDir")
    private String dirData;

    public TestComponent() {
        System.err.println("testComponent");
    }
    
    
    
    public void test(){
        System.out.println(this.getClass().getCanonicalName() +" runs");
        System.out.println(getDirData());
    }

    public String getDirData() {
        return dirData;
    }
    
    
}
