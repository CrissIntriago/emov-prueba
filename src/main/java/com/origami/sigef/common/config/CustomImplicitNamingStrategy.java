/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.common.config;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

/**
 *
 * @author ANGEL NAVARRO
 */
public class CustomImplicitNamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        Identifier userProvidedIdentifier = source.getUserProvidedIdentifier();
        source.getBuildingContext().getBuildingOptions().getSchemaCharset();
        String fk = source.getTableName() + "_" + source.getColumnNames().get(0) + "_fkey";
        Identifier toIdentifier = toIdentifier(fk, source.getBuildingContext());
        return userProvidedIdentifier != null ? userProvidedIdentifier : toIdentifier;
    }

}
