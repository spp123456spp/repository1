/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ydwf.bridge;

import java.util.HashMap;

/**
 *
 * @author huashizhong
 */
public class PAGE extends HashMap {
    
    public PAGE(long begin,long total)
    {
        this.put("BEGIN", begin);
         this.put("TOTAL", total);
    }
   
}
