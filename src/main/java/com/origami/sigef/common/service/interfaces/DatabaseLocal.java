/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.service.interfaces;

import java.sql.Connection;
import javax.ejb.Local;
import javax.sql.DataSource;

/**
 *
 * @author carlosloorvargas
 */
@Local
public interface DatabaseLocal {

    public DataSource getDataSource(String base);

    public Connection getConnection(String base);

}
