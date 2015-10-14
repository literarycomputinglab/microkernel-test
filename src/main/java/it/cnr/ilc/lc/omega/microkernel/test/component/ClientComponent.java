/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.cnr.ilc.lc.omega.microkernel.test.component;

import sirius.kernel.di.std.Part;
import sirius.kernel.di.std.Register;

/**
 *
 * @author angelo
 */
@Register(classes = ClientComponent.class)
public class ClientComponent {

    @Part
    TestComponent component;
    
    public void go(){
        component.test();
    }
    
}
