#!/bin/bash

IPSV="192.168.100.2"
USER="root"
DIR_SERVER="/opt/server-duran/"
WAR_FILE="origamigt-1.0.0.war"
FINAL_WAR_FILE="origamigt.war"
NAME_SERVICE="origamigt-duran"
MSGS="UPDATE SERVER"
CLEAN_BUILD="true"

if [ -z "$1" ] ; then
    echo "Compile true"
else 
	CLEAN_BUILD=$(echo $1 | tr '[:upper:]' '[:lower:]')
	echo "Clean build " $CLEAN_BUILD
fi;


cp src/main/webapp/WEB-INF/applicationContext.xml applicationContext.xml
echo "`DATE` [!! INFO !!] COPY FILE applicationContext.XML"
echo "`DATE` [!! INFO !!] COPY FILE applicationContext.XML" >> test.txt

cat <<EOF > src/main/webapp/WEB-INF/applicationContext.xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans" xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx" xmlns:jee="http://www.springframework.org/schema/jee"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
       http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-3.1.xsd
       http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-3.1.xsd
       http://www.springframework.org/schema/jee http://www.springframework.org/schema/jee/spring-jee-3.1.xsd
       http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-3.1.xsd" >
    <context:annotation-config />
    <bean class="com.origami.sigef.common.config.ApplicationContextUtils"></bean>
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <property name="driverClass" value="org.postgresql.Driver" />
        <property name="user" value="sisapp" />
        <property name="password" value="sisapp98"/>
        <!--<property name="user" value="postgres" />-->
        <!--<property name="password" value="ingsistemas"/>-->
        <!-- Desarrollo -->
        <!--        <property name="jdbcUrl" value="jdbc:postgresql://200.124.240.62:5433/_activiti"/>-->
        <!--        <property name="jdbcUrl" value="jdbc:postgresql://200.124.240.62:5432/activiti_gt"/>-->
        <!--        <property name="jdbcUrl" value="jdbc:postgresql://192.168.100.12:5432/dev_activiti_duran"/>-->
        <!--        <property name="jdbcUrl" value="jdbc:postgresql://192.168.100.12:5432/_activiti"/>-->
        <!--<property name="jdbcUrl" value="jdbc:postgresql://192.168.100.12:5432/activiti_gt"/>-->
        <property name="jdbcUrl" value="jdbc:postgresql://192.168.100.12:5432/activiti_gt"/>
        <!--<property name="jdbcUrl" value="jdbc:postgresql://localhost:5432/activiti_gt"/>-->
        <property name="minPoolSize" value="1" />
        <property name="maxPoolSize" value="5" />
        <property name="maxIdleTime" value="1800" />
    </bean>
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>
    <bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
        <property name="dataSource" ref="dataSource" />
        <property name="transactionManager" ref="transactionManager" />
        <property name="databaseSchemaUpdate" value="true" />
        <property name="asyncExecutorActivate" value="false" />
        <property name="databaseType" value="postgres"/>
    </bean>
    <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean" destroy-method="destroy">
        <property name="processEngineConfiguration" ref="processEngineConfiguration" />
    </bean>
</beans>

EOF

cp src/main/webapp/WEB-INF/sistema.props sistema.props
cp src/main/webapp/WEB-INF/sistema.props.produccion src/main/webapp/WEB-INF/sistema.props


echo "`DATE` [!! INFO !!] COMPILE APPLICATION"
echo "`DATE` [!! INFO !!] COMPILE APPLICATION" >> test.txt

if [ $CLEAN_BUILD = "true" ]; then 
	if mvn clean install -DSkinpTest ; then
		if [ ! -r "target/"$WAR_FILE ] ; then
			echo "`DATE` [!! INFO !!] Could not find file target/${WAR_FILE}" 1>&2
			echo "`DATE` [!! INFO !!] Could not find file target/${WAR_FILE}" >> test.txt
			cp -rf applicationContext.xml src/main/webapp/WEB-INF/applicationContext.xml
			rm -rf applicationContext.xml  
			cp -rf sistema.props src/main/webapp/WEB-INF/sistema.props 
			rm -rf sistema.props
			exit 1
		fi;
		echo "`DATE` [!! INFO !!] "$MSGS" "$USER@$IPSV >> test.txt
		echo "`DATE` [!! INFO !!] "$MSGS" "$USER@$IPSV

		if ! scp "target/"$WAR_FILE $USER@$IPSV:$DIR_SERVER ; then
			echo "`DATE` [!!ERROR!!] Failed to copy war file"
			echo "`DATE` [!!ERROR!!] Failed to copy war file" >> test.txt
		else 
			echo "`DATE` [!! INFO !!] CONNECTION ON SERVER "$IPSV":"$DIR_SERVER
			ssh $USER"@"$IPSV -- "sh -c 'systemctl stop "$NAME_SERVICE" && rm -rf "$DIR_SERVER"webapps/origamigt* && mv "$DIR_SERVER$WAR_FILE" "$DIR_SERVER"webapps/"$FINAL_WAR_FILE" && systemctl start "$NAME_SERVICE"'"
			echo "`DATE` [!! INFO !!] EXECUTE UPDATE APPLICATION ON SERVER "$IPSV":"$DIR_SERVER
			echo "`DATE` [!! INFO !!] EXECUTE UPDATE APPLICATION ON SERVER "$IPSV":"$DIR_SERVER >> test.txt
		fi;
		
		echo "`DATE` [!! INFO !!] RESTORE FILE applicationContext.XML"
		echo "`DATE` [!! INFO !!] RESTORE FILE applicationContext.XML" >> test.txt

	else 
		echo "`DATE` [!!ERROR!!] ERROR IN APPLICATION"
		echo "`DATE` [!!ERROR!!] ERROR IN APPLICATION" >> test.txt
	fi;
else 
	if [ ! -r "target/"$WAR_FILE ] ; then
		echo "`DATE` [!! INFO !!] Could not find file target/${WAR_FILE}" 1>&2
		echo "`DATE` [!! INFO !!] Could not find file target/${WAR_FILE}" >> test.txt
		cp -rf applicationContext.xml src/main/webapp/WEB-INF/applicationContext.xml 
		rm -rf applicationContext.xml 
		cp -rf sistema.props src/main/webapp/WEB-INF/sistema.props 
		rm -rf sistema.props
		exit 1
	fi;
	echo "`DATE` [!! INFO !!] "$MSGS" "$USER@$IPSV >> test.txt
	echo "`DATE` [!! INFO !!] "$MSGS" "$USER@$IPSV

	if ! scp "target/"$WAR_FILE $USER@$IPSV:$DIR_SERVER ; then
		echo "`DATE` [!!ERROR!!] Failed to copy war file"
		echo "`DATE` [!!ERROR!!] Failed to copy war file" >> test.txt
	else 
		echo "`DATE` [!! INFO !!] CONNECTION ON SERVER "$IPSV":"$DIR_SERVER
		ssh $USER"@"$IPSV -- "sh -c 'systemctl stop "$NAME_SERVICE" && rm -rf "$DIR_SERVER"webapps/origamigt* && mv "$DIR_SERVER$WAR_FILE" "$DIR_SERVER"webapps/"$FINAL_WAR_FILE" && systemctl start "$NAME_SERVICE"'"
		echo "`DATE` [!! INFO !!] EXECUTE UPDATE APPLICATION ON SERVER "$IPSV":"$DIR_SERVER
		echo "`DATE` [!! INFO !!] EXECUTE UPDATE APPLICATION ON SERVER "$IPSV":"$DIR_SERVER >> test.txt
	fi;
	
	echo "`DATE` [!! INFO !!] RESTORE FILE applicationContext.XML"
	echo "`DATE` [!! INFO !!] RESTORE FILE applicationContext.XML" >> test.txt
fi;

cp -rf applicationContext.xml src/main/webapp/WEB-INF/applicationContext.xml 
rm -rf applicationContext.xml 
cp -rf sistema.props src/main/webapp/WEB-INF/sistema.props 
rm -rf sistema.props